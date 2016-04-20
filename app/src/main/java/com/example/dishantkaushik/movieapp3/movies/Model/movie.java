package com.example.dishantkaushik.movieapp3.movies.Model;

import java.io.Serializable;

/**
 * Created by dishantkaushik on 20/04/16.
 */

public class movie implements Serializable {
    private movieData[] results;

    private String page;

    private String total_pages;

    private String total_results;

    public movieData[] getResults() {
        return results;
    }

    public void setResults(movieData[] results) {
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public String getTotal_results() {
        return total_results;
    }

    public void setTotal_results(String total_results) {
        this.total_results = total_results;
    }

    @Override
    public String toString() {
        return "ClassPojo [results = " + results + ", page = " + page + ", total_pages = " + total_pages + ", total_results = " + total_results + "]";
    }
}