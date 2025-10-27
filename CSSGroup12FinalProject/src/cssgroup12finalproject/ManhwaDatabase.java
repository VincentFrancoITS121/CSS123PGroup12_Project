package cssgroup12finalproject;

import java.io.*;
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
        manhwaList.add(new Manhwa("The Beginning After the End", "TurtleMe", "Fantasy", "Shounen", "16+", 9.5, 
                                  8.99, true, "TBAE2025", "https://tapas.io/series/tbate"));
        manhwaList.add(new Manhwa("Noblesse", "Son Jeho", "Supernatural", "Shounen", "16+", 9.0,
                                  6.50, false, "", "https://www.webtoons.com/en/fantasy/noblesse"));
        manhwaList.add(new Manhwa("God of High School", "Yongje Park", "Action", "Shounen", "16+", 9.1,
                                  7.99, false, "", "https://www.webtoons.com/en/action/the-god-of-high-school"));
        manhwaList.add(new Manhwa("Sweet Home", "Carnby Kim", "Horror", "Seinen", "16+", 9.3,
                                  8.25, true, "SWHM25", "https://www.webtoons.com/en/thriller/sweethome"));
        manhwaList.add(new Manhwa("Bastard", "Carnby Kim", "Thriller", "Seinen", "16+", 9.5,
                                  8.50, false, "", "https://www.webtoons.com/en/thriller/bastard"));
        manhwaList.add(new Manhwa("Leviathan", "Ryuho Son", "Sci-Fi", "Seinen", "16+", 8.9, 7.70,
                                  false, "", "https://www.webtoons.com/en/action/leviathan"));
        manhwaList.add(new Manhwa("Get Schooled", "Chongtak", "Drama", "Seinen", "16+", 9.0, 7.99,
                                  true, "EDU25", "https://www.webtoons.com/en/drama/get-schooled"));
        manhwaList.add(new Manhwa("Jungle Juice", "Hyung Eun", "Action", "Shounen", "16+", 8.7,
                                  6.99, false, "", "https://www.webtoons.com/en/action/jungle-juice"));
        manhwaList.add(new Manhwa("DICE: The Cube That Changes Everything", "Yun Hyunseok", "Fantasy", "Shounen", "16+", 8.5,
                                  6.70, false, "", "https://www.webtoons.com/en/fantasy/dice"));
        manhwaList.add(new Manhwa("Hardcore Leveling Warrior", "Sehun Kim", "Fantasy", "Shounen", "16+", 8.8,
                                  7.10, true, "HLW25", "https://www.webtoons.com/en/action/hardcore-leveling-warrior"));
        manhwaList.add(new Manhwa("Wind Breaker", "Jo Yongseok", "Sports", "Shounen", "16+", 9.2,
                                  7.50, false, "", "https://www.webtoons.com/en/sports/windbreaker"));
        manhwaList.add(new Manhwa("Viral Hit", "Taejun Park", "Action", "Seinen", "16+", 9.1,
                                  8.10, true, "VRL25", "https://www.webtoons.com/en/action/how-to-fight"));
        manhwaList.add(new Manhwa("Questism", "Taejun Park", "Action", "Seinen", "16+", 8.9,
                                  7.90, true, "QSM25", "https://www.webtoons.com/en/action/questism"));
        manhwaList.add(new Manhwa("Nano Machine", "Han Joong Wueol Ya", "Action", "Shounen", "16+", 9.0,
                                  7.99, true, "NANO25", "https://www.webtoons.com/en/action/nano-machine"));
        manhwaList.add(new Manhwa("Return of the Mount Hua Sect", "Biga", "Martial Arts", "Shounen", "16+", 9.3,
                                  8.25, true, "MTHUA25", "https://www.webtoons.com/en/action/return-of-the-mount-hua-sect"));
        manhwaList.add(new Manhwa("The Breaker: Eternal Force", "Jeon Geuk-jin", "Martial Arts", "Seinen", "16+", 9.1,
                                  7.70, false, "", "https://www.webtoons.com/en/action/the-breaker-eternal-force"));
        manhwaList.add(new Manhwa("Mercenary Enrollment", "YC", "Action", "Seinen", "16+", 9.4,
                                  8.10, true, "MERC25", "https://www.webtoons.com/en/action/mercenary-enrollment"));
        manhwaList.add(new Manhwa("Second Life Ranker", "Sadoyeon", "Fantasy", "Seinen", "16+", 9.2,
                                  7.80, false, "", "https://www.tappytoon.com/en/book/second-life-ranker"));
        manhwaList.add(new Manhwa("Dungeon Reset", "Ant Studio", "Adventure", "Shounen", "16+", 8.8,
                                  6.99, true, "DUNR25", "https://www.webtoons.com/en/fantasy/dungeon-reset"));
        manhwaList.add(new Manhwa("A Returner’s Magic Should Be Special", "Sun-Wook Jeon", "Fantasy", "Shounen", "16+", 9.1,
                                  7.50, false, "", "https://www.webtoons.com/en/fantasy/a-returners-magic-should-be-special"));
        manhwaList.add(new Manhwa("Trash of the Count’s Family", "Yoo Ryeo Han", "Fantasy", "Seinen", "16+", 9.0,
                                  8.00, true, "TCF25", "https://www.tappytoon.com/en/book/trash-of-the-counts-family"));
        manhwaList.add(new Manhwa("Player Who Can’t Level Up", "Garam Teo", "Fantasy", "Seinen", "16+", 8.9,
                                  7.60, true, "PWCLU25", "https://www.webtoons.com/en/fantasy/player-who-cant-level-up"));
        manhwaList.add(new Manhwa("Reaper of the Drifting Moon", "Mok-In", "Action", "Seinen", "16+", 9.3,
                                  8.40, false, "", "https://www.tappytoon.com/en/book/reaper-of-the-drifting-moon"));
        manhwaList.add(new Manhwa("Infinite Level Up in Murim", "Gom-Guk", "Martial Arts", "Shounen", "16+", 8.8,
                                  7.25, false, "", "https://www.webtoons.com/en/action/infinite-level-up-in-murim"));
        manhwaList.add(new Manhwa("The Legend of the Northern Blade", "Hae Min", "Martial Arts", "Seinen", "16+", 9.2,
                                  8.50, true, "NORTH25", "https://www.tappytoon.com/en/book/the-legend-of-the-northern-blade"));
        manhwaList.add(new Manhwa("SSS-Class Revival Hunter", "Shin Noah", "Fantasy", "Seinen", "16+", 9.1,
                                  7.99, false, "", "https://www.webtoons.com/en/action/sss-class-revival-hunter"));
        manhwaList.add(new Manhwa("Survival Story of a Sword King in a Fantasy World", "Kwon Sun Kyu", "Fantasy", "Seinen", "16+", 9.0,
                                  7.80, false, "", "https://www.webtoons.com/en/action/survival-story-of-a-sword-king-in-a-fantasy-world"));
        manhwaList.add(new Manhwa("Tomb Raider King", "San.G", "Action", "Seinen", "16+", 8.9,
                                  7.40, true, "TRK25", "https://www.webtoons.com/en/action/tomb-raider-king"));
        manhwaList.add(new Manhwa("Max Level Returner", "Jang Sung-Lak", "Action", "Shounen", "16+", 8.7,
                                  7.20, true, "MLR25", "https://www.webtoons.com/en/action/max-level-returner"));
        manhwaList.add(new Manhwa("Overgeared", "Park Saenal", "Fantasy", "Seinen", "16+", 9.0,
                                  8.10, true, "OVRG25", "https://www.tappytoon.com/en/book/overgeared"));
        manhwaList.add(new Manhwa("The Live", "Ant Studio", "Drama", "Seinen", "16+", 8.9,
                                  7.40, false, "", "https://www.webtoons.com/en/drama/the-live"));
        manhwaList.add(new Manhwa("Leveling With the Gods", "Ohyeon", "Action", "Seinen", "16+", 9.3,
                                  8.60, true, "LWTG25", "https://www.webtoons.com/en/action/leveling-with-the-gods"));
        manhwaList.add(new Manhwa("The Gamer", "Sung San-Young", "Fantasy", "Shounen", "16+", 8.6,
                                  6.99, false, "", "https://www.webtoons.com/en/action/the-gamer"));
        manhwaList.add(new Manhwa("Gosu", "Ji-Min Park", "Martial Arts", "Seinen", "16+", 9.1,
                                  7.99, false, "", "https://www.webtoons.com/en/action/gosu"));
        manhwaList.add(new Manhwa("Black Haze", "Jin-Hwan Park", "Fantasy", "Seinen", "16+", 8.7,
                                  6.50, false, "", "https://example-retailer.com/black-haze"));
        manhwaList.add(new Manhwa("The God of Blackfield", "Han Ji-Hoon", "Action", "Seinen", "16+", 8.9,
                                  7.50, true, "GBF25", "https://example-retailer.com/god-of-blackfield"));
        manhwaList.add(new Manhwa("The Boxer", "JH", "Sports", "Seinen", "16+", 8.8,
                                  7.20, false, "", "https://www.izepress.com/the-boxer"));
        manhwaList.add(new Manhwa("The Scholar's Reincarnation", "Cheongcho", "Fantasy", "Seinen", "16+", 8.5,
                                  6.99, false, "", "https://www.webtoons.com/en/fantasy/scholars-reincarnation"));
        manhwaList.add(new Manhwa("The Legendary Moonlight Sculptor", "Nam Heesung", "Fantasy", "Shounen", "16+", 9.0,
                                  8.10, true, "LMS25", "https://www.tappytoon.com/en/book/moonlight-sculptor"));
        manhwaList.add(new Manhwa("A Capable Maid", "Hyeok", "Drama", "Seinen", "16+", 8.2,
                                  6.75, false, "", "https://example-retailer.com/a-capable-maid"));
        manhwaList.add(new Manhwa("The Villainess Reverses the Hourglass", "Satoshi", "Drama", "Seinen", "16+", 8.3,
                                  7.00, false, "", "https://example-retailer.com/villainess-hourglass"));
        manhwaList.add(new Manhwa("The Devil's Boy", "Kim Hyeon-Jun", "Supernatural", "Seinen", "16+", 8.1,
                                  6.80, false, "", "https://example-retailer.com/the-devils-boy"));
        manhwaList.add(new Manhwa("Heavenly Demon Instructor", "Park Ji-Hoon", "Martial Arts", "Shounen", "16+", 8.6,
                                  7.25, true, "HDI25", "https://example-retailer.com/heavenly-demon-instructor"));
        manhwaList.add(new Manhwa("The Legendary Moon's Return", "Seo Kyeong", "Fantasy", "Seinen", "16+", 8.5,
                                  7.00, false, "", "https://example-retailer.com/leg-moon-return"));
        manhwaList.add(new Manhwa("The Villainess Lives Twice", "Kim H.", "Drama", "Seinen", "16+", 8.0,
                                  6.50, false, "", "https://example-retailer.com/villainess-twice"));
        manhwaList.add(new Manhwa("My Dad Is Too Strong", "Han Jun", "Action", "Shounen", "16+", 8.4,
                                  6.99, true, "MDTS25", "https://example-retailer.com/my-dad-is-too-strong"));
        manhwaList.add(new Manhwa("The Death Mage Who Doesn't Want a Fourth Time", "Ryu", "Fantasy", "Seinen", "16+", 8.2,
                                  6.90, false, "", "https://example-retailer.com/death-mage"));
        manhwaList.add(new Manhwa("Revenant Architect", "Cho Nam", "Action", "Seinen", "16+", 8.7,
                                  7.40, false, "", "https://example-retailer.com/revenant-architect"));
        manhwaList.add(new Manhwa("Sword of the Emperor", "Kang Min", "Martial Arts", "Seinen", "16+", 8.8,
                                  7.60, true, "SOE25", "https://example-retailer.com/sword-of-the-emperor"));
        manhwaList.add(new Manhwa("The Lone Necromancer", "Park Ji", "Dark Fantasy", "Seinen", "16+", 8.6
                                  , 7.10, false, "", "https://example-retailer.com/lone-necromancer"));
        manhwaList.add(new Manhwa("Kubera", "Currygom", "Fantasy", "Seinen", "16+", 8.7,
                                  7.50, false, "", "https://www.webtoons.com/en/fantasy/kubera"));
        manhwaList.add(new Manhwa("Painter of the Night", "Heewon", "Historical/Drama", "Seinen", "16+", 8.9,
                                  7.80, false, "", "https://example-retailer.com/painter-of-the-night"));
        manhwaList.add(new Manhwa("Who Made Me a Princess", "Plutus", "Romance/Fantasy", "Seinen", "16+", 9.1,
                                  8.10, false, "", "https://www.webtoons.com/en/fantasy/who-made-me-a-princess"));
        manhwaList.add(new Manhwa("Your Throne (I Want to Be You, Just For A Day)", "SAM", "Drama/Romance", "Seinen", "16+", 8.8,
                                  7.40, false, "", "https://example-retailer.com/your-throne"));
        manhwaList.add(new Manhwa("The Reason Why Raeliana Ended Up at the Duke's Mansion", "Milcha", "Romance", "Seinen", "16+", 8.6,
                                  7.20, false, "", "https://example-retailer.com/raeliana"));
        manhwaList.add(new Manhwa("The Duchess' 50 Tea Recipes", "Ant Studio", "Slice of Life/Romance", "Seinen", "16+", 8.4,
                                  6.99, false, "", "https://example-retailer.com/50-tea-recipes"));
        manhwaList.add(new Manhwa("Light and Shadow", "Ryou Maekawa", "Romance/Drama", "Seinen", "16+", 8.5,
                                  7.00, false, "", "https://example-retailer.com/light-and-shadow"));
        manhwaList.add(new Manhwa("The Abandoned Empress", "Yuna", "Drama/Fantasy", "Seinen", "16+", 8.9,
                                  7.60, true, "AE25", "https://example-retailer.com/abandoned-empress"));
        manhwaList.add(new Manhwa("Lady to Queen", "Mok Young", "Romance", "Seinen", "16+", 8.2,
                                  6.80, false, "", "https://example-retailer.com/lady-to-queen"));
        manhwaList.add(new Manhwa("I Became the Villain's Mother", "Kim Jae-heon", "Drama", "Seinen", "16+", 8.3,
                                  6.99, false, "", "https://example-retailer.com/villains-mother"));
        manhwaList.add(new Manhwa("The Monster Duchess and Contract Princess", "Rok Kat", "Fantasy/Romance", "Seinen", "16+", 8.6,
                                  7.30, true, "MDCP25", "https://example-retailer.com/monster-duchess"));
        manhwaList.add(new Manhwa("The Villainess Wants a Divorce", "Kim S", "Romance/Drama", "Seinen", "16+", 8.1,
                                  6.75, false, "", "https://example-retailer.com/villainess-divorce"));
        manhwaList.add(new Manhwa("A Stepmother's Märchen", "Sung Hee", "Romance/Fantasy", "Seinen", "16+", 8.0,
                                  6.50, false, "", "https://example-retailer.com/stepmother-marchen"));
        manhwaList.add(new Manhwa("My Three Tyrant Brothers", "Moomin", "Comedy/Drama", "Seinen", "16+", 7.9,
                                  6.40, false, "", "https://example-retailer.com/my-three-tyrant-brothers"));
        manhwaList.add(new Manhwa("The Duchess with an Empty Soul", "Yoon H.", "Drama", "Seinen", "16+", 8.2,
                                  6.95, false, "", "https://example-retailer.com/duchess-empty-soul"));
        manhwaList.add(new Manhwa("A Stepmother’s Romance", "Lee Eun", "Romance", "Seinen", "16+", 7.8,
                                  6.30, false, "", "https://example-retailer.com/stepmother-romance"));
        manhwaList.add(new Manhwa("The Justice of Villainous Woman", "Han R.", "Drama", "Seinen", "16+", 8.0,
                                  6.85, false, "", "https://example-retailer.com/justice-villainous-woman"));
        manhwaList.add(new Manhwa("The Princess Imprints a Traitor", "Park Min", "Historical/Drama", "Seinen", "16+", 8.1,
                                  7.10, false, "", "https://example-retailer.com/princess-imprints-traitor"));
        manhwaList.add(new Manhwa("Yumi's Cells", "Donggeon Lee", "Slice of Life/Romance", "Seinen", "16+", 8.4,
                                  6.99, false, "", "https://www.webtoons.com/en/drama/yumis-cells"));
        manhwaList.add(new Manhwa("My Dear Cold-Blooded King", "Sookie", "Romance/Drama", "Seinen", "16+", 8.2,
                                  6.50, false, "", "https://example-retailer.com/my-dear-cold-blooded-king"));
        manhwaList.add(new Manhwa("Annarasumanara", "Ha Il-Kwon", "Fantasy/Drama", "Seinen", "16+", 8.6
                                  , 7.20, false, "", "https://example-retailer.com/annarasumanara"));
        manhwaList.add(new Manhwa("The Friendly Winter", "Hee Won", "Romance/Drama", "Seinen", "16+", 7.9,
                                  6.20, false, "", "https://example-retailer.com/friendly-winter"));
        manhwaList.add(new Manhwa("Distant Sky", "Han Rai", "Drama", "Seinen", "16+", 7.8,
                                  6.10, false, "", "https://example-retailer.com/distant-sky"));
        manhwaList.add(new Manhwa("Ares", "Ryu Geum-chel", "Action", "Seinen", "16+", 8.1,
                                  6.80, false, "", "https://example-retailer.com/ares"));
        manhwaList.add(new Manhwa("The Scholar and the Alchemist", "Min Joon", "Fantasy", "Seinen", "16+", 8.0,
                                  6.70, true, "SCH25", "https://example-retailer.com/scholar-alchemist"));
        manhwaList.add(new Manhwa("The Masterful Cat Is Depressed Again Today", "Pochi", "Comedy/Fantasy", "Seinen", "16+", 7.7,
                                  6.00, false, "", "https://example-retailer.com/masterful-cat"));
        manhwaList.add(new Manhwa("The Friendly Necromancer", "Kang H.", "Dark Fantasy", "Seinen", "16+", 8.3,
                                  7.00, false, "", "https://example-retailer.com/friendly-necromancer"));
        manhwaList.add(new Manhwa("The Legendary Florist", "Cho Hye", "Romance/Fantasy", "Seinen", "16+", 7.9,
                                  6.50, false, "", "https://example-retailer.com/legendary-florist"));
        manhwaList.add(new Manhwa("Black Survival", "Kim Doo-Sik", "Action/Sci-Fi", "Seinen", "16+", 8.0,
                                  6.90, false, "", "https://example-retailer.com/black-survival"));
        manhwaList.add(new Manhwa("The Grand Duke's Daughter", "Han Seol", "Drama/Romance", "Seinen", "16+", 8.2,
                                  6.95, true, "GDD25", "https://example-retailer.com/grand-dukes-daughter"));
        manhwaList.add(new Manhwa("I Became the Chef of the Ruined Restaurant", "Lee Ah", "Slice of Life/Comedy", "Seinen", "16+", 7.6,
                                  5.90, false, "", "https://example-retailer.com/chef-ruined-restaurant"));
        manhwaList.add(new Manhwa("The Outcast", "Min H.", "Supernatural/Thriller", "Seinen", "16+", 8.1,
                                  7.10, false, "", "https://example-retailer.com/the-outcast"));
        manhwaList.add(new Manhwa("Paladin's Legacy", "Seo-Kyeong", "Fantasy/Action", "Seinen", "16+", 8.4,
                                  7.30, true, "PL25", "https://example-retailer.com/paladins-legacy"));
        manhwaList.add(new Manhwa("The Apothecary's Gate", "Kim Y.", "Historical/Fantasy", "Seinen", "16+", 7.8,
                                  6.30, false, "", "https://example-retailer.com/apothecarys-gate"));
        manhwaList.add(new Manhwa("The Necromancer's Bride", "Park S.", "Dark Romance", "Seinen", "16+", 8.0,
                                  6.75, false, "", "https://example-retailer.com/necromancers-bride"));
        manhwaList.add(new Manhwa("The Wandering Inn (Korean adaptation)", "Pirate Labs", "Fantasy/Adventure", "Seinen", "16+", 8.5,
                                  7.50, true, "WI25", "https://example-retailer.com/wandering-inn"));
        manhwaList.add(new Manhwa("The Iron Mage", "Choi H.", "Action/Fantasy", "Seinen", "16+", 8.2,
                                  7.00, false, "", "https://example-retailer.com/iron-mage"));
        manhwaList.add(new Manhwa("The Blacksmith's Daughter", "Han Jae", "Historical/Drama", "Seinen", "16+", 7.7,
                                  6.20, false, "", "https://example-retailer.com/blacksmith-daughter"));
        manhwaList.add(new Manhwa("The Forgotten Regent", "Lee G.", "Political/Drama", "Seinen", "16+", 8.1,
                                  7.05, false, "", "https://example-retailer.com/forgotten-regent"));
        manhwaList.add(new Manhwa("The Silent Knight", "Yoon Tae", "Action/Fantasy", "Seinen", "16+", 8.3,
                                  7.10, false, "", "https://example-retailer.com/silent-knight"));
        manhwaList.add(new Manhwa("Echoes of the Abyss", "Nam Soo", "Dark Fantasy/Thriller", "Seinen", "16+", 8.0,
                          6.85, true, "ECHO25", "https://example-retailer.com/echoes-abyss"));
        manhwaList.add(new Manhwa("Chains of Eternity", "Baek Jae", "Supernatural/Action", "Seinen", "16+", 8.1,
                          6.95, false, "", "https://example-retailer.com/chains-eternity"));
        manhwaList.add(new Manhwa("Crimson Waltz", "Kang Mi", "Romance/Mystery", "Seinen", "16+", 8.2,
                          7.00, true, "CRIMSON25", "https://example-retailer.com/crimson-waltz"));
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
