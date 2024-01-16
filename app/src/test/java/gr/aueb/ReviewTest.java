package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReviewTest {
    private static User user;
    private static Movie movie; // 389 movie id for 12angrymen
    private static String tmdbApiKey;
    private static Review review;
    private static Connection connection;
    private static Review reviewDelete;
    static File tmdbFile = new File("c:/Users/Βασιλης/OneDrive/Υπολογιστής/apiKeys/tmdb_api_key.txt");

   
    @BeforeAll
    public static void CreateInserts() throws Exception {
        DB db = new DB(); 
        connection = db.getConnection();
        // αυτη εδω η review για να την κανω delete 
        reviewDelete = new Review(1, 2, 1, "Sample review text", 8, false);
        try (BufferedReader br = new BufferedReader(new FileReader(tmdbFile))) {
            tmdbApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading tmdb API key file.");
            System.exit(1);
        }
        movie = new Movie(389,tmdbApiKey );
    }
@BeforeEach
public void InsertReviewTestSqlQueries(){
    try (PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO review (reviewId, userId, movieId, review_text, rating, spoiler) VALUES (1, 2, 1, 'Sample review text', 8, false)")) {
        insertStmt.executeUpdate();
    } catch (SQLException e) {
        fail("Exception thrown during setup: " + e.getMessage());
    }
}
   @AfterEach
   public void DeleteReviewTestInsert(){
    try (PreparedStatement insertStmt = connection.prepareStatement("DELETE FROM review WHERE userid = 2")) {
        insertStmt.executeUpdate();
    } catch (SQLException e) {
        fail("Exception thrown during setup: " + e.getMessage());
    }
   }



    @AfterAll
    public static void DeleteInserts() throws Exception {
      // Καθαρισμός του πίνακα Reviews μετά τα tests

      try {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM review WHERE userId = ?")) {
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();

         try (PreparedStatement stmt2 = connection.prepareStatement("DELETE FROM appuser WHERE userId = ?")) {
               stmt2.setInt(1, user.getId());
               stmt2.executeUpdate();     
         }
         try (PreparedStatement stmt3 = connection.prepareStatement("DELETE FROM review WHERE userId = ?")) {
            stmt3.setInt(1, reviewDelete.getUserId());
            stmt3.executeUpdate();     
      }
        }
    } catch (SQLException e) {
        fail("Exception thrown during cleanup: " + e.getMessage());
    }
    // Κλείσιμο της σύνδεσης
    if (connection != null) {
        connection.close();
    }
    }

@Test
public void getReviewIdTest() throws SQLException {
//  // Έλεγχος της μεθόδου getReviewId
    try (PreparedStatement stmt = connection.prepareStatement("SELECT reviewid FROM review WHERE reviewId = 1")) {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(reviewDelete.getReviewId(), resultSet.getInt("reviewid"));
            } else {
                fail("the reviewid does not match");
            }
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
    }
}

@Test
public void getUserIdTest() throws SQLException {
//  // Έλεγχος της μεθόδου getUserId
    try (PreparedStatement stmt = connection.prepareStatement("SELECT userid FROM review WHERE reviewId = 1")) {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(reviewDelete.getUserId(), resultSet.getInt("userid"));
            } else {
                fail("the userId does not match");
            }
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
    }
}

@Test
public void getMovieIdTest() throws SQLException {
//  // Έλεγχος της μεθόδου getUserId
    try (PreparedStatement stmt = connection.prepareStatement("SELECT movieid FROM review WHERE reviewId = 1")) {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(reviewDelete.getMovieId(), resultSet.getInt("movieid"));
            } else {
                fail("the movieId does not match");
            }
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
    }
}

@Test
public void getReviewTextTest() throws SQLException {
//  // Έλεγχος της μεθόδου getUserId
    try (PreparedStatement stmt = connection.prepareStatement("SELECT review_text FROM review WHERE reviewId = 1")) {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(reviewDelete.getReviewText(), resultSet.getString("review_text"));
            } else {
                fail("the review_text does not match");
            }
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
    }
}

@Test
public void getRatingTest() throws SQLException {
//  // Έλεγχος της μεθόδου getUserId
    try (PreparedStatement stmt = connection.prepareStatement("SELECT rating FROM review WHERE reviewId = 1")) {
        try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(reviewDelete.getRating(), resultSet.getFloat("rating"));
            } else {
                fail("the rating does not match");
            }
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
    }
}

