import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Chatroom {
    private int roomId;
    private String name;
    private User user;
    private Message message;
    private List<Integer> users = new ArrayList<Integer>();
   
    //Constructors
    public Chatroom(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Chatroom(int roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public Chatroom(int roomId, String name, User user) {
        this.roomId = roomId;
        this.name = name;
        this.user = user;
    }

    public Chatroom(int roomId, List<Integer> users) {
        this.roomId = roomId;
        this.users = users;
    }


    //Setters- Getters
     public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setMember(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

   //Create chatroom method 
    public Chatroom createChatroom() throws Exception {
        DB db = new DB();
        Connection con = null;
        int roomId;
        String sql1 = "INSERT INTO Chatroom(name, creatorId) VALUES(?, ?);";
        String sql2 = "INSERT INTO ChatroomUser VALUES(?,?)";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            stmt1.setString(1, name);
            stmt1.setInt(2, user.getId());
            stmt1.executeUpdate();
            try (ResultSet generatedId = stmt1.getGeneratedKeys()) {
                if (generatedId.next()) {
                    roomId = generatedId.getInt(1);
                } else {
                    throw new Exception("Failed to retrieve generated roomId.");
                }
            }
            stmt1.close();
            try (PreparedStatement stmt2 = con.prepareStatement(sql2)) {
                stmt2.setInt(1, roomId);
                stmt2.setInt(2, user.getId());
                stmt2.executeUpdate();
                System.out.println("Chatroom " + name + " created successfully");
            }
        return new Chatroom(roomId, name, user);
        } catch (Exception e) {
            throw new Exception(e.getMessage());    
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                
            }
        }
    }
    //Get chatrooms method 
    public static List<Chatroom> getChatrooms() throws Exception {
        List<Chatroom> chatrooms = new ArrayList<Chatroom>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * from chatroom;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int roomId = rs.getInt("roomId");
                String name = rs.getString("name");
                Chatroom chatroom = new Chatroom(roomId, name);
                chatrooms.add(chatroom);
            }
            rs.close();
            stmt.close();
            return chatrooms;
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

    //Show chatroom members method
    public static List<String> showChatroomMembers(int chatroomId) throws Exception {
        ArrayList<String> members = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT AppUser.username FROM AppUser "
            + "JOIN ChatroomUser ON AppUser.userId = ChatroomUser.userId "
            + "WHERE ChatroomUser.roomId = ?";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, chatroomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                members.add(rs.getString("username"));
            }
            stmt.close();
            rs.close();
            return members;
        } catch (Exception e) {
            throw new Exception(e.getMessage());    
        } finally {
        try {
            db.close();
        } catch (Exception e) {
                
        }
    }
    }

    //Get messages method
    public static List<String> getMessages(int chatroomId) throws Exception {
        ArrayList<String> messages = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT text FROM message WHERE roomId=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, chatroomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(rs.getString("text"));
            } 
            stmt.close();
            rs.close();
            return messages;
             } catch (Exception e) {
            throw new Exception(e.getMessage());    
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                
            }
        }
    }

    //Get unseen Messages method
    public static List<String> getUnseenMessage(int chatroomId, User user) throws Exception {
        ArrayList<String> messages = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT text FROM message JOIN unseenmessage " +
            "ON unSeenMessage.unSeenMessageId=message.id where unSeenMessage.userId=? " +
            "AND unSeenMessage.roomId =?;";
        String sql = "DELETE FROM unseenmessage WHERE unSeenMessage.userId=? " +
            "AND unSeenMessage.roomId =?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setInt(1, user.getId());
            stmt1.setInt(2, chatroomId);
            ResultSet rs = stmt1.executeQuery();
            while (rs.next()) {
                messages.add(rs.getString("text"));
            }
            stmt1.close();
            rs.close();
            if (rs != null) {
            PreparedStatement stmt2 = con.prepareStatement(sql);
            stmt2.setInt(1, user.getId());
            stmt2.setInt(2, chatroomId);
            stmt2.executeUpdate();
            stmt2.close();
            }
            return messages;
            } catch (Exception e) {
            throw new Exception(e.getMessage());    
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                
            }
        }
    }

    public String toString() {
        return "Chatroom{" +
                "roomId: " + roomId +
                ", name: '" + name + '\'' +
                '}';
    }
}