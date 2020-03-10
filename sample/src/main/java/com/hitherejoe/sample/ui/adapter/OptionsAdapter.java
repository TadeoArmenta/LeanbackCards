package com.hitherejoe.sample.ui.adapter;

import android.content.Context;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.hitherejoe.sample.ui.data.model.IconItem;
import com.hitherejoe.sample.ui.presenter.IconItemPresenter;

public class OptionsAdapter extends ArrayObjectAdapter {

    private IconItemPresenter mOptionsItemPresenter;

    public OptionsAdapter(Context context) {
        mOptionsItemPresenter = new IconItemPresenter();
        setPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                return mOptionsItemPresenter;
            }
        });
    }

    public void addOption(IconItem option) {
        add(option);
    }

}