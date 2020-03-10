package com.hitherejoe.leanbackcards.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hitherejoe.leanbackcards.R;

import java.util.Map;

public class PreviewCardView extends FrameLayout {

    private FrameLayout mMainContainer;
    private LoopingVideoView mVideoView;
    private ImageView mImageView;
    private View mOverlayView;
    private ProgressBar mProgressCard;
    private String mVideoUrl;
    private Map<String,String> mHeaders;

    public PreviewCardView(Context context) {
        super(context);
        init();
    }

    public PreviewCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PreviewCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PreviewCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_preview_card, this);
        mMainContainer = view.findViewById(R.id.main_container);
        mVideoView = view.findViewById(R.id.main_video);
        mImageView =  view.findViewById(R.id.main_image);
        mOverlayView = view.findViewById(R.id.view_overlay);
        mProgressCard = view.findViewById(R.id.progress_card);
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }
    public void setVideoUrl(String videoUrl, Map<String,String> headers) {
        mVideoUrl = videoUrl;
        mHeaders = headers;
    }

    public void setVideoViewSize(int width, int height) {
        mVideoView.setLayoutParams(new LayoutParams(width, height));
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void setLoading() {
        mOverlayView.setVisibility(View.VISIBLE);
        mProgressCard.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);
        if (mHeaders == null) {
            mVideoView.setupMediaPlayer(mVideoUrl, new LoopingVideoView.OnVideoReadyListener() {
                @Override
                public void onVideoReady() {
                    mOverlayView.setVisibility(View.INVISIBLE);
                    mProgressCard.setVisibility(View.INVISIBLE);
                    mImageView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onVideoError() {
                    setNotPlayingViewState();
                }
            });
        }else{
            mVideoView.setupMediaPlayer(mVideoUrl,mHeaders, new LoopingVideoView.OnVideoReadyListener() {
                @Override
                public void onVideoReady() {
                    mOverlayView.setVisibility(View.INVISIBLE);
                    mProgressCard.setVisibility(View.INVISIBLE);
                    mImageView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onVideoError() {
                    setNotPlayingViewState();
                }
            });
        }
    }

    public void setFinished() {
        mVideoView.stopMediaPlayer();
        setNotPlayingViewState();
    }

    private void setNotPlayingViewState() {
        mImageView.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.INVISIBLE);
        mOverlayView.setVisibility(View.INVISIBLE);
        mProgressCard.setVisibility(View.INVISIBLE);
    }

}