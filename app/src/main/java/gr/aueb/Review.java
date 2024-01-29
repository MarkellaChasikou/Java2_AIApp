/*
 * Review
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a review written by a user for a particular movie.
 * 
 * Each review includes details such as the user ID, review ID, associated movie
 * ID,
 * review text, rating, spoiler status, username of the reviewer, date of the
 * review,
 * and the name of the reviewed movie. Provides methods to manage and update
 * various aspects of a review,
 * including modifying the review text, adjusting the rating, toggling the
 * spoiler
 * status, adding new reviews to the system, deleting existing reviews, and
 * retrieving
 * reviews by specific user and movie criteria.
 * Additionally, it handles permission
 * checks to ensure that users can only modify their own reviews.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης, Άγγελος Λαγός
 */

public class Review {
    /** The unique identifier for the review. */
    private final int reviewId;
    /** The user ID associated with the review. */
    private final int userId;
    /** The movie ID for which the review is written. */
    private final int movieId;
    /** The text content of the review. */
    private String reviewText;
    /** The rating given to the movie in the review. */
    private float rating;
    /** The spoiler status indicating if the review contains spoilers. */
    private boolean spoiler;
    /** The username of the user who wrote the review. */
    private final String username;
    /** The date when the review was written. */
    private final Date date;
    /** The name of the movie being reviewed. */
    private final String movieName;

