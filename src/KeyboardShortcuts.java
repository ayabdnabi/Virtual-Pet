package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * Handles keyboard shortcuts for interacting with the pet on the in-game screen.
 *
 * This class allows players to perform key actions using keyboard bindings instead of clicking buttons.
 * Common actions like feeding, exercising, gifting, playing, and visiting the vet are mapped to keys.
 *
 * - AI WAS USED IN ORDER TO LEARN HOW TO TAKE IN KEYBOARD INPUTS
 *
 * Keys mapped:
 * - P: Play with pet
 * - F: Feed the pet
 * - G: Give a gift
 * - E: Exercise
 * - V: Visit the vet
 * - Z or Enter: Put the pet to sleep
 *
 *
 * @author Aya Abdulnabi
 * @version 1.0
 */
public class KeyboardShortcuts {
    /** Reference to the in game UI*/
    private final InGameScreen inGameScreen;

    /** Holds the main panel that uses CardLayout to switch between different game screens */
    private final JPanel mainPanel;

    /** Layout manager used by mainPanel to navigate between screens */
    private final CardLayout cardLayout;

    /** Custom font throughout the UI */
    private final Font customFont;

    /** Holds the current game state and data */
    private final GameData gameData;


    /**
     * Constructs a new KeyboardShortcuts object to manage key bindings for the in-game screen.
     *
     * @param inGameScreen  The main gameplay screen where interactions with the pet occur.
     * @param mainPanel  The main panel holding all game screens, used with CardLayout.
     * @param cardLayout  The layout manager for switching between different screens (e.g. game, store).
     * @param customFont  A custom font used for styling UI components if needed.
     * @param gameData  The central game data containing pet, inventory, and coin information.
     */
    public KeyboardShortcuts(InGameScreen inGameScreen, JPanel mainPanel, CardLayout cardLayout, Font customFont, GameData gameData) {
        this.inGameScreen = inGameScreen;
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.customFont = customFont;
        this.gameData = gameData;
    }


    /**
     * Registers keyboard shortcuts (key bindings) for interactive pet actions in the game.
     * Each key press is mapped to an action that triggers a specific button click on the in-game screen.
     *
     * The following shortcuts are set up:
     * - P: Play with the pet
     * - F: Feed the pet
     * - G: Give the pet a gift
     * - E: Exercise the pet
     * - V: Visit the vet
     * - Z: Put the pet to sleep
     */
    public void setupKeyBindings() {
        InputMap userInput = inGameScreen.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = inGameScreen.getActionMap();

        // Play - P key
        userInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "playWithPet");
        actionMap.put("playWithPet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickButtonAtPosition(390, 550);
            }
        });

        // Gift - G key
        userInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), "giveGift");
        actionMap.put("giveGift", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickButtonAtPosition(560, 550);
            }
        });

        // Feed - F key
        userInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0), "feedPet");
        actionMap.put("feedPet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickButtonAtPosition(210, 550);
            }
        });

        // Exercise - E key
        userInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "exercisePet");
        actionMap.put("exercisePet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickButtonAtPosition(730, 550);
            }
        });

        // Vet - V key
        userInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "visitVet");
        actionMap.put("visitVet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickButtonAtPosition(900, 550);
            }
        });

        // Sleep - Z key
        userInput.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "sleepPet");
        actionMap.put("sleepPet", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clickButtonAtPosition(150, 500);
            }
        });
    }


    /**
     * Simulates a button click at a specific (x, y) position within the InGameScreen.
     * This method loops through all components on the screen, and if a JButton is found
     * at the given coordinates, it triggers a click event on that button.
     *
     * @param x The X-coordinate of the button.
     * @param y The Y-coordinate of the button.
     */
    private void clickButtonAtPosition(int x, int y) {
        // Get all UI elements that are on the game screen (currently)
        Component[] components = inGameScreen.getComponents();

        // Iterate through the array
        for (int i = 0; i < components.length; i++) {
            Component comp = components[i];

            // Check if the current component is a JButton
            if (comp instanceof JButton) {
                // Cast component to JButton to access specific methods (related to buttons)
                JButton button = (JButton) comp;

                // Check if the button's top-left corner matches the given (x, y) coordinates
                if (button.getX() == x && button.getY() == y) {
                    // Simulate a user click on the button.
                    button.doClick();
                    return;  // Exit the method after the button is clicked (avoids duplicates this way)
                }
            }
        }
    }
}