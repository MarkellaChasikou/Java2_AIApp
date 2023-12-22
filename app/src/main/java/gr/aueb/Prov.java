package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Prov {
    @SerializedName("provider_name")
    private String provider_name;
    
    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    @Override
    public String toString() {
        return "Prov [provider_name=" + provider_name + "]";
    }
    
}
