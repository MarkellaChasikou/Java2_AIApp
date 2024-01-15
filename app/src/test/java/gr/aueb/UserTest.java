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

    
    private static Connection connection;
    
    @BeforeAll
    public static void setupDatabase() throws Exception {
        // Σύνδεση στη βάση δεδομένων για τα tests
        DB db = new DB(); 
        connection = db.getConnection();

        // Δημιουργία πίνακα AppUser για τα tests ολόιδιο με τον πινακα appuser που χρησιμοποιειται στο query τησ κανονικης κλασης user
        try (PreparedStatement stmt = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS AppUserTest (userId INT AUTO_INCREMENT PRIMARY KEY,\n" + //
                        " username varchar(25) UNIQUE NOT NULL,\n" + //
                        " pass_word varchar(25) NOT NULL,\n" + //
                        " country varchar(15) NOT NULL)")) {
            stmt.executeUpdate();
        }
    }

    @AfterAll
    public static void tearDownDatabase() throws SQLException {
        // Καθαρισμός του πίνακα AppUser μετά τα tests
        try (PreparedStatement stmt = connection.prepareStatement("DROP TABLE IF EXISTS AppUserTest")) {
            stmt.executeUpdate();
        }

        // Κλείσιμο της σύνδεσης
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testRegisterUser() {
        // Ελέγχουμε αν η εγγραφή νέου χρήστη πραγματοποιείται σωστά

        User user = null;
        try {
            // Προσθήκη χρήστη στη βάση δεδομένων
            user = User.register("testUser", "testPassword", "testCountry");

            // Έλεγχος αν η εγγραφή έγινε σωστά
            assertNotNull(user);
            assertEquals("testUser", user.getUsername());
            assertEquals("testPassword", user.getPassword());
            assertEquals("testCountry", user.getCountry());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        } finally {
            // Αν υπήρξε εγγραφή, τη διαγράφουμε για να μην επηρεάσει άλλα tests
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
        }
    }
}
    // Εδώ μπορείτε να προσθέσετε περισσότερα tests για άλλες μεθόδους της κλάσης User


