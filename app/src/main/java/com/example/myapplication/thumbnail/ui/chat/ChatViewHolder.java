package com.example.myapplication.thumbnail.ui.chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.thumbnail.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public TextView titleView;
    public TextView detailView;
    public ImageView thumbnail;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        titleView = (TextView) itemView.findViewById(R.id.title);
        detailView = (TextView) itemView.findViewById(R.id.detail);
        thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
    }
}
