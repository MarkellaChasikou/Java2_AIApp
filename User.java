import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class User {
    private int id;
    private String username;
    private String password;
    private String country;
    
//Constructors
    public User(String username, String password, String country) {
        this.username = username;
        this.password = password;
        this.country = country;
        }

    public User(int id, String username, String password, String country) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.country = country;
        }
 
    // Setters
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
       this.password = password;
    }

    public void setCountry(String country) {
       this.country = country;
    }

    //Getters
     public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }
    
// Login Method
    public static User login(String username, String password) throws Exception {
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * FROM AppUser WHERE username=? AND pass_word=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                rs.close();
                stmt.close();
                db.close();
                return null;   
            }
            User user = new User(rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("pass_word"),
                    rs.getString("country"));
            rs.close();
            stmt.close();
            db.close();
            return user;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            
            }
        }
    }

//Register Method
    public static void register(User user) throws Exception {	
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * FROM AppUser WHERE username=?";
        String sql = "INSERT INTO AppUser (username, pass_word, country) VALUES(?, ?, ?);";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setString(1, user.getUsername());
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                rs.close();
                stmt1.close();
                db.close();
                throw new Exception("Sorry, username already registered");
            }
            PreparedStatement stmt2 = con.prepareStatement(sql);
            stmt2.setString(1, user.getUsername());
            stmt2.setString(2, user.getPassword());
            stmt2.setString(3, user.getCountry());
            stmt2.executeUpdate();
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

//Follow user
    public static void followUser(User user, String follow_user) {
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * FROM AppUser WHERE username=?";
        String sql = "INSERT INTO Followers (followedId, followerId) VALUES(?, ?);";
        try {
			con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setString(1, follow_user);
            ResultSet rs = stmt1.executeQuery();
            if (!rs.next()) {
                rs.close();
                stmt1.close();
                db.close();
                throw new Exception ("there is no user with username:" + follow_user);
            }
            int follow_id = rs.getInt("userId");
            PreparedStatement stmt2 = con.prepareStatement(sql);
			stmt2.setInt(1, follow_id);
			stmt2.setInt(2, user.getId());
			stmt2.executeUpdate();
			stmt2.close();
        } catch (Exception e) {
           
        } finally {
        try {
            db.close();
        } catch (Exception e) {
            
        }
    }
        
    } 

//Unfollow user
    public static void unfollowUser(User user, String unfollow_user) {
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * FROM AppUser WHERE username=?;";
        String sql = "DELETE FROM Followers WHERE followedId = ? AND followerId = ?;";
        try {
			con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setString(1, unfollow_user);
            ResultSet rs = stmt1.executeQuery();
            if (!rs.next()) {
                rs.close();
                stmt1.close();
                db.close();
                throw new Exception ("there is no user with username:" + unfollow_user);
            }
            int unfollow_id = rs.getInt("userId");
            PreparedStatement stmt2 = con.prepareStatement(sql);
			stmt2.setInt(1, unfollow_id);
			stmt2.setInt(2, user.getId());
			stmt2.executeUpdate();
			stmt2.close();
        } catch (Exception e) {
           
        } finally {
            try {
            db.close();
            } catch (Exception e) {
            
            }
        }    
    } 
//Get Followings Method
    public static List<String> getFollowing(User user) throws Exception {
        List<String> followings = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT username FROM AppUser JOIN Followers ON "  
                        +"Followers.followedId = AppUser.userid "
                        +"WHERE followerId=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                followings.add(rs.getString("username"));
            }
            stmt.close();
            rs.close();
            db.close();
            return followings;
        } catch (Exception e) {
            throw new Exception (e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            
            }
        }    
    } 

//Get Followers Method
    public static List<String> getFollowers(User user) throws Exception {
        List<String> followers = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT username FROM AppUser JOIN Followers ON "  
                        +"Followers.followerId = AppUser.userid "
                        +"WHERE followedId=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                followers.add(rs.getString("username"));
            }
            stmt.close();
            rs.close();
            db.close();
            return followers;
        } catch (Exception e) {
            throw new Exception (e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
            
            }
        }    
    }
//Create List Method
    public static void createList(String listType, String listName, User user) throws Exception {
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * FROM list WHERE name=? AND userId=?;";
        String sql = "INSERT INTO List(listType, name, userId) VALUES (?,?,?)";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setString(1, listName);
            stmt1.setInt(2,user.getId());
            ResultSet rs = stmt1.executeQuery();
            if (rs.next()) {
                rs.close();
                stmt1.close();
                throw new Exception("List " + listName + " already exists");
            }
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,listType);
            stmt.setString(2, listName);
            stmt.setInt(3,user.getId());
            stmt.executeUpdate();
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
    public static List<String> getLists(User user) throws Exception {
        List<String> lists = new ArrayList<String>();
        DB db = new DB();
        Connection con = null;
        String query = "SELECT DISTINCT name FROM List WHERE userId=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, user.getId());
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
    public static void addToList(String listName, String movieName, String movieId, User user) throws Exception {
        int listId;
        DB db = new DB();
        Connection con = null;
        String query = "SELECT list_id FROM List WHERE name=? AND userId=?;";
        String sql = "INSERT INTO MoviesList (list_id, movieName, movieId) VALUES (?,?,?);";
            try {
                con = db.getConnection();
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, listName);
                stmt.setInt(2,user.getId());
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
    public static List<String> getMoviesFromList(String listName, User user) throws Exception {
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
            stmt1.setInt(2, user.getId());
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
    public static void deleteList(String listName, User user) throws Exception {
        DB db = new DB();
        Connection con = null;
        String sql = "DELETE FROM list WHERE userId=? AND name=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, user.getId());
            stmt.setString(2, listName);
            stmt.executeUpdate();
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
    public static void removeMovie(String movieName, String listName, User user) throws Exception {
        int listId;
        DB db = new DB();
        Connection con = null;
        String query = "SELECT list_id FROM List WHERE name=? AND userId=?;";
        String sql = "DELETE FROM MoviesList WHERE list_id=? AND movieName=?;";
        try {
            con = db.getConnection();
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setString(1, listName);
            stmt1.setInt(2, user.getId());
            ResultSet rs1 = stmt1.executeQuery();
            rs1.next();
            listId = rs1.getInt("list_id");
            rs1.close();
            stmt1.close();
            PreparedStatement stmt2 = con.prepareStatement(sql);
            stmt2.setInt(1, listId);
            stmt2.setString(2, movieName);
            stmt2.executeUpdate();
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

















