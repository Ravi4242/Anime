package com.example.anime.Rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static ApiInterface getRestApiService;

    public static ApiInterface getClient(){

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if(getRestApiService == null){
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    /*This is request Type*/
                    Request request = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .header("token","")
                            .build();

                    /*This is Response type*/
                    Response response = chain.proceed(request);
                    String rawJson = Objects.requireNonNull(response.body()).string();


                    return response.newBuilder().body(ResponseBody.create(Objects.requireNonNull(response.body()).contentType(),rawJson)).build();
                }
            });
            httpClient.readTimeout(20, TimeUnit.SECONDS);
            httpClient.connectTimeout(30, TimeUnit.SECONDS);


//            OkHttpClient client = httpClient.build();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://interview-e18de.firebaseio.com")
                    .client(httpClient.addInterceptor(logging).build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            getRestApiService = retrofit.create(ApiInterface.class);

        }



        return getRestApiService;
    }
}
