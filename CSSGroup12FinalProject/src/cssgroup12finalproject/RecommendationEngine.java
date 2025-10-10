/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cssgroup12finalproject;

/**
 *
 * @author jojosh
 */
import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {
    private ManhwaDatabase database;
    
    public RecommendationEngine() {
        this.database = ManhwaDatabase.getInstance();
    }
    
    public List<Manhwa> getRecommendations(String genre, String demographic, String ageRating) {
        List<Manhwa> results = database.getAllManhwa();
        
        if (genre != null && !genre.equals("All")) {
            results = results.stream()
                    .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }
        
        if (demographic != null && !demographic.equals("All")) {
            results = results.stream()
                    .filter(m -> m.getDemographic().equalsIgnoreCase(demographic))
                    .collect(Collectors.toList());
        }
        
        if (ageRating != null && !ageRating.equals("All")) {
            results = results.stream()
                    .filter(m -> m.getAgeRating().equalsIgnoreCase(ageRating))
                    .collect(Collectors.toList());
        }
        
        results.sort((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()));
        
        return results;
    }
    
    public Manhwa getRandomRecommendation() {
        List<Manhwa> all = database.getAllManhwa();
        if (all.isEmpty()) return null;
        Random random = new Random();
        return all.get(random.nextInt(all.size()));
    }
    
    public List<Manhwa> getFeaturedManhwa(int count) {
        return database.getAllManhwa().stream()
                .sorted((m1, m2) -> Double.compare(m2.getRating(), m1.getRating()))
                .limit(count)
                .collect(Collectors.toList());
    }
}