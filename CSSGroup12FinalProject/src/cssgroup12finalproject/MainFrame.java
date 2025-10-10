/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cssgroup12finalproject;

/**
 *
 * @author jojosh
 */
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private HomePanel homePanel;
    private RecommendationPanel recommendationPanel;
    
    public MainFrame() {
        initializeFrame();
        setupPanels();
        setupMenuBar();
    }
    
    private void initializeFrame() {
        setTitle("Manhwa-Link!");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }
    
    private void setupPanels() {
        homePanel = new HomePanel(this);
        recommendationPanel = new RecommendationPanel(this);
        
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(recommendationPanel, "RECOMMENDATIONS");
        
        showPanel("HOME");
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Genre Menu - Steven will expand this
        JMenu genreMenu = new JMenu("Genre");
        JMenuItem actionItem = new JMenuItem("Action");
        JMenuItem romanceItem = new JMenuItem("Romance");
        JMenuItem fantasyItem = new JMenuItem("Fantasy");
        
        actionItem.addActionListener(e -> applyFilters("Action", null, null));
        romanceItem.addActionListener(e -> applyFilters("Romance", null, null));
        fantasyItem.addActionListener(e -> applyFilters("Fantasy", null, null));
        
        genreMenu.add(actionItem);
        genreMenu.add(romanceItem);
        genreMenu.add(fantasyItem);
        
        // Demographic Menu - Steven will expand this
        JMenu demoMenu = new JMenu("Demographic");
        JMenuItem shounenItem = new JMenuItem("Shounen");
        JMenuItem shoujoItem = new JMenuItem("Shoujo");
        
        shounenItem.addActionListener(e -> applyFilters(null, "Shounen", null));
        shoujoItem.addActionListener(e -> applyFilters(null, "Shoujo", null));
        
        demoMenu.add(shounenItem);
        demoMenu.add(shoujoItem);
        
        // Random Recommendation
        JMenu randomMenu = new JMenu("Random");
        JMenuItem randomItem = new JMenuItem("Surprise Me!");
        randomItem.addActionListener(e -> {
            recommendationPanel.showRandomRecommendation();
            showPanel("RECOMMENDATIONS");
        });
        randomMenu.add(randomItem);
        
        menuBar.add(genreMenu);
        menuBar.add(demoMenu);
        menuBar.add(randomMenu);
        
        setJMenuBar(menuBar);
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public void applyFilters(String genre, String demographic, String ageRating) {
        recommendationPanel.updateRecommendations(genre, demographic, ageRating);
        showPanel("RECOMMENDATIONS");
    }
}
