package gr.aueb;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class User {
    private int id;
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
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM AppUser WHERE username=? AND pass_word=?")) {
    
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
                return new User(userId, username, password, country);
            } else {
                throw new Exception("Failed to retrieve generated userId.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    /*//Get all users method
    public static List<String> getAllUsers() throws Exception {
        List<String> users = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT username from appuser;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(rs.getString("username"));
            }
            rs.close();
            stmt.close();
            return users;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            }
        }
    }*/

    //Follow user
    public void followUser(String follow_user) {
        try (DB db = new DB();
             Connection con = db.getConnection();
             PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?");
             ResultSet rs = stmt1.executeQuery()) {
    
            stmt1.setString(1, follow_user);
    
            if (!rs.next()) {
                throw new Exception("There is no user with username: " + follow_user);
            }
    
            int follow_id = rs.getInt("userId");
    
            try (PreparedStatement stmt2 = con.prepareStatement("INSERT INTO Followers (followedId, followerId) VALUES(?, ?)")) {
                stmt2.setInt(1, follow_id);
                stmt2.setInt(2, id);
                stmt2.executeUpdate();
                System.out.println("You follow " + follow_user);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Unfollow user
    public void unfollowUser(String unfollow_user) {
        try (DB db = new DB();
             Connection con = db.getConnection();
             PreparedStatement stmt1 = con.prepareStatement("SELECT * FROM AppUser WHERE username=?");
             ResultSet rs = stmt1.executeQuery()) {
    
            stmt1.setString(1, unfollow_user);
    
            if (!rs.next()) {
                throw new Exception("There is no user with username: " + unfollow_user);
            }
    
            int unfollow_id = rs.getInt("userId");
    
            try (PreparedStatement stmt2 = con.prepareStatement("DELETE FROM Followers WHERE followedId = ? AND followerId = ?")) {
                stmt2.setInt(1, unfollow_id);
                stmt2.setInt(2, id);
                stmt2.executeUpdate();
                System.out.println("You have just unfollow " + unfollow_user);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get Followings Method
    public List<String> getFollowing() throws Exception {
        List<String> followings = new ArrayList<>();
    
        try (DB db = new DB();
             Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT username FROM AppUser JOIN Followers ON " +
                     "Followers.followedId = AppUser.userid " +
                     "WHERE followerId=?")) {
    
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                followings.add(rs.getString("username"));
            }
    
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    
        return followings;
    }
    
    //Get Followers Method
    public List<String> getFollowers() throws Exception {
        List<String> followers = new ArrayList<>();
    
        try (DB db = new DB();
             Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT username FROM AppUser JOIN Followers ON " +
                     "Followers.followerId = AppUser.userid " +
                     "WHERE followedId=?")) {
    
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                followers.add(rs.getString("username"));
            }
    
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    
        return followers;
    }
    
    //Leave chatroom method
    public static void leaveChatroom(int chatroomId, User user) throws Exception {
        DB db = new DB();
        Connection con = null;
        String sql = "DELETE FROM ChatroomUser WHERE roomId=? AND userId=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, chatroomId);
            stmt.setInt(2,user.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());    
        } finally {
            try {
                db.close();
            } catch (Exception e) {
    
            }
        }
        }
    //Join chatroom method
    public static Chatroom joinChatroom(int chatroomId, User user) throws Exception {
        List<Integer> members = new ArrayList<>();
        DB db = new DB();
        Connection con = null;
        String sql = "INSERT INTO ChatroomUser VALUES(?,?);";
        String query = "SELECT * FROM Chatroomuser join chatroom WHERE Chatroomuser.roomId=?";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(sql);
            stmt1.setInt(1, chatroomId);
            stmt1.setInt(2,user.getId());
            stmt1.executeUpdate();
            System.out.println("You can know sent messages in chatroom ");
            stmt1.close();
            PreparedStatement stmt2 = con.prepareStatement(query);
            stmt2.setInt(1, chatroomId);
            ResultSet rs = stmt2.executeQuery();
            while (rs.next()) {
                members.add(rs.getInt("userid"));
            }
    
        return new Chatroom(chatroomId, members);
        } catch (Exception e) {
            throw new Exception(e.getMessage());    
        } finally {
            try {
                db.close();
            } catch (Exception e) {
    
            }
        }
        }  
    public List<Chatroom> getJoinedChatrooms() throws Exception {
        List<Chatroom> joinedChatrooms = new ArrayList<>();
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
    //gets also called to check if the user can access the messages of a chatroom in app
    public List<Chatroom> getNotJoinedChatrooms() throws Exception {
        List<Chatroom> notJoinedChatrooms = new ArrayList<>();
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
}
