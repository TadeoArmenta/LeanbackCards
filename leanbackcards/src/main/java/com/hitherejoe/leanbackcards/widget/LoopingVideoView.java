package com.hitherejoe.leanbackcards.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Util;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class LoopingVideoView extends PlayerView {

    private static final String TAG = "LoopingVideoView";
    private SimpleExoPlayer mMediaPlayer;

    public LoopingVideoView(Context context) {
        super(context);
    }

    public LoopingVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopingVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setupMediaPlayer(String url, Interceptor interceptor, final OnVideoReadyListener onVideoReadyListener) {
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory();
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        mMediaPlayer =  ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        MediaSource videoSource;
        DataSource.Factory dsf = () -> new OkHttpDataSourceFactory(getClient(interceptor), null, null, null)
                .createDataSource();
        int type = Util.inferContentType(Uri.parse(url));
        switch (type) {
            case C.TYPE_HLS:
                videoSource = new HlsMediaSource.Factory(dsf).createMediaSource(Uri.parse(url));
                break;
            case C.TYPE_DASH:
                videoSource = new DashMediaSource.Factory(dsf).createMediaSource(Uri.parse(url));
                break;
            case C.TYPE_OTHER:
                videoSource = new ProgressiveMediaSource.Factory(dsf).createMediaSource(Uri.parse(url));
                break;
            case C.TYPE_SS:
            default:
                videoSource = new ExtractorMediaSource.Factory(dsf).createMediaSource(Uri.parse(url));
        }

        setUseController(false);
        requestFocus();
        setPlayer(mMediaPlayer);

        // Prepare the player with the source.
        mMediaPlayer.prepare(videoSource);
        mMediaPlayer.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged... ");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);
                if (playWhenReady && playbackState == 3){
                    onVideoReadyListener.onVideoReady();

                }
                if (playbackState == Player.STATE_ENDED){onVideoReadyListener.onVideoError();}
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                mMediaPlayer.stop();
                mMediaPlayer.prepare(videoSource);
                mMediaPlayer.setPlayWhenReady(true);
                onVideoReadyListener.onVideoError();
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        mMediaPlayer.setPlayWhenReady(true); //run file/link when ready to play.
    }
    public void stopMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }
    private OkHttpClient getClient(Interceptor i){
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        ConnectionSpec spec =  new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .allEnabledTlsVersions()
                .allEnabledCipherSuites()
                .build();
        OkHttpClient.Builder videoClientBuilder = new OkHttpClient().newBuilder();
        if (i != null){videoClientBuilder.addInterceptor(i);}
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC);
        videoClientBuilder.addInterceptor(logger);
        return videoClientBuilder
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .connectionSpecs(Collections.singletonList(spec))
                .build();
    }
    public interface OnVideoReadyListener {
        void onVideoReady();
        void onVideoError();
    }

}