package gr.aueb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("free")
    private ArrayList<Prov> free;
    @SerializedName("ads")
    private ArrayList<Prov> ads;
    @SerializedName("buy")
    private ArrayList<Prov> buy;
    @SerializedName("flatrate")
    private ArrayList<Prov> flatrate;
    @SerializedName("rent")
    private ArrayList<Prov> rent;
    

    public ArrayList<Prov> getFree() {
        return free;
    }

    public void setFree(ArrayList<Prov> free) {
        this.free = free;
    }

    public ArrayList<Prov> getAds() {
        return ads;
    }

    public void setAds(ArrayList<Prov> ads) {
        this.ads = ads;
    }

    public ArrayList<Prov> getBuy() {
        return buy;
    }

    public void setBuy(ArrayList<Prov> buy) {
        this.buy = buy;
    }

    public ArrayList<Prov> getFlatrate() {
        return flatrate;
    }

    public void setFlatrate(ArrayList<Prov> flatrate) {
        this.flatrate = flatrate;
    }

    public ArrayList<Prov> getRent() {
        return rent;
    }

    public void setRent(ArrayList<Prov> rent) {
        this.rent = rent;
    }

    public static HashMap<String, String> allCountriesNames(String apiKey) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/watch/providers/regions?language=en-US"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());
        
        JsonElement element = JsonParser.parseString(response.body());
        JsonObject jsonObject = element.getAsJsonObject();
        JsonArray resultsArray = jsonObject.getAsJsonArray("results");

        HashMap<String, String> countries = new HashMap<>();

        for (JsonElement countryElement : resultsArray) {
            JsonObject countryObject = countryElement.getAsJsonObject();
            String code = countryObject.get("iso_3166_1").getAsString();
            String name = countryObject.get("english_name").getAsString();

            countries.put(code, name);
        }
        return countries;
    }

    @Override
    public String toString() {
        return "Country [free=" + free + ", ads=" + ads + ", buy=" + buy + ", flatrate=" + flatrate + ", rent=" + rent
                + "]";
    }

    

}
