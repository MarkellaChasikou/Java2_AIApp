package gr.aueb;

import java.util.ArrayList;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;

public class Movie {
    
    private Genre[] genres;
    private String id;
    private String original_title;
    private String overview;
    private String release_date;
    private String runtime;
    private float vote_average;
    private float avgRating;
    private Cast[] cast;
    private Crew[] crew;
    private String poster_path;
    
    public Movie(Contributors creditsResponse, MovieDetails movieDetailsResponse) {
        avgRating = 0;
        genres = movieDetailsResponse.getGenres();
        id = movieDetailsResponse.getId();
        original_title = movieDetailsResponse.getOriginal_title();
        overview = movieDetailsResponse.getOverview();
        release_date = movieDetailsResponse.getRelease_date();
        runtime = movieDetailsResponse.getRuntime();
        vote_average = movieDetailsResponse.getVote_average();
        cast = creditsResponse.getCast();
        crew = creditsResponse.getCrew();
        poster_path = movieDetailsResponse.getPoster_path();
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
    
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    //Searches for a movie id in project's data base UNFINISHED
    public static boolean localSearch(int id){
        return false;
    }

    //Creates new Movie Object if movie doesn't already exist
    public static void createMovie(int id, String apiKey) throws Exception{
        boolean exists = Movie.localSearch(id);
        
        if(exists) {
            //print details from project's data base 
        } else {
            //responce for movie credits
             HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "/credits?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response1 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        
            //responce for movie details
            request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
            HttpResponse<String> response2 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            Contributors creditsResponse = gson.fromJson(response1.body(), Contributors.class);
            MovieDetails movieDetailsResponse = gson.fromJson(response2.body(), MovieDetails.class);


            Movie movie = new Movie(creditsResponse, movieDetailsResponse);
            System.out.println(movie);
        }
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
        ArrayList<String> originalTitlesArray = new ArrayList<String>();

        //Iterate through the existing array and extract original ids
        System.out.println();
        for (int i = 0; i < resultsArray.length(); i++) {
            int originalId = resultsArray.getJSONObject(i).getInt("id");
            originalIdsArray.add(originalId);
            //Prints the title of each result 
            String originalTitle = resultsArray.getJSONObject(i).getString("original_title");
            originalTitlesArray.add(originalTitle);
            System.out.printf("%2d. %s%n", i + 1, originalTitle);
        }
        return originalIdsArray;
    }

    
    // //Searches for all reviews of a specific movie in project's data base and prints them UNFINISHED
    public void getReviews() {
        
    }
    
    @Override
    public String toString() {
        String gens = new String();
        String ca = new String();
        String cr = new String();
        
        for (int i = 0; i < genres.length; i++ ) {
            gens += genres[i].toString();
            if (i < genres.length-1) {
                gens += ", ";
            }
        }

        for (Cast c : cast) {
            ca += c.toString();
        }

        for (Crew c : crew) {
            cr += c.toString();
        }
        
        return "\n" + overview + "\n \n"
            + "Title: " + original_title + "\n"
            + "Runtime: " + runtime + "m" + "\n"
            + "Genres: " + gens + "\n"
            + "Release Date: " + release_date +"\n"
            + "Tmdb Rating: " + vote_average + "\n \n \n"
            + "Movie Contributors: \n \n" 
            + "Cast: \n"  
            + ca + "\n \n"
            + "Crew: \n" 
            + cr;
    }
}
