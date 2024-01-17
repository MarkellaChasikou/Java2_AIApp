package gr.aueb;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MovieDAOTest {
private static Connection connection;

@BeforeAll
    public static void CreateInserts() throws Exception{
        DB db = new DB(); 
        connection = db.getConnection();
        
        try (PreparedStatement insertStmt1 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1, 'User1', 'TestPassword', 'TestCountry')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (2, 'User2', 'TestPassword', 'TestCountry')")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("INSERT INTO review (reviewId, userId, movieId, review_text, rating, spoiler) VALUES (1, 1, 1, 'Sample review text', 8, false)")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("INSERT INTO review (reviewId, userId, movieId, review_text, rating, spoiler) VALUES (2, 2, 1, 'Sample review text', 8.5, true)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        
}
@AfterAll
    public static void DeleteAllInserts() throws Exception {
        try (PreparedStatement insertStmt1 = connection.prepareStatement("DELETE FROM review WHERE reviewid = 1")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM review WHERE reviewid = 2")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 2")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        // Κλείσιμο της σύνδεσης
        if (connection != null) {
            connection.close();
        }
    }
    
@Test
    public void getAllReviewsForMovieTest() throws SQLException {
        try {
        
            List<Review> reviews = MovieDAO.getAllReviewsForMovie(1); // the movieid we have inserted the reviews on 
        
            assertNotNull(reviews);
        
            // Έλεγχος του πρώτου review
            if (reviews.size() >= 1) {
                    Review firstReview = reviews.get(0);
                    assertEquals(1, firstReview.getReviewId()); // Αναμένεται ότι το reviewId θα είναι 1
                    assertEquals(1, firstReview.getUserId()); // Αναμένεται ότι το userId θα είναι 1
                    assertEquals(1, firstReview.getMovieId()); // Επαληθεύουμε το movieId
                    assertEquals("Sample review text", firstReview.getReviewText()); // Επαληθεύουμε το review text
                    assertEquals(8, firstReview.getRating()); // Επαληθεύουμε το rating
                    assertFalse(firstReview.isSpoiler()); // Επαληθεύουμε ότι το spoiler είναι false
            }
        
            // Έλεγχος του δεύτερου review
            if (reviews.size() >= 2) {
                Review secondReview = reviews.get(1);
                assertEquals(2, secondReview.getReviewId()); // Αναμένεται ότι το reviewId θα είναι 2
                assertEquals(2, secondReview.getUserId()); // Αναμένεται ότι το userId θα είναι 2
                assertEquals(1, secondReview.getMovieId()); // Επαληθεύουμε το movieId
                assertEquals("Sample review text", secondReview.getReviewText()); // Επαληθεύουμε το review text
                assertEquals(8.5, secondReview.getRating()); // Επαληθεύουμε το rating
                assertTrue(secondReview.isSpoiler()); // Επαληθεύουμε ότι το spoiler είναι true
            }
        
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

@Test
    public void getSpoilerFreeReviewsForMovieTest() {
        try {
            
            List<Review> spoilerFreeReviews = MovieDAO.getSpoilerFreeReviewsForMovie(1);

            assertNotNull(spoilerFreeReviews);

            // Έλεγχος των σχολίων για spoilers
            for (Review review : spoilerFreeReviews) {
                assertFalse(review.isSpoiler()); // Ελέγχουμε ότι το σχόλιο δεν περιέχει spoilers
            }

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    @Test
    public void getAverageRatingForMovieTest() {
        try {

            double averageRating = MovieDAO.getAverageRatingForMovie(1);

            assertEquals(8.25, averageRating); // Προσδιορίζουμε την αναμενόμενη μέση βαθμολογία με rating1= 8 και rating2=8.5

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
        
}