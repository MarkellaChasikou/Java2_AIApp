package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Message {
    private int id;
    private boolean spoiler;
    private String text;
    private int chatroomId;
    private User user;


    /*public Message(boolean spoiler, String text, int chatroomId, User user) {
        this.spoiler = spoiler;
        this.text = text;
        this.chatroomId = chatroomId;
        this.user = user;
    } */     

    public Message(int id, boolean spoiler, String text, int chatroomId, User user) {
        this.id = id;
        this.spoiler = spoiler;
        this.text = text;
        this.chatroomId = chatroomId;
        this.user = user;    
    }

    public int getId() {
        return id;
    }

    public boolean getSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) throws Exception {
        this.spoiler = spoiler;
        updateSpoilerInDatabase();
    }
    public void updateSpoilerInDatabase() throws Exception {
        DB db = new DB();
        Connection con = null;
        PreparedStatement stmt = null;
    
        try {
            con = db.getConnection();
            stmt = con.prepareStatement("UPDATE message SET spoiler=? WHERE id=?");
            stmt.setBoolean(1, spoiler);
            stmt.setInt(2, id); 
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
            stmt.setInt(2, id);
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


    public User getUser() {
        return user;
    }


    //Add message Method
    //Gets called always when constructor is used
    public Message addMessage() throws Exception {
        DB db = new DB();
        try (Connection con = db.getConnection();
             PreparedStatement stmt1 = con.prepareStatement("INSERT INTO message(roomId, userId, spoiler, text) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement stmt2 = con.prepareStatement("INSERT INTO unseenmessage(userId, roomId, unSeenMessageId) VALUES(?,?,?)");
             PreparedStatement stmtRoomMembers = con.prepareStatement("SELECT userId FROM chatroomuser WHERE roomId=?")) {
    
            stmt1.setInt(1, chatroomId);
            stmt1.setInt(2, user.getId());
            stmt1.setBoolean(3, spoiler);
            stmt1.setString(4, text);
    
            int affectedRows = stmt1.executeUpdate();
    
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int messageId = generatedKeys.getInt(1);
    
                        stmtRoomMembers.setInt(1, chatroomId);
                        try (ResultSet rsRoomMembers = stmtRoomMembers.executeQuery()) {
                            int excludeUserId = user.getId();
                            while (rsRoomMembers.next()) {
                                int roomMemberId = rsRoomMembers.getInt("userId");
                                if (roomMemberId != excludeUserId) {
                                    stmt2.setInt(1, roomMemberId);
                                    stmt2.setInt(2, chatroomId);
                                    stmt2.setInt(3, messageId);
                                    stmt2.executeUpdate();
                                }
                            }
                        }
    
                        // Retrieve the message details from the database using the generated message ID
                        String retrieveMessageQuery = "SELECT * FROM message WHERE id=?";
                        try (PreparedStatement stmtRetrieveMessage = con.prepareStatement(retrieveMessageQuery)) {
                            stmtRetrieveMessage.setInt(1, messageId);
                            try (ResultSet rsMessage = stmtRetrieveMessage.executeQuery()) {
                                if (rsMessage.next()) {
                                    int id = rsMessage.getInt("id");
                                    boolean retrievedSpoiler = rsMessage.getBoolean("spoiler");
                                    String messageText = rsMessage.getString("text");
                                    int retrievedChatroomId = rsMessage.getInt("roomId");
    
                                    // Create and return the Message object
                                    return new Message(id, retrievedSpoiler, messageText, retrievedChatroomId, user);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    
        // Return null in case of an error or no message is retrieved
        return null;
    }
            
    //Delete message Method
    public static void deleteMessage(int messageId) throws Exception {
        DB db = null;
        Connection con = null;
        PreparedStatement stmt = null;
    
        try {
            db = new DB();
            con = db.getConnection();
            stmt = con.prepareStatement("DELETE FROM message WHERE id=?");
            stmt.setInt(1, messageId);
            stmt.executeUpdate();
            System.out.println("Message deleted");
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
        
}