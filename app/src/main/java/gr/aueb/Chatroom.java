/*
 * Chatroom
 * 
 * Copyright 2024 Bugs Bunny
 */

package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Represents a chatroom in the application.
 * This class provides methods to manage and interact with chatrooms,
 * including creating, updating, and retrieving information about chatrooms.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης, Άγγελος Λαγός, Δήμητρα Κουτρουλιά και Μαρκέλλα Χάσικου
 * 
 */

public class Chatroom {
    /** Unique identifier for the chatroom. */
    private final int roomId;

    /** Name of the chatroom. */
    private String name;

    /** Creator's user ID. */
    private final int creatorId;

    /**
     * Constructs a new Chatroom instance with the specified parameters.
     *
     * @param roomId Unique identifier for the chatroom.
     * @param name   Name of the chatroom.
     * @param userId Creator's user ID.
     */
    public Chatroom(int roomId, String name, int userId) {
        this.roomId = roomId;
        this.name = name;
        this.creatorId = userId;
    }

    /**
     * Gets the unique identifier of the chatroom.
     *
     * @return The unique identifier of the chatroom.
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * Gets the name of the chatroom.
     *
     * @return The name of the chatroom.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the creator's user ID.
     *
     * @return The creator's user ID.
     */
    public int getCreatorId() {
        return creatorId;
    }

    /**
     * Sets the name of the chatroom if the calling user is the creator.
     *
     * @param name   The new name for the chatroom.
     * @param userId The user ID making the update request.
     * @throws Exception If the user is not the creator, the update is not allowed.
     */
    public void setName(String name, int userId) throws Exception {
        if (!isChatroomCreator(userId)) {
            // If the user is not the creator, do not allow the update
            System.out.println("Only the creator can update the chatroom name.");
            return;
        }

        this.name = name;
        updateNameInDatabase();
    }

