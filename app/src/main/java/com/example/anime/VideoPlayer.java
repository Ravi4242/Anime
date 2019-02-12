package com.example.anime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.anime.adapter.RecyclerAdapter;
import com.example.anime.model.Video;
import com.example.anime.mvp.exoplayer.ExoContractor;
import com.example.anime.mvp.exoplayer.ExoPresenter;
import com.example.anime.mvp.videolist.VideoContractor;
import com.example.anime.mvp.videolist.VideosPresenter;
import com.example.anime.videoclient.VideoClient;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import java.util.List;

public class VideoPlayer extends AppCompatActivity implements ExoContractor.View, VideoContractor.View,SwipeRefreshLayout.OnRefreshListener{
    SimpleExoPlayer exoPlayer;
    SimpleExoPlayerView simpleExoPlayerView;
    Bundle args;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ExoPresenter exoPresenter;
    VideosPresenter videosPresenter;
    TextView tv_title, tv_description, tv_related;
    int position;
    String title, description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        args = getIntent().getExtras();
        assert args != null;
        String uri = args.getString("VideoURL");
        title = args.getString("title");
        description = args.getString("description");
        position = args.getInt("position");
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector());
        simpleExoPlayerView = findViewById(R.id.simple_video_player);
        videosPresenter = new VideosPresenter(this,new VideoClient());
        exoPresenter = new ExoPresenter(this,exoPlayer,uri);
        recyclerView = findViewById(R.id.recycler_view);
        tv_title = findViewById(R.id.title);
        tv_description = findViewById(R.id.description);
        tv_related = findViewById(R.id.related);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        exoPresenter.buildPlayer();
        videosPresenter.loadVideoList();
        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.release();
    }

    @Override
    public void startExoPlayer(ExoPlayer exoPlayer) {
        simpleExoPlayerView.setPlayer(exoPlayer);
    }

    @Override
    public void showVideoList(List<Video> videos) {
        tv_title.setText(title);
        tv_description.setText(description);
        tv_related.setText("Related");
        if(!videos.isEmpty()){
            adapter.setItems(videos,position);
        }
    }

    @Override
    public void showProgress() {
       swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
       swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        videosPresenter.loadVideoList();
    }
}
