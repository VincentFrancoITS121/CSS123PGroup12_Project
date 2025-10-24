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
import java.awt.event.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePanel extends JPanel {
    private MainFrame parentFrame;
    private RecommendationEngine recommender;
    private List<Manhwa> featuredManhwa;
    private JLabel carouselTitleLabel, carouselGenreLabel, carouselRatingLabel;
    private JPanel carouselPanel;
    private int currentIndex = 0;
    private Timer carouselTimer;
    private JLabel timerLabel;
    private int countdown = 4;

    // Colors and Fonts for consistency
    private final Color BACKGROUND_DARK = new Color(30, 30, 45);
    private final Color TEXT_LIGHT = new Color(230, 230, 230);
    private final Color TEXT_SUBTLE = new Color(180, 180, 180);
    private final Color ACCENT_PRIMARY = new Color(106, 142, 255);
    private final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 32);
    private final Font BUTTON_FONT = new Font("SansSerif", Font.BOLD, 16);

    public HomePanel(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.recommender = new RecommendationEngine();
        this.featuredManhwa = recommender.getFeaturedManhwa(5); // top 5 featured
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_DARK);

        setupUI();
        setupCarousel();
        startCarousel();
    }

    private void setupUI() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Welcome to Manhwa-Link!", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT_PRIMARY);

        headerPanel.add(title, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Navigation Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_DARK);

        JButton browseBtn = createStyledButton("📚 Browse Manhwa");
        browseBtn.addActionListener(e -> parentFrame.showPanel("BROWSE"));

        JButton recBtn = createStyledButton("✨ Recommendations");
        recBtn.addActionListener(e -> parentFrame.showPanel("RECOMMENDATIONS"));

        JButton shopBtn = createStyledButton("🛒 Shopping Hub");
        shopBtn.addActionListener(e -> parentFrame.showPanel("SHOPPING"));

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
                button.setBackground(TEXT_LIGHT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_PRIMARY);
            }
        });

        return button;
    }

    private void setupCarousel() {
        // Center Carousel Section
        carouselPanel = new JPanel();
        carouselPanel.setLayout(new BoxLayout(carouselPanel, BoxLayout.Y_AXIS));
        carouselPanel.setBackground(BACKGROUND_DARK);
        carouselPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        carouselTitleLabel = new JLabel("", SwingConstants.CENTER);
        carouselTitleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        carouselTitleLabel.setForeground(ACCENT_PRIMARY);

        carouselGenreLabel = new JLabel("", SwingConstants.CENTER);
        carouselGenreLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        carouselGenreLabel.setForeground(TEXT_SUBTLE);

        carouselRatingLabel = new JLabel("", SwingConstants.CENTER);
        carouselRatingLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        carouselRatingLabel.setForeground(new Color(255, 215, 0)); // gold rating

        carouselPanel.add(carouselTitleLabel);
        carouselPanel.add(Box.createVerticalStrut(10));
        carouselPanel.add(carouselGenreLabel);
        carouselPanel.add(Box.createVerticalStrut(5));
        carouselPanel.add(carouselRatingLabel);
        
        // ADD COUNTDOWN TIMER DISPLAY
        timerLabel = new JLabel("Next in 4s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        timerLabel.setForeground(TEXT_SUBTLE);
        carouselPanel.add(Box.createVerticalStrut(10));
        carouselPanel.add(timerLabel);
        add(carouselPanel, BorderLayout.CENTER);
    }

    public void startCarousel() {
    // Only start if timer is not already running
    if (carouselTimer == null && featuredManhwa != null && !featuredManhwa.isEmpty()) {
        countdown = 4; // Reset countdown
        carouselTimer = new Timer();
        carouselTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    countdown--;
                    if (countdown <= 0) {
                        updateCarousel();
                        countdown = 4;
                    }
                    if (timerLabel != null) {
                        timerLabel.setText("Next in " + countdown + "s");
                    }
                });
            }
        }, 0, 1000);
    }
}

    private void updateCarousel() {
        if (featuredManhwa.isEmpty()) return;

        Manhwa current = featuredManhwa.get(currentIndex);
        carouselTitleLabel.setText(current.getTitle());
        carouselGenreLabel.setText("Genre: " + current.getGenre());
        carouselRatingLabel.setText("Rating: " + current.getRating() + " ⭐");

        currentIndex = (currentIndex + 1) % featuredManhwa.size();
    }

    public void stopCarousel() {
        if (carouselTimer != null) {
            carouselTimer.cancel();
            carouselTimer = null;
        }
    }
}
