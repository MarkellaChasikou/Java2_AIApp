package main;

import java.util.ArrayList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;

public class Movie {
    
    private Genre[] genres;
    private String id;
    private String original_title;
    private String overview;
    private String release_date;
    private String runtime;
    private float vote_average;
    private transient float avgRating;
    private transient ArrayList<Float> ratings;
   

    private Cast[] cast;
    private Crew[] crew;
    
    public Movie(Contributors creditsResponse, MovieDetails movieDetailsResponse) {
        avgRating = 0;
        ratings = new ArrayList<Float>();
        genres = movieDetailsResponse.getGenres();
        id = movieDetailsResponse.getId();
        original_title = movieDetailsResponse.getOriginal_title();
        overview = movieDetailsResponse.getOverview();
        release_date = movieDetailsResponse.getRelease_date();
        runtime = movieDetailsResponse.getRuntime();
        vote_average = movieDetailsResponse.getVote_average();
        cast = creditsResponse.getCast();
        crew = creditsResponse.getCrew();
    }
    
    public Genre[] getGenres() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float rating) {
        this.avgRating = rating;
    }

    public Cast[] getCast() {
        return cast;
    }

    public void setCast(Cast[] cast) {
        this.cast = cast;
    }

    public Crew[] getCrew() {
        return crew;
    }

    public void setCrew(Crew[] crew) {
        this.crew = crew;
    }

     public ArrayList<Float> getRatings() {
        return ratings;
    }

    public void setRatings(ArrayList<Float> ratings) {
        this.ratings = ratings;
    }

    //Searches for a movie id in project's data base UNFINISHED
    public static boolean localSearch(int id){
        return false;
    }

    //Searches for a movie in TMDB data base, returns arraylist with the ids of all the matches and prints their titles (only page 1)
    //TODO: sort arraylist based on popularity, fix foreign characters, make custom search options
    public static ArrayList<Integer> movieSearch(String searchInput, String apiKey) throws Exception {  //TODO: handle exceptions  
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/search/movie?query=" + searchInput + "&include_adult=true&language=en-US&page=1"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());
        //Parsing the JSON response
        JSONObject jsonResponse = new JSONObject(response.body());
        //Get the results array from the JSON object
        JSONArray resultsArray = jsonResponse.getJSONArray("results");
        ArrayList<Integer> originalIdsArray = new ArrayList<Integer>();

        //Iterate through the existing array and extract original ids
        for (int i = 0; i < resultsArray.length(); i++) {
            int originalId = resultsArray.getJSONObject(i).getInt("id");
            originalIdsArray.add(originalId);
            //Prints the title of each result 
            System.out.println(i+1 + "   " + resultsArray.getJSONObject(i).getString("original_title"));
        }
        return originalIdsArray;
    }

    public void updateRating(float rating) {
        ratings.add(rating);
        float sum = 0;
        int count = 0;
        for(int i = 0; i < ratings.size(); i++) {
            sum += ratings.get(i);
            count++;
        }
        avgRating = (float) sum / count;
    }
    
    // //Searches for all reviews of a specific movie in project's data base and prints them UNFINISHED
    public void getReviews() {
        
    }
    
    //TODO: better form
    @Override
    public String toString() {
        return "Movie [genres=" + Arrays.toString(genres) + ", id=" + id + ", original_title=" + original_title
                + ", overview=" + overview + ", release_date=" + release_date + ", runtime=" + runtime
                + ", vote_average=" + vote_average + ", avgRating=" + avgRating + ", ratings=" + ratings + ", cast="
                + Arrays.toString(cast) + ", crew=" + Arrays.toString(crew) + "]";
    }
}
