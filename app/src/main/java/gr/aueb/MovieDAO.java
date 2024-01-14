package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    // Get All Reviews for a Movie ID
    public static List<Review> getAllReviewsForMovie(int movieId) throws Exception {
        List<Review> reviews = new ArrayList<>();

        try (DB db = new DB();
             Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT * FROM Review WHERE movieId = ?")) {

            stmt.setInt(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("reviewId");
                    int userId = rs.getInt("userId");
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

    // Get Spoiler-Free Reviews for a Movie ID
    public static List<Review> getSpoilerFreeReviewsForMovie(int movieId) throws Exception {
        List<Review> spoilerFreeReviews = new ArrayList<>();

        try (DB db = new DB();
             Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT * FROM Review WHERE movieId = ? AND spoiler = false")) {

            stmt.setInt(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reviewId = rs.getInt("reviewId");
                    int userId = rs.getInt("userId");
                    String reviewText = rs.getString("review_text");
                    float rating = rs.getFloat("rating");
                    boolean spoiler = rs.getBoolean("spoiler");

                    Review review = new Review(reviewId, userId, movieId, reviewText, rating, spoiler);
                    spoilerFreeReviews.add(review);
                }
            }
        }

        return spoilerFreeReviews;
    }
    public static double getAverageRatingForMovie(int movieId) throws Exception {
        double averageRating = 0;
        int totalReviews = 0;
        double totalRating = 0;

        try (DB db = new DB();
             Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     "SELECT rating FROM Review WHERE movieId = ?")) {

            stmt.setInt(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    float rating = rs.getFloat("rating");
                    totalRating += rating;
                    totalReviews++;
                }
            }

            if (totalReviews > 0) {
                averageRating = totalRating / totalReviews;
            }
        }

        return averageRating;
    }
}
