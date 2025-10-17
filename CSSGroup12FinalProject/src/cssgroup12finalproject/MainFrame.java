package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private HomePanel homePanel;
    private RecommendationPanel recommendationPanel;
    private ShoppingHubPanel shoppingHubPanel;
    
    // --- UI CONSTANTS for professional look: Material Design Inspired Palette ---
    private static final Color PRIMARY_COLOR = new Color(63, 81, 181); // Indigo 500
    private static final Color ACCENT_COLOR = new Color(255, 112, 67); // Deep Orange 400
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light Gray
    
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

        // Set a dark background for the frame
        getContentPane().setBackground(new Color(30, 30, 45)); // Dark Navy/Black

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(30, 30, 45)); // Dark Navy/Black
        add(mainPanel);
    }
    
    private void setupPanels() {
        homePanel = new HomePanel(this);
        recommendationPanel = new RecommendationPanel(this);
        shoppingHubPanel = new ShoppingHubPanel(this);
        
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(recommendationPanel, "RECOMMENDATIONS");
        mainPanel.add(shoppingHubPanel, "SHOPPING_HUB");
        
        showPanel("HOME");
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        
        // Customizing Menu appearance for professionalism
        Font menuFont = new Font("Arial", Font.BOLD, 14);
        
        // Genre Menu
        JMenu genreMenu = new JMenu("Genres");
        genreMenu.setFont(menuFont);
        
        JMenuItem actionItem = new JMenuItem("Action");
        JMenuItem romanceItem = new JMenuItem("Romance");
        JMenuItem fantasyItem = new JMenuItem("Fantasy");
        
        actionItem.addActionListener(e -> applyFilters("Action", null, null));
        romanceItem.addActionListener(e -> applyFilters("Romance", null, null));
        fantasyItem.addActionListener(e -> applyFilters("Fantasy", null, null));
        
        genreMenu.add(actionItem);
        genreMenu.add(romanceItem);
        genreMenu.add(fantasyItem);
        
        // Demographic Menu
        JMenu demoMenu = new JMenu("Demographics");
        demoMenu.setFont(menuFont);
        
        JMenuItem shounenItem = new JMenuItem("Shounen");
        JMenuItem shoujoItem = new JMenuItem("Shoujo");
        
        shounenItem.addActionListener(e -> applyFilters(null, "Shounen", null));
        shoujoItem.addActionListener(e -> applyFilters(null, "Shoujo", null));
        
        demoMenu.add(shounenItem);
        demoMenu.add(shoujoItem);
        
        // Random Recommendation
        JMenu randomMenu = new JMenu("Surprise");
        randomMenu.setFont(menuFont);
        
        JMenuItem randomItem = new JMenuItem("Get a Random Manhwa");
        randomItem.addActionListener(e -> {
            recommendationPanel.showRandomRecommendation();
            showPanel("RECOMMENDATIONS");
        });
        randomMenu.add(randomItem);
        
        menuBar.add(genreMenu);
        menuBar.add(demoMenu);
        menuBar.add(randomMenu);
        
        setJMenuBar(menuBar);
        
        // Shopping Hub
        JMenu shopMenu = new JMenu("Shop");
        JMenuItem shopItem = new JMenuItem("Go to Shopping Hub");
        shopItem.addActionListener(e -> {
            showPanel("SHOPPING_HUB");
        });
        shopMenu.add(shopItem);
        
        menuBar.add(genreMenu);
        menuBar.add(demoMenu);
        menuBar.add(randomMenu);
        menuBar.add(shopMenu); // ADD TO MENU BAR
        
        setJMenuBar(menuBar);
    }
    
    public void showPanel(String panelName) {
        // Ensure home panel carousel is managed correctly on panel switch
        if (panelName.equals("HOME")) {
            homePanel.startCarousel();
        } else {
            homePanel.stopCarousel();
        }
        cardLayout.show(mainPanel, panelName);
    }
    
    public void applyFilters(String genre, String demographic, String ageRating) {
        recommendationPanel.updateRecommendations(genre, demographic, ageRating);
        showPanel("RECOMMENDATIONS");
    }

    // Helper methods to expose theme colors to other panels
    public static Color getPrimaryColor() { return PRIMARY_COLOR; }
    public static Color getAccentColor() { return ACCENT_COLOR; }
    public static Color getBackgroundColor() { return BACKGROUND_COLOR; }
}
