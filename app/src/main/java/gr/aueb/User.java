package gr.aueb;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class User {
    private final int id;
    private String username;
    private String password;
    private String country;

    // Constructor
    public User(int id, String username, String password, String country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.country = country;
    }

    // Setters

    public void setUsername(String newUsername, String currentPassword) throws Exception {
        verifyPassword(currentPassword);
        updateUsername(newUsername, currentPassword);
    }

    public void setPassword(String newPassword, String currentPassword) throws Exception {
        verifyPassword(currentPassword);
        updatePassword(newPassword, currentPassword);
    }

    public void setCountry(String newCountry, String currentPassword) throws Exception {
        verifyPassword(currentPassword);
        updateCountry(newCountry, currentPassword);
    }

    // Updates with password verification
    private void updateUsername(String newUsername, String currentPassword) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "UPDATE appuser SET username = ? WHERE userId = ?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, newUsername);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                this.username = newUsername;
                System.out.println("Username updated successfully.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void updatePassword(String newPassword, String currentPassword) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "UPDATE appuser SET pass_word = ? WHERE userId = ?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, newPassword);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                this.password = newPassword;
                System.out.println("Password updated successfully.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void updateCountry(String newCountry, String currentPassword) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "UPDATE appuser SET country = ? WHERE userId = ?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, newCountry);
                stmt.setInt(2, id);
                stmt.executeUpdate();
                this.country = newCountry;
                System.out.println("Country updated successfully.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void verifyPassword(String enteredPassword) throws Exception {
        if (!enteredPassword.equals(this.password)) {
            throw new Exception("Incorrect password. Operation aborted.");
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    // Login Method
    public static User login(String username, String password) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT * FROM AppUser WHERE username=? AND pass_word=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            int userId = rs.getInt("userId");
            String country = rs.getString("country");

            return new User(userId, username, password, country);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Register Method
    public static User register(String username, String password, String country) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?");
                PreparedStatement stmt2 = con.prepareStatement(
                        "INSERT INTO AppUser (username, pass_word, country) VALUES(?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement stmt3 = con.prepareStatement(
                        "INSERT INTO List (listType, name, userId) VALUES('private', 'favorites', ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt1.setString(1, username);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                throw new Exception("Sorry, username already registered");
            }

            stmt2.setString(1, username);
            stmt2.setString(2, password);
            stmt2.setString(3, country);
            stmt2.executeUpdate();

            ResultSet generatedKeys = stmt2.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);

                // Create "favorites" list for the user
                stmt3.setInt(1, userId);
                stmt3.executeUpdate();

                return new User(userId, username, password, country);
            } else {
                throw new Exception("Failed to retrieve generated userId.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Add to Favorites Method
    public void addToFavorites(int movieId, String movieName) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con
                        .prepareStatement("SELECT list_id FROM List WHERE userId=? AND name='favorites'");
                PreparedStatement stmt2 = con.prepareStatement(
                        "INSERT INTO MoviesList (list_id, movieName, movieId) VALUES (?, ?, ?)")) {

            stmt1.setInt(1, this.getId());
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                int listId = rs.getInt("list_id");

                // Add the movie to the "favorites" list
                stmt2.setInt(1, listId);
                stmt2.setString(2, movieName);
                stmt2.setInt(3, movieId);
                stmt2.executeUpdate();

                System.out.println(movieName + " added to your favorites.");
            } else {
                throw new Exception("User's 'favorites' list not found.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    /*
     * //Get all users method
     * public static List<String> getAllUsers() throws Exception {
     * List<String> users = new ArrayList<String>();
     * DB db = new DB();
     * Connection con = null;
     * String query = "SELECT username from appuser;";
     * try {
     * con = db.getConnection();
     * PreparedStatement stmt = con.prepareStatement(query);
     * ResultSet rs = stmt.executeQuery();
     * while (rs.next()) {
     * users.add(rs.getString("username"));
     * }
     * rs.close();
     * stmt.close();
     * return users;
     * } catch (Exception e) {
     * throw new Exception(e.getMessage());
     * } finally {
     * try {
     * db.close();
     * } catch (Exception e) {
     * }
     * }
     * }
     */

    // Follow user
    public void followUser(String follow_user) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?")) {

            stmt1.setString(1, follow_user);
            ResultSet rs = stmt1.executeQuery();

            if (!rs.next()) {
                throw new Exception("There is no user with username: " + follow_user);
            }

            int follow_id = rs.getInt("userId");

            try (PreparedStatement stmt2 = con
                    .prepareStatement("INSERT INTO Followers (followedId, followerId) VALUES(?, ?)")) {
                stmt2.setInt(1, follow_id);
                stmt2.setInt(2, id);
                stmt2.executeUpdate();
                System.out.println("You follow " + follow_user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Unfollow user
    public void unfollowUser(String unfollow_user) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?")) {

            stmt1.setString(1, unfollow_user);
            ResultSet rs = stmt1.executeQuery();

            if (!rs.next()) {
                throw new Exception("There is no user with username: " + unfollow_user);
            }

            int unfollow_id = rs.getInt("userId");

            try (PreparedStatement stmt2 = con
                    .prepareStatement("DELETE FROM Followers WHERE followedId = ? AND followerId = ?")) {
                stmt2.setInt(1, unfollow_id);
                stmt2.setInt(2, id);
                stmt2.executeUpdate();
                System.out.println("You have just unfollow " + unfollow_user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Followings Method
    public ArrayList<User> getFollowing() throws Exception {
        ArrayList<User> followings = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM AppUser JOIN Followers ON " +
                        "Followers.followedId = AppUser.userId " +
                        "WHERE followerId=?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String password = rs.getString("pass_word");
                String country = rs.getString("country");
                User user = new User(userId, username, password, country);
                followings.add(user);
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return followings;
    }

    // Get Followers Method
    public ArrayList<User> getFollowers() throws Exception {
        ArrayList<User> followers = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM AppUser JOIN Followers ON " +
                        "Followers.followerId = AppUser.userId " +
                        "WHERE followedId=?")) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String password = rs.getString("pass_word");
                String country = rs.getString("country");
                User user = new User(userId, username, password, country);
                followers.add(user);
            }

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return followers;
    }

    // Leave chatroom method
    public void leaveChatroom(int chatroomId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "DELETE FROM ChatroomUser WHERE roomId=? AND userId=?;";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, chatroomId);
                stmt.setInt(2, this.id); // Assuming id is the user's ID
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Join chatroom method
    public void joinChatroom(int chatroomId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String insertSql = "INSERT INTO ChatroomUser VALUES(?,?);";

            try (PreparedStatement stmt = con.prepareStatement(insertSql)) {
                stmt.setInt(1, chatroomId);
                stmt.setInt(2, this.id); // Assuming id is the user's ID
                stmt.executeUpdate();
                System.out.println("You can now send messages in the chatroom.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ArrayList<Chatroom> getJoinedChatrooms() throws Exception {
        ArrayList<Chatroom> joinedChatrooms = new ArrayList<>();
        DB db = new DB();

        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT Chatroom.* " +
                        "FROM Chatroom " +
                        "JOIN ChatroomUser ON Chatroom.roomId = ChatroomUser.roomId " +
                        "WHERE ChatroomUser.userId = ?")) {
            stmt.setInt(1, this.id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int roomId = rs.getInt("roomId");
                    String name = rs.getString("name");
                    int creatorId = rs.getInt("creatorId");
                    Chatroom chatroom = new Chatroom(roomId, name, creatorId);
                    joinedChatrooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return joinedChatrooms;
    }

    // gets also called to check if the user can access the messages of a chatroom
    // in app
    public ArrayList<Chatroom> getNotJoinedChatrooms() throws Exception {
        ArrayList<Chatroom> notJoinedChatrooms = new ArrayList<>();
        DB db = new DB();

        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * " +
                        "FROM Chatroom " +
                        "WHERE roomId NOT IN " +
                        "(SELECT roomId FROM ChatroomUser WHERE userId = ?) " +
                        "AND creatorId <> ?")) {
            stmt.setInt(1, this.id);
            stmt.setInt(2, this.id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int roomId = rs.getInt("roomId");
                    String name = rs.getString("name");
                    int creatorId = rs.getInt("creatorId");
                    Chatroom chatroom = new Chatroom(roomId, name, creatorId);
                    notJoinedChatrooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return notJoinedChatrooms;
    }

    public List<Chatroom> getCreatedChatrooms() throws Exception {
        List<Chatroom> createdChatrooms = new ArrayList<>();
        DB db = new DB();

        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM Chatroom WHERE creatorId = ?")) {
            stmt.setInt(1, this.id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int roomId = rs.getInt("roomId");
                    String name = rs.getString("name");
                    int creatorId = rs.getInt("creatorId");
                    Chatroom chatroom = new Chatroom(roomId, name, creatorId);
                    createdChatrooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return createdChatrooms;
    }

    // Get List Method
    public ArrayList<MovieList> getLists() throws Exception {
        ArrayList<MovieList> movieLists = new ArrayList<>();

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query = "SELECT * FROM List WHERE userId=?;";

            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String listType = rs.getString("listType");
                        String listName = rs.getString("name");
                        int listId = rs.getInt("list_id");
                        int creatorId = rs.getInt("userId");

                        // Create and add MovieList object
                        MovieList movieList = new MovieList(listType, creatorId, listName, listId);
                        movieLists.add(movieList);
                    }
                }
            }

            return movieLists;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static boolean doesUsernameExist(String username) throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT username FROM AppUser WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // If rs.next() is true, the username already exists; otherwise, it doesn't.

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<User> getUsersWithPartialUsername(String partialUsername) {
        ArrayList<User> users = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT * FROM AppUser WHERE username LIKE ?")) {

            stmt.setString(1, "%" + partialUsername + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String password = rs.getString("pass_word");
                String country = rs.getString("country");
                User user = new User(userId, username, password, country);
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean isFollowing(User otherUser) {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT 1 FROM Followers WHERE followerId = ? AND followedId = ?")) {

            stmt.setInt(1, this.getId());
            stmt.setInt(2, otherUser.getId());

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If rs.next() is true, the current user follows the other user; otherwise,
                              // they don't

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    // Method to get all user reviews ordered by movie ID
    public ArrayList<Review> getAllUserReviewsOrderedByMovieId() throws Exception {
        ArrayList<Review> userReviews = new ArrayList<>();

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "SELECT Review.reviewId, Review.movieId, Review.reviewText, Review.rating, Review.spoiler, " +
                    "AppUser.username, Review.date, Movie.movieName " +
                    "FROM Review " +
                    "JOIN AppUser ON Review.userId = AppUser.userId " +
                    "JOIN Movie ON Review.movieId = Movie.movieId " +
                    "WHERE Review.userId=? " +
                    "ORDER BY Review.movieId;";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int reviewId = rs.getInt("reviewId");
                        int movieId = rs.getInt("movieId");
                        String reviewText = rs.getString("review_text");
                        float rating = rs.getFloat("rating");
                        boolean spoiler = rs.getBoolean("spoiler");
                        String username = rs.getString("username");
                        Timestamp date = rs.getTimestamp("date");
                        String movieName = rs.getString("movieName");

                        Review review = new Review(reviewId, id, movieId, reviewText, rating, spoiler,
                                username, date, movieName);
                        userReviews.add(review);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return userReviews;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

}
