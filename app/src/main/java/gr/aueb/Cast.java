/*
 * Cast
 * 
 * Copyright 2024 Bugs Bunny
 */

package gr.aueb;

import com.google.gson.annotations.SerializedName;

/**
 * Represents information about cast members in movies.
 * 
 * Container for cast details in media applications. Provides names, characters,
 * IDs, titles,
 * release dates, and popularity scores.
 * 
 * @version 1.8 released on 14th January 2024
 * @author Νίκος Ραγκούσης
 */

public class Cast {
    /** The name of the cast member. */
    @SerializedName("name")
    private String name;

    /** The character played by the cast member. */
    @SerializedName("character")
    private String character;

    /** The unique identifier for the cast member. */
    @SerializedName("id")
    private int id;

    /** The title associated with the cast member. */
    @SerializedName("title")
    private String title;

    /** The release date related to the cast member. */
    @SerializedName("release_date")
    private String release_date;

    /** The popularity score of the cast member. */
    @SerializedName("popularity")
    private float popularity;

    /**
     * Gets the popularity score of the cast member.
     * 
     * @return The popularity score.
     */
    public float getPopularity() {
        return popularity;
    }

    /**
     * Gets the release date related to the cast member.
     * 
     * @return The release date.
     */
    public String getRelease_date() {
        return release_date;
    }

    /**
     * Gets the title associated with the cast member.
     * 
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the unique identifier for the cast member.
     * 
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the cast member.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the character played by the cast member.
     * 
     * @return The character name.
     */
    public String getCharacter() {
        return character;
    }

    /**
     * Returns a formatted string representation of the cast member.
     * 
     * @return A string containing the name and character name.
     */
    @Override
    public String toString() {
        return String.format("Name: %-30s\tCharacter Name: %s", name, character + "\n");
    }
}
