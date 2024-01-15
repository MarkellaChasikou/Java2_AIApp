package gr.aueb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Genre {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;

    public static HashMap<Integer, String> getGenres(String apiKey) {
        HashMap<Integer, String> genres = new HashMap<>();
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/genre/movie/list?language=en"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            JsonArray genresArray = jsonObject.getAsJsonArray("genres");
            
            for (JsonElement genreElement : genresArray) {
                JsonObject genreObject = genreElement.getAsJsonObject();
                int id = genreObject.get("id").getAsInt();
                String name = genreObject.get("name").getAsString();
                genres.put(id, name);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return genres;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}