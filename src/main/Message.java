package main;
import java.util.ArrayList;
import java.util.List;

public class Message {
    private boolean spoiler;
    private String text;
    private String sender;
    private List<String> receivers;
    private String roomId;

    public Message(boolean spoiler, String text, String sender,
        List<String> receivers, String roomId) {
            this.spoiler = spoiler;
            this.text = text;
            this.sender = sender;
            this.receivers = new ArrayList<>();
            this.roomId = roomId;
    }
    
    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }
    public boolean getSpoiler() {
        return spoiler;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getSender() {
        return sender;
    }
    public void setReceivers(List<String> receivers) {
        this.receivers = new ArrayList<>();
    }
    public List<String> getReceivers() {
        return receivers;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    public String getRoomId() {
        return roomId;
    }
}
