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

//φτιαξε μεθοδο για ελεγχο εγκυροτητας

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
                    System.out.println("Exiting the application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option");
            }
            mainCase(scanner);
        }
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
        System.out.println("Choose your country or press 0 to go back");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 0) {
            return "0";
        } else {
            return keys[choice - 1];
        }
    }

    public static void login(Scanner scanner) throws Exception {
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

    
    /**
     * Handles the main menu of the application, providing options based on the
     * user's status.
     *
     * @param scanner The scanner object for user input.
     * @throws Exception If an exception occurs during execution.
     */
    public static void mainCase(Scanner scanner) throws Exception {
        while (skipStartMenu) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline character
            switch (choice) {
                case 1:
                    boolean flag = true;
                    while (flag) {
                        flag = getAIRecommendation(scanner, flag);
                    }
                    break;
                case 2:
                    flag = true;
                    while (flag) {
                        flag = mainCase2(scanner);
                    }
                    break;
                case 3:
                    mainCase3(scanner);
                    break;
                case 4:
                    mainCase4(scanner);
                    break; 
                case 5: 
                    mainCase5(scanner);
                    break;
                case 6: 
                    System.out.println("Exiting the application.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option");
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
    private static void mainCase2CheckObjectType(Scanner scanner, Object o) throws Exception {
        int choice2;
        do {
            System.out.println(o);
            if (o instanceof Movie) {
                Movie m = (Movie)o;
                boolean flag1 = currentUser.isMovieInWatchlist(m.getMd().getId());
                boolean flag2 = currentUser.isMovieInFavorites(m.getMd().getId());
                displayMovieMenuUser(flag1, flag2);
                choice2 = scanner.nextInt();
                scanner.nextLine();
                movieCase(scanner, choice2, m, flag1, flag2);
            } else {
                Person p = (Person)o;
                displayPersonMenu();
                choice2 = scanner.nextInt();
                scanner.nextLine();
                personCase(scanner, choice2, p);
            }
        } while (choice2 != 0);
    }

    private static void mainCase3(Scanner scanner) throws Exception {
        if(guest) {
            skipStartMenu = false;
        } else {
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
                            Object o = pickUser(users, scanner);
                            if (!o.equals(0)) {
                                //int choice;
                                caseUser(scanner, o);
                            } else
                                break;
                        } else System.out.println("No users found");
                    } while (true);
                }
            } while (!userMessage.equals("0"));
        }
    }

    private static void caseUser(Scanner scanner, Object o) throws Exception {
        int choice;
        do {
            User u = (User)o;
            boolean flag = currentUser.isFollowing(u);
            displayUserMenu(flag);
            choice = scanner.nextInt();
            scanner.nextLine();
            if(choice != 0) {
                caseFriend(scanner, choice, flag, u);
            }
        } while(choice != 0);
    }

    private static Object pickUser(ArrayList<User> users, Scanner scanner) {
        System.out.print("Enter your choice or press 0 to go back ");
        int answer = scanner.nextInt();
        scanner.nextLine(); // consume next line character
        if (answer == 0) {
            return 0;
        } else {
            return users.get(answer - 1);
        }
    }

    private static void caseFriend(Scanner scanner, int choice, boolean flag, User u) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1: 
                caseShowLists(scanner, u, 1);
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
    }

    private static void mainCase4(Scanner scanner) throws Exception {
        if(guest) {
            System.out.println("Exiting the application.");
            System.exit(0);
        } else {
            int choice;
            do {
                displayProfileMenu();
                choice = scanner.nextInt();
                scanner.nextLine();
                caseProfile(scanner, choice);
            } while (choice != 0 && choice != 1);
        } 
    }



    private static void caseProfile(Scanner scanner, int choice) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                currentUser = null;
                skipStartMenu = false;
                break;
            case 2:
                caseShowLists(scanner, currentUser, 2);
                break;
            case 3:
            caseShowLists(scanner, currentUser, 3);
                break;
            case 4: 
                caseLists(scanner);
                break;
            case 5:
                caseReviews(scanner);
                break;
            case 6: 
                caseFollowers(scanner);
                break;
            case 7: 
                caseFollowing(scanner);
                break;
            case 8: 
                caseCountry(scanner);
                break;
            default:
                break;
        }
    }

    private static void caseReviews(Scanner scanner) throws Exception {
        int choice;
        do {
            System.out.println();
            displayYourReviewMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            if(choice != 0) {
                switch (choice) {
                    case 0:
                        break;
                    case 1: 
                        caseUserAllReviews(scanner);
                        break;
                    case 2: 
                        caseDeleteReview(scanner);
                        break;
                    default:
                        break;
                }
            }
        } while (choice != 0);
    }

    private static void caseDeleteReview(Scanner scanner) throws Exception {
        Object r;
        do {
            r = chooseReview(scanner);
            if(r instanceof Review) {
                Review review = (Review)r;
                review.deleteReview(currentUser.getId());
            } else {
                break;
            }
        } while (true);
    }

    private static Object chooseReview(Scanner scanner) throws Exception {
        ArrayList<Review> reviews = currentUser.getAllUserReviewsOrderedByMovieId();
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
            System.out.println("\nChoose a review or press 0 to go back ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if(choice != 0) {
                return reviews.get(choice - 1);
            } else {
                return 0;
            }
            
        } else {
            System.out.println("You have not reviewed any movies! ");
            return 0;
        } 
    }

    private static void caseUserAllReviews(Scanner scanner) throws Exception {
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
                    System.out.println(r.toString());;
                }
            } else {
                System.out.println("You have not reviewed any movies! ");
            } break;
        } while(true);
    }

    private static void caseFollowers(Scanner scanner) throws Exception {
        do {
            System.out.println("\nYour followers: ");
            Object f = chooseFollowerIng(scanner, "Followers");
            if(!f.equals("0")) {
                User follower = (User)f;
                caseUser(scanner, follower);
            } else break;
        } while(true);
    }

    private static void caseFollowing(Scanner scanner) throws Exception {
        do {
            System.out.println("\nUsers you follow: ");
            Object f = chooseFollowerIng(scanner, "Following");
            if(!f.equals("0")) {
                User following = (User)f;
                caseUser(scanner, following);
            } else break;
        } while(true);
    }

    private static void caseCountry(Scanner scanner) throws Exception {
        int choice2;
        do {
            TreeMap<String, String> countries = Country.allCountriesNames(tmdbApiKey);
            System.out.println("Your country: " + countries.get(currentUser.getCountry()) + "\n");
            displayCountryMenu();
            choice2 = scanner.nextInt();
            scanner.nextLine();
            if(choice2 == 1) {
                String newCountry = chooseCountry(scanner);
                if(!newCountry.equals("0")) {
                    currentUser.setCountry(newCountry, currentUser.getPassword());
                }
            } else break;
        } while(true);
    }

    private static void caseLists(Scanner scanner) throws Exception {
        int choice;
        do {
            System.out.println();
            displayListMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            if(choice != 0) {
                switch (choice) {
                    case 0:
                        break;
                    case 1: 
                        caseShowLists(scanner, currentUser, 1); //1 = all lists
                        break;
                    case 2: 
                        caseDeleteList(scanner);
                        break;
                    case 3: 
                        caseCreateList(scanner);
                    default:
                        break;
                }
            }
        } while (choice != 0);
    }

    private static void caseCreateList(Scanner scanner) throws Exception {
        String name;
        String type = "0";
        do {
            System.out.println("\nEnter a name for your list or press 0 to go back");
            name = scanner.nextLine();
            if(!name.equals("0") ) { // and method unique false
                System.out.println("\nChoose list type or 0 to go back");
                displayListTypeMenu();
                int choice = scanner.nextInt();
                scanner.nextLine();
                type = caseListType(choice);
            } else if(!name.equals("0")){ // and method true 
                System.out.println("You already have a list with this name!");
            }
        } while (!name.equals("0") && type.equals("0"));        
        if(!type.equals("0")) {
            MovieList.createList(type, name, currentUser.getId());
        }            
    }

    private static void caseDeleteList(Scanner scanner) throws Exception {
        Object l;
        do {
            l = chooseList(scanner, currentUser, 1);
            if(!l.equals("0")) {
                MovieList list = (MovieList)l;
                list.deleteList(currentUser.getId());
            }
        } while(!l.equals("0"));
    }

    private static void caseShowLists(Scanner scanner, User u, int allLists) throws Exception {
        Object l;
        do {
            l = chooseList(scanner, u, allLists);
            if(!l.equals("0")) {
                int choice;
                do { 
                    MovieList list = (MovieList)l;
                    System.out.println("\n" + list.getListName() + "\n");
                    if(u.equals(currentUser)) {
                        displayListContentMenu(true);
                    } else {
                        displayListContentMenu(false);
                    }
                    choice = scanner.nextInt();
                    scanner.nextLine();
                    if(choice != 0) caseListContent(scanner, choice, list);
                } while(choice != 0);
            } if(allLists == 2 || allLists == 3) break;
         } while(!l.equals("0"));
    }

    private static void caseListContent(Scanner scanner, int choice, MovieList list) throws Exception {
        Map<Integer, String> movies = new HashMap<>();
        movies = list.getMoviesFromList();
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
                    Object o  = pick(scanner, movieIds);
                    if(!o.equals(0)) {
                        Movie m = (Movie)o;
                        mainCase2CheckObjectType(scanner, m);
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
                    Object o  = pick(scanner, movieIds);
                    if(!o.equals(0)) {
                        Movie m = (Movie)o;
                        list.removeMovie(m.getMd().getOriginal_title(),m.getMd().getId(), currentUser.getId()); //and movieID
                    } else break;
                } while (true);
                break;
            default:
                break;
        }
    }

    private static Object chooseFollowerIng(Scanner scanner, String erIng) throws Exception {
        ArrayList<User> foll = new ArrayList<>();
        if(erIng.equals("Followers")) {
            foll = currentUser.getFollowers();
        } else {
            foll = currentUser.getFollowing();
        }
        for (int i = 0; i < foll.size(); i++) {
            System.out.printf("%3d. %s \n", i + 1, foll.get(i).getUsername());
        }
        System.out.println("\nEnter your choice or press 0 to go back ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 0) {
            return "0";
        } else {
            return foll.get(choice - 1);
        }
    }

    private static Object chooseList(Scanner scanner, User user, int allLists) throws Exception {
        ArrayList<MovieList> movieLists = user.getLists();
        if(allLists == 2) {
            for (MovieList m : movieLists) {
                if(m.getListName().equals("watchlist")) {
                    return m;
                }
            }
        }
        if(allLists == 3) {
            for (MovieList m : movieLists) {
                if(m.getListName().equals("favorites")) {
                    return m;
                }
            }
        }
        System.out.println();
        int i = 0;
        for (MovieList m : movieLists) {
             if(user.equals(currentUser)) {
                if(!m.getListName().equals("favorites") && !m.getListName().equals("watchlist")) {
                    i++;
                    System.out.printf("%3d. %s \n", i, m.getListName());
                }
             } else {
                String curentList = m.getListType();
                if(currentUser.isFollowing(user)) {
                    if(curentList.equals("Public") || curentList.equals("Protected")) {
                        i++;
                        System.out.printf("%3d. %s \n", i, m.getListName());
                    }
                } else {
                    if(curentList.equals("Public")) {
                        i++;
                        System.out.printf("%3d. %s \n", i, m.getListName());
                    }
                }
             }
            }
        System.out.println("\nEnter your choice or press 0 to go back ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 0) {
            return "0";
        } else {
            return movieLists.get(choice - 1);
        }
    }

    private static String caseListType(int choice) {
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
     * Handles the user options related to a Person.
     *
     * @param scanner The scanner object for user input.
     * @param choice2 The user's choice.
     * @param o       The object representing a Person.
     */
    public static void personCase(Scanner scanner, int choice2, Person p) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1:
                // Display movies related to the person
                ArrayList<Integer> ids2 = p.getMovieIds();
                ArrayList<String> titles = p.getMovieTitles();
                ArrayList<String> dates = p.getMovieDates();
                ArrayList<Float> popularity = p.getMoviePopularity();
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
                            boolean flag1 = currentUser.isMovieInWatchlist(m.getMd().getId());
                            boolean flag2 = currentUser.isMovieInFavorites(m.getMd().getId());
                            displayMovieMenuUser(flag1, flag2);
                            choice3 = scanner.nextInt();
                            scanner.nextLine();
                            movieCase(scanner, choice3, m, flag1, flag2);
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
     * @param choice The user's choice.
     * @param o       The object representing a Movie.
     */
    public static void movieCase(Scanner scanner, int choice, Movie m, boolean flag1, boolean flag2) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                int choice2;
                do {
                    m.printFullCast();
                    displayFullContributorsMenu();
                    choice2 = scanner.nextInt();
                    scanner.nextLine();
                    fullContributorsCase(scanner, choice2, m);
                } while (choice2 != 0);
                break;
            case 2:
                int choice3;
                do {
                    displayReviewContentMenu();
                    choice3 = scanner.nextInt();
                    scanner.nextLine();
                    reviewMovieCase(scanner, choice3, m);
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
                Object l; //movielist
                do {
                    System.out.println("Your lists");
                    l = chooseList(scanner, currentUser, 1);
                    if (!l.equals("0")) {
                        MovieList list = (MovieList)l;
                        boolean flag = list.containsMovie(m.getMd().getOriginal_title(), m.getMd().getId());
                        if(!flag) {
                            list.addToList(m.getMd().getOriginal_title(), m.getMd().getId(), currentUser.getId());
                            break;
                        } else {
                            System.out.println("Movie already exists in List!");
                            break;
                        }
                    }
                } while (!l.equals("0"));
                break;
            case 6: 
                printBonusContent(m.getMd().getOriginal_title(), m.getMd().getRelease_date());
                System.out.println("\nPress 0 to go back ");
                int choice4 = scanner.nextInt();
                scanner.nextLine();
                while (choice4!=0){
                    System.out.println("Invalid choice. Please enter a valid option ");
                    choice4 = scanner.nextInt();
                }
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option ");
                break;
        }
    }

    private static void reviewMovieCase(Scanner scanner, int choice, Movie m) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                do {
                    System.out.println("All reviews: \n");
                    ArrayList<Review> reviews = MovieDAO.getAllReviewsForMovie(m.getMd().getId());
                    for (Review r : reviews) {
                        System.out.println(r);
                    }
                    System.out.println("Press 1 to delete a review or 0 to go back");
                    int choice2 = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice2) {
                        case 0:
                            break;
                        case 1: 
                            caseDeleteReview(scanner);
                            break;
                        default:
                            break;
                    }
                    break;
                } while (true);
                break;
            case 2: 
                do {
                    System.out.println("Spoiler-free reviews: \n");
                    ArrayList<Review> reviews = MovieDAO.getSpoilerFreeReviewsForMovie(m.getMd().getId());
                    if(!reviews.isEmpty()) {
                        for (Review r : reviews) {
                            System.out.println(r.toString());;
                        }
                    } else {
                        System.out.println("No reviews for this movie!");
                    }   
                    System.out.println("Press 0 to go back");
                    int choice2 = scanner.nextInt();
                    scanner.nextLine();
                    while (choice2 != 0) {
                        System.out.println("Invalid choice. Please enter a valid option ");
                        choice2 = scanner.nextInt();
                        scanner.nextLine();
                    }
                    break;
                } while (true);
                break;
            case 3:
                do {
                    System.out.println("Your reviews: \n");
                    ArrayList<Review> reviews = Review.getReviewsByUserAndMovie(currentUser.getId(), m.getMd().getId());
                    if(!reviews.isEmpty()) {
                        for (Review r : reviews) {
                            System.out.println(r.toString());;
                        }
                    } else {
                        System.out.println("You have not reviewed for this movie!");
                    }   
                    System.out.println("Press 0 to go back");
                    int choice2 = scanner.nextInt();
                    scanner.nextLine();
                    while (choice2 != 0) {
                        System.out.println("Invalid choice. Please enter a valid option ");
                        choice2 = scanner.nextInt();
                        scanner.nextLine();
                    }
                    break;
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
                        choice2 = scanner.nextInt();
                        scanner.nextLine();
                        if (choice2 != 0) {
                            if(choice2 == 1) {
                                spoilers = true;
                            }
                            System.out.println("Enter your rating from a scale 1-10 or press 0 to go back ");
                            rating = scanner.nextFloat();
                            scanner.nextLine();
                            while(rating < 0 || rating > 10) {
                                System.out.println("Invalid choice. Please enter a valid option ");
                                rating = scanner.nextFloat();
                                scanner.nextLine();
                            }
                        }
                    }
                } while (!reviewText.equals("0") && (rating == 0 || choice2 == 0));        
                if(!reviewText.equals("0") && rating != 0 && rating != -1) {
                    Review r = Review.addReview(currentUser.getId(), m.getMd().getId(), reviewText, rating, spoilers, currentUser.getUsername(), m.getMd().getOriginal_title());
                    System.out.println("Your review is published! ");
                }
                break; 
            default:
                break;
        }
    }

    private static void fullContributorsCase(Scanner scanner, int choice, Movie m) throws Exception {
        switch (choice) {
            case 0:
                break;
            case 1:
                ArrayList<String> names = m.getPeopleName();
                ArrayList<String> jobs = m.getPeopleJob();
                ArrayList<Integer> originalIds = m.getPeopleId();
                ArrayList<Float> popularity = m.getPeoplePopularity();
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
                            personCase(scanner, choice4, p);
                        } while (choice4 != 0);
                    } else
                        choice5 = 0;
                } while (choice5 != 0);
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option");
                break;
        }
    }

    private static void mainCase5(Scanner scanner) throws Exception {
        int choice = 0;
        do {
            System.out.println();
            displayChatroomMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            caseChatroom(choice, scanner);
        } while (choice != 0);
    }

    private static void caseChatroom(int choice, Scanner scanner) throws Exception {
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
                        System.out.println("Name is taken");
                        name = "0";
                    }
                } while (name.equals("0"));
                Chatroom.createChatroom(name, currentUser.getId());
                break;
            case 2:
                Chatroom chatroom;
                do {
                    ArrayList<Chatroom> chatrooms = currentUser.getJoinedChatrooms();
                    chatroom = chooseChatroom(scanner, chatrooms);
                    if(chatroom != null) {
                        caseYourChatroom(scanner, chatroom);
                    }
                } while (chatroom != null);
                break;
            case 3 :
                do {
                    ArrayList<Chatroom> chatrooms = currentUser.getNotJoinedChatrooms();
                    chatroom = chooseChatroom(scanner, chatrooms);
                    if(chatroom != null) {
                        System.out.println("Press 1 to join chatroom or 0 to go back");
                        int choice2 = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice2) {
                            case 0:
                                break;
                            case 1: 
                                currentUser.joinChatroom(chatroom.getRoomId());
                                caseYourChatroom(scanner, chatroom);
                                break;
                            default:
                                break;
                        }
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
                                System.out.println("Press 1 to join chatroom or 0 to go back");
                                int choice2 = scanner.nextInt();
                                scanner.nextLine();
                                switch (choice2) {
                                    case 0:
                                        break;
                                    case 1: 
                                        currentUser.joinChatroom(chatroom.getRoomId());
                                        caseYourChatroom(scanner, chatroom);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                caseYourChatroom(scanner, chatroom);
                            } 
                        } else System.out.println("Invalid search");
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
        System.out.println("\nEnter your choice or press 0 to go back ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 0) {
            return null;
        } else {
            return chatrooms.get(choice - 1);
        }
    }

    private static void caseYourChatroom(Scanner scanner, Chatroom chatroom) throws Exception {
        int choice;
        do {
            boolean flag = chatroom.isChatroomCreator(currentUser.getId());
            displayYourChatroomMenu(flag);
            choice = scanner.nextInt();
            scanner.nextLine();
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
                            choice2 = scanner.nextInt();
                            scanner.nextLine();
                            if (choice2 != 0) {
                                if(choice2 == 1) {
                                    spoilers = true;
                                }
                            }
                        }
                    } while (!messageText.equals("0") && choice2 == 0);        
                    if(!messageText.equals("0")) {
                        Message.addMessage(currentUser.getId(), spoilers, messageText, chatroom.getRoomId(), currentUser.getUsername());
                        System.out.println("Your message is published! ");
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
                    if(i == 0) {
                        System.out.println("You have no messages in this chatroom");
                    }  
                    int back;
                    do {
                        System.out.println("\nPress 0 to go back");
                        back = scanner.nextInt();
                    } while (back != 0);

                    break;
                case 3:
                    messages = chatroom.getUnseenMessages(currentUser.getId());
                    if(!messages.isEmpty()) {
                        int choice3;
                        for (Message m : messages) {
                            System.out.println(m);
                        }
                        System.out.println("Press 0 to go back");
                        choice3 = scanner.nextInt();
                        scanner.nextLine();
                        while(choice3 != 0) {
                            System.out.println("Invalid choice. Please enter a valid option");
                        }
                    }
                    break;
                case 4:
                    messages = chatroom.getMessages();
                    if(!messages.isEmpty()) {
                        int choice4;
                        for (Message m : messages) {
                            System.out.println(m);
                        }
                        System.out.println("Press 0 tp go back");
                        choice4 = scanner.nextInt();
                        scanner.nextLine();
                        while(choice4 != 0) {
                            System.out.println("Invalid choice. Please enter a valid option");
                        }
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
                            Object o = pickUser(users, scanner);
                            if (!o.equals(0)) {
                                //int choice;
                                caseUser(scanner, o);
                            } else
                                break;
                        } else if(users.size() == 1) {
                            System.out.println("You are the only member in this chatroom");
                            break;
                        } else {
                            System.out.println("No members");
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
                        System.out.println("Deleting chatroom " + chatroom.getName() + "\n");
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

    private static Message chooseMessage(ArrayList<Message> messages, Scanner scanner) {
        System.out.println();
        int i = 0;
        for (Message m : messages) {
            if(currentUser.getId() == m.getUserId()) {
                i++;
                System.out.println(i + ".");
                System.out.println(m);
            } 
        }
        if(i == 0) {
            System.out.println("You have no messages in this chatroom");
            return null;
        }   
        System.out.println("\nEnter your choice or press 0 to go back ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 0) {
            return null;
        } else {
            return messages.get(choice - 1);
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
     * Displays the menu options for interacting with a movie, including viewing
     * cast and crew, reviews,
     * adding reviews, managing watchlist and seen status, adding to favorites and
     * lists, and getting bonus content.
     */
    private static void displayMovieMenuUser(boolean flag1, boolean flag2) {
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
        
        System.out.println("Enter your choice ");
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
    private static void displayUserMenu(boolean flag) {
        System.out.println();
        System.out.println("0. Back");
        System.out.println("1. See lists");
        if(flag) {
            System.out.println("2. Unfollow");
        } else {
            System.out.println("2. Follow");
        }
        System.out.println("Enter your choice ");
    }

    private static void displayReviewContentMenu() {
        System.out.println("0. Back");
        System.out.println("1. See all reviews");
        System.out.println("2. See all spoiler-free reviews");
        System.out.println("3. Your reviews");
        System.out.println("4. Add review");
        System.out.println("Enter your choice ");
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
        System.out.println("2. Watchlist");
        System.out.println("3. Favorites");
        System.out.println("4. Your lists");
        System.out.println("5. Your reviews");
        System.out.println("6. Your followers");
        System.out.println("7. Users you follow");
        System.out.println("8. Your country");
        System.out.print("Enter your choice ");
    }

    /**
     * Displays the menu options for managing the content of a user's review.
     */
    private static void displayYourReviewMenu() {
        System.out.println("0. Back");
        System.out.println("1. All your reviews");
        System.out.println("2. Delete review");
        System.out.println("Enter your choice ");
    }

    private static void displayListMenu() {
        System.out.println("0. Back");
        System.out.println("1. See your lists");
        System.out.println("2. Delete a list");
        System.out.println("3. Create list");
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
        System.out.println("Enter your choice ");
    }

    private static void displayListTypeMenu() {
        System.out.println("0. Back");
        System.out.println("1. Private");
        System.out.println("2. Public");
        System.out.println("3. Protected");
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
        System.out.println("1. Create a chatroom");
        System.out.println("2. See your chatrooms"); 
        System.out.println("3. New chatrooms");
        System.out.println("4. Search for a chatroom");
        System.out.println("Enter your choice ");
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
        System.out.println("Enter your choice ");
    }

    private static void displayAddMessageTypeMenu() {
        System.out.println("Does your message contain spoilers?");
        System.out.println("0. Back");
        System.out.println("1. YES");
        System.out.println("2. NO");
        System.out.println("Enter your choice ");
    }

    /**
     * Initiates the process of getting AI recommendations based on user
     * preferences.
     *
     * @param scanner The scanner object for user input.
     * @throws Exception If an exception occurs during execution.
     */
    private static boolean getAIRecommendation(Scanner scanner, boolean flag1) throws Exception {
        System.out.println("\nType your preferences for movie recommendations or press 0 to go back");
        String userMessage = scanner.nextLine();
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
        } else flag1 = false;
        return flag1;
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
     * @param year       The release year of the movie, or null if not available.
     */
    public static void printBonusContent(String movieTitle, String year) {
        if (year != null) {
            BonusContent.searchAndPrintVideo(movieTitle + "  movie " + year, "Fun Facts", youtubeApiKey); // edw den tha to vgazei swsta logw toy thematos p legame
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
                } else {
                     System.out.println("No results found"); //FIX!!!!!
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
