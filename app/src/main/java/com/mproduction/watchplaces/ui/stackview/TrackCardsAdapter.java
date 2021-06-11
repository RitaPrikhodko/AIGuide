package com.mproduction.watchplaces.ui.stackview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.data.TrackCardRecord;

import java.util.ArrayList;

public class TrackCardsAdapter extends RecyclerView.Adapter<TrackCardsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<TrackCardRecord> data = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackCardsAdapter.ViewHolder holder, int position) {
        TrackCardRecord record = data.get(position);
        holder.itemView.bindView(record);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TrackCardsItem itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = (TrackCardsItem) itemView;
        }
    }

    public void changeData(ArrayList<TrackCardRecord> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
