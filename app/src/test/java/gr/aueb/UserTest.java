package gr.aueb;


import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserTest {

    private static User user1;
    private static User user2;
    private static MovieList movieList;
    private int movieId = 58969;
    private String movieName = "top gun";
    private static int roomId = 1;
    private static Chatroom chatroom;
    private static Connection connection;
    
    @BeforeAll
public static void CreateInserts() throws Exception {
    // Σύνδεση στη βάση δεδομένων για τα tests
    DB db = new DB(); 
    connection = db.getConnection();
    try {
        // create 2 users
        user1 = new User(1,"testUser1", "testPassword1", "testCountry1");
        user2 = new User(2, "testUser2", "testPassword2", "testCountry2");
        
        // create a chatroom with chatroomid:1 and creatorId:1
        chatroom = new Chatroom(roomId, "friends", 1);

        // create a favorite movieList object for user1
        movieList = new MovieList("private", 1, "favorites", 1);

        // pass values in mysql database
        try (PreparedStatement insertStmt1 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1, 'testUser1', 'testPassword1', 'testCountry1')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("INSERT INTO List (list_id, listType, name, userId) VALUES(1, 'private', 'favorites', 1)")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (2, 'testUser2', 'testPassword2', 'testCountry2')")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("INSERT INTO chatroom ( roomId, name, creatorId) VALUES (1, 'friends', 1)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        // call login method for user1
        user1 = User.login("testUser1", "testPassword1");
       
    } catch (Exception e) {
        fail("Exception thrown: " + e.getMessage());
    }
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
                    stmt.setInt(1, roomId);
                    stmt.setInt(2, user2.getId());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                fail("Exception thrown during cleanup: " + e.getMessage());
            }
        }

         // clean list table after tests
         if (user1 != null) {
            try {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM list WHERE userId=? OR userId=?")) {
                    stmt.setInt(1, user1.getId());
                    stmt.setInt(2, user2.getId());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                fail("Exception thrown during cleanup: " + e.getMessage());
            }
        }

        // clean chatroom table after tests
        if (user1 != null) {
            try {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM chatroom WHERE roomId=?")) {
                    stmt.setInt(1, roomId);
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                fail("Exception thrown during cleanup: " + e.getMessage());
            }
        }
        // clean appuser table after tests
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
        // close db connection
        if (connection != null) {
            connection.close();
           
        }
    }
    
    public void testRegisterUser() {
        // check user's 1 registration
        try {
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
        // check if a user can log in
        try {
            //try user1 to log in
            assertDoesNotThrow(() -> {
                User user1 = User.login("testUser1", "testPassword1");
            assertNotNull(user1);
            assertEquals("testUser1", user1.getUsername());
            assertEquals("testPassword1", user1.getPassword());
            assertEquals("testCountry1", user1.getCountry());
            });
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
        //checks if user1 can follow user2
        try {
            assertDoesNotThrow(() -> user1.followUser("testUser2"));
            try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM Followers WHERE followedId = (SELECT userId FROM AppUser WHERE username = ?) AND followerId = ?")) {
            selectStmt.setString(1, "testUser2");
            selectStmt.setInt(2, user1.getId());
            ResultSet resultSet = selectStmt.executeQuery();
            //if there is no row in the result set resultSet.next() returns false
            //this means that user1 could not follow user2 and the following message displayed
            assertTrue(resultSet.next(), "User is not being followed."); 
        }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }

    @Test
    public void followUser_nonexistentUsername_throwException() {
        // check if there is exception message when user try to follow a user that doesn't exists
        Exception exception = assertThrows(Exception.class, () -> user1.followUser("nonexistentUser"));
        assertEquals("There is no user with username: nonexistentUser", exception.getMessage());
    }

    @Test
    public void testGetFollowingUser() {
        // user 1 follows user2 and then check if list contains user2 that user1 follows
        assertDoesNotThrow(() -> {
            user1.followUser("testUser2");
            List<User> followings = user1.getFollowing();
            assertNotNull(followings);
            assertEquals(1, followings.size());
            User resultUser = followings.get(0);
            assertNotNull(resultUser);
            assertEquals(2, resultUser.getId());
            assertEquals("testUser2", resultUser.getUsername());
            assertEquals("testPassword2", resultUser.getPassword());
            assertEquals("testCountry2", resultUser.getCountry());
        });
    }

    @Test 
    public void testGetFollowersUser() {
        // user1 follows user 2 then check if user1 exists in user2 followers list 
        assertDoesNotThrow(() -> {
            user1.followUser("testUser2");
            List<User> followings = user2.getFollowers();
            assertNotNull(followings);
            assertEquals(1, followings.size());
            User resultUser = followings.get(0);
            assertNotNull(resultUser);
            assertEquals(1, resultUser.getId());
            assertEquals("testUser1", resultUser.getUsername());
            assertEquals("testPassword1", resultUser.getPassword());
            assertEquals("testCountry1", resultUser.getCountry());
        });   
    }

    @Test
    public void testJoinChatroom() throws Exception {
        // check if user2 can join chatroom 1 
        assertDoesNotThrow(() -> {
            user2.joinChatroom(roomId);
        });
        // Verify that the user is added to the ChatroomUser table
        try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM ChatroomUser WHERE roomId = ? AND userId = ?")) {
            selectStmt.setInt(1, roomId);
            selectStmt.setInt(2, user2.getId());
            try (ResultSet resultSet = selectStmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(roomId, resultSet.getInt("roomId"));
                    assertEquals(user2.getId(), resultSet.getInt("userId"));
                } else {
                    fail("User is not added to the chatroom.");
                }
            }
        }
    }
     
    @Test
    public void testLeaveChatroomUser() {
        // check if user2 can leave chatroom 1
        try {
            assertDoesNotThrow(() -> user2.leaveChatroom(roomId));
            try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM ChatroomUser WHERE roomId = ? AND userId = ?")) {
            selectStmt.setInt(1, roomId);
            selectStmt.setInt(2, user2.getId());
            ResultSet resultSet = selectStmt.executeQuery();
            assertFalse(resultSet.next(), "User is not removed from the chatroom.");
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
    
     
    @Test
    public void testUnFollowUser() {
        // check if user1 can unfollow user2
        try {
            //user1 unfollows user2 with username:"testUser2"
            assertDoesNotThrow(() -> user1.unfollowUser("testUser2"));
            try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM Followers WHERE followedId = (SELECT userId FROM AppUser WHERE username = ?) AND followerId = ?")) {
            selectStmt.setString(1, "testUser2");
            selectStmt.setInt(2, user1.getId());
            ResultSet resultSet = selectStmt.executeQuery();
            //if there is row the result set resultSet.next() returns true
            //this means that user1 could not unfollow user2 and the row did not deleted
            assertFalse(resultSet.next(), "Follow relationship is not removed"); 
        }
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

    @Test
    void testAddToFavorites() throws Exception {
        assertDoesNotThrow(() -> {
            user1.addToFavorites(movieId, movieName);
        });
         // Verify that the movie is added to the MoviesList table
        try (PreparedStatement selectStmt = connection.prepareStatement(
            "SELECT * FROM MoviesList WHERE movieId = ? AND list_id IN (SELECT list_id FROM List WHERE userId = ? AND name = 'favorites')")) {
        selectStmt.setInt(1, movieId);
        selectStmt.setInt(2, user1.getId());
        try (ResultSet resultSet = selectStmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(movieList.getListId(), resultSet.getInt("list_id"));
                assertEquals(movieName, resultSet.getString("movieName"));
                assertEquals(movieId, resultSet.getInt("movieId"));
            } else {
                fail("Movie is not added to the favorites list.");
            }
        }
        }
    }

    @Test
    void testGetCreatedChatrooms() throws Exception {
    assertDoesNotThrow(() -> {
        List<Chatroom> createdChatrooms = user1.getCreatedChatrooms();
        // Assertions
        assertNotNull(createdChatrooms);
        assertEquals(1, createdChatrooms.size());
        // Verify chatroom names and creatorId
        assertEquals(chatroom.getName(), createdChatrooms.get(0).getName());
        assertEquals(roomId, createdChatrooms.get(0).getRoomId());
        assertEquals(user1.getId(), createdChatrooms.get(0).getCreatorId());
    });
}


}




    


