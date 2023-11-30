package main;

import java.util.Arrays;

public class MovieDetails {
    private Genre[] genres;
    private String id;
    private String original_title;
    private String overview;
    private String release_date;
    private String runtime;
    private float vote_average;
    
    public MovieDetails(Genre[] genres, String id, String original_title, String overview, String release_date,
            String runtime, float vote_average) {
        this.genres = genres;
        this.id = id;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
        this.runtime = runtime;
        this.vote_average = vote_average;
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

    @Override
    public String toString() {
        return "MovieDetails [genres=" + Arrays.toString(genres) + ", id=" + id + ", original_title=" + original_title
                + ", overview=" + overview + ", release_date=" + release_date + ", runtime=" + runtime
                + ", vote_average=" + vote_average + "]";
    }
}
