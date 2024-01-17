package gr.aueb;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

public class CountryTest {

    @Test
    public void testAllCountriesNames() {
        // Test for the allCountriesNames method of the Country class

        // Assume that we have a valid API key
        String apiKey = "your_api_key_here";

        // Call the method
        TreeMap<String, String> countries = Country.allCountriesNames(apiKey);

        // Check that the result is not null and contains at least one country
        assertNotNull(countries);
        assertTrue(countries.size() > 0);
    }

    @Test
    public void testGetters() {
        // Test for the getter methods of the Country class

        // Create a Country object
        Country country = new Country();

        // Create a list of providers
        ArrayList<Provider> providers = new ArrayList<>();

        // Assign the list of providers to the corresponding methods of Country
      //  country.free = providers;
      //  country.ads = providers;
      //  country.buy = providers;
      //  country.flatrate = providers;
      //  country.rent = providers;

        // Check that the getter methods return the expected lists of providers
        assertSame(providers, country.getFree());
        assertSame(providers, country.getAds());
        assertSame(providers, country.getBuy());
        assertSame(providers, country.getFlatrate());
        assertSame(providers, country.getRent());
    }
}
