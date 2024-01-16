package gr.aueb;


import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gr.aueb.DB;
import gr.aueb.User;

public class UserTest {

    private static User user1;
    private static User user2;
    private static Chatroom chatroom;
    private static Connection connection;
    
    @BeforeAll
public static void CreateInserts() throws Exception {
    // Σύνδεση στη βάση δεδομένων για τα tests
    DB db = new DB(); 
    connection = db.getConnection();
    try {
        // Προσθήκη χρήστη στη βάση δεδομένων
        user1= User.register("testUser1", "testPassword1", "testCountry1");
        user2= User.register("testUser2", "testPassword2", "testCountry2");
        // Σύνδεση χρήστη στην εφαρμογή
        user1 = User.login("testUser1", "testPassword1");
       
    } catch (Exception e) {
        fail("Exception thrown: " + e.getMessage());
    }
    //assertNotNull(user1);
    //assertNotNull(user2);
}

     
    @AfterAll
    public static void DeleteInserts() throws SQLException {
        // Καθαρισμός του πίνακα followers μετά τα tests
        if (user1 != null) {
            try {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM followers WHERE followerId = ?")) {
                    stmt.setInt(1, user1.getId());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                fail("Exception thrown during cleanup: " + e.getMessage());
            }
        }
        // Καθαρισμός του πίνακα chatroomUser μετά τα tests
        if (user1 != null) {
            try {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM chatroomuser WHERE roomId=? AND userId=?")) {
                    stmt.setInt(1, 1);
                    stmt.setInt(2, user2.getId());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                fail("Exception thrown during cleanup: " + e.getMessage());
            }
        }
        // Καθαρισμός του πίνακα appuser μετά τα tests
        if (user1 != null) {
            try {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM AppUser WHERE userId = ? OR userId = ?")) {
                    stmt.setInt(1, user1.getId());
                    stmt.setInt(2, user2.getId());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                fail("Exception thrown during cleanup: " + e.getMessage());
            }
        }
        // Κλείσιμο της σύνδεσης
        if (connection != null) {
            connection.close();
        }
    }
    

    @Test
    public void testRegisterUser() {
        // Ελέγχουμε αν η εγγραφή νέου χρήστη πραγματοποιείται σωστά
        try {

            // Έλεγχος αν η εγγραφή των χρηστών έγινε σωστά
            assertNotNull(user1);
            assertEquals("testUser1", user1.getUsername());
            assertEquals("testPassword1", user1.getPassword());
            assertEquals("testCountry1", user1.getCountry());
            
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        } 
            
        
    }

    @Test
    public void testLoginUser() {
        // Ελέγχουμε αν η σύνδεση νέου χρήστη πραγματοποιείται σωστά
        try {

            // Έλεγχος αν η σύνδεση έγινε σωστά
            assertNotNull(user1);
            assertEquals("testUser1", user1.getUsername());
            assertEquals("testPassword1", user1.getPassword());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }

    /*@Test
    public void testSetUsernameUser() {
        // Ελέγχουμε αν η αλλάγη username πραγματοποιείται σωστά
        try {
            user.setUsername("newUsername", "testPassword");
            assertEquals("newUsername", user.getUsername());
        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }*/

    @Test
    public void testFollowUser() {
        // Ελέγχουμε αν ακολουθεί το χρήστη 
        try {
            assertDoesNotThrow(() -> user1.followUser("testUser2"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }

    @Test
    public void followUser_nonexistentUsername_throwException() {
        // Ελέγχουμε αν εμφανίζει μήνυμα εξαίρεσης όταν ακολουθεί χρήστη που δεν υπάρχει το username
        Exception exception = assertThrows(Exception.class, () -> user1.followUser("nonexistentUser"));
        assertEquals("There is no user with username: nonexistentUser", exception.getMessage());
    }

    /*@Test
    public void testGetFollowingUser() {
        // Ελέγχουμε αν εμφανίζονται στη λίστα οι users που ακολουθεί o user1
        try {
            List<String> followingList = user1.getFollowing();
            assertTrue(followingList.contains("testUser2"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }*/

    @Test
    public void testJoinChatroom() {
        // Ελέγχουμε αν μπορεί να κανει join το chatroom
        try {
            // Δημιουργία chatroom ***to chatroom 1 uparxei idi sti vasi ***
            // prepei na kanw create chatroom kanonika
            Chatroom chatroom = new Chatroom(1, "testChatroom", user1.getId());
            // user2 join chatroom
            assertDoesNotThrow(() -> user2.joinChatroom(chatroom.getRoomId()));  
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }

    /*den einai sosti eno tha eprepe to chekara kai sto terminal vgazei sosto apotelesma
     @Test 
    public void testGetFollowingUser() {
        // Ελέγχουμε αν εμφανίζονται στη λίστα οι users που ακολουθεί o user1
        try {
            List<String> followingList = user1.getFollowing();
            assertTrue(followingList.contains("testUser2"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    } */

    
     
    @Test
    public void testLeaveChatroomUser() {
        // Ελέγχουμε αν μπορεί να βγει απο chatroom
        try {
            //user2 leaves chatroomId: 1
            assertDoesNotThrow(() -> user2.leaveChatroom(1));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }
    
     
    @Test
    public void testUnFollowUser() {
        // Ελέγχουμε αν κάνει σωστά unfollow το χρήστη 
        try {
            //user1 unfollows user with username:"testUser2"
            assertDoesNotThrow(() -> user1.unfollowUser("testUser2"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }

    @Test
    void testGetUsersWithPartialUsername() {
        try {
            // Call the getUsersWithPartialUsername method
            List<User> users = User.getUsersWithPartialUsername("test");
            // Verify the results
            assertEquals(2, users.size()); 
            assertTrue(users.stream().anyMatch(u -> u.getUsername().equals(user1.getUsername())));
            assertTrue(users.stream().anyMatch(u -> u.getUsername().equals(user2.getUsername())));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testDoesUsernameExist() {
        try {
            // Call the doesUsernameExist method
            assertTrue(User.doesUsernameExist("testUser1"));
            assertFalse(User.doesUsernameExist("nonExistingUser"));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
    


