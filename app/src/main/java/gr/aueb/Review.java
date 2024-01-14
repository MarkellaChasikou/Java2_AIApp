package gr.aueb;

public class Review {
    private int reviewId;
    private int userId;
    private int movieId;
    private String reviewText;
    private float rating;
    private boolean spoiler;


    public Review(int userId, int movieId, String reviewText, float rating, boolean spoiler) {
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

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
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
                '}';
    }
}
