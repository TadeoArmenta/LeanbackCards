package com.hitherejoe.sample.ui.presenter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.RowHeaderPresenter;

import com.hitherejoe.sample.R;

public class HeaderItemPresenter extends RowHeaderPresenter {

    private float mUnselectedAlpha;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mUnselectedAlpha = viewGroup.getResources()
                .getFraction(R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        View view = inflater.inflate(R.layout.item_icon_header, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {
        HeaderItem headerItem = ((ListRow) o).getHeaderItem();
        View rootView = viewHolder.view;
        rootView.setAlpha(mUnselectedAlpha);
        TextView label = rootView.findViewById(R.id.header_label);
        label.setText(headerItem.getName());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        // no op
    }

    // TODO: TEMP - remove me when leanback onCreateViewHolder no longer sets the mUnselectAlpha,AND
    // also assumes the xml inflation will return a RowHeaderView
    @Override
    protected void onSelectLevelChanged(RowHeaderPresenter.ViewHolder holder) {
        // this is a temporary fix
        holder.view.setAlpha(mUnselectedAlpha + holder.getSelectLevel() *
                (1.0f - mUnselectedAlpha));
    }

}