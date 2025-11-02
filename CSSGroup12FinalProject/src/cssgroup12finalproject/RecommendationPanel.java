package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationPanel extends JPanel {
    private MainFrame parentFrame;
    private RecommendationEngine engine;
    private JPanel manhwaGrid;
    private JScrollPane scrollPane;
    private String currentGenre = null;
    private String currentDemographic = null;
    private String currentAgeRating = null;
    private JTextField searchField;
    private JLabel filterLabel;
    
    public RecommendationPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.engine = new RecommendationEngine();
        setLayout(new BorderLayout());
        setBackground(AppConfig.BG_DARK);
        setupUI();
    }
    
private void setupUI() {
    // Header
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(AppConfig.BG_DARK);
    headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    // Back button (West)
    JButton backBtn = new JButton("← Back");
    backBtn.setFont(AppConfig.BUTTON);
    backBtn.setBackground(AppConfig.BG_DARK);
    backBtn.setForeground(AppConfig.ACCENT_PRIMARY);
    backBtn.setBorderPainted(false);
    backBtn.setFocusPainted(false);
    backBtn.addActionListener(e -> parentFrame.showPanel("HOME"));
    headerPanel.add(backBtn, BorderLayout.WEST);
    
    // Center panel with title and search
    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
    centerPanel.setBackground(AppConfig.BG_DARK);
    
    JLabel titleLabel = new JLabel("Browse Manhwa", SwingConstants.CENTER);
    titleLabel.setFont(AppConfig.TITLE_MEDIUM);
    titleLabel.setForeground(AppConfig.TEXT_LIGHT);
    titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    // Search bar
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    searchPanel.setBackground(AppConfig.BG_DARK);
    
    searchField = new JTextField(20);
    searchField.setFont(AppConfig.NORMAL);
    searchField.setBackground(AppConfig.BG_CARD);
    searchField.setForeground(AppConfig.TEXT_LIGHT);
    searchField.setCaretColor(AppConfig.TEXT_LIGHT);
    
    JButton searchBtn = new JButton("🔍 Search");
    searchBtn.setFont(AppConfig.BUTTON);
    searchBtn.setBackground(AppConfig.ACCENT_PRIMARY);
    searchBtn.setForeground(Color.BLACK);
    searchBtn.setFocusPainted(false);
    searchBtn.addActionListener(e -> performSearch());
    
    searchField.addActionListener(e -> performSearch());
    
    JLabel searchLabel = new JLabel("Search:");
    searchLabel.setForeground(AppConfig.TEXT_SUBTLE);
    
    searchPanel.add(searchLabel);
    searchPanel.add(searchField);
    searchPanel.add(searchBtn);
    
    // Filter indicator label
    filterLabel = new JLabel("Showing: All Manhwa");
    filterLabel.setFont(AppConfig.SMALL);
    filterLabel.setForeground(AppConfig.TEXT_SUBTLE);
    filterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    centerPanel.add(titleLabel);
    centerPanel.add(Box.createVerticalStrut(10));
    centerPanel.add(searchPanel);
    centerPanel.add(Box.createVerticalStrut(5));
    centerPanel.add(filterLabel);
    
    headerPanel.add(centerPanel, BorderLayout.CENTER);
    
    // Clear filters button (East)
    JButton clearBtn = new JButton("Clear Filters");
    clearBtn.setFont(AppConfig.BUTTON);
    clearBtn.setBackground(AppConfig.BG_DARK);
    clearBtn.setForeground(AppConfig.TEXT_SUBTLE);
    clearBtn.setBorderPainted(false);
    clearBtn.setFocusPainted(false);
    clearBtn.addActionListener(e -> {
        currentGenre = null;
        currentDemographic = null;
        currentAgeRating = null;
        searchField.setText("");
        updateRecommendations(null, null, null);
    });
    headerPanel.add(clearBtn, BorderLayout.EAST);
    
    add(headerPanel, BorderLayout.NORTH);
    
    // THIS WAS THE MISSING PART:
    // Grid for manhwa cards
    manhwaGrid = new JPanel();
    manhwaGrid.setBackground(AppConfig.BG_DARK);
    manhwaGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    scrollPane = new JScrollPane(manhwaGrid);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.setBorder(null);
    scrollPane.getViewport().setBackground(AppConfig.BG_DARK);
    add(scrollPane, BorderLayout.CENTER);
    
    updateRecommendations(null, null, null);
}

    private void performSearch() {
    String query = searchField.getText().trim();
    if (query.isEmpty()) {
        updateRecommendations(currentGenre, currentDemographic, currentAgeRating);
        return;
    }
    
    List<Manhwa> results = engine.getRecommendations(
        currentGenre, currentDemographic, currentAgeRating
    ).stream()
     .filter(m -> m.getTitle().toLowerCase().contains(query.toLowerCase()))
     .collect(Collectors.toList());
    
    AppLogger.info("Search for '" + query + "': " + results.size() + " results");
    
    manhwaGrid.removeAll();
    manhwaGrid.setLayout(new GridLayout(0, 4, 20, 20));
    
    if (results.isEmpty()) {
        manhwaGrid.setLayout(new GridBagLayout());
        JLabel noResults = new JLabel("No results for '" + query + "' 🥺");
        noResults.setFont(AppConfig.TITLE_MEDIUM);
        noResults.setForeground(AppConfig.TEXT_SUBTLE);
        manhwaGrid.add(noResults);
    } else {
        for (Manhwa manhwa : results) {
            manhwaGrid.add(createManhwaCard(manhwa));
        }
    }
    
    filterLabel.setText("Search: \"" + query + "\" | Found: " + results.size());
    manhwaGrid.revalidate();
    manhwaGrid.repaint();
    scrollPane.revalidate();
}
    
    public void updateRecommendations(String genre, String demographic, String ageRating) {
        if (genre != null) currentGenre = genre;
        if (demographic != null) currentDemographic = demographic;
        if (ageRating != null) currentAgeRating = ageRating;
        
        List<Manhwa> recommendations = engine.getRecommendations(
            currentGenre, currentDemographic, currentAgeRating
        );
        
        AppLogger.info("Updating recommendations: " + recommendations.size() + " found");
        
        manhwaGrid.removeAll();
        manhwaGrid.setLayout(new GridLayout(0, 4, 20, 20));
        
        if (recommendations.isEmpty()) {
            manhwaGrid.setLayout(new GridBagLayout());
            JLabel noResults = new JLabel("No manhwa found 🥺");
            noResults.setFont(AppConfig.TITLE_MEDIUM);
            noResults.setForeground(AppConfig.TEXT_SUBTLE);
            manhwaGrid.add(noResults);
        } else {
            for (Manhwa manhwa : recommendations) {
                manhwaGrid.add(createManhwaCard(manhwa));
            }
        }
        // Update filter label
String filterText = "Showing: ";
if (currentGenre != null) filterText += currentGenre + " | ";
if (currentDemographic != null) filterText += currentDemographic + " | ";
if (currentAgeRating != null) filterText += currentAgeRating + " | ";
filterText += recommendations.size() + " manhwa";
filterLabel.setText(filterText);

        manhwaGrid.revalidate();
        manhwaGrid.repaint();
        scrollPane.revalidate();
        scrollPane.getViewport().setViewPosition(new Point(0, 0));
    }
    
    public void showRandomRecommendation() {
        Manhwa random = engine.getRandomRecommendation();
        if (random != null) {
            AppLogger.info("Showing random: " + random.getTitle());
            manhwaGrid.removeAll();
            manhwaGrid.setLayout(new GridBagLayout());
            
            JPanel card = createManhwaCard(random);
            card.setPreferredSize(new Dimension(350, 500));
            manhwaGrid.add(card);
            
            manhwaGrid.revalidate();
            manhwaGrid.repaint();
        }
    }
    
    private JPanel createManhwaCard(Manhwa m) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setBackground(AppConfig.BG_CARD);
        card.setPreferredSize(new Dimension(AppConfig.CARD_WIDTH, AppConfig.CARD_HEIGHT));
        
        // Cover image
        JLabel imageLabel = new JLabel();
        ImageIcon icon = MainFrame.toManhwaCoverIcon(m.getCoverImagePath(), 230, 200);
        imageLabel.setIcon(icon);
        imageLabel.setPreferredSize(new Dimension(230, 200));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(AppConfig.BG_IMAGE);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.NORTH);
        
        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(AppConfig.BG_CARD);
        
        JLabel titleLabel = new JLabel("<html><center>" + m.getTitle() + "</center></html>");
        titleLabel.setFont(AppConfig.CARD_TITLE);
        titleLabel.setForeground(AppConfig.TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel genreLabel = new JLabel(m.getGenre() + " | " + m.getDemographic());
        genreLabel.setFont(AppConfig.SMALL);
        genreLabel.setForeground(AppConfig.TEXT_SUBTLE);
        genreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel ratingLabel = new JLabel("★ " + m.getRating() + "/10");
        ratingLabel.setFont(AppConfig.NORMAL);
        ratingLabel.setForeground(AppConfig.GOLD);
        ratingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(genreLabel);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(ratingLabel);
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Hover effects
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(AppConfig.BG_IMAGE);
                card.setBorder(BorderFactory.createLineBorder(AppConfig.ACCENT_PRIMARY, 2));
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(AppConfig.BG_CARD);
                card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                card.setCursor(Cursor.getDefaultCursor());
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                showManhwaDetails(m);
            }
        });
        
        return card;
    }
    
    private void showManhwaDetails(Manhwa m) {
        String html = "<html><body style='width: 350px; font-family: SansSerif;'>" +
                "<h2 style='color: " + MainFrame.toHtmlColor(AppConfig.ACCENT_PRIMARY) + ";'>" + m.getTitle() + "</h2>" +
                "<p><b>Author:</b> " + m.getAuthor() + "</p>" +
                "<p><b>Genre:</b> " + m.getGenre() + " | <b>Demographic:</b> " + m.getDemographic() + "</p>" +
                "<p><b>Age Rating:</b> " + m.getAgeRating() + "</p>" +
                "<p><b>Rating:</b> <span style='color: " + MainFrame.toHtmlColor(AppConfig.GOLD) + ";'>★ " + m.getRating() + "/10</span></p>" +
                "<p><i>" + m.getDescription() + "</i></p>" +
                "</body></html>";
        
        JOptionPane.showMessageDialog(this, html, "Manhwa Details", JOptionPane.PLAIN_MESSAGE);
    }
}