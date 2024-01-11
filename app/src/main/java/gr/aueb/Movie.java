package gr.aueb;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;

public class Movie {
    
    private Availability av;
    private Contributors co;
    private MovieDetails md;
    private double imdbRating;
    private ArrayList<Integer> peopleId;
    private ArrayList<String> peopleName;
    private ArrayList<String> peopleJob;
    private HashMap<Integer, Object[]> people;
    private ArrayList<Float> peoplePopularity;
            
    public Movie(int id, String apiKey) {
        Gson gson = new Gson();
        peopleId = new ArrayList<>();
        peopleName = new ArrayList<>();
        peopleJob = new ArrayList<>();
        people = new HashMap<>();
        peoplePopularity = new ArrayList<>();        
        //responce for movie credits
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "/credits?language=en-US"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())                
            .build();
        HttpResponse<String> response1;
        try {
            response1 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            co = gson.fromJson(response1.body(), Contributors.class);
        } catch (IOException e) {
            System.err.println("Check your internet connection!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        //responce for movie details
        request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "?language=en-US"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response2;
        try {
            response2 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            md = gson.fromJson(response2.body(), MovieDetails.class);
        } catch (IOException e) {
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //responce for movie availability
        request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.themoviedb.org/3/movie/" + id + "/watch/providers"))
            .header("accept", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
        HttpResponse<String> response3;
        try {
            response3 = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
            av = gson.fromJson(response3.body(), Availability.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        imdbRating = getImdbRatingFromID(md.getImdb_id());

        if(co.getCast() != null) {
            Object temp[]; 
            for (Cast c : co.getCast()) {
                temp = new Object[3];
                temp[0] = c.getName();
                temp[1] = "Actor";
                temp[2] = c.getPopularity();
                people.put(c.getId(), temp);
            }
        }

        if(co.getCrew() != null) {
            Object temp[]; 
            for (Crew c : co.getCrew()) {
                temp = new Object[3];
                temp[0] = c.getName();
                temp[1] = c.getJob();
                temp[2] = c.getPopularity();
                people.put(c.getId(), temp);
            }
        }
 
        for (Integer i : people.keySet()) {
            peopleId.add(i);
        }

        for (Object[] i : people.values()) {
            peopleName.add((String)i[0]);
            peopleJob.add((String)i[1]);
            peoplePopularity.add((float)i[2]);
        }
    }
    
    public static double getImdbRatingFromID(String imdbID) {
        String filePath = "C:\\Users\\Nick\\api_keys\\title.rating.tsv"; // Replace this with your file path
        String line;
        double imdbRating = -1; // Default value if the ID is not found

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");

                if (data.length >= 3 && data[0].equals(imdbID)) {
                    imdbRating = Double.parseDouble(data[1]);
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return imdbRating;
    }

        
    private ArrayList<String> getDirectors() {
        ArrayList<String> directors = new ArrayList<>();
        if(this.co.getCrew() != null) {
            for (Crew c : this.co.getCrew()) {
                if(c.getJob().equals("Director")) {
                    directors.add(c.getName());
                }
            }
        }
        return directors;
    }

    private ArrayList<String> getWriters() {
        ArrayList<String> writers = new ArrayList<>();
        if(this.co.getCrew() != null) {
            for (Crew c : this.co.getCrew()) {
                if(c.getDepartment().equals("Writing")) {
                    writers.add(c.getName());
                }
            }
        }
        return writers;
    }
    
    private ArrayList<String> getActors() {
        ArrayList<String> actors = new ArrayList<>();
        if(this.co.getCast() != null) {
            if(this.co.getCast().length >= 4) {
                actors.add(this.co.getCast()[0].getName() + " (" + this.co.getCast()[0].getCharacter() + ")");
                actors.add(this.co.getCast()[1].getName() + " (" + this.co.getCast()[1].getCharacter() + ")");
                actors.add(this.co.getCast()[2].getName() + " (" + this.co.getCast()[2].getCharacter() + ")");
                actors.add(this.co.getCast()[3].getName() + " (" + this.co.getCast()[3].getCharacter() + ")");
            } else {
                for (Cast c : this.co.getCast()) {
                    actors.add(c.getName() + " (" + this.co.getCast()[0].getCharacter() + ")");
                }
            }
        }
        return actors;
    }
    
    private String printResult() {
        StringBuilder returnString = new StringBuilder();
        if(this.md.getOriginal_title() != null) {
            returnString.append("Title: " + this.md.getOriginal_title() + "\n\n");
        }
        
        if(this.md.getOverview() != null) {
            returnString.append(this.md.getOverview() + "\n \n");
        }

        if(this.md.getGenres() != null) {
            returnString.append("Genres: ");
            Genre[] genres = this.md.getGenres();
            for (int i = 0; i < genres.length; i++) {
                returnString.append(genres[i]);
                if (i < genres.length - 1) {
                    returnString.append(", ");
                }
            }
            returnString.append("\n\n");
        }

        if(this.getImdbRating() != -1) returnString.append("Imdb Rating: " + this.getImdbRating() + "\n \n");
        
        if(this.md.getRuntime() != 0) {
            returnString.append("Runtime: " + this.md.getRuntime() + "m" + "\n\n");
        }

        if(this.md.getRelease_date() != null) {
            returnString.append("Release Date: " + this.md.getRelease_date() + "\n\n");
        }

        if(getDirectors() != null) {
            returnString.append("Director(s): ");
            for (int i = 0; i < getDirectors().size(); i++) {
                returnString.append(getDirectors().get(i));
                if (i < getDirectors().size() - 1) {
                    returnString.append(", ");
                }
            }
            returnString.append("\n\n");
        }

        if(getWriters() != null) {
            returnString.append("Writer(s): ");
            for (int i = 0; i < getWriters().size(); i++) {
                returnString.append(getWriters().get(i));
                if (i < getWriters().size() - 1) {
                    returnString.append(", ");
                }
            }
            returnString.append("\n\n");
        }

        if(getActors() != null) {
            returnString.append("Top billed cast:\n");
            for (int i = 0; i < getActors().size(); i++) {
                returnString.append(getActors().get(i));
                if (i < getActors().size() - 1) {
                    returnString.append("\n");
                }
            }
            returnString.append("\n\n");
        }

        returnString.append(av.toString("GR")); //temp
        return returnString.toString();
    }

    public void printFullCast() {
        StringBuilder ca = new StringBuilder();
        StringBuilder cr = new StringBuilder();

        // Cast
        for (Cast c : this.co.getCast()) {
            ca.append(c);
        }

        // Crew
        for (Crew c : this.co.getCrew()) {
            cr.append(c);
        }

        System.out.println("\n\nCast:");
        System.out.println(ca + "\n");
        System.out.println("Crew:");
        System.out.println(cr + "\n");
    }

    @Override
    public String toString() {
        String s = printResult();
        return "\n\n" + s;
    }


    public Availability getAv() {
        return av;
    }


    public Contributors getCo() {
        return co;
    }


    public MovieDetails getMd() {
        return md;
    }


    public double getImdbRating() {
        return imdbRating;
    }

    public ArrayList<Integer> getPeopleId() {
        return peopleId;
    }

    public ArrayList<String> getPeopleName() {
        return peopleName;
    }

    public ArrayList<String> getPeopleJob() {
        return peopleJob;
    }

    public HashMap<Integer, Object[]> getPeople() {
        return people;
    }

    public ArrayList<Float> getPeoplePopularity() {
        return peoplePopularity;
    }
}