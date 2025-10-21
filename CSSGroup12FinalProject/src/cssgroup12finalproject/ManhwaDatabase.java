package cssgroup12finalproject;

import java.io
import java.util.*;
import java.util.stream.Collectors;

public class ManhwaDatabase implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "manhwa_db.ser";

    private static ManhwaDatabase instance;
    private List<Manhwa> manhwaList;

    private ManhwaDatabase() {
        manhwaList = new ArrayList<>();
        initializeSampleData();
        initializeShoppingData();
    }

    // Thread-safe lazy singleton
    public static synchronized ManhwaDatabase getInstance() {
        if (instance == null) {
            // try loading from disk first
            instance = loadFromFile();
            if (instance == null) {
                instance = new ManhwaDatabase();
            }
        }
        return instance;
    }

    // Persistence: save entire database to local file (binary serialization)
    public synchronized void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static ManhwaDatabase loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            if (o instanceof ManhwaDatabase) {
                return (ManhwaDatabase) o;
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // CRUD
    public synchronized void addManhwa(Manhwa m) {
        manhwaList.add(m);
        saveToFile();
    }

    public synchronized boolean removeManhwaByTitle(String title) {
        boolean removed = manhwaList.removeIf(m -> m.getTitle().equalsIgnoreCase(title));
        if (removed) saveToFile();
        return removed;
    }

    public synchronized boolean updateManhwa(String title, Manhwa updated) {
        for (int i = 0; i < manhwaList.size(); i++) {
            if (manhwaList.get(i).getTitle().equalsIgnoreCase(title)) {
                manhwaList.set(i, updated);
                saveToFile();
                return true;
            }
        }
        return false;
    }

    // Getters / queries
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

    public List<Manhwa> searchByTitle(String term) {
        String q = term.toLowerCase();
        return manhwaList.stream()
                .filter(m -> m.getTitle().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<Manhwa> getTopRated(int count) {
        return manhwaList.stream()
                .sorted((a,b) -> Double.compare(b.getRating(), a.getRating()))
                .limit(count)
                .collect(Collectors.toList());
    }

    // ---- Your existing sample population methods ----
    private void initializeSampleData() {
        // (Use your existing initializers — same entries you provided)
        // For brevity in this snippet just call a helper that adds your sample list
        // Replace with your sample-add code or call an existing method.
        // Example:
        manhwaList.add(new Manhwa("Solo Leveling", "Chugong", "Action", "Shounen", "13+", 9.5,
                "A weak hunter becomes the strongest through a mysterious system."));
        manhwaList.add(new Manhwa("Tower of God", "SIU", "Fantasy", "Shounen", "13+", 9.2,
                "A boy enters a mysterious tower to find his friend."));
        manhwaList.add(new Manhwa("The Remarried Empress", "Alphatart", "Romance", "Shoujo", "16+", 9.0,
                "An empress remarries after her husband betrays her."));
        manhwaList.add(new Manhwa("Omniscient Reader's Viewpoint", "singNsong", "Fantasy", "Shounen", "13+", 9.4,
                "A reader becomes part of the web novel he was reading."));
        manhwaList.add(new Manhwa("True Beauty", "Yaongyi", "Romance", "Shoujo", "13+", 8.8,
                "A girl becomes popular after mastering makeup."));
        manhwaList.add(new Manhwa("Weak Hero", "Seopass", "Action", "Seinen", "16+", 9.3,
                "A deceptively weak-looking genius student uses strategy to dominate a school filled with bullies."));
        manhwaList.add(new Manhwa("Villains Are Destined to Die", "Suri", "Romance", "Josei", "16+", 9.1,
                "A student is transmigrated into a dating sim game as the despised villainess and must choose a target to survive."));
        manhwaList.add(new Manhwa("Lookism", "Park Tae-joon", "Drama", "Shounen", "13+", 8.9,
                "A bullied high school student lives a double life."));
        manhwaList.add(new Manhwa("Eleceed", "Jejak", "Fantasy", "Shounen", "13+", 9.6,
                "A powerful cat with human powers teams up with a kind-hearted boy."));
        manhwaList.add(new Manhwa("Unordinary", "Uru-chan", "Action", "Shounen", "13+", 9.4,
                "In a world where everyone has superpowers, a 'cripple' hides a dark secret."));
    }

    private void initializeShoppingData() {
        Map<String, Manhwa> map = manhwaList.stream().collect(Collectors.toMap(Manhwa::getTitle, m -> m));
        if (map.containsKey("Solo Leveling")) {
            map.get("Solo Leveling").setPrice(12.99);
            map.get("Solo Leveling").setHasDiscount(true);
            map.get("Solo Leveling").setCouponCode("SL-20OFF");
            map.get("Solo Leveling").setPurchaseUrl("https://www.sample-retailer.com/solo-leveling");
        }
        // ... (add the rest same as your previous initializeShoppingData)
        if (map.containsKey("Tower of God")) map.get("Tower of God").setPrice(14.50);
        if (map.containsKey("The Remarried Empress")) {
            map.get("The Remarried Empress").setPrice(9.99);
            map.get("The Remarried Empress").setHasDiscount(true);
            map.get("The Remarried Empress").setCouponCode("EMPRESS-SALE");
        }
        if (map.containsKey("Omniscient Reader's Viewpoint")) map.get("Omniscient Reader's Viewpoint").setPrice(11.99);
        if (map.containsKey("True Beauty")) map.get("True Beauty").setPrice(8.50);
        if (map.containsKey("Weak Hero")) map.get("Weak Hero").setPrice(10.50);
        if (map.containsKey("Villains Are Destined to Die")) {
            map.get("Villains Are Destined to Die").setPrice(13.99);
            map.get("Villains Are Destined to Die").setHasDiscount(true);
            map.get("Villains Are Destined to Die").setCouponCode("VILLAIN-DEAL");
        }
        if (map.containsKey("Lookism")) map.get("Lookism").setPrice(9.00);
        if (map.containsKey("Eleceed")) {
            map.get("Eleceed").setPrice(12.00);
            map.get("Eleceed").setHasDiscount(true);
            map.get("Eleceed").setCouponCode("ELECEED-FLASH");
        }
        if (map.containsKey("Unordinary")) map.get("Unordinary").setPrice(10.99);

        // Save to disk now that DB is initialized
        saveToFile();
    }
}
