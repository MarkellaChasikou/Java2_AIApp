package gr.aueb;
import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
public class BonusContent {
       
	Movie movie = new Movie();//dhmioyrgia antikeimenoy klashs Movie

    
    String movieName = movie.getMovieId();//Pairnw to movie id apo thn klash Movie
       
       //apo edw k katw skip

        public void BonusContent() {
            try {
               
                Document document = Jsoup.connect("https://www.google.com/" + movieName).get();// tha xrhsimopoihsw brave

               
               
                Elements funFacts = document.select(".fun-facts");

                if (!funFacts.isEmpty()) {
                    System.out.println("Βρέθηκαν Fun Facts: " + funFacts.text());
                   
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
