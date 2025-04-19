package src;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.Timer;


/**
 * The {@code InGameScreen} class represents the main interactive screen of the virtual pet game.
 * It provides a fully functional UI for players to interact with their pet, manage pet stats,
 * and perform actions such as feeding, playing, exercising, dressing up, and visiting the vet.
 *
 * <p>This screen also features:
 * <ul>
 *   <li>Progress bars displaying health, happiness, sleep, and fullness.</li>
 *   <li>Live stat decay over time using a Swing Timer.</li>
 *   <li>Coin tracking and in-game purchases through a store interface.</li>
 *   <li>Custom popups for inventory interaction (food, toys).</li>
 *   <li>Keyboard shortcuts, sound effects, and parental session tracking.</li>
 * </ul>
 *
 * <p>The class extends {@link JLayeredPane} to allow for flexible layering of background, UI,
 * sprite animations, and overlays. It integrates closely with supporting classes such as
 * {@code Pet}, {@code PlayerInventory}, {@code GameData}, and {@code StoreScreen}.
 *
 * @author
 * Mohammed Abdulnabi,
 * Aya Abdulnabi,
 * Kamaldeep Ghotra
 * @version 1.0
 */
public class InGameScreen extends JLayeredPane {
    /** Custom font for styling (for the text labels on buttons) */
    private Font customFont;

    /** Layout to swap different screens in the game */
    private CardLayout cardLayout;

    /** Main panel that holds all the screens*/
    private JPanel mainPanel;

    /** Progress bars to display health stat (visual) */
    private JProgressBar HealthProgressBar;
    /** Progress bars to display sleep stat (visual) */
    private JProgressBar SleepProgressBar;
    /** Progress bars to display happiness stat (visual) */
    private JProgressBar HappinessProgressBar;
    /** Progress bars to display fullness stat (visual) */
    private JProgressBar FullnessProgressBar;

    /** Players pet object */
    private Pet pet;

    /** Timer to reduce the pets stats */
    private Timer statDecayTimer;

    /** Store the current game sessions data */
    private GameData gameData;

    /** Path to save/load the current game as a file */
    private String saveFilePath;

    /** Label to display pet animations (GIF) */
    private JLabel gifLabel;

    /** Track when the player starts playing the game */
    private long sessionStartTime;

    /** Feed Button for the player to interact with the pet */
    private JButton feedButton;
    /** Play Button for the player to interact with the pet */
    private JButton playButton;
    /** Sleep Button for the player to interact with the pet */
    private JButton sleepButton;
    /** Gift Button for the player to interact with the pet */
    private JButton giveGiftButton;
    /** Exercise Button for the player to interact with the pet */
    private JButton exerciseButton;
    /** Vet Button for the player to interact with the pet */
    private JButton vetButton;
    /** Shop Button for the player to interact with the pet */
    private JButton shopButton;

    /** Used to track which base pet sprite is on the screen */
    private String base;

    /** The current state of the pet */
    private String state;

    /** Track the path of the sprite that is currently displayed */
    private String currentSpritePath = "";

    /** To display how many coins the user has */
    private JLabel coinLabel;


    /**
     * Sets up the InGame screen where the user can interact with their pet.
     * This includes everything from rendering the pet, showing stats,
     * buttons, backgrounds, coin display, and even wiring up keyboard shortcuts.
     * It also starts the stat decay timer right away.
     *
     * @param customFont    The font used for buttons and labels across the UI.
     * @param cardLayout    The layout used to switch between screens.
     * @param mainPanel     The main container holding all app screens.
     * @param gameData      The current game data, including pet and inventory.
     * @param saveFilePath  The file path used when saving the current session.
     *
     */
    public InGameScreen(Font customFont, CardLayout cardLayout, JPanel mainPanel, GameData gameData, String saveFilePath) {
        this.customFont = customFont;  // Store custom font
        this.cardLayout = cardLayout;  // Store Layout
        this.mainPanel = mainPanel;  // Store main panel
        this.pet = gameData.getPet();  // Store the current pet from GameData
        this.gameData = gameData;  // Store the game data
        this.saveFilePath = saveFilePath;  // Store the file path for saving
        this.sessionStartTime = System.currentTimeMillis();  // Recording when the session starts

        // Add this to InGameScreen under the name InGame
        mainPanel.add(this, "InGame");
        setPreferredSize(new Dimension(1080, 750));  // Preferred size matches game window resolution

        // Show the appropriate pet sprite
        initializePetSprite();

        // Add the layered background
        setBackground();

        // Make the 4 stat bars
        createProgressBars();
        healthBars();  // Show the graphical progress of the bar

        // Make the action buttons
        commandButtons();

        // Add back/home button
        createBackButton();

        // Show the user the # of coins
        displayCoins();

        // Attach the keyboard shortcuts for the user
        new KeyboardShortcuts(this, mainPanel, cardLayout, customFont, gameData).setupKeyBindings();

        // Start the timer for decaying the pets stats
        startStatDecayTimer();
    }


    /**
     * Overrides the default {@code setVisible} method to include additional behavior
     * when the in-game screen is shown or hidden.
     *
     * If the screen is becoming visible, this method ensures the coin counter
     * is refreshed to reflect the current game data.
     *
     * @param visible {@code true} to make the screen visible; {@code false} to hide it.
     *
     */
    @Override
    public void setVisible(boolean visible) {
        // Call setVisible from JLayeredPane
        super.setVisible(visible);

        // If the screen is shown then update the coin counter
        if (visible) {
            refreshCoinDisplay();
        }
    }


    /**
     * Sets up and displays all four progress bars representing the pet’s stats:
     * health, sleep, fullness, and happiness. Values are gotten from the pet.
     *
     * This method is typically called during the initial setup of the InGame screen.
     * and in decay.
     *
     * @author  Mohammed ABDULNABI
     */
    private void createProgressBars() {
        // Create all 4 progress bars to represent the pets stats (with corresponding positions)
        HealthProgressBar = createBar(26, 81, pet.getMaxHealth(), pet.getHealth());
        SleepProgressBar = createBar(101, 81, pet.getMaxSleep(), pet.getSleep());
        FullnessProgressBar = createBar(26, 299, pet.getMaxFullness(), pet.getFullness());
        HappinessProgressBar = createBar(101, 299, pet.getMaxHappiness(), pet.getHappiness());
    }


