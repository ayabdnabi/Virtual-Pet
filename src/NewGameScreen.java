package src;

import javax.swing.*;
import java.awt.*;

/**
 * Construct the UI for pet selection upon the user navigating to the "New Game" screen
 *
 * <p>
 * The {@code NewGameScreen} class handles the user interface for creating a new pet
 * in the Virtual Pet application. It allows users to choose from three pet types,
 * name their pet, and start a new game with it. Each pet has a screen showing its ID
 * card and a "Choose" button. Selecting a pet triggers a popup where the user can
 * name the pet and confirm the creation.
 * </p>
 *
 * @author
 * Aya Abdulnabi,
 * Mohammed Abdulnabi
 * Kamaldeep Ghorta
 * Clair Yu
 * @version 1.0
 */
public class NewGameScreen extends JLayeredPane {
    /** Font used throughout the entire UI */
    private static Font customFont;
    /** CardLayout manager for switching between different pet selection screens/views. */
    private CardLayout petCardLayout;
    /** Main panel that contains all pet selection views managed by petCardLayout. */
    private JPanel petPanel;
    /** Primary CardLayout for the application's main screens */
    private static CardLayout mainCardLayout;
    /** Main panel that contains all the screens */
    private static JPanel mainPanel;
    /** Button to return to the home/main menu screen. */
    private JButton homeButton;
    /** Flag indicating whether a popup is currently visible */
    private boolean popupVisible = false;

    /**
     * Constructs the New Game screen with custom font and layout manager
     * Creates the pet selection panels and home button
     *
     * @param customFont the custom font to apply to UI
     * @param mainCardLayout the main {@link CardLayout} controlling application screens
     * @param mainPanel the main container that holds all pet selection cards
     *
     */
    public NewGameScreen(Font customFont, CardLayout mainCardLayout, JPanel mainPanel) {
        this.customFont = customFont;
        this.mainCardLayout = mainCardLayout;
        this.mainPanel = mainPanel;
        setPreferredSize(new Dimension(1080, 750));

        // Set up internal pet selection navigation
        this.petCardLayout = new CardLayout();
        this.petPanel = new JPanel(petCardLayout);
        petPanel.setBounds(0, 0, 1080, 750);
        add(petPanel, Integer.valueOf(0));

        // Create the pet screens
        JLayeredPane firstPet = firstPet();
        JLayeredPane secondPet = secondPet();
        JLayeredPane thirdPet = thirdPet();

        // Add the screens to the pet panel
        petPanel.add(firstPet, "First Pet");
        petPanel.add(secondPet, "Second Pet");
        petPanel.add(thirdPet, "Third Pet");

        // By default show the first pet screen
        petCardLayout.show(petPanel, "First Pet");

        // Home button (uses the buttoncreate method in mainscren)
        homeButton = MainScreen.buttonCreate(20, 20, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "Home");
        homeButton.addActionListener(e -> {
            if (!popupVisible) {
                mainCardLayout.show(mainPanel, "Main");
            }
        });
        // Designing the home button
        JLabel homeText = new JLabel("< HOME");
        homeText.setFont(customFont);
        homeText.setForeground(Color.BLACK);
        homeText.setBounds(20,20,192,64);
        homeText.setHorizontalAlignment(SwingConstants.CENTER);
        homeText.setVerticalAlignment(SwingConstants.CENTER);
        add(homeText, Integer.valueOf(4));
        add(homeButton, Integer.valueOf(3));

        setVisible(true);
    }

    /**
     * Builds the common background layer for each pet screen including background and ID card image.
     *
     * @param idCardPet the file path of the pet ID card image
     * @param screenSource the layered pane to modify
     * @return the modified layered pane with background and ID card
     *
     * @author Clair Yu
     */
    private JLayeredPane backgroundScreen(String idCardPet, JLayeredPane screenSource) {
        // Set up the background for the screen
        ImageIcon background = new ImageIcon("resources/new_game.png");
        Image scaledBG = background.getImage().getScaledInstance(1080, 750, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledBG));
        backgroundLabel.setBounds(0, 0, 1080, 750);
        screenSource.add(backgroundLabel, Integer.valueOf(0));

        // Create a text label for the screen
        JLabel newGameLabel = new JLabel("NEW GAME");
        newGameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newGameLabel.setVerticalAlignment(SwingConstants.CENTER);
        newGameLabel.setFont(customFont.deriveFont(24f));
        newGameLabel.setForeground(Color.WHITE);
        newGameLabel.setBounds(270,50,192,64);
        add(newGameLabel, Integer.valueOf(2));

