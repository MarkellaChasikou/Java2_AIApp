package gr.aueb;

import java.util.ArrayList;
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
            
    public Movie(int id, String apiKey) {
        Gson gson = new Gson();
        
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
                if(c.getJob().equals("Writer")) {
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
        
        if(this.md.getRuntime() != null) {
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

    @Override
    public String toString() {
        String s = printResult();
        return s;
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
}