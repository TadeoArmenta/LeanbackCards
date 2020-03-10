package com.hitherejoe.sample.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hitherejoe.sample.R;
import com.hitherejoe.sample.ui.fragment.MainFragment;

public class MainActivity extends FragmentActivity {

    FrameLayout mFragmentContainer;

    private Fragment mBrowseFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentContainer = findViewById(R.id.frame_container);

        mBrowseFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(mFragmentContainer.getId(), mBrowseFragment).commit();
    }

    public boolean isFragmentActive() {
        return mBrowseFragment instanceof MainFragment &&
                mBrowseFragment.isAdded() &&
                !mBrowseFragment.isDetached() &&
                !mBrowseFragment.isRemoving() &&
                !((MainFragment) mBrowseFragment).isStopping();
    }

}