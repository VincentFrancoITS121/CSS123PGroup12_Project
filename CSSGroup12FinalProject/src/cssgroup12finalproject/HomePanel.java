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

public class HomePanel extends JPanel {
    private MainFrame parentFrame;
    private JPanel featuredCarousel;
    private JLabel featuredLabel;
    private Timer carouselTimer;
    private int currentIndex = 0;
    
    // --- New Color Palette for Dark Mode ---
    private final Color BACKGROUND_DARK = new Color(30, 30, 45); // Main Dark Background
    private final Color BACKGROUND_LIGHTER = new Color(45, 45, 60); // Component Background
    private final Color ACCENT_PRIMARY = new Color(106, 142, 255); // Azure Blue
    private final Color ACCENT_SECONDARY = new Color(96, 227, 176); // Mint Green
    private final Color TEXT_LIGHT = new Color(230, 230, 230); // Light Text Color
    private final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 64);
    private final Font HEADING_FONT = new Font("SansSerif", Font.BOLD, 18);
    private final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 16);
    // ----------------------------------------
    
    public HomePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_DARK); // Set main panel background
        setupUI();
        startCarousel(); // Vincent will enhance this
    }
    
    private void setupUI() {
        // Header
        JLabel titleLabel = new JLabel("Manhwa-Link!", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT); // Larger, more impactful font
        titleLabel.setForeground(ACCENT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0)); // More padding
        add(titleLabel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_DARK);
        
        // Featured Carousel
        featuredCarousel = new JPanel();
        featuredCarousel.setLayout(new BorderLayout());
        featuredCarousel.setBackground(BACKGROUND_LIGHTER); // Lighter dark for component
        
        // Custom Titled Border with better styling
        featuredCarousel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 80, 20, 80), // More horizontal padding
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_PRIMARY, 2, true), // Rounded blue line
                "🔥 Featured Manhwa", // Added an emoji
                0, 0,
                HEADING_FONT,
                TEXT_LIGHT // Light text for the border title
            )
        ));
        featuredCarousel.setPreferredSize(new Dimension(800, 400)); // Larger
        
        featuredLabel = new JLabel("Loading...", SwingConstants.CENTER);
        featuredLabel.setFont(new Font("Arial", Font.PLAIN, 24)); // Slightly larger font
        featuredLabel.setForeground(TEXT_LIGHT);
        featuredCarousel.add(featuredLabel, BorderLayout.CENTER);
        
        centerPanel.add(featuredCarousel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // Navigation Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 40)); // More spacing
        buttonPanel.setBackground(BACKGROUND_DARK);
        
        // Custom Button Styling Function
        JButton browseBtn = createStyledButton("Browse Recommendations", ACCENT_PRIMARY);
        JButton shopBtn = createStyledButton("Shopping Hub", ACCENT_SECONDARY);
        
        browseBtn.addActionListener(e -> parentFrame.showPanel("RECOMMENDATIONS"));
        shopBtn.addActionListener(e -> parentFrame.showPanel("SHOPPING_HUB"));
        
        buttonPanel.add(browseBtn);
        buttonPanel.add(shopBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JButton createStyledButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(new Dimension(300, 60)); // Larger buttons
        button.setBackground(background);
        button.setForeground(Color.BLACK); // Dark text on bright button
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        return button;
    }
    
    void startCarousel() {
        // Vincent will enhance this with better animations
        RecommendationEngine engine = new RecommendationEngine();
        java.util.List<Manhwa> featured = engine.getFeaturedManhwa(5);
        
        if (featured.isEmpty()) {
            featuredLabel.setText("No manhwa available");
            return;
        }
        
        carouselTimer = new Timer(5000, e -> {
            currentIndex = (currentIndex + 1) % featured.size();
            Manhwa current = featured.get(currentIndex);
            updateCarouselLabel(current);
        });
        
        // Show first item immediately
        updateCarouselLabel(featured.get(0));
        
        carouselTimer.start();
    }
    
    private void updateCarouselLabel(Manhwa manhwa) {
        // Updated HTML for better dark theme text presentation
        String html = "<html><div style='text-align: center; padding: 20px; color: " + toHtmlColor(TEXT_LIGHT) + ";'>" +
                     "<h2 style='font-size: 32px; color: " + toHtmlColor(ACCENT_PRIMARY) + "; margin-bottom: 10px;'>" + manhwa.getTitle() + "</h2>" +
                     "<p style='margin: 5px 0;'><b>Author:</b> " + manhwa.getAuthor() + "</p>" +
                     "<p style='margin: 5px 0;'><b>Genre:</b> " + manhwa.getGenre() + "</p>" +
                     "<p style='font-size: 24px; color: gold; margin: 15px 0;'>★ " + manhwa.getRating() + "</p>" +
                     "<p style='width: 600px; margin: 0 auto; font-style: italic;'>" + manhwa.getDescription() + "</p>" +
                     "</div></html>";
        
        featuredLabel.setText(html);
    }
    
    private String toHtmlColor(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public void stopCarousel() {
        if (carouselTimer != null) {
            carouselTimer.stop();
        }
    }
}