        // Scale the ID card
        ImageIcon idCard = new ImageIcon(idCardPet);
        int width = idCard.getIconWidth() - 1000;
        int height = idCard.getIconHeight() - 700;
        Image scaledID = idCard.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel idCardLabel = new JLabel(new ImageIcon(scaledID));
        idCardLabel.setBounds(35, 30, width, height);
        screenSource.add(idCardLabel, Integer.valueOf(4));

        return screenSource;
    }

    /**
     * Adds arrow navigation buttons and icons for pet selection screens.
     *
     * @param screenSource the layered pane to add buttons to
     * @param petNum the current pet index (1, 2, or 3) to determine which buttons to show
     * @return the updated layered pane
     *
     * @author Clair Yu
     */
    private JLayeredPane arrowButtons(JLayeredPane screenSource, int petNum) {
        // Load in the default arrow icons
        ImageIcon rightArrowImage = new ImageIcon("resources/right_arrow.png");
        ImageIcon leftArrowImage = new ImageIcon("resources/left_arrow.png");
        JLabel rightArrowLabel = new JLabel(rightArrowImage);
        JLabel leftArrowLabel = new JLabel(leftArrowImage);
        rightArrowLabel.setBounds(868, 365, 32, 32);
        leftArrowLabel.setBounds(160, 365, 32, 32);

        // Determine which type of arrow to use depending on the pet
        if (petNum == 1) {
            // Only a right arrow should show up on pet 1
            JButton rightButton = createPetNavigationButton(850, 350, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Second Pet");
            screenSource.add(rightButton, Integer.valueOf(4));
            screenSource.add(rightArrowLabel, Integer.valueOf(5));
        }
        else if (petNum == 2) {
            // Both left and right arrows on pet 2
            JButton rightButton = createPetNavigationButton(850, 350, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Third Pet");
            JButton leftButton = createPetNavigationButton(145, 350, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "First Pet");
            screenSource.add(rightButton, Integer.valueOf(4));
            screenSource.add(leftButton, Integer.valueOf(4));
            screenSource.add(rightArrowLabel, Integer.valueOf(5));
            screenSource.add(leftArrowLabel, Integer.valueOf(5));
        }
        else {
            // Only left arrow on pet 3
            JButton leftButton = createPetNavigationButton(145, 350, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Second Pet");
            screenSource.add(leftButton, Integer.valueOf(4));
            screenSource.add(leftArrowLabel, Integer.valueOf(5));
        }
        return screenSource;
    }

    /**
     * Creates a navigation button that switches the pet selection card.
     *
     * @param x x-position of the button
     * @param y y-position of the button
     * @param width button width
     * @param height button height
     * @param defaultImg default image path
     * @param pressedImg pressed image path
     * @param cardName the card to show on click
     * @return the created {@link JButton}
     *
     * @author Aya Abdulnabi
     */
    private JButton createPetNavigationButton(int x, int y, int width, int height, String defaultImg, String pressedImg, String cardName) {
        JButton button = MainScreen.buttonCreate(x, y, width, height, defaultImg, pressedImg, cardName);
        button.addActionListener(e -> petCardLayout.show(petPanel, cardName));
        return button;
    }

    /**
     * Displays the semi-transparent overlay and popup for naming the pet.
     *
     * @param sourceScreen the screen to attach the popup to
     * @return an array of {@link JLabel}s containing overlay and popup elements
     *
     * @author Aya Abdulnabi
     */
    private JLabel[] displayPopup(JLayeredPane sourceScreen) {
        // Overlay to "blur" the main background
        ImageIcon overlayIcon = new ImageIcon("resources/opacity.png");
        JLabel overlayLabel = new JLabel(overlayIcon);
        overlayLabel.setBounds(0, 0, 1080, 750);
        overlayLabel.setVisible(false);
        sourceScreen.add(overlayLabel, Integer.valueOf(5));

        // Name popup to name the pet with styling
        ImageIcon popupIcon = new ImageIcon("resources/password_popup.png");
        int width = popupIcon.getIconWidth() - 1000;
        int height = popupIcon.getIconHeight() - 664;
        Image scaledPopup = popupIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        JLabel popUpLabel = new JLabel(new ImageIcon(scaledPopup));
        popUpLabel.setBounds(20, 66, width, height);
        popUpLabel.setVisible(false);
        sourceScreen.add(popUpLabel, Integer.valueOf(6));
        return new JLabel[]{overlayLabel, popUpLabel};
    }

    /**
     * Constructs the screen for the first pet.
     *
     * @return a complete layered pane for the first pet
     *
     * @author Aya Abdulnabi
     * @author Clair Yu
     */
    private JLayeredPane firstPet() {
        // Create a pane for the first pet and add its respective ID card
        JLayeredPane firstPet = new JLayeredPane();
        firstPet.setPreferredSize(new Dimension(1080, 750));
        arrowButtons(firstPet, 1);
        backgroundScreen("resources/id_card_sprite1.png", firstPet);
        JLabel[] popupElements = displayPopup(firstPet); // Store overlay and popup labels

        // Choose button if you decide to choose this pet
        JButton chooseButton = MainScreen.buttonCreate(445, 620, 192, 64, "resources/button.png", "resources/button_clicked.png", "Choose");
        firstPet.add(chooseButton, Integer.valueOf(4));
        JLabel homeText = new JLabel("CHOOSE");
        homeText.setFont(customFont);
        homeText.setForeground(Color.WHITE);
        homeText.setBounds(445,620,192,64);
        homeText.setHorizontalAlignment(SwingConstants.CENTER);
        homeText.setVerticalAlignment(SwingConstants.CENTER);
        add(homeText, Integer.valueOf(4));
        chooseButton.addActionListener(e -> showPopup(firstPet, popupElements[0], popupElements[1], "PetOption1"));


        return firstPet;
    }

    /**
     * Constructs the screen for the second pet type.
     *
     * @return a complete layered pane for the second pet
     *
     * @author Aya Abdulnabi
     * @author Clair Yu
     */
    private JLayeredPane secondPet() {
        // Create a pane for the second pet and add its respective ID card
        JLayeredPane secondPet = new JLayeredPane();
        secondPet.setPreferredSize(new Dimension(1080, 750));
        arrowButtons(secondPet, 2);
        backgroundScreen("resources/id_card_sprite2.png", secondPet);
        JLabel[] popupElements = displayPopup(secondPet); // Store overlay and popup labels

        // Choose button if you decide to choose this pet
        JButton chooseButton = MainScreen.buttonCreate(445, 620, 192, 64, "resources/button.png", "resources/button_clicked.png", "Choose");
        chooseButton.addActionListener(e -> showPopup(secondPet, popupElements[0], popupElements[1],"PetOption2"));
        secondPet.add(chooseButton, Integer.valueOf(4));

        return secondPet;
    }

    /**
     * Constructs the screen for the third pet type.
     *
     * @return a complete layered pane for the third pet
     *
     * @author Aya Abdulnabi
     * @author Clair Yu
     */
    private JLayeredPane thirdPet() {
        // Create a pane for the third pet and add its respective ID card
        JLayeredPane thirdPet = new JLayeredPane();
        thirdPet.setPreferredSize(new Dimension(1080, 750));
        arrowButtons(thirdPet, 3);
        backgroundScreen("resources/id_card_sprite3.png", thirdPet);
        JLabel[] popupElements = displayPopup(thirdPet); // Store overlay and popup labels

        // Choose button if you decide to choose this pet
        JButton chooseButton = MainScreen.buttonCreate(445, 620, 192, 64, "resources/button.png", "resources/button_clicked.png", "Choose");
        chooseButton.addActionListener(e -> showPopup(thirdPet, popupElements[0], popupElements[1], "PetOption3"));
        thirdPet.add(chooseButton, Integer.valueOf(4));

        return thirdPet;
    }

    /**
     * Displays the popup dialog allowing the user to enter a name for their chosen pet.
     * Also checks parental control and save limits before confirming game creation.
     *
     * @param parentPane the parent screen to place popup components on
     * @param overlayLabel the dimmed overlay background
     * @param popUpLabel the popup image label
     * @param petType the selected pet type
     *
     * @author Mohammed Abdulnabi
     * @author Kamaldeep Ghorta
     */
    private void showPopup(JLayeredPane parentPane, JLabel overlayLabel, JLabel popUpLabel, String petType) {
        // Show the name pop up
        this.popupVisible = true;
        this.homeButton.setEnabled(false); // Disable home button

        // Show popup overlay AND the popup itself
        overlayLabel.setVisible(true);
        popUpLabel.setVisible(true);

        // Add "NAME YOUR PET:" text label with styling
        JLabel nameLabel = new JLabel("NAME YOUR PET:");
        nameLabel.setFont(customFont.deriveFont(24f));
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setBounds(210, 310, 650, 40);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        parentPane.add(nameLabel, Integer.valueOf(7));

        // Pet name input field
        JTextField petNameField = new JTextField();
        petNameField.setBounds(210, 360, 650, 40);
        parentPane.add(petNameField, Integer.valueOf(7));
        petNameField.requestFocusInWindow();

        // Back Button (closes popup)
        JButton backButton = MainScreen.buttonCreate(250, 440, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "");
        backButton.setText("BACK");
        backButton.setFont(customFont);
        backButton.setForeground(Color.decode("#7392B2"));
        backButton.setHorizontalTextPosition(JButton.CENTER);
        backButton.setVerticalTextPosition(JButton.CENTER);

        // Enter Button (validates and saves pet name)
        JButton enterButton = MainScreen.buttonCreate(600, 440, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "");
        enterButton.setText("ENTER");
        enterButton.setFont(customFont);
        enterButton.setForeground(Color.decode("#7392B2"));
        enterButton.setHorizontalTextPosition(JButton.CENTER);
        enterButton.setVerticalTextPosition(JButton.CENTER);

        // Add action listeners
        backButton.addActionListener(e -> {
            parentPane.remove(nameLabel); // Remove the label when closing
            closePopup(parentPane, overlayLabel, popUpLabel, backButton, enterButton, petNameField);
        });

        enterButton.addActionListener(e -> {
            //Check if playtime is currently allowed
            if (!MainScreen.getParentalControl().isPlayAllowedNow()) {
                JOptionPane.showMessageDialog(parentPane,
                        "Playtime is currently restricted.\nPlease try again during allowed hours.",
                        "Playtime Restricted",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Check to see if there are enough spaces to save a new save file
            if (!GameDataManager.canCreateNewGame()) {
                JOptionPane.showMessageDialog(parentPane,
                        "Maximum save files reached! Delete a save to create a new game.",
                        "Save Limit Reached",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create the pet depending on the type
            String petName = petNameField.getText().trim();
            if (!petName.isEmpty()) {
                int maxHealth = 100, maxSleep = 100, maxFullness = 100, maxHappiness = 100;

                Pet newPet = new Pet(
                        petName, petType,
                        maxHealth, maxSleep, maxFullness, maxHappiness,
                        maxHealth, maxSleep, maxFullness, maxHappiness,
                        5, 5, 5, 5,
                        false, false, true, false,
                        0, 30, 0, 20,
                        null
                );

                Store store = new Store();  // Create store instance
                PlayerInventory inventory = new PlayerInventory(store); // Pass store to inventory

                String filename = "saves/" + petName + ".json";
                GameData newData = new GameData(newPet, inventory, 0);
                MainScreen.showInGameScreen(newData, filename);
                GameDataManager.saveGame(filename, newPet, inventory, 0);  // Save after loading screen
            }

            parentPane.remove(nameLabel); // Remove the label when closing
            closePopup(parentPane, overlayLabel, popUpLabel, backButton, enterButton, petNameField);
        });

        parentPane.add(backButton, Integer.valueOf(8));
        parentPane.add(enterButton, Integer.valueOf(8));
        parentPane.repaint();
    }

    /**
     * Hides and removes all popup components, restores interaction with the main screen.
     *
     * @param parentPane the parent screen containing popup elements
     * @param overlayLabel the dimmed background
     * @param popUpLabel the popup dialog
     * @param backButton the "Back" button to remove
     * @param enterButton the "Enter" button to remove
     * @param petNameField the text field for pet name entry
     *
     * @author Aya Abdulnabi
     * @author Kamaldeep Ghorta
     */
    private void closePopup(JLayeredPane parentPane, JLabel overlayLabel, JLabel popUpLabel, JButton backButton, JButton enterButton, JTextField petNameField) {
        // Hide popup elements
        overlayLabel.setVisible(false);
        popUpLabel.setVisible(false);

        // Remove components
        parentPane.remove(backButton);
        parentPane.remove(enterButton);
        parentPane.remove(petNameField);

        // Update state and enable home button
        this.popupVisible = false;
        this.homeButton.setEnabled(true);

        parentPane.repaint();
    }
}