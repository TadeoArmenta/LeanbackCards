package com.hitherejoe.sample.ui.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;

import com.hitherejoe.leanbackcards.LoadingCardView;
import com.hitherejoe.leanbackcards.TagCardView;
import com.hitherejoe.sample.R;
import com.hitherejoe.sample.ui.adapter.CardAdapter;
import com.hitherejoe.sample.ui.adapter.OptionsAdapter;
import com.hitherejoe.sample.ui.adapter.PostAdapter;
import com.hitherejoe.sample.ui.data.model.IconItem;
import com.hitherejoe.sample.ui.data.model.Post;
import com.hitherejoe.sample.ui.presenter.HeaderItemPresenter;

public class MainFragment extends BrowseSupportFragment {

    private ArrayObjectAdapter mRowsAdapter;

    private boolean mIsStopping;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(mRowsAdapter);

        setupUIElements();
        addCardRows();
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsStopping = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsStopping = true;
    }

    public boolean isStopping() {
        return mIsStopping;
    }

    private void setupUIElements() {
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.primary));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.accent));
        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new HeaderItemPresenter();
            }
        });
    }

    private void addCardRows() {
        CardAdapter mLoadingCardAdapter = new CardAdapter(getActivity());
        mLoadingCardAdapter.add(new LoadingCardView(getActivity(), R.style.LoadingCardStyle));
        HeaderItem gridLoadingCardHeader =
                new HeaderItem(mRowsAdapter.size(), getString(R.string.header_text_loading_card));
        mRowsAdapter.add(new ListRow(gridLoadingCardHeader, mLoadingCardAdapter));

        OptionsAdapter mIconCardAdapter = new OptionsAdapter(getActivity());
        IconItem iconItem = new IconItem();
        iconItem.title = "auto-loop";
        iconItem.value = "enabled";
        iconItem.iconResource = R.drawable.ic_loop;
        mIconCardAdapter.addOption(iconItem);
        HeaderItem gridIconCardHeader =
                new HeaderItem(mRowsAdapter.size(), getString(R.string.header_text_icon_card));
        mRowsAdapter.add(new ListRow(gridIconCardHeader, mIconCardAdapter));

        CardAdapter mTagCardAdapter = new CardAdapter(getActivity());
        TagCardView tagCardView = new TagCardView(getActivity());
        tagCardView.setCardText("hitherejoe");
        mTagCardAdapter.add(tagCardView);
        HeaderItem gridTagCardHeader =
                new HeaderItem(mRowsAdapter.size(), getString(R.string.header_text_tag_card));
        mRowsAdapter.add(new ListRow(gridTagCardHeader, mTagCardAdapter));

        PostAdapter mVideoCardAdapter = new PostAdapter(getActivity());
        Post post = new Post();
        post.username = "hitherejoe";
        post.description = "Just a video!";
        post.thumbnail = R.drawable.hitherejoe;
        post.videoUrl =
                "https://live.bongotv.mx/live/fubo.amc/playlist.m3u8";
        mVideoCardAdapter.addOption(post);
        HeaderItem gridLiveCardHeader =
                new HeaderItem(mRowsAdapter.size(), getString(R.string.header_text_live_card));
        mRowsAdapter.add(new ListRow(gridLiveCardHeader, mVideoCardAdapter));
    }

}