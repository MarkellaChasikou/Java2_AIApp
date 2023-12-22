package gr.aueb;

import java.util.HashMap;
import com.google.gson.annotations.SerializedName;

public class Availability {
    @SerializedName("results")
    private HashMap<String, Country> results;

    public HashMap<String, Country> getResults() {
        return results;
    }

    public void setResults(HashMap<String, Country> results) {
        this.results = results;

    }

    @Override
    public String toString() {
        return "Availability [results=" + results + "]";
    }
}
