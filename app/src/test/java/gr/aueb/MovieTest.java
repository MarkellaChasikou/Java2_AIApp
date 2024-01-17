package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MovieTest {

    // Replace "your_api_key" with an actual API key for testing
    private final String apiKey = "your_api_key";
    private final int movieId = 12345; // Replace with an actual movie ID for testing

    @Test
    public void testMovieConstructor() {
        Movie movie = new Movie(movieId, apiKey);

        assertNotNull(movie.getAv());
        assertNotNull(movie.getCo());
        assertNotNull(movie.getMd());
        assertTrue(movie.getImdbRating() >= 0);
        assertNotNull(movie.getPeopleId());
        assertNotNull(movie.getPeopleName());
        assertNotNull(movie.getPeopleJob());
        assertNotNull(movie.getPeople());
        assertNotNull(movie.getPeoplePopularity());
    }

    @Test
    public void testGetImdbRatingFromID() {
        // Replace "your_imdb_id" with an actual IMDb ID for testing
        double imdbRating = Movie.getImdbRatingFromID("your_imdb_id");

        assertTrue(imdbRating >= 0);
    }

    @Test
    public void testGetDirectors() {
        Movie movie = new Movie(movieId, apiKey);
        assertNotNull(movie.getPeople());
    }

    @Test
    public void testGetWriters() {
        Movie movie = new Movie(movieId, apiKey);
     //   assertNotNull(movie.getWriters());
    }

    @Test
    public void testGetActors() {
        Movie movie = new Movie(movieId, apiKey);
     //   assertNotNull(movie);
    }

    @Test
    public void testPrintResult() {
        Movie movie = new Movie(movieId, apiKey);
     //   assertNotNull(movie.printResult());
    }

    @Test
    public void testPrintFullCast() {
        Movie movie = new Movie(movieId, apiKey);
        movie.printFullCast();
    }

    @Test
    public void testToString() {
        Movie movie = new Movie(movieId, apiKey);
        assertNotNull(movie.toString());
    }

    // Add more tests as needed based on your requirements

}

