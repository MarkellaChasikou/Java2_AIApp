/*
 * Message
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents messages in a chatroom, stored in a database.
 * 
 * The class includes methods for updating message details, adding messages to
 * the database,
 * and deleting messages. It also provides methods to fetch and set message
 * attributes such as
 * spoiler status and text. The class interacts with the database using JDBC.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Νίκος Ραγκούσης, Μαρκέλλα Χάσικου και Άγγελος Λαγός
 */

public class Message {
    /** The unique identifier for the message. */
    private final int messageId;
    /** The user ID associated with the message. */
    private final int userId;
    /** The spoiler status of the message. */
    private boolean spoiler;
    /** The text content of the message. */
    private String text;
    /** The chatroom ID associated with the message. */
    private final int chatroomId;
    /** The username of the user who sent the message. */
    private String username;

    /*public Message(boolean spoiler, String text, int chatroomId, User user) {
        this.spoiler = spoiler;
        this.text = text;
        this.chatroomId = chatroomId;
        this.user = user;
    } */     
    
    /** Constructs a Message object with specified attributes. */
    public Message(int messageId, int userId, boolean spoiler, String text, int chatroomId, String username) {
        this.messageId = messageId;
        this.userId = userId;
        this.spoiler = spoiler;
        this.text = text;
        this.chatroomId = chatroomId;
        this.username = username;
    }

    /**
     * Gets the unique identifier for the message.
     * 
     * @return The message ID.
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Gets the user ID associated with the message.
     * 
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Gets the spoiler status of the message.
     * 
     * @return The spoiler status.
     */
    public boolean getSpoiler() {
        return spoiler;
    }

    /**
     * Sets the spoiler status of the message and updates it in the database.
     * 
     * @param spoiler The new spoiler status.
     * @throws Exception if there is an error during the update process.
     */
    public void setSpoiler(boolean spoiler) throws Exception {
        this.spoiler = spoiler;
        updateSpoilerInDatabase();
    }

    /**
     * Updates the spoiler status of the message in the database.
     * 
     * @throws Exception if there is an error during the update process.
     */
    private void updateSpoilerInDatabase() throws Exception {
        DB db = new DB();
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = db.getConnection();
            stmt = con.prepareStatement("UPDATE message SET spoiler=? WHERE id=?");
            stmt.setBoolean(1, spoiler);
            stmt.setInt(2, messageId);
            stmt.executeUpdate();
            System.out.println("Spoiler updated in the database");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
                if (db != null) {
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the text content of the message.
     * 
     * @return The message text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text content of the message and updates it in the database.
     * 
     * @param text The new message text.
     * @throws Exception if there is an error during the update process.
     */
    public void setText(String text) throws Exception {
        this.text = text;
        updateTextInDatabase();
    }

    /**
     * Updates the text content of the message in the database.
     * 
     * @throws Exception if there is an error during the update process.
     */
    private void updateTextInDatabase() throws Exception {
        DB db = null;
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            db = new DB();
            con = db.getConnection();
            stmt = con.prepareStatement("UPDATE message SET text=? WHERE id=?");
            stmt.setString(1, text);
            stmt.setInt(2, messageId);
            stmt.executeUpdate();
            System.out.println("Text updated in the database");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
                if (db != null) {
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the chatroom ID associated with the message.
     * 
     * @return The chatroom ID.
     */
    public int getChatroomId() {
        return chatroomId;
    }

    /**
     * Gets the username of the user who sent the message.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Adds a new message to the database and returns a corresponding Message
     * object.
     * 
     * @param userId     The user ID associated with the message.
     * @param spoiler    The spoiler status of the message.
     * @param text       The text content of the message.
     * @param chatroomId The chatroom ID associated with the message.
     * @param username   The username of the user who sent the message.
     * @return The newly created Message object.
     * @throws Exception if there is an error during the insertion process.
     */
    public static Message addMessage(int userId, boolean spoiler, String text, int chatroomId, String username)
            throws Exception {
        try (
                DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Message (userId, spoiler, text, roomId, username) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);) {
            stmt.setInt(1, userId);
            stmt.setBoolean(2, spoiler);
            stmt.setString(3, text);
            stmt.setInt(4, chatroomId);
            stmt.setString(5, username);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }

            // Get the generated messageId
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int messageId = generatedKeys.getInt(1);

                    // Insert into UnSeenMessage for all other members of the chatroom
                    insertIntoUnSeenMessage(chatroomId, userId, messageId);

                    // Return a new Message object with the generated messageId and other fields
                    return new Message(messageId, userId, spoiler, text, chatroomId, username);
                } else {
                    throw new SQLException("Creating message failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Helper method to insert into UnSeenMessage for all other members of the
     * chatroom.
     * 
     * @param chatroomId   The chatroom ID.
     * @param senderUserId The user ID of the message sender.
     * @param messageId    The ID of the newly created message.
     * @throws Exception if there is an error during the insertion process.
     */
    private static void insertIntoUnSeenMessage(int chatroomId, int senderUserId, int messageId) throws Exception {
        try (
                DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO UnSeenMessage (userId, roomId, UnSeenMessageId) SELECT userId, ?, ? FROM ChatroomUser WHERE roomId = ? AND userId <> ?");) {
            stmt.setInt(1, chatroomId);
            stmt.setInt(2, messageId);
            stmt.setInt(3, chatroomId);
            stmt.setInt(4, senderUserId);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error inserting into UnSeenMessage: " + e.getMessage());
        }
    }

    /**
     * Deletes a message from the database.
     * 
     * @param message The message to be deleted.
     * @param userId  The user ID attempting to delete the message.
     * @throws Exception if the user does not have permission or if there is an
     *                   error during the deletion process.
     */
    public static void deleteMessage(Message message, int userId) throws Exception {
        if (message.getUserId() != userId) {
            throw new Exception("User does not have permission to delete this message.");
        }

        int messageId = message.getMessageId();
        int chatroomId = message.getChatroomId();

        try (
                DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmtDeleteMessage = con.prepareStatement("DELETE FROM Message WHERE id = ?");
                PreparedStatement stmtDeleteUnSeenMessage = con
                        .prepareStatement("DELETE FROM UnSeenMessage WHERE UnSeenMessageId = ?");) {
            // Delete from Message table
            stmtDeleteMessage.setInt(1, messageId);
            stmtDeleteMessage.executeUpdate();

            // Delete from UnSeenMessage table
            stmtDeleteUnSeenMessage.setInt(1, messageId);
            stmtDeleteUnSeenMessage.executeUpdate();

            System.out.println("Message deleted successfully.");
        } catch (Exception e) {
            throw new Exception("Error deleting message: " + e.getMessage());
        }
    }

    /**
     * Returns a string representation of the Message object.
     * 
     * @return A string representation including message details.
     */
    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", userId=" + userId +
                ", spoiler=" + spoiler +
                ", text='" + text + '\'' +
                ", chatroomId=" + chatroomId +
                ", username='" + username + '\'' +
                '}';
    }
}
