/*
 * BonusContent
 * 
 * Copyright 2024 Bugs Bunny
 */

package gr.aueb;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Represents a utility class for searching and printing bonus content videos.
 * 
 * This class integrates with the YouTube Data API to search for videos related
 * to
 * specific search queries and categories. It provides methods to perform
 * searches
 * and print relevant video information, such as titles and URLs, for a given
 * category.
 * 
 * @version 1.8 released on 14th January 2024
 * @author Νίκος Ραγκούσης
 */
public class BonusContent {

    /**
     * Searches for and prints videos related to the specified search query and
     * category.
     * 
     * @param searchQuery The search query to be used, typically representing a
     *                    movie or content title.
     * @param category    The category of the bonus content (e.g., "Fun Facts",
     *                    "Behind the Scenes", "Interviews").
     * @param apiKey      The API key required for accessing the YouTube Data API.
     */
    public static void searchAndPrintVideo(String searchQuery, String category, String apiKey ) {
        if (apiKey == null) {
            throw new IllegalArgumentException("ApiKey cannot be null or empty.");
        }
        if (searchQuery == null) {
            throw new IllegalArgumentException("Search Query cannot be null or empty.");
        }
        if (category == null) {
            throw new IllegalArgumentException("category cannot be null or empty.");
        }
        try {

            String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" +
                URLEncoder.encode(searchQuery + " " + category, "UTF-8") +
                "&type=video&key=" + apiKey;

        InputStream input = new URL(searchUrl).openStream();
        JsonArray items = JsonParser.parseReader(new InputStreamReader(input, "UTF-8")).getAsJsonObject().getAsJsonArray("items");


            if (items.size() > 0) {
                System.out.println("Videos for the category '" + category + "':");

                for (int i = 0; i < Math.min(3, items.size()); i++) {
                    JsonObject item = items.get(i).getAsJsonObject();
                    JsonObject snippet = item.getAsJsonObject("snippet");

                    JsonObject idObject = item.getAsJsonObject("id");

                    if (idObject != null) {
                        String videoId = idObject.get("videoId").getAsString();
                        String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

                        String videoTitle = snippet.get("title").getAsString();
                        System.out.println(category + " " + (i + 1) + ": " + videoTitle + " - " + videoUrl);
                    } else {
                        System.out.println("No video ID found for the category '" + category + "'.");
                    }

                }
            } else {
                System.out.println("No videos found for the category '" + category + "'.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
// to fix: sometimes movie titles do not appear in the right way e.g: doesn't does not appear right
