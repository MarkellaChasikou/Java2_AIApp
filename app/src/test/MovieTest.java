package gr.aueb;

import static org.junit.Assert.*;
import org.junit.Test;

public class MovieTest {

    // Replace "YOUR_API_KEY" with a valid API key for testing
    private static final String API_KEY = "YOUR_API_KEY";
    
    @Test
    public void testConstructor() {
        // Replace "MOVIE_ID" with a valid movie ID for testing
        int movieId = MOVIE_ID;
        Movie movie = new Movie(movieId, API_KEY);

        assertNotNull(movie);
        assertNotNull(movie.getCo());
        assertNotNull(movie.getMd());
        assertNotNull(movie.getAv());
        assertTrue(movie.getImdbRating() >= 0);
        assertNotNull(movie.getPeopleId());
        assertNotNull(movie.getPeopleName());
        assertNotNull(movie.getPeopleJob());
        assertNotNull(movie.getPeople());
        assertNotNull(movie.getPeoplePopularity());
    }

    @Test
    public void testGetImdbRatingFromID() {
        // Assuming you have a test file with IMDb ratings
        double rating = Movie.getImdbRatingFromID("tt0123456");
        assertEquals(7.5, rating, 0.01);
    }

    @Test
    public void testGetDirectors() {
        // Create a Movie instance for testing
        Movie movie = new Movie(MOVIE_ID, API_KEY);

        // Test if the list of directors is not null
        assertNotNull(movie.getDirectors());
    }

    @Test
    public void testGetWriters() {
        // Create a Movie instance for testing
        Movie movie = new Movie(MOVIE_ID, API_KEY);

        // Test if the list of writers is not null
        assertNotNull(movie.getWriters());
    }

    @Test
    public void testGetActors() {
        // Create a Movie instance for testing
        Movie movie = new Movie(MOVIE_ID, API_KEY);

        // Test if the list of actors is not null
        assertNotNull(movie.getActors());
    }

    @Test
    public void testPrintResult() {
        // Create a Movie instance for testing
        Movie movie = new Movie(MOVIE_ID, API_KEY);

        // Test if the result string is not null
        assertNotNull(movie.printResult());
    }

    // Add more tests as needed based on your specific requirements
}
