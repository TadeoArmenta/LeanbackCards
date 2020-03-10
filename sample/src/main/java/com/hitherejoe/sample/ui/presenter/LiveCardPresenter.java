package com.hitherejoe.sample.ui.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.hitherejoe.leanbackcards.LiveCardView;
import com.hitherejoe.sample.R;
import com.hitherejoe.sample.ui.activity.MainActivity;
import com.hitherejoe.sample.ui.data.model.Post;

import java.util.HashMap;
import java.util.Map;

public class LiveCardPresenter extends Presenter {

    private static final int CARD_WIDTH = 300;
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

        final LiveCardView cardView = new LiveCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardView.stopVideo();
            }
        });

        cardView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cardView.startVideo();
                } else {
                    if (mContext instanceof MainActivity) {
                        if (((MainActivity) mContext).isFragmentActive()) {
                            cardView.stopVideo();
                        }
                    } else {
                        cardView.stopVideo();
                    }
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
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if (item instanceof Post) {
            Post post = (Post) item;

            final LiveCardView cardView = (LiveCardView) viewHolder.view;
            cardView.setTitleText(post.description);
            cardView.setContentText(post.username);
            cardView.setMainContainerDimensions(CARD_WIDTH, CARD_HEIGHT);
            int size = (int) (CARD_WIDTH * 1.25);
            cardView.setVideoViewSize(size, size);
            Map<String, String> map = new HashMap<String, String>();
            map.put("deviceid", Settings.Secure.getString(viewHolder.view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
            map.put("User-Agent",getUserAgent(viewHolder.view.getContext(),"elbrazodealbanil("+Settings.Secure.getString(viewHolder.view.getContext().getContentResolver(), Settings.Secure.ANDROID_ID)+")"));
            cardView.setVideoUrl(post.videoUrl,map);

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
        return applicationName + "/3.0.0-RC (Linux;Android " + Build.VERSION.RELEASE
                + ") ";
    }
}