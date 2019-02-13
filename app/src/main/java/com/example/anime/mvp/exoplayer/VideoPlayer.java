package com.example.anime.mvp.exoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.anime.R;
import com.example.anime.adapter.RecyclerAdapter;
import com.example.anime.model.Video;
import com.example.anime.mvp.videolist.VideoContractor;
import com.example.anime.mvp.videolist.VideosPresenter;
import com.example.anime.sqlite.DBManager;
import com.example.anime.videoclient.VideoClient;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import java.util.List;

public class VideoPlayer extends AppCompatActivity implements VideoContractor.View,SwipeRefreshLayout.OnRefreshListener{
    ExoPlayer exoPlayer;
    SimpleExoPlayerView simpleExoPlayerView;
    Bundle args;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    VideosPresenter videosPresenter;
    TextView tv_title, tv_description, tv_related;
    int position;
    String title, description;
    DBManager dbManager;
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
        recyclerView = findViewById(R.id.recycler_view);
        tv_title = findViewById(R.id.title);
        tv_description = findViewById(R.id.description);
        tv_related = findViewById(R.id.related);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        dbManager = new DBManager(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        simpleExoPlayerView.setPlayer(exoPlayer);
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(uri),dataSourceFactory,extractorsFactory,null,null);
        try{
            dbManager.opens();
            long resumeTime = dbManager.fetch(title);
            exoPlayer.seekTo(resumeTime);
        }catch (Exception e){
            e.printStackTrace();
        }
        exoPlayer.prepare(mediaSource,false,false);
        exoPlayer.setPlayWhenReady(true);
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
        dbManager.opens();
        dbManager.insert(title,exoPlayer.getDuration());
        exoPlayer.release();
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
