package gr.aueb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private String listType; // Τύπος λίστας (public ή private)
    private final String creatorId; // ID του δημιουργού της λίστας
    private List<String> movies; // Λίστα των ταινιών

    public MovieList(String listType, String creatorId) {
        this.listType = listType;
        this.creatorId = creatorId;
        this.movies = new ArrayList<>();
    }

    // Προσθήκη ταινίας στη λίστα
    public void addMovie(String movieName) {
        if (!movies.stream().anyMatch(movieName::equalsIgnoreCase)) {
            movies.add(movieName);
            System.out.println(movieName + " added to the list.");
        } else {
            System.out.println("Movie '" + movieName + "' is already in the list.");
        }
    }

    // Αφαίρεση ταινίας από τη λίστα
    public void removeMovie(String movieName) {
           
        if (movies.contains(movieName)) {
            movies.remove(movieName);
            System.out.println(movieName + " removed from the list.");
        } else {
            System.out.println(movieName + " is not in the list.");
        } 
    }

    // Εμφάνιση των ταινιών στη λίστα
    public void displayMovies() {
        System.out.println("Movies in the list: " + movies);
    }
        //Create List Method
    public void createList(String listType, String listName) throws Exception {
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * FROM list WHERE name=? AND userId=?;";
        String sql = "INSERT INTO List(listType, name, userId) VALUES (?,?,?)";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setString(1, listName);
            stmt1.setInt(2, id);
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                rs.close();
                stmt1.close();
                throw new Exception("List " + listName + " already exists");
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,listType);
            stmt.setString(2, listName);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            System.out.println("List: " + listName + " created successfully");
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
//Get List Method
    public List<String> getLists() throws Exception {
        List<String> lists = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT DISTINCT name FROM List WHERE userId=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                lists.add(rs.getString("name"));
            }
            rs.close();
            stmt.close();
            return lists;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {

            }
        }
    }

// Add to List Method 
    public void addToList(String listName, String movieName, String movieId) throws Exception {
        int listId;
        DB db = new DB();
        Connection con = null;
        String query = "SELECT list_id FROM List WHERE name=? AND userId=?;";
        String sql = "INSERT INTO MoviesList (list_id, movieName, movieId) VALUES (?,?,?);";
            try {
                con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, listName);
                stmt.setInt(2, id);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                listId = rs.getInt("list_id");
                rs.close();
                stmt.close();
                PreparedStatement stmt1 = con.prepareStatement(sql);
                stmt1.setInt(1, listId);
                stmt1.setString(2, movieName);
                stmt1.setString(3, movieId);
                stmt1.executeUpdate();
                System.out.println(movieName + "added to your list " +listName);
                stmt1.close();
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
            } finally {
                try {
                    db.close();
                } catch (Exception e) {

                }
            }
    }

// Get Movies From List Method
    public List<String> getMoviesFromList(String listName) throws Exception {
        int listId;
        List<String> movies = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query1 = "SELECT list_id FROM List WHERE name=? AND userId=?;";
        String query2 = "SELECT movieName FROM MoviesList WHERE list_id=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query1);
            stmt1.setString(1, listName);
            stmt1.setInt(2, id);
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            listId = rs1.getInt("list_id");
            rs1.close();
            stmt1.close();
            PreparedStatement stmt2 = con.prepareStatement(query2);
            stmt2.setInt(1, listId);
            ResultSet rs2 = stmt2.executeQuery();
            while (rs2.next()) {
                movies.add(rs2.getString("movieName"));
            }
            stmt2.close();
            rs2.close();
            return movies;
        } catch (Exception e) {
                    throw new Exception(e.getMessage());
        } finally {
                try {
                    db.close();
                } catch (Exception e) {

                }
        }
    }
// Delete List Method
    public void deleteList(String listName) throws Exception {
        DB db = new DB();
        Connection con = null;
        String sql = "DELETE FROM list WHERE userId=? AND name=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, listName);
            stmt.executeUpdate();
            System.out.println("List: " + listName + " deleted successfully");
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
// Remove movie from List Method
    public void removeMovie(String movieName, String listName) throws Exception {
        int listId;
        DB db = new DB();
        Connection con = null;
        String query = "SELECT list_id FROM List WHERE name=? AND userId=?;";
        String sql = "DELETE FROM MoviesList WHERE list_id=? AND movieName=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setString(1, listName);
            stmt1.setInt(2, id);
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            listId = rs1.getInt("list_id");
            rs1.close();
            stmt1.close();
            PreparedStatement stmt2 = con.prepareStatement(sql);
            stmt2.setInt(1, listId);
            stmt2.setString(2, movieName);
            stmt2.executeUpdate();
            System.out.println("Movie " + movieName + " deleted successfully");
            stmt2.close();
             } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {

            }
        }
    }
}
