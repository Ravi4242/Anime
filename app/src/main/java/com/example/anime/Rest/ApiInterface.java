package com.example.anime.Rest;

import com.example.anime.model.Video;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("/media.json")
    Call<ArrayList<Video>> getVideosList(@Query("print") String print);
}
