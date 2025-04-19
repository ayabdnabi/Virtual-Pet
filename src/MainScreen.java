package src;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The {@code MainScreen} class represents the entry point for the game.
 * It manages all screen transitions, UI components, and core game functionality.
 *
 * <p>Key responsibilities include:
 * <ul>
 *   <li>Initializing and managing all game screens (Home, Tutorial, New Game, etc.)</li>
 *   <li>Handles transitions between screens using CardLayout</li>
 *   <li>Managing fonts, sounds, and visual assets</li>
 *   <li>Implementing parental control features</li>
 *   <li>Providing utility methods for UI component creation</li>
 * </ul>
 *
 * <p>The class extends {@link JFrame} and serves as the container for all
 * game screens. It integrates other classes like {@code TutorialScreen},
 * {@code ParentalControl}, and {@code MusicPlayer}.
 *
 * @author Aya Abdulnabi
 * @author Mohammed Abdulnabi
 * @author Kamaldeep Ghotra
 * @author Clair Yu
 * @version 1.0
 */
public class MainScreen extends JFrame {
    /** Layout manager for switching between different screens */
    private static CardLayout cardLayout;
    /** Main container panel that holds all the screens */
    private static JPanel mainPanel;
    /** Custom font used throughout the entire class */
    private static Font customFont;
    /** Label for the password popup (parental controls) */
    private static JLabel passwordLabel;
    /** Overlay for dialogs */
    private static JLabel overlayLabel;
    /** Reference to tutorial screen instance */
    private static TutorialScreen tutorialScreen;
    /** Parental control manager instance */
    private static ParentalControl parentalControl;
    /** Reference to parental control screen instance */
    private static ParentalControlScreen parentalControlScreen;
    /** Sound clip for button click sound effect*/
    private static Clip buttonClickSound;
    /** Current game screen instance */
    private static InGameScreen inGameScreen;

    /**
     * Constructs the main application window and initializes all components.
     * Performs the following setup:
     * 1. Loads custom font
     * 2. Initializes sound effects
     * 3. Sets up background music
     * 4. Creates all game screens
     * 5. Configures window properties
     *
     * @author Aya Abdulnabi
     * @author Mohammed Abdulnabi
     * @author Kamaldeep Ghotra
     */
    MainScreen() {
        // Try-catch statement in order to load the font
        try {
            InputStream fontStream = getClass().getResourceAsStream("/Early GameBoy.ttf");
            if (fontStream == null) {
                throw new IOException("Font file not found!"); // Error message thrown if font not found
            }
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            // if font fails to load, fallback to default
            customFont = new Font("Arial", Font.PLAIN, 24); // Fallback font
        }

        // Try-catch statement to load in the button-click sound effect
        try {
            InputStream soundStream = getClass().getResourceAsStream("/button_clicked.wav");
            if (soundStream != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(soundStream));
                buttonClickSound = AudioSystem.getClip();
                buttonClickSound.open(audioInputStream);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Could not load button click sound");
        }


        // Background music player
        MusicPlayer.playBackgroundMusic("background_music.wav");
        MusicPlayer.setVolume(0.2f);

        // Window configuration
        this.setTitle("Virtual Pet");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1080, 750);

        // Screen management setup
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Creation of the actual screens
        JLayeredPane homeScreen = createMainScreen();
        tutorialScreen = new TutorialScreen(customFont);
        JLayeredPane newGameScreen = new NewGameScreen(customFont, cardLayout, mainPanel);
        JLayeredPane loadScreen = new LoadScreen(customFont, mainPanel, cardLayout);
        JLayeredPane creditScreen = new CreditScreen(customFont, cardLayout, mainPanel);
        Store store = GameDataManager.getSharedStore();

        // Adding the screens to the main panel
        mainPanel.add(homeScreen, "Home");
        mainPanel.add(tutorialScreen, "Tutorial");
        mainPanel.add(newGameScreen, "New Game");
        mainPanel.add(creditScreen, "Credit");

        // Parental control setup
        parentalControl = GameDataManager.loadParentalControlSettings();
        parentalControlScreen = new ParentalControlScreen(customFont, cardLayout, mainPanel, parentalControl);
        mainPanel.add(parentalControlScreen, "ParentalControlScreen");
        mainPanel.add(parentalControlScreen, "ParentalControlScreen");
        this.add(mainPanel);

        // Show the home screen by default
        cardLayout.show(mainPanel, "Home");

        // Setting up the window icon
        ImageIcon iconImage = new ImageIcon("resources/Purple.png");
        this.setIconImage(iconImage.getImage());

