
import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class User {
    private String username;
    private String password;
    private List<String> followers;
    private List<String> following;
    
    //Constructor

    public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

    //User toString() method
    @Override
    public String toString() {
        return  "\nUsername:" + this.username + 
                "\nPassword:" + this.password +
                "\nFollowers:" + this.followers + "total followers:" + followers.size() +
                "\nFollowing:" + this.following + "total following:" + following.size() ;
    }

    
    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword( String password) {
       this.password = password;
    }

    //Getters

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getFollowing() {
        return following;
    }
    
    //Follow user
    public void followUser(User user) {
        if (following.contains(user.getUsername()) == false) {
        following.add(user.getUsername());
        user.followers.add(this.getUsername());
        }
        else {
            System.out.println(username + " you are already follow: " + user.getUsername());
        }
    }

   // Authenticate Method
    public static User authenticate(String username, String password) throws Exception {
        DB db = new DB();
        Connection con = null;
        String query = "SELECT * FROM users WHERE username=? AND password=?;";
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
                throw new Exception("Wrong username or password");
            }
            User user = new User(rs.getString("username"),
                        rs.getString("password"));
            System.out.println("Logged in");
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
    public void register(User user) throws Exception {	
		DB db = new DB();
		Connection con = null;
		String query = "SELECT * FROM users WHERE username=?";
		String sql = "INSERT INTO users (username, password) VALUES(?, ?);";
		try {
			con = db.getConnection();
			PreparedStatement stmt1 = con.prepareStatement(query);
			stmt1.setString(1, user.getUsername());
			ResultSet rs = stmt1.executeQuery();
			if (rs.next()) {
				rs.close();
				stmt1.close();
				db.close();
				throw new Exception("Sorry, username " + user.getUsername() + " is already used.Please choose another one");
			}
			PreparedStatement stmt2 = con.prepareStatement(sql);
			stmt2.setString(1, user.getUsername());
			stmt2.setString(2, user.getPassword());
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

    //Get UserId Method 
    public static int getUserid(String username) throws Exception {
        DB db = new DB();
		Connection con = null;
        String query = "SELECT user_id FROM users WHERE username=?";
        try {
			con = db.getConnection();
			PreparedStatement stmt1 = con.prepareStatement(query);
			stmt1.setString(1, username);
			ResultSet rs = stmt1.executeQuery();
            if (!rs.next()) {
				rs.close();
				stmt1.close();
				db.close();
				throw new Exception("There is no user " + username);
			}
            return rs.getInt("user_id");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                db.close();
            } catch (Exception e) {
                
            }
        }
	}

    //Add toList
    public static void addToList(User user, String listType, String filmName) throws Exception {	
	    DB db = new DB();
		Connection con = null;
		String query = "SELECT * FROM lists WHERE user_id=? AND list_type=? AND film_name=?";
		String sql = "INSERT INTO lists VALUES(?, ?, ?);";
		try {
			con = db.getConnection();
			PreparedStatement stmt1 = con.prepareStatement(query);
			stmt1.setInt(1, getUserid(user.getUsername()));
            stmt1.setString(2, listType);
            stmt1.setString(3, filmName);
			ResultSet rs = stmt1.executeQuery();
			if (rs.next()) {
				rs.close();
				stmt1.close();
				db.close();
				throw new Exception(filmName + " is already in your list");
			}
			PreparedStatement stmt2 = con.prepareStatement(sql);
			stmt2.setInt(1, getUserid(user.getUsername()));
            stmt2.setString(2, listType);
			stmt2.setString(3, filmName);
			stmt2.executeUpdate();
            System.out.println(filmName + " is now in " +listType);
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

    //Get List
    public static List<String> getList(User user, String listType) throws Exception {
        List<String> myList = new ArrayList<String>();
		DB db = new DB();
		Connection con = null;
		String query = "SELECT * FROM lists WHERE user_id=? AND list_type=?";
		try {
			con = db.getConnection();
			PreparedStatement stmt1 = con.prepareStatement(query);
			stmt1.setInt(1, getUserid(user.getUsername()));
            stmt1.setString(2, listType);
			ResultSet rs = stmt1.executeQuery();
			while (rs.next()) {
                myList.add(rs.getString("film_name"));	
			}
            rs.close();
            stmt1.close();
            db.close();
            System.out.println("Your " + listType + ":");
            return myList;
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













