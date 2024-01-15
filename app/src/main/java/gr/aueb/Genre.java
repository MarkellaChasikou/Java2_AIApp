/*
 * Genre
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
     * Retrieves a mapping of genre IDs to names using The Movie Database (TMDb)
     * API.
     * 
     * @param apiKey The API key for TMDb authentication.
     * @return A HashMap containing genre IDs as keys and corresponding names as
     *         values.
     */
    public static HashMap<Integer, String> getGenres(String apiKey) {
        HashMap<Integer, String> genres = new HashMap<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/genre/movie/list?language=en"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            JsonElement element = JsonParser.parseString(response.body());
            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");

            for (JsonElement g : resultsArray) {
                JsonObject countryObject = g.getAsJsonObject();
                int id = countryObject.get("id").getAsInt();
                String name = countryObject.get("name").getAsString();

                genres.put(id, name);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return genres;
    }

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
