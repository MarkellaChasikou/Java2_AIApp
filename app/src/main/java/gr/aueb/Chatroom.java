package gr.aueb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chatroom {
    //βαζουμε και ονομα στο τσατρουμ + set get μεθοδους με queries kai kataskevastis
    private String roomId;
    private List<String> memberIds;
    private List<String> messages;
    private Map<String, List<String>> unseenMessages; 

    public Chatroom(String roomId) {
        this.roomId = roomId;
        this.memberIds = new ArrayList<>();
        this.messages = new ArrayList<>();
        this.unseenMessages = new HashMap<>();
    }

    public void addMessage(String memberId, String message) {
        String newMessage = memberId + ": " + message;
        messages.add(newMessage);
        for (String member : memberIds) {
            if (!member.equals(memberId)) {
                unseenMessages.computeIfAbsent(member, k -> new ArrayList<>()).add(newMessage);
            }
        }
    }

    public void deleteMessage(int messageIndex) {
        if (messageIndex >= 0 && messageIndex < messages.size()) {
            String deletedMessage = messages.remove(messageIndex);
    
            
            for (List<String> memberUnseenMessages : unseenMessages.values()) {
                if (messageIndex < memberUnseenMessages.size()) {
                    memberUnseenMessages.remove(messageIndex);
                }
            }
        }
    }

    public String getMessage(int messageIndex) {
        if (messageIndex >= 0 && messageIndex < messages.size()) {
            return messages.get(messageIndex);
        } else {
            return null;
        }
    }

    public List<String> getUnseenMessages(String memberId) {
        List<String> memberUnseenMessages = unseenMessages.getOrDefault(memberId, new ArrayList<>());
        unseenMessages.put(memberId, new ArrayList<>()); 
        return memberUnseenMessages;
    }

    @Override
    public String toString() {
        return "Chatroom [roomId=" + roomId + ", memberIds=" + memberIds + ", messages=" + messages
        + ", unseenMessages=" + unseenMessages + "]";
    }

}
