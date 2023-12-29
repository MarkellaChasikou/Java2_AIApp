package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class MovieDetails {
    @SerializedName("genres")
    private Genre[] genres;
    @SerializedName("id")
    private String id;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("runtime")
    private String runtime;
    @SerializedName("vote_average")
    private float vote_average;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("imdb_id")
    private String imdb_id;
    
    public Genre[] getGenres() {
        return genres;
    }
    public String getId() {
        return id;
    }
    public String getOriginal_title() {
        return original_title;
    }
    public String getOverview() {
        return overview;
    }
    public String getRelease_date() {
        return release_date;
    }
    public String getRuntime() {
        return runtime;
    }
    public float getVote_average() {
        return vote_average;
    }
    public String getPoster_path() {
        return poster_path;
    }
    public String getImdb_id() {
        return imdb_id;
    }
}
