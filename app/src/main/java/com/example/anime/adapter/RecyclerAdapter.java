package com.example.anime.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.anime.mvp.exoplayer.VideoPlayer;
import com.example.anime.model.Video;
import com.example.anime.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VideosViewHolder> {

    private Context context;
    private ArrayList<Video> videoListwithSkipItem = new ArrayList<>();
    private ArrayList<Video> totalVideoList = new ArrayList<>();
    private int skipItemPosition = -1;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Video> videos, String videoTitle) {
        totalVideoList.clear();
        totalVideoList = videos;
        videoListwithSkipItem.clear();
        for (int i = 0; i < videos.size(); i++) {
            if (!(videos.get(i).getTitle().equalsIgnoreCase(videoTitle))) {
                videoListwithSkipItem.add(videos.get(i));
            } else {
                skipItemPosition = i;
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, final int position) {
        Picasso.with(holder.itemView.getContext()).load(videoListwithSkipItem.get(position).getThumb()).into(holder.iv_thumbnail);
        holder.tv_title.setText(videoListwithSkipItem.get(position).getTitle());
        holder.tv_description.setText(videoListwithSkipItem.get(position).getDescription());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoPlayer = new Intent(context, VideoPlayer.class);
                if ((skipItemPosition != -1) && (position >= skipItemPosition))
                    videoPlayer.putExtra("position", position + 1);
                else
                    videoPlayer.putExtra("position", position);
                videoPlayer.putParcelableArrayListExtra("videos", totalVideoList);
                context.startActivity(videoPlayer);
                if ((context) instanceof VideoPlayer) {
                    ((Activity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoListwithSkipItem.size();
    }


    class VideosViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumbnail;
        TextView tv_title;
        TextView tv_description;
        RelativeLayout relativeLayout;
        CardView cardView;

        VideosViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            tv_title = itemView.findViewById(R.id.video_title);
            tv_description = itemView.findViewById(R.id.video_description);
            relativeLayout = itemView.findViewById(R.id.video_card_relative_layout);
            cardView = itemView.findViewById(R.id.video_card_view);
        }
    }
}
