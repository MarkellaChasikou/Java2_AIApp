package gr.aueb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private static String currentUser;
    private static String tmdbApiKey;
    private static String chatgptApiKey;
    private static String youtubeApiKey;
    private static boolean skipStartMenu;
    private static boolean guest;

    public static void main(String[] args) throws Exception {
        loadApiKeys(); // Load API keys from files

        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayStartMenu();

            int startChoice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (startChoice) {
                case 1:
                    //login
                    skipStartMenu = true;
                    break;
                case 2:
                    //sign up
                    skipStartMenu = true;
                    break;
                case 3:
                    // Continue as a guest
                    skipStartMenu = true;
                    guest = true;
                    break;
                case 4:
                    System.out.println("Exiting the application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

            // If the user is logged in or a guest, display the main menu
            if (skipStartMenu) {
                while (true) {
                    displayMainMenu();

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume the newline character

                    switch (choice) {
                        case 1:
                            getAIRecommendation(scanner);
                            break;
                        case 2:
                            searchForMovie(scanner);
                            break;
                        case 3:
                            if(!guest) {
                                //logout
                                skipStartMenu = false;
                            } else {
                                //register
                                skipStartMenu = false;
                            }
                            break;
                        case 4:
                            System.out.println("Exiting the application.");
                            System.exit(0);
                        default:
                            System.out.println("Invalid choice. Please enter a valid option.");
                    }
                }
            }
        }
    }

    private static void loadApiKeys() {
        File tmdbFile = new File("C:\\Users\\Nick\\api_keys\\tmdb_api_key.txt");
        File chatgptFile = new File("C:\\Users\\Nick\\api_keys\\chat_gpt_key.txt");
        File youtubeFile = new File("C:\\Users\\Nick\\api_keys\\youtube_key.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(tmdbFile))) {
            tmdbApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading TMDB API key file.");
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(chatgptFile))) {
            chatgptApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading ChatGPT API key file.");
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(youtubeFile))) {
            youtubeApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading ChatGPT API key file.");
            System.exit(1);
        }
    }

    private static void displayStartMenu() {
        System.out.println("\nStart Menu:");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Continue as a Guest");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Get AI recommendation for a movie");
        System.out.println("2. Search for a movie or movie contributor");
        System.out.println("3. Search for friends");
        
        if(!guest) {
            System.out.println("4. Your profile"); //fix main
            System.out.println("5. Create list");
            System.out.println("5. Chatrooms");
            System.out.println("6. Exit");
        } else {
            System.out.println("4. Login");
        }
        System.out.print("Enter your choice: ");
    }

    private static void displayProfileMenu() {
        System.out.println("\nProfile Menu:");
        System.out.println("1. Logout");
        System.out.println("2. Your 4 favourite movies");
        System.out.println("4. Your 4 favourite movie contributors");
        System.out.println("5. Your 4 favourite genres");
        System.out.println("5. Watchlist");
        System.out.println("6. Seen");
        System.out.println("7. Favourites");
        System.out.println("8. Your lists");
        System.out.println("9. Your followers/following");
        System.out.println("4. Your country");
        System.out.print("Enter your choice: ");
    }
    
    private static void displayMovieMenu() {
        System.out.println("1. See reviews");
        System.out.println("2. Add review");
        System.out.println("3. Add to list");
        System.out.println("4. Get Bonus content");
    }

    private static void displayChatroomMenu() {
        System.out.println("1. Your chatrooms");
        System.out.println("2. Join chatroom");
        System.out.println("3. Create chatroom");
    }

    private static void getAIRecommendation(Scanner scanner) throws Exception {
        System.out.println("\nType your preferences for movie recommendations.");
        String userMessage = scanner.nextLine();
        AiRecommendation2.testChatCompletions(userMessage + " (Only movie titles, no description or other movie details, no apologies for your previous responses or things you can't do as an AI.)", chatgptApiKey);
        System.out.println("\nChoose your title");
        scanner.nextInt();
    }

    private static void searchForMovie(Scanner scanner) throws Exception {
        System.out.println("\nType your search. \n");
        String userMessage = scanner.nextLine();
        userMessage = encodeMovieTitle(userMessage);
        ArrayList<?> ids = Movie.movieSearch(userMessage, tmdbApiKey, "id");
        ArrayList<?> titles = Movie.movieSearch(userMessage, tmdbApiKey, "title");
        ArrayList<?> years = Movie.movieSearch(userMessage, tmdbApiKey, "year");
        System.out.println("\nChoose your title. \n");
        int answer = scanner.nextInt();
        Movie m = new Movie((int)ids.get(answer - 1), tmdbApiKey);
        System.out.println(m);
        /* System.out.println("\nDo you want bonus content for your movie? (yes/no)");
        scanner.nextLine(); // consume the newline character
        String bonusContentChoice = scanner.nextLine();
        if (bonusContentChoice.equals("yes")) {
            String title = (String) titles.get(answer - 1);
            int year = (int) years.get(answer - 1);
            printBonusContent(title, year);
        }*/
    
    }
    
    public static void printBonusContent(String movieTitle, int year) {
        if(year != -1) {
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Fun Facts", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Interviews", youtubeApiKey);
        } else{
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Fun Facts", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Interviews", youtubeApiKey);
        }
    }

    private static String encodeMovieTitle(String title) {
        try {
            return URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return title;
        }
    }
}