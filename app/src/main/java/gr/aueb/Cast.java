package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Cast {
    @SerializedName("name")
    private String name;
    @SerializedName("character")
    private String character;
    
    public String getName() {
        return name;
    }
    
    public String getCharacter() {
        return character;
    }
    
    @Override
    public String toString() {
        return String.format("Name: %-30s\tCharacter Name: %s", name, character + "\n");
    }
    
}