package cssgroup12finalproject;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ManhwaDatabase implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "manhwa_db.ser";
    private static ManhwaDatabase instance;
    private List<Manhwa> manhwaList;
    
    private static final String GITHUB_BASE_URL = "https://raw.githubusercontent.com/mapuanstudent27/CSS123PGroup12_Project/main/images/";

    private ManhwaDatabase() {
        manhwaList = new ArrayList<>();
        initializeSampleData();
        initializeShoppingData();
    }

    public static synchronized ManhwaDatabase getInstance() {
        if (instance == null) {
            instance = loadFromFile();
            if (instance == null) {
                instance = new ManhwaDatabase();
            }
        }
        return instance;
    }

    public synchronized void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
        } catch (IOException ex) {
            AppLogger.error("Failed to save database", ex);
        }
    }

    private static ManhwaDatabase loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object o = ois.readObject();
            if (o instanceof ManhwaDatabase) {
                AppLogger.info("Loaded existing database");
                return (ManhwaDatabase) o;
            }
        } catch (Exception ex) {
            AppLogger.error("Failed to load database", ex);
        }
        return null;
    }

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

    private void initializeSampleData() {
        // Add all manhwa to the list
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
                "A genius student uses strategy to dominate a school filled with bullies."));
        manhwaList.add(new Manhwa("Villains Are Destined to Die", "Suri", "Romance", "Josei", "16+", 9.1,
                "A student is transmigrated into a dating sim as the despised villainess."));
        manhwaList.add(new Manhwa("Lookism", "Park Tae-joon", "Drama", "Shounen", "13+", 8.9,
                "A bullied high school student lives a double life."));
        manhwaList.add(new Manhwa("Eleceed", "Jejak", "Fantasy", "Shounen", "13+", 9.6,
                "A powerful cat with human powers teams up with a kind-hearted boy."));
        manhwaList.add(new Manhwa("Unordinary", "Uru-chan", "Action", "Shounen", "13+", 9.4,
                "In a world where everyone has superpowers, a cripple hides a dark secret."));
        manhwaList.add(new Manhwa("The Beginning After the End", "TurtleMe", "Fantasy", "Shounen", "16+", 9.5,
                "A reincarnated king seeks redemption and strength in a magical world."));
        manhwaList.add(new Manhwa("Noblesse", "Son Jeho", "Supernatural", "Shounen", "16+", 9.0,
                "An ancient noble awakens in the modern world to protect humanity."));
        manhwaList.add(new Manhwa("God of High School", "Yongje Park", "Action", "Shounen", "16+", 9.1,
                "Teen fighters compete in an epic tournament hiding divine secrets."));
        manhwaList.add(new Manhwa("Sweet Home", "Carnby Kim", "Horror", "Seinen", "16+", 9.3,
                "A teenager struggles to survive in an apartment overrun by monsters."));
        manhwaList.add(new Manhwa("Bastard", "Carnby Kim", "Thriller", "Seinen", "16+", 9.5,
                "A boy lives in fear as he hides that his father is a serial killer."));
        manhwaList.add(new Manhwa("Leviathan", "Ryuho Son", "Sci-Fi", "Seinen", "16+", 8.9,
                "In a flooded world, a boy and sister fight to survive against pirates."));
        manhwaList.add(new Manhwa("Get Schooled", "Chongtak", "Drama", "Seinen", "16+", 9.0,
                "A government enforcer uses brutal methods to reform corrupt schools."));
        manhwaList.add(new Manhwa("Jungle Juice", "Hyung Eun", "Action", "Shounen", "16+", 8.7,
                "A student hiding insect wings gets thrust into a world of hybrids."));
        manhwaList.add(new Manhwa("DICE", "Yun Hyunseok", "Fantasy", "Shounen", "16+", 8.5,
                "Mysterious cubes grant players the ability to alter their fate."));
        manhwaList.add(new Manhwa("Hardcore Leveling Warrior", "Sehun Kim", "Fantasy", "Shounen", "16+", 8.8,
                "A top gamer seeks redemption after losing everything in a deadly VR world."));
        manhwaList.add(new Manhwa("Wind Breaker", "Jo Yongseok", "Sports", "Shounen", "16+", 9.2,
                "A biker gang protects their town while learning about loyalty."));
        manhwaList.add(new Manhwa("Viral Hit", "Taejun Park", "Action", "Seinen", "16+", 9.1,
                "A bullied teen becomes viral by fighting back through livestreams."));
        manhwaList.add(new Manhwa("Questism", "Taejun Park", "Action", "Seinen", "16+", 8.9,
                "A delinquent's life turns into an RPG with bizarre quests."));
        manhwaList.add(new Manhwa("Nano Machine", "Han Joong Wueol Ya", "Action", "Shounen", "16+", 9.0,
                "Nanotechnology from the future grants a prince unparalleled power."));
        manhwaList.add(new Manhwa("Return of the Mount Hua Sect", "Biga", "Martial Arts", "Shounen", "16+", 9.3,
                "The last member of a fallen sect reincarnates to restore its glory."));
        manhwaList.add(new Manhwa("The Breaker", "Jeon Geuk-jin", "Martial Arts", "Seinen", "16+", 9.1,
                "A bullied teenager meets a martial artist who drags him into Murim."));
        manhwaList.add(new Manhwa("Mercenary Enrollment", "YC", "Action", "Seinen", "16+", 9.4,
                "A teenage ex-mercenary tries to live a normal high school life."));
        manhwaList.add(new Manhwa("Second Life Ranker", "Sadoyeon", "Fantasy", "Seinen", "16+", 9.2,
                "A man climbs a deadly tower seeking revenge for his twin brother."));
        manhwaList.add(new Manhwa("Dungeon Reset", "Ant Studio", "Adventure", "Shounen", "16+", 8.8,
                "A dungeon glitch traps a player who turns endless resets into strength."));
        manhwaList.add(new Manhwa("A Returner's Magic Should Be Special", "Sun-Wook Jeon", "Fantasy", "Shounen", "16+", 9.1,
                "A magician goes back in time to prevent humanity's destruction."));
        manhwaList.add(new Manhwa("Trash of the Count's Family", "Yoo Ryeo Han", "Fantasy", "Seinen", "16+", 9.0,
                "A man transmigrates as a trashy noble and tries to live peacefully."));
        manhwaList.add(new Manhwa("Player Who Can't Level Up", "Garam Teo", "Fantasy", "Seinen", "16+", 8.9,
                "A player stuck at level one discovers unique growth abilities."));
        manhwaList.add(new Manhwa("Reaper of the Drifting Moon", "Mok-In", "Action", "Seinen", "16+", 9.3,
                "A man raised as an assassin escapes and embarks on vengeance."));
        manhwaList.add(new Manhwa("Infinite Level Up in Murim", "Gom-Guk", "Martial Arts", "Shounen", "16+", 8.8,
                "A man gains a system that lets him level up endlessly in Murim."));
        manhwaList.add(new Manhwa("The Legend of the Northern Blade", "Hae Min", "Martial Arts", "Seinen", "16+", 9.2,
                "The last heir trains to reclaim his father's legacy."));
        manhwaList.add(new Manhwa("SSS-Class Revival Hunter", "Shin Noah", "Fantasy", "Seinen", "16+", 9.1,
                "A weak hunter gains the ability to return to the past upon death."));
        manhwaList.add(new Manhwa("Survival Story of a Sword King", "Kwon Sun Kyu", "Fantasy", "Seinen", "16+", 9.0,
                "A man trapped in a harsh world becomes overpowered after trials."));
        manhwaList.add(new Manhwa("Tomb Raider King", "San.G", "Action", "Seinen", "16+", 8.9,
                "A relic hunter races to seize powerful artifacts and rewrite destiny."));
        manhwaList.add(new Manhwa("Max Level Returner", "Jang Sung-Lak", "Action", "Shounen", "16+", 8.7,
                "The first player to clear a deadly game returns to protect humanity."));
        manhwaList.add(new Manhwa("Overgeared", "Park Saenal", "Fantasy", "Seinen", "16+", 9.0,
                "A struggling gamer obtains a legendary class and crafts his way up."));
        manhwaList.add(new Manhwa("The Live", "Ant Studio", "Drama", "Seinen", "16+", 8.9,
                "A man gets a second chance to save his family by clearing a game."));
        manhwaList.add(new Manhwa("Leveling With the Gods", "Ohyeon", "Action", "Seinen", "16+", 9.3,
                "A warrior climbs from the abyss to challenge the gods again."));
        manhwaList.add(new Manhwa("The Gamer", "Sung San-Young", "Fantasy", "Shounen", "16+", 8.6,
                "A student gains the ability to live life like a video game."));
        manhwaList.add(new Manhwa("Gosu", "Ji-Min Park", "Martial Arts", "Seinen", "16+", 9.1,
                "A master's disciple sets out on a comedic journey of revenge."));
        manhwaList.add(new Manhwa("Black Haze", "Jin-Hwan Park", "Fantasy", "Seinen", "16+", 8.7,
                "A powerful magician hiding his identity becomes a student bodyguard."));
        manhwaList.add(new Manhwa("The God of Blackfield", "Han Ji-Hoon", "Action", "Seinen", "16+", 8.9,
                "A killed soldier awakens in another body seeking vengeance."));
        manhwaList.add(new Manhwa("The Boxer", "JH", "Sports", "Seinen", "16+", 8.8,
                "A quiet prodigy explores the meaning of strength in boxing."));
        manhwaList.add(new Manhwa("The Scholar's Reincarnation", "Cheongcho", "Fantasy", "Seinen", "16+", 8.5,
                "A warlord reborn as a scholar's son seeks redemption through peace."));
        manhwaList.add(new Manhwa("The Legendary Moonlight Sculptor", "Nam Heesung", "Fantasy", "Shounen", "16+", 9.0,
                "A broke gamer becomes a legend through creativity in VR."));
        
        AppLogger.info("Initialized " + manhwaList.size() + " manhwa entries");
        
        // Set cover image paths
        for (Manhwa m : manhwaList) {
            String normalizedTitle = m.getTitle().toLowerCase()
                    .replaceAll("[^a-z0-9]", "_")
                    .replaceAll("_+", "_")
                    .replaceAll("'", "");
            m.setCoverImagePath(GITHUB_BASE_URL + normalizedTitle + ".jpg");
        }
    }

    private void initializeShoppingData() {
        Map<String, Manhwa> map = manhwaList.stream()
                .collect(Collectors.toMap(Manhwa::getTitle, m -> m, (a, b) -> a));
        
        if (map.containsKey("Solo Leveling")) {
            map.get("Solo Leveling").setPrice(12.99);
            map.get("Solo Leveling").setHasDiscount(true);
            map.get("Solo Leveling").setCouponCode("SL-20OFF");
            map.get("Solo Leveling").setPurchaseUrl("https://www.sample-retailer.com/solo-leveling");
        }
        if (map.containsKey("Tower of God")) {
            map.get("Tower of God").setPrice(14.50);
            map.get("Tower of God").setPurchaseUrl("https://www.sample-retailer.com/tower-of-god");
        }
        if (map.containsKey("The Remarried Empress")) {
            map.get("The Remarried Empress").setPrice(9.99);
            map.get("The Remarried Empress").setHasDiscount(true);
            map.get("The Remarried Empress").setCouponCode("EMPRESS-SALE");
            map.get("The Remarried Empress").setPurchaseUrl("https://www.sample-retailer.com/empress");
        }
        if (map.containsKey("Omniscient Reader's Viewpoint")) {
            map.get("Omniscient Reader's Viewpoint").setPrice(11.99);
            map.get("Omniscient Reader's Viewpoint").setPurchaseUrl("https://www.sample-retailer.com/orv");
        }
        if (map.containsKey("Eleceed")) {
            map.get("Eleceed").setPrice(12.00);
            map.get("Eleceed").setHasDiscount(true);
            map.get("Eleceed").setCouponCode("ELECEED-FLASH");
            map.get("Eleceed").setPurchaseUrl("https://www.sample-retailer.com/eleceed");
        }
        
        AppLogger.info("Shopping data initialized");
        saveToFile();
    }
}