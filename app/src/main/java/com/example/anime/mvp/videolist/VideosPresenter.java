package com.example.anime.mvp.videolist;

import com.example.anime.model.Video;
import com.example.anime.videoclient.VideoClient;

import java.util.List;

public class VideosPresenter implements VideoContractor.Presenter {

    private VideoContractor.View view;
    private VideoClient videoClient;

    public  VideosPresenter(VideoContractor.View view, VideoClient videoClient){
        this.view = view;
        this.videoClient = videoClient;
    }

    private final VideoContractor.OnResponseCallback callback = new VideoContractor.OnResponseCallback() {
        @Override
        public void onSuccess(List<Video> videos) {
             view.showVideoList(videos);
             view.hideProgress();
        }

        @Override
        public void onFailure(String msg) {
             view.hideProgress();
        }
    };

    @Override
    public void loadVideoList() {
        view.showProgress();
        videoClient.getVideosList(callback);
    }
    @Override
    public void dropView() {
      view = null;
    }
}
