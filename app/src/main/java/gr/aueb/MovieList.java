package gr.aueb;
import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private String listType; // Τύπος λίστας (public ή private)
    private final String creatorId; // ID του δημιουργού της λίστας
    private List<String> movies; // Λίστα των ταινιών

    public MovieList(String listType, String creatorId) {
        this.listType = listType;
        this.creatorId = creatorId;
        this.movies = new ArrayList<>();
    }

    // Προσθήκη ταινίας στη λίστα
    public void addMovie(String movieName) {
        if (!movies.stream().anyMatch(movieName::equalsIgnoreCase)) {
            movies.add(movieName);
            System.out.println(movieName + " added to the list.");
        } else {
            System.out.println("Movie '" + movieName + "' is already in the list.");
        }
    }

    // Αφαίρεση ταινίας από τη λίστα
    public void removeMovie(String movieName) {
           
        if (movies.contains(movieName)) {
            movies.remove(movieName);
            System.out.println(movieName + " removed from the list.");
        } else {
            System.out.println(movieName + " is not in the list.");
        } 
    }

    // Εμφάνιση των ταινιών στη λίστα
    public void displayMovies() {
        System.out.println("Movies in the list: " + movies);
    }
}
