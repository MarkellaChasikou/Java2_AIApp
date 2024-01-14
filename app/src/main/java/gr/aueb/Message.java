package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Message {
    private final int messageId;
    private final int userId;
    private boolean spoiler;
    private String text;
    private final int chatroomId;
    private String  username;


    /*public Message(boolean spoiler, String text, int chatroomId, User user) {
        this.spoiler = spoiler;
        this.text = text;
        this.chatroomId = chatroomId;
        this.user = user;
    } */     

    public Message(int messageId, int userId, boolean spoiler, String text, int chatroomId, String username) {
        this.messageId = messageId;
        this.userId = userId;
        this.spoiler = spoiler;
        this.text = text;
        this.chatroomId = chatroomId;
        this.username = username;    
    }

    public int getMessageId() {
        return messageId;
    }
    public int getUserId() {
        return userId;
    }

    public boolean getSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) throws Exception {
        this.spoiler = spoiler;
        updateSpoilerInDatabase();
    }
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
    

    public String getText() {
        return text;
    }

    public void setText(String text) throws Exception {
        this.text = text;
        updateTextInDatabase();
    }
    public void updateTextInDatabase() throws Exception {
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
    

    public int getChatroomId() {
        return chatroomId;
    }


    public String getUsername() {
        return username;
    }


    //Add message Method
    //Gets called instead of constructor
    public static Message addMessage(int userId, boolean spoiler, String text, int chatroomId, String username) throws Exception {
        try (
            DB db = new DB();
            Connection con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Message (userId, spoiler, text, roomId, username) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ) {
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

    // Helper method to insert into UnSeenMessage for all other members of the chatroom
    private static void insertIntoUnSeenMessage(int chatroomId, int senderUserId, int messageId) throws Exception {
        try (
            DB db = new DB();
            Connection con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO UnSeenMessage (userId, roomId, UnSeenMessageId) SELECT userId, ?, ? FROM ChatroomUser WHERE roomId = ? AND userId <> ?");
        ) {
            stmt.setInt(1, chatroomId);
            stmt.setInt(2, messageId);
            stmt.setInt(3, chatroomId);
            stmt.setInt(4, senderUserId);

            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error inserting into UnSeenMessage: " + e.getMessage());
        }
    }
            
    //Delete message Method
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
            PreparedStatement stmtDeleteUnSeenMessage = con.prepareStatement("DELETE FROM UnSeenMessage WHERE UnSeenMessageId = ?");
        ) {
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