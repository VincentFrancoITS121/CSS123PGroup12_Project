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
                "A reincarnated king in a magical world seeks redemption and strength to protect his loved ones from powerful enemies."));
        manhwaList.add(new Manhwa("Noblesse", "Son Jeho", "Supernatural", "Shounen", "16+", 9.0,
                "An ancient noble awakens in the modern world to protect humanity and his friends from supernatural threats."));
        manhwaList.add(new Manhwa("God of High School", "Yongje Park", "Action", "Shounen", "16+", 9.1,
                "Teen fighters compete in an epic martial arts tournament that hides divine secrets and world-altering powers."));
        manhwaList.add(new Manhwa("Sweet Home", "Carnby Kim", "Horror", "Seinen", "16+", 9.3,
                "A reclusive teenager struggles to survive in an apartment complex overrun by terrifying monsters born from human desires."));
        manhwaList.add(new Manhwa("Bastard", "Carnby Kim", "Thriller", "Seinen", "16+", 9.5,
                "A high school boy lives in fear as he hides the horrifying truth that his father is a serial killer."));
        manhwaList.add(new Manhwa("Leviathan", "Ryuho Son", "Sci-Fi", "Seinen", "16+", 8.9, 7.70,
                "In a flooded post-apocalyptic world, a boy and his sister fight to survive against pirates, sea monsters, and the cruelty of humanity."));
        manhwaList.add(new Manhwa("Get Schooled", "Chongtak", "Drama", "Seinen", "16+", 9.0, 7.99,
                "A strict government enforcer uses brutal methods to reform violent and corrupt schools across the country."));
        manhwaList.add(new Manhwa("Jungle Juice", "Hyung Eun", "Action", "Shounen", "16+", 8.7,
                "A college student who hides his insect wings gets thrust into a hidden world of human-insect hybrids after his secret is exposed."));
        manhwaList.add(new Manhwa("DICE: The Cube That Changes Everything", "Yun Hyunseok", "Fantasy", "Shounen", "16+", 8.5,
                "A bullied student’s life changes when mysterious cubes grant players the ability to alter their fate."));
        manhwaList.add(new Manhwa("Hardcore Leveling Warrior", "Sehun Kim", "Fantasy", "Shounen", "16+", 8.8,
                "A once-top gamer seeks redemption after losing everything in a deadly virtual world that blurs the line with reality."));
        manhwaList.add(new Manhwa("Wind Breaker", "Jo Yongseok", "Sports", "Shounen", "16+", 9.2,
                "A high school biker gang protects their town and friends while learning about loyalty, strength, and self-discovery."));
        manhwaList.add(new Manhwa("Viral Hit", "Taejun Park", "Action", "Seinen", "16+", 9.1,
                "A bullied teen becomes a viral sensation by fighting back and exposing corruption through live-streamed street battles."));
        manhwaList.add(new Manhwa("Questism", "Taejun Park", "Action", "Seinen", "16+", 8.9,
                "A delinquent’s life turns into an RPG where completing bizarre quests grants him powers and changes his fate."));
        manhwaList.add(new Manhwa("Nano Machine", "Han Joong Wueol Ya", "Action", "Shounen", "16+", 9.0,
                "A neglected prince’s life transforms when nanotechnology from the future grants him unparalleled martial power."));
        manhwaList.add(new Manhwa("Return of the Mount Hua Sect", "Biga", "Martial Arts", "Shounen", "16+", 9.3,
                "The last surviving member of a fallen martial sect reincarnates and vows to restore its former glory."));
        manhwaList.add(new Manhwa("The Breaker: Eternal Force", "Jeon Geuk-jin", "Martial Arts", "Seinen", "16+", 9.1,
                "A bullied teenager’s life changes when he meets a mysterious martial artist who drags him into the hidden world of Murim."));
        manhwaList.add(new Manhwa("Mercenary Enrollment", "YC", "Action", "Seinen", "16+", 9.4,
                "A teenage ex-mercenary tries to live a normal high school life while using his deadly skills to protect those he cares about."));
        manhwaList.add(new Manhwa("Second Life Ranker", "Sadoyeon", "Fantasy", "Seinen", "16+", 9.2,
                "A man climbs a deadly tower seeking revenge for his twin brother, armed with his brother’s memories and power."));
        manhwaList.add(new Manhwa("Dungeon Reset", "Ant Studio", "Adventure", "Shounen", "16+", 8.8,
                "After a dungeon glitch traps him in a deadly loop, a player uses creativity and persistence to turn his endless resets into strength."));
        manhwaList.add(new Manhwa("A Returner’s Magic Should Be Special", "Sun-Wook Jeon", "Fantasy", "Shounen", "16+", 9.1,
                "A powerful magician goes back in time to prevent humanity’s destruction and train his friends for the coming apocalypse."));
        manhwaList.add(new Manhwa("Trash of the Count’s Family", "Yoo Ryeo Han", "Fantasy", "Seinen", "16+", 9.0,
                "A powerful magician goes back in time to prevent humanity’s destruction and train his friends for the coming apocalypse."));
        manhwaList.add(new Manhwa("Player Who Can’t Level Up", "Garam Teo", "Fantasy", "Seinen", "16+", 8.9,
                "A player stuck at level one discovers unique abilities that let him grow stronger in ways no one else can."));
        manhwaList.add(new Manhwa("Reaper of the Drifting Moon", "Mok-In", "Action", "Seinen", "16+", 9.3,
               "A man raised as a tool of assassination escapes his captors and embarks on a path of vengeance in a dark martial world."));
        manhwaList.add(new Manhwa("Infinite Level Up in Murim", "Gom-Guk", "Martial Arts", "Shounen", "16+", 8.8,
               "A man reincarnated into a martial world gains a game-like system that lets him level up endlessly beyond human limits."));
        manhwaList.add(new Manhwa("The Legend of the Northern Blade", "Hae Min", "Martial Arts", "Seinen", "16+", 9.2,
               "The last heir of a fallen sect trains in isolation to reclaim his father’s legacy and restore the Northern Heavenly Sect."));
        manhwaList.add(new Manhwa("SSS-Class Revival Hunter", "Shin Noah", "Fantasy", "Seinen", "16+", 9.1,
               "A weak hunter gains the ability to return to the past upon death, using it to climb the ranks and outsmart fate itself."));
        manhwaList.add(new Manhwa("Survival Story of a Sword King in a Fantasy World", "Kwon Sun Kyu", "Fantasy", "Seinen", "16+", 9.0,
               "A man trapped in a harsh game-like world becomes overpowered after countless deaths and survival trials."));
        manhwaList.add(new Manhwa("Tomb Raider King", "San.G", "Action", "Seinen", "16+", 8.9,
               "A relic hunter races against gods and rivals to seize powerful artifacts and rewrite his destiny."));
        manhwaList.add(new Manhwa("Max Level Returner", "Jang Sung-Lak", "Action", "Shounen", "16+", 8.7,
               "The first player to clear a deadly game returns to the real world, only to face its reemergence and fight to protect humanity again."));
        manhwaList.add(new Manhwa("Overgeared", "Park Saenal", "Fantasy", "Seinen", "16+", 9.0,
               "A struggling gamer’s luck turns when he obtains a legendary class and crafts his way to power and respect in a vast virtual world."));
        manhwaList.add(new Manhwa("The Live", "Ant Studio", "Drama", "Seinen", "16+", 8.9,
               "After losing everything, a man is given a second chance to change the past and save his family by clearing a deadly game-like world."));
        manhwaList.add(new Manhwa("Leveling With the Gods", "Ohyeon", "Action", "Seinen", "16+", 9.3,
               "A warrior who failed to defeat the gods climbs back from the abyss to challenge them again through relentless battles and growth."));
        manhwaList.add(new Manhwa("The Gamer", "Sung San-Young", "Fantasy", "Shounen", "16+", 8.6,
               "A high school student gains the ability to live life like a video game, complete with levels, skills, and quests."));
        manhwaList.add(new Manhwa("Gosu", "Ji-Min Park", "Martial Arts", "Seinen", "16+", 9.1,
               "The disciple of a slain martial arts master sets out on a comedic yet action-packed journey of revenge in the Murim world."));
        manhwaList.add(new Manhwa("Black Haze", "Jin-Hwan Park", "Fantasy", "Seinen", "16+", 8.7,
               "A powerful magician hiding his identity becomes a student bodyguard, uncovering secrets about his past and the magic world."));
        manhwaList.add(new Manhwa("The God of Blackfield", "Han Ji-Hoon", "Action", "Seinen", "16+", 8.9,
               "A soldier killed in battle mysteriously awakens in another body and seeks vengeance on those who betrayed him."));
        manhwaList.add(new Manhwa("The Boxer", "JH", "Sports", "Seinen", "16+", 8.8,
               "A quiet prodigy with monstrous talent enters the boxing world, exploring the meaning of strength, pain, and purpose."));
        manhwaList.add(new Manhwa("The Scholar's Reincarnation", "Cheongcho", "Fantasy", "Seinen", "16+", 8.5,
               "A warlord reborn as a scholar’s son seeks redemption through peace and wisdom instead of violence."));
        manhwaList.add(new Manhwa("The Legendary Moonlight Sculptor", "Nam Heesung", "Fantasy", "Shounen", "16+", 9.0,
               "A broke gamer enters a virtual reality game to earn money, only to become a legend through his creativity and hard work."));
        manhwaList.add(new Manhwa("A Capable Maid", "Hyeok", "Drama", "Seinen", "16+", 8.2,
               "A once-powerless servant uses her hidden intelligence and resourcefulness to rise above her station and protect her master."));
        manhwaList.add(new Manhwa("The Villainess Reverses the Hourglass", "Satoshi", "Drama", "Seinen", "16+", 8.3,
               "After being executed unjustly, a noblewoman is sent back in time with an hourglass that lets her rewrite her fate."));
        manhwaList.add(new Manhwa("The Devil's Boy", "Kim Hyeon-Jun", "Supernatural", "Seinen", "16+", 8.1,
               "A bullied teen gains demonic powers through a dangerous pact and struggles between revenge and his humanity."));
        manhwaList.add(new Manhwa("Heavenly Demon Instructor", "Park Ji-Hoon", "Martial Arts", "Shounen", "16+", 8.6,
               "A boy gains the guidance of a powerful martial spirit who trains him to survive and grow stronger in a harsh world."));
        manhwaList.add(new Manhwa("The Legendary Moon's Return", "Seo Kyeong", "Fantasy", "Seinen", "16+", 8.5,
               "A once-feared martial artist returns from exile to reclaim his place and restore his sect’s honor."));
        manhwaList.add(new Manhwa("The Villainess Lives Twice", "Kim H.", "Drama", "Seinen", "16+", 8.0,
               "Betrayed and executed in her first life, a cunning noblewoman reincarnates to outmaneuver her enemies and seize control of her destiny."));
        manhwaList.add(new Manhwa("My Dad Is Too Strong", "Han Jun", "Action", "Shounen", "16+", 8.4,
               "A former special agent trying to live a peaceful life struggles to hide his overwhelming strength from his adorable adopted daughter."));
        manhwaList.add(new Manhwa("The Death Mage Who Doesn't Want a Fourth Time", "Ryu", "Fantasy", "Seinen", "16+", 8.2,
                "After countless reincarnations filled with betrayal, a boy born in a new world seeks peace while wielding death magic."));
        manhwaList.add(new Manhwa("Revenant Architect", "Cho Nam", "Action", "Seinen", "16+", 8.7,
                "A brilliant architect returns from death to rebuild a corrupted city and expose the truth behind his murder."));
        manhwaList.add(new Manhwa("Sword of the Emperor", "Kang Min", "Martial Arts", "Seinen", "16+", 8.8,
                "A loyal warrior rises through blood and battle to become the blade that defends the empire and defines its destiny."));
        manhwaList.add(new Manhwa("The Lone Necromancer", "Park Ji", "Dark Fantasy", "Seinen", "16+", 8.6
                "A university student awakens necromantic powers in a post-apocalyptic world where the dead and living clash for survival."));
        manhwaList.add(new Manhwa("Kubera", "Currygom", "Fantasy", "Seinen", "16+", 8.7,
                "A young girl named Kubera embarks on a perilous journey for revenge, entwined with gods, destruction, and destiny itself."));
        manhwaList.add(new Manhwa("Painter of the Night", "Heewon", "Historical/Drama", "Seinen", "16+", 8.9,
                "In Joseon-era Korea, a young artist’s life is upended when he’s drawn into a dangerous relationship with a powerful nobleman."));
        manhwaList.add(new Manhwa("Who Made Me a Princess", "Plutus", "Romance/Fantasy", "Seinen", "16+", 9.1,
                "A modern girl reincarnates as a doomed princess in a fairy-tale empire and must win her cold father’s love to survive."));
        manhwaList.add(new Manhwa("Your Throne (I Want to Be You, Just For A Day)", "SAM", "Drama/Romance", "Seinen", "16+", 8.8,
                "A cunning noblewoman swaps bodies with her rival to seize power and uncover the truth behind betrayal."));
        manhwaList.add(new Manhwa("The Reason Why Raeliana Ended Up at the Duke's Mansion", "Milcha", "Romance", "Seinen", "16+", 8.6,
                "A woman reborn into a novel strikes a deal with a cold duke to avoid her scripted death."));
        manhwaList.add(new Manhwa("The Duchess' 50 Tea Recipes", "Ant Studio", "Slice of Life/Romance", "Seinen", "16+", 8.4,
                "A modern tea expert reincarnates as a duchess and revives her household’s prestige through her exquisite brews."));
        manhwaList.add(new Manhwa("Light and Shadow", "Ryou Maekawa", "Romance/Drama", "Seinen", "16+", 8.5,
                "A servant girl’s hidden identity and fierce determination transform her into a powerful duchess."));
        manhwaList.add(new Manhwa("The Abandoned Empress", "Yuna", "Drama/Fantasy", "Seinen", "16+", 8.9,
                "Betrayed by fate, an empress who loses everything finds herself reborn with the chance to rewrite her destiny."));
        manhwaList.add(new Manhwa("Lady to Queen", "Mok Young", "Romance", "Seinen", "16+", 8.2,
                "A fallen noblewoman becomes queen by playing the political game that once destroyed her family."));
        manhwaList.add(new Manhwa("I Became the Villain's Mother", "Kim Jae-heon", "Drama", "Seinen", "16+", 8.3,
                "A woman reincarnates into a novel as the mother of its villain and chooses to love and protect him instead of fearing him."));
        manhwaList.add(new Manhwa("The Monster Duchess and Contract Princess", "Rok Kat", "Fantasy/Romance", "Seinen", "16+", 8.6,
                "hA lonely duchess adopts a cursed child, forming an unbreakable bond that defies prejudice and prophecy."));
        manhwaList.add(new Manhwa("The Villainess Wants a Divorce", "Kim S", "Romance/Drama", "Seinen", "16+", 8.1,
                "A lonely duchess adopts a cursed child, forming an unbreakable bond that defies prejudice and prophecy."));
        manhwaList.add(new Manhwa("A Stepmother's Märchen", "Sung Hee", "Romance/Fantasy", "Seinen", "16+", 8.0,
                "A devoted stepmother struggles to reconnect with her stepchildren and rewrite the tragic ending of their fairy-tale family."));
        manhwaList.add(new Manhwa("My Three Tyrant Brothers", "Moomin", "Comedy/Drama", "Seinen", "16+", 7.9,
                "A reincarnated girl tries to survive and tame her three overprotective royal brothers in a twisted palace drama."));
        manhwaList.add(new Manhwa("The Duchess with an Empty Soul", "Yoon H.", "Drama", "Seinen", "16+", 8.2,
                "A woman trapped in a loveless marriage learns to rediscover herself and find meaning beyond her hollow title."));
        manhwaList.add(new Manhwa("A Stepmother’s Romance", "Lee Eun", "Romance", "Seinen", "16+", 7.8,
                "A widowed stepmother’s life changes when a kind nobleman offers her a second chance at love and happiness."));
        manhwaList.add(new Manhwa("The Justice of Villainous Woman", "Han R.", "Drama", "Seinen", "16+", 8.0,
                "Framed and executed, a noblewoman is granted another life to uncover the truth and change her cruel fate."));
        manhwaList.add(new Manhwa("The Princess Imprints a Traitor", "Park Min", "Historical/Drama", "Seinen", "16+", 8.1,
                "A fallen princess saves a condemned knight and marks him as her own to reclaim her power."));
        manhwaList.add(new Manhwa("Yumi's Cells", "Donggeon Lee", "Slice of Life/Romance", "Seinen", "16+", 8.4,
                "Yumi’s story unfolds through the voices of her emotions, showing the funny and heartfelt complexities of everyday life and love."));
        manhwaList.add(new Manhwa("My Dear Cold-Blooded King", "Sookie", "Romance/Drama", "Seinen", "16+", 8.2,
                "A merchant’s daughter gets entangled with a mysterious assassin king and uncovers dangerous secrets of his empire."));
        manhwaList.add(new Manhwa("Annarasumanara", "Ha Il-Kwon", "Fantasy/Drama", "Seinen", "16+", 8.6
                "A mysterious magician helps a struggling girl rediscover her lost sense of wonder and belief in magic."));
        manhwaList.add(new Manhwa("The Friendly Winter", "Hee Won", "Romance/Drama", "Seinen", "16+", 7.9,
                 "Two lonely souls—an ill young man and a girl with a child’s heart—form a tender bond that heals their emotional wounds."));
        manhwaList.add(new Manhwa("Distant Sky", "Han Rai", "Drama", "Seinen", "16+", 7.8,
                 "Two students awaken in a ruined, monster-filled Seoul and struggle to uncover the mystery behind the city’s destruction."));
        manhwaList.add(new Manhwa("Ares", "Ryu Geum-chel", "Action", "Seinen", "16+", 8.1,
                 "In a brutal war-torn land, a young swordsman joins an elite mercenary group in search of vengeance and honor."));
        manhwaList.add(new Manhwa("The Scholar and the Alchemist", "Min Joon", "Fantasy", "Seinen", "16+", 8.0,
                 "A curious scholar and a secretive alchemist join forces to uncover ancient mysteries that challenge science and faith."));
        manhwaList.add(new Manhwa("The Masterful Cat Is Depressed Again Today", "Pochi", "Comedy/Fantasy", "Seinen", "16+", 7.7,
                 "A woman’s life is hilariously managed by her giant, responsible cat who acts more like a human than she does."));
        manhwaList.add(new Manhwa("The Friendly Necromancer", "Kang H.", "Dark Fantasy", "Seinen", "16+", 8.3,
                 "A kind-hearted necromancer uses his control over the dead not for power, but to bring peace to restless souls."));
        manhwaList.add(new Manhwa("The Legendary Florist", "Cho Hye", "Romance/Fantasy", "Seinen", "16+", 7.9,
                 "A once-ordinary man builds a legend through flowers, crafting beauty and meaning in a world obsessed with power."));
        manhwaList.add(new Manhwa("Black Survival", "Kim Doo-Sik", "Action/Sci-Fi", "Seinen", "16+", 8.0,
                 "Stranded on a deadly island, players must fight, craft, and strategize to be the last one standing in a brutal survival game."));
        manhwaList.add(new Manhwa("The Grand Duke's Daughter", "Han Seol", "Drama/Romance", "Seinen", "16+", 8.2,
                 "Reborn as the daughter of a feared noble, a clever young woman uses wit and charm to change her tragic destiny."));
        manhwaList.add(new Manhwa("I Became the Chef of the Ruined Restaurant", "Lee Ah", "Slice of Life/Comedy", "Seinen", "16+", 7.6,
                 "After a sudden downfall, a passionate cook rebuilds a once-famous restaurant using creativity, grit, and heartwarming dishes."));
        manhwaList.add(new Manhwa("The Outcast", "Min H.", "Supernatural/Thriller", "Seinen", "16+", 8.1,
                 "Branded as cursed, a lone survivor wields forbidden powers to defy the world that cast him aside."));
        manhwaList.add(new Manhwa("Paladin's Legacy", "Seo-Kyeong", "Fantasy/Action", "Seinen", "16+", 8.4,
                 "A fallen paladin journeys to restore his faith and protect the innocent in a world consumed by corruption."));
        manhwaList.add(new Manhwa("The Apothecary's Gate", "Kim Y.", "Historical/Fantasy", "Seinen", "16+", 7.8,
                 "An outcast healer discovers a hidden gateway to another realm and must balance alchemy, politics, and forbidden love."));
        manhwaList.add(new Manhwa("The Necromancer's Bride", "Park S.", "Dark Romance", "Seinen", "16+", 8.0,
                 "A woman betrothed to a feared necromancer uncovers the tragic reasons behind his dark power and their fated connection."));
        manhwaList.add(new Manhwa("The Wandering Inn (Korean adaptation)", "Pirate Labs", "Fantasy/Adventure", "Seinen", "16+", 8.5,
                 "Combining magic with machinery, a disgraced sorcerer creates powerful inventions to challenge both kings and gods."));
        manhwaList.add(new Manhwa("The Iron Mage", "Choi H.", "Action/Fantasy", "Seinen", "16+", 8.2,
                 "Combining magic with machinery, a disgraced sorcerer creates powerful inventions to challenge both kings and gods."));
        manhwaList.add(new Manhwa("The Blacksmith's Daughter", "Han Jae", "Historical/Drama", "Seinen", "16+", 7.7,
                 "Gifted with her late father’s forging skills, a humble girl crafts weapons that could change the fate of nations."));
        manhwaList.add(new Manhwa("The Forgotten Regent", "Lee G.", "Political/Drama", "Seinen", "16+", 8.1,
                 "Awakened after centuries of slumber, a deposed ruler fights to reclaim his throne in a world that has erased his name."));
        manhwaList.add(new Manhwa("The Silent Knight", "Yoon Tae", "Action/Fantasy", "Seinen", "16+", 8.3,
                 "A voiceless warrior protects his kingdom from the shadows, concealing a tragic past and an unspoken vow."));
        manhwaList.add(new Manhwa("Echoes of the Abyss", "Nam Soo", "Dark Fantasy/Thriller", "Seinen", "16+", 8.0,
                 "A young mage ventures into a forbidden abyss where memories, monsters, and forgotten gods whisper the truth of creation."));
        manhwaList.add(new Manhwa("Chains of Eternity", "Baek Jae", "Supernatural/Action", "Seinen", "16+", 8.1,
                 "In a kingdom ruled by blood and deceit, a noblewoman dances between love and betrayal to uncover the secret behind a cursed royal lineage."));
        manhwaList.add(new Manhwa("Crimson Waltz", "Kang Mi", "Romance/Mystery", "Seinen", "16+", 8.2,
                 "In a kingdom ruled by blood and deceit, a noblewoman dances between love and betrayal to uncover the secret behind a cursed royal lineage."));
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

