package gr.aueb;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
    private float avgRating;
    private ArrayList<Float> ratings;
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

        public double getImdbRatingFromID(String imdbID) {
        String filePath = "C:\\Users\\George\\Desktop\\imdb data\\title.rating.tsv"; // Replace this with your file path
        String line;
        double imdbRating = -1; // Default value if the ID is not found

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");

                if (data.length >= 3 && data[0].equals(imdbID)) {
                    imdbRating = Double.parseDouble(data[1]);
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return imdbRating;
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
            System.out.println(i+1 + "\t" + resultsArray.getJSONObject(i).getString("original_title"));
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
