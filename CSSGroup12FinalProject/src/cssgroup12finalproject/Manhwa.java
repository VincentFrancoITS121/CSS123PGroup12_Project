/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cssgroup12finalproject;

/**
 *
 * @author jojosh
 */
public class Manhwa {
    private String title;
    private String author;
    private String genre;
    private String demographic;
    private String ageRating;
    private double rating;
    private String description;
    private String coverImagePath;
    private double price;
    private boolean hasDiscount;
    private String couponCode;
    
    public Manhwa(String title, String author, String genre, String demographic, 
                  String ageRating, double rating, String description) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.demographic = demographic;
        this.ageRating = ageRating;
        this.rating = rating;
        this.description = description;
        this.price = 0.0;
        this.hasDiscount = false;
    }
    
    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getDemographic() { return demographic; }
    public String getAgeRating() { return ageRating; }
    public double getRating() { return rating; }
    public String getDescription() { return description; }
    public String getCoverImagePath() { return coverImagePath; }
    public double getPrice() { return price; }
    public boolean hasDiscount() { return hasDiscount; }
    public String getCouponCode() { return couponCode; }
    
    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDemographic(String demographic) { this.demographic = demographic; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }
    public void setRating(double rating) { this.rating = rating; }
    public void setDescription(String description) { this.description = description; }
    public void setCoverImagePath(String coverImagePath) { this.coverImagePath = coverImagePath; }
    public void setPrice(double price) { this.price = price; }
    public void setHasDiscount(boolean hasDiscount) { this.hasDiscount = hasDiscount; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    
    @Override
    public String toString() {
        return title + " by " + author + " [" + genre + "]";
    }
}