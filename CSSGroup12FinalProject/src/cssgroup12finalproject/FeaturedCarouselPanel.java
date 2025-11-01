package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeaturedCarouselPanel extends JPanel {
    private RecommendationEngine engine;
    private List<Manhwa> featured;
    private int currentIndex = 0;

    private float fadeAlpha = 1.0f; // 1.0 => fully showing current, 0 => fully showing next
    private boolean isFading = false;

    private Timer rotationTimer; // controls when to switch items
    private Timer fadeTimer;     // controls animation frames

    private final int rotationDelayMs = 5000; // show each item for 5s
    private final int fadeFrameMs = 40; // ~25 FPS
    private final int fadeDurationMs = 400; // fade total duration
    private final int fadeSteps = Math.max(1, fadeDurationMs / fadeFrameMs);

    // visual constants
    private final Color bg = new Color(45, 45, 60);
    private final Color titleColor = new Color(106, 142, 255);
    private final Color textColor = new Color(230, 230, 230);

    public FeaturedCarouselPanel() {
        this.engine = new RecommendationEngine();
        setBackground(bg);
        setPreferredSize(new Dimension(800, 400));
        loadFeatured();
        setupTimers();
    }

    private void loadFeatured() {
        this.featured = engine.getFeaturedManhwa(6);
        if (featured == null || featured.isEmpty()) {
            featured = engine.getRecommendations(null, null, null);
        }
        currentIndex = 0;
    }

    private void setupTimers() {
        rotationTimer = new Timer(rotationDelayMs, e -> startFadeToNext());
        fadeTimer = new Timer(fadeFrameMs, new FadeStep());

        if (!featured.isEmpty()) {
            rotationTimer.start();
        }
    }

    private void startFadeToNext() {
        if (isFading || featured.size() <= 1) return;
        isFading = true;
        fadeAlpha = 0.0f;
        fadeTimer.start();
    }

    private class FadeStep implements ActionListener {
        int step = 0;
        @Override
        public void actionPerformed(ActionEvent e) {
            step++;
            fadeAlpha = Math.min(1.0f, (float)step / (float)fadeSteps);
            repaint();
            if (step >= fadeSteps) {
                // finish fade: advance index
                fadeTimer.stop();
                step = 0;
                isFading = false;
                currentIndex = (currentIndex + 1) % featured.size();
                fadeAlpha = 1.0f;
                repaint();
            }
        }
    }

    public void start() {
        if (rotationTimer != null && !rotationTimer.isRunning()) rotationTimer.start();
    }

    public void stop() {
        if (rotationTimer != null && rotationTimer.isRunning()) rotationTimer.stop();
        if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (featured == null || featured.isEmpty()) {
            g.setColor(textColor);
            g.drawString("No manhwa available", 20, 20);
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        // draw current item and (if fading) the next item with alpha compositing
        Manhwa current = featured.get(currentIndex);
        Manhwa next = featured.get((currentIndex + 1) % featured.size());

        // Draw current at alpha = (1 - fadeAlpha) when fading, else 1.0
        float alphaCurrent = isFading ? 1.0f - fadeAlpha : 1.0f;
        float alphaNext = isFading ? fadeAlpha : 0.0f;

        // background
        g2.setColor(getBackground());
        g2.fillRect(0,0,getWidth(),getHeight());

        // draw both layers using composite
        if (alphaCurrent > 0.001f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaCurrent));
            drawItem(g2, current);
        }
        if (alphaNext > 0.001f) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaNext));
            drawItem(g2, next);
        }

        g2.dispose();
    }

    // simple draw routine — shows title, author, genre, rating, description (clamped)
    private void drawItem(Graphics2D g2, Manhwa m) {
        int w = getWidth();
        int h = getHeight();
        int padding = 30;
        int x = padding;
        int y = padding;
        
        // Image implementation: Calculate dimensions and draw the cover image
        int imageWidth = 250; 
        int imageHeight = h - 2*padding;
        int imageX = w - imageWidth - padding;
        
        // Get the Image object from the MainFrame mock utility
        Image coverImage = MainFrame.toManhwaCoverIcon(
                m.getCoverImagePath(), imageWidth, imageHeight).getImage();
        
        // Draw the image on the right
        g2.drawImage(coverImage, imageX, y, imageWidth, imageHeight, null);
        
        // Set the text max width to stop before the image
        int textMaxWidth = imageX - x - padding;

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Title
        g2.setFont(new Font("SansSerif", Font.BOLD, 32));
        g2.setColor(titleColor);
        g2.drawString(m.getTitle(), x, y + 32);

        // Author & Genre
        g2.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g2.setColor(textColor);
        g2.drawString("by " + m.getAuthor() + " • " + m.getGenre() + " | " + m.getDemographic(), x, y + 70);

        // Rating
        g2.setFont(new Font("SansSerif", Font.BOLD, 22));
        g2.setColor(new Color(255, 215, 0));
        g2.drawString("★ " + m.getRating(), x, y + 110);

        // Description (wrap)
        g2.setFont(new Font("SansSerif", Font.ITALIC, 14));
        g2.setColor(textColor);

        int descX = x;
        int descY = y + 140;
        int maxWidth = textMaxWidth; // Use the updated width
        String desc = m.getDescription();
        drawStringWrapped(g2, desc, descX, descY, maxWidth, 18);
    }

    private void drawStringWrapped(Graphics2D g2, String text, int x, int y, int maxWidth, int lineHeight) {
        if (text == null) return;
        FontMetrics fm = g2.getFontMetrics();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        int curY = y;
        for (String word : words) {
            String test = line.length() == 0 ? word : line + " " + word;
            if (fm.stringWidth(test) > maxWidth) {
                g2.drawString(line.toString(), x, curY);
                curY += lineHeight;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(test);
            }
            // limit to ~6 lines to avoid overflow
            if ((curY - y) / lineHeight > 6) {
                g2.drawString(line.toString() + "...", x, curY);
                return;
            }
        }
        if (line.length() > 0) {
            g2.drawString(line.toString(), x, curY);
        }
    }
}