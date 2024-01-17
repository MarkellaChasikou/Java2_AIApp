package gr.aueb;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class AvailabilityTest {

    @Test
    public void testFormatAvailability() {
        Availability availability = new Availability();
        ArrayList<Provider> providers = new ArrayList<>();
     //   providers.add(new Provider("Provider1"));
     //   providers.add(new Provider("Provider2"));

        String formatted = availability.formatAvailability("TestCategory", providers);

        assertEquals("TestCategory: Provider1, Provider2\n", formatted);
    }

    @Test
    public void testToString() {
        Availability availability = new Availability();
        HashMap<String, Country> results = new HashMap<>();
        ArrayList<Provider> providers = new ArrayList<>();
     //   providers.add(new Provider("Provider1"));
     //   providers.add(new Provider("Provider2"));
    //    Country country = new Country(providers, providers, providers, providers, providers);
    //    results.put("TestCountry", country);
    //    availability.results = results;

        String resultString = availability.toString("TestCountry");

        assertEquals("Free: Provider1, Provider2\n" +
                "Ads: Provider1, Provider2\n" +
                "Buy: Provider1, Provider2\n" +
                "Stream: Provider1, Provider2\n" +
                "Rent: Provider1, Provider2\n", resultString);
    }

    @Test
    public void testToStringCountryNotAvailable() {
        Availability availability = new Availability();
        HashMap<String, Country> results = new HashMap<>();
    //    results.put("AvailableCountry", new Country(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
   //             new ArrayList<>(), new ArrayList<>()));
    //    availability.results = results;

        String resultString = availability.toString("NotAvailableCountry");

        assertEquals("\nNot available in NotAvailableCountry", resultString);
    }

    // Add more tests as needed based on your requirements

}