    /**
     * Checks if the specified user is the creator of the chatroom.
     *
     * @param userId The user ID to check.
     * @return True if the user is the creator; otherwise, false.
     */
    public boolean isChatroomCreator(int userId) {
        DB db = new DB();
        try (Connection con = db.getConnection();
                PreparedStatement stmt = con
                        .prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE roomId = ? AND creatorId = ?")) {

            stmt.setInt(1, roomId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Updates the chatroom name in the database.
     *
     * @throws Exception If there is an issue updating the name in the database
     *                   or if a chatroom with the new name already exists.
     */
    private void updateNameInDatabase() throws Exception {
        if (isNameUnique(name)) {
            DB db = new DB();
            try (Connection con = db.getConnection();
                    PreparedStatement stmt = con.prepareStatement("UPDATE Chatroom SET name = ? WHERE roomId = ?")) {

                stmt.setString(1, name);
                stmt.setInt(2, roomId);
                stmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Failed to update name in the database.");
            } finally {
                try {
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new Exception("Chatroom with the same name already exists. Choose a different name.");
        }
    }

    /**
     * Checks if a chatroom with the given name already exists.
     *
     * @param newName The name to check for uniqueness.
     * @return True if the name is unique; otherwise, false.
     */
    public static boolean isNameUnique(String newName) {
        DB db = new DB();
        try (Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE name = ?")) {

            stmt.setString(1, newName);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) == 0; // If count is 0, the name is unique
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle the exception or log it accordingly
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates a new chatroom and adds it to the database.
     *
     * @param name      The name of the new chatroom.
     * @param creatorId The user ID of the creator.
     * @return The newly created Chatroom instance.
     * @throws Exception If there is an issue creating the chatroom.
     */
    public static Chatroom createChatroom(String name, int creatorId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            int roomId;
            String sql1 = "INSERT INTO Chatroom(name, creatorId) VALUES(?, ?);";
            String sql2 = "INSERT INTO ChatroomUser VALUES(?,?)";

            try (PreparedStatement stmt1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
                stmt1.setString(1, name);
                stmt1.setInt(2, creatorId);
                stmt1.executeUpdate();

                try (ResultSet generatedId = stmt1.getGeneratedKeys()) {
                    if (generatedId.next()) {
                        roomId = generatedId.getInt(1);
                    } else {
                        throw new Exception("Failed to retrieve generated roomId.");
                    }
                }
            }

            try (PreparedStatement stmt2 = con.prepareStatement(sql2)) {
                stmt2.setInt(1, roomId);
                stmt2.setInt(2, creatorId);
                stmt2.executeUpdate();
                System.out.println("Chatroom " + name + " created successfully");
            }

            return new Chatroom(roomId, name, creatorId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Retrieves a list of all chatrooms from the database.
     *
     * @return An ArrayList containing all chatrooms.
     * @throws Exception If there is an issue retrieving chatrooms from the
     *                   database.
     */
    public static ArrayList<Chatroom> getChatrooms() throws Exception {
        ArrayList<Chatroom> chatrooms = new ArrayList<>();
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            try (PreparedStatement stmt = con.prepareStatement("SELECT * from chatroom;");
                    ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int roomId = rs.getInt("roomId");
                    String name = rs.getString("name");
                    int creatorId = rs.getInt("creatorId");
                    Chatroom chatroom = new Chatroom(roomId, name, creatorId);
                    chatrooms.add(chatroom);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return chatrooms;
    }

    /**
     * Retrieves a list of members in the chatroom.
     *
     * @return An ArrayList containing User instances representing members of the
     *         chatroom.
     * @throws Exception If there is an issue retrieving members from the database.
     */
    public ArrayList<User> showChatroomMembers() throws Exception {
        ArrayList<User> members = new ArrayList<>();
        try (DB db = new DB(); Connection con = db.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(
                    "SELECT AppUser.userId, AppUser.username, AppUser.pass_word, AppUser.country " +
                            "FROM AppUser " +
                            "JOIN ChatroomUser ON AppUser.userId = ChatroomUser.userId " +
                            "WHERE ChatroomUser.roomId = ?;")) {

                stmt.setInt(1, roomId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int userId = rs.getInt("userId");
                        String username = rs.getString("username");
                        String password = rs.getString("pass_word");
                        String country = rs.getString("country");
                        User user = new User(userId, username, password, country);
                        members.add(user);
                    }
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
        return members;
    }

    /**
     * Retrieves a list of messages in the chatroom.
     *
     * @return An ArrayList containing Message instances representing messages in
     *         the chatroom.
     * @throws Exception If there is an issue retrieving messages from the database.
     */
    public ArrayList<Message> getMessages() throws Exception {
        ArrayList<Message> messages = new ArrayList<>();

        try (
                Connection con = new DB().getConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "SELECT id, Message.userId, text, spoiler, Message.username " +
                                "FROM Message " +
                                "JOIN AppUser ON Message.userId = AppUser.userId " +
                                "WHERE roomId=?");) {
            stmt.setInt(1, roomId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int messageId = rs.getInt("id");
                    int userId = rs.getInt("userId");
                    String messageText = rs.getString("text");
                    boolean spoiler = rs.getBoolean("spoiler");
                    String senderUsername = rs.getString("username");
                    Message message = new Message(messageId, userId, spoiler, messageText, roomId, senderUsername);
                    messages.add(message);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return messages;
    }

    /**
     * Retrieves unseen messages for a specific user in the chatroom and removes
     * them from the unseen messages list.
     *
     * @param userId The user ID for which to retrieve unseen messages.
     * @return An ArrayList containing Message instances representing unseen
     *         messages.
     * @throws Exception If there is an issue retrieving or removing unseen messages
     *                   from the database.
     */
    public ArrayList<Message> getUnseenMessages(int userId) throws Exception {
        ArrayList<Message> messages = new ArrayList<>();

        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt1 = con.prepareStatement(
                        "SELECT Message.id, Message.userId, Message.text, Message.spoiler, AppUser.username " +
                                "FROM Message " +
                                "JOIN UnseenMessage ON UnseenMessage.UnSeenMessageId = Message.id " +
                                "JOIN AppUser ON Message.userId = AppUser.userId " +
                                "WHERE UnseenMessage.userId=? AND UnseenMessage.roomId =?");
                PreparedStatement stmt2 = con.prepareStatement(
                        "DELETE FROM UnseenMessage WHERE UnseenMessage.userId=? AND UnseenMessage.roomId =?");) {
            stmt1.setInt(1, userId);
            stmt1.setInt(2, roomId);

            try (ResultSet rs = stmt1.executeQuery()) {
                while (rs.next()) {
                    int messageId = rs.getInt("id");
                    int senderUserId = rs.getInt("userId");
                    String messageText = rs.getString("text");
                    boolean spoiler = rs.getBoolean("spoiler");
                    String senderUsername = rs.getString("username");
                    Message message = new Message(messageId, senderUserId, spoiler, messageText, roomId,
                            senderUsername);
                    messages.add(message);
                }
            }

            if (!messages.isEmpty()) {
                stmt2.setInt(1, userId);
                stmt2.setInt(2, roomId);
                stmt2.executeUpdate();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return messages;
    }

    /**
     * Retrieves a chatroom by its name.
     *
     * @param chatroomName The name of the chatroom to retrieve.
     * @return The Chatroom instance if found; otherwise, null.
     * @throws Exception If there is an issue retrieving the chatroom from the
     *                   database.
     */
    public static Chatroom getChatroomByName(String chatroomName) throws Exception {
        Chatroom chatroom = null;

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "SELECT * FROM Chatroom WHERE name = ?";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, chatroomName);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int roomId = rs.getInt("roomId");
                        int creatorId = rs.getInt("creatorId");
                        chatroom = new Chatroom(roomId, chatroomName, creatorId);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return chatroom;
    }

    /**
     * Checks if a user is a member of the chatroom.
     *
     * @param userId The user ID to check.
     * @return True if the user is a member of the chatroom; otherwise, false.
     * @throws Exception If there is an issue checking the user's membership in the
     *                   database.
     */
    public boolean isUserInChatroom(int userId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "SELECT COUNT(*) FROM ChatroomUser WHERE roomId = ? AND userId = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, roomId);
                stmt.setInt(2, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    /**
     * Returns a string representation of the Chatroom object.
     *
     * @return A string containing the chatroom's details.
     */
    @Override
    public String toString() {
        return "Chatroom{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", createorId=" + creatorId +
                '}';
    }
}
