package gr.aueb;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ChatroomTest {
private static User user;
private static User user2;
private static Connection connection;
private static Chatroom chatroom;
private static Chatroom chatroom1;
private static Message message;
private static Message message2;




@BeforeAll
    public static void CreateInserts() throws Exception{
        DB db = new DB(); 
        connection = db.getConnection();
        user = new User(1,"TestUser","TestPassword","TestCountry");
        chatroom = new Chatroom(1, "ChatroomTest", user.getId());
        message = new Message(1, user.getId(), false, "TestText", 1, user.getUsername());
        try (PreparedStatement insertStmt1 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1, 'TestUser', 'TestPassword', 'TestCountry')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("INSERT INTO chatroom (roomId, name, creatorId) VALUES (1, 'ChatroomTest', 1)")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("INSERT INTO chatroomuser (roomId, userId) VALUES (1, 1)")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("INSERT INTO message ( userid, spoiler, text , roomid, username) VALUES (1, false, 'TestText', 1, 'TestUser')")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        }
@AfterAll
    public static void DeleteAllInserts() throws Exception {
        try (PreparedStatement insertStmt1 = connection.prepareStatement("DELETE FROM message WHERE id = 1")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM chatroomuser WHERE userid = 1")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM chatroomuser WHERE userid = 2")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("DELETE FROM chatroom WHERE creatorid = 1")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection.prepareStatement("DELETE FROM chatroom WHERE creatorid = 2")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 2")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt7 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1")) {
            insertStmt7.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    
        // Κλείσιμο της σύνδεσης
        if (connection != null) {
            connection.close();
        }
    } 

