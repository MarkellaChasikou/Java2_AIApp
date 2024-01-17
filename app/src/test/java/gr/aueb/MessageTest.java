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

public class MessageTest {

private static Connection connection;
private static Message message;
private static Message message2;

@BeforeAll
    public static void CreateInserts() throws Exception{
        DB db = new DB(); 
        connection = db.getConnection();
        message = new Message(1,1,false,"TestMessage",1,"TestUser");
        
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
        try (PreparedStatement insertStmt4 = connection.prepareStatement("INSERT INTO message (id,roomId, userId,spoiler,text,username) VALUES (1, 1, 1, false, 'TestMessage','TestUser')")) {
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
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM chatroom WHERE creatorid = 1")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
    
        // Κλείσιμο της σύνδεσης
        if (connection != null) {
            connection.close();
        }
    } 

@Test
    public void getMessageIdTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getMessageId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getMessageId(), resultSet.getInt("id"));
                } else {
                    fail("the Messageid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
@Test
    public void getUserIdTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getUserId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT userid FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getUserId(), resultSet.getInt("userid"));
                } else {
                    fail("the userid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
    @Test
    public void getSpoilerTest() throws SQLException {
        boolean t = false;
    //  // Έλεγχος της μεθόδου getSpoiler
        try (PreparedStatement stmt = connection.prepareStatement("SELECT spoiler FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getInt("spoiler")==0) {
                    t = false;
                    assertEquals(message.getSpoiler(), t);
                }else{
                t = true;
                 assertEquals(message.getSpoiler(), t);
                }
                } else {
                    fail("the spoiler does not match");
                }
            } catch (SQLException e) {
            fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
@Test
    public void getTextTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getText
        try (PreparedStatement stmt = connection.prepareStatement("SELECT text FROM message WHERE Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getText(), resultSet.getString("text"));
                } else {
                    fail("the message text does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
       }
    }
@Test
    public void getChatroomIdTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getRoomId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT roomid FROM message WHERE id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getChatroomId(), resultSet.getInt("roomid"));
                } else {
                    fail("the Roomid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
@Test
    public void getUsernameTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getUsername
        try (PreparedStatement stmt = connection.prepareStatement("SELECT username FROM message WHERE Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(message.getUsername(), resultSet.getString("username"));
                } else {
                    fail("the username does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
       }
    }

}

