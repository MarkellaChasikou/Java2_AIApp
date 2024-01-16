package gr.aueb;


import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import gr.aueb.DB;
import gr.aueb.User;

public class UserTest {

    private static User user;
    private static Connection connection;
    
    @BeforeAll
public static void CreateInserts() throws Exception {
    // Σύνδεση στη βάση δεδομένων για τα tests
    DB db = new DB(); 
    connection = db.getConnection();
    try {
        // Προσθήκη χρήστη στη βάση δεδομένων
        user = User.register("testUser", "testPassword", "testCountry");
       
    } catch (Exception e) {
        fail("Exception thrown: " + e.getMessage());
    }
    assertNotNull(user);
}   


    @AfterAll
    public static void DeleteInserts() throws SQLException {
        // Καθαρισμός του πίνακα AppUser μετά τα tests
        if (user != null) {
            try {
                try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM AppUser WHERE userId = ?")) {
                    stmt.setInt(1, user.getId());
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

            // Έλεγχος αν η εγγραφή έγινε σωστά
            assertNotNull(user);
            assertEquals("testUser", user.getUsername());
            assertEquals("testPassword", user.getPassword());
            assertEquals("testCountry", user.getCountry());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        } 
            
        
    }
}
    // Εδώ μπορείτε να προσθέσετε περισσότερα tests για άλλες μεθόδους της κλάσης User


