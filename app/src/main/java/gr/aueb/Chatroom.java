package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Chatroom {
    private int roomId;
    private String name;
    private int createorId;
    //private User user;
    private Message message;
    private List<Integer> users = new ArrayList<Integer>();

    //Constructors
    /*public Chatroom(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Chatroom(int roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }*/

    public Chatroom(int roomId, String name, int userid) {
        this.roomId = roomId;
        this.name = name;
        createorId = userid;
    }

    /*public Chatroom(int roomId, List<Integer> users) {
        this.roomId = roomId;
        this.users = users;
    }*/


    //Setters- Getters
     public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateNameInDatabase();
    }

    private void updateNameInDatabase() {
        DB db = new DB();
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement("UPDATE Chatroom SET name = ? WHERE roomId = ?")) {
    
            stmt.setString(1, name);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /*public User getUser() {
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
    }*/

   //Create chatroom method 
   //Gets called immediately after creating a chatroom from constructor
   public Chatroom createChatroom() throws Exception {
    DB db = new DB();
    Connection con = null;
    int roomId;
    String sql1 = "INSERT INTO Chatroom(name, creatorId) VALUES(?, ?);";
    String sql2 = "INSERT INTO ChatroomUser VALUES(?,?)";
    try {
        con = db.getConnection();
        try (PreparedStatement stmt1 = con.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {
            stmt1.setString(1, name);
            stmt1.setInt(2, createorId);
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
            stmt2.setInt(2, createorId);
            stmt2.executeUpdate();
            System.out.println("Chatroom " + name + " created successfully");
        }

        return new Chatroom(roomId, name, createorId);
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
}

    //Get chatrooms method 
    public static List<Chatroom> getChatrooms() throws Exception {
        List<Chatroom> chatrooms = new ArrayList<>();
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
    



    //Show chatroom members method
    public static List<String> showChatroomMembers(int chatroomId) throws Exception {
        ArrayList<String> members = new ArrayList<>();
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            try (PreparedStatement stmt = con.prepareStatement(
                    "SELECT AppUser.username FROM AppUser "
                            + "JOIN ChatroomUser ON AppUser.userId = ChatroomUser.userId "
                            + "WHERE ChatroomUser.roomId = ?;")) {
    
                stmt.setInt(1, chatroomId);
                ResultSet rs = stmt.executeQuery();
    
                while (rs.next()) {
                    members.add(rs.getString("username"));
                }
    
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
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
        return members;
    }
    
    

    //Get messages method
    public static List<String> getMessages(int chatroomId) throws Exception {
        ArrayList<String> messages = new ArrayList<>();
        try (
            Connection con = new DB().getConnection();
            PreparedStatement stmt = con.prepareStatement(
                    "SELECT message.text, appuser.username " +
                            "FROM message " +
                            "JOIN appuser ON message.userId = appuser.userId " +
                            "WHERE message.roomId=?;");
        ) {
    
            stmt.setInt(1, chatroomId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String messageText = rs.getString("text");
                String senderUsername = rs.getString("username");
                messages.add(senderUsername + ": " + messageText);
            }
    
            return messages;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    
    //Get unseen Messages method
    public static List<String> getUnseenMessage(int chatroomId, User user) throws Exception {
        ArrayList<String> messages = new ArrayList<>();
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            try (PreparedStatement stmt1 = con.prepareStatement(
                         "SELECT message.text, appuser.username " +
                                 "FROM message " +
                                 "JOIN unseenmessage ON unseenmessage.unSeenMessageId = message.id " +
                                 "JOIN appuser ON message.userId = appuser.userId " +
                                 "WHERE unseenmessage.userId=? AND unseenmessage.roomId =?;");
                 PreparedStatement stmt2 = con.prepareStatement(
                         "DELETE FROM unseenmessage WHERE unseenmessage.userId=? AND unseenmessage.roomId =?;")) {
    
                stmt1.setInt(1, user.getId());
                stmt1.setInt(2, chatroomId);
                ResultSet rs = stmt1.executeQuery();
    
                while (rs.next()) {
                    String messageText = rs.getString("text");
                    String senderUsername = rs.getString("username");
                    messages.add(senderUsername + ": " + messageText);
                }
    
                if (rs != null) {
                    stmt2.setInt(1, user.getId());
                    stmt2.setInt(2, chatroomId);
                    stmt2.executeUpdate();
                }
    
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
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
        return messages;
    }
    
    public String toString() {
        return "Chatroom{" +
                "roomId: " + roomId +
                ", name: '" + name + '\'' +
                '}';
    }
}