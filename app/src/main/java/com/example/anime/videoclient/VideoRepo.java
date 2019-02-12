package com.example.anime.videoclient;

import com.example.anime.mvp.videolist.VideoContractor;

public interface VideoRepo {
   void getVideosList(VideoContractor.OnResponseCallback callback);
}
