package com.example.anime.videoclient;

import android.util.Log;

import com.example.anime.Rest.ApiClient;
import com.example.anime.Rest.ApiInterface;
import com.example.anime.model.Video;
import com.example.anime.mvp.videolist.VideoContractor;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoClient implements VideoRepo {

    @Override
    public void getVideosList(final VideoContractor.OnResponseCallback callback) {
        ApiInterface apiInterface = ApiClient.getClient();
        apiInterface.getVideosList("pretty").enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
