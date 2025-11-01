package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ShoppingHubPanel extends JPanel {
    private MainFrame parentFrame;
    private ManhwaDatabase database;
    private JPanel shopGrid;
    
    // --- UI Constants imported from AppConfig ---
    private final Color BACKGROUND_DARK = AppConfig.BG_DARK;
    private final Color CARD_BACKGROUND = AppConfig.BG_CARD;
    private final Color ACCENT_PRIMARY = AppConfig.ACCENT_PRIMARY;
    private final Color ACCENT_SECONDARY = AppConfig.ACCENT_SECONDARY;
    private final Color TEXT_LIGHT = AppConfig.TEXT_LIGHT;
    private final Color TEXT_SUBTLE = AppConfig.TEXT_SUBTLE;
    private final Color PRICE_COLOR = AppConfig.GOLD; 
    private final Color DISCOUNT_COLOR = AppConfig.DISCOUNT_RED; 
    private final Color BG_IMAGE = AppConfig.BG_IMAGE; 

    private final Font TITLE_FONT = AppConfig.TITLE_MEDIUM;
    private final Font CARD_TITLE_FONT = AppConfig.CARD_TITLE;
    private final Font BUTTON_FONT = AppConfig.BUTTON;
    
    public ShoppingHubPanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.database = ManhwaDatabase.getInstance();
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_DARK);
        setupUI();
        loadProducts();
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
        
        JLabel titleLabel = new JLabel("Manhwa Shopping Hub 🛒", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_LIGHT);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Manhwa Shop Grid
        shopGrid = new JPanel(new GridLayout(0, AppConfig.GRID_COLUMNS, AppConfig.GRID_GAP, AppConfig.GRID_GAP)); 
        shopGrid.setBackground(BACKGROUND_DARK);
        shopGrid.setBorder(BorderFactory.createEmptyBorder(AppConfig.PADDING, AppConfig.PADDING, AppConfig.PADDING, AppConfig.PADDING));
        
        JScrollPane scrollPane = new JScrollPane(shopGrid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND_DARK);
        
        add(scrollPane, BorderLayout.CENTER);
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
    
    private void loadProducts() {
        shopGrid.removeAll();
        List<Manhwa> allManhwa = database.getAllManhwa();
        
        if (allManhwa.isEmpty()) {
             shopGrid.setLayout(new GridBagLayout()); 
             JLabel noProducts = new JLabel("No products available in the shop.");
             noProducts.setFont(AppConfig.TITLE_MEDIUM);
             noProducts.setForeground(TEXT_SUBTLE);
             shopGrid.add(noProducts);
        } else {
             shopGrid.setLayout(new GridLayout(0, AppConfig.GRID_COLUMNS, AppConfig.GRID_GAP, AppConfig.GRID_GAP));
             for (Manhwa manhwa : allManhwa) {
                 shopGrid.add(createProductCard(manhwa));
             }
        }
        
        shopGrid.revalidate();
        shopGrid.repaint();
    }
    
    private JPanel createProductCard(Manhwa manhwa) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        // UI Consistency Change: Use empty border by default, similar to RecommendationPanel
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        card.setBackground(CARD_BACKGROUND);
        card.setPreferredSize(new Dimension(AppConfig.CARD_WIDTH, AppConfig.SHOP_CARD_HEIGHT));
        
        // Image implementation: Load and display the cover image
        JLabel imageLabel = new JLabel();
        ImageIcon coverIcon = MainFrame.toManhwaCoverIcon(
            manhwa.getCoverImagePath(), 
            AppConfig.CARD_WIDTH - 20, // width
            180 // height
        );
        imageLabel.setIcon(coverIcon);
        imageLabel.setPreferredSize(new Dimension(AppConfig.CARD_WIDTH - 20, 180));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(AppConfig.BG_IMAGE);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.NORTH);
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_BACKGROUND);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel titleLabel = new JLabel(manhwa.getTitle());
        titleLabel.setFont(CARD_TITLE_FONT);
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel authorLabel = new JLabel("by " + manhwa.getAuthor());
        authorLabel.setFont(AppConfig.SMALL);
        authorLabel.setForeground(TEXT_SUBTLE);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Price Label Setup
        JLabel priceLabel = new JLabel(String.format("$%.2f", manhwa.getPrice()));
        priceLabel.setFont(AppConfig.TITLE_SMALL);
        
        JLabel discountLabel = new JLabel();
        
        if (manhwa.getPrice() > 0.0) { // Only show price section if price > 0
            if (manhwa.hasDiscount()) {
                priceLabel.setText("SALE: " + priceLabel.getText());
                priceLabel.setForeground(ACCENT_SECONDARY); // Green for sale
                discountLabel.setText("Coupon: " + manhwa.getCouponCode());
                discountLabel.setForeground(DISCOUNT_COLOR); // Red for urgent discount
            } else {
                priceLabel.setForeground(PRICE_COLOR); // Gold for full price
                discountLabel.setText("Full Price");
                discountLabel.setForeground(TEXT_SUBTLE);
            }
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            discountLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
            discountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(titleLabel);
            infoPanel.add(authorLabel);
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(priceLabel);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(discountLabel);
            infoPanel.add(Box.createVerticalGlue()); 
            
        } else {
            // No Price available message
            JLabel priceUnavailable = new JLabel("Price Unavailable");
            priceUnavailable.setFont(AppConfig.NORMAL);
            priceUnavailable.setForeground(TEXT_SUBTLE);
            priceUnavailable.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(titleLabel);
            infoPanel.add(authorLabel);
            infoPanel.add(Box.createVerticalStrut(10));
            infoPanel.add(priceUnavailable);
            infoPanel.add(Box.createVerticalGlue());
        }
        
        // Buy Button
        JButton buyBtn = new JButton("Buy at Retailer"); 
        buyBtn.setFont(BUTTON_FONT);
        buyBtn.setBackground(ACCENT_SECONDARY);
        buyBtn.setForeground(Color.BLACK);
        buyBtn.setFocusPainted(false);
        buyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyBtn.setMaximumSize(new Dimension(200, 40));
        
        // ACTION LISTENER: Open the purchase URL in a browser
        buyBtn.addActionListener(e -> {
            String url = manhwa.getPurchaseUrl();
            // ... (rest of the action listener logic remains the same)
            if (url == null || url.trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, 
                    "Purchase link not available for " + manhwa.getTitle(), 
                    "No Link Found", JOptionPane.WARNING_MESSAGE);
                 return;
            }
            
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Could not open link. Please check your browser settings or URL: " + url, 
                        "Error Opening Link", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Browser browsing not supported on this system.\nURL: " + url, 
                    "System Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        infoPanel.add(buyBtn);
        infoPanel.add(Box.createVerticalStrut(10));
        
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Hover effect consistency: Match RecommendationPanel
        card.addMouseListener(new MouseAdapter() {
            private Color originalColor = card.getBackground();
            
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(BG_IMAGE); // Slightly darker on hover
                card.setBorder(BorderFactory.createLineBorder(ACCENT_PRIMARY, 2)); // Accent border
                card.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(originalColor);
                card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Back to empty border
                card.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return card;
    }
}