package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Crew {
    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    @SerializedName("department")
    private String department;    
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("popularity")
    private float popularity;

    public float getPopularity() {
        return popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getDepartment() {
        return department;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public int getId() {
        return id;
    }

    
    @Override
    public String toString() {
        return String.format("Name: %-30s\tJob: %s", name, job + "\n");
    }

} 
