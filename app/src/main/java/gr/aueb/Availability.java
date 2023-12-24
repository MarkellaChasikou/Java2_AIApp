package gr.aueb;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gson.annotations.SerializedName;

public class Availability {
    @SerializedName("results")
    private HashMap<String, Country> results;

    public HashMap<String, Country> getResults() {
        return results;
    }

    public void setResults(HashMap<String, Country> results) {
        this.results = results;

    }


    public String toString(boolean allCountries, boolean myServices, Country c) {
        String free = null;
        String ads = null;
        String buy = null;
        String flatrate = null;
        String rent = null;

        if(allCountries == false && myServices == false) {
            if(!c.getFree().isEmpty()) {
                free = "Free: \n";
                for (Prov f : c.getFree()) {
                    free += f.toString();
                }
            }
            
            if(!c.getAds().isEmpty()) {
                ads = "Ads: \n";
                for (Prov a : c.getAds()) {
                    ads += a.toString();
                }
            }
            
            if(!c.getBuy().isEmpty()) {
                buy = "Buy: \n";
                for (Prov b : c.getBuy()) {
                    buy += b.toString();
                } 
            }

            if(!c.getFlatrate().isEmpty()) {
                flatrate = "Stream: ";
                for (Prov s : c.getFlatrate()) {
                    flatrate += s.toString();
                }
            }
            
            
            if(!c.getAds().isEmpty()) {
                rent = "Rent: ";
                for (Prov r : c.getRent()) {
                    rent += r.toString();
                }
            }

            if(free == null && ads == null && buy == null && flatrate == null && rent == null) {
                return "Not available in " + c;
            } else return "";
            
        
            //Unfinished
        
        } else if(allCountries == true && myServices == false) {
            for (Country co : results.values()) {
                for (Prov f : c.getFree()) {
                    
                }
            }
            
            return "";
        } else if(allCountries == false && myServices == false) {
            return "";
        } else {
            return "";
        }
    }
}
