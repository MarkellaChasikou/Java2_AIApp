import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieList {
    private String listType; // Τύπος λίστας (public ή private)
    private String creatorId; // ID του δημιουργού της λίστας
    private List<String> movies; // Λίστα των ταινιών

    public MovieList(String listType, String creatorId) {
        this.listType = listType;
        this.creatorId = creatorId;
        this.movies = new ArrayList<>();
    }

    // Προσθήκη ταινίας στη λίστα
    public void AddMovie(String movieName) {
        movies.add(movieName);
    }

    // Αφαίρεση ταινίας από τη λίστα
    public void RemoveMovie(String movieName) {
        movies.remove(movieName);
    }

    // Εμφάνιση των ταινιών στη λίστα
    public void DisplayMovies() {
        System.out.println("Movies in the list: " + movies);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String listType;
        while (true) {
            System.out.print("Enter list type (public or private): ");
            listType = scanner.nextLine();
            if (listType.equalsIgnoreCase("public") || listType.equalsIgnoreCase("private")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'public' or 'private'.");
            }
        }

        System.out.print("Enter creator ID: ");
        String creatorId = scanner.nextLine();

        // Δημιουργία μιας λίστας ταινιών
        MovieList movieList = new MovieList(listType, creatorId);

        while (true) {
            System.out.print("Enter a movie name (or 'exit' to quit, 'remove' to remove a movie): ");
            String userInput = scanner.nextLine();

            if (userInput.equals("exit")) {
                break;
            } else if (userInput.equals("remove")) {
                System.out.print("Enter the movie name to remove: ");
                String movieToRemove = scanner.nextLine();
                movieList.RemoveMovie(movieToRemove);
                System.out.println(movieToRemove + " removed from the list.");
            } else {
                movieList.AddMovie(userInput);
                System.out.println(userInput + " added to the list.");
            }
        }

        // Εμφάνιση των ταινιών στη λίστα
        movieList.DisplayMovies();

        scanner.close();
    }
}
