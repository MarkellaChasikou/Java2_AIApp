import java.sql.Connection;
import java.sql.PreparedStatement;

public class Message {
    private int id;
    private boolean spoiler;
    private String text;
    private User user;
    private Chatroom chatroom;

    public Message(boolean spoiler, String text, User user, Chatroom chatroom) {
        this.spoiler = spoiler;
        this.text = text;
        this.user = user;
        this.chatroom = chatroom;
    }       

    public Message(int id, boolean spoiler, String text, User user, Chatroom chatroom) {
        this.id = id;
        this.spoiler = spoiler;
        this.text = text;
        this.user = user;
        this.chatroom = chatroom;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chatroom getChatroom() {
        return chatroom;
    }

    public void setChatroom(Chatroom chatroom) {
        this.chatroom = chatroom;
    }
    
    //Add message Method
    public static void addMessage(Message message) throws Exception {
    DB db = new DB();
    Connection con = null;
    String sql = "INSERT INTO message(roomId, userId, spoiler, text) VALUES(?,?,?,?);";
    try {
        con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, message.getChatroom().getRoomId());
        stmt.setInt(2, message.getUser().getId());
        stmt.setBoolean(3, message.isSpoiler());
        stmt.setString(4, message.getText());
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
