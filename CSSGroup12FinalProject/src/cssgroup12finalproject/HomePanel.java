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
    
    public HomePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setupUI();
        startCarousel(); // Vincent will enhance this
    }
    
    private void setupUI() {
        // Header
        JLabel titleLabel = new JLabel("Manhwa-Link!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(70, 130, 180));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Featured Carousel
        featuredCarousel = new JPanel();
        featuredCarousel.setLayout(new BorderLayout());
        featuredCarousel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(20, 50, 20, 50),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2),
                "Featured Manhwa",
                0, 0,
                new Font("Arial", Font.BOLD, 18)
            )
        ));
        featuredCarousel.setPreferredSize(new Dimension(600, 300));
        
        featuredLabel = new JLabel("Loading...", SwingConstants.CENTER);
        featuredLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        featuredCarousel.add(featuredLabel, BorderLayout.CENTER);
        
        centerPanel.add(featuredCarousel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // Navigation Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        
        JButton browseBtn = new JButton("Browse Recommendations");
        browseBtn.setFont(new Font("Arial", Font.BOLD, 16));
        browseBtn.setPreferredSize(new Dimension(250, 50));
        browseBtn.setBackground(new Color(70, 130, 180));
        browseBtn.setForeground(Color.WHITE);
        browseBtn.setFocusPainted(false);
        
        JButton shopBtn = new JButton("Shopping Hub");
        shopBtn.setFont(new Font("Arial", Font.BOLD, 16));
        shopBtn.setPreferredSize(new Dimension(250, 50));
        shopBtn.setBackground(new Color(60, 179, 113));
        shopBtn.setForeground(Color.WHITE);
        shopBtn.setFocusPainted(false);
        
        browseBtn.addActionListener(e -> parentFrame.showPanel("RECOMMENDATIONS"));
        shopBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "Shopping Hub coming soon! (Steven/Vincent will implement)"));
        
        buttonPanel.add(browseBtn);
        buttonPanel.add(shopBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void startCarousel() {
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
            
            String html = "<html><div style='text-align: center; padding: 20px;'>" +
                         "<h2>" + current.getTitle() + "</h2>" +
                         "<p><b>Author:</b> " + current.getAuthor() + "</p>" +
                         "<p><b>Genre:</b> " + current.getGenre() + "</p>" +
                         "<p><b>Rating:</b> ★ " + current.getRating() + "</p>" +
                         "<p style='width: 500px;'>" + current.getDescription() + "</p>" +
                         "</div></html>";
            
            featuredLabel.setText(html);
        });
        
        // Show first item immediately
        Manhwa first = featured.get(0);
        String html = "<html><div style='text-align: center; padding: 20px;'>" +
                     "<h2>" + first.getTitle() + "</h2>" +
                     "<p><b>Author:</b> " + first.getAuthor() + "</p>" +
                     "<p><b>Genre:</b> " + first.getGenre() + "</p>" +
                     "<p><b>Rating:</b> ★ " + first.getRating() + "</p>" +
                     "<p style='width: 500px;'>" + first.getDescription() + "</p>" +
                     "</div></html>";
        featuredLabel.setText(html);
        
        carouselTimer.start();
    }
    
    public void stopCarousel() {
        if (carouselTimer != null) {
            carouselTimer.stop();
        }
    }
}