@Test
    public void getRoomIdTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getRoomId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT roomid FROM chatroom WHERE roomid = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(chatroom.getRoomId(), resultSet.getInt("roomid"));
                } else {
                    fail("the roomid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

@Test
    public void getNameTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getName
        try (PreparedStatement stmt = connection.prepareStatement("SELECT name FROM chatroom WHERE roomid = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(chatroom.getName(), resultSet.getString("name"));
            } else {
                fail("the name does not match");
            }
            } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
@Test

    public void getCreatorIdTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getCreatorId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT creatorid FROM chatroom WHERE roomid = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(chatroom.getCreatorId(), resultSet.getInt("creatorid"));
                } else {
                    fail("the creatorid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }

@Test
    public void testIsChatroomCreator() throws SQLException, Exception {
        // Έλεγχος για την περίπτωση όταν ο χρήστης είναι ο δημιουργός του chatroom
        DB db = new DB();
        try (Connection con = db.getConnection();
            PreparedStatement stmt = con
            .prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE roomId = ? AND creatorId = ?")) {

            stmt.setInt(1, chatroom.getRoomId());
            stmt.setInt(2, chatroom.getCreatorId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                   assertTrue( rs.getInt(1) > 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }  
        }
    }
@Test
    public void testIsNotChatroomCreator() throws SQLException, Exception {
        // Έλεγχος για την περίπτωση όταν ο χρήστης δεν είναι ο δημιουργός του chatroom
        DB db = new DB();
        try (Connection con = db.getConnection();
            PreparedStatement stmt = con
            .prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE roomId = ? AND creatorId = ?")) {
            stmt.setInt(1, chatroom.getRoomId());
            stmt.setInt(2, 2);  // δεν υπαρχει ο χρηστης 2/ακομα και αν υπηρχε δεν ειναι ο creator του chatroom με id = 1
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                   assertFalse( rs.getInt(1) > 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }  
        }
    }

@Test
    public void testSetNameAsCreator() throws Exception {
        // Προετοιμασία των δεδομένων
        try {
            // Εκτέλεση της setName με τον δημιουργό του chatroom 
        chatroom.setName("NewName", 1);
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
        // Έλεγχος αν το όνομα του chatroom έχει ενημερωθεί σωστά
        assertEquals("NewName", chatroom.getName());
    }
    
@Test
    public void testSetNameAsNonCreator() throws Exception {
        // Προετοιμασία των δεδομένων
        try {
            // Εκτέλεση της setName με μη δημιουργό του chatroom
            chatroom.setName("NewName", 2);
        } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
        }
        // Έλεγχος αν το όνομα του chatroom ΔΕΝ έχει αλλάξει
        assertNotEquals("NewName", chatroom.getName());
    }




@Test
    public void testIsNameUniqueWhenUniqueTrue() {
        // Τεστ για την περίπτωση όταν το όνομα είναι μοναδικό
        DB db = new DB();
        try (Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE name = ?")) {
        stmt.setString(1,"ChatroomTestName1");
        try (ResultSet rs = stmt.executeQuery()) {
            rs.next();
            assertTrue(rs.getInt(1) == 0); // If count is 0, the name is unique
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
@Test
    public void testIsNameUniqueWhenNotUnique() {
        // Τεστ για την περίπτωση όταν το όνομα δεν είναι μοναδικό
        DB db = new DB();
        //Έλεγχος αν το όνομα του chatroom υπάρχει
        try (Connection con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT COUNT(*) FROM Chatroom WHERE name = ?")) {
            stmt.setString(1, chatroom.getName());
                try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                assertFalse(rs.getInt(1) == 0); // If count is 0, the name is unique
                }
        } catch (Exception e) {
        e.printStackTrace();
        }
    }

@Test
    public void testUpdateNameInDatabaseCorrect() throws Exception {
         // αλλαζω το ονομα του chatroom απο τον χρήστη 1 ( εχω ηδη τεσταρει την λειτουργια της setName)
        chatroom.setName("name2",1);
    DB db = new DB();
    Connection con = db.getConnection();
        // Έλεγχος αν το όνομα του chatroom έχει ενημερωθεί σωστά στη βάση δεδομένων
        try (PreparedStatement stmt = con.prepareStatement("SELECT name FROM Chatroom WHERE roomId = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals("name2", resultSet.getString("name")); // 
                } else {
                    fail("Chatroom not found in the database.");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    
}
    @Test
    public void testCreateChatroom() throws Exception {
        // Έλεγχος εάν το chatroom δημιουργήθηκε σωστά
        chatroom1 = Chatroom.createChatroom("CreatedChatroomTest",1);
        assertNotNull(chatroom1);
        assertEquals("CreatedChatroomTest", chatroom1.getName());
        assertEquals(1, chatroom1.getCreatorId());

        // Έλεγχος εάν τα δεδομένα έχουν καταχωρηθεί στη βάση δεδομένων
        try (DB db = new DB(); Connection con = db.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("SELECT * FROM Chatroom WHERE roomId = ?")) {
                stmt.setInt(1, chatroom1.getRoomId());
                try (ResultSet resultSet = stmt.executeQuery()) {
                    assertTrue(resultSet.next());
                    assertEquals("CreatedChatroomTest", resultSet.getString("name"));
                    assertEquals(1, resultSet.getInt("creatorId"));
                }
            }
        } catch (Exception e) {
            fail("Exception during database check: " + e.getMessage());
        }
    }

@Test
public void testGetChatrooms() {
    try {
        List<Chatroom> chatrooms = Chatroom.getChatrooms();

        // Έλεγχος εάν η λίστα δεν είναι null
        assertNotNull(chatrooms);

        // Έλεγχος εάν το πλήθος των chatrooms είναι σωστό 
        assertEquals(1, chatrooms.size()); 

        // Έλεγχος εάν τα δεδομένα είναι σωστά 
        assertEquals("ChatroomTest", chatrooms.get(0).getName());
        assertEquals(1, chatrooms.get(0).getCreatorId());


    } catch (Exception e) {
        fail("Exception during test: " + e.getMessage());
    }
}
@Test
    public void testShowChatroomMembers() throws Exception {
        user2= User.register("User2","Password2" , "Greece");
        user2.joinChatroom(1);
        
        try {
            // Κλήση της showChatroomMembers με τον εικονικό DB
            List<String> members = chatroom.showChatroomMembers();

            // Έλεγχος εάν η λίστα δεν είναι null
            assertNotNull(members);

            // Έλεγχος εάν τα μέλη που περιέχονται στη λίστα είναι σωστά (προσαρμόστε τον έλεγχο ανάλογα με την υλοποίησή σας)
            List<String> expectedMembers = Arrays.asList("TestUser", "User2"); // Υποθέτουμε τα αναμενόμενα μέλη
            assertEquals(expectedMembers, members);

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

   

@Test
public void testGetMessages() {
    try {
        Message.addMessage(1, false, "great Movie!", 1, user.getUsername());
        Message.addMessage(2, false, "Haha yeah agree", 1, user2.getUsername());
        Message.addMessage(1, false, "Goodnight", 1, user.getUsername());

        List<Message> messages = chatroom.getMessages();

        // Έλεγχος του αποτελέσματος
        assertNotNull(messages);
        assertEquals(3, messages.size());

        // Έλεγχος του περιεχομένου του πρώτου μηνύματος 
        Message firstMessage = messages.get(0);
        assertEquals(1, firstMessage.getMessageId());
        assertEquals(1, firstMessage.getUserId());
        assertEquals("great Movie!", firstMessage.getText());
        assertFalse(firstMessage.getSpoiler());
        assertEquals("TestUser", firstMessage.getUsername());

        // Έλεγχος του περιεχομένου του δεύτερου μηνύματος
        Message secondMessage = messages.get(1);
        assertEquals(2, secondMessage.getMessageId());
        assertEquals(2, secondMessage.getUserId());
        assertEquals("Haha yeah agree", secondMessage.getText());
        assertFalse(secondMessage.getSpoiler());
        assertEquals("User2", secondMessage.getUsername());

        // Έλεγχος του περιεχομένου του τρίτου μηνύματος
        Message thirdMessage = messages.get(2);
        assertEquals(3, thirdMessage.getMessageId());
        assertEquals(1, thirdMessage.getUserId());
        assertEquals("Goodnight", thirdMessage.getText());
        assertFalse(thirdMessage.getSpoiler());
        assertEquals("TestUser", thirdMessage.getUsername());

    } catch (Exception e) {
        fail("Exception during test: " + e.getMessage());
    }
}
}


































