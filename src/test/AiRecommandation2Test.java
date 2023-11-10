package test;

import main.AiRecommandation2;

public class AiRecommandation2Test {

    public static void main(String[] args) {
        testChatGPT();
    }

    public static void testChatGPT() {
        AiRecommandation2 aiRecommandation = new AiRecommandation2();

        // We will replace the hello how are you messages with the actual input
        String userInput = "Hello, how are you?";

        try {
            String response = aiRecommandation.chatGPT(userInput);

            // Display the results
            System.out.println("User Input: " + userInput);
            System.out.println("ChatGPT Response: " + response);
        } catch (RuntimeException e) {
            // Handle the exception (print an error message)
            System.err.println("Error during ChatGPT API call: " + e.getMessage());
        }
    }
}
