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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class App {
// να φτιαξω τη στοιχιση για αποτελεσματα 100+
    private static User currentUser;
    private static String tmdbApiKey;
    private static String chatgptApiKey;
    private static String youtubeApiKey;
    private static boolean skipStartMenu;
    private static boolean guest;

    public static void main(String[] args) throws Exception {
        loadApiKeys(); // Load API keys from files

        Scanner scanner = new Scanner(System.in);

        startCase(scanner);
    }
    

    public static void startCase(Scanner scanner) throws Exception {
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
                    System.out.println("Invalid choice. Please enter a valid option");
            }
            mainCase(scanner);
        }
    }

    public static void mainCase(Scanner scanner) throws Exception  {
        
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

    private static boolean mainCase2(Scanner scanner) throws Exception {
        int choice = 0;
        int choice2;
        System.out.println("\nType your search or press 0 to retun to main menu ");
        String userMessage = scanner.nextLine();
        userMessage = encodeMovieTitle(userMessage);
        
        if(!userMessage.equals("0")) {
            do {
                ArrayList<Integer> ids = search(userMessage);
                if(!ids.isEmpty()) {
                    Object o = pick(scanner, ids);
                    if(!o.equals(0)){
                        System.out.println(o);
                        do {
                            System.out.println(o);
                            if(o instanceof Movie) {
                                displayMovieMenu();
                                // check for input
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
                    } else break;
                }       
            } while(choice == 0);
        } else return false;
        return true;
    }

    public static void personCase(Scanner scanner, int choice2, Object o) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1: 
                ArrayList<Integer> ids2 = ((Person)o).getMovieIds();
                ArrayList<String> titles = ((Person)o).getMovieTitles();
                ArrayList<String> dates = ((Person)o).getMovieDates();
                int choice3;
                int choice4 = 1;
                do {
                    for (int i = 0; i < ids2.size(); i++) {
                        if(!dates.get(i).isEmpty()) {
                            int year = extractYear(dates.get(i));
                            System.out.printf("%2d. %s (%d)\n", i + 1, titles.get(i), year);
                        } else {
                            System.out.printf("%2d. %s (%s)\n", i + 1, titles.get(i), "Release date not available");
                        }
                    }   
                    Object ob = pick(scanner, ids2);
                    if(ob instanceof Movie) {
                        Movie m = (Movie)ob;
                        System.out.println(m);
                        do {
                            displayMovieMenu();
                            choice3 = scanner.nextInt();
                            scanner.nextLine();
                            movieCase(scanner, choice3, ob);
                        } while (choice3 != 0);
                    } else choice4 = 0;
                } while(choice4 != 0 );
                
                
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option");
                break;
        }
    }

    public static void movieCase(Scanner scanner , int choice2, Object o) throws Exception {
        switch (choice2) {
            case 0:
                break;
            case 1: 
                int choice3;
                do {
                    Movie m = (Movie)o;
                    m.printFullCast();
                    displayFullContributorsMenu();
                    choice3 = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice3) {
                        case 0:
                            break;
                        case 1: 
                            ArrayList<String> names = ((Movie)o).getPeopleName();
                            ArrayList<String> jobs = ((Movie)o).getPeopleJob();
                            ArrayList<Integer> originalIds = ((Movie) o).getPeopleId();
                            ArrayList<Integer> ids2 = new ArrayList<>();
                            for (Integer id : originalIds) { //negative values for prick()
                                ids2.add(-id);
                            }
                            int choice4;
                            int choice5 = 1;
                            do {
                                for (int i = 0; i < ids2.size(); i++) {
                                    if(!jobs.get(i).isEmpty()) {
                                        System.out.printf("%2d. %s (%s)\n", i + 1, names.get(i), jobs.get(i));
                                    } else {
                                        System.out.printf("%2d. %s (%s)\n", i + 1, jobs.get(i), "Known for department not available");
                                    }
                                }   
                                Object ob = pick(scanner, ids2);
                                if(ob instanceof Person) {
                                    Person p = (Person)ob;
                                    System.out.println(p);
                                    do {
                                        displayPersonMenu();
                                        choice4 = scanner.nextInt();
                                        scanner.nextLine();
                                        personCase(scanner, choice4, ob);
                                    } while (choice4 != 0);
                                } else choice5 = 0;
                            } while(choice5 != 0);
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a valid option");
                            break;
                    }
                }while(choice3 != 0);
                break;
            default:
                System.out.println("choice2 " + choice2);
                System.out.println("Invalid choice. Please enter a valid option");
                break;
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
        System.out.print("Enter your choice ");
    }

    private static void displayMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Get AI recommendation for a movie");
        System.out.println("2. Search for a movie or movie contributor");
        System.out.println("3. Search for friends");
        
        if(!guest) {
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

    private static void displayReviewTypeMenu() {
        System.out.println("Does your review contain spoilers?");
        System.out.println("0. Back");
        System.out.println("1. YES");
        System.out.println("2. NO");
    }

    private static void displayFullContributorsMenu() {
        System.out.println("0. Back");
        System.out.println("1. Details for a contributor");
        System.out.println("Enter your choice ");
    }

    private static void displayPersonMenu() {
        System.out.println("0. Back");
        System.out.println("1. Details for a movie");
        System.out.println("Enter your choice ");
    }

    private static void userMenu() {
        System.out.println("0. Back");
        System.out.println("1. Choose a list");
        System.out.println("2. Follow"); //or unfollow
        System.out.println("Enter your choice ");
    }

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

    private static void displayListContentMenu() {
        System.out.println("0. Back");
        System.out.println("1. Modify");
        System.out.println("2. View details"); //not for genres
        System.out.println("Enter your choice ");
    }

    private static void displayReviewContentMenu() {
        System.out.println("0. Back");
        System.out.println("1. Modify");
        System.out.println("2. Remove");
        System.out.println("3. View");
        System.out.println("Enter your choice ");
    }

    private static void displayCountryMenu() {
        System.out.println("0. Back");
        System.out.println("1. Change country");
        System.out.println("Enter your choice ");
    }

    private static void displayChatroomMenu() {
        System.out.println("0. Back");
        System.out.println("1. Your chatrooms");
        System.out.println("2. Find chatrooms");
        System.out.println("3. Create chatroom"); //not sure what happens with members when creating
        System.out.println("Enter your choice ");
    }

    private static void displayYourChatroomMenu() {
        System.out.println("0. Back");
        System.out.println("1. Write a message");
        System.out.println("2. Delete a Message");
        System.out.println("3. Leave chatroom");
        System.out.println("4. Rename chatroom");
        System.out.println("5. Delete chatroom"); //if creator
        System.out.println("6. Add friends"); // not sure if it is a function
        System.out.println("Enter your choice ");
    }

    private static void displayChatroomFindMenu() {
        System.out.println("0. Back");
        System.out.println("1. Search for a chatroom");
        System.out.println("2. See existing chatrooms");
    }

    private static void getAIRecommendation(Scanner scanner) throws Exception {
        System.out.println("\nType your preferences for movie recommendations.");
        String userMessage = scanner.nextLine();
        AiRecommendation2.testChatCompletions(userMessage + " (Only movie titles, no description or other movie details, no apologies for your previous responses or things you can't do as an AI.)", chatgptApiKey);
        System.out.println("\nEnter your choice ");
        scanner.nextInt();
    }

    private static Object pick(Scanner scanner, ArrayList<Integer> ids) throws Exception {
        System.out.print("Enter your choice or press 0 to go back ");
        int answer = scanner.nextInt();
        scanner.nextLine(); //consume next line character
        if(answer == 0) return 0;
        if(ids.get(answer - 1) > 0) {
            Movie m = new Movie(ids.get(answer - 1), tmdbApiKey);
            return m;
        } else {
            Person p = new Person(-ids.get(answer - 1), tmdbApiKey);
            return p;
        }
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

    private static ArrayList<Integer> search(String userMessage) {
        Gson gson = new Gson();
        ArrayList<Integer> idsList = new ArrayList<>();
        ArrayList<String> prints = new ArrayList<>();
        ArrayList<Float> popularity = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/search/multi?query="+ userMessage + "&include_adult=false&language=en-US&page=1"))
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
                                if(!releaseDate.isEmpty()) {
                                    int year = extractYear(releaseDate);
                                    prints.add(String.format("%s (%s)", resultObject.get("original_title").getAsString(), "Movie, " + year));
                                } else {
                                    prints.add(String.format("%s (%s)", resultObject.get("original_title").getAsString(), "Release date not available"));
                                } 
                                idsList.add(resultObject.get("id").getAsInt());
                                popularity.add(resultObject.get("popularity").getAsFloat());
                            } else if(mediaType.equals("person")) {
                                idsList.add(-resultObject.get("id").getAsInt());
                                popularity.add(resultObject.get("popularity").getAsFloat());
                                String job = resultObject.get("known_for_department").getAsString();
                                if(!job.isEmpty()) {
                                    prints.add(String.format("%s (%s)", resultObject.get("original_name").getAsString(), job));
                                } else {
                                    prints.add(String.format("%s (%s)", resultObject.get("original_name").getAsString(), "Known for department not available"));
                                }
                            }
                        }
                    }
                }
            }
            idsList = sortResultsOnPopul(idsList, prints, popularity);

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

    private static void printResults(ArrayList<String> prints) {
        if(!prints.isEmpty()) {
            System.out.println();
            for (int i = 0; i < prints.size(); i++) {
                String newPrint = String.format("%2d. %s", i + 1, prints.get(i));
                prints.set(i, newPrint);
                System.out.println(prints.get(i));
            }
        }
    }

    private static ArrayList<Integer> sortResultsOnPopul(ArrayList<Integer> idsList, ArrayList<String> prints, ArrayList<Float> popularity) {
        int n = idsList.size();
        if(n >= 2){
            for (int i = 1; i <= n - 1; i++) {
                for (int j = n - 1; j >= i; j--) {
                    if(popularity.get(j - 1) < popularity.get(j)) {
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
        printResults(prints);
        return idsList;
    }

    private static int extractYear(String d){
        LocalDate date = LocalDate.parse(d);
        int year = date.getYear();
        return year;
    } 
}