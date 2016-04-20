package com.example.dishantkaushik.movieapp3.movies;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dishantkaushik.movieapp3.R;
import com.example.dishantkaushik.movieapp3.movies.APIKEY.apiKeyManager;
import com.example.dishantkaushik.movieapp3.movies.APILogic.MovieAPI;
import com.example.dishantkaushik.movieapp3.movies.APILogic.NetworkAPI;
import com.example.dishantkaushik.movieapp3.movies.Adapters.movieAdapter;
import com.example.dishantkaushik.movieapp3.movies.DB.favDBHelper;
import com.example.dishantkaushik.movieapp3.movies.Model.movie;
import com.example.dishantkaushik.movieapp3.movies.Model.movieData;
import com.example.dishantkaushik.movieapp3.movies.Model.movieModel;
import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;


public class MovieListActivity extends AppCompatActivity {
    final CharSequence[] items = {" Most Popular ", " Highest Rated ", " My Favourites "};
    private final String MOST_POPULAR = "popularity.desc";
    private final String HIGHLY_RATED = "vote_count.desc";
    View recyclerView;
    private AlertDialog choice;
    private String FLAG_CURRENT = MOST_POPULAR;
    private String FLAG_FAV = "FAVOURITE";
    private TextView errorTextView;
    private ImageView errorImageview;


    private boolean mTwoPane;
    private movie mMoviegeneralData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);


        recyclerView = findViewById(R.id.movie_list);
        errorImageview = (ImageView) findViewById(R.id.errimg);
        errorTextView = (TextView) findViewById(R.id.errtext);

        assert recyclerView != null;

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }
        if (savedInstanceState == null)
            FetchMovie((RecyclerView) recyclerView, FLAG_CURRENT);
        else {
            if (savedInstanceState.getString("CURRENT") == FLAG_FAV) {
                FetchMovie((RecyclerView) recyclerView, FLAG_FAV);
            } else if (savedInstanceState.getSerializable("adapter") != null) {
                mMoviegeneralData = (movie) savedInstanceState.getSerializable("adapter");
                drawLayout((RecyclerView) recyclerView, mMoviegeneralData);
            } else {
                FetchMovie((RecyclerView) recyclerView, FLAG_CURRENT);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapMenu:
                showChoices();
                break;
        }
        return true;
    }

    private void showChoices() {

        choice = new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                FetchMovie((RecyclerView) recyclerView, MOST_POPULAR);
                                break;
                            case 1:
                                FetchMovie((RecyclerView) recyclerView, HIGHLY_RATED);
                                break;
                            case 2:
                                FetchMovie((RecyclerView) recyclerView, FLAG_FAV);
                                break;
                        }
                        choice.dismiss();
                    }
                }).setTitle("Choose")
                .show();
    }

    protected void FetchFavourites(@NonNull final RecyclerView recyclerView) {
        favDBHelper db = new favDBHelper(getApplicationContext());
        List<movieModel> movieGeneralModals = db.getAllMovies();
        if (movieGeneralModals.size() > 0)
            attachAdapter(recyclerView, movieGeneralModals);
        else {
            Toast.makeText(getApplicationContext(), "It seems No Favourites! check back Later", Toast.LENGTH_LONG).show();
        }
    }

    protected void getPaneChanges() {
        mTwoPane = findViewById(R.id.movie_detail_container) != null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        getPaneChanges();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("adapter", mMoviegeneralData);
        outState.putString("CURRENT", FLAG_CURRENT);

    }

    private void attachAdapter(@NonNull final RecyclerView recyclerView, List<movieModel> movieGeneralModals) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int number;
        if (!mTwoPane) {
            number = 3;
        } else {
            number = 3;
        }
        GridLayoutManager lLayout = new GridLayoutManager(getApplicationContext(), number);
        RecyclerView rView = recyclerView;
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        FragmentManager fm = getSupportFragmentManager();
        movieAdapter mMovieGeneralAdapter = new movieAdapter(getApplicationContext(), movieGeneralModals, mTwoPane, fm);
        rView.setAdapter(mMovieGeneralAdapter);

    }

    private void drawLayout(@NonNull final RecyclerView recyclerView, movie mMoviegeneral) {
        List<movieModel> movieGeneralModals = new ArrayList<movieModel>();
        movieData[] mResult = mMoviegeneral.getResults();
        for (movieData result : mResult) {
            movieModel obj = new movieModel(result.getTitle(), result.getPoster_path(), result.getVote_average(), result.getId(), result.getVote_count(), result.getRelease_date(), result.getOverview());
            movieGeneralModals.add(obj);
        }
        if (mResult.length > 0) {
            attachAdapter(recyclerView, movieGeneralModals);
        } else {
            errorImageview.setVisibility(View.VISIBLE);
            errorTextView.setVisibility(View.VISIBLE);
        }
    }

    private void FetchMovie(@NonNull final RecyclerView recyclerView, String temp) {

        errorImageview.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        errorTextView.setText("Sorry!Network Error! check back Later");

        FLAG_CURRENT = temp;
        if (FLAG_CURRENT != FLAG_FAV) {
            MovieAPI mMovieAPI = NetworkAPI.createService(MovieAPI.class);
            mMovieAPI.fetchMovies(FLAG_CURRENT, apiKeyManager.ACCESS_TOKEN, "en", new Callback<movie>() {
                @Override
                public void success(movie mMoviegeneral, retrofit.client.Response response) {
                    mMoviegeneralData = mMoviegeneral;
                    drawLayout(recyclerView, mMoviegeneral);
                }

                @Override
                public void failure(RetrofitError error) {
                    errorImageview.setVisibility(View.VISIBLE);
                    errorTextView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            FetchFavourites(recyclerView);
        }
    }


}
