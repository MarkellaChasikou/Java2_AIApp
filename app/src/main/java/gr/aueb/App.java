/*
 * App
 * 
 * Copyright 2024 Bugs Bunny
 */

package gr.aueb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import exercise2_2023_2024_8210070.User;

/**
 * main application class for a movie recommendation system
 * Main application class that serves as the entry point for the movie
 * recommendation system.
 * This class handles user interactions, menu displays, and orchestrates various
 * functionalities.
 * 
 * @version 1.8 14 January 2024
 * @author Νίκος Ραγκούσης 
 */
public class App {
    /** The currently logged-in user */
    private static User currentUser;
    /** The API key for The Movie Database (TMDB) */
    private static String tmdbApiKey;
    /** The API key for ChatGPT */
    private static String chatgptApiKey;
    /** The API key for YouTube */
    private static String youtubeApiKey;
    /** Flag to skip the start menu if the user is logged in or a guest */
    private static boolean skipStartMenu;
    /** Flag to identify if the user is a guest */
    private static boolean guest;

    /**
     * Entry point of the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) throws Exception {
        loadApiKeys(); // Load API keys from files

        Scanner scanner = new Scanner(System.in);

        startCase(scanner);
    }

    /**
     * Handles the start menu options.
     *
     * @param scanner The scanner object for user input.
     */
    public static void startCase(Scanner scanner) throws Exception {
        while (true) {
            displayStartMenu();
            int startChoice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character

            switch (startChoice) {
                case 1:
                    // login
                    skipStartMenu = true;
                    break;
                case 2:
                    // sign up
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
                    System.out.println("Invalid choice. Please enter a valid option");
            }
            mainCase(scanner);
        }
    }

