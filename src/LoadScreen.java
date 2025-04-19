package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;

/**
 * The {@code LoadScreen} class represents the screen where users are able to load/deleted previously saved game files.
 * The JLayeredPane shows all available save files with pet information (health, pet type, and coins) and provides
 * functionality to load or delete saves.
 *
 * <p>Features include:
 * <ul>
 *   <li>Shows up to 3 save files with information about each pet</li>
 *   <li>Loading save files</li>
 *   <li>Deleting save files that the user no longer wants</li>
 *   <li>Navigation back to the main screen</li>
 * </ul>
 *
 * <p>The class extends {@link JLayeredPane} to manage layered displays of the background, buttons, as well as text
 * It also uses {@code GameData}, {@code GameDataManager}, and {@code MainScreen} classes
 *
 * @author
 * Aya Abdulnabi,
 * Mohammed Abdulnabi,
 * Kamaldeep Ghotra
 * @version 1.0
 */
public class LoadScreen extends JLayeredPane {
    /** Custom font used for styling throughout the whole screen */
    private Font customFont;
    /** Directory path where the game files are saved */
    private static final String SAVE_DIR = "saves/";
    /** Path for the default button image used for save files */
    private static final String BUTTON_IMG = "resources/load_file_clicked.png";

    /** Reference to the main panel that has all screens for the card layout navigation */
    private JPanel mainPanel;
    /** Card layout used to transition between different screens */
    private CardLayout cardLayout;

