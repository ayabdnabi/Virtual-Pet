package src;
import javax.swing.*;
import java.awt.*;

/**
 * This class constructs the credits screen UI for the Virtual Pet application.
 *
 * <p>
 * It extends {@link JLayeredPane} to allow layering of background images, UI components, and text. This screen
 * displays the names of the project contributors and references the course and term for which it was developed.
 * It includes a background image, a credit title, and a label describing the project.
 * </p>
 *
 * @author
 * Clair Yu,
 * Aya Abdulnabi
 * @version 1.0
 */

public class CreditScreen extends JLayeredPane {
    /** Font used throughout store UI */
    private Font customFont;
    /** Layout to swap different screens in the game */
    private CardLayout cardLayout;
    /** Main panel that holds all the screens*/
    private JPanel mainPanel;

    /**
     * Constructs the credits screen and initializes all visual components, including
     * the background image, credit information, and the Home button that navigates
     * back to the main screen.
     *
     * @param customFont the custom font to apply to all text components
     * @param cardLayout the CardLayout manager that controls screen switching
     * @param mainPanel the main JPanel container that holds all screens
     */
    public CreditScreen(Font customFont, CardLayout cardLayout, JPanel mainPanel) {
        this.customFont = customFont;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        setPreferredSize(new Dimension(1080, 750));

        // Default background
        ImageIcon background = new ImageIcon("resources/grid.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1080, 750);
        add(backgroundLabel, Integer.valueOf(0));

        ImageIcon creditImage = new ImageIcon("resources/credit_screen.png");

        // Credit screen image
        JLabel creditLabel = new JLabel(creditImage);
        creditLabel.setBounds(0, 0, creditImage.getIconWidth(), creditImage.getIconHeight());
        add(creditLabel, Integer.valueOf(1));

        // Game screen content
        JLabel titleLabel = new JLabel("Credits - Group 19");
        titleLabel.setFont(customFont.deriveFont(28f));
        titleLabel.setForeground(Color.decode("#987e78"));
        titleLabel.setBounds(315, 30, 1000, 50);
        add(titleLabel, Integer.valueOf(2));

        // Text to reprent the semester it was made + which group made it
        JLabel subLabel = new JLabel("\"Virtual Pet\" is a project created for COMPSCI 2212 Winter 2025 at Western University");
        subLabel.setFont(customFont.deriveFont(12f));
        subLabel.setForeground(Color.decode("#d09b62"));
        subLabel.setBounds(70, 70, 1000, 50);
        add(subLabel, Integer.valueOf(4));

        // Home button with styling
        JButton homeButton = MainScreen.buttonCreate(20, 20, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "Home");
        JLabel homeText = new JLabel("< HOME");
        homeText.setFont(customFont);
        homeText.setForeground(Color.BLACK);
        homeText.setBounds(20,20,192,64);
        homeText.setHorizontalAlignment(SwingConstants.CENTER);
        homeText.setVerticalAlignment(SwingConstants.CENTER);
        add(homeText, Integer.valueOf(4));
        homeButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        add(homeButton, Integer.valueOf(4));
    }
}