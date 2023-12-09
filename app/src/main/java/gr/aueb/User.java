package gr.aueb;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;


public class User {
    private String username;
    private String password;
    private List<String> followers;
    private List<String> following;
    private List<String> favorites;
    private List<String> watchList;
    private List<String> createdList;
    
    //Constructors

    public User(){ }

    public User(String username, String password) {
            this.username = username;
            this.password = password;
            /*this.followers = new ArrayList<>();
            this.following = new ArrayList<>();
            this.favorites = new ArrayList<>();
            this.watchList = new ArrayList<>();
            this.createdList = new ArrayList<>();
            */
        }

    //User toString() method
    @Override
    public String toString() {
        return  "\nUsername:" + this.username + 
                "\nPassword:" + this.password +
                "\nFollowers:" + this.followers + "total followers:" + followers.size() +
                "\nFollowing:" + this.following + "total following:" + following.size() +
                "\nFavorites:" + this.favorites + 
                "\nWatch List:" + this.watchList + 
                "\nUser Lists:" + this.createdList;
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

    public List<String> getFavoriteList() {
        return favorites;
    }

    public List<String> getWatchList() {
        return watchList;
    }

    public List<String> getCreatedList() {
        return createdList;
    }

    //Add to favorites
    public void addFavorite(String movie) {
        favorites.add(movie);
    }
    //Add to watch list
    public void addWatchlist(String movie) {
        watchList.add(movie);
    }
    //Create a list
    public void creatList(String listName) {
        createdList.add(listName);
    }
    //Delete a list
    public void deleteList(String listName) {
        createdList.remove(listName);
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
    public User authenticate(String username, String password) throws Exception {
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
    public int getUserid(String username) throws Exception {
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
    
}



