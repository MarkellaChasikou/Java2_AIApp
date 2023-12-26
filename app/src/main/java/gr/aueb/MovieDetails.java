package gr.aueb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MovieDetails {
    private Genre[] genres;
    private String id;
    private String original_title;
    private String overview;
    private String release_date;
    private String runtime;
    private float vote_average;
    private String poster_path;
    private String imdb_id;
    private double imdb_rating;
    
    public MovieDetails(Genre[] genres, String id, String original_title, String overview, String release_date,
            String runtime, float vote_average, String poster_path, String imdb_id) {
        this.genres = genres;
        this.id = id;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.runtime = runtime;
        this.vote_average = vote_average;
        this.poster_path = poster_path;
        this.imdb_id = imdb_id;
        //imdb_rating = getImdbRatingFromID(imdb_id);
    }

    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public double getImdb_rating() {
        return imdb_rating;
    }

    public void setImdb_rating(double imdb_rating) {
        this.imdb_rating = imdb_rating;
    }
}
