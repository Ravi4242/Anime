package com.example.anime.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.anime.VideoPlayer;
import com.example.anime.model.Video;
import com.example.anime.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VideosViewHolder> {

    private Context context;
    private List<Video> videoList = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Video> videos, int removeItemFromList) {
        videoList.clear();
        videoList.addAll(videos);
        if (removeItemFromList != -1)
            videoList.remove(removeItemFromList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_row, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, final int position) {
        Picasso.with(holder.itemView.getContext()).load(videoList.get(position).getThumb()).into(holder.iv_thumbnail);
        holder.tv_title.setText(videoList.get(position).getTitle());
        holder.tv_description.setText(videoList.get(position).getDescription());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoPlayer = new Intent(context, VideoPlayer.class);
                videoPlayer.putExtra("position", position);
                videoPlayer.putExtra("VideoURL", videoList.get(position).getUrl());
                videoPlayer.putExtra("title", videoList.get(position).getTitle());
                videoPlayer.putExtra("description", videoList.get(position).getDescription());
                context.startActivity(videoPlayer);
                if(context instanceof VideoPlayer){
                    ((Activity)context).finish();
                };
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


    class VideosViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_title;
        TextView tv_description;
        RelativeLayout relativeLayout;

        VideosViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            tv_title = itemView.findViewById(R.id.video_title);
            tv_description = itemView.findViewById(R.id.video_description);
            relativeLayout = itemView.findViewById(R.id.video_card);
        }
    }
}
