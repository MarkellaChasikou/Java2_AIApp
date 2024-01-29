package gr.aueb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.TreeMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("free")
    private ArrayList<Provider> free;
    @SerializedName("ads")
    private ArrayList<Provider> ads;
    @SerializedName("buy")
    private ArrayList<Provider> buy;
    @SerializedName("flatrate")
    private ArrayList<Provider> flatrate;
    @SerializedName("rent")
    private ArrayList<Provider> rent;


    public static TreeMap<String, String> getAllCountriesNames(String apiKey)  {
        TreeMap<String, String> countries = new TreeMap<>();
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/watch/providers/regions?language=en-US"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            JsonElement element = JsonParser.parseString(response.body());
            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");

            for (JsonElement countryElement : resultsArray) {
                JsonObject countryObject = countryElement.getAsJsonObject();
                String code = countryObject.get("iso_3166_1").getAsString();
                String name = countryObject.get("english_name").getAsString();

                countries.put(code, name);
            }

        } catch (IOException e) {
            System.err.println("Check your internet connection!");
            e.printStackTrace();
        
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return countries;
    }

    public ArrayList<Provider> getFree() {
        return free;
    }

    public ArrayList<Provider> getAds() {
        return ads;
    }

    public ArrayList<Provider> getBuy() {
        return buy;
    }

    public ArrayList<Provider> getFlatrate() {
        return flatrate;
    }

    public ArrayList<Provider> getRent() {
        return rent;
    }
}
