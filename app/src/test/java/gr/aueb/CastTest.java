package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CastTest {

    @Test
    public void testToString() {
        // Δημιουργία ενός αντικειμένου Cast για το τεστ
        Cast cast = new Cast();
      //  cast.setName("John Doe");
        // Χρησιμοποιούμε reflection για να αλλάξουμε το πεδίο character
        // Σημείωση: Η χρήση reflection για τέτοιου είδους αλλαγές δεν είναι συνηθισμένη
        // και μπορεί να έχει ανεπιθύμητες επιπτώσεις στη συντηρησιμότητα του κώδικα.
      //  cast.setField("character", "Main Character");

        // Έλεγχος της μεθόδου toString
        assertEquals("Name: null                          	Character Name: null\n" , cast.toString());
    }

@Test
public void testCast() {
    // Τεστ για την κλάση Cast

    // Δημιουργία αντικειμένου Cast
    Cast cast = new Cast();
 //   cast.setName("Actor Name");
 //   cast.setCharacter("Character Name");
 //   cast.setId(1);

    // Έλεγχος των πεδίων
    assertEquals("Actor Name", cast.getName());
    assertEquals("Character Name", cast.getCharacter());
    assertEquals(1, cast.getId());

    // Έλεγχος της μεθόδου toString
    assertEquals("Name: Actor Name                  Character Name: Character Name\n", cast.toString());
}
}