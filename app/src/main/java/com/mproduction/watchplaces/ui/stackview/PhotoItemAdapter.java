package com.mproduction.watchplaces.ui.stackview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.data.PhotoItem;

import java.util.ArrayList;

public class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PhotoItem> data = new ArrayList<>();

    @NonNull
    @Override
    public PhotoItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item_view, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoItemAdapter.ViewHolder holder, int position) {
        PhotoItem record = data.get(position);
        holder.itemView.bindView(record);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        PhotoItemView itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = (PhotoItemView) itemView;
        }
    }

    public void changeData(ArrayList<PhotoItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
