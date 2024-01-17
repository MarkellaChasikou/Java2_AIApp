package gr.aueb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import javax.management.relation.Role;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URISyntaxException;

public class AiRecommendation {

    public static String testChatCompletions(String userMessage, String apiKey) throws URISyntaxException {
        String url = "https://api.openai.com/v1/chat/completions";
        String model = "gpt-3.5-turbo";
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                // Create the HTTP POST request
                URL obj = new URI(url).toURL();
                try {
                    obj = new URI(url).toURL(); // Handle URISyntaxException
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    // return;
                }

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Authorization", "Bearer " + apiKey);
                con.setRequestProperty("Content-Type", "application/json");

                // Build the request body with only the user's message
                String body = "{"
                        + "\"model\": \"" + model + "\","
                        + "\"messages\": ["
                        + "{\"role\": \"user\", \"content\": \"" + userMessage + "\"}"
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
                    return AiRecommendation.aiMessage(response);
                    // Successful response, exit the retry loop
                } else if (responseCode == 429) {
                    // Retry after the specified duration
                    int retryAfter = con.getHeaderFieldInt("Retry-After", -1);
                    // Handle retry logic if needed
                } else {
                    // Handle other response codes if needed
                    return ("Unexpected response code: " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }
    public static String aiMessage(StringBuilder response) {
        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(response.toString(), JsonObject.class);

        String content = jo.getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString();

        return "\n\n" + content;
    }
}