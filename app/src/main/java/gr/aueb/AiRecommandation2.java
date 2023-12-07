package gr.aueb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;

public class AiRecommandation2 {

    public static void main(String[] args) {
        // Get user input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a user message:");
        try {
            String userMessage = reader.readLine();
            testChatCompletions(userMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testChatCompletions(String userMessage) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "-"; // Actual api key will be read from a file
        String model = "gpt-3.5-turbo";
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Create the HTTP POST request
                URL obj;
                try {
                    obj = new URI(url).toURL(); // Handle URISyntaxException
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    return; 
                }

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", "Bearer " + apiKey);
                con.setRequestProperty("Content-Type", "application/json");

                // Build the request body
                String body = "{"
                        + "\"model\": \"" + model + "\","
                        + "\"messages\": ["
                        + "{\"role\": \"user\", \"content\": \"" + userMessage + "\"},"
                        + "{\"role\": \"assistant\", \"content\": \"Here are 10 movie examples for you:\\n1. Movie 1\\n2. Movie 2\\n3. Movie 3\\n4. Movie 4\\n5. Movie 5\\n6. Movie 6\\n7. Movie 7\\n8. Movie 8\\n9. Movie 9\\n10. Movie 10\"}"
                        + "]"
                        + "}";

                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();

                // Get the response
                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Print the response
                    System.out.println("ChatGPT Response: " + response.toString());
                    break; // Successful response, exit the retry loop
                } else if (responseCode == 429) {
                    // Retry after the specified duration
                    int retryAfter = con.getHeaderFieldInt("Retry-After", -1);
                    if (retryAfter > 0) {
                        System.out.println("Too Many Requests. Retrying after " + retryAfter + " seconds.");
                        Thread.sleep(retryAfter * 1000);
                    } else {
                        System.out.println("Too Many Requests. Retrying after a short delay.");
                        Thread.sleep(5000); // Retry after a short delay if Retry-After is not provided
                    }
                } else {
                    // Handle other response codes if needed
                    System.out.println("Unexpected response code: " + responseCode);
                    break; 
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
