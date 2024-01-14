/*
 * Availability
 * 
 * Copyright 2024 Bugs Bunny
 */

package gr.aueb;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.annotations.SerializedName;

/**
 * Represents the availability of content in different countries.
 * 
 * This class serves as a comprehensive representation of the availability of
 * digital content,
 * categorizing it based on various criteria such as Free, Ads-supported,
 * Purchase (Buy),
 * Streaming (Flatrate), and Rental (Rent). It encapsulates information about
 * the availability
 * of content across different providers and presents a structured view of the
 * digital content landscape
 * in a given country.
 * The class includes methods to retrieve and format availability data, offering
 * a convenient way to
 * present information about the accessibility of content in specific categories
 * for a particular country.
 * 
 * @version 1.8, released on 14th January 2024
 * @author Νίκος Ραγκούσης
 */
public class Availability {
    /**
     * Holds the availability results for different countries.
     */
    @SerializedName("results")
    private HashMap<String, Country> results;

    /**
     * Gets the availability results.
     * 
     * @return The availability results.
     */
    public HashMap<String, Country> getResults() {
        return results;
    }

    /**
     * Formats the availability information for a specific category and providers.
     * 
     * @param category  The category of availability (e.g., Free, Ads, Buy, Stream,
     *                  Rent).
     * @param providers The list of providers for the specified category.
     * @return The formatted availability information.
     */
    public String formatAvailability(String category, ArrayList<Provider> providers) {
        if (providers != null && !providers.isEmpty()) {
            StringBuilder formatted = new StringBuilder(category + ": ");
            for (Provider provider : providers) {
                formatted.append(provider).append(", ");
            }

            formatted.setLength(formatted.length() - 2);
            formatted.append("\n");
            return formatted.toString();
        }
        return "";
    }

    /**
     * Generates a string representation of the availability for a specific country.
     * 
     * @param country The country for which availability information is requested.
     * @return The string representation of availability for the specified country.
     */
    public String toString(String country) {
        StringBuilder result = new StringBuilder();
        Country c = results.get(country);
        if (results.containsKey(country)) {
            result.append(formatAvailability("Free", c.getFree()));
            result.append(formatAvailability("Ads", c.getAds()));
            result.append(formatAvailability("Buy", c.getBuy()));
            result.append(formatAvailability("Stream", c.getFlatrate()));
            result.append(formatAvailability("Rent", c.getRent()));
            return result.toString();
        } else
            return "\nNot available in " + country;
    }
}
