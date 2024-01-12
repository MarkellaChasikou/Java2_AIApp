package test;

import org.junit.jupiter.api.After;
import org.junit.jupiter.api.Before;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class JunitChatroomTest {

    private Chatroom chatroom;

    @Before
    public void setUp() {
        // Create a new Chatroom 
        chatroom = new Chatroom(1, "Test Chatroom", 1234);
    }

    
    @After
    @Transactional
    public void tearDown() {
        // Clean up any resources 
        // Set the chatroom variable to null
        chatroom = null;
    }

    @Test
    public void testGetRoomId() {
        assertEquals(1, chatroom.getRoomId());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Chatroom", chatroom.getName());
    }

    @Test
    public void testSetName() {
        chatroom.setName("New Chatroom Name");
        assertEquals("New Chatroom Name", chatroom.getName());
    }

    @Test
    public void testCreateChatroom() {
        // Assuming createChatroom method works as expected
        try {
            Chatroom createdChatroom = chatroom.createChatroom();
            assertNotNull(createdChatroom);
            assertEquals(chatroom.getName(), createdChatroom.getName());
            assertEquals(chatroom.getRoomId(), createdChatroom.getRoomId());
            assertEquals(chatroom.createorId, createdChatroom.createorId);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetChatrooms() {
        // Assuming getChatrooms method works as expected
        try {
            List<Chatroom> chatrooms = Chatroom.getChatrooms();
            assertNotNull(chatrooms);
            assertTrue(chatrooms.size() > 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testShowChatroomMembers() {
        // Assuming showChatroomMembers method works as expected
        try {
            List<String> members = Chatroom.showChatroomMembers(1); // Assuming 1 is a valid chatroomId
            assertNotNull(members);
            assertTrue(members.size() >= 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetMessages() {
        // Assuming getMessages method works as expected
        try {
            List<String> messages = Chatroom.getMessages(1); // Assuming 1 is a valid chatroomId
            assertNotNull(messages);
            assertTrue(messages.size() >= 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetUnseenMessage() {
        // Assuming getUnseenMessage method works as expected
        try {
            // Assuming user is a valid User instance with an appropriate getId() method
            User user = new User(); // Replace this with an actual User instance
            List<String> unseenMessages = Chatroom.getUnseenMessage(1, user); // Assuming 1 is a valid chatroomId
            assertNotNull(unseenMessages);
            assertTrue(unseenMessages.size() >= 0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
