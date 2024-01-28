/*
 * MovieDetails
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * Represents details of a movie.
 * Includes movie details such as genres, ID, title, overview,
 * release date, runtime, vote average, and IMDb ID.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης
 */
public class MovieDetails {
    /** An array of genres associated with the movie. */
    @SerializedName("genres")
    private Genre[] genres;

    /** The unique identifier of the movie. */
    @SerializedName("id")
    private int id;

    /** The original title of the movie. */
    @SerializedName("original_title")
    private String original_title;

    /** A brief overview or summary of the movie. */
    @SerializedName("overview")
    private String overview;

    /** The release date of the movie. */
    @SerializedName("release_date")
    private String release_date;

    /** The duration of the movie in minutes. */
    @SerializedName("runtime")
    private int runtime;

    /** The average vote rating for the movie. */
    @SerializedName("vote_average")
    private float vote_average;

    /** The IMDb ID associated with the movie. */
    @SerializedName("imdb_id")
    private String imdb_id;

    /**
     * Gets the array of genres associated with the movie.
     *
     * @return An array of genres.
     */
    public Genre[] getGenres() {
        return genres;
    }

    /**
     * Gets the unique identifier of the movie.
     *
     * @return The movie's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the original title of the movie.
     *
     * @return The original title.
     */
    public String getOriginal_title() {
        return original_title;
    }

    /**
     * Gets a brief overview or summary of the movie.
     *
     * @return The movie's overview.
     */
    public String getOverview() {
        return overview;
    }

    /**
     * Gets the release date of the movie.
     *
     * @return The release date.
     */
    public String getRelease_date() {
        return release_date;
    }

    /**
     * Gets the duration of the movie in minutes.
     *
     * @return The movie's runtime.
     */
    public int getRuntime() {
        return runtime;
    }

    /**
     * Gets the average vote rating for the movie.
     *
     * @return The vote average.
     */
    public float getVote_average() {
        return vote_average;
    }

    /**
     * Gets the IMDb ID associated with the movie.
     *
     * @return The IMDb ID.
     */
    public String getImdb_id() {
        return imdb_id;
    }
}
