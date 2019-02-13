package com.example.anime.mvp.videolist;

import android.database.sqlite.SQLiteDatabase;

import com.example.anime.model.Video;

import java.util.List;

public interface VideoContractor {
    interface View {
        void showVideoList(List<Video> videos);
        void showProgress();
        void hideProgress();
    }

    interface Presenter {
        void loadVideoList();
        void dropView();

    }
    interface OnResponseCallback {
        void onSuccess(List<Video> videos);
        void onFailure(String msg);
    }
}
