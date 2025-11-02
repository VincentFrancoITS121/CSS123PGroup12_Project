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
import java.util.concurrent.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    private HomePanel homePanel;
    private RecommendationPanel recommendationPanel;
    private ShoppingHubPanel shoppingHubPanel;
    
    // Image cache to prevent reloading
    private static final ConcurrentHashMap<String, ImageIcon> imageCache = new ConcurrentHashMap<>();
    private static final ExecutorService imageLoader = Executors.newFixedThreadPool(4);
    
    private static final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private static final Color ACCENT_COLOR = new Color(255, 112, 67);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    
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
        getContentPane().setBackground(new Color(30, 30, 45));
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(30, 30, 45));
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
        Font menuFont = new Font("Arial", Font.BOLD, 14);
        
        JMenu genreMenu = new JMenu("Genres");
        genreMenu.setFont(menuFont);
        
        // Only genres that actually exist in the database
        String[] genres = {"Action", "Fantasy", "Romance", "Drama", "Horror", "Thriller", 
                          "Sci-Fi", "Supernatural", "Sports", "Adventure", "Martial Arts"};
        
        for (String genre : genres) {
            JMenuItem item = new JMenuItem(genre);
            item.addActionListener(e -> applyFilters(genre, null, null));
            genreMenu.add(item);
        }
        
        JMenu ageMenu = new JMenu("Age Rating");
        ageMenu.setFont(menuFont);
        JMenuItem teen = new JMenuItem("13+");
        JMenuItem mature = new JMenuItem("16+");
        teen.addActionListener(e -> applyFilters(null, null, "13+"));
        mature.addActionListener(e -> applyFilters(null, null, "16+"));
        ageMenu.add(teen);
        ageMenu.add(mature);
        
        JMenu demoMenu = new JMenu("Demographics");
        demoMenu.setFont(menuFont);
        
        String[] demographics = {"Shounen", "Shoujo", "Seinen", "Josei"};
        for (String demo : demographics) {
            JMenuItem item = new JMenuItem(demo);
            item.addActionListener(e -> applyFilters(null, demo, null));
            demoMenu.add(item);
        }
        
        JMenu randomMenu = new JMenu("Surprise");
        randomMenu.setFont(menuFont);
        JMenuItem randomItem = new JMenuItem("Get a Random Manhwa");
        randomItem.addActionListener(e -> {
            recommendationPanel.showRandomRecommendation();
            showPanel("RECOMMENDATIONS");
        });
        randomMenu.add(randomItem);
        
        JMenu shopMenu = new JMenu("Shop");
        shopMenu.setFont(menuFont);
        JMenuItem shopItem = new JMenuItem("Go to Shopping Hub");
        shopItem.addActionListener(e -> showPanel("SHOPPING_HUB"));
        shopMenu.add(shopItem);
        
        menuBar.add(genreMenu);
        menuBar.add(demoMenu);
        menuBar.add(ageMenu);
        menuBar.add(randomMenu);
        menuBar.add(shopMenu);
        
        setJMenuBar(menuBar);
    }
    
    public void showPanel(String panelName) {
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

    public static String toHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * FIXED: Async image loading with immediate placeholder return
     * This prevents UI freezing and makes the app responsive
     */
    public static ImageIcon toManhwaCoverIcon(String imagePath, int width, int height) {
        // Check cache first
        String cacheKey = imagePath + "_" + width + "x" + height;
        if (imageCache.containsKey(cacheKey)) {
            return imageCache.get(cacheKey);
        }
        
        // Return placeholder immediately
        ImageIcon placeholder = createPlaceholder(imagePath, width, height);
        
        // Load actual image asynchronously
        if (imagePath != null && !imagePath.isEmpty()) {
            imageLoader.submit(() -> {
                try {
                    URL url = new URL(imagePath);
                    BufferedImage image = ImageIO.read(url);
                    if (image != null) {
                        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        ImageIcon icon = new ImageIcon(scaledImage);
                        imageCache.put(cacheKey, icon);
                        AppLogger.info("Loaded image: " + imagePath);
                    }
                } catch (IOException e) {
                    AppLogger.error("Failed to load image: " + imagePath, e);
                    imageCache.put(cacheKey, placeholder);
                }
            });
        }
        
        return placeholder;
    }
    
    private static ImageIcon createPlaceholder(String imagePath, int width, int height) {
        String fallbackTitle = "LOADING...";
        
        if (imagePath != null && !imagePath.isEmpty()) {
            int lastSlash = imagePath.lastIndexOf('/');
            String tempTitle = imagePath.substring(lastSlash + 1)
                .replace(".jpg", "").replace(".png", "")
                .replace("_", " ");
            fallbackTitle = tempTitle.length() > 20 ? tempTitle.substring(0, 17) + "..." : tempTitle;
        }
        
        int colorSeed = (imagePath != null) ? imagePath.hashCode() : 0;
        Random random = new Random(colorSeed);
        Color mockColor = new Color(
            50 + random.nextInt(150), 
            50 + random.nextInt(150), 
            50 + random.nextInt(150)
        );

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(mockColor);
        g2.fillRect(0, 0, width, height);
        
        g2.setColor(AppConfig.TEXT_LIGHT);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(fallbackTitle)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(fallbackTitle, x, y);
        g2.dispose();
        
        return new ImageIcon(img);
    }
    
    public RecommendationPanel getRecommendationPanel() {
        return recommendationPanel;
    }

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