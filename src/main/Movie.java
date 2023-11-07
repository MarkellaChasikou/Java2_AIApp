import java.util.ArrayList;
public class Movie {
    private final int movieID;
    private final String description;
    private ArrayList <String> ratings = new ArrayList<String>();
    private ArrayList <String> genres = new ArrayList<String>();
    private ArrayList <String> actors = new ArrayList<String>();

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

    private final String title;
    public String getReleaseDate() {
        return releaseDate;
    }

    private final String releaseDate;
    public String getDescription() {
        return description;
    }


}
