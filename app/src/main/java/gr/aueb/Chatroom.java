package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Chatroom {
    private final int roomId;
    private String name;
    private final int createorId;

    public Chatroom(int roomId, String name, int userid) {
        this.roomId = roomId;
        this.name = name;
        createorId = userid;
    }

    // Setters- Getters
    public int getRoomId() {
        return roomId;
    }

    public String getName() {
        return name;
    }

    public int getCreatorId() {
        return createorId;
    }

    public void setName(String name, int userId) throws Exception {

        if (!isChatroomCreator(userId)) {
            // If the user is not the creator, do not allow the update
            System.out.println("Only the creator can update the chatroom name.");
            return;
        }

        this.name = name;
        updateNameInDatabase();
    }

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

    private boolean isNameUnique(String newName) {
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

    // Create chatroom method
    // Gets called immediately after creating a chatroom from constructor
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

    // Get chatrooms method
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

    // Show chatroom members method
    public List<String> showChatroomMembers() throws Exception {
        List<String> members = new ArrayList<>();
        DB db = new DB();
        Connection con = null;
        try {
            con = db.getConnection();
            try (PreparedStatement stmt = con.prepareStatement(
                    "SELECT AppUser.username FROM AppUser "
                            + "JOIN ChatroomUser ON AppUser.userId = ChatroomUser.userId "
                            + "WHERE ChatroomUser.roomId = ?;")) {

                stmt.setInt(1, roomId);
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

    // Get messages method
    public List<Message> getMessages() throws Exception {
        List<Message> messages = new ArrayList<>();

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

    // Get unseen Messages method
    public List<Message> getUnseenMessages(int userId) throws Exception {
        List<Message> messages = new ArrayList<>();

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

    // Search chatroom by name method
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

    @Override
    public String toString() {
        return "Chatroom{" +
                "roomId=" + roomId +
                ", name='" + name + '\'' +
                ", createorId=" + createorId +
                '}';
    }
}