package main;
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
    public void AddMovie(String movieName) {
        if (!movies.stream().anyMatch(movieName::equalsIgnoreCase)) {
            movies.add(movieName);
            System.out.println(movieName + " added to the list.");
        } else {
            System.out.println("Movie '" + movieName + "' is already in the list.");
        }
    }

    // Αφαίρεση ταινίας από τη λίστα
    public void RemoveMovie(String movieName) {
        try {   
            if (movies.contains(movieName)) {
                movies.remove(movieName);
                System.out.println(movieName + " removed from the list.");
            } else {
                System.out.println(movieName + " is not in the list.");
                throw new IndexOutOfBoundsException();
            } 
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: Index out of bounds.");
        }
    }

    // Εμφάνιση των ταινιών στη λίστα
    public void DisplayMovies() {
        System.out.println("Movies in the list: " + movies);
    }
}
