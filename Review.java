public class Review {
    private int reviewID;
    private int userID;
    private int movieID;
    private float rating;
    private String reviewText;
    private boolean isSpoiler;
    public Review(int reviewID, int userID, int movieID, float rating, String reviewTextext, boolean isSpoiler) {
        this.reviewID = reviewID;
        this.userID = userID;
        this.movieID = movieID;
        this.rating = rating;
        this.reviewText = reviewTextext;
        this.isSpoiler = isSpoiler;
    }

//Getter methods
    public int getReviewID() {
        return reviewID;
    }

    public int getUserID() {
        return userID;
    }

    public int getMovieID() {
        return movieID;
    }

    public float getRating() {
        return rating;
    }
    public String GetReviewText() {
        return reviewText;
    }
    public boolean GetIsSpoiler() {
        return isSpoiler;
    }

//Setter methods
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public void SetReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

//Other Methods
    public void editReview(String reviewText) {

    }
    public void checkSpoiler(boolean isSpoiler, String reviewText) {
        if (isSpoiler) {

        } else {

        }
    }
    public void deleteReview() {

    }
}
