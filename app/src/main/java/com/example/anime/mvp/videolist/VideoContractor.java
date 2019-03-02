package com.example.anime.mvp.videolist;


import com.example.anime.model.Video;

import java.util.ArrayList;


public interface VideoContractor {
    interface View {
        void showVideoList(ArrayList<Video> videos, int position);
        void showProgress();
        void hideProgress();
    }

    interface Presenter {
        void loadVideoList(int position);
        void dropView();

    }
    interface OnResponseCallback {
        void onSuccess(ArrayList<Video> videos, int position);
        void onFailure(String msg);
    }
}
