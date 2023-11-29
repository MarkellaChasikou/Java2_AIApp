package main;

import java.util.ArrayList;
import java.util.List;

public class Chatroom {
    private String roomId;
    private List<String> memberIds;
    private List<String> messages;

    public Chatroom(String roomId) {
        this.roomId = roomId;
        this.memberIds = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void addMessage(String memberId, String message) {
        String newMessage = memberId + ": " + message;
        messages.add(newMessage);
    }

    public void deleteMessage(int messageIndex) {
        if (messageIndex >= 0 && messageIndex < messages.size()) {
            messages.remove(messageIndex);
        }
    }

    public String getMessage(int messageIndex) {
        if (messageIndex >= 0 && messageIndex < messages.size()) {
            return messages.get(messageIndex);
        } else {
            return null;
        }
    }

}
