package gr.aueb;

public class Contributors {
    private String id;
    public Cast[] cast;
    private Crew[] crew;
    
    public Contributors(String id, Cast[] cast, Crew[] crew) {
        this.id = id;
        this.cast = cast;
        this.crew = crew;
    }
   
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Cast[] getCast() {
        return cast;
    }
    public void setCast(Cast[] cast) {
        this.cast = cast;
    }
    public Crew[] getCrew() {
        return crew;
    }
    public void setCrew(Crew[] crew) {
        this.crew = crew;
    }
}
