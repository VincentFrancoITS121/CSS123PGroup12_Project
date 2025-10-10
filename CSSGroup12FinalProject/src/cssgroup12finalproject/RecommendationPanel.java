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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class RecommendationPanel extends JPanel {
    private MainFrame parentFrame;
    private RecommendationEngine engine;
    private JPanel manhwaGrid;
    private String currentGenre = null;
    private String currentDemographic = null;
    private String currentAgeRating = null;
    
    public RecommendationPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.engine = new RecommendationEngine();
        setLayout(new BorderLayout());
        setupUI();
    }
    
    private void setupUI() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton backBtn = new JButton("← Back to Home");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        backBtn.addActionListener(e -> parentFrame.showPanel("HOME"));
        headerPanel.add(backBtn, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Browse Manhwa", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton clearBtn = new JButton("Clear Filters");
        clearBtn.addActionListener(e -> {
            currentGenre = null;
            currentDemographic = null;
            currentAgeRating = null;
            updateRecommendations(null, null, null);
        });
        headerPanel.add(clearBtn, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Manhwa Grid
        manhwaGrid = new JPanel(new GridLayout(0, 3, 15, 15));
        manhwaGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(manhwaGrid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load all manhwa initially
        updateRecommendations(null, null, null);
    }
    
    public void updateRecommendations(String genre, String demographic, String ageRating) {
        if (genre != null) currentGenre = genre;
        if (demographic != null) currentDemographic = demographic;
        if (ageRating != null) currentAgeRating = ageRating;
        
        manhwaGrid.removeAll();
        List<Manhwa> recommendations = engine.getRecommendations(
            currentGenre, currentDemographic, currentAgeRating
        );
        
        if (recommendations.isEmpty()) {
            JLabel noResults = new JLabel("No manhwa found matching your filters :(");
            noResults.setFont(new Font("Arial", Font.PLAIN, 18));
            noResults.setHorizontalAlignment(SwingConstants.CENTER);
            manhwaGrid.add(noResults);
        } else {
            for (Manhwa manhwa : recommendations) {
                JPanel card = createManhwaCard(manhwa);
                manhwaGrid.add(card);
            }
        }
        
        manhwaGrid.revalidate();
        manhwaGrid.repaint();
    }
    
    public void showRandomRecommendation() {
        Manhwa random = engine.getRandomRecommendation();
        if (random != null) {
            manhwaGrid.removeAll();
            JPanel card = createManhwaCard(random);
            card.setPreferredSize(new Dimension(400, 500));
            
            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.add(card);
            
            manhwaGrid.setLayout(new BorderLayout());
            manhwaGrid.add(centerPanel, BorderLayout.CENTER);
            
            manhwaGrid.revalidate();
            manhwaGrid.repaint();
        }
    }
    
    private JPanel createManhwaCard(Manhwa manhwa) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(250, 350));
        
        // Image placeholder (Vincent can add real images later)
        JLabel imageLabel = new JLabel("📚", SwingConstants.CENTER);
        imageLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        imageLabel.setPreferredSize(new Dimension(230, 150));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(240, 240, 240));
        card.add(imageLabel, BorderLayout.NORTH);
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(manhwa.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel genreLabel = new JLabel(manhwa.getGenre() + " | " + manhwa.getDemographic());
        genreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        genreLabel.setForeground(Color.GRAY);
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel("★ " + manhwa.getRating() + "/10");
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ratingLabel.setForeground(new Color(255, 140, 0));
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(ratingLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Joshua: YOUR MOUSE EVENT IMPLEMENTATION HERE
        card.addMouseListener(new MouseAdapter() {
            private Color originalColor = card.getBackground();
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 248, 255));
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                // Show tooltip with detailed info
                String tooltip = "<html><div style='width: 300px; padding: 10px;'>" +
                                "<b>" + manhwa.getTitle() + "</b><br><br>" +
                                "<b>Author:</b> " + manhwa.getAuthor() + "<br>" +
                                "<b>Rating:</b> ★ " + manhwa.getRating() + "/10<br>" +
                                "<b>Age Rating:</b> " + manhwa.getAgeRating() + "<br><br>" +
                                "<i>" + manhwa.getDescription() + "</i>" +
                                "</div></html>";
                card.setToolTipText(tooltip);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(originalColor);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY, 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                // You can add click functionality here
                JOptionPane.showMessageDialog(card, 
                    manhwa.getTitle() + " by " + manhwa.getAuthor() + "\n\n" +
                    manhwa.getDescription() + "\n\n" +
                    "Rating: " + manhwa.getRating() + "/10\n" +
                    "Genre: " + manhwa.getGenre() + "\n" +
                    "Demographic: " + manhwa.getDemographic(),
                    "Manhwa Details",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        
        return card;
    }
}
