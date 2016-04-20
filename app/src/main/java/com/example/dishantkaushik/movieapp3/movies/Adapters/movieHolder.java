package com.example.dishantkaushik.movieapp3.movies.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dishantkaushik.movieapp3.R;

/**
 * Created by dishantkaushik on 20/04/16.
 */
public class movieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView movieName, movieAvg;
    public ImageView moviePhoto;
    public View mView;

    public movieHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mView = itemView;
        movieName = (TextView) itemView.findViewById(R.id.movieName);
        movieAvg = (TextView) itemView.findViewById(R.id.vote);
        moviePhoto = (ImageView) itemView.findViewById(R.id.moviePhoto);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}