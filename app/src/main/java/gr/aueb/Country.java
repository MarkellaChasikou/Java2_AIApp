package gr.aueb;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("free")
    private ArrayList<Prov> free;
    @SerializedName("ads")
    private ArrayList<Prov> ads;
    @SerializedName("buy")
    private ArrayList<Prov> buy;
    @SerializedName("flatrate")
    private ArrayList<Prov> flatrate;
    @SerializedName("rent")
    private ArrayList<Prov> rent;
    

    public ArrayList<Prov> getFree() {
        return free;
    }

    public void setFree(ArrayList<Prov> free) {
        this.free = free;
    }

    public ArrayList<Prov> getAds() {
        return ads;
    }

    public void setAds(ArrayList<Prov> ads) {
        this.ads = ads;
    }

    public ArrayList<Prov> getBuy() {
        return buy;
    }

    public void setBuy(ArrayList<Prov> buy) {
        this.buy = buy;
    }

    public ArrayList<Prov> getFlatrate() {
        return flatrate;
    }

    public void setFlatrate(ArrayList<Prov> flatrate) {
        this.flatrate = flatrate;
    }

    public ArrayList<Prov> getRent() {
        return rent;
    }

    public void setRent(ArrayList<Prov> rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return "Country [free=" + free + ", ads=" + ads + ", buy=" + buy + ", flatrate=" + flatrate + ", rent=" + rent
                + "]";
    }

    

}
