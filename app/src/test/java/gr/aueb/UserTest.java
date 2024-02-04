/*
 * UserTest
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This class contains unit tests for the User class.
 * @version 1.8 30 January 2024
 * @author Μαρκέλα Χάσικου
 */

public class UserTest {
    /** The firts user we create to test the methods of the User class. */
    private static User user1;
    /** The second user we create to test the methods of the User class. */
    private static User user2;
    /** The movie list we create to test the methods of the User class. */
    private static MovieList movieList;
    /** The movie Id we create to test the methods of the User class. */
    private int movieId = 58969;
    /** The movie name we create to test the methods of the User class. */
    private String movieName = "test movie";
    /** The room Id we create to test the methods of the User class. */
    private static int roomId = 1;
    /** The chatroom object we create to test the methods of the User class. */
    private static Chatroom chatroom;
    /** The connection object for connection to the database. */
    private static Connection connection;
    
    @BeforeAll
    public static void CreateInserts() throws Exception {
    /**
    * Connection to the database.
    */
    DB db = new DB();
    connection = db.getConnection();
     
    try {
        /**
        * Create 2 users objects of the User class.
        */
        user1 = new User(1,"testUser1", "testPassword1", "testCountry1");
        user2 = new User(2, "testUser2", "testPassword2", "testCountry2");
        
        /**
        * Create a chatroom object of the Chatroom class.
        */
        chatroom = new Chatroom(roomId, "testChatroom", user1.getId());
        /**
        * Create a favorite movieList object of the MovieList class.
        */
        movieList = new MovieList("private", user1.getId(), "favorites", 1);
        
        /**
        * Insert into database user 1.
        */
        try (PreparedStatement insertStmt1 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1, 'testUser1', 'testPassword1', 'testCountry1')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        /**
        * Insert into database favorite list for user 1.
        */
        try (PreparedStatement insertStmt2 = connection.prepareStatement("INSERT INTO List (list_id, listType, name, userId) VALUES(1, 'private', 'favorites', 1)")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        /**
        * Insert into database watchlist list for user 1.
        */
        try (PreparedStatement insertStmt2 = connection.prepareStatement("INSERT INTO List (list_id, listType, name, userId) VALUES(2, 'private', 'watchlist', 1)")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        /**
        * Insert into database user 2.
        */
        try (PreparedStatement insertStmt3 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (2, 'testUser2', 'testPassword2', 'testCountry2')")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        /**
        * Insert into database user 1 follows user2.
        */
        try (PreparedStatement insertStmt4 = connection.prepareStatement("INSERT INTO followers ( followedId, followerId) VALUES (2, 1)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        /**
        * Insert into databse user 1 creates Chatroom with name testChatroom.
        */
        try (PreparedStatement insertStmt5 = connection.prepareStatement("INSERT INTO chatroom ( roomId, name, creatorId) VALUES (1, 'testChatroom', 1)")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }

        /**
        * Insert into database user 2 joins chatroom 1.
        */
        try (PreparedStatement insertStmt5 = connection.prepareStatement("INSERT INTO chatroomuser ( roomId, userId) VALUES (1, 2)")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
       
    } catch (Exception e) {
        fail("Exception thrown: " + e.getMessage());
    }
}

     
    @AfterAll
    public static void DeleteInserts() throws SQLException {
        /**
        * Clean followers database table after tests.
        */
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

        /**
        * Clean chatroomUser database table after tests.
        */
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

        /**
        * Clean list database table after tests.
        */
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

        /**
        * Clean chatroom database table after tests.
        */
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

        /**
        * Clean appuser database table after tests.
        */
        if (user1 != null) {
            try {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM AppUser WHERE userId = ? OR userId=?")) {
                    stmt.setInt(1, user1.getId());
                    stmt.setInt(2, user2.getId());
                    stmt.executeUpdate();
                }
            } catch (SQLException e) {
                fail("Exception thrown during cleanup: " + e.getMessage());
            }
        }

        /**
        * Close db connection after tests.
        */
        if (connection != null) {   
            connection.close();   
        }
    }
    
    /**
     * Test the register method of the User class.
     */
    @Test
    public void testRegisterUser() {
        // Check user's 1 registration
        try {
            assertNotNull(user1);
            assertEquals("testUser1", user1.getUsername());
            assertEquals("testPassword1", user1.getPassword());
            assertEquals("testCountry1", user1.getCountry());
            
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test the login method of the User class.
     */
    @Test
    public void testLoginUser() {
        try {
            // Try user1 to log in
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

    /**
     * Test the followUser method of the User class.
     */
    @Test
    public void testFollowUser() { 
        // Checks if user1 can follow user2
        try {
            assertDoesNotThrow(() -> user1.followUser("testUser2"));
            try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM Followers WHERE followedId = (SELECT userId FROM AppUser WHERE username = ?) AND followerId = ?")) {
            selectStmt.setString(1, "testUser2");
            selectStmt.setInt(2, user1.getId());
            ResultSet resultSet = selectStmt.executeQuery();
            // If there is no row in the result set resultSet.next() returns false
            // This means that user1 could not follow user2 and the following message displayed
            assertTrue(resultSet.next(), "User is not being followed."); 
        }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }

    /**
     * Test the followUser method of the User class if throws the expected Exception
     * if username does not exist
     */
    @Test
    public void followUser_nonexistentUsername_throwException() {
        // Check if there is exception message when user try to follow a user that doesn't exists
        Exception exception = assertThrows(Exception.class, () -> user1.followUser("nonexistentUser"));
        assertEquals("There is no user with username: nonexistentUser", exception.getMessage());
    }

    /**
     * Test the getFollowing method of the User class.
     */
    @Test
    public void testGetFollowingUser() {
        // User 1 follows user2 and then check if list contains user2 that user1 follows
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

    /**
     * Test the getFollowers method of the User class.
     */
    @Test 
    public void testGetFollowersUser() {
        // User1 follows user 2 then check if user1 exists in user2 followers list 
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

    /**
     * Test the joinChatroom method of the User class.
     */
    @Test
    public void testJoinChatroom() throws Exception {
        // Check if user2 can join chatroom 1 
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
    
    /**
     * Test the leaveChatroom method of the User class.
     */
    @Test
    public void testLeaveChatroom() {
        // Check if user2 can leave chatroom 1
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
    
    /**
     * Test the unfollowUser method of the User class.
     */
    @Test
    public void testUnFollowUser() {
        // Check if user1 can unfollow user2 with username:"testUser2"
        try {
            assertDoesNotThrow(() -> user1.unfollowUser("testUser2"));
            try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM Followers WHERE followedId = (SELECT userId FROM AppUser WHERE username = ?) AND followerId = ?")) {
            selectStmt.setString(1, "testUser2");
            selectStmt.setInt(2, user1.getId());
            ResultSet resultSet = selectStmt.executeQuery();
            // If there is row the result set resultSet.next() returns true
            // This means that user1 could not unfollow user2 and the row did not deleted
            assertFalse(resultSet.next(), "Follow relationship is not removed"); 
        }
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }     
    }

    /**
     * Test the getUsersWithPartialUsername method of the User class.
     */
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

    /**
     * Test the doesUsernameExist method of the User class.
     */
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

    /**
     * Test the addToFavorites method of the User class.
     */
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
                assertEquals(1, resultSet.getInt("list_id"));
                assertEquals(movieName, resultSet.getString("movieName"));
                assertEquals(movieId, resultSet.getInt("movieId"));
            } else {
                fail("Movie is not added to the favorites list.");
            }
        }
        }
    }

    /**
     * Test the addToWatchlist method of the User class.
     */
    @Test
    void testAddToWatchlist() throws Exception {
        assertDoesNotThrow(() -> {
            // Add to watchlist movieId, movieName
            user1.addToWatchlist(movieId, movieName);
        });
         // Verify that the movie is added to the MoviesList table
         try (PreparedStatement selectStmt = connection.prepareStatement(
            "SELECT * FROM MoviesList WHERE movieId = ? AND list_id IN (SELECT list_id FROM List WHERE userId = ? AND name = 'watchlist')")) {
        selectStmt.setInt(1, movieId);
        selectStmt.setInt(2, user1.getId());
        try (ResultSet resultSet = selectStmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(2, resultSet.getInt("list_id"));
                assertEquals(movieName, resultSet.getString("movieName"));
                assertEquals(movieId, resultSet.getInt("movieId"));
            } else {
                fail("Movie is not added to the watchlist list.");
            }
        }
        }
    }

   
    /**
     * Test the isMovieInWatchlist method of the User class.
     */
    @Test
    void testIsMovieInWatchlist() throws Exception {
        user1.addToWatchlist(37584, "In time");
        // Assuming movie with ID movieId = 37584 is in the user's watchlist
        boolean result = user1.isMovieInWatchlist(37584);
        assertTrue(result, "Movie 37584 should be in watchlist.");
    }

    /**
     * Test the removeFromWatchlist method of the User class.
     */ 
    @Test
    void testRemoveFromWatchlist() throws Exception {
        assertDoesNotThrow(() -> {
            // Remove from wathclist movie with id = movieId
            user1.removeFromWatchlist(movieId);
            assertFalse(user1.isMovieInWatchlist(movieId), "Movie should be removed from watchlist");
        });
    }
    
    /**
     * Test the removeFromFavorites method of the User class.
     */
    @Test
    void testRemoveFromFavorites() throws Exception {
        assertDoesNotThrow(() -> {
            user1.addToFavorites(458649, "The tourist");
            // Remove from favorites movie with id = movieId
            user1.removeFromFavorites(458649);
            assertFalse(user1.isMovieInFavorites(458649), "Movie should be removed from favorites");
        });
    }
    
    

    /**
     * Test the getCreatedChatrooms method of the User class.
     */
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

    /**
     * Test the getLists method of the User class.
     */
    @Test
    void testGetLists() throws Exception {
        // Check if you can get user1 movielists
        assertDoesNotThrow(() -> {
            user1.getLists();
        });
        ArrayList<MovieList> userLists = user1.getLists();
        try (PreparedStatement selectStmt = connection.prepareStatement(
            "SELECT * FROM List WHERE userId=?;")) {
        selectStmt.setInt(1, user1.getId());
        try (ResultSet resultSet = selectStmt.executeQuery()) {
            if (resultSet.next()) {
                assertEquals(userLists.get(0).getListId(), resultSet.getInt("list_id"));
                assertEquals(userLists.get(0).getCreatorId(), resultSet.getInt("userId"));
                assertEquals(userLists.get(0).getListName(), resultSet.getString("name"));
                assertEquals(userLists.get(0).getListType(), resultSet.getString("listType"));
            } else {
                fail("user has no lists");
            }
        }
        }
    }

    /**
     * Test the isFollowing method of the User class.
     */
    @Test
    void testIsFollowing() {
        // Check if user1 follows user2
        assertDoesNotThrow(() -> {
            user1.isFollowing(user2);
        });
            try {
                assertNotNull(user1);
                assertNotNull(user2);
                assertEquals(true, user1.isFollowing(user2));    
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
        
    /**
     * Test the isNotFollowing method of the User class.
     */
    @Test
    void testIsNotFollowing() {
        // Check if user2 follows user1
        assertDoesNotThrow(() -> {
            user2.isFollowing(user1);
        });
            try {
                assertNotNull(user1);
                assertNotNull(user2);
                assertEquals(false, user2.isFollowing(user1));
            } catch (Exception e) {
                fail("Exception thrown: " + e.getMessage());
            }
        }
        
    /**
     * Test the SetUsername method of the User class.
     */
    @Test
    void testSetUsername() throws Exception {
        // Check if can set new username for user1
        // Call the private method using reflection
        Method updateUsernameMethod = User.class.getDeclaredMethod("updateUsername", String.class, String.class);
        updateUsernameMethod.setAccessible(true);
        updateUsernameMethod.invoke(user1, "newUsername", user1.getPassword());
        // Verify the expected behavior
        assertEquals(user1.getUsername(),"newUsername");
        // Set username back to the first one  to continue testing
        updateUsernameMethod.invoke(user1, "testUsername1", user1.getPassword());
        assertEquals(user1.getUsername(),"testUsername1");   
    }

    /**
     * Test the UpadateUsername method of the User class with wrong Password
     */
    @Test
    void testUpdateUsernameWithWrongPassword() throws Exception {
    // Call the method with the wrong password and verify an exception is thrown
        assertThrows(Exception.class, () -> user1.setUsername("newUsername", "wrongPassword"));
    }

    /**
     * Test the setPassword method of the User class.
     */
    @Test
    void testSetPassword() throws Exception {
        // Check if can set new password for user1
        // Call the private method using reflection
        Method updatePasswordMethod = User.class.getDeclaredMethod("updatePassword", String.class, String.class);
        updatePasswordMethod.setAccessible(true);
        updatePasswordMethod.invoke(user1, "newPassword", user1.getPassword());
        // Verify the expected behavior
        assertEquals(user1.getPassword(),"newPassword");
        // Reset password back to the fisrt one to continue testing
        updatePasswordMethod.invoke(user1, "testPassword1", user1.getPassword());
        assertEquals(user1.getPassword(),"testPassword1");   
    }

    /**
     * Test the updatePassword method of the User class with wrong Password.
     */
    @Test
    void testUpdatePasswordWithWrongPassword() throws Exception {
    // Call the method with the wrong password and verify an exception is thrown
    assertThrows(Exception.class, () -> user1.setPassword("newPassword", "wrongPassword"));
    }

    /**
     * Test the setCountry method of the User class.
     */
    @Test
    void testSetCountry() throws Exception {
        // Check if can set new password for user1
        // call the private method using reflection
        Method updateCountryMethod = User.class.getDeclaredMethod("updateCountry", String.class, String.class);
        updateCountryMethod.setAccessible(true);
        updateCountryMethod.invoke(user1, "newCountry", user1.getPassword());
        // Verify the expected behavior
        assertEquals(user1.getCountry(),"newCountry");
        // Reset country back to the fisrt one
        updateCountryMethod.invoke(user1, "testCountry1", user1.getPassword());
        assertEquals(user1.getCountry(),"testCountry1");  
    }
        
    /**
     * Test the updateCountry method of the User class with wrong Password.
     */
    @Test
    void testUpdateCountryWithWrongPassword() throws Exception {
    // Call the method with the wrong password and verify an exception is thrown
        assertThrows(Exception.class, () -> user1.setCountry("newCountry", "wrongPassword"));
    }
    
}





    


