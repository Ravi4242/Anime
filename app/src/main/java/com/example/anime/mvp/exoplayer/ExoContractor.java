package com.example.anime.mvp.exoplayer;

import android.database.sqlite.SQLiteDatabase;

import com.google.android.exoplayer2.ExoPlayer;

public interface ExoContractor {
    interface View{
        void startExoPlayer(ExoPlayer exoPlayer);
    }
    interface Presenter{
        void buildPlayer();
        void dropView();
    }
}
