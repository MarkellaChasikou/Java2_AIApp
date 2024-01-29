/*
 * Person Credits
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.util.HashSet;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the movie credits of a person, including both cast and crew roles.
 * 
 * This class provides information about the roles a person has played in
 * movies,
 * distinguishing between cast roles (e.g., actor) and crew roles (e.g.,
 * director).
 *
 * The class includes methods to retrieve the array of cast roles, the array of
 * crew roles,
 * and the associated ID for the person's credits.
 *
 * The toString() method generates a human-readable string representation of the
 * person's movie credits,
 * including details such as the title, character name (for cast roles), job
 * (for crew roles), and more.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης
 */
public class PersonCredits {
    /** Array of cast roles performed by the person. */
    @SerializedName("cast")
    private Cast[] cast;
    /** Array of crew roles performed by the person. */
    @SerializedName("crew")
    private Crew[] crew;
    /** The ID associated with the person's credits. */
    @SerializedName("id")
    private int id;

    @Override
    /**
     * Returns a string representation of the person's movie credits, including cast
     * and crew roles.
     */
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        // Append information about cast roles
        if (cast.length > 0) {
            returnString.append("Actor\n");
            for (Cast c : cast) {
                returnString.append("Title: " + c.getTitle() + "\n" + "Character name: " + c.getCharacter() + "\n\n");
            }
        }

        // Append information about crew roles
        if (crew != null) {
            HashSet<String> uniqueValues = new HashSet<>();
            for (Crew c : crew) {
                if (!uniqueValues.contains(c.getJob())) {
                    uniqueValues.add(c.getJob());
                    returnString.append("\n" + c.getJob() + "\n");
                }
                returnString.append("Title: " + c.getTitle() + "\n\n");
            }
        }
        return returnString.toString();
    }

    /**
     * Gets the array of cast roles performed by the person.
     * 
     * @return Array of Cast objects.
     */
    public Cast[] getCast() {
        return cast;
    }

    /**
     * Gets the array of crew roles performed by the person.
     * 
     * @return Array of Crew objects.
     */
    public Crew[] getCrew() {
        return crew;
    }

    /**
     * Gets the ID associated with the person's credits.
     * 
     * @return The ID of the person's credits.
     */
    public int getId() {
        return id;
    }
}
