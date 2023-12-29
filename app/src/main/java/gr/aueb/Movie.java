package gr.aueb;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;

public class Movie {
    
    private Availability av;
    private Contributors co;
    private MovieDetails md;
    private double imdbRating;
            
    public Movie(int id, String apiKey) throws IOException, InterruptedException {
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

        //responce for movie availability
        request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "/watch/providers"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response3 = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();

        av = gson.fromJson(response3.body(), Availability.class);
        co = gson.fromJson(response1.body(), Contributors.class);
        md = gson.fromJson(response2.body(), MovieDetails.class);

        imdbRating = getImdbRatingFromID(md.getImdb_id());
    }
    public static double getImdbRatingFromID(String imdbID) {
        String filePath = "C:\\Users\\Nick\\api_keys\\title.rating.tsv"; // Replace this with your file path
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

        
    //Searches for a movie in TMDB data base, returns arraylist with the ids of all the matches and prints their titles (only page 1)
    public static ArrayList<?> movieSearch(String searchInput, String apiKey, String returnType) throws Exception {  //TODO: handle exceptions  
        ArrayList<?> result;
        
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
        ArrayList<Integer> yearsArray = new ArrayList<Integer>();
        ArrayList<String> originalTitlesArray = new ArrayList<String>();

        //Iterate through the existing array and extract original ids
        System.out.println();
        
        for (int i = 0; i < resultsArray.length(); i++) {
            int originalId = resultsArray.getJSONObject(i).getInt("id");
            String originalTitle = resultsArray.getJSONObject(i).getString("original_title");
            String originalReleaseDate = resultsArray.getJSONObject(i).getString("release_date");
            originalIdsArray.add(originalId);
            originalTitlesArray.add(originalTitle);
                if(!originalReleaseDate.isEmpty()) {
                    LocalDate date = LocalDate.parse(originalReleaseDate);
                    int year = date.getYear();
                    yearsArray.add(year);
                    if(returnType.equals("title")) {
                        System.out.printf("%2d. %s (%d)%n", i + 1, originalTitle, year);
                    }      
                 } else {
                    yearsArray.add(-1);
                    if(returnType.equals("title")) {
                        System.out.printf("%2d. %s (%s)%n", i + 1, originalTitle, "Release date not available");
                    } 
                }
        }
    
        if (returnType.equals("title")) {
            result = originalTitlesArray;
            return result;
        } else if (returnType.equals("id")) {
            result = originalIdsArray;
            return result;
        } else {
            result = yearsArray;
            return result;
        }
    }


    
    @Override
    public String toString() {
        StringBuilder gens = new StringBuilder();
        StringBuilder ca = new StringBuilder();
        StringBuilder cr = new StringBuilder();

        // Genres
        Genre[] genres = this.md.getGenres();
        for (int i = 0; i < genres.length; i++) {
            gens.append(genres[i]);
            if (i < genres.length - 1) {
                gens.append(", ");
            }
        }

        // Cast
        for (Cast c : this.co.getCast()) {
            ca.append(c);
        }

        // Crew
        for (Crew c : this.co.getCrew()) {
            cr.append(c);
        }

        return "\n" + this.md.getOverview() + "\n \n"
            + "Title: " + this.md.getOriginal_title() + "\n"
            + "Runtime: " + this.md.getRuntime() + "m" + "\n"
            + "Genres: " + gens + "\n"
            + "Release Date: " + this.md.getRelease_date() + "\n"
            + "Tmdb Rating: " + this.md.getVote_average() + "\n"
            //+ "Imdb Rating: " + this.md.getImdbRatingFromID(this.md.getImdb_id()) + "\n \n \n"
            + "Imdb Rating: " + this.getImdbRating() + "\n \n \n" 
            + "Movie Contributors: \n \n"
            + "Cast: \n"
            + ca + "\n \n"
            + "Crew: \n"
            + cr + "\n\n";
            //+ this.av.toString(null)
    }


    public Availability getAv() {
        return av;
    }


    public Contributors getCo() {
        return co;
    }


    public MovieDetails getMd() {
        return md;
    }


    public double getImdbRating() {
        return imdbRating;
    }
}