        this.setVisible(true);
    }

    /**
     * Creates a button with specified images + sounds
     *
     * @param x Horizontal position of the button
     * @param y Vertical position of the button
     * @param width Width of the button
     * @param height Height of the button
     * @param defaultImageSource Path to default button image
     * @param pressedImageSource Path to pressed state image
     * @param location Screen location to navigate to when clicked
     * @return Configured JButton instance
     *
     */
    public static JButton buttonCreate(int x, int y, int width, int height, String defaultImageSource, String pressedImageSource, String location) {
        // Load the default image and the image when pressed
        ImageIcon defaultImage = new ImageIcon(defaultImageSource);
        ImageIcon pressedImage = new ImageIcon(pressedImageSource);
        JButton buttonLabel = new JButton(defaultImage);
        buttonLabel.setBounds(x, y, width, height);

        // Visual styling of the button
        buttonLabel.setBorderPainted(false);
        buttonLabel.setContentAreaFilled(false);
        buttonLabel.setFocusPainted(false);

        // For press/release effect
        buttonLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Change to the "pressed" image when mouse is pressed
                buttonLabel.setIcon(pressedImage);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Revert to the default image when mouse is released
                buttonLabel.setIcon(defaultImage);
            }
        });

        // Play sound effect when button is clicked
        buttonLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MusicPlayer.playSoundEffect("button_clicked.wav");
                if (location.equals("Load")) {
                    // Refresh load screen
                    Component[] components = mainPanel.getComponents();
                    for (int i = 0; i < components.length; i++) {
                        if (components[i] instanceof LoadScreen) {
                            mainPanel.remove(components[i]);
                            break;
                        }
                    }

                    // Rebuild LoadScreen with updated data
                    JLayeredPane refreshedLoadScreen = new LoadScreen(customFont, mainPanel, cardLayout);
                    mainPanel.add(refreshedLoadScreen, "Load");
                }

                cardLayout.show(mainPanel, location);
            }
        });

        return buttonLabel;
    }

    /**
     * Creates a text label for the button
     *
     * @param text The text to display
     * @param x Horizontal position
     * @param y Vertical position
     * @param width Label width
     * @param height Label height
     * @return Configured JLabel instance
     *
     * @author Clair Yu
     */
    private static JLabel buttonText(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(customFont);
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, width, height);

        return label;
    }

    /**
     * Creates and lays out the main screen with all the UI elements
     *
     * @return Configured JLayeredPane containing all home screen components
     * @author Aya Abdulnabi
     * @author Mohammed Abdulnabi
     * @author Clair Yu
     */
    private static JLayeredPane createMainScreen() {
        // Create the layered pane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1080, 750));


        // Set up the background for the main screen
        ImageIcon background = new ImageIcon("resources/grid.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1080, 750);
        layeredPane.add(backgroundLabel, Integer.valueOf(0));

        // Main artwork
        ImageIcon windowsImage = new ImageIcon("resources/windowsicon.png");
        int width = windowsImage.getIconWidth() + 100;
        int height = windowsImage.getIconHeight() + 100;
        Image scaledWindow = windowsImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon windowIcon = new ImageIcon(scaledWindow);

        // Sprites displayed on the screen
        ImageIcon main_art = new ImageIcon("resources/MainScreenImg.png");
        JLabel artLabel = new JLabel(main_art);
        artLabel.setBounds(512, 127, main_art.getIconWidth(), main_art.getIconHeight()); // Set proper x,y coordinates
        layeredPane.add(artLabel, Integer.valueOf(2));
        JLabel windowsLabel = new JLabel(windowIcon);
        windowsLabel.setBounds(-66, -60, width, height);
        layeredPane.add(windowsLabel, Integer.valueOf(1));

        // Overlay setup so when password popup is on screen, everything behind it becomes less clear
        ImageIcon overlayImage = new ImageIcon("resources/opacity.png");
        overlayLabel = new JLabel(overlayImage);
        overlayLabel.setBounds(0, 0, 1080, 750);
        overlayLabel.setVisible(false);
        layeredPane.add(overlayLabel, Integer.valueOf(2));

        // Button creation for each screen and styling
        JButton tutorialButton = buttonCreate(240, 430, 192, 64, "resources/button.png", "resources/button_clicked.png", "Tutorial");
        tutorialButton.addActionListener(e -> tutorialScreen.resetToGiveGift());
        cardLayout.show(mainPanel, "Tutorial");
        JLabel tutorialLabel = buttonText("Tutorial", 270, 430, 192, 64);
        layeredPane.add(tutorialLabel, Integer.valueOf(2));
        layeredPane.add(tutorialButton, Integer.valueOf(2));

        JButton newGameButton = buttonCreate(240, 340, 192, 64, "resources/button.png", "resources/button_clicked.png", "New Game");
        JLabel newGameLabel = buttonText("New Game", 275, 340, 192, 64);
        layeredPane.add(newGameLabel, Integer.valueOf(2));
        layeredPane.add(newGameButton, Integer.valueOf(2));

        JButton loadButton = buttonCreate(450, 340, 192, 64, "resources/button.png", "resources/button_clicked.png", "Load");
        JLabel loadLabel = buttonText("Load", 510, 340, 192, 64);
        layeredPane.add(loadLabel, Integer.valueOf(2));
        layeredPane.add(loadButton, Integer.valueOf(2));

        JButton creditButton = buttonCreate(450, 430, 192, 64, "resources/button.png", "resources/button_clicked.png", "Credit");
        JLabel creditLabel = buttonText("Credits", 490, 430, 192, 64);
        layeredPane.add(creditLabel, Integer.valueOf(2));
        layeredPane.add(creditButton, Integer.valueOf(2));

        // Parental control button to first show the password popup before going to the main parental control screen
        JButton parentalControlButton = buttonCreate(850, 620, 192, 64, "resources/button.png", "resources/button_clicked.png", "");
        parentalControlButton.addActionListener(e -> showPasswordPopup(layeredPane));
        JLabel homeText = new JLabel("PARENTAL CONTROL");
        homeText.setFont(customFont.deriveFont(11f));
        homeText.setForeground(Color.WHITE);
        homeText.setBounds(850,620,192,64);
        homeText.setHorizontalAlignment(SwingConstants.CENTER);
        homeText.setVerticalAlignment(SwingConstants.CENTER);
        layeredPane.add(homeText, Integer.valueOf(3));
        layeredPane.add(parentalControlButton, Integer.valueOf(2));

        // Password UI elements
        JLabel passwordTextLabel = new JLabel("ENTER PASSWORD");
        passwordTextLabel.setFont(customFont);
        passwordTextLabel.setFont(customFont.deriveFont(23f));
        passwordTextLabel.setForeground(Color.BLACK); // Set text color
        passwordTextLabel.setBounds(380, 290, 800, 30);
        passwordTextLabel.setVisible(false); // Initially hidden
        layeredPane.add(passwordTextLabel, Integer.valueOf(4));
        ImageIcon passwordIcon = new ImageIcon("resources/password_popup.png");
        int desiredWidth = 1000;
        int desiredHeight = 664;

        Image scaledPassword = passwordIcon.getImage().getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        passwordLabel = new JLabel(new ImageIcon(scaledPassword));
        passwordLabel.setBounds(20, 66, desiredWidth, desiredHeight);
        passwordLabel.setVisible(false);
        layeredPane.add(passwordLabel, Integer.valueOf(3));

        // Music toggle button
        JButton musicToggle = MainScreen.buttonCreate(20, 15, 50, 50, "resources/save.png", "resources/save_clicked.png", "");
        ImageIcon musicIcon = new ImageIcon("resources/Speaker-Crossed.png");
        JLabel musicLabel = new JLabel(musicIcon);

        // Position the icon centered on the button (adjust these values as needed)
        int iconX = 20  + (50 - 26)/2;  // button x + (button width - icon width)/2
        int iconY = 15 + (50 - 28)/2;  // button y + (button height - icon height)/2
        musicLabel.setBounds(iconX, iconY, 26, 28);
        layeredPane.add(musicLabel, Integer.valueOf(3));
        layeredPane.add(musicToggle, Integer.valueOf(2));
        musicToggle.addActionListener(e -> {
            MusicPlayer.toggleBackgroundMusic();
        });

        return layeredPane;
    }

    /**
     * Displays the password entry popup for parental controls.
     *
     * @param parentPane The parent container to add popup components to
     * @author Aya Abdulnabi
     * @author Mohammed Abdulnabi
     * @author Kamaldeep Ghotra
     */
    private static void showPasswordPopup(JLayeredPane parentPane) {
        // Set the popup visible when button is clicked
        passwordLabel.setVisible(true);
        overlayLabel.setVisible(true);

        // Show the password prompt box
        JTextField passwordField = new JTextField();
        Component[] components = parentPane.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component comp = components[i];
            if (comp instanceof JLabel && ((JLabel)comp).getText().equals("ENTER PASSWORD")) {
                comp.setVisible(true);
                break;
            }
        }

        // Managing the size and position of the input field
        passwordField.setBounds(210, 335, 650, 40);
        parentPane.add(passwordField, Integer.valueOf(4));
        passwordField.requestFocusInWindow();

        // "Back" button to leave the popup
        JButton backButton = buttonCreate(250, 400, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "Home");
        backButton.setText("Back");
        backButton.setFont(customFont);
        backButton.setForeground(Color.decode("#7392B2"));
        backButton.setHorizontalTextPosition(JButton.CENTER);
        backButton.setVerticalTextPosition(JButton.CENTER);
        backButton.setVisible(true);

        // "Enter" button to check if password is correct or no
        JButton doneButton = buttonCreate(600, 400, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "");
        doneButton.setText("ENTER");
        doneButton.setFont(customFont);
        doneButton.setForeground(Color.decode("#7392B2"));
        doneButton.setHorizontalTextPosition(JButton.CENTER);
        doneButton.setVerticalTextPosition(JButton.CENTER);
        doneButton.setVisible(true);

        // Configure action for the Back button - cancels password entry
        backButton.addActionListener(e -> {
            passwordLabel.setVisible(false);
            overlayLabel.setVisible(false);
            Component[] comps = parentPane.getComponents();
            for (int i = 0; i < comps.length; i++) {
                Component comp = comps[i];
                if (comp instanceof JLabel && ((JLabel)comp).getText().equals("ENTER PASSWORD")) {
                    comp.setVisible(false);
                    break;
                }
            }
            // When back is clicked, remove all the buttons, and refresh the display
            parentPane.remove(backButton);
            parentPane.remove(doneButton);
            parentPane.remove(passwordField);
            parentPane.repaint();
        });

        // Configure action for the Done button - processes password entry
        doneButton.addActionListener(e -> {
            String enteredPassword = passwordField.getText();

            // Verify if the password matches the one already set up
            if (parentalControl.authenticate(enteredPassword)) {
                MainScreen.updateParentalStatLabels();

                cardLayout.show(mainPanel, "ParentalControlScreen");
            } else {
                JOptionPane.showMessageDialog(parentPane, "Incorrect password!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            }

            // Regardless of success/failure, clean up the UI:
            passwordLabel.setVisible(false);
            overlayLabel.setVisible(false);
            // Find and hide the "ENTER PASSWORD" text label
            Component[] comppass = parentPane.getComponents();
            for (int i = 0; i < comppass.length; i++) {
                Component comp = comppass[i];
                if (comp instanceof JLabel && ((JLabel)comp).getText().equals("ENTER PASSWORD")) {
                    comp.setVisible(false);
                    break;
                }
            }
            // When back is clicked, remove all the buttons, and refresh the display
            parentPane.remove(backButton);
            parentPane.remove(doneButton);
            parentPane.remove(passwordField);
            parentPane.repaint();
        });

        // Add the buttons to the parent pane at layer 4 (highest layer)
        parentPane.add(backButton, Integer.valueOf(4));
        parentPane.add(doneButton, Integer.valueOf(4));
        parentPane.repaint();
    }

    /**
     * Updates the parental control status labels on the settings screen.
     *
     */
    public static void updateParentalStatLabels() {
        if (parentalControlScreen != null) {
            parentalControlScreen.updateStatLabels();
        }
    }


    /**
     * Transitions to the game screen with the specified game data.
     *
     * @param gameData The game state to load
     * @param saveFilePath Path to the save file for persistence
     *
     */
    public static void showInGameScreen(GameData gameData, String saveFilePath) {
        // Remove existing in game screen
        Component[] components = mainPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof InGameScreen) {
                mainPanel.remove(components[i]);
                break;
            }
        }

        // Create a new InGameScreen adn add it
        inGameScreen = new InGameScreen(customFont, cardLayout, mainPanel, gameData, saveFilePath);
        mainPanel.add(inGameScreen, "InGame");

        // Initialize the store screen
        Store store = GameDataManager.getSharedStore();
        JLayeredPane shopScreen = new StoreScreen(customFont, cardLayout, mainPanel, store, gameData,saveFilePath);

        // Remove existing shop screen
        components = mainPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof StoreScreen) {
                mainPanel.remove(components[i]);
                break;
            }
        }

        mainPanel.add(shopScreen, "Shop");
        // Switch to InGame screen
        cardLayout.show(mainPanel, "InGame");
        inGameScreen.refreshCoinDisplay();
    }

    /**
     * Gets the parental control manager instance.
     *
     * @return The ParentalControl instance
     *
     */
    public static ParentalControl getParentalControl() {
        return parentalControl;
    }

}