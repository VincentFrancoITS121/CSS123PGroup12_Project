package cssgroup12finalproject;

import java.util.*;
import java.util.stream.Collectors;

public class ManhwaDatabase {
    private static ManhwaDatabase instance;
    private List<Manhwa> manhwaList;
    
    private ManhwaDatabase() {
        manhwaList = new ArrayList<>();
        // 1. Initialize core metadata first
        initializeSampleData();
        // 2. Initialize shopping/commerce data separately
        initializeShoppingData(); 
    }
    
    public static ManhwaDatabase getInstance() {
        if (instance == null) {
            instance = new ManhwaDatabase();
        }
        return instance;
    }
    
    /**
     * Initializes core Manhwa metadata (Title, Author, Genre, Rating, Description).
     */
    private void initializeSampleData() {
        // Core Manhwa data population
        manhwaList.add(new Manhwa(
            "Solo Leveling",
            "Chugong",
            "Action",
            "Shounen",
            "13+",
            9.5,
            "A weak hunter becomes the strongest through a mysterious system."
        ));
        
        manhwaList.add(new Manhwa(
            "Tower of God",
            "SIU",
            "Fantasy",
            "Shounen",
            "13+",
            9.2,
            "A boy enters a mysterious tower to find his friend."
        ));
        
        manhwaList.add(new Manhwa(
            "The Remarried Empress",
            "Alphatart",
            "Romance",
            "Shoujo",
            "16+",
            9.0,
            "An empress remarries after her husband betrays her."
        ));
        
        manhwaList.add(new Manhwa(
            "Omniscient Reader's Viewpoint",
            "singNsong",
            "Fantasy",
            "Shounen",
            "13+",
            9.4,
            "A reader becomes part of the web novel he was reading."
        ));
        
        manhwaList.add(new Manhwa(
            "True Beauty",
            "Yaongyi",
            "Romance",
            "Shoujo",
            "13+",
            8.8,
            "A girl becomes popular after mastering makeup."
        ));
        manhwaList.add(new Manhwa(
            "Weak Hero",
            "Seopass",
            "Action",
            "Seinen",
            "16+",
            9.3,
            "A deceptively weak-looking genius student uses strategy to dominate a school filled with bullies."
        ));
        
        manhwaList.add(new Manhwa(
            "Villains Are Destined to Die",
            "Suri",
            "Romance",
            "Josei",
            "16+",
            9.1,
            "A student is transmigrated into a dating sim game as the despised villainess and must choose a target to survive."
        ));
        
        manhwaList.add(new Manhwa(
            "Lookism",
            "Park Tae-joon",
            "Drama",
            "Shounen",
            "13+",
            8.9,
            "A bullied high school student lives a double life, switching between his two vastly different bodies: one handsome, one unattractive."
        ));
        
        manhwaList.add(new Manhwa(
            "Eleceed",
            "Jejak",
            "Fantasy",
            "Shounen",
            "13+",
            9.6,
            "A powerful cat with human powers teams up with a kind-hearted boy who secretly possesses superhuman abilities."
        ));
        
        manhwaList.add(new Manhwa(
            "Unordinary",
            "Uru-chan",
            "Action",
            "Shounen",
            "13+",
            9.4,
            "In a world where everyone has superpowers, a 'cripple' hides a dark secret about his own devastating power."
        ));
    }
    
    /**
     * Initializes shopping-related data (Price, Discount, Coupon).
     * This method encapsulates commerce-specific details.
     */
    private void initializeShoppingData() {
        // Commerce-specific data population
        
        Map<String, Manhwa> manhwaMap = manhwaList.stream()
                .collect(Collectors.toMap(Manhwa::getTitle, m -> m));

        // Original 5
        manhwaMap.get("Solo Leveling").setPrice(12.99);
        manhwaMap.get("Solo Leveling").setHasDiscount(true);
        manhwaMap.get("Solo Leveling").setCouponCode("SL-20OFF");
        manhwaMap.get("Solo Leveling").setPurchaseUrl("https://www.sample-retailer.com/solo-leveling"); 
        
        manhwaMap.get("Tower of God").setPrice(14.50);
        manhwaMap.get("Tower of God").setPurchaseUrl("https://www.sample-retailer.com/tower-of-god");
        
        manhwaMap.get("The Remarried Empress").setPrice(9.99);
        manhwaMap.get("The Remarried Empress").setHasDiscount(true);
        manhwaMap.get("The Remarried Empress").setCouponCode("EMPRESS-SALE");
        manhwaMap.get("The Remarried Empress").setPurchaseUrl("https://www.sample-retailer.com/remarried-empress");
        
        manhwaMap.get("Omniscient Reader's Viewpoint").setPrice(11.99);
        manhwaMap.get("Omniscient Reader's Viewpoint").setPurchaseUrl("https://www.sample-retailer.com/omniscient-reader");
        
        manhwaMap.get("True Beauty").setPrice(8.50);
        manhwaMap.get("True Beauty").setPurchaseUrl("https://www.sample-retailer.com/true-beauty");

        // --- NEW 5 MANHWA SHOPPING DATA ---
        manhwaMap.get("Weak Hero").setPrice(10.50);
        manhwaMap.get("Weak Hero").setPurchaseUrl("https://www.sample-retailer.com/weak-hero");
        
        manhwaMap.get("Villains Are Destined to Die").setPrice(13.99);
        manhwaMap.get("Villains Are Destined to Die").setHasDiscount(true);
        manhwaMap.get("Villains Are Destined to Die").setCouponCode("VILLAIN-DEAL");
        manhwaMap.get("Villains Are Destined to Die").setPurchaseUrl("https://www.sample-retailer.com/villainess-destined");
        
        manhwaMap.get("Lookism").setPrice(9.00);
        manhwaMap.get("Lookism").setPurchaseUrl("https://www.sample-retailer.com/lookism");

        manhwaMap.get("Eleceed").setPrice(12.00);
        manhwaMap.get("Eleceed").setHasDiscount(true);
        manhwaMap.get("Eleceed").setCouponCode("ELECEED-FLASH");
        manhwaMap.get("Eleceed").setPurchaseUrl("https://www.sample-retailer.com/eleceed");

        manhwaMap.get("Unordinary").setPrice(10.99);
        manhwaMap.get("Unordinary").setPurchaseUrl("https://www.sample-retailer.com/unordinary");
    }

    // --- Standard Database Getters (Unchanged) ---
    
    public List<Manhwa> getAllManhwa() {
        return new ArrayList<>(manhwaList);
    }
    
    public List<Manhwa> getByGenre(String genre) {
        return manhwaList.stream()
                .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }
    
    public List<Manhwa> getByDemographic(String demographic) {
        return manhwaList.stream()
                .filter(m -> m.getDemographic().equalsIgnoreCase(demographic))
                .collect(Collectors.toList());
    }
    
    public List<Manhwa> getByAgeRating(String ageRating) {
        return manhwaList.stream()
                .filter(m -> m.getAgeRating().equalsIgnoreCase(ageRating))
                .collect(Collectors.toList());
    }
    
    public void addManhwa(Manhwa manhwa) {
        manhwaList.add(manhwa);
    }
}