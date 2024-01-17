package gr.aueb;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class ContributorsTest {
     @Test
    public void testContributors() {
        // Τεστ για την κλάση Contributors

        // Δημιουργία αντικειμένου Contributors
        Contributors contributors = new Contributors();
      //  contributors.setId(1);

        // Δημιουργία αντικειμένων Cast
        Cast cast1 = new Cast();
        Cast cast2 = new Cast();
       // contributors.setCast(new Cast[] { cast1, cast2 });

        // Δημιουργία αντικειμένων Crew
        Crew crew1 = new Crew();
        Crew crew2 = new Crew();
       // contributors.setCrew(new Crew[] { crew1, crew2 });

        // Έλεγχος των πεδίων
        assertEquals(1, contributors.getId());
        assertArrayEquals(new Cast[] { cast1, cast2 }, contributors.getCast());
        assertArrayEquals(new Crew[] { crew1, crew2 }, contributors.getCrew());
    }
}










