package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MovieListTest {

    private static MovieList testList;
    private static int testUserId;

    @BeforeAll
    public static void setup() {
        // Πραγματοποιήστε τις αρχικοποιήσεις που απαιτούνται για τα τεστ
        // Μπορείτε να δημιουργήσετε μια λίστα και να ορίσετε τον χρήστη που θα χρησιμοποιηθεί για τα τεστ
        testList = new MovieList("public", 1, "Test List", 123);
        testUserId = 2; // Ορίστε τον χρήστη που θα χρησιμοποιηθεί για τα τεστ (ο 2 υπαρχει ηδη)
    }

    // Μέθοδος που χρησιμοποιεί ανάκληση για να πάρει τιμή από ιδιωτικό πεδίο
    private Object getPrivateField(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    
    @Test
    public void createListTest() throws Exception {
        // Χρησιμοποιήστε τη μέθοδο createList για να δημιουργήσετε μια λίστα
        MovieList createdList = MovieList.createList("public", "New Test List", testUserId);

        // Ελέγχει αν η λίστα δημιουργήθηκε με τις σωστές τιμές
        assertEquals("public", getPrivateField(createdList, "listType"));
        assertEquals("New Test List", getPrivateField(createdList, "listName"));
        assertEquals(testUserId, getPrivateField(createdList, "creatorId"));
    }

    @Test
    public void addToListTest() throws Exception {
        // Χρησιμοποιήστε τη μέθοδο addToList για να προσθέσετε μια ταινία στη λίστα
        testList.addToList("Movie Title", "123456", testUserId);
    
        // Ανακτήστε τις ταινίες από τη λίστα και ελέγξτε αν προστέθηκε η σωστή ταινία
        Map<String, String> movies = getMoviesFromList(testList, Integer.toString((int) getPrivateField(testList, "listId")));
        assertTrue(movies.containsKey("Movie Title"));
        assertEquals("123456", movies.get("Movie Title"));
    }
     // Μέθοδος που χρησιμοποιεί ανάκληση για να καλέσει την ιδιωτική μέθοδο getMoviesFromList
     private Map<String, String> getMoviesFromList(MovieList movieList, String listName) throws Exception {
        return (Map<String, String>) invokePrivateMethod(movieList, "getMoviesFromList", listName);
    }

    // Μέθοδος που χρησιμοποιεί ανάκληση για να καλέσει ιδιωτική μέθοδο στο αντικείμενο
    private Object invokePrivateMethod(Object object, String methodName, Object... args) throws Exception {
        Class<?>[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }

        try {
            java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (NoSuchMethodException e) {
            throw new Exception("Method " + methodName + " not found");
        }
    }

}


