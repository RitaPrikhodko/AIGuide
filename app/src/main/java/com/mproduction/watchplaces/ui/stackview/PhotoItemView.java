package com.mproduction.watchplaces.ui.stackview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.data.PhotoItem;
import com.mproduction.watchplaces.utils.Utils;

public class PhotoItemView extends LinearLayout {
    private Context mContext;

    public PhotoItemView(Context context) {
        super(context);
        mContext = context;
    }

    public PhotoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public PhotoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void bindView(PhotoItem item) {
        ImageView thumb = findViewById(R.id.card_image);
        TextView title = findViewById(R.id.card_title);

        Utils.setImageUri(mContext, thumb, item.thumbUri, R.drawable.grocery_1);
        title.setText(item.title);
    }
}