    /**
     * Handles the main menu options.
     *
     * @param scanner The scanner object for user input.
     */
    public static void mainCase(Scanner scanner) throws Exception {

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
                        boolean flag = true;
                        while (flag) {
                            flag = mainCase2(scanner);
                        }
                        break;
                    case 3:
                        break;
                    case 4:
                        System.out.println("Exiting the application.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option");
                }
            }
        }
    }

    /**
     * Handles the main menu of the application, providing options based on the
     * user's status.
     *
     * @param scanner The scanner object for user input.
     * @throws Exception If an exception occurs during execution.
     */
    public static void mainCase(Scanner scanner) throws Exception {

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
                        boolean flag = true;
                        while (flag) {
                            flag = mainCase2(scanner);
                        }
                        break;
                    case 3:
                        break;
                    case 4:
                        System.out.println("Exiting the application.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please enter a valid option");
                }
            }
        }
    }

    /**
     * Handles the second part of the main case, which involves searching for movies
     * or contributors.
     *
     * @param scanner The scanner object for user input.
     * @return true if the user wants to continue searching, false otherwise.
     */
    private static boolean mainCase2(Scanner scanner) throws Exception {
        int choice = 0;
        System.out.println("\nType your search or press 0 to return to the main menu ");
        String userMessage = scanner.nextLine();
        userMessage = encodeMovieTitle(userMessage);

        if (!userMessage.equals("0")) {
            do {
                ArrayList<Integer> ids = search(userMessage, true);
                if (!ids.isEmpty()) {
                    Object o = pick(scanner, ids);
                    if (!o.equals(0)) {
                        mainCase2CheckObjectType(scanner, o);
                    } else
                        break;
                }
            } while (choice == 0);
        } else
            return false;
        return true;
    }

    /**
     * Checks the object type (Movie or Person) and navigates to the corresponding
     * menu.
     *
     * @param scanner The scanner object for user input.
     * @param o       The object representing a Movie or Person.
     */
    public static void mainCase2CheckObjectType(Scanner scanner, Object o) throws Exception {
        int choice2;
        do {
            System.out.println(o);
            if (o instanceof Movie) {
                displayMovieMenu();
                choice2 = scanner.nextInt();
                scanner.nextLine();
                movieCase(scanner, choice2, o);
            } else {
                displayPersonMenu();
                choice2 = scanner.nextInt();
                scanner.nextLine();
                personCase(scanner, choice2, o);
            }
        } while (choice2 != 0);
    }

    /**
     * Handles the user options related to a Person.
     *
     * @param scanner The scanner object for user input.
     * @param choice2 The user's choice.
     * @param o       The object representing a Person.
     */
    public static void personCase(Scanner scanner, int choice2, Object o) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1:
                // Display movies related to the person
                ArrayList<Integer> ids2 = ((Person) o).getMovieIds();
                ArrayList<String> titles = ((Person) o).getMovieTitles();
                ArrayList<String> dates = ((Person) o).getMovieDates();
                ArrayList<Float> popularity = ((Person) o).getMoviePopularity();
                ArrayList<String> prints = new ArrayList<>();
                int choice3;
                int choice4 = 1;
                do {
                    // Display movies with details
                    for (int i = 0; i < ids2.size(); i++) {
                        if (!dates.get(i).isEmpty()) {
                            int year = extractYear(dates.get(i));
                            prints.add(String.format("%s (%d)", titles.get(i), year));
                        } else {
                            prints.add(String.format("%s (%s)", titles.get(i), "Release date not available"));
                        }
                    }
                    sortResultsOnPopul(ids2, prints, popularity, true);
                    Object ob = pick(scanner, ids2);
                    if (ob instanceof Movie) {
                        Movie m = (Movie) ob;
                        System.out.println(m);
                        do {
                            displayMovieMenu();
                            choice3 = scanner.nextInt();
                            scanner.nextLine();
                            movieCase(scanner, choice3, ob);
                        } while (choice3 != 0);
                    } else
                        choice4 = 0;
                } while (choice4 != 0);
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option");
                break;
        }
    }

    /**
     * Handles the user options related to a Movie.
     *
     * @param scanner The scanner object for user input.
     * @param choice2 The user's choice.
     * @param o       The object representing a Movie.
     */
    public static void movieCase(Scanner scanner, int choice2, Object o) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1:
                int choice3;
                do {
                    Movie m = (Movie) o;
                    m.printFullCast();
                    displayFullContributorsMenu();
                    choice3 = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice3) {
                        case 0:
                            break;
                        case 1:
                            ArrayList<String> names = ((Movie) o).getPeopleName();
                            ArrayList<String> jobs = ((Movie) o).getPeopleJob();
                            ArrayList<Integer> originalIds = ((Movie) o).getPeopleId();
                            ArrayList<Float> popularity = ((Movie) o).getPeoplePopularity();
                            ArrayList<String> prints = new ArrayList<>();
                            ArrayList<Integer> ids2 = new ArrayList<>();
                            for (Integer id : originalIds) { // negative values for pick()
                                ids2.add(-id);
                            }
                            int choice4;
                            int choice5 = 1;
                            do {
                                // Display people related to the movie
                                for (int i = 0; i < ids2.size(); i++) {
                                    if (!jobs.get(i).isEmpty()) {
                                        prints.add(String.format("%s (%s)", names.get(i), jobs.get(i)));
                                    } else {
                                        prints.add(String.format("%s (%s)", jobs.get(i),
                                                "Known for department not available"));
                                    }
                                }
                                sortResultsOnPopul(ids2, prints, popularity, true);
                                Object ob = pick(scanner, ids2);
                                if (ob instanceof Person) {
                                    Person p = (Person) ob;
                                    System.out.println(p);
                                    do {
                                        displayPersonMenu();
                                        choice4 = scanner.nextInt();
                                        scanner.nextLine();
                                        personCase(scanner, choice4, ob);
                                    } while (choice4 != 0);
                                } else
                                    choice5 = 0;
                            } while (choice5 != 0);
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option");
                            break;
                    }
                } while (choice3 != 0);
                break;
            default:
                System.out.println("choice2 " + choice2);
                System.out.println("Invalid choice. Please enter a valid option");
                break;
        }
    }

    /**
     * Loads API keys from files.
     */
    private static void loadApiKeys() {
        // Load TMDB, ChatGPT, and YouTube API keys from files
        File tmdbFile = new File("C:\\Users\\Nick\\api_keys\\tmdb_api_key.txt");
        File chatgptFile = new File("C:\\Users\\Nick\\api_keys\\chat_gpt_key_2.txt");
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
            System.err.println("Error reading YouTube API key file.");
            System.exit(1);
        }
    }

    /**
     * Handles the display of the start menu.
     */
    private static void displayStartMenu() {
        System.out.println("\nStart Menu:");
        System.out.println("1. Login");
        System.out.println("2. Sign Up");
        System.out.println("3. Continue as a Guest");
        System.out.println("4. Exit");
        System.out.print("Enter your choice ");
    }

    /**
     * Handles the display of the main menu based on the user's status.
     */
    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Get AI recommendation for a movie");
        System.out.println("2. Search for a movie or movie contributor");
        System.out.println("3. Search for friends");

        if (!guest) {
            System.out.println("4. Your profile");
            System.out.println("5. Create list");
            System.out.println("5. Chatrooms");
            System.out.println("6. Exit");
        } else {
            System.out.println("4. Login");
            System.out.println("5. Exit");
        }
        System.out.print("Enter your choice ");
    }

    /**
     * Displays the menu options for interacting with a movie, including viewing
     * cast and crew, reviews,
     * adding reviews, managing watchlist and seen status, adding to favorites and
     * lists, and getting bonus content.
     */
    private static void displayMovieMenu() {
        System.out.println("0. Back");
        System.out.println("1. See full Cast and Crew");
        System.out.println("2. See reviews");
        System.out.println("3. Add review");
        System.out.println("4. Add to Watchlist");
        System.out.println("5. Add to Seen");
        System.out.println("6. Add to Favourites");
        System.out.println("7. Add to list");
        System.out.println("8. Get Bonus content");
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for selecting whether a review contains spoilers or
     * not.
     */
    private static void displayReviewTypeMenu() {
        System.out.println("Does your review contain spoilers?");
        System.out.println("0. Back");
        System.out.println("1. YES");
        System.out.println("2. NO");
    }

    /**
     * Displays the menu options for obtaining details about a contributor.
     */
    private static void displayFullContributorsMenu() {
        System.out.println("0. Back");
        System.out.println("1. Details for a contributor");
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for obtaining details about a person (assuming a
     * movie contributor).
     */
    private static void displayPersonMenu() {
        System.out.println("0. Back");
        System.out.println("1. Details for a movie");
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for user-specific actions such as choosing a list
     * or following/unfollowing.
     */
    private static void userMenu() {
        System.out.println("0. Back");
        System.out.println("1. Choose a list");
        System.out.println("2. Follow"); // or unfollow
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the profile menu options for a user, including various preferences
     * and statistics.
     */
    private static void displayProfileMenu() {
        System.out.println("\nProfile Menu:");
        System.out.println("0. Back");
        System.out.println("1. Logout");
        System.out.println("2. Your 4 favourite movies");
        System.out.println("3. Your 4 favourite movie contributors");
        System.out.println("4. Your 4 favourite genres");
        System.out.println("5. Watchlist");
        System.out.println("6. Seen");
        System.out.println("7. Favourites");
        System.out.println("8. Your lists");
        System.out.println("9 your reviews");
        System.out.println("10. Your followers");
        System.out.println("11. You follow");
        System.out.println("12. Your country");
        System.out.print("Enter your choice ");
    }

    /**
     * Displays the menu options for managing the content of a user's list.
     */
    private static void displayListContentMenu() {
        System.out.println("0. Back");
        System.out.println("1. Modify");
        System.out.println("2. View details"); // not for genres
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for managing the content of a user's review.
     */
    private static void displayReviewContentMenu() {
        System.out.println("0. Back");
        System.out.println("1. Modify");
        System.out.println("2. Remove");
        System.out.println("3. View");
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for changing a user's country.
     */
    private static void displayCountryMenu() {
        System.out.println("0. Back");
        System.out.println("1. Change country");
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for interacting with chatrooms, including viewing,
     * finding, creating, and managing chatrooms.
     */
    private static void displayChatroomMenu() {
        System.out.println("0. Back");
        System.out.println("1. Your chatrooms");
        System.out.println("2. Find chatrooms");
        System.out.println("3. Create chatroom"); // not sure what happens with members when creating
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for managing the content of a user's chatroom.
     */
    private static void displayYourChatroomMenu() {
        System.out.println("0. Back");
        System.out.println("1. Write a message");
        System.out.println("2. Delete a Message");
        System.out.println("3. Leave chatroom");
        System.out.println("4. Rename chatroom");
        System.out.println("5. Delete chatroom"); // if creator
        System.out.println("6. Add friends"); // not sure if it is a function
        System.out.println("Enter your choice ");
    }

    /**
     * Displays the menu options for finding and interacting with chatrooms.
     */
    private static void displayChatroomFindMenu() {
        System.out.println("0. Back");
        System.out.println("1. Search for a chatroom");
        System.out.println("2. See existing chatrooms");
    }

    /**
     * Initiates the process of getting AI recommendations based on user
     * preferences.
     *
     * @param scanner The scanner object for user input.
     * @throws Exception If an exception occurs during execution.
     */
    private static void getAIRecommendation(Scanner scanner) throws Exception {
        System.out.println("\nType your preferences for movie recommendations or press 0 to go back");
        String userMessage = scanner.nextLine();
        if (!userMessage.equals("0")) {
            boolean flag = false;
            for (int i = 0; i <= 2; i++) {
                String result = AiRecommendation2.testChatCompletions(
                        userMessage + "Don't include summaries and firector names. Include release year",
                        chatgptApiKey);
                ArrayList<String> movieTitles = extractMovieTitles(result);
                if (movieTitles != null) {
                    String title = "1";
                    do {
                        System.out.println(result);
                        flag = true;
                        title = pickRecommendation(scanner, movieTitles);
                        title = encodeMovieTitle(title);
                        if (title != "0") {
                            ArrayList<Integer> ids = search(title, false);
                            Movie m = new Movie(ids.get(0), tmdbApiKey);
                            mainCase2CheckObjectType(scanner, m);
                        }
                    } while (!title.equals("0"));
                    break;
                }
            }
            if (flag == false) {
                System.out.println(
                        "Something went wrong. Try to be more specific and limit your message to movie recommendations.");
            }
        }
    }

    /**
     * Allows the user to pick a movie recommendation from the provided list of
     * titles.
     * 
     * @param scanner The Scanner object for user input.
     * @param titles  The list of movie titles to choose from.
     * @return The selected movie title or "0" to go back.
     * @throws Exception If an error occurs during user input.
     */
    private static String pickRecommendation(Scanner scanner, ArrayList<String> titles) throws Exception {
        System.out.print("Enter your choice or press 0 to go back ");
        int answer = scanner.nextInt();
        scanner.nextLine(); // consume next line character
        if (answer == 0)
            return "0";
        else {
            return titles.get(answer - 1);
        }
    }

    /**
     * Allows the user to pick a movie or person from the provided list of IDs.
     * 
     * @param scanner The Scanner object for user input.
     * @param ids     The list of IDs to choose from.
     * @return Movie or Person object based on the user's choice, or 0 to go back.
     * @throws Exception If an error occurs during user input.
     */
    private static Object pick(Scanner scanner, ArrayList<Integer> ids) throws Exception {
        System.out.print("Enter your choice or press 0 to go back ");
        int answer = scanner.nextInt();
        scanner.nextLine(); // consume next line character
        if (answer == 0)
            return 0;
        if (ids.get(answer - 1) > 0) {
            Movie m = new Movie(ids.get(answer - 1), tmdbApiKey);
            return m;
        } else {
            Person p = new Person(-ids.get(answer - 1), tmdbApiKey);
            return p;
        }
    }

    /**
     * Prints bonus content related to a movie, including Fun Facts, Behind the
     * Scenes, and Interviews.
     * 
     * @param movieTitle The title of the movie.
     * @param year       The release year of the movie, or -1 if not available.
     */
    public static void printBonusContent(String movieTitle, int year) {
        if (year != -1) {
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Fun Facts", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Interviews", youtubeApiKey);
        } else {
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Fun Facts", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Interviews", youtubeApiKey);
        }
    }

    /**
     * Encodes a movie title using UTF-8 encoding.
     * 
     * @param title The original movie title.
     * @return The encoded movie title.
     */
    private static String encodeMovieTitle(String title) {
        try {
            return URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return title;
        }
    }

    /**
     * Searches for movies or persons based on the user's message.
     * 
     * @param userMessage The user's search message.
     * @param flag        A boolean flag indicating whether to print the results.
     * @return The list of IDs representing the search results.
     */
    private static ArrayList<Integer> search(String userMessage, boolean flag) {
        Gson gson = new Gson();
        ArrayList<Integer> idsList = new ArrayList<>();
        ArrayList<String> prints = new ArrayList<>();
        ArrayList<Float> popularity = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.themoviedb.org/3/search/multi?query=" + userMessage
                        + "&include_adult=false&language=en-US&page=1"))
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + tmdbApiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);

            if (jsonObject.has("results") && jsonObject.get("results").isJsonArray()) {
                JsonArray resultsArray = jsonObject.getAsJsonArray("results");

                if (!resultsArray.isEmpty()) {
                    for (int i = 0; i < resultsArray.size(); i++) {
                        JsonObject resultObject = resultsArray.get(i).getAsJsonObject();
                        if (resultObject.has("media_type")) {
                            String mediaType = resultObject.get("media_type").getAsString();
                            if (mediaType.equals("movie")) {
                                String releaseDate = resultObject.get("release_date").getAsString();
                                if (!releaseDate.isEmpty()) {
                                    int year = extractYear(releaseDate);
                                    prints.add(String.format("%s (%s)",
                                            resultObject.get("original_title").getAsString(), "Movie, " + year));
                                } else {
                                    prints.add(
                                            String.format("%s (%s)", resultObject.get("original_title").getAsString(),
                                                    "Release date not available"));
                                }
                                idsList.add(resultObject.get("id").getAsInt());
                                popularity.add(resultObject.get("popularity").getAsFloat());
                            } else if (mediaType.equals("person")) {
                                idsList.add(-resultObject.get("id").getAsInt());
                                popularity.add(resultObject.get("popularity").getAsFloat());
                                String job = resultObject.get("known_for_department").getAsString();
                                if (!job.isEmpty()) {
                                    prints.add(String.format("%s (%s)", resultObject.get("original_name").getAsString(),
                                            job));
                                } else {
                                    prints.add(String.format("%s (%s)", resultObject.get("original_name").getAsString(),
                                            "Known for department not available"));
                                }
                            }
                        }
                    }
                }
            }
            idsList = sortResultsOnPopul(idsList, prints, popularity, flag);

        } catch (IOException e) {
            System.err.println("Check your internet connection!");
            e.printStackTrace();
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return idsList;
    }

    /**
     * Prints the search results.
     * 
     * @param prints The list of formatted search result strings.
     */
    private static void printResults(ArrayList<String> prints) {
        if (!prints.isEmpty()) {
            System.out.println();
            for (int i = 0; i < prints.size(); i++) {
                String newPrint = String.format("%3d. %s", i + 1, prints.get(i));
                prints.set(i, newPrint);
                System.out.println(prints.get(i));
            }
        }
    }

    /**
     * Sorts the search results based on popularity.
     * 
     * @param idsList    The list of IDs representing the search results.
     * @param prints     The list of formatted search result strings.
     * @param popularity The list of popularity values.
     * @param flag       A boolean flag indicating whether to print the results.
     * @return The sorted list of IDs.
     */
    private static ArrayList<Integer> sortResultsOnPopul(ArrayList<Integer> idsList, ArrayList<String> prints,
            ArrayList<Float> popularity, boolean flag) {
        int n = idsList.size();
        if (n >= 2) {
            for (int i = 1; i <= n - 1; i++) {
                for (int j = n - 1; j >= i; j--) {
                    if (popularity.get(j - 1) < popularity.get(j)) {
                        float temp1 = popularity.get(j - 1);
                        popularity.set(j - 1, popularity.get(j));
                        popularity.set(j, temp1);
                        int temp2 = idsList.get(j - 1);
                        idsList.set(j - 1, idsList.get(j));
                        idsList.set(j, temp2);
                        String temp3 = prints.get(j - 1);
                        prints.set(j - 1, prints.get(j));
                        prints.set(j, temp3);
                    }
                }
            }
        }
        if (flag)
            printResults(prints);
        return idsList;
    }

    /**
     * Extracts the year from a date string.
     * 
     * @param d The date string.
     * @return The extracted year.
     */
    private static int extractYear(String d) {
        LocalDate date = LocalDate.parse(d);
        return date.getYear();
    }

    /**
     * Extracts movie titles from a response string.
     * 
     * @param response The response string containing movie titles.
     * @return The list of extracted movie titles.
     */
    public static ArrayList<String> extractMovieTitles(String response) {
        ArrayList<String> movieTitles = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+\\.\\s+\"?([^\",(]+)\"?(?:\\s+\\(\\d{4}\\))?,?.*");
        Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            movieTitles.add(matcher.group(1).trim());
        }
        return movieTitles;
    }
}
