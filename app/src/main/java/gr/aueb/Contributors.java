package gr.aueb;

import com.google.gson.annotations.SerializedName;

public class Contributors {
    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private Cast[] cast;
    @SerializedName("crew")
    private Crew[] crew;
   
    public int getId() {
        return id;
    }
    
    public Cast[] getCast() {
        return cast;
    }
   
    public Crew[] getCrew() {
        return crew;
    }
}
