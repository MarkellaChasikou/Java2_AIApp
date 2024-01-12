package gr.aueb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

public class Person {
    private PersonDetails pd;
    private PersonCredits pc;
    private ArrayList<Integer> movieIds;
    private ArrayList<String> movieTitles;
    private ArrayList<String> movieDates;
    private HashMap<Integer, Object[]> movies;
    private ArrayList<Float> moviePopularity;

    public Person(int id, String apikey) {
        Gson gson = new Gson();
        movieIds = new ArrayList<>();
        movieTitles = new ArrayList<>();
        movieDates = new ArrayList<>();
        movies = new HashMap<>();
        moviePopularity = new ArrayList<>();

        //Details
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/person/" + id + "?language=en-US"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apikey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        try {
            HttpResponse<String> response1 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            pd = gson.fromJson(response1.body(), PersonDetails.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //credits
        request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/person/" + id +"/movie_credits?language=en-US"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apikey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        try {
            HttpResponse<String> response2 = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            pc = gson.fromJson(response2.body(), PersonCredits.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(pc.getCast() != null) {
            Object temp[]; 
            for (Cast c : pc.getCast()) {
                temp = new Object[3];
                temp[0] = c.getTitle();
                temp[1] = c.getRelease_date();
                temp[2] = c.getPopularity();
                movies.put(c.getId(), temp);
            }
        }

        if(pc.getCrew() != null) {
            Object temp[]; 
            for (Crew c : pc.getCrew()) {
                temp = new Object[3];
                temp[0] = c.getTitle();
                temp[1] = c.getRelease_date();
                temp[2] = c.getPopularity();
                movies.put(c.getId(), temp);
            }
        }
 
        for (Integer i : movies.keySet()) {
            movieIds.add(i);
        }

        for (Object[] i : movies.values()) {
            movieTitles.add((String)i[0]);
            movieDates.add((String)i[1]);
            moviePopularity.add((float)i[2]);
        }
    } 

    @Override
    public String toString() {
        return "\n\n" + pd.toString() + pc.toString();
    }

    public PersonDetails getPd() {
        return pd;
    }

    public PersonCredits getPc() {
        return pc;
    }

    public HashMap<Integer, Object[]> getMovies() {
        return movies;
    }

    public ArrayList<Integer> getMovieIds() {
        return movieIds;
    }

    public ArrayList<String> getMovieTitles() {
        return movieTitles;
    }

    public ArrayList<String> getMovieDates() {
        return movieDates;
    }

    public ArrayList<Float> getMoviePopularity() {
        return moviePopularity;
    }
}