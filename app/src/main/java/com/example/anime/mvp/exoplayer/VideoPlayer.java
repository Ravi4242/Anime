package com.example.anime.mvp.exoplayer;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.anime.R;
import com.example.anime.adapter.RecyclerAdapter;
import com.example.anime.model.Video;
import com.example.anime.mvp.videolist.VideoContractor;
import com.example.anime.mvp.videolist.VideosPresenter;
import com.example.anime.sqlite.DBManager;
import com.example.anime.utils.Utils;
import com.example.anime.videoclient.VideoClient;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class VideoPlayer extends AppCompatActivity implements VideoContractor.View, SwipeRefreshLayout.OnRefreshListener {
    ExoPlayer exoPlayer;
    PlayerView simpleExoPlayerView;
    Bundle args;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    VideosPresenter videosPresenter;
    TextView tv_title, tv_description, tv_related;
    DBManager dbManager;
    List<String> titles;
    Utils utils;
    ArrayList<Video> videoList;
    Collection<MediaSource> mediaSources;
    ConcatenatingMediaSource concatenatingMediaSource;
    int position, refreshAdapterOnAutoTrackChange = 0;
    ImageButton previous, next;
    boolean itemFoundInDB = false, isPlaybackControllerClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        initViews();
        initializePlayer();
        setPlaybackControllers();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
        titles = new ArrayList<>();
        titles = dbManager.fetchAllTitles();
        if (titles.size() > 0) {
            for (String titleFromDB : titles) {
                if (videoList.get(position).getTitle().equalsIgnoreCase(titleFromDB)) {
                    dbManager.update(videoList.get(position).getTitle(), exoPlayer.getCurrentPosition());
                    itemFoundInDB = true;
                    return;
                }
            }
            if (!itemFoundInDB)
                dbManager.insert(videoList.get(position).getTitle(), exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
        } else {
            dbManager.insert(videoList.get(position).getTitle(), exoPlayer.getCurrentPosition(), exoPlayer.getDuration());
        }
    }

    private void initViews() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        args = getIntent().getExtras();
        assert args != null;
        videoList = args.getParcelableArrayList("videos");
        position = args.getInt("position");
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        simpleExoPlayerView = findViewById(R.id.simple_video_player);
        videosPresenter = new VideosPresenter(this, new VideoClient());
        recyclerView = findViewById(R.id.recycler_view);
        tv_title = findViewById(R.id.title);
        tv_description = findViewById(R.id.description);
        tv_related = findViewById(R.id.related);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        dbManager = new DBManager(this);
        layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new RecyclerAdapter(this);
        previous = findViewById(R.id.exo_prev);
        next = findViewById(R.id.exo_next);
        concatenatingMediaSource = new ConcatenatingMediaSource();
        utils = new Utils(this);
        simpleExoPlayerView.setNextFocusRightId(R.id.exo_nextt);
        simpleExoPlayerView.setNextFocusLeftId(R.id.exo_previous);
        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }
            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                if (isPlaybackControllerClicked) {
                    isPlaybackControllerClicked = false;
                    videosPresenter.loadVideoList(position);
                } else if (refreshAdapterOnAutoTrackChange != 0) {
                    videosPresenter.loadVideoList(position = position + 1);
                } else videosPresenter.loadVideoList(position);

                refreshAdapterOnAutoTrackChange++;
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }

    private void initializePlayer() {
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        simpleExoPlayerView.setPlayer(exoPlayer);
        mediaSources = new ArrayList<>();
        for (Video video : videoList) {
            mediaSources.add(new ExtractorMediaSource.Factory(dataSourceFactory).setExtractorsFactory(extractorsFactory).createMediaSource(Uri.parse(video.getUrl())));
        }
        setMediaSource(position);
        exoPlayer.prepare(concatenatingMediaSource, false, false);
        exoPlayer.setPlayWhenReady(true);
    }

    private void setMediaSource(int position) {
        concatenatingMediaSource.addMediaSources(mediaSources);
        while (position > 0) {
            concatenatingMediaSource.removeMediaSource(position - 1);
            position--;
        }
        setPlayer(concatenatingMediaSource);
    }

    private void setPlayer(ConcatenatingMediaSource concatenatingMediaSource) {
        try {
            long resumeTime = dbManager.fetchVideoCurrentDuration(videoList.get(position).getTitle());
            long totalDuration = dbManager.fetchVideoTotalDuration(videoList.get(position).getTitle());
            if (utils.setDurationToZero(resumeTime, totalDuration)) exoPlayer.seekTo(0);
            else exoPlayer.seekTo(resumeTime);

        } catch (Exception e) {
            e.printStackTrace();
        }
        exoPlayer.prepare(concatenatingMediaSource, false, false);
        exoPlayer.setPlayWhenReady(true);
    }

    private void setPlaybackControllers() {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaybackControllerClicked = true;
                concatenatingMediaSource.clear();
                if (position != 0) {
                    setMediaSource(position = position - 1);
                } else {
                    setMediaSource(position);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaybackControllerClicked = true;
                refreshAdapterOnAutoTrackChange = 0;
                concatenatingMediaSource.clear();
                setMediaSource(position = position + 1);
            }
        });
    }

    @Override
    public void showVideoList(ArrayList<Video> videos, int position) {
        tv_title.setText(videos.get(position).getTitle());
        tv_description.setText(videos.get(position).getDescription());
        tv_related.setText(getString(R.string.related));
        if (!videos.isEmpty()) {
            adapter.setItems(videos, videos.get(position).getTitle());
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
        videosPresenter.loadVideoList(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        exoPlayer.release();
    }

}
