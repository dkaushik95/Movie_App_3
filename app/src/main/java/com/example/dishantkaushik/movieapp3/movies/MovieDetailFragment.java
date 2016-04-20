package com.example.dishantkaushik.movieapp3.movies;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dishantkaushik.movieapp3.R;
import com.example.dishantkaushik.movieapp3.movies.APIKEY.apiKeyManager;
import com.example.dishantkaushik.movieapp3.movies.APILogic.MovieAPI;
import com.example.dishantkaushik.movieapp3.movies.APILogic.NetworkAPI;
import com.example.dishantkaushik.movieapp3.movies.DB.favDBHelper;
import com.example.dishantkaushik.movieapp3.movies.Model.Reviews.movieReview;
import com.example.dishantkaushik.movieapp3.movies.Model.Reviews.reviewData;
import com.example.dishantkaushik.movieapp3.movies.Model.Trailers.movieTrailer;
import com.example.dishantkaushik.movieapp3.movies.Model.movieModel;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private FragmentManager fm;
    private movieModel movieModel;
    private TextView reviewText;
    private String shareYoutubeID;

    public void movieDetailFragment() {

    }

    public void setArgument(FragmentManager fm) {
        this.fm = fm;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        if (savedInstanceState != null) {
            this.movieModel = (movieModel) savedInstanceState.getSerializable("DATA");
        }
        updateGeneralUI(rootView);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("DATA", movieModel);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setMovieData(movieModel moviegeneralModal) {
        this.movieModel = moviegeneralModal;
    }

    private void updateGeneralUI(View v) {
        TextView titleText = (TextView) v.findViewById(R.id.titleText);
        TextView voteText = (TextView) v.findViewById(R.id.rating);
        TextView calendarText = (TextView) v.findViewById(R.id.calendar);
        TextView peoplesText = (TextView) v.findViewById(R.id.people);
        ImageView titleImage = (ImageView) v.findViewById(R.id.titleimg);
        TextView plotSynopsis = (TextView) v.findViewById(R.id.plotsynopsis);
        reviewText = (TextView) v.findViewById(R.id.reviewText);
        LinearLayout youtubeViewHolder = (LinearLayout) v.findViewById(R.id.youtubelayout);
        TextView shareYoutube = (TextView) v.findViewById(R.id.youtubesharer);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        titleText.setText(movieModel.getTitle());
        voteText.setText(movieModel.getmVote());
        peoplesText.setText(movieModel.getmPeople());
        calendarText.setText(movieModel.getmReleaseDate());
        plotSynopsis.setText(movieModel.getmOverview());
        getMovieReview(reviewText);
        Picasso.with(getContext())
                .load(movieModel.getThumbnail())
                .into(titleImage);
        getMovieReview(reviewText);
        getTrailer(youtubeViewHolder);
        shareYoutube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareYoutubeID != null) {
                    shareYoutubeIntent(shareYoutubeID);
                } else {
                    Toast.makeText(getContext(), "No Youtube Videos Available! Sorry", Toast.LENGTH_LONG).show();
                }
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase();
            }
        });
    }

    protected void saveToDatabase() {
        favDBHelper db = new favDBHelper(getContext());
        if (!reviewText.getText().toString().contains("Sorry")) {
            movieModel.setmReview(reviewText.getText().toString());
        }
        boolean b = db.insertMovie(movieModel);
        if (b)
            Toast.makeText(getContext(), "Added to Favourites", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getContext(), "Seems Already in Favourites!", Toast.LENGTH_LONG).show();
    }
    protected void shareYoutubeIntent(String shareYoutubeID) {
        String url = "http://www.youtube.com/watch?v" + shareYoutubeID;
        String shareMsg = "hey,there new film named " + movieModel.getTitle() + " has been released and here is the Trailer link,Have a look at it " + url;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Movies Now - Android App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareMsg);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
    }

    protected String generateYoutubeThumbnailURL(String id) {
        String url = "http://img.youtube.com/vi/" + id + "/mqdefault.jpg";
        return url;
    }

    public void watchYoutubeVideo(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

    protected void getTrailer(final LinearLayout youtubeViewHolder) {
        MovieAPI mMovieAPI = NetworkAPI.createService(MovieAPI.class);
        mMovieAPI.fetchVideos(apiKeyManager.ACCESS_TOKEN, this.movieModel.getmId(), new Callback<movieTrailer>() {

            @Override
            public void success(movieTrailer movieYoutubeModal, Response response) {
                youtubeViewHolder.setPadding(5, 10, 5, 0);
                com.example.dishantkaushik.movieapp3.movies.Model.Trailers.trailerData[] trailer = movieYoutubeModal.getResults();
                if (trailer.length > 0) {
                    shareYoutubeID = trailer[0].getKey();
                    for (final com.example.dishantkaushik.movieapp3.movies.Model.Trailers.trailerData obj : trailer) {
                        String url = generateYoutubeThumbnailURL(obj.getKey());
                        ImageView myImage = new ImageView(getContext());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                180,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.leftMargin = 3;
                        params.rightMargin = 3;
                        params.topMargin = 6;
                        params.bottomMargin = 3;
                        myImage.setLayoutParams(params);
                        Picasso.with(getContext())
                                .load(url)
                                .into(myImage);
                        youtubeViewHolder.addView(myImage);
                        myImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                watchYoutubeVideo(obj.getKey());
                            }
                        });

                    }

                } else {
                    youtubeViewHolder.setPadding(50, 50, 50, 50);
                    TextView errmsg = new TextView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            30
                    );
                    errmsg.setLayoutParams(params);
                    errmsg.setText("That's Bad Luck,No Trailers Found!Check later");
                    youtubeViewHolder.addView(errmsg);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                youtubeViewHolder.setPadding(50, 50, 50, 50);
                TextView errmsg = new TextView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        30
                );
                errmsg.setLayoutParams(params);
                errmsg.setText("Network Error! You can't view Trailers Rite Now");
                youtubeViewHolder.addView(errmsg);

            }
        });
    }

    protected void getMovieReview(final View review) {
        MovieAPI mMovieAPI = NetworkAPI.createService(MovieAPI.class);
        mMovieAPI.fetchReview(apiKeyManager.ACCESS_TOKEN, this.movieModel.getmId(), new Callback<movieReview>() {

            @Override
            public void success(movieReview movieReview, Response response) {
                reviewData[] movieResult = movieReview.getResults();
                if (movieResult.length > 0)
                    ((TextView) review).setText(movieResult[0].getContent());
                else
                    ((TextView) review).setText("Sorry No Review is Available Till Now!");

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("error", error.toString());
                ((TextView) review).setText("Sorry! Check Back Latter! Network Error!");
            }
        });
    }

    protected void generateThumbnail() {

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
