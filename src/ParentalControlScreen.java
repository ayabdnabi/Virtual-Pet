package src;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code ParentalControlScreen} class represents the parental control settings screen in the game.
 * It provides functionality for monitoring and restricting
 * play time, reviving pets, and resetting game statistics.
 *
 * <p>Key features:
 * <ul>
 *   <li>Display total and average play time stats</li>
 *   <li>Controls for setting play time restrictions</li>
 *   <li>Pet revival</li>
 *   <li>Stats reset capability</li>
 * </ul>
 *
 * <p>This class extends {@link JLayeredPane} to manage layered display of UI elements
 * and interacts with the {@link ParentalControl} class for core functionality.
 *
 * @author Aya Abdulnabi
 * @author Mohammed Abdulnabi
 * @author Kamaldeep Ghotra
 * @version 1.0
 */
public class ParentalControlScreen extends JLayeredPane {
    /** Custom font used throughout the screen */
    private Font customFont;
    /** Reference to the parental control manager*/
    private ParentalControl parentalControl;
    /** Label to display the total play time value*/
    private JLabel totalPlayValue;
    /** Label to display the average play time value*/
    private JLabel avgPlayValue;

    /**
     * Constructs a new ParentalControlScreen with the specified parameters
     *
     * @param customFont The custom font to use for text elements
     * @param cardLayout The card layout manager for screen transitions
     * @param mainPanel The main panel containing all screens
     * @param parentalControl The parental control manager instance
     *
     */
    public ParentalControlScreen(Font customFont, CardLayout cardLayout, JPanel mainPanel, ParentalControl parentalControl) {
        // Set font and dimensions of the screen
        this.customFont = customFont;
        setPreferredSize(new Dimension(1080, 750));
        this.parentalControl = parentalControl;

        // Background setup
        ImageIcon gridBackground = new ImageIcon("resources/grid.png");
        Image scaledGrid = gridBackground.getImage().getScaledInstance(1080, 750, Image.SCALE_SMOOTH);
        JLabel gridLabel = new JLabel(new ImageIcon(scaledGrid));
        gridLabel.setBounds(0, 0, 1080, 750);
        add(gridLabel, Integer.valueOf(0));

        // Add parental control window on top of grid
        ImageIcon windowBg = new ImageIcon("resources/parentalcontrol_background.png");
        Image scaledWindowBg = windowBg.getImage().getScaledInstance(1011, 600, Image.SCALE_SMOOTH);
        JLabel windowLabel = new JLabel(new ImageIcon(scaledWindowBg));
        windowLabel.setBounds(0, 60, 1080, 750);
        add(windowLabel, Integer.valueOf(1));


        // Back button to take you back to the main screen with styling
        JButton homeButton = MainScreen.buttonCreate(20, 20, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "Home");
        add(homeButton, Integer.valueOf(1));
        JLabel homeText = new JLabel("< HOME");
        homeText.setFont(customFont);
        homeText.setForeground(Color.BLACK);
        homeText.setBounds(20,20,192,64);
        homeText.setHorizontalAlignment(SwingConstants.CENTER);
        homeText.setVerticalAlignment(SwingConstants.CENTER);
        add(homeText, Integer.valueOf(4));

        // Labels to display total play time and average play time
        JLabel totalPlayLabel = createLabel("TOTAL PLAY TIME:", 240, 245, 300, 40);
        totalPlayValue = createLabel(formatMillis(parentalControl.getTotalPlayTime()), 250, 328, 400, 40);
        totalPlayValue.setFont(customFont.deriveFont(40f));

        JLabel avgPlayLabel = createLabel("AVERAGE PLAY TIME", 220, 419, 300, 40);
        avgPlayValue = createLabel(formatMillis(parentalControl.getAveragePlayTime()), 250, 498, 500, 40);
        avgPlayValue.setFont(customFont.deriveFont(40f));

        // Add all play time statistic labels to the layered pane
        add(totalPlayLabel, Integer.valueOf(2));
        add(totalPlayValue, Integer.valueOf(2));
        add(avgPlayLabel, Integer.valueOf(2));
        add(avgPlayValue, Integer.valueOf(2));

        updateStatLabels();

        // Resets stats, set play time, revive pet button and styling
        JButton resetStatsButton = MainScreen.buttonCreate(620,190, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "");
        resetStatsButton.setText("RESET STATS");
        resetStatsButton.setFont(customFont.deriveFont(13f));
        resetStatsButton.setForeground(Color.decode("#7392B2"));
        resetStatsButton.setHorizontalTextPosition(SwingConstants.CENTER);
        resetStatsButton.setVerticalTextPosition(SwingConstants.CENTER);

        JButton setPlayTimeButton = MainScreen.buttonCreate(620, 320, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "");
        setPlayTimeButton.setText("SET PLAY TIME");
        setPlayTimeButton.setFont(customFont.deriveFont(12f));
        setPlayTimeButton.setForeground(Color.decode("#7392B2"));
        setPlayTimeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        setPlayTimeButton.setVerticalTextPosition(SwingConstants.CENTER);

        JButton revivePetButton = MainScreen.buttonCreate(620, 440, 192, 62, "resources/white_button.png", "resources/white_button_clicked.png", "");
        revivePetButton.setText("REVIVE PET");
        revivePetButton.setFont(customFont.deriveFont(13f));
        revivePetButton.setForeground(Color.decode("#7392B2"));
        revivePetButton.setHorizontalTextPosition(SwingConstants.CENTER);
        revivePetButton.setVerticalTextPosition(SwingConstants.CENTER);

        // Add all labels to the layered pane
        add(resetStatsButton, Integer.valueOf(2));
        add(setPlayTimeButton, Integer.valueOf(2));
        add(revivePetButton, Integer.valueOf(2));

        // Reset play time button logic
        resetStatsButton.addActionListener(e -> {
            parentalControl.resetStats();
            GameDataManager.saveParentalControlSettings(parentalControl);
            updateStatLabels();
            JOptionPane.showMessageDialog(this, "Play time statistics have been reset.");
        });

        // Action listener in order to get the starting and ending hour of the allowed play time
        setPlayTimeButton.addActionListener(e -> {
            try {
                String startStr = JOptionPane.showInputDialog(this, "Enter allowed start hour (0–23):");
                String endStr = JOptionPane.showInputDialog(this, "Enter allowed end hour (0–23):");

                // store them
                int startHour = Integer.parseInt(startStr);
                int endHour = Integer.parseInt(endStr);

                // Show the user the start and end time of the window
                parentalControl.setPlayTimeWindow(startHour, endHour);
                parentalControl.setLimitationsEnabled(true);
                GameDataManager.saveParentalControlSettings(parentalControl);
                JOptionPane.showMessageDialog(this,
                        "Play time window set from " + startHour + ":00 to " + endHour + ":00.",
                        "Play Time Set",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid hours (0–23).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        // Reset play time button styling
        JButton resetPlayTimeButton = MainScreen.buttonCreate(620, 560, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "");
        resetPlayTimeButton.setText("RESET PLAY TIME");
        resetPlayTimeButton.setFont(customFont.deriveFont(11f));
        resetPlayTimeButton.setForeground(Color.decode("#7392B2"));
        resetPlayTimeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        resetPlayTimeButton.setVerticalTextPosition(SwingConstants.CENTER);

        // Reset playtime logic
        resetPlayTimeButton.addActionListener(e -> {
            parentalControl.resetPlayTimeRestrictions();
            GameDataManager.saveParentalControlSettings(parentalControl);
            JOptionPane.showMessageDialog(this,
                    "Play time restrictions have been reset.\nPlay is now allowed at any time.",
                    "Reset Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        add(resetPlayTimeButton, Integer.valueOf(2));

        // Revive pet logic
        revivePetButton.addActionListener(e -> {
            // Let the user select a save file
            String saveFile = selectSaveFile();
            if (saveFile == null) {
                JOptionPane.showMessageDialog(null, "No save file selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Load the pet from the save file
            GameData gameData = GameDataManager.loadGame(saveFile);
            if (gameData == null || gameData.getPet() == null) {
                JOptionPane.showMessageDialog(null, "Failed to load pet from save file.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Pet pet = gameData.getPet();


            // Try reviving the pet using parental controls
            boolean revived = parentalControl.revivePet(pet);

            // Show the result to the user
            if (revived) {
                JOptionPane.showMessageDialog(null, "Pet successfully revived!", "Success", JOptionPane.INFORMATION_MESSAGE);
                GameDataManager.saveGame(saveFile, pet, gameData.getInventory(), gameData.getTotalPlayTime()); // Save updated pet state
            } else {
                JOptionPane.showMessageDialog(null, "The pet is not dead and doesn't need revival.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        setVisible(true);
    }

    /**
     * Creates a styled label with the specified parameters
     *
     * @param text The text to display
     * @param x The x-coordinate position
     * @param y The y-coordinate position
     * @param width The label width
     * @param height The label height
     * @return The configured JLabel instance
     *
     * @author Kamaldeep Ghorta
     */
    private JLabel createLabel(String text, int x, int y, int width, int height) {
        // Create a new label with styling
        JLabel label = new JLabel(text);
        label.setFont(customFont);
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, width, height);
        return label;
    }

    /**
     * Opens a file chooser dialog to select a save file
     *
     * @return The absolute path of the selected file, or null if none selected
     *
     * @author Kamaldeep Ghorta
     */
    private String selectSaveFile() {
       // Ask user to choose a save file to, used for revival pet
        JFileChooser fileChooser = new JFileChooser("saves/");
        fileChooser.setDialogTitle("Choose Save File");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    /**
     * Formats milliseconds into minutes + seconds.
     *
     * @param millis The time in milliseconds
     * @return Formatted string in "Xm Ys" format
     *
     * @author Kamaldeep Ghorta
     */
    private String formatMillis(long millis) {
        // Convert milliseconds to seconds, minutes from seconds, calcualte remaining minutes and return format
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return minutes + "m " + remainingSeconds + "s";
    }

    /**
     * Updates the statistics labels with current values from parental controls
     *
     */
    public void updateStatLabels() {
        totalPlayValue.setText(formatMillis(parentalControl.getTotalPlayTime()));
        avgPlayValue.setText(formatMillis(parentalControl.getAveragePlayTime()));
    }

}
