package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.Random; 

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
        setupKeyboardShortcuts();
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
        
        // Age Rating Menu
        JMenu ageMenu = new JMenu("Age Rating");
        ageMenu.setFont(menuFont);

        JMenuItem teen = new JMenuItem("13+");
        JMenuItem mature = new JMenuItem("16+");

        teen.addActionListener(e -> applyFilters(null, null, "13+"));
        mature.addActionListener(e -> applyFilters(null, null, "16+"));

        ageMenu.add(teen);
        ageMenu.add(mature);
        
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
        
        // Shopping Hub
        JMenu shopMenu = new JMenu("Shop");
        shopMenu.setFont(menuFont); // <-- FIX: Set the font here to make it bold
        JMenuItem shopItem = new JMenuItem("Go to Shopping Hub");
        shopItem.addActionListener(e -> {
            showPanel("SHOPPING_HUB");
        });
        shopMenu.add(shopItem);
        
        menuBar.add(genreMenu);
        menuBar.add(demoMenu);
        menuBar.add(ageMenu);   
        menuBar.add(randomMenu);
        menuBar.add(shopMenu); 
        
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

    // Helper method to convert a Color object to an HTML hex string (e.g., #FF00FF)
    public static String toHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Attempts to load an image from a URL, resizes it, and returns an ImageIcon.
     * If loading fails (e.g., URL invalid, network error), it returns a solid color placeholder.
     */
    public static ImageIcon toManhwaCoverIcon(String imagePath, int width, int height) {
        BufferedImage image = null;
        String fallbackTitle = "NO IMAGE";

        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                URL url = new URL(imagePath);
                // Set the fallback title to be the manhwa title part of the URL/Path for better debugging
                int lastSlash = imagePath.lastIndexOf('/');
                String tempTitle = imagePath.substring(lastSlash + 1).replace(".jpg", "").replace(".png", "");
                // Clean up title for display
                fallbackTitle = tempTitle.replace("_", " ");
                fallbackTitle = fallbackTitle.length() > 20 ? fallbackTitle.substring(0, 17) + "..." : fallbackTitle;
                
                image = ImageIO.read(url);
            } catch (IOException e) {
                AppLogger.error("Failed to load image from URL: " + imagePath, e);
            }
        }
        
        if (image != null) {
            // Resize the image to the target dimensions
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            // Fallback: Create a solid colored box with the title
            int colorSeed = (imagePath != null) ? imagePath.hashCode() : 0;
            Random random = new Random(colorSeed);
            
            // Generate a consistent but distinct color for the placeholder
            Color mockColor = new Color(
                50 + random.nextInt(150), 
                50 + random.nextInt(150), 
                50 + random.nextInt(150)
            );

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
            Graphics2D g2 = img.createGraphics();
            
            // Draw background
            g2.setColor(mockColor);
            g2.fillRect(0, 0, width, height);

            // Draw placeholder text
            g2.setColor(AppConfig.TEXT_LIGHT);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            // Center the text
            FontMetrics fm = g2.getFontMetrics();
            int x = (width - fm.stringWidth(fallbackTitle)) / 2;
            int y = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(fallbackTitle, x, y);

            g2.dispose();
            AppLogger.info("Using placeholder for: " + (imagePath != null ? imagePath : "null path"));
            
            return new ImageIcon(img);
        }
    }
    
    // Helper method to expose RecommendationPanel instance
    public RecommendationPanel getRecommendationPanel() {
        return recommendationPanel;
    }

    // Helper methods to expose theme colors to other panels
    public static Color getPrimaryColor() { return PRIMARY_COLOR; }
    public static Color getAccentColor() { return ACCENT_COLOR; }
    public static Color getBackgroundColor() { return BACKGROUND_COLOR; }
    
    private void setupKeyboardShortcuts() {
    KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_H: showPanel("HOME"); return true;
                    case KeyEvent.VK_R: showPanel("RECOMMENDATIONS"); return true;
                    case KeyEvent.VK_S: showPanel("SHOPPING_HUB"); return true;
                }
            }
            return false;
        });
    }
}