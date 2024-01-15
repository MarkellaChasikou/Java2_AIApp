package gr.aueb;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;


public class BonusContentTest {
   private static String youtubeApiKey;
       
      
// τεστάρω αν κάποια απο 3 ορισματα που εχουν δωθει στην μεθοδο ειναι null ή λαθος

    @Test
    public void testSearchAndPrintVideo_NullSearchQuery() {
        String searchQuery = null;
        String category = "Fun facts";
        File youtubeFile = new File("c:/Users/Βασιλης/OneDrive/Υπολογιστής/apiKeys/youtube_key.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(youtubeFile))) {
            youtubeApiKey = br.readLine();
        } catch (Exception e) {
            System.err.println("Error reading YouTube API key file.");
            System.exit(1);
        }
    
        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, youtubeApiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Search Query cannot be null or empty.", e.getMessage());
        }
    }

    @Test
    public void testSearchAndPrintVideo_EmptyCategory() {
        String searchQuery = "Pulp Fiction";
        String category = null;
       
        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, youtubeApiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("category cannot be null or empty.", e.getMessage());
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

        assertTrue(items.size() > 0);
    }

    @Test
    public void testCheckItemsSize_EmptyList() {
        List<Object> items = new ArrayList<>();

        assertFalse(items.size() > 0);
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
        
        // Καλείστε τη στατική μέθοδο wrapper στην κλάση δοκιμών
        BonusContentTest.iterateAndPrintWrapper(items);

        // Ελέγχουμε αν η έξοδος περιέχει τα αναμενόμενα κείμενα
        String expectedOutput = String.format("Item 1%sItem 2%sItem 3%s", System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testIterateAndPrint_EmptyList() {
        List<String> items = new ArrayList<>();

        // Εκτέλεση της μεθόδου iterateAndPrint και αποθήκευση της έξοδου
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        // Καλείστε τη στατική μέθοδο wrapper στην κλάση δοκιμών
        BonusContentTest.iterateAndPrintWrapper(items);

        // Ελέγχουμε αν η έξοδος είναι κενή
        assertEquals("", outContent.toString());
    }
// Wrapper γύρω από την iterateAndPrint για την κλάση δοκιμών
private static void iterateAndPrintWrapper(List<String> items) {
    for (String item : items) {
        System.out.println(item);
    }
}

}




 



