/*
 * Genre
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import com.google.gson.annotations.SerializedName;

/**
 * Represents information about movie genres retrieved from The Movie Database
 * (TMDb).
 * 
 * The class includes methods to fetch and store genre information using the
 * TMDb API.
 * It provides a static method, getGenres, to retrieve a mapping of genre IDs to
 * names.
 * The attributes include the genre name and ID, and the class is designed to be
 * used in the context
 * of media-related applications or systems where knowledge of movie genres is
 * essential.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Νίκος Ραγκούσης
 */

public class Genre {
    /** The name of the genre. */
    @SerializedName("name")
    private String name;

    /** The unique identifier for the genre. */
    @SerializedName("id")
    private int id;

    /**
     * Gets the name of the genre.
     * 
     * @return The genre name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a string representation of the genre.
     * 
     * @return The genre name as a string.
     */
    @Override
    public String toString() {
        return name;
    }
}
