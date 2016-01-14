package com.hitherejoe.leanbackcards;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

public class LoadingCardView extends BaseCardView {

    private ProgressBar mProgressBar;

    public LoadingCardView(Context context) {
        this(context, null);
    }

    public LoadingCardView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v17.leanback.R.attr.imageCardViewStyle);
    }

    public LoadingCardView(Context context, int styleResId) {
        super(new ContextThemeWrapper(context, styleResId), null, 0);
        buildLoadingCardView(styleResId);
    }

    public LoadingCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getStyledContext(context, attrs, defStyleAttr), attrs, defStyleAttr);
        buildLoadingCardView(getImageCardViewStyle(context, attrs, defStyleAttr));
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    private void buildLoadingCardView(int styleResId) {
        setFocusable(false);
        setFocusableInTouchMode(false);
        setCardType(CARD_TYPE_MAIN_ONLY);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.view_loading_card, this);
        TypedArray cardAttrs =
                getContext().obtainStyledAttributes(
                        styleResId, android.support.v17.leanback.R.styleable.lbImageCardView);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_indicator);

        cardAttrs.recycle();
    }

    public void setCardBackgroundColor(int colorResource) {
        setBackgroundColor(ContextCompat.getColor(getContext(), colorResource));
    }

    public void isLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private static Context getStyledContext(Context context, AttributeSet attrs, int defStyleAttr) {
        int style = getImageCardViewStyle(context, attrs, defStyleAttr);
        return new ContextThemeWrapper(context, style);
    }

    private static int getImageCardViewStyle(Context context,
                                             AttributeSet attrs,
                                             int defStyleAttr) {
        int style = null == attrs ? 0 : attrs.getStyleAttribute();
        if (0 == style) {
            TypedArray styledAttrs =
                    context.obtainStyledAttributes(
                            android.support.v17.leanback.R.styleable.LeanbackTheme);
            style = styledAttrs.getResourceId(
                    android.support.v17.leanback.R.styleable.LeanbackTheme_imageCardViewStyle, 0);
            styledAttrs.recycle();
        }
        return style;
    }


}