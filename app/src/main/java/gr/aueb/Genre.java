package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}