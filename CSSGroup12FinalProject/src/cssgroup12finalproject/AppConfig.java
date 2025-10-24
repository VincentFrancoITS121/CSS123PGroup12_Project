/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cssgroup12finalproject;

import java.awt.*;

public class AppConfig {
    // === COLORS ===
    public static final Color BG_DARK = new Color(30, 30, 45);
    public static final Color BG_CARD = new Color(55, 55, 75);
    public static final Color BG_IMAGE = new Color(45, 45, 60);
    public static final Color ACCENT_PRIMARY = new Color(106, 142, 255);
    public static final Color ACCENT_SECONDARY = new Color(96, 227, 176);
    public static final Color TEXT_LIGHT = new Color(230, 230, 230);
    public static final Color TEXT_SUBTLE = new Color(180, 180, 180);
    public static final Color GOLD = new Color(255, 215, 0);
    public static final Color DISCOUNT_RED = new Color(255, 99, 71);
    
    // === FONTS ===
    public static final Font TITLE_LARGE = new Font("SansSerif", Font.BOLD, 32);
    public static final Font TITLE_MEDIUM = new Font("SansSerif", Font.BOLD, 28);
    public static final Font TITLE_SMALL = new Font("SansSerif", Font.BOLD, 26);
    public static final Font CARD_TITLE = new Font("SansSerif", Font.BOLD, 18);
    public static final Font BUTTON = new Font("SansSerif", Font.BOLD, 16);
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font SMALL = new Font("SansSerif", Font.PLAIN, 12);
    
    // === LAYOUT ===
    public static final int CARD_WIDTH = 250;
    public static final int CARD_HEIGHT = 380;
    public static final int SHOP_CARD_HEIGHT = 400;
    public static final int GRID_COLUMNS = 4;
    public static final int GRID_GAP = 25;
    public static final int PADDING = 30;
    
    // === TIMING ===
    public static final int CAROUSEL_INTERVAL_MS = 4000;
    public static final int COUNTDOWN_INTERVAL_MS = 1000;
    
    // === PANEL NAMES ===
    public static final String PANEL_HOME = "HOME";
    public static final String PANEL_RECOMMENDATIONS = "RECOMMENDATIONS";
    public static final String PANEL_SHOPPING = "SHOPPING_HUB";
    
    private AppConfig() {} // Prevent instantiation
}
