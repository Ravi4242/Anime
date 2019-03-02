package com.example.anime.videoclient;

import com.example.anime.Rest.ApiClient;
import com.example.anime.Rest.ApiInterface;
import com.example.anime.model.Video;
import com.example.anime.mvp.videolist.VideoContractor;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoClient implements VideoRepo {

    @Override
    public void getVideosList(final VideoContractor.OnResponseCallback callback, final int position) {
        ApiInterface apiInterface = ApiClient.getClient();
        apiInterface.getVideosList("pretty").enqueue(new Callback<ArrayList<Video>>() {
            @Override
            public void onResponse(Call<ArrayList<Video>> call, Response<ArrayList<Video>> response) {
                callback.onSuccess(response.body(),position);
            }

            @Override
            public void onFailure(Call<ArrayList<Video>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
}