    /**
     * Constructs a new Review object with the given parameters.
     * 
     * @param reviewId   The unique identifier for the review.
     * @param userId     The user ID associated with the review.
     * @param movieId    The movie ID for which the review is written.
     * @param reviewText The text content of the review.
     * @param rating     The rating given to the movie in the review.
     * @param spoiler    The spoiler status indicating if the review contains
     *                   spoilers.
     * @param username   The username of the user who wrote the review.
     * @param date       The date when the review was written.
     * @param movieName  The name of the movie being reviewed.
     */
    public Review(int reviewId, int userId, int movieId, String reviewText, float rating, boolean spoiler,
            String username, Date date, String movieName) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.movieId = movieId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.spoiler = spoiler;
        this.username = username;
        this.date = date;
        this.movieName = movieName;
    }

    /**
     * Gets the unique identifier for the review.
     * 
     * @return The review ID.
     */
    public int getReviewId() {
        return reviewId;
    }

    /**
     * 
     * Gets the user ID associated with the review.
     * 
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the movie ID for which the review is written.
     * 
     * @return The movie ID.
     */
    public int getMovieId() {
        return movieId;
    }

    /**
     * Gets the text content of the review.
     * 
     * @return The review text.
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Gets the username of the user who wrote the review.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the date when the review was written.
     * 
     * @return The review date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the name of the movie being reviewed.
     * 
     * @return The movie name.
     */
    public String getMovieName() {
        return movieName;
    }

    /**
     * Sets the review text for the review if the user has the permission to do so.
     * 
     * @param reviewText The new review text.
     * @param userId     The user ID trying to update the review text.
     * @throws Exception If the user does not have permission to update the review
     *                   text.
     */
    public void setReviewText(String reviewText, int userId) throws Exception {
        if (userId == this.userId) {
            this.reviewText = reviewText;
            updateReviewTextInDatabase();
        } else {
            throw new Exception("User does not have permission to update review text.");
        }
    }

    /**
     * Updates the review text in the database.
     * 
     * @throws Exception If there is an issue updating the review text in the
     *                   database.
     */
    private void updateReviewTextInDatabase() throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE Review SET review_text = ? WHERE reviewId = ?")) {

            stmt.setString(1, reviewText);
            stmt.setInt(2, reviewId);
            stmt.executeUpdate();

            System.out.println("Review text updated in the database");
        }
    }

    /**
     * Gets the rating given to the movie in the review.
     * 
     * @return The review rating.
     */
    public float getRating() {
        return rating;
    }

    /**
     * Sets the rating for the review if the user has the permission to do so.
     * 
     * @param rating The new review rating.
     * @param userId The user ID trying to update the review rating.
     * @throws Exception If the user does not have permission to update the review
     *                   rating.
     */
    public void setRating(float rating, int userId) throws Exception {
        if (userId == this.userId) {
            this.rating = rating;
            updateRatingInDatabase();
        } else {
            throw new Exception("User does not have permission to update rating.");
        }
    }

    /**
     * Updates the review rating in the database.
     * 
     * @throws Exception If there is an issue updating the review rating in the
     *                   database.
     */
    private void updateRatingInDatabase() throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE Review SET rating = ? WHERE reviewId = ?")) {

            stmt.setFloat(1, rating);
            stmt.setInt(2, reviewId);
            stmt.executeUpdate();

            System.out.println("Rating updated in the database");
        }
    }

    /**
     * Checks if the review contains spoilers.
     * 
     * @return true if the review contains spoilers, false otherwise.
     */
    public boolean isSpoiler() {
        return spoiler;
    }

    /**
     * Sets the spoiler status for the review if the user has the permission to do
     * so.
     * 
     * @param spoiler The new spoiler status.
     * @param userId  The user ID trying to update the spoiler status.
     * @throws Exception If the user does not have permission to update the spoiler
     *                   status.
     */
    public void setSpoiler(boolean spoiler, int userId) throws Exception {
        if (userId == this.userId) {
            this.spoiler = spoiler;
            updateSpoilerInDatabase();
        } else {
            throw new Exception("User does not have permission to update spoiler status.");
        }
    }

    /**
     * Updates the spoiler status in the database.
     * 
     * @throws Exception If there is an issue updating the spoiler status in the
     *                   database.
     */
    private void updateSpoilerInDatabase() throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE Review SET spoiler = ? WHERE reviewId = ?")) {

            stmt.setBoolean(1, spoiler);
            stmt.setInt(2, reviewId);
            stmt.executeUpdate();

            System.out.println("Spoiler updated in the database");
        }
    }

    /**
     * Adds a new review to the database and returns the corresponding Review
     * object.
     * 
     * @param userId     The user ID creating the new review.
     * @param movieId    The movie ID for which the review is written.
     * @param reviewText The text content of the review.
     * @param rating     The rating given to the movie in the review.
     * @param spoiler    The spoiler status indicating if the review contains
     *                   spoilers.
     * @param username   The username of the user who wrote the review.
     * @param movieName  The name of the movie being reviewed.
     * @return The newly created Review object.
     * @throws Exception If there is an issue adding the review to the database.
     */
    public static Review addReview(int userId, int movieId, String reviewText, float rating, boolean spoiler,
            String username, String movieName) throws Exception {
        int reviewId;

        Date currentDate = new Date();

        try (
                DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement(
                        "INSERT INTO Review(userId, movieId, review_text, rating, spoiler, username, date, movieName) "
                                +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);) {
            stmt1.setInt(1, userId);
            stmt1.setInt(2, movieId);
            stmt1.setString(3, reviewText);
            stmt1.setFloat(4, rating);
            stmt1.setBoolean(5, spoiler);
            stmt1.setString(6, username);
            stmt1.setDate(7, new java.sql.Date(currentDate.getTime()));
            stmt1.setString(8, movieName);
            stmt1.executeUpdate();

            try (ResultSet generatedKeys = stmt1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reviewId = generatedKeys.getInt(1);
                } else {
                    throw new Exception("Failed to retrieve generated reviewId.");
                }
            }
        }

        return new Review(reviewId, userId, movieId, reviewText, rating, spoiler, username, currentDate, movieName);
    }

    /**
     * Deletes the review from the database if the user has the permission to do so.
     * 
     * @param userId The user ID trying to delete the review.
     * @throws Exception If the user does not have permission to delete the review.
     */
    public void deleteReview(int userId) throws Exception {
        if (userId == this.userId) {
            try (DB db = new DB();
                    Connection con = db.getConnection();
                    PreparedStatement stmt = con.prepareStatement(
                            "DELETE FROM Review WHERE reviewId = ? AND userId = ?")) {

                stmt.setInt(1, reviewId);
                stmt.setInt(2, userId);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Review deleted successfully");
                } else {
                    System.out.println("Review not found or you do not have permission to delete it.");
                }
            }
        } else {
            throw new Exception("User does not have permission to delete this review.");
        }
    }

    /**
     * Retrieves reviews by user and movie from the database.
     * 
     * @param userId  The user ID for which reviews are retrieved.
     * @param movieId The movie ID for which reviews are retrieved.
     * @return A list of Review objects matching the criteria.
     * @throws Exception If there is an issue retrieving reviews from the database.
     */
    public static ArrayList<Review> getReviewsByUserAndMovie(int userId, int movieId) throws Exception {
        ArrayList<Review> reviews = new ArrayList<>();

        try (
                DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT * FROM Review WHERE userId = ? AND movieId = ?");) {
            stmt.setInt(1, userId);
            stmt.setInt(2, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("reviewId");
                    String reviewText = rs.getString("review_text");
                    float rating = rs.getFloat("rating");
                    boolean spoiler = rs.getBoolean("spoiler");
                    String username = rs.getString("username");
                    Date date = rs.getDate("date");
                    String movieName = rs.getString("movieName");

                    Review review = new Review(reviewId, userId, movieId, reviewText, rating, spoiler, username, date,
                            movieName);
                    reviews.add(review);
                }
            }
        }

        return reviews;
    }

    @Override
    /**
     * Returns a string representation of the review.
     * 
     * @return A string containing details about the review.
     */
    public String toString() {
        String spoilerStatus = spoiler ? "Yes" : "No";

        return String.format(
                "Username: \"%s\"\nMovie Name: \"%s\"\nSpoiler: %s\nDate: \"%s\"\nRating: \"%s\"\nReview Text: \"%s\"",
                username, movieName, spoilerStatus, date, rating, reviewText);
    }
}
