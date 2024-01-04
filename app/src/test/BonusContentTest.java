package gr.aueb;
import main.BonusContent;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;



public class BonusContentTest {
   
// τεστάρω αν κάποια απο 3 ορισματα που εχουν δωθει στην μεθοδο ειναι null ή λαθος

    @Test
    public void testSearchAndPrintVideo_NullSearchQuery() {
        String searchQuery = null;
        String category = "Fun facts";
        String apiKey ;

        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, apiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Search Query cannot be null or empty.", e.getMessage());
        }
    }

    @Test
    public void testSearchAndPrintVideo_EmptyCategory() {
        String searchQuery = "Pulp Fiction";
        String category = "";
        String apiKey ;

        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, apiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Category cannot be null or empty.", e.getMessage());
        }
    }

    @Test
    public void testSearchAndPrintVideo_NullApiKey() {
        String searchQuery = "Barbie";
        String category = "Behind the Scenes";
        String apiKey = null;

        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, apiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("ApiKey cannot be null or empty.", e.getMessage());
        }
    }
// ελεγχος για την items
    @Test
    public void testCheckItemsSize_NotEmptyList() {
        List<Object> items = new ArrayList<>();
        items.add(new Object()); // Προσθέτουμε ένα στοιχείο στη λίστα

        assertTrue(BonusContent.checkItemsSize(items));
    }

    @Test
    public void testCheckItemsSize_EmptyList() {
        List<Object> items = new ArrayList<>();

        assertFalse(BonusContent.checkItemsSize(items));
    }

    @Test
    public void testIterateAndPrint_NonEmptyList() {
        List<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");

        // Εκτέλεση της μεθόδου iterateAndPrint και αποθήκευση της έξοδου
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        BonusContent.iterateAndPrint(items);

        // Ελέγχουμε αν η έξοδος περιέχει τα αναμενόμενα κείμενα
        String expectedOutput = "Item 1\nItem 2\nItem 3\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testIterateAndPrint_EmptyList() {
        List<String> items = new ArrayList<>();

        // Εκτέλεση της μεθόδου iterateAndPrint και αποθήκευση της έξοδου
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        BonusContent.iterateAndPrint(items);

        // Ελέγχουμε αν η έξοδος είναι κενή
        assertEquals("", outContent.toString());
    }

}




 



