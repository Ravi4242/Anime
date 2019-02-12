package com.example.anime.mvp.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class ExoPresenter implements ExoContractor.Presenter {
    private ExoContractor.View view;
    private ExoPlayer player;
    private String uri;

    public ExoPresenter(ExoContractor.View view,ExoPlayer player,String uri){
        this.view = view;
        this.player = player;
        this.uri = uri;
    }

    @Override
    public void buildPlayer() {
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer");
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(uri),dataSourceFactory,extractorsFactory,null,null);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        view.startExoPlayer(player);
    }

    @Override
    public void dropView() {
        view = null;
    }
}
