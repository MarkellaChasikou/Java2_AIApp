package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

public class AiRecommendationTest {

    @Test
    public void testChatCompletions() {
        // Provide a valid API key
        String apiKey = "your_api_key_here";

        // Test user message
        String userMessage = "What movies do you recommend?";

        try {
            // Call the method
            String result = AiRecommendation.testChatCompletions(userMessage, apiKey);

            // Check that the result is not null or empty
            assertNotNull(result);
            assertFalse(result.isEmpty());

            // You can add more specific assertions based on the expected behavior of the method

        } catch (URISyntaxException e) {
            fail("Exception during test: " + e.getMessage());
        }
    }

    @Test
    public void testAiMessage() {
        // Create a sample response from the AI model
        StringBuilder response = new StringBuilder("{ \"choices\": [ { \"message\": { \"content\": \"Recommended movies: Movie 1, Movie 2\" } } ] }");

        // Call the method
        String result = AiRecommendation.aiMessage(response);

        // Check that the result is not null or empty
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // You can add more specific assertions based on the expected behavior of the method
    }
}
