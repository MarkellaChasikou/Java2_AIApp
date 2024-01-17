package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Review {
    private final int reviewId;
    private final int userId;
    private final int movieId;
    private String reviewText;
    private float rating;
    private boolean spoiler;
    private final String username;
    private final Date date;
    private final String movieName;

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

    public String getUsername() {
        return username;
    }

    public Date getDate() {
        return date;
    }

    public String getMovieName() {
        return movieName;
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

    // toString method
    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", userId=" + userId +
                ", movieId=" + movieId +
                ", reviewText='" + reviewText + '\'' +
                ", rating=" + rating +
                ", spoiler=" + spoiler +
                ", username='" + username + '\'' +
                ", date=" + date +
                ", movieName='" + movieName + '\'' +
                '}';
    }
}
