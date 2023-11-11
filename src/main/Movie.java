import java.util.ArrayList;
public class Movie {
    
    private final String releaseDate;
    private final String title;
    private final int movieID;
    private final String description;
    //not sure ArrayLists will be used
    private ArrayList <String> ratings = new ArrayList<String>();
    private ArrayList <String> genres = new ArrayList<String>();
    private ArrayList <String> reviews = new ArrayList<String>();
    private ArrayList <String> actors = new ArrayList<String>();
    private ArrayList <String> directors = new ArrayList<String>();
    private ArrayList <String> writers = new ArrayList<String>();
    private ArrayList <String> producers = new ArrayList<String>();
   
   
    //unfinished
    public Movie(int movieID, String title, String releaseDate, String description) {
        this.movieID = movieID;
        this.title = title;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    
    public String getReleaseDate() {
        return releaseDate;
    }

    
    public String getDescription() {
        return description;
    }

    //public String getDetails() {}

    //public String getRecommendation(){}

    //public double getRating(){}

    //public String getAvailability() {}

    //public ?? getBonusContent() {}
    
    //pulic void filterReviewSpoilers
    
}
