/*
 * Bonus Content Test
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import main.BonusContent;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Represents a JUnit test class for the BonusContent class
 * 
 * This class is responsible for
 * testing the functionality of methods related to searching and printing bonus
 * video content.
 *
 * @version 1.8 28 January 2024
 * @author Βασίλης Μυλωνάς
 */
public class BonusContentTest {

    @Test
    /**
     * Verifies that an IllegalArgumentException is thrown when the search query is
     * null.
     */
    public void testSearchAndPrintVideo_NullSearchQuery() {
        String searchQuery = null;
        String category = "Fun facts";
        String apiKey;

        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, apiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Search Query cannot be null or empty.", e.getMessage());
        }
    }

    @Test
    /**
     * Verifies that an IllegalArgumentException is thrown when the category is
     * empty.
     */
    public void testSearchAndPrintVideo_EmptyCategory() {
        String searchQuery = "Pulp Fiction";
        String category = "";
        String apiKey;

        try {
            BonusContent.searchAndPrintVideo(searchQuery, category, apiKey);
            fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Category cannot be null or empty.", e.getMessage());
        }
    }

    @Test
    /**
     * Verifies that an IllegalArgumentException is thrown when the API key is null.
     */
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

    @Test
    /**
     * Verifies that the checkItemsSize method returns true for a non-empty list.
     */
    public void testCheckItemsSize_NotEmptyList() {
        List<Object> items = new ArrayList<>();
        items.add(new Object()); // add object to the list

        assertTrue(BonusContent.checkItemsSize(items));
    }

    @Test
    /**
     * Verifies that the checkItemsSize method returns false for an empty list.
     */
    public void testCheckItemsSize_EmptyList() {
        List<Object> items = new ArrayList<>();

        assertFalse(BonusContent.checkItemsSize(items));
    }

    @Test
    /**
     * Verifies that the iterateAndPrint method prints the expected output for a
     * non-empty list.
     */
    public void testIterateAndPrint_NonEmptyList() {
        List<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");

        // execute method iterateAndPrint and save the output.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        BonusContent.iterateAndPrint(items);

        // Checking if the output includes the expected content.
        String expectedOutput = "Item 1\nItem 2\nItem 3\n";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    /**
     * Verifies that the iterateAndPrint method produces no output for an empty
     * list.
     */
    public void testIterateAndPrint_EmptyList() {
        List<String> items = new ArrayList<>();

        // Execute the method iterateAndPrint and save the output.
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        BonusContent.iterateAndPrint(items);

        // Checking if the output is empty.
        assertEquals("", outContent.toString());
    }
}
