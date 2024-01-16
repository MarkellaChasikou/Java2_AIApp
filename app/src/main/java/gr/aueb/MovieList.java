package gr.aueb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieList {
    private String listType; // Τύπος λίστας (public ή private)
    private final int creatorId;
    private String listName;
    private final int listId; // ID του δημιουργού της λίστας

    public MovieList(String listType, int creatorId, String listName, int listId) {
        this.listType = listType;
        this.creatorId = creatorId;
        this.listName = listName;
        this.listId = listId;
    }

    public String getListType() {
        return listType;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getListName() {
        return listName;
    }

    public int getListId() {
        return listId;
    }

    public void setListType(String listType, int userId) throws Exception {
        if (userId == creatorId) {
            this.listType = listType;
            updateListTypeInDatabase();
        } else {
            throw new Exception("User does not have permission to change the list type.");
        }
    }

    public void setListName(String listName, int userId) throws Exception {
        if (userId == creatorId) {
            this.listName = listName;
            updateListNameInDatabase();
        } else {
            throw new Exception("User does not have permission to change the list name.");
        }
    }

    // Database update methods
    private void updateListTypeInDatabase() throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE List SET listType = ? WHERE list_id = ?")) {

            stmt.setString(1, listType);
            stmt.setInt(2, listId);
            stmt.executeUpdate();

            System.out.println("List type updated in the database");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void updateListNameInDatabase() throws Exception {
        try (DB db = new DB();
                Connection con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement("UPDATE List SET name = ? WHERE list_id = ?")) {

            stmt.setString(1, listName);
            stmt.setInt(2, listId);
            stmt.executeUpdate();

            System.out.println("List name updated in the database");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Create List Method
    public static MovieList createList(String listType, String listName, int creatorId) throws Exception {
        int listId;

        try (DB db = new DB();
                Connection con = db.getConnection()) {

            String query = "SELECT * FROM List WHERE name=? AND userId=?;";
            String insertSql = "INSERT INTO List(listType, name, userId) VALUES (?,?,?)";
            String selectSql = "SELECT list_id FROM List WHERE name=? AND userId=?";

            // Check if the list already exists
            try (PreparedStatement checkStmt = con.prepareStatement(query)) {
                checkStmt.setString(1, listName);
                checkStmt.setInt(2, creatorId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    throw new Exception("List " + listName + " already exists");
                }
            }

            // Insert new list
            try (PreparedStatement insertStmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, listType);
                insertStmt.setString(2, listName);
                insertStmt.setInt(3, creatorId);
                insertStmt.executeUpdate();

                // Retrieve the generated list_id
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        listId = generatedKeys.getInt(1);
                    } else {
                        throw new Exception("Failed to retrieve generated list_id.");
                    }
                }
            }

            System.out.println("List: " + listName + " created successfully");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // Return a new MovieList object with the obtained listId
        return new MovieList(listType, creatorId, listName, listId);
    }

    // Add to List Method
    public void addToList(String movieName, String movieId, int userId) throws Exception {
        int listId;

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query = "SELECT list_id FROM List WHERE name=? AND userId=?;";
            String sql = "INSERT INTO MoviesList (list_id, movieName, movieId) VALUES (?,?,?);";

            // Check if the user is the creator of the list
            try (PreparedStatement checkStmt = con.prepareStatement(query)) {
                checkStmt.setString(1, listName);
                checkStmt.setInt(2, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new Exception("User is not the creator of the list or the list does not exist.");
                    }
                    listId = rs.getInt("list_id");
                }
            }

            // Insert the movie into the list
            try (PreparedStatement stmt1 = con.prepareStatement(sql)) {
                stmt1.setInt(1, listId);
                stmt1.setString(2, movieName);
                stmt1.setString(3, movieId);
                stmt1.executeUpdate();
                System.out.println(movieName + " added to your list " + listName);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Get Movies From List Method
    public Map<String, String> getMoviesFromList(String listName) throws Exception {
        int listId;
        Map<String, String> movies = new HashMap<>();

        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query1 = "SELECT list_id FROM List WHERE name=?;";
            String query2 = "SELECT movieName, movieId FROM MoviesList WHERE list_id=?;";

            // Get the list ID
            try (PreparedStatement stmt1 = con.prepareStatement(query1)) {
                stmt1.setString(1, listName);
                try (ResultSet rs1 = stmt1.executeQuery()) {
                    if (!rs1.next()) {
                        throw new Exception("List " + listName + " not found.");
                    }
                    listId = rs1.getInt("list_id");
                }
            }

            // Get movies from the list
            try (PreparedStatement stmt2 = con.prepareStatement(query2)) {
                stmt2.setInt(1, listId);
                try (ResultSet rs2 = stmt2.executeQuery()) {
                    while (rs2.next()) {
                        String movieName = rs2.getString("movieName");
                        String movieId = rs2.getString("movieId");
                        movies.put(movieName, movieId);
                    }
                }
            }

            return movies;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Delete List Method
    public void deleteList(int userId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "DELETE FROM list WHERE userId=? AND list_id=?;";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, listId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("List deleted successfully");
                } else {
                    throw new Exception("List not found or user is not the creator.");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Remove movie from List Method
    public void removeMovie(String movieName, int userId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String query = "SELECT userId FROM List WHERE list_id=?;";
            String sql = "DELETE FROM MoviesList WHERE list_id=? AND movieName=?;";

            // Check if the user is the creator of the list
            try (PreparedStatement checkStmt = con.prepareStatement(query)) {
                checkStmt.setInt(1, listId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next() || rs.getInt("userId") != userId) {
                        throw new Exception("User is not the creator of the list or the list does not exist.");
                    }
                }
            }

            // Remove the movie from the list
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, listId);
                stmt.setString(2, movieName);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Movie deleted successfully");
                } else {
                    throw new Exception("Movie not found in the list.");
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Retrieve Movie Lists Method
    public static List<MovieList> getMovieLists(int userId) throws Exception {
        List<MovieList> movieLists = new ArrayList<>();
        try (DB db = new DB();
                Connection con = db.getConnection()) {

            String sql = "SELECT * FROM List WHERE listType IN ('public', 'protected') AND userId=?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, userId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String listType = rs.getString("listType");
                        String listName = rs.getString("name");
                        int listId = rs.getInt("list_id");
                        int creatorId = rs.getInt("userId");

                        // For protected lists, check if the user is a follower of the list creator
                        if (listType.equalsIgnoreCase("protected") && !isFollower(userId, creatorId, con)) {
                            continue; // Skip this list if not a follower
                        }

                        movieLists.add(new MovieList(listType, creatorId, listName, listId));
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return movieLists;
    }

    // Helper method to check if the user is a follower of the list creator
    private static boolean isFollower(int userId, int creatorId, Connection con) throws SQLException {
        String followerSql = "SELECT * FROM Followers WHERE followedId=? AND followerId=?";
        try (PreparedStatement followerStmt = con.prepareStatement(followerSql)) {
            followerStmt.setInt(1, creatorId);
            followerStmt.setInt(2, userId);

            try (ResultSet followerRs = followerStmt.executeQuery()) {
                return followerRs.next();
            }
        }
    }

    // Check if the movie with the given name or ID exists in the list
    public boolean containsMovie(String movieName, String movieId) throws Exception {
        try (DB db = new DB(); Connection con = db.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM MoviesList WHERE list_id=? AND (movieName=? OR movieId=?);";

            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, listId);
                stmt.setString(2, movieName);
                stmt.setString(3, movieId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int count = rs.getInt("count");
                        return count > 0;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return false;
    }

    @Override
    public String toString() {
        return "MovieList{" +
                "listType='" + listType + '\'' +
                ", creatorId=" + creatorId +
                ", listName='" + listName + '\'' +
                ", listId=" + listId +
                '}';
    }
}
