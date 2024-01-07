package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class PersonDetails {
    @SerializedName("name")
    private String name;
    @SerializedName("biography")
    private String biography;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("deathday")
    private String deathday;
    @SerializedName("id")
    private int id;
    
    public String getName() {
        return name;
    }
    public String getBiography() {
        return biography;
    }
    public String getBirthday() {
        return birthday;
    }
    public String getDeathday() {
        return deathday;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        if(this.name != null) returnString.append("Name: " +this.name+"\n\n");
        if(this.biography != null) returnString.append(this.biography+"\n\n");
        if(this.birthday != null) returnString.append("Birthday: " +this.birthday+"\n");
        if(this.deathday != null) {
            returnString.append("Deathday: " +this.name+"\n\n");
        } else returnString.append("\n");
        return returnString.toString();
    }
}
