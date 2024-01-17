package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DBTest {

    private DB db;

    @BeforeEach
    public void setup() {
        // Create a new instance of the DB class before each test
        db = new DB();
    }

    @AfterEach
    public void cleanup() {
        try {
            // Close the connection after each test
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetConnection() {
        try {
            // Call the getConnection method
            Connection connection = db.getConnection();

            // Check that the connection is not null
            assertNotNull(connection);

            // You can add more specific assertions based on the expected behavior of the method

        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    @Test
public void testClose() {
    try (DB db = new DB()) {
        Connection connection = db.getConnection();
        assertNotNull(connection);

        db.close();
        assertTrue(connection.isClosed());
    } catch (Exception e) {
        fail("Exception during test: " + e.getMessage());
    }
}

}
