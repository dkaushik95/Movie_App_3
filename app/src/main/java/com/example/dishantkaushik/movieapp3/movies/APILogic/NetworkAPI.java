package com.example.dishantkaushik.movieapp3.movies.APILogic;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by dishantkaushik on 20/04/16.
 */
public class NetworkAPI {
    public static final String API_BASE_URL = "http://api.themoviedb.org";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
    }
}