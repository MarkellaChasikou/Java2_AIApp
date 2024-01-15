/*
 * Contributors
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import com.google.gson.annotations.SerializedName;

/**
 * Represents contributors associated with a movie.
 * 
 * The class includes information such as contributor ID, an array of cast
 * members and an array of crew members.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Νίκος Ραγκούσης
 */

public class Contributors {
    /** Unique identifier for the contributor. */
    @SerializedName("id")
    private int id;

    /** Array of cast members associated with the contributor. */
    @SerializedName("cast")
    private Cast[] cast;

    /** Array of crew members associated with the contributor. */
    @SerializedName("crew")
    private Crew[] crew;

    /**
     * Gets the unique identifier for the contributor.
     * 
     * @return The contributor's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets an array of cast members associated with the contributor.
     * 
     * @return An array of cast members.
     */
    public Cast[] getCast() {
        return cast;
    }

    /**
     * Gets an array of crew members associated with the contributor.
     * 
     * @return An array of crew members.
     */
    public Crew[] getCrew() {
        return crew;
    }
}
