package gr.aueb;

import java.util.ArrayList;
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

    public String formatAvailability(String category, ArrayList<Prov> providers) {
        if (providers != null && !providers.isEmpty()) {
            StringBuilder formatted = new StringBuilder(category + ": ");
            for (Prov provider : providers) {
                formatted.append(provider).append(", ");
            }

            formatted.setLength(formatted.length() - 2);
            formatted.append("\n\n");
            return formatted.toString();
        }
        return "";
    }


    public void printAvailability(String country) {
        StringBuilder result = new StringBuilder();
        Country c = results.get(country);
        if(results.containsKey(country)) {
            result.append(formatAvailability("Free", c.getFree()));
            result.append(formatAvailability("Ads", c.getAds()));
            result.append(formatAvailability("Buy", c.getBuy()));
            result.append(formatAvailability("Stream", c.getFlatrate()));                
            result.append(formatAvailability("Rent", c.getRent()));
            System.out.println(result);
        } else System.out.println("Not available in " + country);
    }
}
