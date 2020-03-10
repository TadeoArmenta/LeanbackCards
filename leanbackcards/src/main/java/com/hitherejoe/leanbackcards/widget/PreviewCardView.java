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

import okhttp3.Interceptor;

public class PreviewCardView extends FrameLayout {

    private FrameLayout mMainContainer;
    private LoopingVideoView mVideoView;
    private ImageView mImageView;
    private View mOverlayView;
    private ProgressBar mProgressCard;
    private String mVideoUrl;
    private Context mContext;

    public PreviewCardView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PreviewCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public PreviewCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public PreviewCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
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

    public void setVideoViewSize(int width, int height) {
        mVideoView.setLayoutParams(new LayoutParams(width, height));
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public void setLoading(Interceptor i) {
        mOverlayView.setVisibility(View.VISIBLE);
        mProgressCard.setVisibility(View.VISIBLE);
        mVideoView.setVisibility(View.VISIBLE);

        mVideoView.setupMediaPlayer(mVideoUrl,i ,new LoopingVideoView.OnVideoReadyListener() {
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