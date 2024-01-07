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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import javax.xml.crypto.Data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
                            pickResult(scanner);
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

    private static void pickResult(Scanner scanner) throws Exception {
        System.out.println("\nType your search.");
        String userMessage = scanner.nextLine();
        userMessage = encodeMovieTitle(userMessage);
        ArrayList<Integer> ids = search(userMessage);
        if(!ids.isEmpty()) {
            System.out.println("Choose your title. \n");
            int answer = scanner.nextInt();
            if(ids.get(answer - 1) > 0) {
                Movie m = new Movie(ids.get(answer - 1), tmdbApiKey);
                System.out.println(m);
            } else {
                Person p = new Person(-ids.get(answer - 1), tmdbApiKey);
                System.out.println(p);
            }
            
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
                    int count = 0;
                    for (int i = 0; i < resultsArray.size(); i++) {
                        JsonObject resultObject = resultsArray.get(i).getAsJsonObject();
                        if (resultObject.has("media_type")) {
                            String mediaType = resultObject.get("media_type").getAsString();
                            if (mediaType.equals("movie")) {
                                count++;
                                String releaseDate = resultObject.get("release_date").getAsString();
                                if(!releaseDate.isEmpty()) {
                                    LocalDate date = LocalDate.parse(releaseDate);
                                    int year = date.getYear();
                                    prints.add(String.format("%s (%s)", resultObject.get("original_title").getAsString(), "Movie, " + year));
                                } else {
                                    prints.add(String.format("%s (%s)", resultObject.get("original_title").getAsString(), "Release date not available"));
                                } 
                                idsList.add(resultObject.get("id").getAsInt());
                                popularity.add(resultObject.get("popularity").getAsFloat());
                            } else if(mediaType.equals("person")) {
                                count++;
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

            if(!prints.isEmpty()) {
                for (int i = 0; i < prints.size(); i++) {
                    String newPrint = String.format("%2d. %s", i + 1, prints.get(i));
                    prints.set(i, newPrint);
                    System.out.println(prints.get(i));
                }
            } else {
                System.out.println("No results found");
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
}