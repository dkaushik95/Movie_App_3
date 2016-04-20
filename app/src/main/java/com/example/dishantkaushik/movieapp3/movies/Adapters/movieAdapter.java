package com.example.dishantkaushik.movieapp3.movies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dishantkaushik.movieapp3.R;
import com.example.dishantkaushik.movieapp3.movies.Model.movieModel;
import com.example.dishantkaushik.movieapp3.movies.MovieDetailActivity;
import com.example.dishantkaushik.movieapp3.movies.MovieDetailFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dishantkaushik on 20/04/16.
 */
public class movieAdapter extends RecyclerView.Adapter<movieHolder> {
    private List<movieModel> mMovieGeneralModal;
    private Context context;
    private boolean mTwoPane;
    private FragmentManager fm;

    public movieAdapter(Context context, List<movieModel> itemList, boolean mTwoPane, FragmentManager fm) {
        this.mMovieGeneralModal = itemList;
        this.context = context;
        this.mTwoPane = mTwoPane;
        this.fm = fm;
    }

    @Override
    public movieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_cards, null);
        movieHolder rcv = new movieHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(movieHolder holder, final int position) {
        holder.movieName.setText(mMovieGeneralModal.get(position).getTitle());
        holder.movieAvg.setText(mMovieGeneralModal.get(position).getmVote());
        //picasso loading here
        Picasso.with(context)
                .load(mMovieGeneralModal.get(position).getThumbnail())
                .into(holder.moviePhoto);
        if (position == 0 && mTwoPane) {
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setMovieData(mMovieGeneralModal.get(0));
            fragment.setArgument(fm);
            fm
                    .beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setMovieData(mMovieGeneralModal.get(position));
                    fragment.setArgument(fm);
                    fm
                            .beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra("DATA_MOVIE", mMovieGeneralModal.get(position));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mMovieGeneralModal.size();
    }
}