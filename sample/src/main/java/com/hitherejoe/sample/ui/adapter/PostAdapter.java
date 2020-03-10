package com.hitherejoe.sample.ui.adapter;

import android.content.Context;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.hitherejoe.sample.ui.data.model.Post;
import com.hitherejoe.sample.ui.presenter.LiveCardPresenter;

public class PostAdapter extends ArrayObjectAdapter {

    private LiveCardPresenter mOptionsItemPresenter;

    public PostAdapter(Context context) {
        mOptionsItemPresenter = new LiveCardPresenter(context);
        setPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                return mOptionsItemPresenter;
            }
        });
    }

    public void addOption(Post post) {
        add(post);
    }

}