    /**
     * Makes a new load screen with a specific font and navigation controls
     *
     * @param customFont the custom font to be used throughout the screen
     * @param mainPanel the main panel containing all screens for card layout navigation
     * @param cardLayout the card layout manager for screen transitions
     */
    public LoadScreen(Font customFont, JPanel mainPanel, CardLayout cardLayout) {
        this.customFont = customFont; // store the custom font
        this.mainPanel = mainPanel; // store the main panel
        this.cardLayout = cardLayout; // store the layout
        setPreferredSize(new Dimension(1080, 750)); // preferred size matches game window resolution

        // Background setup
        ImageIcon background = new ImageIcon("resources/grid.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1080, 750);
        add(backgroundLabel, Integer.valueOf(0));

        // Main load screen overlay
        ImageIcon fileLoad = new ImageIcon("resources/save_load_screen.png");
        Image scaledLoad = fileLoad.getImage().getScaledInstance(1080, 750, Image.SCALE_SMOOTH);
        ImageIcon scaledLoadIcon = new ImageIcon(scaledLoad);
        JLabel loadLabel = new JLabel(scaledLoadIcon);
        loadLabel.setBounds(0, 0, 1080, 750);
        add(loadLabel, Integer.valueOf(1)); // Add this layer above the grid background

        // "LOAD" text overlay
        JLabel loadText = new JLabel("LOAD");
        loadText.setForeground(Color.WHITE);
        loadText.setFont(customFont.deriveFont(24f));
        // Fix the position of the text and set the vertical and horizontal alignments to the centre
        loadText.setBounds(800, 70, 192, 64);
        loadText.setVerticalAlignment(SwingConstants.CENTER);
        loadText.setHorizontalAlignment(SwingConstants.CENTER);
        add(loadText, Integer.valueOf(2)); // Add it above the load screen image

        // Delete save button
        JButton deleteButton = MainScreen.buttonCreate(540, 70, 192, 64, "resources/button.png", "resources/button_clicked.png", "");
        deleteButton.addActionListener(e -> promptDeleteSave());
        add(deleteButton, Integer.valueOf(2));

        // "DELETE" text overlay
        JLabel deleteText = new JLabel("DELETE SAVE");
        deleteText.setFont(customFont);
        deleteText.setForeground(Color.WHITE);
        deleteText.setBounds(540,70,192,64);
        deleteText.setHorizontalAlignment(SwingConstants.CENTER);
        deleteText.setVerticalAlignment(SwingConstants.CENTER);
        add(deleteText, Integer.valueOf(3));

        // Home button setup and "HOME" text overlay
        JButton homeButton = MainScreen.buttonCreate(20, 20, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "Home");
        JLabel homeText = new JLabel("< HOME");
        homeText.setFont(customFont);
        homeText.setForeground(Color.BLACK);
        homeText.setBounds(20,20,192,64);
        homeText.setHorizontalAlignment(SwingConstants.CENTER);
        homeText.setVerticalAlignment(SwingConstants.CENTER);
        add(homeText, Integer.valueOf(2));
        add(homeButton, Integer.valueOf(2));

        // Load the save files and display them
        File[] saveFiles = getSaveFiles();

        // Create UI elements for each save file (there can only be 3 max)
        for (int i = 0; i < Math.min(3, saveFiles.length); i++) {
            final String filePath = saveFiles[i].getAbsolutePath();

            // Load game data from the file path
            GameData gameData = GameDataManager.loadGame(filePath);
            if (gameData != null) {
                // Get the pet name, the health, as well as coins
                String petName = gameData.getPet().getName();
                int petHealth = gameData.getPet().getHealth();
                int playerCoins = gameData.getInventory().getPlayerCoins();

                // Create a scaled version of the default load button
                ImageIcon defaultIcon = scaleImageIcon(BUTTON_IMG, 798, 138);

                // Determine the type of pet and display a specific icon depending on the type
                String petIconLocation = "";
                if (Objects.equals(gameData.getPet().getPetType(), "PetOption1")){
                    petIconLocation = "resources/PetOption1Icon.PNG";
                } else if (Objects.equals(gameData.getPet().getPetType(), "PetOption2")){
                    petIconLocation = "resources/PetOption2Icon.PNG";
                } else {
                    petIconLocation = "resources/PetOption3Icon.PNG";
                }

                // Create, scale, and position the icon
                ImageIcon petIcon = scaleImageIcon(petIconLocation, 220, 200);
                JLabel petIconLabel = new JLabel(petIcon);
                petIconLabel.setBounds(100, 135 + (i * 170), 200, 200);
                add(petIconLabel, Integer.valueOf(3));

                // Create interactive save file button
                final JButton saveButton = new JButton(defaultIcon);
                saveButton.setBounds(141, 174 + (i * 170), 798, 138);
                saveButton.setBorderPainted(false);
                saveButton.setContentAreaFilled(false);
                saveButton.setFocusPainted(false);

                // Add a click handler in order to load saves
                saveButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Play sound effect
                        MusicPlayer.playSoundEffect("resources/button_clicked.wav");
                        if (!MainScreen.getParentalControl().isPlayAllowedNow()) {
                            // Check the parental control screen
                            showStyledDialog("Playtime Restricted",
                                    "Playtime is currently restricted.\nPlease try again during allowed hours.");
                            return;
                        }

                        // Load and switch to game if successful
                        GameData loadedGame = GameDataManager.loadGame(filePath);
                        if (loadedGame != null) {
                            switchToInGameScreen(loadedGame, filePath);
                        }
                    }
                });

                add(saveButton, Integer.valueOf(2));

                // Create a panel for pet stats to display
                JPanel labelPanel = new JPanel(new GridBagLayout());
                labelPanel.setOpaque(false);
                labelPanel.setBounds(saveButton.getBounds());

                // Create a new GridBagConstrains to control the component layout
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = GridBagConstraints.RELATIVE;
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.insets = new Insets(2, 0, 2, 0);

                // Pet name label
                JLabel nameLabel = new JLabel(petName);
                nameLabel.setFont(customFont.deriveFont(Font.BOLD, 20f));
                nameLabel.setForeground(Color.decode("#7392B2"));

                // Pet health stats label
                JLabel healthLabel = new JLabel("Health: " + petHealth);
                healthLabel.setFont(customFont.deriveFont(16f));
                healthLabel.setForeground(Color.BLACK);

                // Coin amount label
                JLabel coinsLabel = new JLabel("Coins: " + playerCoins);
                coinsLabel.setFont(customFont.deriveFont(16f));
                coinsLabel.setForeground(Color.BLACK);

                // Add all labels to the panel
                labelPanel.add(nameLabel, gbc);
                labelPanel.add(healthLabel, gbc);
                labelPanel.add(coinsLabel, gbc);

                add(labelPanel, Integer.valueOf(3));
            }
        }
    }

    /**
     * Helper method used to scale an image icon to specified dimensions
     *
     * @param path file path to the original image
     * @param width desired output width
     * @param height desired output height
     * @return scaled ImageIcon instance
     * @author Mohammed Abdulnabi
     */
    private ImageIcon scaleImageIcon(String path, int width, int height) {
        // Get the image from the specified path and scale it
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Retrieves all save files from the save directory
     *
     * @return array of File objects representing save files (empty array if none found)
     * @author Mohammed Abdulnabi
     */
    private File[] getSaveFiles() {
        File dir = new File(SAVE_DIR);
        if (dir.exists() && dir.isDirectory()) {
            return dir.listFiles((dir1, name) -> name.endsWith(".json"));
        }
        return new File[0];
    }

    /**
     * Transitions to the game screen with loaded game data
     *
     * @param gameData the loaded game state
     * @param filePath path to the save file for future autosaving
     * @author Mohammed Abdulnabi
     */
    private void switchToInGameScreen(GameData gameData, String filePath) {
        InGameScreen inGameScreen = new InGameScreen(customFont, cardLayout, mainPanel, gameData, filePath);
        mainPanel.add(inGameScreen, "InGameScreen");
        cardLayout.show(mainPanel, "InGameScreen");
    }

    /**
     * Initiates the save file deletion process with user prompts
     *
     * @author Aya Abdulnabi
     */
    private void promptDeleteSave() {
        File[] saveFiles = getSaveFiles();

        // Hnadle cases with no saves
        if (saveFiles.length == 0) {
            showStyledDialog("Delete Save", "No save files found.");
            return;
        }

        // Extract save file name
        String[] saveNames = new String[saveFiles.length];
        for (int i = 0; i < saveFiles.length; i++) {
            saveNames[i] = saveFiles[i].getName();
        }

        // Create selection dialog panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 240));
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + "<font size=4 color='#2E86C1'><b>Select a save file to delete:</b></font></div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        // Create a dropdown that contains all the save files
        JComboBox<String> saveComboBox = new JComboBox<>(saveNames);
        saveComboBox.setFont(customFont.deriveFont(14f));
        panel.add(saveComboBox, BorderLayout.SOUTH);

        // Show the different options of the save files
        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Delete Save",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Select", "Cancel"},
                "Select"
        );

        // If the user does not enter ok, leave
        if (option != JOptionPane.OK_OPTION) return;
        String selectedFile = (String) saveComboBox.getSelectedItem();
        if (selectedFile == null) return;

        // Create confirmation dialog
        JPanel confirmPanel = new JPanel(new BorderLayout(10, 10));
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        confirmPanel.setBackground(new Color(240, 240, 240));
        JLabel confirmLabel = new JLabel("<html><div style='text-align: center;'>" + "<font size=4 color='#2E86C1'><b>Confirm Delete</b></font><br>" + "<font size=3 color='#5D6D7E'>Are you sure you want to delete " + selectedFile + "?</font></div></html>");
        confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmPanel.add(confirmLabel, BorderLayout.CENTER);

        // Create a confirmation button
        JButton yesButton = new JButton("Yes");
        yesButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        yesButton.setBackground(new Color(52, 152, 219));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFocusPainted(false);
        yesButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Create a no confirmation button
        JButton noButton = new JButton("No");
        noButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        noButton.setBackground(new Color(231, 76, 60));
        noButton.setForeground(Color.WHITE);
        noButton.setFocusPainted(false);
        noButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Add the buttons to the panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        confirmPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create a show a confirm dialog
        JDialog confirmDialog = new JDialog((Frame)null, "Confirm Delete", true);
        confirmDialog.setContentPane(confirmPanel);
        confirmDialog.setSize(350, 200);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setResizable(false);

        // Create a boolean array to track the confirmation status
        final boolean[] confirmed = {false};
        yesButton.addActionListener(e -> {
            confirmed[0] = true;
            confirmDialog.dispose();
        });
        noButton.addActionListener(e -> confirmDialog.dispose());
        confirmDialog.setVisible(true);

        // Handle a confirmed deletion
        if (confirmed[0]) {
            File fileToDelete = new File(SAVE_DIR + selectedFile);
            if (fileToDelete.delete()) {
                showStyledDialog("Delete Save", "Save file deleted successfully.");
                refreshLoadScreen();
            } else {
                showStyledDialog("Delete Save", "Failed to delete save file.");
            }
        }
    }

    /**
     * Displays a styled dialog box with title and message
     *
     * @param title the dialog title text
     * @param message the main dialog content
     * @author Aya Abdulnabi
     */
    private void showStyledDialog(String title, String message) {
        // Create a new JPanel with BorderLayout as its layout manager
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 240));

        // Create a formatted message label
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + "<font size=4 color='#2E86C1'><b>" + title + "</b></font><br>" + "<font size=3 color='#5D6D7E'>" + message + "</font></div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        // Create an OK button with the specific font and what it does
        JButton okButton = new JButton("OK");
        okButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        okButton.setBackground(new Color(52, 152, 219));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        okButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(panel);
            if (window != null) {
                window.dispose();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(okButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Create and show dialog
        JDialog dialog = new JDialog((Frame)null, title, true);
        dialog.setContentPane(panel);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    /**
     * Refreshes the load screen to reflect any changes (e.g. after deletion)
     *
     * @author Kamaldeep Ghorta
     */
    private void refreshLoadScreen() {
        // remove the current instance
        mainPanel.remove(this);

        // Create a new instance
        LoadScreen newLoadScreen = new LoadScreen(customFont, mainPanel, cardLayout);

        // add the new load screen into the panel
        mainPanel.add(newLoadScreen, "LoadScreen");

        // Switch to the updated LoadScreen
        cardLayout.show(mainPanel, "LoadScreen");

        // Force the UI to update
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
