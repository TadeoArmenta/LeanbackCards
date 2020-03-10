package com.hitherejoe.leanbackcards.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.util.Map;

public class LoopingVideoView extends VideoView {

    private MediaPlayer mMediaPlayer;

    public LoopingVideoView(Context context) {
        super(context);
    }

    public LoopingVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopingVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoopingVideoView(Context context,
                            AttributeSet attrs,
                            int defStyleAttr,
                            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setupMediaPlayer(String url, final OnVideoReadyListener onVideoReadyListener) {
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer = mp;
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0, 0);
                mMediaPlayer.start();
                onVideoReadyListener.onVideoReady();
            }
        });
        setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onVideoReadyListener.onVideoError();
                return false;
            }
        });
        setVideoURI(Uri.parse(url));
    }
    public void setupMediaPlayer(String url, Map<String,String> headers, final OnVideoReadyListener onVideoReadyListener) {
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer = mp;
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0, 0);
                mMediaPlayer.start();
                onVideoReadyListener.onVideoReady();
            }
        });
        setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch(extra){
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        // Do Something
                        // eg. reset the media player and restart
                        break;
                    case MediaPlayer.MEDIA_ERROR_IO:
                        // Do Something
                        // eg. Show dialog to user indicating bad connectivity
                        // or attempt to restart the player
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        //Do Something
                        //eg. Show dialog that there was error in connecting to the server
                        // or attempt some retries
                        Log.d("MediaError","TimeOut");
                        break;
                }
                //You must always return true if you want the error listener to work
                return true;
            }
        });
        // Start the MediaController
        MediaController mediacontroller = new MediaController(getContext());
        mediacontroller.setAnchorView(this);
        setMediaController(mediacontroller);
        // Get the URL from String videoUrl
        setVideoURI(Uri.parse(url),headers);
    }
    public void stopMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }

    public interface OnVideoReadyListener {
        void onVideoReady();
        void onVideoError();
    }

}