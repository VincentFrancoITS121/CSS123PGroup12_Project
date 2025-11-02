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
        manhwaList.add(new Manhwa("Yumi's Cells", "Lee Dong-gun", "Romance", "Josei", 16+", 9.0,
                "A heartwarming story that explores Yumi’s emotions through her personified brain cells as she navigates love, work, and everyday life."));
        
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

        if (map.containsKey("True Beauty")) {
            map.get("True Beauty").setPrice(10.99);
            map.get("True Beauty").setHasDiscount(true);
            map.get("True Beauty").setCouponCode("TB-15OFF");
            map.get("True Beauty").setPurchaseUrl("https://www.sample-retailer.com/true-beauty");
        }

        if (map.containsKey("Weak Hero")) {
            map.get("Weak Hero").setPrice(11.99);
            map.get("Weak Hero").setPurchaseUrl("https://www.sample-retailer.com/weak-hero");
        }

        if (map.containsKey("Villains Are Destined to Die")) {
            map.get("Villains Are Destined to Die").setPrice(12.49);
            map.get("Villains Are Destined to Die").setHasDiscount(true);
            map.get("Villains Are Destined to Die").setCouponCode("VDD-10OFF");
            map.get("Villains Are Destined to Die").setPurchaseUrl("https://www.sample-retailer.com/villains-are-destined-to-die");
        }

        if (map.containsKey("Lookism")) {
            map.get("Lookism").setPrice(9.99);
            map.get("Lookism").setPurchaseUrl("https://www.sample-retailer.com/lookism");
        }

        if (map.containsKey("Unordinary")) {
            map.get("Unordinary").setPrice(10.49);
            map.get("Unordinary").setHasDiscount(true);
            map.get("Unordinary").setCouponCode("UNORD-5OFF");
            map.get("Unordinary").setPurchaseUrl("https://www.sample-retailer.com/unordinary");
        }

        if (map.containsKey("The Beginning After the End")) {
            map.get("The Beginning After the End").setPrice(13.99);
            map.get("The Beginning After the End").setPurchaseUrl("https://www.sample-retailer.com/the-beginning-after-the-end");
        }

        if (map.containsKey("Noblesse")) {
            map.get("Noblesse").setPrice(11.50);
            map.get("Noblesse").setPurchaseUrl("https://www.sample-retailer.com/noblesse");
        }

        if (map.containsKey("God of High School")) {
            map.get("God of High School").setPrice(12.75);
            map.get("God of High School").setHasDiscount(true);
            map.get("God of High School").setCouponCode("GOHS-10OFF");
            map.get("God of High School").setPurchaseUrl("https://www.sample-retailer.com/god-of-high-school");
        }

        if (map.containsKey("Sweet Home")) {
            map.get("Sweet Home").setPrice(11.99);
            map.get("Sweet Home").setPurchaseUrl("https://www.sample-retailer.com/sweet-home");
        }

        if (map.containsKey("Bastard")) {
            map.get("Bastard").setPrice(10.99);
            map.get("Bastard").setHasDiscount(true);
            map.get("Bastard").setCouponCode("BST-20OFF");
            map.get("Bastard").setPurchaseUrl("https://www.sample-retailer.com/bastard");
        }

        if (map.containsKey("Leviathan")) {
            map.get("Leviathan").setPrice(12.25);
            map.get("Leviathan").setPurchaseUrl("https://www.sample-retailer.com/leviathan");
        }

        if (map.containsKey("Get Schooled")) {
            map.get("Get Schooled").setPrice(10.75);
            map.get("Get Schooled").setPurchaseUrl("https://www.sample-retailer.com/get-schooled");
        }

        if (map.containsKey("Jungle Juice")) {
            map.get("Jungle Juice").setPrice(11.25);
            map.get("Jungle Juice").setHasDiscount(true);
            map.get("Jungle Juice").setCouponCode("JJ-10OFF");
            map.get("Jungle Juice").setPurchaseUrl("https://www.sample-retailer.com/jungle-juice");
        }

        if (map.containsKey("DICE")) {
            map.get("DICE").setPrice(9.49);
            map.get("DICE").setPurchaseUrl("https://www.sample-retailer.com/dice");
        }

        if (map.containsKey("Hardcore Leveling Warrior")) {
            map.get("Hardcore Leveling Warrior").setPrice(12.99);
            map.get("Hardcore Leveling Warrior").setPurchaseUrl("https://www.sample-retailer.com/hardcore-leveling-warrior");
        }

        if (map.containsKey("Wind Breaker")) {
            map.get("Wind Breaker").setPrice(11.49);
            map.get("Wind Breaker").setHasDiscount(true);
            map.get("Wind Breaker").setCouponCode("WB-15OFF");
            map.get("Wind Breaker").setPurchaseUrl("https://www.sample-retailer.com/wind-breaker");
        }

        if (map.containsKey("Viral Hit")) {
            map.get("Viral Hit").setPrice(10.25);
            map.get("Viral Hit").setPurchaseUrl("https://www.sample-retailer.com/viral-hit");
        }

        if (map.containsKey("Questism")) {
            map.get("Questism").setPrice(10.75);
            map.get("Questism").setHasDiscount(true);
            map.get("Questism").setCouponCode("QSM-10OFF");
            map.get("Questism").setPurchaseUrl("https://www.sample-retailer.com/questism");
        }

        if (map.containsKey("Nano Machine")) {
            map.get("Nano Machine").setPrice(12.50);
            map.get("Nano Machine").setPurchaseUrl("https://www.sample-retailer.com/nano-machine");
        }

        if (map.containsKey("Return of the Mount Hua Sect")) {
            map.get("Return of the Mount Hua Sect").setPrice(13.25);
            map.get("Return of the Mount Hua Sect").setHasDiscount(true);
            map.get("Return of the Mount Hua Sect").setCouponCode("RMHS-10OFF");
            map.get("Return of the Mount Hua Sect").setPurchaseUrl("https://www.sample-retailer.com/return-of-the-mount-hua-sect");
        }

        if (map.containsKey("The Breaker")) {
            map.get("The Breaker").setPrice(11.99);
            map.get("The Breaker").setHasDiscount(true);
            map.get("The Breaker").setCouponCode("BRK-10OFF");
            map.get("The Breaker").setPurchaseUrl("https://www.sample-retailer.com/the-breaker");
        }

        if (map.containsKey("Mercenary Enrollment")) {
            map.get("Mercenary Enrollment").setPrice(12.49);
            map.get("Mercenary Enrollment").setPurchaseUrl("https://www.sample-retailer.com/mercenary-enrollment");
        }

        if (map.containsKey("Second Life Ranker")) {
            map.get("Second Life Ranker").setPrice(13.25);
            map.get("Second Life Ranker").setHasDiscount(true);
            map.get("Second Life Ranker").setCouponCode("SLR-15OFF");
            map.get("Second Life Ranker").setPurchaseUrl("https://www.sample-retailer.com/second-life-ranker");
        }

        if (map.containsKey("Dungeon Reset")) {
            map.get("Dungeon Reset").setPrice(10.99);
            map.get("Dungeon Reset").setPurchaseUrl("https://www.sample-retailer.com/dungeon-reset");
        }

        if (map.containsKey("A Returner's Magic Should Be Special")) {
            map.get("A Returner's Magic Should Be Special").setPrice(12.75);
            map.get("A Returner's Magic Should Be Special").setPurchaseUrl("https://www.sample-retailer.com/a-returners-magic");
        }

        if (map.containsKey("Trash of the Count's Family")) {
            map.get("Trash of the Count's Family").setPrice(11.50);
            map.get("Trash of the Count's Family").setHasDiscount(true);
            map.get("Trash of the Count's Family").setCouponCode("TCF-10OFF");
            map.get("Trash of the Count's Family").setPurchaseUrl("https://www.sample-retailer.com/trash-of-the-counts-family");
        }

        if (map.containsKey("Player Who Can't Level Up")) {
            map.get("Player Who Can't Level Up").setPrice(11.25);
            map.get("Player Who Can't Level Up").setPurchaseUrl("https://www.sample-retailer.com/player-who-cant-level-up");
        }

        if (map.containsKey("Reaper of the Drifting Moon")) {
            map.get("Reaper of the Drifting Moon").setPrice(13.75);
            map.get("Reaper of the Drifting Moon").setHasDiscount(true);
            map.get("Reaper of the Drifting Moon").setCouponCode("RDM-20OFF");
            map.get("Reaper of the Drifting Moon").setPurchaseUrl("https://www.sample-retailer.com/reaper-of-the-drifting-moon");
        }

        if (map.containsKey("Infinite Level Up in Murim")) {
            map.get("Infinite Level Up in Murim").setPrice(12.50);
            map.get("Infinite Level Up in Murim").setPurchaseUrl("https://www.sample-retailer.com/infinite-level-up-in-murim");
        }

        if (map.containsKey("The Legend of the Northern Blade")) {
            map.get("The Legend of the Northern Blade").setPrice(12.99);
            map.get("The Legend of the Northern Blade").setPurchaseUrl("https://www.sample-retailer.com/the-legend-of-the-northern-blade");
        }

        if (map.containsKey("SSS-Class Revival Hunter")) {
            map.get("SSS-Class Revival Hunter").setPrice(11.75);
            map.get("SSS-Class Revival Hunter").setHasDiscount(true);
            map.get("SSS-Class Revival Hunter").setCouponCode("SSS-15OFF");
            map.get("SSS-Class Revival Hunter").setPurchaseUrl("https://www.sample-retailer.com/sss-class-revival-hunter");
        }

        if (map.containsKey("Survival Story of a Sword King")) {
            map.get("Survival Story of a Sword King").setPrice(12.25);
            map.get("Survival Story of a Sword King").setPurchaseUrl("https://www.sample-retailer.com/survival-story-of-a-sword-king");
        }

        if (map.containsKey("Tomb Raider King")) {
            map.get("Tomb Raider King").setPrice(11.49);
            map.get("Tomb Raider King").setHasDiscount(true);
            map.get("Tomb Raider King").setCouponCode("TRK-10OFF");
            map.get("Tomb Raider King").setPurchaseUrl("https://www.sample-retailer.com/tomb-raider-king");
        }

        if (map.containsKey("Max Level Returner")) {
            map.get("Max Level Returner").setPrice(10.99);
            map.get("Max Level Returner").setPurchaseUrl("https://www.sample-retailer.com/max-level-returner");
        }

        if (map.containsKey("Overgeared")) {
            map.get("Overgeared").setPrice(13.50);
            map.get("Overgeared").setPurchaseUrl("https://www.sample-retailer.com/overgeared");
        }

        if (map.containsKey("The Live")) {
            map.get("The Live").setPrice(11.25);
            map.get("The Live").setHasDiscount(true);
            map.get("The Live").setCouponCode("LIVE-10OFF");
            map.get("The Live").setPurchaseUrl("https://www.sample-retailer.com/the-live");
        }

        if (map.containsKey("Leveling With the Gods")) {
            map.get("Leveling With the Gods").setPrice(12.99);
            map.get("Leveling With the Gods").setPurchaseUrl("https://www.sample-retailer.com/leveling-with-the-gods");
        }

        if (map.containsKey("The Gamer")) {
            map.get("The Gamer").setPrice(10.75);
            map.get("The Gamer").setPurchaseUrl("https://www.sample-retailer.com/the-gamer");
        }

        if (map.containsKey("Gosu")) {
            map.get("Gosu").setPrice(11.99);
            map.get("Gosu").setHasDiscount(true);
            map.get("Gosu").setCouponCode("GOSU-15OFF");
            map.get("Gosu").setPurchaseUrl("https://www.sample-retailer.com/gosu");
        }

        if (map.containsKey("Black Haze")) {
            map.get("Black Haze").setPrice(10.99);
            map.get("Black Haze").setPurchaseUrl("https://www.sample-retailer.com/black-haze");
        }

        if (map.containsKey("The God of Blackfield")) {
            map.get("The God of Blackfield").setPrice(12.25);
            map.get("The God of Blackfield").setHasDiscount(true);
            map.get("The God of Blackfield").setCouponCode("GOB-10OFF");
            map.get("The God of Blackfield").setPurchaseUrl("https://www.sample-retailer.com/the-god-of-blackfield");
        }

        if (map.containsKey("The Boxer")) {
            map.get("The Boxer").setPrice(11.50);
            map.get("The Boxer").setPurchaseUrl("https://www.sample-retailer.com/the-boxer");
        }
        
        if (map.containsKey("The Scholar's Reincarnation")) {
            map.get("The Scholar's Reincarnation").setPrice(11.99);
            map.get("The Scholar's Reincarnation").setHasDiscount(true);
            map.get("The Scholar's Reincarnation").setCouponCode("TSR-15OFF");
            map.get("The Scholar's Reincarnation").setPurchaseUrl("https://www.sample-retailer.com/the-scholars-reincarnation");
        }

        if (map.containsKey("The Legendary Moonlight Sculptor")) {
            map.get("The Legendary Moonlight Sculptor").setPrice(13.25);
            map.get("The Legendary Moonlight Sculptor").setPurchaseUrl("https://www.sample-retailer.com/the-legendary-moonlight-sculptor");
        }

        if (map.containsKey("Yumi's Cells")) {
            map.get("Yumi's Cells").setPrice(11.49);
            map.get("Yumi's Cells").setHasDiscount(true);
            map.get("Yumi's Cells").setCouponCode("YC-15OFF");
            map.get("Yumi's Cells").setPurchaseUrl("https://www.sample-retailer.com/yumis-cells");
        }
        
        AppLogger.info("Shopping data initialized");
        saveToFile();
    }

}
