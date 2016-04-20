package com.example.dishantkaushik.movieapp3.movies.Model.Trailers;

/**
 * Created by dishantkaushik on 20/04/16.
 */
public class movieTrailer {

    private String id;

    private trailerData[] results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public trailerData[] getResults() {
        return results;
    }

    public void setResults(trailerData[] results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", results = " + results + "]";
    }
}