    /**
     * Creates and returns a vertical progress bar for one of the pet's stats
     *  The bar is styled and placed at the specified coordinates with color updated based on its current value.
     *
     * This method is used during screen setup to display all pet stat bars.
     *
     * @param x     The X position of the bar on the screen
     * @param y     The Y position of the bar on the screen
     * @param max   The maximum value the bar can represent
     * @param value The current value to display in the bar
     * @return A styled and positioned JProgressBar
     * @author Mohammed ABUDLNABI
     */
    private JProgressBar createBar(int x, int y, int max, int value) {
        // Vertical bar for the pets from 0 to max
        JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL, 0, max);
        bar.setBounds(x, y, 25, 135);  // Position and size
        bar.setValue(value);  // Set the current value
        bar.setStringPainted(false);  // Hide numbers inside the bar

        // Update the color based on stat level
        updateProgressBarColor(bar, value);
        bar.setBackground(Color.decode("#f9e6c6"));

        // Add the bar to this screen
        add(bar, Integer.valueOf(1));

        return bar;
    }


    /**
     * Initializes and displays the pet's idle sprite based on its type and outfit status.
     *
     * This method determines the correct base name for the pet and checks if the pet is
     * currently wearing an outfit. If so, it prepends "Outfit_"
     * to the sprite file name. It then loads the corresponding idle GIF sprite for display.
     *
     * @author Kamaldeep Ghotra
     * @author Mohammed Abdulnabi
     */
    private void initializePetSprite() {
        String type = pet.getPetType();  // Access which pet was chosen
        String outfitPrefix = "";  // Holds "Outfit_" if pet is dressed
        String base = "";  // Hold pets name

        // If pet is wearing an outfit, add prefix
        if (pet.isWearingOutfit()) {
            outfitPrefix = "Outfit_";
        }

        // Match the pet type to the corresponding file name
        if (type.equals("PetOption1")) {
            base = "PetOne";
        } else if (type.equals("PetOption2")) {
            base = "PetTwo";
        } else if (type.equals("PetOption3")) {
            base = "PetThree";
        }

        // Load the idle animation sprite
        spriteGifs("resources/" + base + outfitPrefix + "Idle.gif");
    }


    /**
     * Creates the back button on the InGame screen.
     * When clicked, it stops the stat decay timer, logs session time,
     * saves parental control data, and switches back to the Home screen.
     *
     *
     * @author Mohammed Abdulnabi
     * @author Aya Abdulnabi
     * @author Kamaldeep Ghotra
     */
    private void createBackButton() {
        // Clickable button using MainScreen
        JButton backButton = MainScreen.buttonCreate(800, 70, 70, 70, "resources/home_button.png", "resources/home_button_clicked.png", "");
        backButton.setBounds(990, 15, 64, 64);

        // Load the home icon image to place on top of button
        ImageIcon homeIcon = new ImageIcon("resources/home_icon.png");

        // Create a label to hold the icon graphic
        JLabel homeIconLabel = new JLabel(homeIcon);

        // Position the icon label where the button is
        homeIconLabel.setBounds(990, 15, 64, 64);

        // Add the icon label to the screen on layer 3
        add(homeIconLabel, Integer.valueOf(3));

        // Add the actual clickable button to the screen on layer 2
        add(backButton, Integer.valueOf(2));

        // Add an action listener that triggers when the back button is clicked
        backButton.addActionListener(e -> {
            stopDecayTimer();  // Stop the stat decay timer

            // Calculate how long the current play session lasted
            long sessionDuration = System.currentTimeMillis() - sessionStartTime;

            // Update the parental control system with this session's play duration
            MainScreen.getParentalControl().updateAfterSession(sessionDuration);

            // Save the updated parental control settings to storage
            GameDataManager.saveParentalControlSettings(MainScreen.getParentalControl());

            // Switch the current screen back to the Home screen using CardLayout
            cardLayout.show(mainPanel, "Home");
        });
    }


    /**
     * Starts the timer that gradually decreases the pet’s stats over time.
     * This is what gives the game its ongoing feel  health, hunger,
     * sleep, and happiness will slowly drop while the pet is active.
     *
     * If the timer is already running, it won't start a new one.
     *
     * @author Mohammed Abdulnabi
     * @author Kamaldeep Ghotra
     *
     */
    private void startStatDecayTimer() {
        // Check if the timer already exists and if its running, return if so
        if (statDecayTimer != null && statDecayTimer.isRunning()) {
            return;
        }

        // Create a new timer that runs every 250ms
        statDecayTimer = new Timer(250, e -> {

            // Apply stat decay to the pet
            pet.applyDecline();

            // Update each progress bar to reflect the new stat values
            updateBar(HealthProgressBar, pet.getMaxHealth(), pet.getHealth());
            updateBar(SleepProgressBar, pet.getMaxSleep(), pet.getSleep());
            updateBar(FullnessProgressBar, pet.getMaxFullness(), pet.getFullness());
            updateBar(HappinessProgressBar, pet.getMaxHappiness(), pet.getHappiness());

            // Refresh the coin counter
            refreshCoinDisplay();

            // Update the pet’s state
            updatePetState();
        });
        // External Resources used while debugging java Swing
        SwingUtilities.invokeLater(statDecayTimer::start);
    }

    /**
     * Updates a progress bar's maximum value, current value, and visual color.
     *
     * This method is used to refresh a specific stat bar (e.g., health, sleep, etc.)
     * after changes in the pet's state. It ensures the bar reflects the latest values
     * and visually updates its color based on the current percentage.
     *
     * @param bar   The {@link JProgressBar} to update.
     * @param max   The maximum value the bar can reach.
     * @param value The current value to display on the bar.
     *
     * @author Mohammed Abdulnabi
     */
    private void updateBar(JProgressBar bar, int max, int value) {
        // Set values for the progress bar
        bar.setMaximum(max);
        bar.setValue(value);
        updateProgressBarColor(bar, value);  // Update the progress bars colour
    }


    /**
     * Updates the pet's current state and adjusts the user interface accordingly.
     *
     * This method determines the pet's state (e.g., Dead, Sleep, Angry, Hungry, Idle)
     * and updates the base sprite name and current animation state. It also enables or
     * disables specific action buttons depending on the pet’s condition.
     *
     * Once the state is determined, the pet's sprite is updated to reflect it visually.
     *
     * @author Kamaldeep Ghotra
     * @author Mohammed Abdulnabi
     */
    private void updatePetState() {
        // Get the type of the pet selected by the player
        String type = pet.getPetType();

        // Match the type to its sprite name
        if (type.equals("PetOption1")) {
            base = "PetOne";
        } else if (type.equals("PetOption2")) {
            base = "PetTwo";
        } else if (type.equals("PetOption3")) {
            base = "PetThree";
        } else {
            base = "Pet";  // Use default if the type isn't recognized
        }

        // Setting which commands are available during the different states of the pet
        // Check if the pet is dead
        if (pet.isDead()) {
            state = "Dead";
            setButtonsEnabled(false);  // Disable all buttons

            // Check if the pet is sleeping
        } else if (pet.isSleeping()) {
            state = "Sleep";
            setButtonsEnabled(false);  // Disable all buttons

            // Check if the pet is angry
        } else if (pet.isAngry()) {
            state = "Angry";
            feedButton.setEnabled(false);  // Disable feed button
            vetButton.setEnabled(false);  // Disable vet button

            // Check if the pet is hungry
        } else if (pet.isHungry()) {
            state = "Hungry";
            setButtonsEnabled(true);

            // If none of the above, pet is in normal state
        } else {
            state = "Idle";
            setButtonsEnabled(true);
        }
        // Update the pet's displayed sprite based on current state and outfit
        updateSprite(pet);
    }

    /**
     * Enables or disables all interactive command buttons on the in-game screen.
     *
     * This method is typically used to prevent the player from performing actions
     * when the pet is in a certain state (e.g., sleeping, dead, or angry).
     *
     * @param enabled {@code true} to enable all buttons; {@code false} to disable them.
     *
     * @author Mohammed Abdulnabi
     */
    private void setButtonsEnabled(boolean enabled) {
        feedButton.setEnabled(enabled);
        playButton.setEnabled(enabled);
        sleepButton.setEnabled(enabled);
        giveGiftButton.setEnabled(enabled);
        vetButton.setEnabled(enabled);
        exerciseButton.setEnabled(enabled);
        shopButton.setEnabled(enabled);
    }


    /**
     * Updates the displayed pet animation with a new GIF for a limited duration.
     *
     * Removes the current animation loads and displays a new GIF from the specified path,
     * and plays it for a specified length of time.
     *
     * @param gifPath the file path to the new GIF animation
     * @param duration the time in milliseconds to show the new GIF before reverting
     *
     * @author Mohammed Abdulnabi
     * @author Kamaldeep Ghotra
     */
    private void updateGif(String gifPath, int duration) {

        // Ensure the old GIF is removed properly
        if (gifLabel != null) {
            remove(gifLabel);
            gifLabel = null; // Reset to avoid referencing a removed label
        }

        // Load new gif and update the global gifLabel
        ImageIcon newGif = new ImageIcon(gifPath);
        gifLabel = new JLabel(newGif);
        gifLabel.setBounds(300, 30, 622, 632);
        add(gifLabel, Integer.valueOf(3));

        //reload the screen
        revalidate();
        repaint();

        // Reset back to idle animation after a delay
        Timer revertTimer = new Timer(duration, e -> {
            if (gifLabel != null) {
                remove(gifLabel);
                gifLabel = null;
            }

            // Restore the correct idle animation based on pet type
            String petType = pet.getPetType();

            //Find the right gif using depending on the type and if they are wearing an outfit.
            if (petType.equals("PetOption1"))
            {
                if (pet.isWearingOutfit())
                {
                    spriteGifs("resources/PetOneOutfit_Idle.gif");
                } else
                {
                    spriteGifs("resources/PetOne_Idle.gif");
                }
            }
            else if (petType.equals("PetOption2"))
            {
                if(pet.isWearingOutfit())
                {
                    spriteGifs("resources/PetTwoOutfit_Idle.gif");
                }
                else
                {
                    spriteGifs("resources/PetTwo_Idle.gif");
                }
            }
            else if (petType.equals("PetOption3"))
            {
                if(pet.isWearingOutfit())
                {
                    spriteGifs("resources/PetThreeOutfit_Idle.gif");
                }
                else
                {
                    spriteGifs("resources/PetThree_Idle.gif");
                }
            }

            //reaload the screen
            revalidate();
            repaint();
        });


        revertTimer.setRepeats(false);
        revertTimer.start();
    }


    /**
     * Updates the color of a progress bar based on the current health value.
     *
     * Applies a gradient of colors ranging from red (low health) to green (high health)
     * to indicate the pet’s health level.
     *
     * @param progressBar the JProgressBar to update
     * @param health the current health value (0–100)
     *
     */
    public static void updateProgressBarColor(JProgressBar progressBar, int health) {
        // Change color based on health percentage
        if (health <= 10) {
            progressBar.setForeground(Color.decode("#A94337"));
        } else if (health <= 20) {
            progressBar.setForeground(Color.decode("#B54F32"));
        } else if (health <= 30) {
            progressBar.setForeground(Color.decode("#C05C2E"));
        } else if (health <= 40) {
            progressBar.setForeground(Color.decode("#CB6829"));
        } else if (health <= 50) {
            progressBar.setForeground(Color.decode("#D67524"));
        } else if (health <= 60) {
            progressBar.setForeground(Color.decode("#E1821F"));
        } else if (health <= 70) {
            progressBar.setForeground(Color.decode("#EC8E1A"));
        } else if (health <= 80) {
            progressBar.setForeground(Color.decode("#F79B15"));
        } else if (health <= 90) {
            progressBar.setForeground(Color.decode("#83B52B"));
        } else {
            progressBar.setForeground(Color.decode("#37A943"));
        }

        progressBar.repaint();
    }


    /**
     * Sets the visual background and adds utility buttons to the screen.
     *
     * Includes ->
     * - Grid and window background images
     * - A save button that stores game progress
     * - A music toggle button to start/stop background music

     *
     * @author Aya Abdulnabi
     */
    private void setBackground() {
        // background
        ImageIcon background = new ImageIcon("resources/grid.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1080, 750);
        add(backgroundLabel, Integer.valueOf(0));

        // actual file thing
        ImageIcon inGameWindow = new ImageIcon("resources/ingamescreen.png");
        Image scaledWindow = inGameWindow.getImage().getScaledInstance(1080, 750, Image.SCALE_SMOOTH);
        ImageIcon scaledWindowIcon = new ImageIcon(scaledWindow);

        // create label
        JLabel windowLabel = new JLabel(scaledWindowIcon);
        windowLabel.setBounds(-13, -10, 1080, 750);

        // add above grid
        add(windowLabel, Integer.valueOf(1));

        // save icon
        JButton saveButton = MainScreen.buttonCreate(17, 15, 50, 50, "resources/save.png", "resources/save_clicked.png", "Save");
        ImageIcon saveIcon = new ImageIcon("resources/save_icon.png");
        JLabel saveLabel = new JLabel(saveIcon);

        // Position the icon centered on the button (adjust these values as needed)

        // button x + (button width - icon width)/2
        int xPos = 17 + (50 - 28)/2;

        // button y + (button height - icon height)/2
        int yPos = 15 + (50 - 28)/2;
        saveLabel.setBounds(xPos, yPos, 28, 28);
        add(saveLabel, Integer.valueOf(3));
        add(saveButton, Integer.valueOf(2));

        saveButton.addActionListener(e -> {
            // Save the game
            GameDataManager.saveGame(
                    "saves/" + pet.getName() + ".json",
                    gameData.getPet(),
                    gameData.getInventory(),
                    gameData.getTotalPlayTime()
            );

            // Create custom components
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            panel.setBackground(new Color(240, 240, 240));

            // Custom message
            JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
                    + "<font size=4 color='#2E86C1'><b>Game Saved!</b></font><br>"
                    + "<font size=3 color='#5D6D7E'>Your progress is safe</font></div></html>");
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(messageLabel, BorderLayout.CENTER);

            // Create a custom button without focus painting
            JButton okButton = new JButton("Got it!");
            okButton.setFont(new Font("Arial", Font.BOLD, 14));
            okButton.setBackground(new Color(52, 152, 219));
            okButton.setForeground(Color.WHITE);
            okButton.setFocusPainted(false);  // This removes the focus border
            okButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            okButton.addActionListener(ev -> {
                Window window = SwingUtilities.getWindowAncestor(panel);
                if (window != null) {
                    window.dispose();
                }
            });

            // Create button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(new Color(240, 240, 240));
            buttonPanel.add(okButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            // Create the dialog
            JDialog dialog = new JDialog((Frame)null, "Save Successful", true);
            dialog.setContentPane(panel);
            dialog.setSize(350, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.setVisible(true);
        });

        // stop music button
        JButton musicToggle = MainScreen.buttonCreate(90, 15, 50, 50, "resources/save.png", "resources/save_clicked.png", "");
        ImageIcon musicIcon = new ImageIcon("resources/Speaker-Crossed.png");
        JLabel musicLabel = new JLabel(musicIcon);

        // Position the icon centered on the button (adjust these values as needed)
        int iconX = 90 + (50 - 26)/2;  // button x + (button width - icon width)/2
        int iconY = 15 + (50 - 28)/2;  // button y + (button height - icon height)/2
        musicLabel.setBounds(iconX, iconY, 26, 28);

        add(musicLabel, Integer.valueOf(3));
        add(musicToggle, Integer.valueOf(2));


        musicToggle.addActionListener(e -> {
            MusicPlayer.toggleBackgroundMusic();
        });

    }


    /**
     * Displays the pet status bars (health, sleep, hunger, happiness) on the store screen.
     *
     * Loads and positions each status bar image at fixed coordinates. These bars are
     * purely visual and meant to reflect the pet's current state while shopping.
     *
     * @author Aya Abdulnabi
     */

    private void healthBars(){
        //Draw and place the health bar UI element
        ImageIcon healthBar = new ImageIcon("resources/health_bar.png");
        JLabel healthBarLabel = new JLabel(healthBar);
        healthBarLabel.setBounds(-145, -30, 350, 350);
        add(healthBarLabel, Integer.valueOf(2));

        //Draw and place the sleepBar UI element
        ImageIcon sleepBar = new ImageIcon("resources/sleep_bar.png");
        JLabel sleepBarLabel = new JLabel(sleepBar);
        sleepBarLabel.setBounds(-70, -30,350, 350);
        add(sleepBarLabel, Integer.valueOf(2));

        //Draw and place the hungerBar UI element
        ImageIcon hungerBar = new ImageIcon("resources/hunger_bar.png");
        JLabel hungerBarLabel = new JLabel(hungerBar);
        hungerBarLabel.setBounds(-145, 190, 350, 350);
        add(hungerBarLabel, Integer.valueOf(2));

        //Draw and place the happiness_bar UI element
        ImageIcon happiness_bar = new ImageIcon("resources/happiness_bar.png");
        JLabel happinessBarLabel = new JLabel(happiness_bar);
        happinessBarLabel.setBounds(-70, 190, 350, 350);
        add(happinessBarLabel, Integer.valueOf(2));
    }


    /**
     * Sets up all the interactive command buttons for the InGame screen,
     * including shop, feed, play, gift, exercise, vet, and sleep.
     * Each button has its own behavior when clicked, triggering things like
     * opening the inventory, rewarding the pet, or saving/loading game state.
     *
     * This is usually called during the setup of the InGameScreen UI.
     *
     * @author Aya Abdulnabi
     * @author Mohammed Abdulabi
     * @author Kamaldeep Ghotra
     */
    private void commandButtons() {
        // === SHOP BUTTON ===================================================================================================================================================================================//
        shopButton = MainScreen.buttonCreate(30, 550, 128, 128, "resources/command_button.png", "resources/command_button_clicked.png", "Shop");


        // When clicked, save current game, load latest data, and refresh the store screen
        shopButton.addActionListener(e -> {

            //save the game when the store button is clicked
            GameDataManager.saveGame(saveFilePath, gameData.getPet(), gameData.getInventory(), gameData.getTotalPlayTime());

            // Reload fresh game data
            GameData updatedGameData = GameDataManager.loadGame(saveFilePath);

            // Remove old StoreScreen if it exists
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof StoreScreen) {
                    mainPanel.remove(comp);
                    break;
                }
            }

            // Create new StoreScreen with updated game data
            Store store = GameDataManager.getSharedStore();
            StoreScreen storeScreen = new StoreScreen(customFont, cardLayout, mainPanel, store, updatedGameData, saveFilePath);

            //show the the new store/
            mainPanel.add(storeScreen, "Shop");
            cardLayout.show(mainPanel, "Shop");
        });



        // Icon + label for Shop
        ImageIcon shopIcon = new ImageIcon("resources/store_icon.png");
        JLabel shopIconLabel = new JLabel(shopIcon);
        shopIconLabel.setBounds(30 + (128 - 44)/2, 545 + (128 - 38)/2, 44, 38);
        add(shopIconLabel, Integer.valueOf(3));
        add(shopButton, Integer.valueOf(2));
        JLabel shopTextLabel = new JLabel("SHOP");
        shopTextLabel.setFont(customFont.deriveFont(Font.BOLD, 14f));
        shopTextLabel.setForeground(Color.BLACK);
        shopTextLabel.setBounds(30, 530 + 128 + 5, 128, 20);
        shopTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(shopTextLabel, Integer.valueOf(3));


        // === FEED BUTTON ===================================================================================================================================================================================//
        //create style and place the feed button
        feedButton = MainScreen.buttonCreate(210,550,128,128, "resources/command_button.png", "resources/command_button_clicked.png", "");
        feedButton.addActionListener(e -> showInventoryPopup(feedButton, "Feed"));
        ImageIcon feedIcon = new ImageIcon("resources/feed_icon.png");
        JLabel feedIconLabel = new JLabel(feedIcon);
        feedIconLabel.setBounds(210 + (128 - 39)/2, 545 + (128 - 38)/2, 39, 38);
        add(feedIconLabel, Integer.valueOf(3));
        add(feedButton, Integer.valueOf(2));
        JLabel feedTextLabel = new JLabel("FEED");
        feedTextLabel.setFont(customFont.deriveFont(Font.BOLD, 14f));
        feedTextLabel.setForeground(Color.BLACK);
        feedTextLabel.setBounds(210, 530 + 128 + 5, 128, 20);
        feedTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(feedTextLabel, Integer.valueOf(3));

        // === play BUTTON ===================================================================================================================================================================================//
        //create style and place the play button
        playButton = MainScreen.buttonCreate(390,550, 128,128, "resources/command_button.png", "resources/command_button_clicked.png", "");
        playButton.addActionListener(e -> showInventoryPopup(playButton, "Play"));
        ImageIcon playIcon = new ImageIcon("resources/play_icon.png");
        JLabel playIconLabel = new JLabel(playIcon);
        playIconLabel.setBounds(390 + (128 - 47)/2, 545 + (128 - 58)/2, 47, 58);
        add(playIconLabel, Integer.valueOf(3));
        add(playButton, Integer.valueOf(2));
        JLabel playTextLabel = new JLabel("PLAY");
        playTextLabel.setFont(customFont.deriveFont(Font.BOLD, 14f));
        playTextLabel.setForeground(Color.BLACK);
        playTextLabel.setBounds(390, 530 + 128 + 5, 128, 20);
        playTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(playTextLabel, Integer.valueOf(3));

        // === GIFT BUTTON ===================================================================================================================================================================================//
        //create style and place the gift button
        giveGiftButton = MainScreen.buttonCreate(560, 550, 128,128, "resources/command_button.png", "resources/command_button_clicked.png", "");
        ImageIcon giftIcon = new ImageIcon("resources/gift_icon.png");
        JLabel giftIconLabel = new JLabel(giftIcon);
        giftIconLabel.setBounds(560 + (128 - 50)/2, 545 + (128 - 47)/2, 50, 47);
        add(giftIconLabel, Integer.valueOf(3));
        add(giveGiftButton, Integer.valueOf(2));

        //gift button logic. i,e action listener
        giveGiftButton.addActionListener(e -> {
            //get the player information and pet info
            PlayerInventory inventory = gameData.getInventory();
            Pet pet = gameData.getPet();

            // Toggle outfit
            toggleOutfit(pet, inventory);

            // Update sprite
            updateSprite(pet);

            // If wearing outfit, increase happiness and update coins
            if (pet.isWearingOutfit()) {
                pet.setHappiness(pet.getMaxHappiness());
                // Add coin reward for wearing outfit
                inventory.setPlayerCoins(inventory.getPlayerCoins() + 100);
                refreshCoinDisplay();
            }
        });

        //Stylizes and places the gidt button on the screen.
        JLabel giftTextLabel = new JLabel("GIFT");
        giftTextLabel.setFont(customFont.deriveFont(Font.BOLD, 14f));
        giftTextLabel.setForeground(Color.BLACK);
        giftTextLabel.setBounds(560, 530 + 128 + 5, 128, 20);
        giftTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(giftTextLabel, Integer.valueOf(3));

        // === EXERCISE BUTTON ===================================================================================================================================================================================//
        //create style and place the feed button
        exerciseButton = MainScreen.buttonCreate(730,550,128,128, "resources/command_button.png", "resources/command_button_clicked.png", "");
        add(exerciseButton, Integer.valueOf(2));

        //Exercise button logic
        exerciseButton.addActionListener(e -> {

            //get the player data 
            PlayerInventory inventory = gameData.getInventory();
            //execute the excervise logic
            inventory.exercisePet(pet);
            //play the exercise sound and adjust voluem
            MusicPlayer.playSoundEffect("resources/exercise_sound.wav");
            MusicPlayer.setSfxVolume(0.07f);
            //update all the bar graphs
            HealthProgressBar.setValue(pet.getHealth());
            FullnessProgressBar.setValue(pet.getFullness());
            SleepProgressBar.setValue(pet.getSleep());

            // Add this to update coin display
            refreshCoinDisplay();
            updateGif(getGifPath("Exercising"),1500);
        });


        //display the exercise icon
        ImageIcon exerciseIcon = new ImageIcon("resources/exercise_icon.png");
        JLabel exerciseIconLabel = new JLabel(exerciseIcon);
        exerciseIconLabel.setBounds(730 + (128 - 50)/2, 545 + (128 - 47)/2, 50, 47);
        add(exerciseIconLabel, Integer.valueOf(3));
        add(exerciseButton, Integer.valueOf(2));

        //display the icon label
        JLabel exerciseTextLabel = new JLabel("EXERCISE");
        exerciseTextLabel.setFont(customFont.deriveFont(Font.BOLD, 14f));
        exerciseTextLabel.setForeground(Color.BLACK);
        exerciseTextLabel.setBounds(730, 530 + 128 + 5, 128, 20);
        exerciseTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(exerciseTextLabel, Integer.valueOf(3));

        //Vet button logic
        vetButton = MainScreen.buttonCreate(900,550,128,128, "resources/command_button.png", "resources/command_button_clicked.png", "");
        vetButton = MainScreen.buttonCreate(900,550,128,128, "resources/command_button.png", "resources/command_button_clicked.png", "");
        vetButton.addActionListener(e -> {

            //Play the healing sound
            MusicPlayer.playSoundEffect("resources/heal_sound.wav");
            MusicPlayer.setSfxVolume(0.07f);

            // Get PlayerInventory
            PlayerInventory inventory = gameData.getInventory();
            int currentTime = (int) (System.currentTimeMillis() / 1000);

            //Check the cooldownf or the vet
            if (inventory.takePetToVet(pet, currentTime)) {
                // update the progress bar if no cooldown
                HealthProgressBar.setValue(pet.getHealth());
                //update coins
                refreshCoinDisplay();
            } else {
                //if the cooldown is not done
                int remainingCooldown = pet.getVetCooldownDuration() - (currentTime - pet.getLastVetVisitTime());
                remainingCooldown = Math.max(remainingCooldown, 0);

                //SHOW POPUP fi the cooldown is not done not allowing heplayer to take to vet.
                showStyledDialog("Vet Cooldown",pet.getName() + " must wait " + remainingCooldown + " seconds before visiting the vet again.");
            }
        });

        //Display and show the vet button and icon
        ImageIcon vetIcon = new ImageIcon("resources/vet_icon.png");
        JLabel vetIconLabel = new JLabel(vetIcon);
        vetIconLabel.setBounds(900 + (128 - 47)/2, 545 + (128 - 47)/2, 47, 44);
        add(vetIconLabel, Integer.valueOf(3));
        add(vetButton, Integer.valueOf(2));
        JLabel vetTextLabel = new JLabel("VET");
        vetTextLabel.setFont(customFont.deriveFont(Font.BOLD, 14f));
        vetTextLabel.setForeground(Color.BLACK);
        vetTextLabel.setBounds(900, 530 + 128 + 5, 128, 20);
        vetTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(vetTextLabel, Integer.valueOf(3));

        // === SLEEP BUTTON ===================================================================================================================================================================================//
        // Style place th sleep button and icno
        sleepButton = MainScreen.buttonCreate(150, 500, 56,47, "resources/sleep_button.png", "resources/sleep_button.png", "");
        add(sleepButton, Integer.valueOf(2));

        //sleep button logic - set sleep to true and pet takes care of the rest.
        sleepButton.addActionListener(e -> {
            pet.setSleeping(true);
        });
    }


    /**
     * Equips or unequips an outfit for the pet depending on its current state.
     * If the pet is already wearing an outfit, it will be removed. If not, it checks
     * the player's inventory and equips the outfit if it is owned.
     *
     * @param pet       The current pet whose outfit state is being toggled.
     * @param inventory The player's inventory used to check ownership and apply outfits.
     *
     * @author Mohammed Abdulnabi
     * @author Kamaldeep Singh Ghotra
     */
    private void toggleOutfit(Pet pet, PlayerInventory inventory) {

        if (pet.isWearingOutfit())
        {
            // If the pet is already wearing something, take it off
            inventory.unequipOutfit(pet);
        }
        else {
            // Try to equip the outfit the player owns for this pet
            String petType = pet.getPetType();
            String matchingOutfit = "";

            //assign depending on pet type
            if (petType.equals("PetOption1")) matchingOutfit = "outfit1";
            else if (petType.equals("PetOption2")) matchingOutfit = "outfit2";
            else if (petType.equals("PetOption3")) matchingOutfit = "outfit3";

            if (inventory.ownsOutfit(matchingOutfit))
            {
                inventory.equipOutfit(matchingOutfit, pet);
            }
            else
            {
                // Play error sound and log message if outfit isn't owned
                MusicPlayer.playSoundEffect("resources/error_button_sound.wav");
            }
        }
    }


    /**
     * Displays a popup inventory menu above the selected command button this shows up to six usable items
     * food or toys that the player owns. Items are displayed in a grid with their icons
     * and quantities, and clicking an item will apply it to the pet and play an animation.
     *
     * @param sourceButton   The button that triggered this popup, used to anchor its position.
     * @param inventoryType  Either "Feed" or "Play" — determines which inventory items to display.
     * @author Aya Abdulnabi
     * @author Mohammed Abdulabi
     * @author Kamaldeep Ghotra
     */
    private void showInventoryPopup(JButton sourceButton, String inventoryType) {
        // inventory popup
        ImageIcon originalIcon = new ImageIcon("resources/inventory_popup.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(originalIcon.getIconWidth()/2, originalIcon.getIconHeight()/2, Image.SCALE_SMOOTH);
        ImageIcon popupIcon = new ImageIcon(scaledImage);
        JLabel popupLabel = new JLabel(popupIcon);

        // Original close button implementation
        JButton closeButton = MainScreen.buttonCreate(380, 60, 100, 100, "resources/save.png", "resources/save_clicked.png", "");

        // X label on top of close button
        JLabel xLabel = new JLabel("X");
        xLabel.setFont(customFont.deriveFont(Font.BOLD, 19f));
        xLabel.setForeground(Color.BLACK);
        xLabel.setBounds(423, 100, 20, 20);
        xLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // inventory JLayeredPane
        JLayeredPane inventoryPane = new JLayeredPane();
        inventoryPane.setPreferredSize(new Dimension(popupIcon.getIconWidth(), popupIcon.getIconHeight()));

        // add components to inventoryPane - background first
        popupLabel.setBounds(0, 30, popupIcon.getIconWidth(), popupIcon.getIconHeight());
        inventoryPane.add(popupLabel, JLayeredPane.DEFAULT_LAYER); // Bottom layer
        inventoryPane.add(closeButton, JLayeredPane.MODAL_LAYER);
        inventoryPane.add(xLabel, JLayeredPane.POPUP_LAYER);

        // Create inventory grid squares
        ImageIcon squareIcon = new ImageIcon("resources/inventory_item_square_1.png");
        int startX = 100;
        int startY = 140;
        int squareWidth = 90;
        int squareHeight = 90;
        int horizontalGap = 20;
        int verticalGap = 15;

        JLabel[] squares = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            int row = i / 3;
            int col = i % 3;
            squares[i] = new JLabel(squareIcon);
            squares[i].setBounds(
                    startX + col * (squareWidth + horizontalGap),
                    startY + row * (squareHeight + verticalGap),
                    squareWidth,
                    squareHeight
            );
            inventoryPane.add(squares[i], JLayeredPane.PALETTE_LAYER);
        }

        // Position the popup
        int popupX = sourceButton.getX() + (sourceButton.getWidth() - popupIcon.getIconWidth())/2;
        int popupY = sourceButton.getY() - popupIcon.getIconHeight() + 110;
        popupX = Math.max(0, Math.min(popupX, getWidth() - popupIcon.getIconWidth()));
        popupY = Math.max(0, Math.min(popupY, getHeight() - popupIcon.getIconHeight()));
        inventoryPane.setBounds(popupX, popupY, popupIcon.getIconWidth(), popupIcon.getIconHeight());
        add(inventoryPane, JLayeredPane.POPUP_LAYER);

        // Create a label for the item GIF that will appear beside the pet
        JLabel itemGifLabel = new JLabel();
        itemGifLabel.setBounds(330, 200, 405, 393); // Position beside the pet
        add(itemGifLabel, Integer.valueOf(4)); // Higher layer than pet

        // Handle food items
        if (inventoryType.equals("Feed")) {
            PlayerInventory inventory = gameData.getInventory();
            int itemIndex = 0;

            for (int i = 0; i < inventory.getFoodInventory().keySet().size(); i++) {
                Food food = (Food) inventory.getFoodInventory().keySet().toArray()[i];
                int quantity = inventory.getFoodCount(food);
                if (quantity <= 0 || itemIndex >= 6) continue;

                JLabel square = squares[itemIndex];

                // Load and scale food icon to 40x40
                String iconPath = "resources/food_" + food.getName().replace(" ", "_").toLowerCase() + ".png";
                ImageIcon originalFoodIcon = new ImageIcon(iconPath);
                Image scaledFoodImage = originalFoodIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

                // Create food button with scaled icon
                JButton foodButton = new JButton(new ImageIcon(scaledFoodImage));
                foodButton.setBounds(
                        square.getX() + (square.getWidth() - 40) / 2,
                        square.getY() + (square.getHeight() - 40) / 2,
                        40,
                        40
                );
                foodButton.setContentAreaFilled(false);
                foodButton.setBorderPainted(false);
                foodButton.setFocusPainted(false);

                // Quantity label
                JLabel quantityLabel = new JLabel("x" + quantity);
                quantityLabel.setFont(customFont.deriveFont(12f));
                quantityLabel.setForeground(Color.BLACK);
                quantityLabel.setBounds(square.getX() + square.getWidth() - 25, square.getY() + square.getHeight() - 20, 25, 15);

                inventoryPane.add(foodButton, JLayeredPane.MODAL_LAYER);
                inventoryPane.add(quantityLabel, JLayeredPane.MODAL_LAYER);

                foodButton.addActionListener(e -> {
                    itemGifLabel.setBounds(330, 200, 405, 393); // Food position
                    // Show the food GIF beside the pet
                    String foodGifPath = "resources/food_" + food.getName().replace(" ", "_").toLowerCase() + ".gif";
                    ImageIcon foodGif = new ImageIcon(foodGifPath);
                    itemGifLabel.setIcon(foodGif);

                    boolean fed = inventory.feedPet(pet, food);
                    if (fed) {
                        FullnessProgressBar.setValue(pet.getFullness());
                        playSound("eating_sound.wav");
                        updateGif(getGifPath("Eating"),1500);
                        refreshCoinDisplay(); // Add this to update coin display

                        // Remove the food GIF after 1.5 seconds
                        Timer gifTimer = new Timer(1500, ev -> {
                            itemGifLabel.setIcon(null);
                        });
                        gifTimer.setRepeats(false);
                        gifTimer.start();

                        remove(inventoryPane);
                        revalidate();
                        repaint();
                    } else {
                        showStyledDialog("No Food", "Out of " + food.getName() + "!");
                    }
                });
                itemIndex++;
            }
        }

        // Handle toy items
        if (inventoryType.equals("Play")) {
            PlayerInventory inventory = gameData.getInventory();
            int itemIndex = 0;

            for (int i = 0; i < inventory.getToyInventory().keySet().size(); i++) {
                Toys toy = (Toys) inventory.getToyInventory().keySet().toArray()[i];
                int quantity = inventory.getToyCount(toy);
                if (quantity <= 0 || itemIndex >= 6) continue;

                JLabel square = squares[itemIndex];

                // Load and scale toy icon to 40x40
                String iconPath = "resources/toy_" + toy.getName().replace(" ", "_").toLowerCase() + ".png";
                ImageIcon originalToyIcon = new ImageIcon(iconPath);
                Image scaledToyImage = originalToyIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);

                // Create toy button with scaled icon
                JButton toyButton = new JButton(new ImageIcon(scaledToyImage));
                toyButton.setBounds(square.getX() + (square.getWidth() - 40) / 2, square.getY() + (square.getHeight() - 40) / 2, 40, 40);
                toyButton.setContentAreaFilled(false);
                toyButton.setBorderPainted(false);
                toyButton.setFocusPainted(false);


                inventoryPane.add(toyButton, JLayeredPane.MODAL_LAYER);

                // In the toy button action listener (play action)
                toyButton.addActionListener(e -> {
                    itemGifLabel.setBounds(430, 350, 100, 100);

                    // Show the toy GIF
                    String toyPngPath = "resources/toy_" + toy.getName().replace(" ", "_").toLowerCase() + ".png";
                    ImageIcon originalToyPNG = new ImageIcon(toyPngPath);
                    Image scaledToyPNG = originalToyPNG.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    itemGifLabel.setIcon(new ImageIcon(scaledToyPNG));

                    if (inventory.hasToy(toy)) {
                        pet.increaseHappiness(25);
                        HappinessProgressBar.setValue(pet.getHappiness());
                        playSound("play_sound.wav");
                        updateGif(getGifPath("Playing"),1500);

                        // Add coin reward for playing
                        inventory.setPlayerCoins(inventory.getPlayerCoins() + 100);
                        refreshCoinDisplay(); // Update the coin display

                        // Remove the toy GIF after 1.5 seconds
                        Timer gifTimer = new Timer(1500, ev -> {
                            itemGifLabel.setIcon(null);
                        });
                        gifTimer.setRepeats(false);
                        gifTimer.start();

                        remove(inventoryPane);
                        revalidate();
                        repaint();
                    } else {
                        showStyledDialog("No Toy", "You don't have " + toy.getName() + "!");
                    }
                });
                itemIndex++;
            }
        }

        // Original close button action
        closeButton.addActionListener(e -> {
            remove(inventoryPane);
            itemGifLabel.setIcon(null); // Clear any displayed GIF
            revalidate();
            repaint();
        });

        revalidate();
        repaint();
    }

    //
    /**
     * Updates the pet's sprite on screen based on its current state and outfit.
     * If the sprite path hasn't changed, it skips the update to avoid unnecessary redraws.
     *
     * @param pet The pet whose appearance should be updated on screen.
     * @author Mohammed Abdulnbi
     */
    private void updateSprite(Pet pet) {
        String outfit = pet.getCurrentOutfit();
        String spritePath;

        if (outfit == null || outfit.isEmpty()) {
            spritePath = "resources/" + base + "_"+ state + ".gif";
        } else {
            spritePath = "resources/" + base + "Outfit_" + state + ".gif";
        }

        // Only update if sprite has changed
        if (spritePath.equals(currentSpritePath)) return;

        // Remove old label
        if (gifLabel != null) remove(gifLabel);

        ImageIcon gifIcon = new ImageIcon(spritePath);
        gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(300, 30, 622, 632);
        add(gifLabel, Integer.valueOf(3));
        currentSpritePath = spritePath;

        revalidate();
        repaint();
    }

    /**
     * Builds the file path to the correct pet gif based on the interactin and outfit status.
     * It checks the pet type and whether the pet is wearing an outfit to determine
     * which sprite version to return.
     *
     * @param action The action requested (like "Idle", "Eating", or "Playing").
     * @return The file path to the matching gif for that action and pet state.
     * @author Mohammed Abdulnabi
     */
    private String getGifPath(String action) {
        String petType = pet.getPetType();
        String baseName = "";

        // Determine the base name for the pet type
        if (petType.equals("PetOption1")) {
            baseName = "PetOne";
        } else if (petType.equals("PetOption2")) {
            baseName = "PetTwo";
        } else {
            baseName = "PetThree";
        }

        // Append outfit status and action
        if (pet.isWearingOutfit()) {
            return "resources/" + baseName + "Outfit_" + action + ".gif";
        } else {
            return "resources/" + baseName + "_" + action + ".gif";
        }
    }

    /**
     * Updates the pet's sprite by loading and displaying a new GIF.
     * If there's already a sprite showing, it gets removed and replaced with the new one.
     * Used whenever the pet's state or outfit changes.
     *
     * @param spriteFilePath The file path to the new GIF you want to show.
     * @author Mohammed Abdulnbi
     */
    private void spriteGifs(String spriteFilePath) {
        //If the sprite is loaded and not null
        if (gifLabel != null) {
            remove(gifLabel); // Remove existing GIF before adding a new one
        }

        //access the new pet from the directory and load it.
        ImageIcon gifIcon = new ImageIcon(spriteFilePath);
        gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(300, 30, 622, 632);
        add(gifLabel, Integer.valueOf(3));

        //reload
        revalidate();
        repaint();
    }


    /**
     * Stops the timer that is reducing the vitals of the pet.
     * This is called every time the InGameScreen is left.
     *
     */
    public void stopDecayTimer() {
        // if the timer is not stopped then stop it.
        if (statDecayTimer != null) {
            statDecayTimer.stop();
        }
    }


    /**
     * Plays a sound effect from the specified file path.
     * This method loads the audio file, opens a clip, and starts playback immediately.
     * If an error occurs (e.g. file not found or unsupported format), the stack trace is printed.
     *
     * @param soundFilePath The relative or absolute path to the audio file to play.
     * @author Aya Abdulnabi
     */
    private void playSound(String soundFilePath) {
        try {
            InputStream raw = getClass().getResourceAsStream("/" + soundFilePath);
            if (raw == null) {
                System.out.println("Sound file not found: " + soundFilePath);
                return;
            }

            BufferedInputStream buffered = new BufferedInputStream(raw);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(buffered);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            // Suppress stack trace and show a simple message
            System.out.println("Could not play sound: " + soundFilePath + " (" + e.getMessage() + ")");
        }
    }


    /**
     * Displays the player's current coin count on the screen using a styled label and coin icon.
     * This method creates and positions both the coin image and the numeric label,
     * styling them appropriately and adding them to the component.
     *
     */
    public void displayCoins(){

        // Load and scale the coin display image
        ImageIcon originalCoinIcon = new ImageIcon("resources/coins_display.png");
        Image scaledCoinImage = originalCoinIcon.getImage().getScaledInstance(200, 46, Image.SCALE_SMOOTH);
        ImageIcon scaledCoinIcon = new ImageIcon(scaledCoinImage);

        // create the label with scaled image
        JLabel coinDisplayLabel = new JLabel(scaledCoinIcon);
        coinDisplayLabel.setBounds(820, 460, 200, 46);

        //get coins from inve
        int coins = gameData.getInventory().getPlayerCoins();

        //create and style the coin label.
        coinLabel = new JLabel(String.valueOf(coins));
        coinLabel.setFont(customFont.deriveFont(Font.BOLD, 17f));
        coinLabel.setForeground(Color.BLACK);

        coinLabel.setBounds(890, 470, 100, 30);

        //reload and refresh
        add(coinLabel, Integer.valueOf(4));
        add(coinDisplayLabel, Integer.valueOf(3));
        revalidate();
        repaint();
    }



    /**
     * Updates the coin label on the screen to reflect the player's current coin count.
     * This method gets the latest coin value from the GameData's PlayerInventory
     * and updates the label accordingly.
     */
    public void refreshCoinDisplay() {
        // if the label exists
        if (coinLabel != null) {
            //get the coins from the player inventory
            coinLabel.setText(String.valueOf(gameData.getInventory().getPlayerCoins()));

            //update the display.
            revalidate();
            repaint();
        }
    }


    /**
     * Displays a custom-styled modal dialog with a title and message, centered on the current component.
     * The dialog features a modern, flat design with a single "OK" button to dismiss it.
     *
     * @param title   The title to be displayed in bold at the top of the dialog.
     * @param message The message body content to show below the title.
     * @author Aya Abdulnabi
     */
    private void showStyledDialog(String title, String message) {

        // Create the main panel with padding and background color
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 240));

        // Create and style the message label using HTML
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<font size=4 color='#2E86C1'><b>" + title + "</b></font><br>"
                + "<font size=3 color='#5D6D7E'>" + message + "</font></div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add the label to the center of the panel
        panel.add(messageLabel, BorderLayout.CENTER);

        // Create and style the OK button
        JButton okButton = new JButton("OK");
        okButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        okButton.setBackground(new Color(52, 152, 219));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Close the dialog when OK is clicked
        okButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(panel);
            if (window != null) {
                window.dispose();
            }
        });

        //create panel and center button horizonrtal
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.add(okButton);

        // Add the button panel to the bottom
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Create and show the dialog, make it look cool also
        JDialog dialog = new JDialog((Frame)null, title, true);
        dialog.setContentPane(panel);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

}