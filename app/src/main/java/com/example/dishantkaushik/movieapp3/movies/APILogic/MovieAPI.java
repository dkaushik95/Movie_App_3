package com.example.dishantkaushik.movieapp3.movies.APILogic;

/**
 * Created by dishantkaushik on 20/04/16.
 */
import com.example.dishantkaushik.movieapp3.movies.Model.Reviews.movieReview;
import com.example.dishantkaushik.movieapp3.movies.Model.Trailers.movieTrailer;
import com.example.dishantkaushik.movieapp3.movies.Model.movie;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface MovieAPI {

    //this method is to fetch the ALL movies with specific sort
    @GET("/3/discover/movie")
    void fetchMovies(
            @Query("sort_by") String mSort,
            @Query("api_key") String mApiKey,
            @Query("language") String lang,
            Callback<movie> cb
    );

    @GET("/3/movie/{id}/reviews")
    void fetchReview(
            @Query("api_key") String mApiKey,
            @Path("id") String id,
            Callback<movieReview> cb
    );

    @GET("/3/movie/{id}/videos")
    void fetchVideos(
            @Query("api_key") String mApiKey,
            @Path("id") String id,
            Callback<movieTrailer> cb
    );

}