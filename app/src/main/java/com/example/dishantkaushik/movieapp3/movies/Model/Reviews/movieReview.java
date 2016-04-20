package com.example.dishantkaushik.movieapp3.movies.Model.Reviews;

/**
 * Created by dishantkaushik on 20/04/16.
 */
public class movieReview {
    private String id;

    private reviewData[] results;

    private String page;

    private String total_pages;

    private String total_results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public reviewData[] getResults() {
        return results;
    }

    public void setResults(reviewData[] results) {
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
        return "ClassPojo [id = " + id + ", results = " + results + ", page = " + page + ", total_pages = " + total_pages + ", total_results = " + total_results + "]";
    }
}