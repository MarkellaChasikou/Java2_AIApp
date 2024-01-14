package gr.aueb;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BonusContent {

    

    public static void searchAndPrintVideo(String searchQuery, String category, String apiKey) {
        try {

            String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" +
                    URLEncoder.encode(searchQuery + " " + category, StandardCharsets.UTF_8) +
                    "&type=video&key=" + apiKey;

            InputStream input = new URL(searchUrl).openStream();
            JsonArray items = JsonParser.parseReader(new InputStreamReader(input)).getAsJsonObject().getAsJsonArray("items");

            if (items.size() > 0) {
                System.out.println("Βίντεο για την κατηγορία '" + category + "':");

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
                        System.out.println("Δεν βρέθηκε αναγνωριστικό βίντεο για την κατηγορία '" + category + "'.");
                    }
                    

                }
            } else {
                System.out.println("Δεν βρέθηκαν βίντεο για την κατηγορία '" + category + "'.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
// to fix: δεν βγαζει μερικες φορες σωστα τις λεξεις στους τιτλους πχ: στο doesn't βγαινει λαθος το '

