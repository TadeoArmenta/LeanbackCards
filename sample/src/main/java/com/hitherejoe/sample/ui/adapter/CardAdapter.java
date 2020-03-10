package com.hitherejoe.sample.ui.adapter;

import android.content.Context;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.hitherejoe.leanbackcards.IconCardView;
import com.hitherejoe.leanbackcards.LoadingCardView;
import com.hitherejoe.leanbackcards.TagCardView;
import com.hitherejoe.sample.ui.data.model.Post;
import com.hitherejoe.sample.ui.presenter.IconItemPresenter;
import com.hitherejoe.sample.ui.presenter.LiveCardPresenter;
import com.hitherejoe.sample.ui.presenter.LoadingPresenter;
import com.hitherejoe.sample.ui.presenter.TagItemPresenter;


public class CardAdapter extends ArrayObjectAdapter {

    private LoadingPresenter mLoadingPresenter;
    private IconItemPresenter mIconItemPresenter;
    private TagItemPresenter mTagItemPresenter;
    private LiveCardPresenter mLiveCardPresenter;

    public CardAdapter(Context context) {
        mLoadingPresenter = new LoadingPresenter();
        mIconItemPresenter = new IconItemPresenter();
        mTagItemPresenter = new TagItemPresenter();
        mLiveCardPresenter = new LiveCardPresenter(context);
        setPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                if (item instanceof LoadingCardView) {
                    return mLoadingPresenter;
                } else if (item instanceof IconCardView) {
                    return mIconItemPresenter;
                } else if (item instanceof TagCardView) {
                    return mTagItemPresenter;
                } else if (item instanceof Post) {
                    return mLiveCardPresenter;
                }
                return null;
            }
        });
    }

}