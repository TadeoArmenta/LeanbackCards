package com.hitherejoe.sample.ui.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.hitherejoe.leanbackcards.LiveCardView;
import com.hitherejoe.sample.R;
import com.hitherejoe.sample.ui.activity.MainActivity;
import com.hitherejoe.sample.ui.data.model.Post;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class LiveCardPresenter extends Presenter {

    private static final int CARD_WIDTH = 210;
    private static final int CARD_HEIGHT = 300;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;
    private Context mContext;

    public LiveCardPresenter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        final Context context = parent.getContext();
        sDefaultBackgroundColor = ContextCompat.getColor(context, R.color.primary);
        sSelectedBackgroundColor = ContextCompat.getColor(context, R.color.primary_dark);
        mDefaultCardImage = ContextCompat.getDrawable(context, R.drawable.ic_play);

        final LiveCardView cardView = new LiveCardView(mContext,null) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setOnClickListener(v -> cardView.stopVideo());

        cardView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                cardView.startVideo();
                ScaleAnimation anim = new ScaleAnimation(
                        1.0f,
                        2.5f,
                        1.0f,
                        1.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        1.0f);
                anim.setDuration(135);
                anim.setFillAfter(true);
                v.startAnimation(anim);
            } else {
                if (mContext instanceof MainActivity) {
                    if (((MainActivity) mContext).isFragmentActive()) {
                        cardView.stopVideo();
                        ScaleAnimation anim = new ScaleAnimation(
                                2.5f,
                                1.0f,
                                1.0f,
                                1.0f,
                                Animation.RELATIVE_TO_SELF,
                                0.0f,
                                Animation.RELATIVE_TO_SELF,
                                1.0f);
                        anim.setDuration(135);
                        anim.setFillAfter(true);
                        v.startAnimation(anim);
                    }
                } else {
                    cardView.stopVideo();
                    ScaleAnimation anim = new ScaleAnimation(
                            2.5f,
                            1.0f,
                            1.0f,
                            1.0f,
                            Animation.RELATIVE_TO_SELF,
                            0.0f,
                            Animation.RELATIVE_TO_SELF,
                            1.0f);
                    anim.setDuration(135);
                    anim.setFillAfter(true);
                    v.startAnimation(anim);
                }
            }
        });

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    private static void updateCardBackgroundColor(LiveCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    @SuppressLint("HardwareIds")
    public void onBindViewHolder(final Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof Post) {
            Post post = (Post) item;

            final LiveCardView cardView = (LiveCardView) viewHolder.view;
            cardView.setTitleText(post.description);
            cardView.setContentText(post.username);
            cardView.setMainContainerDimensions(CARD_WIDTH, CARD_HEIGHT);
            int size = (int) (CARD_HEIGHT * 1.25);
            cardView.setVideoViewSize(size, size);
            Interceptor i = chain -> {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("User-Agent", getUserAgent(viewHolder.view.getContext(),"elbrazodealbanil("+Settings.Secure.getString(viewHolder.view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID)+")"))
                        .header("deviceid", Settings.Secure.getString(viewHolder.view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID))
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            };
            cardView.setVideoUrl(post.videoUrl,i);

            Glide.with(cardView.getContext())
                    .load(post.thumbnail)
                    .centerCrop()
                    .error(mDefaultCardImage)
                    .into(cardView.getMainImageView());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) { }
    public static String getUserAgent(Context context, String applicationName) {
        String versionName;
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "?";
        }
        return applicationName + "/2.4.0-RC (Linux;Android " + Build.VERSION.RELEASE
                + ") ";
    }
}