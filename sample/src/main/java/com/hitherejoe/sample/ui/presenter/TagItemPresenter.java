package com.hitherejoe.sample.ui.presenter;

import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.Presenter;

import com.hitherejoe.leanbackcards.TagCardView;
import com.hitherejoe.sample.R;

public class TagItemPresenter extends Presenter {

    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        sDefaultBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.primary);
        sSelectedBackgroundColor = ContextCompat.getColor(parent.getContext(), R.color.primary_dark);

        TagCardView cardView = new TagCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    private static void updateCardBackgroundColor(TagCardView view, boolean selected) {
        view.setBackgroundColor(selected ? sSelectedBackgroundColor : sDefaultBackgroundColor);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        if (item instanceof TagCardView) {
            TagCardView cardView = (TagCardView) viewHolder.view;
            cardView.setCardText(((TagCardView) item).getCardText());
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

}