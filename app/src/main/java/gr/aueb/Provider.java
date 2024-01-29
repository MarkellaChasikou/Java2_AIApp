/*
 * Provider
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a provider and includes information about the provider's name.
 * 
 * This class is designed to hold details about a content provider, such as a streaming service or platform.
 * The primary attribute is the name of the provider, providing a concise identification of the service.
 *
 * @version 1.8 28 January 2024
 * @author Νίκος Ραγκούσης
 */
public class Provider {
    /** The name of the content provider. */
    @SerializedName("provider_name")
    private String provider_name;
    
    /** 
     * Gets the name of the content provider.
     * 
     * @return The name of the content provider.
     */
    public String getProvider_name() {
        return provider_name;
    }

    @Override
    /** 
     * Returns a string representation of the provider, which is its name.
     * 
     * @return The name of the content provider.
     */
    public String toString() {
        return provider_name;
    }   
}
