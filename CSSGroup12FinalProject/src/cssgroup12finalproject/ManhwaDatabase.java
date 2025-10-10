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

public class ManhwaDatabase {
    private static ManhwaDatabase instance;
    private List<Manhwa> manhwaList;
    
    private ManhwaDatabase() {
        manhwaList = new ArrayList<>();
        initializeSampleData();
    }
    
    public static ManhwaDatabase getInstance() {
        if (instance == null) {
            instance = new ManhwaDatabase();
        }
        return instance;
    }
    
    private void initializeSampleData() {
        // Vincent will add TONS more data here
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
    
    public void addManhwa(Manhwa manhwa) {
        manhwaList.add(manhwa);
    }
}