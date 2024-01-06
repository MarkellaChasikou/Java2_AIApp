package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Provider {
    @SerializedName("provider_name")
    private String provider_name;
    
    public String getProvider_name() {
        return provider_name;
    }

    @Override
    public String toString() {
        return provider_name;
    }
    
}
