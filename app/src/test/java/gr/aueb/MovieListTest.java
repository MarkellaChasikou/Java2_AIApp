package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MovieListTest {
private static Connection connection;
private static MovieList movielist;


@BeforeAll
    public static void CreateInserts() throws Exception{
        DB db = new DB(); 
        connection = db.getConnection();
        movielist = new MovieList("public",1,"list1",1);
        try (PreparedStatement insertStmt1 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (1, 'User1', 'TestPassword', 'TestCountry')")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("INSERT INTO appuser ( userId, username, pass_word, country) VALUES (2, 'User2', 'TestPassword', 'TestCountry')")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("INSERT INTO list (list_Id, listType, name, userid) VALUES (1, 'public', 'list1', 1)")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("INSERT INTO list (list_Id, listType, name, userid) VALUES (2, 'protected', 'list2', 1)")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection.prepareStatement("INSERT INTO list (list_Id, listType, name, userid) VALUES (3, 'private', 'list3', 1)")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement("INSERT INTO list (list_Id, listType, name, userid) VALUES (4, 'protected', 'list4', 2)")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt7 = connection.prepareStatement("INSERT INTO followers (followedid,followerid) VALUES (1, 2)")) {
            insertStmt7.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt8 = connection.prepareStatement("INSERT INTO movieslist (list_Id, moviename, movieid) VALUES (1, 'movie1',  1)")) {
            insertStmt8.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt9 = connection.prepareStatement("INSERT INTO movieslist (list_Id, moviename, movieid) VALUES (2, 'movie1',  1)")) {
            insertStmt9.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt10 = connection.prepareStatement("INSERT INTO movieslist (list_Id, moviename, movieid) VALUES (3, 'movie1',  1)")) {
            insertStmt10.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
}
@AfterAll
    public static void DeleteAllInserts() throws Exception {
        try (PreparedStatement insertStmt1 = connection.prepareStatement("DELETE FROM movieslist WHERE movieid = 1")) {
            insertStmt1.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt2 = connection.prepareStatement("DELETE FROM list WHERE userid = 1")) {
            insertStmt2.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt3 = connection.prepareStatement("DELETE FROM list WHERE userid = 2")) {
            insertStmt3.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt4 = connection.prepareStatement("DELETE FROM followers WHERE followerId = 2")) {
            insertStmt4.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt5 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 1")) {
            insertStmt5.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        try (PreparedStatement insertStmt6 = connection.prepareStatement("DELETE FROM appuser WHERE userid = 2")) {
            insertStmt6.executeUpdate();
        } catch (SQLException e) {
            fail("Exception thrown during setup: " + e.getMessage());
        }
        // Κλείσιμο της σύνδεσης
        if (connection != null) {
            connection.close();
        }
    }
@Test
    public void getListTypeTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getListTypeTest
        try (PreparedStatement stmt = connection.prepareStatement("SELECT listType FROM list WHERE list_id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(movielist.getListType(), resultSet.getString("listType"));
                } else {
                    fail("the listType does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
@Test
    public void getCreatorIdTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getCreatorId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT userid FROM list WHERE list_Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(movielist.getCreatorId(), resultSet.getInt("userid"));
                } else {
                    fail("the userid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
    @Test
    public void getListNameTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getListName
        try (PreparedStatement stmt = connection.prepareStatement("SELECT name FROM list WHERE list_Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(movielist.getListName(), resultSet.getString("name"));
                } else {
                    fail("the name does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
    @Test
    public void getListIdTest() throws SQLException {
    //  // Έλεγχος της μεθόδου getCreatorId
        try (PreparedStatement stmt = connection.prepareStatement("SELECT list_id FROM list WHERE list_Id = 1")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    assertEquals(movielist.getListId(), resultSet.getInt("list_id"));
                } else {
                    fail("the listid does not match");
                }
            } catch (SQLException e) {
                fail("Exception thrown during select: " + e.getMessage());
            }
        }
    }
}


