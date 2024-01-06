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


    public Message(boolean spoiler, String text, int chatroomId, User user) {
        this.spoiler = spoiler;
        this.text = text;
        this.chatroomId = chatroomId;
        this.user = user;
    }       

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

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(int chatroomId) {
        this.chatroomId = chatroomId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    //Add message Method
    public void addMessage() throws Exception {
    DB db = new DB();
    Connection con = null;
    String sql1 = "INSERT INTO message(roomId, userId, spoiler, text) VALUES(?,?,?,?);";
    String sql2 = "INSERT INTO unseenmessage(userId, roomId, unSeenMessageId) VALUES(?,?,?);";
    try {
        con = db.getConnection();
        PreparedStatement stmt1 = con.prepareStatement(sql1,  Statement.RETURN_GENERATED_KEYS);
        stmt1.setInt(1, chatroomId);
        stmt1.setInt(2, user.getId());
        stmt1.setBoolean(3, spoiler);
        stmt1.setString(4, text);
        stmt1.executeUpdate();
        System.out.print("send!");
        ResultSet generatedKeys = stmt1.getGeneratedKeys();
        generatedKeys.next();
        int messageId = generatedKeys.getInt(1);
        stmt1.close();
        String sqlRoomMembers = "SELECT userId FROM chatroomuser WHERE roomId=?";
        PreparedStatement stmt2 = con.prepareStatement(sqlRoomMembers);
        stmt2.setInt(1, chatroomId);
        ResultSet rsRoomMembers = stmt2.executeQuery();
        int excludeUserId = user.getId(); 
        while (rsRoomMembers.next()) {
            int roomMemberId = rsRoomMembers.getInt("userId");
            if (roomMemberId != excludeUserId) {
                PreparedStatement stmtUnseenMessage = con.prepareStatement(sql2);
                stmtUnseenMessage.setInt(1, roomMemberId);
                stmtUnseenMessage.setInt(2, chatroomId);
                stmtUnseenMessage.setInt(3, messageId);
                stmtUnseenMessage.executeUpdate();
                stmtUnseenMessage.close();
            }
        }
        rsRoomMembers.close();
        stmt2.close();
    } catch (Exception e) {
        throw new Exception(e.getMessage());
    } finally {
        try {
            db.close();
        } catch (Exception e) {

        }
    }   
    }

    //Delete message Method
    public static void deleteMessage(int messageId) throws Exception {
    DB db = new DB();
    Connection con = null;
    String sql = "DELETE FROM message WHERE id=?;";
    try {
        con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, messageId);
        stmt.executeUpdate();
        System.out.println("Message deleted");
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
}