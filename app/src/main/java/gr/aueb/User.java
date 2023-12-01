package gr.aueb;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class User {

    private int userID;
    private String username;
    private String password;
    private List<String> followers;
    private List<String> following;
    private List<String> favorites;
    private List<String> watchList;
    private List<String> createdList;
    private static List<User> allUsers = new ArrayList<>();
    private static HashMap<String, String> userCredentials = new HashMap<>();

    
    //Constructor
    public User(String username, String password) {
        boolean check = checkUserExistance(username);
        if (check == false) {
            this.userID = allUsers.size() +1 ;
            this.username = username;
            this.password = password;
            this.followers = new ArrayList<>();
            this.following = new ArrayList<>();
            this.favorites = new ArrayList<>();
            this.watchList = new ArrayList<>();
            this.createdList = new ArrayList<>();
            allUsers.add(this);
            userCredentials.put(username, password);
        }
        else {
            System.out.println("Username " + username + " is not available");
        }
    }

    //User toString() method
    @Override
    public String toString() {
        return "\nUserID:" + this.userID + 
                "\nUsername:" + this.username + 
                "\nPassword:" + this.password +
                "\nFollowers:" + this.followers + "total followers:" + followers.size() +
                "\nFollowing:" + this.following + "total following:" + following.size() +
                "\nFavorites:" + this.favorites + 
                "\nWatch List:" + this.watchList + 
                "\nUser Lists:" + this.createdList;
    }

    //Method returns all Users
    public static List<User> getAllUsers() {
        return allUsers;
    }

    //Method returns all User's Credentials
    public static HashMap<String, String> getUsersCredentials() {
        return userCredentials;
    }

    /*Method getUser checks if specific username exists and return the user
        if doesn't exist throws exception*/ 
    public static User getUser(String username) throws UserNotFoundException {
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            } 
        }
            throw new UserNotFoundException("There is no User with username:" +username);
    }

    //Method checks if user exists, if user exists returns true
    public static boolean checkUserExistance(String username) {
        boolean check = false; 
        for (User user: allUsers) {
            if ( user.getUsername() == username) {
                check = true;
            }
        }
        return check;
    }

    //Method to LOG IN
    public void logIn(String username, String password) {
        if (userCredentials.containsKey(username)) {
            String storedPassword = userCredentials.get(username);
            if (storedPassword.equals(password)) {
                System.out.println(username +" you are now logged in FILMBRO!!!");
            }
            else {
                System.out.println ("Password does not match.Try again!");
            }
        }   else {
            System.out.println("User: " + username + " does not exist\nPlease,try again!");
        }
    }
    //Method checks if user exists in usercredentials Hashmap
    public boolean checkLogIn(String username, String password) {
        if (userCredentials.containsKey(username)) {
            String storedPassword = userCredentials.get(username);
            if (storedPassword.equals(password)) 
                return true;
            else 
                return false;
            
        } else {
                return false;
        }
    }

    
    // Setters
    public void setUsername(String current_username, String current_password, String new_username) {
        if (current_username.equals(username) && current_password.equals(password)) {
        this.username = new_username;
        } else {
            System.out.println("Wrong credentials.If you want to change your username "+
             "you have to give the correct username and password");
        }
    }

    public void setPassword(String current_username, String current_password, String new_password) {
        if (current_username.equals(username) && current_password.equals(password)) {
        this.password = new_password;
        } else {
            System.out.println("Wrong credentials.If you want to change your password "+
             "you have to give the correct username and password");
        }
    }

    //Getters
    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public List<String> getFavoriteList() {
        return favorites;
    }

    public List<String> getWatchList() {
        return watchList;
    }

    public List<String> getCreatedList() {
        return createdList;
    }

    //Add to favorites
    public void addFavorite(String movie) {
        favorites.add(movie);
    }
    //Add to watch list
    public void addWatchlist(String movie) {
        watchList.add(movie);
    }
    //Create a list
    public void creatList(String listName) {
        createdList.add(listName);
    }
    //Delete a list
    public void deleteList(String listName) {
        createdList.remove(listName);
    }
    //Follow user
    public void followUser(User user) {
        if (following.contains(user.getUsername()) == false) {
        following.add(user.getUsername());
        user.followers.add(this.getUsername());
        }
        else {
            System.out.println(username + " you are already follow: " + user.getUsername());
        }
    }
   
}
