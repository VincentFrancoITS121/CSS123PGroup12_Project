package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePanel extends JPanel {
    private MainFrame parentFrame;
    private FeaturedCarouselPanel carouselPanel;
    
    // --- UI Constants imported from AppConfig ---
    private final Color BACKGROUND_DARK = AppConfig.BG_DARK;
    private final Color ACCENT_PRIMARY = AppConfig.ACCENT_PRIMARY;
    private final Color TEXT_LIGHT = AppConfig.TEXT_LIGHT;
    private final Font TITLE_FONT = AppConfig.TITLE_LARGE;
    private final Font BUTTON_FONT = AppConfig.BUTTON;

    public HomePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_DARK);

        // Initialize carousel panel first
        carouselPanel = new FeaturedCarouselPanel(); 
        
        setupUI();
    }

    private void setupUI() {
        // Header Panel (Top Title)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(AppConfig.PADDING, AppConfig.PADDING, 
                                                             AppConfig.PADDING, AppConfig.PADDING));

        JLabel title = new JLabel("Welcome to Manhwa-Link!", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT_PRIMARY);

        headerPanel.add(title, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);
        
        // Center: Featured Carousel
        JPanel centerWrapper = new JPanel(new GridBagLayout()); // Use GridBag to center the carousel
        centerWrapper.setBackground(BACKGROUND_DARK);
        centerWrapper.setBorder(BorderFactory.createEmptyBorder(0, AppConfig.PADDING, 0, AppConfig.PADDING));
        centerWrapper.add(carouselPanel);
        add(centerWrapper, BorderLayout.CENTER);
        
        // Navigation Buttons (Bottom)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, AppConfig.GRID_GAP, AppConfig.PADDING / 2));
        buttonPanel.setBackground(BACKGROUND_DARK);

        JButton browseBtn = createStyledButton("📚 Browse Manhwa");
        browseBtn.addActionListener(e -> parentFrame.showPanel(AppConfig.PANEL_RECOMMENDATIONS));

        JButton recBtn = createStyledButton("✨ Recommendations");
        recBtn.addActionListener(e -> {
             // For a pure rec click, we show the panel and update to random
             parentFrame.showPanel(AppConfig.PANEL_RECOMMENDATIONS);
             parentFrame.getRecommendationPanel().showRandomRecommendation();
        });

        JButton shopBtn = createStyledButton("🛒 Shopping Hub");
        shopBtn.addActionListener(e -> parentFrame.showPanel(AppConfig.PANEL_SHOPPING));

        buttonPanel.add(browseBtn);
        buttonPanel.add(recBtn);
        buttonPanel.add(shopBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.BLACK);
        button.setBackground(ACCENT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Hover effect: TEXT_LIGHT is white/light gray
                button.setBackground(TEXT_LIGHT); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_PRIMARY);
            }
        });

        return button;
    }

    // Public methods for MainFrame to manage carousel animation
    public void startCarousel() {
        carouselPanel.start();
    }

    public void stopCarousel() {
        carouselPanel.stop();
    }
}