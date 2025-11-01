package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class RecommendationPanel extends JPanel {
    private MainFrame parentFrame;
    private RecommendationEngine engine;
    private JPanel manhwaGrid;
    private JPanel gridContainer;
    private String currentGenre = null;
    private String currentDemographic = null;
    private String currentAgeRating = null;
    
    // UI Constants (Matching the Dark Theme)
    private final Color BACKGROUND_DARK = AppConfig.BG_DARK;
    private final Color CARD_BACKGROUND = AppConfig.BG_CARD;
    private final Color ACCENT_PRIMARY = AppConfig.ACCENT_PRIMARY;
    private final Color TEXT_LIGHT = AppConfig.TEXT_LIGHT;
    private final Color TEXT_SUBTLE = AppConfig.TEXT_SUBTLE;
    private final Color GOLD = AppConfig.GOLD;
    private final Color BG_IMAGE = AppConfig.BG_IMAGE;
    private final Font TITLE_FONT = AppConfig.TITLE_MEDIUM;
    private final Font CARD_TITLE_FONT = AppConfig.CARD_TITLE;
    private final Font BUTTON_FONT = AppConfig.BUTTON;
    
    public RecommendationPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.engine = new RecommendationEngine();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_DARK); 
        setupUI();
    }
    
    private void setupUI() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(AppConfig.PADDING, AppConfig.PADDING, 
                                                             AppConfig.PADDING, AppConfig.PADDING));
        
        JButton backBtn = createHeaderButton("← Back to Home", ACCENT_PRIMARY);
        backBtn.addActionListener(e -> parentFrame.showPanel(AppConfig.PANEL_HOME));
        headerPanel.add(backBtn, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel("Browse Manhwa", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_LIGHT);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        JButton clearBtn = createHeaderButton("Clear Filters", TEXT_SUBTLE);
        clearBtn.addActionListener(e -> {
            currentGenre = null;
            currentDemographic = null;
            currentAgeRating = null;
            updateRecommendations(null, null, null);
        });
        headerPanel.add(clearBtn, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Manhwa Grid setup:
        manhwaGrid = new JPanel(new GridLayout(0, AppConfig.GRID_COLUMNS, AppConfig.GRID_GAP, AppConfig.GRID_GAP));
        manhwaGrid.setBackground(BACKGROUND_DARK);
        manhwaGrid.setBorder(BorderFactory.createEmptyBorder(AppConfig.PADDING, AppConfig.PADDING, AppConfig.PADDING, AppConfig.PADDING));

        gridContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridContainer.setBackground(BACKGROUND_DARK);
        gridContainer.add(manhwaGrid);
        
        JScrollPane scrollPane = new JScrollPane(gridContainer); 
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        scrollPane.getViewport().setBackground(BACKGROUND_DARK);
        
        add(scrollPane, BorderLayout.CENTER);
        
        updateRecommendations(null, null, null);
    }
    
    private JButton createHeaderButton(String text, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BACKGROUND_DARK);
        button.setForeground(foreground);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        return button;
    }
    
    public void updateRecommendations(String genre, String demographic, String ageRating) {
        if (genre != null) currentGenre = genre;
        if (demographic != null) currentDemographic = demographic;
        if (ageRating != null) currentAgeRating = ageRating;
        
        manhwaGrid.removeAll();
        // Reset layout to Grid for recommendations
        if (!(manhwaGrid.getLayout() instanceof GridLayout)) {
            manhwaGrid.setLayout(new GridLayout(0, AppConfig.GRID_COLUMNS, AppConfig.GRID_GAP, AppConfig.GRID_GAP));
        }

        List<Manhwa> recommendations = engine.getRecommendations(
            currentGenre, currentDemographic, currentAgeRating
        );
        
        if (recommendations.isEmpty()) {
            // Temporarily set manhwaGrid to GridBagLayout to center the message
            manhwaGrid.setLayout(new GridBagLayout()); 
            JLabel noResults = new JLabel("No manhwa found matching your filters 🥺");
            noResults.setFont(AppConfig.TITLE_MEDIUM);
            noResults.setForeground(TEXT_SUBTLE);
            manhwaGrid.add(noResults);
        } else {
            for (Manhwa manhwa : recommendations) {
                JPanel card = createManhwaCard(manhwa);
                manhwaGrid.add(card);
            }
        }
        
        manhwaGrid.revalidate();
        manhwaGrid.repaint();
        gridContainer.revalidate();
        gridContainer.repaint();
    }
    
    public void showRandomRecommendation() {
        Manhwa random = engine.getRandomRecommendation();
        if (random != null) {
            manhwaGrid.removeAll();
            
            // Set manhwaGrid to GridBagLayout for centering the single card
            manhwaGrid.setLayout(new GridBagLayout());
            
            JPanel card = createManhwaCard(random);
            card.setPreferredSize(new Dimension(AppConfig.CARD_WIDTH * 150/100, AppConfig.CARD_HEIGHT * 115/100)); // Larger card for focus
            
            manhwaGrid.add(card); 
            
            manhwaGrid.revalidate();
            manhwaGrid.repaint();
            gridContainer.revalidate();
            gridContainer.repaint();
        }
    }
    
    private JPanel createManhwaCard(Manhwa manhwa) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setBackground(CARD_BACKGROUND); 
        card.setPreferredSize(new Dimension(AppConfig.CARD_WIDTH, AppConfig.CARD_HEIGHT));
        
        // Image implementation: Load and display the cover image
        JLabel imageLabel = new JLabel();
        ImageIcon coverIcon = MainFrame.toManhwaCoverIcon(
            manhwa.getCoverImagePath(), 
            AppConfig.CARD_WIDTH - 20, // width
            200 // height
        );
        imageLabel.setIcon(coverIcon);
        imageLabel.setPreferredSize(new Dimension(AppConfig.CARD_WIDTH - 20, 200));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(BG_IMAGE);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.NORTH);
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BACKGROUND);
        
        JLabel titleLabel = new JLabel(manhwa.getTitle());
        titleLabel.setFont(CARD_TITLE_FONT);
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel genreLabel = new JLabel(manhwa.getGenre() + " | " + manhwa.getDemographic());
        genreLabel.setFont(AppConfig.SMALL);
        genreLabel.setForeground(TEXT_SUBTLE);
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel("★ " + manhwa.getRating() + "/10");
        ratingLabel.setFont(AppConfig.NORMAL);
        ratingLabel.setForeground(GOLD); 
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(ratingLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Mouse Event Implementation (Hover Effect)
        card.addMouseListener(new MouseAdapter() {
            private Color originalColor = card.getBackground();
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(BG_IMAGE); // Slightly darker on hover
                card.setBorder(BorderFactory.createLineBorder(ACCENT_PRIMARY, 2));
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                String tooltip = "<html><div style='width: 300px; padding: 10px; color: black; background: #e0e0e0;'>" +
                                "<b>" + manhwa.getTitle() + "</b><br><br>" +
                                "<b>Author:</b> " + manhwa.getAuthor() + "<br>" +
                                "<b>Rating:</b> <span style='color: " + MainFrame.toHtmlColor(GOLD) + ";'>★ " + manhwa.getRating() + "/10</span><br>" + // USE HELPER
                                "<b>Age Rating:</b> " + manhwa.getAgeRating() + "<br><br>" +
                                "<i>" + manhwa.getDescription() + "</i>" +
                                "</div></html>";
                card.setToolTipText(tooltip);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(originalColor);
                card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(card, 
                    "<html><div style='width: 300px; font-family: SansSerif;'>" + 
                    "<h3 style='color: " + MainFrame.toHtmlColor(ACCENT_PRIMARY) + ";'>" + manhwa.getTitle() + "</h3>" + // USE HELPER
                    "<b>Author:</b> " + manhwa.getAuthor() + "<br>" +
                    "<b>Description:</b> <i>" + manhwa.getDescription() + "</i><br><br>" +
                    "<b>Rating:</b> <span style='color: " + MainFrame.toHtmlColor(GOLD) + ";'>★ " + manhwa.getRating() + "/10</span><br>" + // USE HELPER
                    "<b>Genre:</b> " + manhwa.getGenre() + "<br>" +
                    "<b>Demographic:</b> " + manhwa.getDemographic() + 
                    "</div></html>",
                    "Manhwa Details",
                    JOptionPane.PLAIN_MESSAGE 
                );
            }
        });
        
        return card;
    }
}