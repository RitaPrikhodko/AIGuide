package com.mproduction.watchplaces.ui.stackview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.data.TrackCardRecord;

public class TrackCardsItem extends LinearLayout {
    public TrackCardsItem(Context context) {
        super(context);
    }

    public TrackCardsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrackCardsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindView(TrackCardRecord item) {
        ImageView thumb = findViewById(R.id.card_image);
        TextView title = findViewById(R.id.card_title);

        thumb.setImageResource(item.resId);
        title.setText(item.title);
    }
}
