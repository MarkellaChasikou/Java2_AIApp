/*
 * Crew
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import com.google.gson.annotations.SerializedName;

/**
 * Represents information about crew members associated with a movie.
 * 
 * The class includes details such as crew member name, job role, department,
 * unique identifier,
 * title, release date, and popularity score. It is designed to be used in the
 * context of media-related
 * applications or systems where information about the crew involved in a
 * production is essential.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Νίκος Ραγκούσης
 */

public class Crew {
    /** The name of the crew member. */
    @SerializedName("name")
    private String name;

    /** The job role of the crew member. */
    @SerializedName("job")
    private String job;

    /** The department to which the crew member belongs. */
    @SerializedName("department")
    private String department;

    /** Unique identifier for the crew member. */
    @SerializedName("id")
    private int id;

    /** The title associated with the crew member. */
    @SerializedName("title")
    private String title;

    /** The release date related to the crew member. */
    @SerializedName("release_date")
    private String release_date;

    /** The popularity score of the crew member. */
    @SerializedName("popularity")
    private float popularity;

    /**
     * Gets the popularity score of the crew member.
     * 
     * @return The popularity score.
     */
    public float getPopularity() {
        return popularity;
    }

    /**
     * Gets the release date related to the crew member.
     * 
     * @return The release date.
     */
    public String getRelease_date() {
        return release_date;
    }

    /**
     * Gets the department to which the crew member belongs.
     * 
     * @return The department.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Gets the title associated with the crew member.
     * 
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the name of the crew member.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the job role of the crew member.
     * 
     * @return The job role.
     */
    public String getJob() {
        return job;
    }

    /**
     * Gets the unique identifier for the crew member.
     * 
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns a formatted string representation of the crew member.
     * 
     * @return A string containing the name and job role.
     */
    @Override
    public String toString() {
        return String.format("Name: %-30s\tJob: %s", name, job + "\n");
    }
}