@Test
public void getSpoilerTest() throws SQLException {
//  // Έλεγχος της μεθόδου getUserId
boolean t = false;
    try (PreparedStatement stmt = connection.prepareStatement("SELECT spoiler FROM review WHERE reviewId = 1")) {
        try (ResultSet resultSet = stmt.executeQuery()) {

            if (resultSet.next()) {
                if (resultSet.getInt("spoiler")==0) {
                    t = false;
                    assertEquals(reviewDelete.isSpoiler(), t);
                }else{
                t = true;
                 assertEquals(reviewDelete.isSpoiler(), t);
                }
            } else {
                fail("the spoiler does not match");
            }
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
    }
}


   @Test
        public void addReviewTest() throws Exception{
            user = User.register("TestName7","TestPassword","TestCountry");
            review = Review.addReview (user.getId(),389, "VERY BAD , why is robert de niro still acting????", 9, false);
            try {
            // Έλεγχος αν η εγγραφή έγινε σωστά
            assertNotNull(review);
            assertEquals(movie.getMd().getId() ,review.getMovieId() );
           assertEquals(9, review.getRating());
            assertEquals(false, review.isSpoiler());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
       } 
    }

    @Test
    public void setReviewTextTest() throws Exception {

        // Κλήση της setReviewText με νέο κείμενο
        String newReviewText = "Okey robert de niro isnt that bad";
        review.setReviewText(newReviewText,user.getId());

        // Ελέγχουμε αν το κείμενο της κριτικής έχει αλλάξει σωστά
        assertEquals(newReviewText, review.getReviewText());
    }

    @Test
    public void updateReviewTextInDatabaseTest() throws Exception {
       
        // Ελέγχουμε αν το κείμενο στη βάση έχει αλλάξει σωστά
        String newReviewText = "THE BEST MOVIE EVER! Robert de niro rules";
        review.setReviewText(newReviewText,user.getId());
        try (PreparedStatement stmt = connection.prepareStatement("SELECT review_text FROM review WHERE reviewId = ?")) {
            stmt.setInt(1, review.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(newReviewText, resultSet.getString("review_text"));
                } else {
                    fail("No rows returned from the database");
                }
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
    }

    @Test
    public void setRatingTest() throws Exception {

        // Κλήση της setReviewText με νέο κείμενο
        float newRating = 9.9f;
        review.setRating(newRating,user.getId());

        // Ελέγχουμε αν το κείμενο της κριτικής έχει αλλάξει σωστά
        assertEquals(newRating, review.getRating());
    }

    @Test
    public void updateRatingInDatabaseTest() throws Exception {
     
        // Ελέγχουμε αν το κείμενο στη βάση έχει αλλάξει σωστά
        float newRating= 8.7f;
        review.setRating(newRating,user.getId());
        try (PreparedStatement stmt = connection.prepareStatement("SELECT rating FROM review WHERE reviewId = ?")) {
            stmt.setInt(1, review.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(newRating, resultSet.getFloat("rating"));
                } else {
                    fail("No rows returned from the database");
                }
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
    }

    @Test
    public void setSpoilerTest() throws Exception {

        // Κλήση της setReviewText με νέο κείμενο
        boolean newSpoiler = true;
        review.setSpoiler(newSpoiler,user.getId());

        // Ελέγχουμε αν το κείμενο της κριτικής έχει αλλάξει σωστά
        assertEquals(newSpoiler, review.isSpoiler());
    }

    @Test
    public void updateSpoilerInDatabaseTest() throws Exception {
       
        // Ελέγχουμε αν το κείμενο στη βάση έχει αλλάξει σωστά
        boolean newSpoiler= false;
        review.setSpoiler(newSpoiler,user.getId());
        try (PreparedStatement stmt = connection.prepareStatement("SELECT spoiler FROM review WHERE reviewId = ?")) {
            stmt.setInt(1, review.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(0, resultSet.getInt("spoiler"));
                } else {
                    fail("No rows returned from the database");
                }
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
    }

    @Test
    public void deleteReviewTest() throws Exception {
      
        // Εκτέλεση της διαγραφής 
        reviewDelete.deleteReview(reviewDelete.getUserId());

        // Έλεγχος αν το reviewId δεν βρίσκεται πλέον στη βάση
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Review WHERE reviewId = ?")) {
            stmt.setInt(1, reviewDelete.getReviewId());
            try (ResultSet resultSet = stmt.executeQuery()) {
                assertFalse(resultSet.next(), "Review still exists in the database");
            }
        }
    }

 @Test
    public void getReviewsByUserAndMovieTest() throws Exception {
        // Εκτελεί τη μέθοδο για να ανακτήσει τις κριτικές
        List<Review> retrievedReviews = Review.getReviewsByUserAndMovie(review.getUserId(),movie.getMd().getId());

       // Aυτό θα ελέγξει αν υπάρχει κάποια κριτική με το ίδιο reviewId στη λίστα retrievedReviews.
        assertTrue(retrievedReviews.stream().anyMatch(r -> r.getReviewId() == review.getReviewId()));
    }

 @Test
    public void getReviewsByUserAndMovieNoMatchTest() throws Exception {
        // Εκτελεί τη μέθοδο για έναν χρήστη και ταινία χωρίς κριτικές
        List<Review> retrievedReviews = Review.getReviewsByUserAndMovie(user.getId(), 999);

        // Ελέγχει αν η λίστα είναι άδεια
        assertTrue(retrievedReviews.isEmpty());
    }
}

