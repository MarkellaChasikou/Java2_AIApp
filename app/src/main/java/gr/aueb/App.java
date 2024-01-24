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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

        handleStartMenu(scanner);
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

    private static int choose(int lowBound, int upBound, Scanner scanner) {
        boolean validInput = false;
        int choice = -1;
        while (!validInput) {
            try {
                choice = scanner.nextInt();
                if (choice < lowBound || choice > upBound) {
                    System.out.print("\nInvalid choice. Please enter a valid option ");
                } else {
                    validInput = true;
                }
            } catch (java.util.InputMismatchException e) {
                System.out.print("\nInvalid choice. Please enter a valid option ");
            } finally {
                scanner.nextLine(); // consume the newline character
            }
        }
        return choice;
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
     * Handles the start menu options.
     *
     * @param scanner The scanner object for user input.
     */
    private static void handleStartMenu(Scanner scanner) throws Exception {
        while (true) {
            displayStartMenu();
            int startChoice = choose(1, 4, scanner);
             switch (startChoice) {
                case 1:
                    login(scanner);
                    guest = false; //check!!!
                    break;
                case 2:
                    signup(scanner);
                    guest = false; //check!!!!
                    break;
                case 3:
                    skipStartMenu = true;
                    guest = true;
                    break;
                case 4:
                    System.out.println("\nExiting the application.");
                    System.exit(0);
            }
            handleMainMenu(scanner);
        }
    }

    private static void login(Scanner scanner) throws Exception {
        String name;
        String password = "0";
        boolean flag = true;
        do {
            do {
                System.out.println("\nEnter your username or press 0 to go back");
                name = scanner.nextLine();
                if(!name.equals("0")) { 
                    System.out.println("Enter your password or press 0 to go back");
                    password = scanner.nextLine();
                } else {
                    flag = true;
                }
            } while (!name.equals("0") && password.equals("0"));        
            if(!password.equals("0") && !name.equals("0")) {
                currentUser = User.login(name, password);
                if(currentUser != null){
                    System.out.println("\nWelcome to Filmbro!");
                    skipStartMenu = true;
                    flag = true;
                } else {
                    System.out.println("\nWrong credentials! Try again\n");
                    flag = false;
                }
            }  
        } while(!flag);     
    }

    private static void signup(Scanner scanner) throws Exception {
        String name;
        String password = "0";
        String country = "0";
        do {
            System.out.println("\nEnter your username or press 0 to go back");
            name = scanner.nextLine();
            if(!name.equals("0") && !User.doesUsernameExist(name)) {
                System.out.println("Enter your password or press 0 to go back");
                password = scanner.nextLine();
                if(!password.equals("0")) country = chooseCountry(scanner);
            } else if(!name.equals("0") && User.doesUsernameExist(name)) { 
                System.out.println("This username is taken!");
            }
        } while (!name.equals("0") && country.equals("0"));        
        if(!password.equals("0")) {
            currentUser = User.register(name, password, country);
            System.out.println("\nWelcome to Filmbro!");
            skipStartMenu = true;
        }            
    }

    private static String chooseCountry(Scanner scanner) {
        TreeMap<String, String> countries = Country.allCountriesNames(tmdbApiKey);
        int i = 0;
        String[] keys = new String[countries.size()];
        System.out.println();
        for (String s : countries.keySet()) {
            i++;
            System.out.printf("%3d. %s (%s)\n", i, s, countries.get(s));
            keys[i - 1] = s;
        }
        System.out.print("Choose your country or press 0 to go back ");
        int choice = choose(0, countries.size(), scanner);
        if(choice == 0) {
            return "0";
        } else {
            return keys[choice - 1];
        }
    }

    /**
     * Handles the display of the main menu based on the user's status.
     */
    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Get AI recommendation for a movie");
        System.out.println("2. Search for a movie or movie contributor");
        if (!guest) {
            System.out.println("3. Search for friends");
            System.out.println("4. Your profile");
            System.out.println("5. Chatrooms");
            System.out.println("6. Exit");
        } else {
            System.out.println("3. Login/Sign up");
            System.out.println("4. Exit");
        }
        System.out.print("Enter your choice ");
    }
    
    /**
     * Handles the main menu of the application, providing options based on the
     * user's status.
     *
     * @param scanner The scanner object for user input.
     * @throws Exception If an exception occurs during execution.
     */
    private static void handleMainMenu(Scanner scanner) throws Exception {
        while (skipStartMenu) {
            displayMainMenu();
            int choice;
            if(!guest) {
                choice = choose(1, 6, scanner);
            } else {
                choice = choose(1, 4, scanner);
            }
            switch (choice) {
                case 1:
                    getAIRecommendation(scanner);
                    break;
                case 2:
                    searchMoviePerson(scanner);
                    break;
                case 3:
                    handleSearchForUserCase(scanner);
                    break;
                case 4:
                    handleMainCase4(scanner);
                    break; 
                case 5: 
                    handleChatroom(scanner);
                    break;
                case 6: 
                    System.out.println("Exiting the application.");
                    System.exit(0);
                default:
                    System.out.print("Invalid choice. Please enter a valid option ");
            }
        }
    }

    /**
     * Initiates the process of getting AI recommendations based on user
     * preferences.
     *
     * @param scanner The scanner object for user input.
     * @throws Exception If an exception occurs during execution.
     */
    private static void getAIRecommendation(Scanner scanner) throws Exception {
        String userMessage;
        do {
            System.out.println("\nType your preferences for movie recommendations or press 0 to go back");
            userMessage = scanner.nextLine();
            if (!userMessage.equals("0")) {
                boolean flag = false;
                for (int i = 0; i <= 2; i++) {
                    String result = AiRecommendation.testChatCompletions(
                            userMessage + "Don't include summaries and firector names. Include release year",
                            chatgptApiKey);
                    ArrayList<String> movieTitles = extractMovieTitles(result);
                    if (movieTitles != null) {
                        String title = "1";
                        do {
                            System.out.println(result);
                            flag = true;
                            title = chooseRecommendation(scanner, movieTitles);
                            title = encodeUserInput(title);
                            if (title != "0") {
                                ArrayList<Integer> ids = search(title, false);
                                Movie m = new Movie(ids.get(0), tmdbApiKey);
                                checkObjectType(scanner, m);
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
        }while(!userMessage.equals("0"));
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
    private static String chooseRecommendation(Scanner scanner, ArrayList<String> titles) throws Exception {
        System.out.print("Enter your choice or press 0 to go back ");
        int answer = choose(0, titles.size(), scanner);
        if (answer == 0)
            return "0";
        else {
            return titles.get(answer - 1);
        }
    }

    /**
     * Extracts movie titles from a response string.
     * 
     * @param response The response string containing movie titles.
     * @return The list of extracted movie titles.
     */
    private static ArrayList<String> extractMovieTitles(String response) {
        ArrayList<String> movieTitles = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+\\.\\s+\"?([^\",(]+)\"?(?:\\s+\\(\\d{4}\\))?,?.*");
        Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            movieTitles.add(matcher.group(1).trim());
        }
        return movieTitles;
    }

    private static String encodeUserInput(String title) {
        try {
            return URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return title;
        }
    }

    /**
     * Handles the second part of the main case, which involves searching for movies
     * or contributors.
     *
     * @param scanner The scanner object for user input.
     * @return true if the user wants to continue searching, false otherwise.
     */
    private static void searchMoviePerson(Scanner scanner) throws Exception {
        String userMessage;
        do {
            System.out.println("\nType your search or press 0 to return to the main menu ");
            userMessage = scanner.nextLine();
            userMessage = encodeUserInput(userMessage);
            if (!userMessage.equals("0")) {
                int choice = 0;
                do {
                    ArrayList<Integer> ids = search(userMessage, true);
                    if (!ids.isEmpty()) {
                        Object o = chooseMoviePerson(scanner, ids);
                        if (!o.equals(0)) {
                            checkObjectType(scanner, o);
                        } else
                            break;
                    } else {
                        System.out.println("\nNo results found!");
                        break;
                    }
                } while (choice == 0);
            }
        } while(!userMessage.equals("0"));
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
            idsList = sortResultsOnPopul(idsList, prints, popularity, flag);
            } 
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
     * Allows the user to pick a movie or person from the provided list of IDs.
     * 
     * @param scanner The Scanner object for user input.
     * @param ids     The list of IDs to choose from.
     * @return Movie or Person object based on the user's choice, or 0 to go back.
     * @throws Exception If an error occurs during user input.
     */
    private static Object chooseMoviePerson(Scanner scanner, ArrayList<Integer> ids) throws Exception {
        System.out.print("Enter your choice or press 0 to go back ");
        int answer = choose(0, ids.size(), scanner);
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
     * Displays the menu options for interacting with a movie, including viewing
     * cast and crew, reviews,
     * adding reviews, managing watchlist and seen status, adding to favorites and
     * lists, and getting bonus content.
     */
    private static void displayMovieMenu(boolean flag1, boolean flag2) {
        System.out.println("0. Back");
        System.out.println("1. See full Cast and Crew");
        if(!guest) {
            System.out.println("2. Reviews");
            if(flag1) {
                System.out.println("3. Remove from Watchlist");
            } else {
                System.out.println("3. Add to Watchlist");
            }
            if(flag2) {
                System.out.println("4. Remove from Favorites");
            } else {
                System.out.println("4. Add to Favorites");
            }
            System.out.println("5. Add to list");
            System.out.println("6. Get Bonus content");
        }
        
        System.out.print("Enter your choice ");
    }

    /**
     * Checks the object type (Movie or Person) and navigates to the corresponding
     * menu.
     *
     * @param scanner The scanner object for user input.
     * @param o       The object representing a Movie or Person.
     */
    private static void checkObjectType(Scanner scanner, Object o) throws Exception {
        int choice2;
        do {
            System.out.println(o);
            if (o instanceof Movie) {
                Movie m = (Movie)o;
                boolean flag1 = true;
                boolean flag2 = true;
                if(!guest) {
                    flag1 = currentUser.isMovieInWatchlist(m.getMd().getId());
                    flag2 = currentUser.isMovieInFavorites(m.getMd().getId());
                }
                displayMovieMenu(flag1, flag2);
                choice2 = choose(0, 6, scanner);
                handleMovieCase(scanner, choice2, m, flag1, flag2);
            } else {
                Person p = (Person)o;
                displayPersonMenu();
                choice2 = choose(0, 1, scanner);
                handlePersonCase(scanner, choice2, p);
            }
        } while (choice2 != 0);
    }

    /**
     * Displays the menu options for obtaining details about a person (assuming a
     * movie contributor).
     */
    private static void displayPersonMenu() {
        System.out.println("0. Back");
        System.out.println("1. Details for a movie");
        System.out.print("Enter your choice ");
    }

    /**
     * Handles the user options related to a Person.
     *
     * @param scanner The scanner object for user input.
     * @param choice2 The user's choice.
     * @param o       The object representing a Person.
     */
    private static void handlePersonCase(Scanner scanner, int choice2, Person p) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1:
                ArrayList<Integer> ids2 = p.getMovieIds();
                ArrayList<String> titles = p.getMovieTitles();
                ArrayList<String> dates = p.getMovieDates();
                ArrayList<Float> popularity = new ArrayList<>(p.getMoviePopularity());
                ArrayList<String> prints = new ArrayList<>();
                int choice3;
                int choice4 = 1;
                do {
                    for (int i = 0; i < ids2.size(); i++) {
                        if (!dates.get(i).isEmpty()) {
                            int year = extractYear(dates.get(i));
                            prints.add(String.format("%s (%d)", titles.get(i), year));
                        } else {
                            prints.add(String.format("%s (%s)", titles.get(i), "Release date not available"));
                        }
                    }
                    sortResultsOnPopul(ids2, prints, popularity, true);
                    Object ob = chooseMoviePerson(scanner, ids2);
                    if (ob instanceof Movie) {
                        Movie m = (Movie) ob;
                        System.out.println(m);
                        do {
                            boolean flag1 = true;
                            boolean flag2 = true;
                            if(!guest) {
                                flag1 = currentUser.isMovieInWatchlist(m.getMd().getId());
                                flag2 = currentUser.isMovieInFavorites(m.getMd().getId());
                            }
                            displayMovieMenu(flag1, flag2);
                            choice3 = choose(0, 6, scanner);
                            handleMovieCase(scanner, choice3, m, flag1, flag2);
                        } while (choice3 != 0);
                    } else
                        choice4 = 0;
                } while (choice4 != 0);
                break;
            default:
                System.out.print("Invalid choice. Please enter a valid option");
                break;
        }
    }

    /**
     * Handles the user options related to a Movie.
     *
     * @param scanner The scanner object for user input.
     * @param choice The user's choice.
     * @param o       The object representing a Movie.
     */
    private static void handleMovieCase(Scanner scanner, int choice, Movie m, boolean flag1, boolean flag2) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                int choice2;
                do {
                    m.printFullCast();
                    displayFullContributorsMenu();
                    choice2 = choose(0, 1, scanner);
                    handleFullContributorsCase(scanner, choice2, m);
                } while (choice2 != 0);
                break;
            case 2:
                int choice3;
                do {
                    displayReviewContentMenu();
                    choice3 = choose(0, 4, scanner);
                    handleReviewMovieCase(scanner, choice3, m);
                } while (choice3 != 0);
                break;
            case 3: 
                if(!flag1) {
                    currentUser.addToWatchlist(m.getMd().getId(), m.getMd().getOriginal_title());
                } else {
                    currentUser.removeFromWatchlist(m.getMd().getId());
                }
                break;
            case 4: 
                if(!flag2) {
                    currentUser.addToFavorites(m.getMd().getId(), m.getMd().getOriginal_title());
                } else {
                    currentUser.removeFromFavorites(m.getMd().getId());
                }
                break;
            case 5: 
                MovieList l;
                do {
                    l = chooseList(scanner, currentUser, 1);
                    if (l != null) {
                        boolean flag = l.containsMovie(m.getMd().getOriginal_title(), m.getMd().getId());
                        if(!flag) {
                            l.addToList(m.getMd().getOriginal_title(), m.getMd().getId(), currentUser.getId());
                            break;
                        } else {
                            System.out.println("Movie already exists in List!");
                            break;
                        }
                    }
                } while (l != null);
                break;
            case 6: 
                int year = 0;
                if (!m.getMd().getRelease_date().isEmpty()) {
                    year = extractYear( m.getMd().getRelease_date()); 
                } 
                printBonusContent(m.getMd().getOriginal_title(), year);
                System.out.print("\nPress 0 to go back ");
                int choice4 = scanner.nextInt();
                scanner.nextLine(); // consume newline character
                while (choice4!=0){
                    System.out.print("Invalid choice. Please enter a valid option ");
                    choice4 = scanner.nextInt();
                }
                break;
            default:
                System.out.print("Invalid choice. Please enter a valid option ");
                break;
        }
    }

    /**
     * Displays the menu options for obtaining details about a contributor.
     */
    private static void displayFullContributorsMenu() {
        System.out.println("0. Back");
        System.out.println("1. Details for a contributor");
        System.out.print("Enter your choice ");
    }

    private static void handleFullContributorsCase(Scanner scanner, int choice, Movie m) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                ArrayList<String> names = m.getPeopleName();
                ArrayList<String> jobs = m.getPeopleJob();
                ArrayList<Integer> originalIds = m.getPeopleId();
                ArrayList<Float> popularity = new ArrayList<>(m.getPeoplePopularity());
                ArrayList<String> prints = new ArrayList<>();
                ArrayList<Integer> ids2 = new ArrayList<>();
                int choice5 = 1;
                for (Integer id : originalIds) { // negative values for pick()
                    ids2.add(-id);
                }
                for (int i = 0; i < ids2.size(); i++) {
                    if (!jobs.get(i).isEmpty()) {
                        prints.add(String.format("%s (%s)", names.get(i), jobs.get(i)));
                    } else {
                        prints.add(String.format("%s (%s)", jobs.get(i),
                                "Known for department not available"));
                    }
                }
                do {
                    sortResultsOnPopul(ids2, prints, popularity, true);
                    Object ob = chooseMoviePerson(scanner, ids2);
                    if (ob instanceof Person) {
                        int choice4;
                        do {
                            Person p = (Person) ob;
                            System.out.println(p);
                            displayPersonMenu();
                            choice4 = choose(0, 1, scanner);
                            handlePersonCase(scanner, choice4, p);
                        } while (choice4 != 0);
                    } else
                        choice5 = 0;
                } while (choice5 != 0);
                break;
            default:
                System.out.print("Invalid choice. Please enter a valid option ");
                break;
        }
    }

    private static void displayReviewContentMenu() {
        System.out.println("0. Back");
        System.out.println("1. See all reviews");
        System.out.println("2. See all spoiler-free reviews");
        System.out.println("3. Your reviews");
        System.out.println("4. Add review");
        System.out.print("Enter your choice ");
    }

    private static void handleReviewMovieCase(Scanner scanner, int choice, Movie m) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                do {
                    ArrayList<Review> reviews = MovieDAO.getAllReviewsForMovie(m.getMd().getId());
                    if(!reviews.isEmpty()) {
                        System.out.println("All reviews: \n");
                        for (Review r : reviews) {
                            System.out.println(r);
                        }
                    } else {
                        System.out.println("No reviews for this movie!");
                    }
                    System.out.println("Press 0 to go back");
                    int choice2 = scanner.nextInt();
                    scanner.nextLine(); // consume newline character
                    while (choice2 != 0) {
                        System.out.print("Invalid choice. Please enter a valid option ");
                        choice2 = scanner.nextInt();
                        scanner.nextLine(); // consume newline character
                    }
                    break;
                } while (true);
                break;
            case 2: 
                do {
                    ArrayList<Review> reviews = MovieDAO.getSpoilerFreeReviewsForMovie(m.getMd().getId());
                    if(!reviews.isEmpty()) {
                        System.out.println("Spoiler-free reviews: \n");
                        for (Review r : reviews) {
                            System.out.println(r.toString());;
                        }
                    } else {
                        System.out.println("No spoiler-free reviews for this movie!");
                    }   
                    System.out.println("Press 0 to go back");
                    int choice2 = scanner.nextInt();
                    scanner.nextLine(); // consume newline character
                    while (choice2 != 0) {
                        System.out.print("Invalid choice. Please enter a valid option ");
                        choice2 = scanner.nextInt();
                        scanner.nextLine(); // consume newline character
                    }
                    break;
                } while (true);
                break;
            case 3:
                do {
                    ArrayList<Review> reviews = Review.getReviewsByUserAndMovie(currentUser.getId(), m.getMd().getId());
                    if(!reviews.isEmpty()) {
                        System.out.println("Your reviews: \n");
                        for (Review r : reviews) {
                            System.out.println(r.toString());;
                        }
                        System.out.print("Press 1 to delete a review or 0 to go back ");
                        int choice2 = choose(0, 1, scanner);
                        switch (choice2) {
                            case 0:
                                break;
                            case 1: 
                                ArrayList<Review> reviews2 = Review.getReviewsByUserAndMovie(currentUser.getId(), m.getMd().getId());
                                handleDeleteReviewCase(scanner, reviews2);
                                break;
                            default:
                                break;
                        }
                    } else {
                        System.out.println("You have not reviewed this movie!");
                        break;
                    }   
                } while (true);
                break;
            case 4: 
                String reviewText;
                float rating = -1;
                boolean spoilers = false;
                int choice2 = 0;
                do {
                    System.out.println("\nWrite your review or press 0 to go back");
                    reviewText = scanner.nextLine();
                    if(!reviewText.equals("0")) {
                        displayAddReviewTypeMenu();
                        choice2 = choose(0, 1, scanner);
                        if (choice2 != 0) {
                            if(choice2 == 1) {
                                spoilers = true;
                            }
                            System.out.print("Enter your rating from a scale 1-10 or press 0 to go back ");
                            rating = scanner.nextFloat();
                            scanner.nextLine(); // consume newline character
                            while(rating < 0 || rating > 10) {
                                System.out.print("Invalid choice. Please enter a valid option ");
                                rating = scanner.nextFloat();
                                scanner.nextLine(); // consume newline character
                            }
                        }
                    }
                } while (!reviewText.equals("0") && (rating == 0 || choice2 == 0));        
                if(!reviewText.equals("0") && rating != 0 && rating != -1) {
                    Review.addReview(currentUser.getId(), m.getMd().getId(), reviewText, rating, spoilers, currentUser.getUsername(), m.getMd().getOriginal_title());
                    System.out.println("Your review is published! ");
                }
                break; 
            default:
                break;
        }
    }

    /**
     * Displays the menu options for selecting whether a review contains spoilers or
     * not.
     */
    private static void displayAddReviewTypeMenu() {
        System.out.println("Does your review contain spoilers?");
        System.out.println("0. Back");
        System.out.println("1. YES");
        System.out.println("2. NO");
        System.out.print("Enter your choice ");
    }

    private static void handleDeleteReviewCase(Scanner scanner, ArrayList<Review> reviews) throws Exception {
        Review r;
        do {
            r = chooseReview(scanner, reviews);
            if(r != null) {
                r.deleteReview(currentUser.getId());
                break;
            } else {
                System.out.println("No reviews to delete");
                break;
            }
        } while (true);
    }

    private static Review chooseReview(Scanner scanner, ArrayList<Review> reviews) throws Exception {
        if(!reviews.isEmpty()) {
            int currrentId = 0;
            int i = 0;
            for (Review r : reviews) {
                i++;
                if(r.getMovieId() != currrentId) {
                    System.out.println("Movie: " + r.getMovieName() + "\n");
                    currrentId = r.getMovieId();
                }
                System.out.println(i + ".");
                System.out.println(r.toString());;
            }
            System.out.print("\nChoose a review or press 0 to go back ");
            int choice = choose(0, reviews.size(), scanner);
            if(choice != 0) {
                return reviews.get(choice - 1);
            } else {
                return null;
            }
        } else {
            return null;
        } 
    }

    /**
     * Prints bonus content related to a movie, including Fun Facts, Behind the
     * Scenes, and Interviews.
     * 
     * @param movieTitle The title of the movie.
     * @param year       The release year of the movie, or null if not available.
     */
    public static void printBonusContent(String movieTitle, int year) {
        if (year != 0) {
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Fun Facts", youtubeApiKey); // edw den tha to vgazei swsta logw toy thematos p legame
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Interviews", youtubeApiKey);
        } else {
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Fun Facts", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Behind the Scenes", youtubeApiKey);
            BonusContent.searchAndPrintVideo(movieTitle + "  movie ", "Interviews", youtubeApiKey);
        }
    }

    private static void handleSearchForUserCase(Scanner scanner) throws Exception {
        String userMessage;
        do {
            System.out.println("\nType your search or press 0 to return to the main menu ");
            userMessage = scanner.nextLine(); 
            if (!userMessage.equals("0")) {
                do {
                    ArrayList<User> users = User.getUsersWithPartialUsername(userMessage);
                    if (!users.isEmpty()) {
                        for (int i = 0; i < users.size(); i++) {
                            System.out.printf("%3d. %s \n", i + 1, users.get(i).getUsername());
                        }
                        User u = chooseUser(users, scanner);
                        if (u != null) {
                            handleUserCase(scanner, u);
                        } else
                            break;
                    } else {
                        System.out.println("No users found");
                        break;
                    }
                } while (true);
            }
        } while (!userMessage.equals("0"));
    }

    private static User chooseUser(ArrayList<User> users, Scanner scanner) {
        System.out.print("Enter your choice or press 0 to go back ");
        int answer = choose(0, users.size(), scanner);
        if (answer == 0) {
            return null;
        } else {
            return users.get(answer - 1);
        }
    }

    /**
     * Displays the menu options for user-specific actions such as choosing a list
     * or following/unfollowing.
     */
    private static void displayUserMenu(boolean flag) {
        System.out.println();
        System.out.println("0. Back");
        System.out.println("1. See lists");
        if(flag) {
            System.out.println("2. Unfollow");
        } else {
            System.out.println("2. Follow");
        }
        System.out.print("Enter your choice ");
    }

    private static void handleUserCase(Scanner scanner, User u) throws Exception {
        int choice;
        do {
            boolean flag = currentUser.isFollowing(u);
            displayUserMenu(flag);
            choice = choose(0, 2, scanner);
            switch (choice) {
                case 0:
                    break;
                case 1: 
                    showListsCase(scanner, u, 1);
                    break;
                case 2: 
                    if(flag) {
                        currentUser.unfollowUser(u.getUsername());
                    } else {
                        currentUser.followUser(u.getUsername());
                    }
                default:
                    break;
            }
        } while(choice != 0);
    }

    private static void showListsCase(Scanner scanner, User u, int allLists) throws Exception {
        MovieList l;
        do {
            l = chooseList(scanner, u, allLists);
            if(l != null) {
                int choice;
                do { 
                    System.out.println("\n" + l.getListName() + "\n");
                    if(u.equals(currentUser)) {
                        displayListContentMenu(true);
                        choice = choose(0, 2, scanner);
                    } else {
                        displayListContentMenu(false);
                        choice = choose(0, 1, scanner);
                    }
                    handleListContentCase(scanner, choice, l);
                } while(choice != 0);
            } 
            if(allLists == 2 || allLists == 3) break;
         } while(l != null);
    }

    /**
     * Displays the menu options for managing the content of a user's list.
     */
    private static void displayListContentMenu(boolean flag) {
        System.out.println("0. Back");
        System.out.println("1. Details for a movie");
        if (flag) {
            System.out.println("2. Remove a movie");
        }
        System.out.print("Enter your choice ");
    }

    private static MovieList chooseList(Scanner scanner, User user, int allLists) throws Exception {
        ArrayList<MovieList> movieLists = user.getLists();
        if(allLists == 2) {
            for (MovieList l : movieLists) {
                if(l.getListName().equals("watchlist")) {
                    return l;
                }
            }
        }
        if(allLists == 3) {
            for (MovieList l : movieLists) {
                if(l.getListName().equals("favorites")) {
                    return l;
                }
            }
        }
        if(movieLists.size() > 2) {
            ArrayList<MovieList> tempLists = new ArrayList<>();
            for (int j = 2; j < movieLists.size(); j++) {
                if(user.equals(currentUser)) {
                    tempLists.add(movieLists.get(j));
                } else {
                    String curentList = movieLists.get(j).getListType();
                    if(currentUser.isFollowing(user)) {
                        if(curentList.equals("Public") || curentList.equals("Protected")) {
                            tempLists.add(movieLists.get(j));
                        }
                    } else {
                        if(curentList.equals("Public")) {
                            tempLists.add(movieLists.get(j));
                            
                        }
                    }
                }
            }
            if(tempLists.isEmpty()) {
                System.out.println("Can't access user's lists!");
                return null;
            } else {
                for (int i = 0; i < tempLists.size(); i++) {
                    System.out.printf("%3d. %s \n", i + 1, tempLists.get(i).getListName());
                }
                System.out.print("Enter your choice or press 0 to go back ");
                int choice = choose(0, tempLists.size(), scanner);
                if(choice == 0) {
                    return null;
                } else {
                    return tempLists.get(choice - 1);
                }
            }
        } else {
            System.out.println("No lists created!");
            return null;
        }  
    }

    private static void handleListContentCase(Scanner scanner, int choice, MovieList list) throws Exception {
        Map<Integer, String> movies = new HashMap<>();
        movies = list.getMoviesFromList();
        if(!movies.isEmpty()) {
            ArrayList<Integer> movieIds = new ArrayList<>();
            ArrayList<String> movieTitles = new ArrayList<>();
            for (int i : movies.keySet()) {
                movieIds.add(i);
                movieTitles.add(movies.get(i));
            }
            switch (choice) {
                case 0:
                    break;
                case 1:
                    System.out.println("See details for a movie ");
                    do {
                        for (int i = 0; i < movies.size(); i++) {
                            System.out.printf("%3d. %s \n", i + 1, movieTitles.get(i));
                        }
                        Object o  = chooseMoviePerson(scanner, movieIds);
                        if(!o.equals(0)) {
                            Movie m = (Movie)o;
                            checkObjectType(scanner, m);
                        } else break;
                    } while (true);
                    break;
                case 2: 
                    System.out.println("Remove a movie ");
                    do {
                        movies = list.getMoviesFromList();
                        movieIds = new ArrayList<>();
                        movieTitles = new ArrayList<>();
                        for (int i : movies.keySet()) {
                            movieIds.add(i);
                            movieTitles.add(movies.get(i));
                        }
                        for (int i = 0; i < movies.size(); i++) {
                            System.out.printf("%3d. %s \n", i + 1, movieTitles.get(i));
                        }
                        Object o  = chooseMoviePerson(scanner, movieIds);
                        if(!o.equals(0)) {
                            Movie m = (Movie)o;
                            list.removeMovie(m.getMd().getOriginal_title(),m.getMd().getId(), currentUser.getId()); //and movieID
                            break;
                        } else break;
                    } while (true);
                    break;
                default:
                    break;
            }
        } else System.out.println("\nList is empty!");
    }

    /**
     * Displays the profile menu options for a user, including various preferences
     * and statistics.
     */
    private static void displayProfileMenu() {
        System.out.println("\nProfile Menu:");
        System.out.println("0. Back");
        System.out.println("1. Logout");
        System.out.println("2. Watchlist");
        System.out.println("3. Favorites");
        System.out.println("4. Your lists");
        System.out.println("5. Your reviews");
        System.out.println("6. Your followers");
        System.out.println("7. Users you follow");
        System.out.println("8. Your country");
        System.out.print("Enter your choice ");
    }

    private static void handleMainCase4(Scanner scanner) throws Exception {
        if(guest) {
            System.out.println("Exiting the application.");
            System.exit(0);
        } else {
            int choice;
            do {
                displayProfileMenu();
                choice = choose(0, 8, scanner);
                handleProfileCase(scanner, choice);
            } while (choice != 0 && choice != 1);
        } 
    }

    private static void handleProfileCase(Scanner scanner, int choice) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                currentUser = null;
                skipStartMenu = false;
                break;
            case 2:
                showListsCase(scanner, currentUser, 2);
                break;
            case 3:
                showListsCase(scanner, currentUser, 3);
                break;
            case 4: 
                handleListsCase(scanner);
                break;
            case 5:
                handleReviewsCase(scanner);
                break;
            case 6: 
                caseFollowers(scanner);
                break;
            case 7: 
                caseFollowing(scanner);
                break;
            case 8: 
                handleCountryCase(scanner);
                break;
            default:
                break;
        }
    }

    private static void displayListMenu() {
        System.out.println("0. Back");
        System.out.println("1. See your lists");
        System.out.println("2. Delete a list");
        System.out.println("3. Create list");
        System.out.print("Enter your choice ");
    }

    private static void handleListsCase(Scanner scanner) throws Exception {
        int choice;
        do {
            System.out.println();
            displayListMenu();
            choice = choose(0, 3, scanner);
            if(choice != 0) {
                switch (choice) {
                    case 0:
                        break;
                    case 1: 
                        showListsCase(scanner, currentUser, 1); //1 = all lists
                        break;
                    case 2: 
                        deleteListCase(scanner);
                        break;
                    case 3: 
                        createListCase(scanner);
                    default:
                        break;
                }
            }
        } while (choice != 0);
    }

    private static void deleteListCase(Scanner scanner) throws Exception {
        MovieList l;
        do {
            l = chooseList(scanner, currentUser, 1);
            if(l != null) {
                l.deleteList(currentUser.getId());
            }
        } while(l != null);
    }

    private static void displayListTypeMenu() {
        System.out.println("0. Back");
        System.out.println("1. Private");
        System.out.println("2. Public");
        System.out.println("3. Protected");
        System.out.print("Enter your choice ");
    }

    private static void createListCase(Scanner scanner) throws Exception {
        String name;
        String type = "0";
        do {
            System.out.println("\nEnter a name for your list or press 0 to go back");
            name = scanner.nextLine();
            if(!name.equals("0") ) { // and method unique false
                System.out.println("\nChoose list type or 0 to go back");
                displayListTypeMenu();
                int choice = choose(0, 3, scanner);
                type = handleListTypeCase(choice);
            } else if(!name.equals("0")){ // and method true 
                System.out.println("You already have a list with this name!");
            }
        } while (!name.equals("0") && type.equals("0"));        
        if(!type.equals("0")) {
            MovieList.createList(type, name, currentUser.getId());
        }            
    }

    private static String handleListTypeCase(int choice) {
        switch (choice) {
            case 0:
                return "0";
            case 1: 
                return "Private";
            case 2:
                return "Public";
            case 3:
                return "Protected";
            default:
                return "unreachable";
        }
    }

    /**
     * Displays the menu options for managing the content of a user's review.
     */
    private static void displayYourReviewMenu() {
        System.out.println("0. Back");
        System.out.println("1. All your reviews");
        System.out.println("2. Delete review");
        System.out.print("Enter your choice ");
    }

    private static void handleReviewsCase(Scanner scanner) throws Exception {
        int choice;
        do {
            System.out.println();
            displayYourReviewMenu();
            choice = choose(0, 2, scanner);
            if(choice != 0) {
                switch (choice) {
                    case 0:
                        break;
                    case 1: 
                        handleUserAllReviews(scanner);
                        break;
                    case 2: 
                        ArrayList<Review> reviews = currentUser.getAllUserReviewsOrderedByMovieId();
                        handleDeleteReviewCase(scanner, reviews);
                        break;
                    default:
                        break;
                }
            }
        } while (choice != 0);
    }

    private static void handleUserAllReviews(Scanner scanner) throws Exception {
        do {
            ArrayList<Review> reviews = currentUser.getAllUserReviewsOrderedByMovieId(); 
            if(!reviews.isEmpty()) {
                System.out.println("\nYour reviews: \n");
                int currrentId = 0;
                for (Review r : reviews) {
                    if(r.getMovieId() != currrentId) {
                        System.out.println("Movie: " + r.getMovieName() + "\n");
                        currrentId = r.getMovieId();
                    }
                    System.out.println(r);
                }
            } else {
                System.out.println("You have not reviewed any movies! ");
            } break;
        } while(true);
    }

    private static void caseFollowers(Scanner scanner) throws Exception {
        do {
            System.out.println("\nYour followers: ");
            User f = chooseFollowerIng(scanner, "Followers");
            if(f != null) {
                handleUserCase(scanner, f);
            } else break;
        } while(true);
    }

    private static void caseFollowing(Scanner scanner) throws Exception {
        do {
            System.out.println("\nUsers you follow: ");
            User f = chooseFollowerIng(scanner, "Following");
            if(f != null) {
                handleUserCase(scanner, f);
            } else break;
        } while(true);
    }

    private static User chooseFollowerIng(Scanner scanner, String erIng) throws Exception {
        ArrayList<User> foll = new ArrayList<>();
        if(erIng.equals("Followers")) {
            foll = currentUser.getFollowers();
            if(foll.isEmpty()) {
                System.out.println("You have 0 followers!");
                return null;
            }
        } else {
            foll = currentUser.getFollowing();
            if(foll.isEmpty()) {
                System.out.println("You don't follow any users!");
                return null;
            }
        }
        for (int i = 0; i < foll.size(); i++) {
            System.out.printf("%3d. %s \n", i + 1, foll.get(i).getUsername());
        }
        System.out.print("\nEnter your choice or press 0 to go back ");
        int choice = choose(0, foll.size(), scanner);
        if(choice == 0) {
            return null;
        } else {
            return foll.get(choice - 1);
        }
    }

    /**
     * Displays the menu options for changing a user's country.
     */
    private static void displayCountryMenu() {
        System.out.println("0. Back");
        System.out.println("1. Change country");
        System.out.print("Enter your choice ");
    }

    private static void handleCountryCase(Scanner scanner) throws Exception {
        int choice;
        do {
            TreeMap<String, String> countries = Country.allCountriesNames(tmdbApiKey);
            System.out.println("\nYour country: " + countries.get(currentUser.getCountry()) + "\n");
            displayCountryMenu();
            choice = choose(0, countries.size(), scanner);
            if(choice == 1) {
                String newCountry = chooseCountry(scanner);
                if(!newCountry.equals("0")) {
                    currentUser.setCountry(newCountry, currentUser.getPassword());
                }
            } else break;
        } while(true);
    }

    /**
     * Displays the menu options for interacting with chatrooms, including viewing,
     * finding, creating, and managing chatrooms.
     */
    private static void displayChatroomMenu() {
        System.out.println("0. Back");
        System.out.println("1. Create a chatroom");
        System.out.println("2. See your chatrooms"); 
        System.out.println("3. New chatrooms");
        System.out.println("4. Search for a chatroom");
        System.out.print("Enter your choice ");
    }

    private static void handleChatroom(Scanner scanner) throws Exception {
        int choice = 0;
        do {
            System.out.println();
            displayChatroomMenu();
            choice = choose(0, 4, scanner);
            handleChatroomCase(choice, scanner);
        } while (choice != 0);
    }

    private static void handleChatroomCase(int choice, Scanner scanner) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1: 
                String name;
                do {
                    System.out.println("Enter a name for your chatroom or press 0 to go back"); 
                    name = scanner.nextLine();
                    boolean flag = Chatroom.isNameUnique(name);
                    if(!flag) {
                        System.out.println("A chatroom with this name already exists!");
                        name = "0";
                    }
                } while (name.equals("0"));
                Chatroom.createChatroom(name, currentUser.getId());
                break;
            case 2:
                Chatroom chatroom;
                do {
                    ArrayList<Chatroom> chatrooms = currentUser.getJoinedChatrooms();
                    if(!chatrooms.isEmpty()) {
                        chatroom = chooseChatroom(scanner, chatrooms);
                        if(chatroom != null) {
                            handleYourChatroomCase(scanner, chatroom);
                        }
                    } else {
                        System.out.println("You have not joined any chatrooms!");
                        break;
                    }
                } while (chatroom != null);
                break;
            case 3 :
                do {
                    ArrayList<Chatroom> chatrooms = currentUser.getNotJoinedChatrooms();
                    if(!chatrooms.isEmpty()) {
                        chatroom = chooseChatroom(scanner, chatrooms);
                        if(chatroom != null) {
                            System.out.print("Press 1 to join chatroom or 0 to go back ");
                            int choice2 = choose(0, 1, scanner);
                            switch (choice2) {
                                case 0:
                                    break;
                                case 1: 
                                    currentUser.joinChatroom(chatroom.getRoomId());
                                    handleYourChatroomCase(scanner, chatroom);
                                    break;
                                default:
                                    break;
                            }
                        } 
                    } else {
                        System.out.println("\nYou have joined all existing chatrooms!");
                        break;
                    }
                    
                } while (chatroom != null);
                break;
            case 4:
                String chatroomName;
                do {
                    System.out.println("Type your search or press 0 to go back");
                    chatroomName = scanner.nextLine();
                    if(!chatroomName.equals("0")) {
                        chatroom = Chatroom.getChatroomByName(chatroomName);
                        if(chatroom != null) {
                            boolean flag = chatroom.isUserInChatroom(currentUser.getId());
                            if(!flag) {
                                System.out.print("Press 1 to join chatroom or 0 to go back ");
                                int choice2 = choose(0, 1, scanner);
                                switch (choice2) {
                                    case 0:
                                        break;
                                    case 1: 
                                        currentUser.joinChatroom(chatroom.getRoomId());
                                        handleYourChatroomCase(scanner, chatroom);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                handleYourChatroomCase(scanner, chatroom);
                            } 
                        } else System.out.println("No results found!");
                    }
                } while (!chatroomName.equals("0"));
                break;
            default:
                break;
        }
    }

    private static Chatroom chooseChatroom(Scanner scanner, ArrayList<Chatroom> chatrooms) throws Exception {
        System.out.println();
        int i = 0;
        for (Chatroom c : chatrooms) {
            i++;
            System.out.printf("%3d. %s \n", i, c.getName());
        }   
        System.out.print("\nEnter your choice or press 0 to go back ");
        int choice = choose(0, chatrooms.size(), scanner);
        if(choice == 0) {
            return null;
        } else {
            return chatrooms.get(choice - 1);
        }
    }

    /**
     * Displays the menu options for managing the content of a user's chatroom.
     */
    private static void displayYourChatroomMenu(boolean flag) {
        System.out.println("0. Back");
        System.out.println("1. Add a message");
        System.out.println("2. Your messages");
        System.out.println("3. See unread messages");
        System.out.println("4. See all messages");
        System.out.println("5. See chatroom members");
        System.out.println("6. Delete a message");
        if (flag) {
            System.out.println("7. Delete chatroom"); 
        } else {
            System.out.println("7. Leave chatroom");
        }
        System.out.print("Enter your choice ");
    }

    private static void handleYourChatroomCase(Scanner scanner, Chatroom chatroom) throws Exception {
        int choice;
        do {
            boolean flag = chatroom.isChatroomCreator(currentUser.getId());
            displayYourChatroomMenu(flag);
            choice = choose(0, 7, scanner);
            switch (choice) {
                case 0:
                    break;
                case 1 :
                    String messageText;
                    boolean spoilers = false;
                    int choice2 = 0;
                    do {
                        System.out.println("\nWrite your message or press 0 to go back");
                        messageText = scanner.nextLine();
                        if(!messageText.equals("0")) {
                            displayAddMessageTypeMenu();
                            choice2 = choose(0, 2, scanner);
                            if (choice2 != 0) {
                                if(choice2 == 1) {
                                    spoilers = true;
                                }
                            }
                        }
                    } while (!messageText.equals("0") && choice2 == 0);        
                    if(!messageText.equals("0")) {
                        Message.addMessage(currentUser.getId(), spoilers, messageText, chatroom.getRoomId(), currentUser.getUsername());
                        System.out.println("Your message is published!");
                    }
                    break;
                case 2:
                    ArrayList<Message> messages = chatroom.getMessages();
                    System.out.println();
                    int i = 0;
                    for (Message m : messages) {
                        if(currentUser.getId() == m.getUserId()) {
                            i++;
                            System.out.println(i + ".");
                            System.out.println(m);
                        } 
                    }
                    int back = 1;
                    if(i == 0) {
                        System.out.println("You have no messages in this chatroom");
                        back = 0;
                    }  
                    while (back != 0) {
                        System.out.println("\nPress 0 to go back");
                        back = scanner.nextInt();
                        scanner.nextLine(); // consume newline character
                    }
                    break;
                case 3:
                    messages = chatroom.getUnseenMessages(currentUser.getId());
                    if(!messages.isEmpty()) {
                        for (Message m : messages) {
                            System.out.println(m);
                        }
                        int back2 = 1;
                        while (back2 != 0) {
                            System.out.println("\nPress 0 to go back");
                            back = scanner.nextInt();
                            scanner.nextLine(); // consume newline character
                        }
                    } else {
                        System.out.println("You have no new messages!");
                    }
                    break;
                case 4:
                    messages = chatroom.getMessages();
                    if(!messages.isEmpty()) {
                        for (Message m : messages) {
                            System.out.println(m);
                        }
                        int back3 = 1;
                        while (back3 != 0) {
                            System.out.println("\nPress 0 to go back");
                            back = scanner.nextInt();
                            scanner.nextLine(); // consume newline character
                        }
                    } else {
                        System.out.println("There are no messages!");
                    }
                    break;
                case 5:
                    ArrayList<User> users = chatroom.showChatroomMembers();
                    do {
                        if (users.size() > 1) {
                            int j = 0;
                            for (User u : users) {
                                if(u.getId() != currentUser.getId()) {
                                    System.out.printf("%3d. %s \n", j + 1, u.getUsername());
                                }
                            }
                            User u = chooseUser(users, scanner);
                            if (u != null) {
                                handleUserCase(scanner, u);
                            } else
                                break;
                        } else if(users.size() == 1) {
                            System.out.println("You are the only member in this chatroom!");
                            break;
                        } else {
                            System.out.println("No members!");
                            break;
                        }
                    } while (true);
                    break;
                case 6:
                    messages = chatroom.getMessages();
                    Message message;
                    do {
                        message = chooseMessage(messages, scanner);
                        if(message != null) {
                            message.deleteMessage(currentUser.getId());
                            break;
                        }
                    } while (message != null);
                    break;
                case 7:
                    if(flag) {
                        currentUser.deleteChatroom(chatroom.getRoomId());
                        choice = 0;
                    } else {
                        currentUser.leaveChatroom(chatroom.getRoomId());
                        System.out.println("Leaving chatroom " + chatroom.getName() + "\n");
                        choice = 0;
                    }
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }

    private static void displayAddMessageTypeMenu() {
        System.out.println("Does your message contain spoilers?");
        System.out.println("0. Back");
        System.out.println("1. YES");
        System.out.println("2. NO");
        System.out.print("Enter your choice ");
    }

    private static Message chooseMessage(ArrayList<Message> messages, Scanner scanner) {
        ArrayList<Message> tempMessages = new ArrayList<>();
        for (Message message : messages) {
            if(currentUser.getId() == message.getUserId()) {
                tempMessages.add(message);
            }
        }
        if(!tempMessages.isEmpty()) {
            System.out.println();
            int i = 0;
            for (Message m : messages) {
                i++;
                System.out.println(i + ".");
                System.out.println(m);
            }
            System.out.print("\nEnter your choice or press 0 to go back ");
            int choice = choose(0, tempMessages.size(), scanner);
            if(choice == 0) {
                return null;
            } else {
                return messages.get(choice - 1);
            }
        } else {
            System.out.println("You have no messages in this chatroom!");
            return null;
        }
    }
}
