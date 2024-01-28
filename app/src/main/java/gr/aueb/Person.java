/*
 * Person 
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

/**
 * Represents details about a person, including their movie credits.
 * 
 * This class fetches data from The Movie Database (TMDb) API using a given
 * person ID
 * and API key, providing details such as personal information, movie credits as
 * both
 * a cast member and crew, and associated movie details like titles, release
 * dates, and popularity.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης
 */
public class Person {
    /** Details about the person. */
    private PersonDetails pd;

    /** Credits of the person, including movies they were part of. */
    private PersonCredits pc;

    /** List of movie IDs associated with the person. */
    private ArrayList<Integer> movieIds;

    /** List of movie titles associated with the person. */
    private ArrayList<String> movieTitles;

    /** List of movie release dates associated with the person. */
    private ArrayList<String> movieDates;

    /**
     * Map of movie IDs to an array of details (title, release date, popularity).
     */
    private HashMap<Integer, Object[]> movies;

    /** List of movie popularity values associated with the person. */
    private ArrayList<Float> moviePopularity;

    /**
     * Constructs a Person object with the given ID and API key.
     * 
     * @param id     The ID of the person.
     * @param apikey The API key for authentication.
     */
    public Person(int id, String apikey) {
        Gson gson = new Gson();
        movieIds = new ArrayList<>();
        movieTitles = new ArrayList<>();
        movieDates = new ArrayList<>();
        movies = new HashMap<>();
        moviePopularity = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/person/" + id + "?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response1 = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            pd = gson.fromJson(response1.body(), PersonDetails.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // credits
        request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/person/" + id + "/movie_credits?language=en-US"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + apikey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response2 = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            pc = gson.fromJson(response2.body(), PersonCredits.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (pc.getCast() != null) {
            Object temp[];
            for (Cast c : pc.getCast()) {
                temp = new Object[3];
                temp[0] = c.getTitle();
                temp[1] = c.getRelease_date();
                temp[2] = c.getPopularity();
                movies.put(c.getId(), temp);
            }
        }

        if (pc.getCrew() != null) {
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
            movieTitles.add((String) i[0]);
            movieDates.add((String) i[1]);
            moviePopularity.add((float) i[2]);
        }
    }

    @Override
    /**
     * Returns a string representation of the person, including details and credits.
     */
    public String toString() {
        return "\n\n" + pd.toString() + pc.toString();
    }

    /**
     * Returns details about the person.
     * 
     * @return PersonDetails object.
     */
    public PersonDetails getPd() {
        return pd;
    }

    /**
     * Returns credits of the person.
     * 
     * @return PersonCredits object.
     */
    public PersonCredits getPc() {
        return pc;
    }

    /**
     * Returns a map of movie IDs to an array of details (title, release date,
     * popularity).
     * 
     * @return Map<Integer, Object[]>.
     */
    public HashMap<Integer, Object[]> getMovies() {
        return movies;
    }

    /**
     * Returns a list of movie IDs associated with the person.
     * 
     * @return ArrayList<Integer>.
     */
    public ArrayList<Integer> getMovieIds() {
        return movieIds;
    }

    /**
     * Returns a list of movie titles associated with the person.
     * 
     * @return ArrayList<String>.
     */
    public ArrayList<String> getMovieTitles() {
        return movieTitles;
    }

    /**
     * Returns a list of movie release dates associated with the person.
     * 
     * @return ArrayList<String>.
     */
    public ArrayList<String> getMovieDates() {
        return movieDates;
    }

    /**
     * Returns a list of movie popularity values associated with the person.
     * 
     * @return ArrayList<Float>.
     */
    public ArrayList<Float> getMoviePopularity() {
        return moviePopularity;
    }
}
