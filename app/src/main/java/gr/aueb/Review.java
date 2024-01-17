package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Review {
    private final int reviewId;
    private final int userId;
    private final int movieId;
    private String reviewText;
    private float rating;
    private boolean spoiler;

    public Review(int reviewId, int userId, int movieId, String reviewText, float rating, boolean spoiler) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.movieId = movieId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.spoiler = spoiler;
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText, int userId) throws Exception {
        if (userId == this.userId) {
            this.reviewText = reviewText;
            updateReviewTextInDatabase();
        } else {
            throw new Exception("User does not have permission to update review text.");
        }
    }

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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating, int userId) throws Exception {
        if (userId == this.userId) {
            this.rating = rating;
            updateRatingInDatabase();
        } else {
            throw new Exception("User does not have permission to update rating.");
        }
    }

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

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler, int userId) throws Exception {
        if (userId == this.userId) {
            this.spoiler = spoiler;
            updateSpoilerInDatabase();
        } else {
            throw new Exception("User does not have permission to update spoiler status.");
        }
    }

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

    public static Review addReview(int userId, int movieId, String reviewText, float rating, boolean spoiler)
            throws Exception {
        int reviewId;

        try (
                DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement(
                        "INSERT INTO Review(userId, movieId, review_text, rating, spoiler) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);) {
            stmt1.setInt(1, userId);
            stmt1.setInt(2, movieId);
            stmt1.setString(3, reviewText);
            stmt1.setFloat(4, rating);
            stmt1.setBoolean(5, spoiler);
            stmt1.executeUpdate();

            try (ResultSet generatedKeys = stmt1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reviewId = generatedKeys.getInt(1);
                } else {
                    throw new Exception("Failed to retrieve generated reviewId.");
                }
            }
        }

        return new Review(reviewId, userId, movieId, reviewText, rating, spoiler);
    }

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

                    Review review = new Review(reviewId, userId, movieId, reviewText, rating, spoiler);
                    reviews.add(review);
                }
            }
        }

        return reviews;
    }

    // toString method
    @Override
    public String toString() {
        return 
            "Author: " + //username
            "Rating: " + this.getRating()
            + this.getReviewText() 
            + "\n\n";
    }
}
