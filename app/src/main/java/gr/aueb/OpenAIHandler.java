package gr.aueb;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class OpenAIHandler {
    private final String apiKey;

    public OpenAIHandler(String apiKey) {
        this.apiKey = apiKey;
    }

    public HashMap<String, String> getMovieRecommendations(String userMessage) {
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo";

        try {
            // Create the HTTP POST request
            URL obj = new URI(url).toURL(); // Handle URISyntaxException
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
            try (OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream())) {
                writer.write(body);
                writer.flush();
            }

            // Get the response
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    return parseResponse(response.toString());
                }
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
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    private HashMap<String, String> parseResponse(String jsonResponse) {
        HashMap<String, String> movieMap = new HashMap<>();
        JsonArray choices = JsonParser.parseString(jsonResponse).getAsJsonObject().getAsJsonArray("choices");

        if (!choices.isJsonNull() && choices.size() > 0) {
            JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");

            if (message != null) {
                String content = message.getAsJsonPrimitive("content").getAsString();
                // Parse content to extract movie names and ids, and populate the movieMap
                // Example parsing logic: Split content by "\\n" and extract information
            }
        }

        return movieMap;
    }
}