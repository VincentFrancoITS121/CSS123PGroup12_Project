package cssgroup12finalproject;

import javax.swing.*;
import java.awt.*;

public class ManhwaLinkApp {
    public static void main(String[] args) {
        // --- PROFESSIONAL UI ENHANCEMENT: Set Nimbus Look and Feel ---
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Fallback to default L&F if Nimbus is unavailable
            System.err.println("Could not set Nimbus Look and Feel. Using default.");
        }
        
        SwingUtilities.invokeLater(() -> {
            // Set global defaults for a clean look
            UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
            UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 12));
            UIManager.put("TitledBorder.font", new Font("Arial", Font.BOLD, 18));
            
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
