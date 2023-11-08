import java.util.Scanner;

public class MovieListTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");


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
        System.out.println();

        // Δημιουργία μιας λίστας ταινιών
        MovieList movieList = new MovieList(listType, creatorId);

        System.out.println("Enter a movie name or 'exit' to quit, 'remove' to remove a movie.");
        System.out.println("Else enter 'ls' to show list.");
        
        while(true) {
            System.out.print("Enter: ");
            String userInput = scanner.nextLine();
                if (userInput.equals("exit")) {
                    break;
                } else if (userInput.equals("remove")) {
                    System.out.print("Enter the movie name to remove: ");
                    String movieToRemove = scanner.nextLine();
                    movieList.RemoveMovie(movieToRemove);
                } else if ( userInput.equals("ls")) {
                    movieList.DisplayMovies();
                } else {
                    movieList.AddMovie(userInput);
                }
        }
        // Εμφάνιση των ταινιών στη λίστα
        movieList.DisplayMovies();
        scanner.close();
    }
}
