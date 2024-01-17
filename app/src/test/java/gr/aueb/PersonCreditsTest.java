package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PersonCreditsTest {

    @Test
    public void testPersonCreditsToString() {
        // Τεστ για τη μέθοδο toString της κλάσης PersonCredits

        // Δημιουργία αντικειμένου PersonCredits
        PersonCredits personCredits = new PersonCredits();

        // Δημιουργία αντικειμένου Cast
        Cast cast = new Cast();
        //cast.setTitle("Movie Title");
        //cast.setCharacter("Character Name");

        // Δημιουργία αντικειμένου Crew
        Crew crew = new Crew();
       // crew.setTitle("Movie Title");
     //   crew.setJob("Director");

        //personCredits.setCast(new Cast[] { cast });
        //personCredits.setCrew(new Crew[] { crew });

        // Έλεγχος της μεθόδου toString
        String expectedString = "Actor\nTitle: Movie Title\nCharacter name: Character Name\n\n\nDirector\nTitle: Movie Title\n\n";
        assertEquals(expectedString, personCredits.toString());
    }

    @Test
    public void testPersonCreditsGetters() {
        // Τεστ για τις μεθόδους getCast, getCrew, getId της κλάσης PersonCredits

        // Δημιουργία αντικειμένου PersonCredits
        PersonCredits personCredits = new PersonCredits();

        // Δημιουργία αντικειμένων Cast
        Cast cast1 = new Cast();
        Cast cast2 = new Cast();
      //  personCredits.setCast(new Cast[] { cast1, cast2 });

        // Δημιουργία αντικειμένων Crew
        Crew crew1 = new Crew();
        Crew crew2 = new Crew();
       // personCredits.setCrew(new Crew[] { crew1, crew2 });

        // Δημιουργία ID
     //   personCredits.setId(1);

        // Έλεγχος των μεθόδων getCast, getCrew, getId
        assertArrayEquals(new Cast[] { cast1, cast2 }, personCredits.getCast());
        assertArrayEquals(new Crew[] { crew1, crew2 }, personCredits.getCrew());
        assertEquals(1, personCredits.getId());
    }
}
