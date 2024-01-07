package gr.aueb;

import java.util.HashSet;

import com.google.gson.annotations.SerializedName;

public class PersonCredits {
    @SerializedName("cast")
    private Cast[] cast;
    @SerializedName("crew")
    private Crew[] crew;
    @SerializedName("id") 
    private int id;

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        if(cast != null){
            returnString.append("Actor\n" );
            for (Cast c : cast) {
                returnString.append("Title: " + c.getTitle() + "\n" + "Character name: " + c.getCharacter() + "\n\n");
            }
        }
        if(crew != null) {
            HashSet<String> uniqueValues = new HashSet<>();
            for (Crew c : crew) {
                if (!uniqueValues.contains(c.getJob())) {
                    uniqueValues.add(c.getJob());
                    returnString.append("\n" + c.getJob() + "\n");
                }
                returnString.append("Title: " + c.getTitle() + "\n\n");
            }
        }
        return returnString.toString();
    }
}
