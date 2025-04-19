package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The StoreScreen class represents the in-game store interface where players can purchase items
 * for their pets. It provides a multiple pages with categorized items (food, toys, and gifts),
 * purchase functionality with coin validation, and visual feedback for transactions.
 *
 * Features include:
 * - Three categorized store pages (food, toys, outfits) with navigation buttons
 * - Interactive item displays with images, prices, and descriptions
 * - Purchase validation based on player's coin balance and pet compatibility
 * - Real-time coin display updates
 * - Custom popup dialogs for item details and purchase feedback
 * - Integration with the game's inventory system for persistent item storage
 *
 * The class extends JLayeredPane to manage visual elements at different depth levels
 * and uses CardLayout for seamless page transitions within the store.
 *
 * @author
 * Aya Abdulnabi,
 * Mohammed Abdulnabi
 * Kamaldeep Ghorta
 * @version 1.0
 */
public class StoreScreen extends JLayeredPane {
    /** Font used throughout store UI */
    private Font customFont;
    /** Layout for switching screens */
    private CardLayout mainCardLayout;
    /** Main container that holds all screens */
    private JPanel mainPanel;
    /** Switching screen within the store */
    private CardLayout shopCardLayout;
    /** Panel that contains the store pages */
    private JPanel shopPanel;
    /** A list of all the clickable buttons */
    private List<JButton> allButtons = new ArrayList<>();
    /** The store that holds foods, toys, and gifts */
    private Store store;
    /** The players inventory */
    private PlayerInventory playerInventory;
    /** The pets and players inventory */
    private GameData gameData;
    /**Button to go to the next screen */
    private JButton nextButton;
    /** Button to go to the previous screen */
    private JButton prevButton;
    /** A label to show the player how many coins they have */
    private JLabel coinLabel;
    /** Where the game will be saved */
    private String saveFilePath;



    /** Constructor of the class that sets the font, switches screens, and sets up the store screen
     *
     * @param customFont     The custom font to be used for all text rendering in the UI
     * @param mainCardLayout The CardLayout manager that handles screen transition between different screens.
     * @param mainPanel      The main container panel where all screens are added
     * @param store          The Store instance that contains available items and handles purchasing logic.
     * @param gameData       The current game state data, including player inventory and resources, used to populate the store interface.
     * @param saveFilePath   The save file where game data will be saved. Required for loading/saving state
     */
    public StoreScreen(Font customFont, CardLayout mainCardLayout, JPanel mainPanel, Store store, GameData gameData, String saveFilePath) {
        /* Save custom font */
        this.customFont = customFont;
        /* Save layout for switching screens */
        this.mainCardLayout = mainCardLayout;
        /* Save main container */
        this.mainPanel = mainPanel;
        /* Save store reference */
        this.store = store;
        /* Save game data*/
        this.gameData = gameData;
        /* Get players inventory */
        this.playerInventory = gameData.getInventory();
        this.saveFilePath = saveFilePath;

        /* Printing debug messages*/
        debugInventory();
        /* Preferred size of this panel*/
        setPreferredSize(new Dimension(1080, 750));
        /* Setting up background visuals like grid, store BG, title */
        setupBackground();
        /* Setup layout panel that holds the stores pages*/
        setupShopPanel();
        /* Setup next and previous buttons */
        setupNavButtons();
        /* Adding things to the store like food, toys, and gifts */
        populateStorePages();
        /* Setup the home button to allow user to go to the home screen when clicked */
        setupHomeButton();
        /* Setup the exit to allow the user to exit the game when clicked */
        setupExitIcon();
        /* Displaying the players coins */
        updateCoinDisplay();
    }


    /**
     * Prints debug information about the player's inventory and main panel components.
     *
     * Logs the player's coin count at the start of the store and lists all components
     * currently in the main panel. Used for debugging initialization issues.
     *
     * @author Mohammed Abdulnabi
     */
    private void debugInventory() {
        // If the players inventory is empty, print to console
        if (this.playerInventory == null) {
            System.out.println("ERROR: PlayerInventory is NULL in StoreScreen!");
        // Otherwise, print to console how many coins at the start of the store
        } else {
            System.out.println("Player Coins at Store Start: " + this.playerInventory.getPlayerCoins());
        }

        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            System.out.println("Component: " + comp.getClass().getName());
        }
    }

    /**
     * Initializes the shop panel with a CardLayout and sets its visual properties.
     *
     * The panel is made transparent and sized to fit the screen. It is then added
     * to the layered pane at the appropriate layer.
     *
     * @author Mohammed Abdulnabi
     */

    private void setupShopPanel() {
        this.shopCardLayout = new CardLayout();
        this.shopPanel = new JPanel(shopCardLayout);
        shopPanel.setOpaque(false);
        shopPanel.setBounds(0, 0, 1080, 750);
        add(shopPanel, Integer.valueOf(2));
    }


    /**
     * Sets up the home navigation buttons with functionality to switch in between
     * the different screens between the stores.
     *
     * @author Aya Abdulnabi
     */
    private void setupNavButtons() {
        // Load the default next page arrow
        nextButton = createNavButton("resources/next_page.png", 700, 600);
        nextButton.addActionListener(e -> {
            shopCardLayout.next(shopPanel);
            updateNavButtons();
        });
        add(nextButton, Integer.valueOf(3));

        // Load the default previous page arrow
        prevButton = createNavButton("resources/prev_page.png", 300, 600);
        prevButton.addActionListener(e -> {
            shopCardLayout.previous(shopPanel);
            updateNavButtons();
        });
        prevButton.setVisible(false);
        add(prevButton, Integer.valueOf(3));
    }

    /**
     * Sets up the home button with functionality to return to InGameScreen.
     */
    private void setupHomeButton() {
        // Create the home button to bring you back to the InGame screeen
        JButton homeButton = MainScreen.buttonCreate(800, 50, 192, 64, "resources/home_button.png", "resources/home_button_clicked.png", "InGame");
        homeButton.addActionListener(e -> {
            resetToFirstPage();

            // Save updated inventory only so health doesnt get overwritten
            System.out.println("Calling saveInventoryToGameFile from StoreScreen...");
            GameDataManager.saveInventoryToGameFile(saveFilePath, gameData.getInventory());

            // Reload the full GameData (including inventory updates)
            GameData updatedGameData = GameDataManager.loadGame(saveFilePath);

            // Remove the old InGameScreen instance
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof InGameScreen) {
                    mainPanel.remove(comp);
                    break;
                }
            }

            // Create and add a new InGameScreen with fresh data
            InGameScreen newInGameScreen = new InGameScreen(customFont, mainCardLayout, mainPanel, updatedGameData, saveFilePath);
            mainPanel.add(newInGameScreen, "InGameScreen");

            // Show the new screen
            mainCardLayout.show(mainPanel, "InGameScreen");
        });

        add(homeButton, Integer.valueOf(3));
        allButtons.add(homeButton);
    }


    /**
     * Adds the exit icon image to the store UI (decorative only).
     */
    private void setupExitIcon() {
        ImageIcon exitIcon = new ImageIcon("resources/exit_store.png");
        JLabel exitLabel = new JLabel(exitIcon);

        int xPos = 800 + (192 - 24) / 2;
        int yPos = 50 + (64 - 28) / 2;
        exitLabel.setBounds(xPos, yPos, 24, 28);
        add(exitLabel, Integer.valueOf(4));
    }



    /**
     * Updates the visibility of navigation buttons based on the currently visible store page.
     *
     * - Page 1: only the next button is shown
     * - Page 2: both next and previous buttons are shown
     * - Page 3: only the previous button is shown
     *
     * Checks which page is currently visible in the shop panel and adjusts the
     * visibility of the navigation buttons accordingly.
     *
     * @author Aya Abdulnabi
     */
    private void updateNavButtons() {
        // Get current page name
        Component[] components = shopPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i].isVisible()) {
                String pageName = shopPanel.getComponentZOrder(components[i]) == 0 ? "Page 1" :
                        shopPanel.getComponentZOrder(components[i]) == 1 ? "Page 2" : "Page 3";

                // Update button visibility based on current page
                if (pageName.equals("Page 1")) {
                    nextButton.setVisible(true);
                    prevButton.setVisible(false);
                } else if (pageName.equals("Page 2")) {
                    nextButton.setVisible(true);
                    prevButton.setVisible(true);
                } else if (pageName.equals("Page 3")) {
                    nextButton.setVisible(false);
                    prevButton.setVisible(true);
                }
                break;
            }
        }
    }


    /**
     * Creates a navigation button with a given image and position.
     *
     * The button is styled to be transparent and plays a click sound when pressed.
     *
     * @param imagePath the path to the button's image
     * @param x the x-coordinate position of the button
     * @param y the y-coordinate position of the button
     * @return the styled JButton with click sound behavior
     *
     * @author Aya Abdulnabi
     */
    private JButton createNavButton(String imagePath, int x, int y) {
        // Create the buttons and make them have a sound when clicked on them
        JButton button = new JButton(new ImageIcon(imagePath));
        button.setBounds(x, y, 64, 64);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            MusicPlayer.playSoundEffect("resources/button_clicked.wav");
        });

        return button;
    }

    /**
     * Resets the shop view to the first page.
     *
     * If the shop panel and its layout are properly initialized,
     * this method displays "Page 1" and updates the navigation buttons accordingly.
     */
    public void resetToFirstPage() {
        if (shopCardLayout != null && shopPanel != null) {
            shopCardLayout.show(shopPanel, "Page 1");
            updateNavButtons();
        }
    }

    /**
     * Sets up the visual background for the store screen.
     *
     * Adds layered background images and a "SHOP" title label to the screen.
     * Elements are positioned and layered to ensure proper visuals
     *
     * @author Aya Abdulnabi
     */
    private void setupBackground() {
        // Default background
        ImageIcon background = new ImageIcon("resources/grid.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1080, 750);
        add(backgroundLabel, Integer.valueOf(0));

        // Default shop image
        ImageIcon shopBG = new ImageIcon("resources/shop_bg.png");
        JLabel bgLabel = new JLabel(shopBG);
        bgLabel.setBounds(0, -15, 1080, 750);
        add(bgLabel, Integer.valueOf(1));

        // Text inside the shop_bg
        JLabel shopText = new JLabel("SHOP");
        shopText.setFont(customFont.deriveFont(Font.BOLD, 18f));
        shopText.setForeground(Color.BLACK);
        shopText.setBounds(500, 70, 200, 30);
        add(shopText, Integer.valueOf(3)); // Higher layer to appear above background

    }

    /**
     * Populates the store with item pages.
     *
     * Initializes and adds three separate pages to the shop panel:
     * one for food items, one for toys, and one for gifts (outfits).
     *
     * @author Aya Abdulnabi
     */
    private void populateStorePages() {
        // Create three pages
        createPage1();
        createPage2();
        createPage3();
    }

    /**
     * Creates and populates the first store page with food items.
     *
     * Displays up to six food items retrieved from the store,
     * arranged in a grid layout, and adds the page to the shop panel.
     *
     * @author Aya Abdulnabi
     */

    private void createPage1() {
        JLayeredPane page = new JLayeredPane();
        page.setPreferredSize(new Dimension(1080, 750));
        page.setOpaque(false);

        int x = 200, y = 130;
        int itemsPerRow = 3;
        int count = 0;

        // Add first 6 food items
        List<String> foodNames = new ArrayList<>(store.getAllFood().keySet());
        for (int i = 0; i < Math.min(6, foodNames.size()); i++) {
            String foodName = foodNames.get(i);
            Food food = store.getFood(foodName);
            JPanel itemPanel = createShopItem(food.getName(), food.getPrice(), x, y);
            page.add(itemPanel, Integer.valueOf(2));

            x += 250;
            count++;
            if (count % itemsPerRow == 0) {
                x = 200;
                y += 240;
            }
        }
        shopPanel.add(page, "Page 1");
    }


    /**
     * Creates and populates the second store page with toy items.
     *
     * Displays up to six food items retrieved from the store,
     * arranged in a grid layout, and adds the page to the shop panel.
     *
     * @author Aya Abdulnabi
     */

    private void createPage2() {
        JLayeredPane page = new JLayeredPane();
        page.setPreferredSize(new Dimension(1080, 750));
        page.setOpaque(false);

        int x = 200, y = 130;
        int itemsPerRow = 3;
        int count = 0;

        // Add all 5 toys
        List<String> toyNames = new ArrayList<>(store.getAllToys().keySet());
        for (int i = 0; i < Math.min(6, toyNames.size()); i++) {
            String toyName = toyNames.get(i);
            Toys toy = store.getToy(toyName);
            JPanel itemPanel = createShopItem(toy.getName(), toy.getPrice(), x, y);
            page.add(itemPanel, Integer.valueOf(2));

            x += 250;
            count++;
            if (count % itemsPerRow == 0) {
                x = 200;
                y += 240;
            }
        }

        shopPanel.add(page, "Page 2");
    }

    /**
     * Creates and populates the third store page with gift items (outfits).
     *
     * Retrieves all gifts from the store and displays them in a grid layout.
     * The completed page is added to the shop panel as "Page 3".
     *
     * @author Aya Abdulnabi
     */
    private void createPage3() {
        JLayeredPane page = new JLayeredPane();
        page.setPreferredSize(new Dimension(1080, 750));
        page.setOpaque(false);

        int x = 200, y = 130;
        int itemsPerRow = 3;
        int count = 0;

        // Add all gifts (outfits)
        List<String> giftNames = new ArrayList<>(store.getAllGifts().keySet());
        for (int i = 0; i < giftNames.size(); i++) {
            String giftName = giftNames.get(i);
            Gifts gift = store.getGift(giftName);
            JPanel itemPanel = createShopItem(gift.getName(), gift.getPrice(), x, y);
            page.add(itemPanel, Integer.valueOf(2));

            x += 250;
            count++;
            if (count % itemsPerRow == 0) {
                x = 200;
                y += 240;
            }
        }

        shopPanel.add(page, "Page 3");
    }

    /**
     * Creates a visual panel for a shop item, including image, price, and click behavior.
     *
     * Displays the item's image centered on a styled button. When clicked, a popup
     * with item details is shown. The method supports food, toys, and gift items.
     *
     * @param itemName the name of the item to display
     * @param price the price of the item
     * @param x the x-coordinate for positioning the panel
     * @param y the y-coordinate for positioning the panel
     * @return a JPanel representing the shop item
     *
     * @author Aya Abdulnabi
     */
    private JPanel createShopItem(String itemName, int price, int x, int y) {
        JPanel panel = new JPanel();
        panel.setLayout(new OverlayLayout(panel));
        panel.setBounds(x, y, 165, 221);
        panel.setOpaque(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(165, 221));
        layeredPane.setOpaque(false);

        // Base background image
        ImageIcon baseIcon = loadAndScaleImage("resources/item_image.png", 165, 221);
        JButton imageButton = new JButton(baseIcon);
        imageButton.setBounds(0, 0, 165, 221);
        imageButton.setContentAreaFilled(false);
        imageButton.setBorderPainted(false);
        imageButton.setFocusPainted(false);
        imageButton.addActionListener(e -> {
            MusicPlayer.playSoundEffect("resources/button_clicked.wav");
            showPopup(itemName, price, this.gameData.getPet());
        });

        // Determine item type and load appropriate image
        String imagePath = getItemImagePath(itemName);
        if (imagePath != null) {
            ImageIcon itemIcon = loadAndScaleImage(imagePath, 70, 70);
            if (itemIcon != null) {
                JLabel itemImageLabel = new JLabel(itemIcon);
                int iconX = (165 - 70) / 2;
                int iconY = (int)((221 - 70) * 0.3);
                itemImageLabel.setBounds(iconX, iconY, 70, 70);
                layeredPane.add(itemImageLabel, Integer.valueOf(1));
            }
        }

        // Price label
        JLabel priceLabel = new JLabel(String.valueOf(price), SwingConstants.CENTER);
        priceLabel.setBounds(5, 175, 165, 30);
        priceLabel.setFont(customFont.deriveFont(Font.BOLD, 16f));
        priceLabel.setForeground(Color.BLACK);
        priceLabel.setOpaque(false);

        // Layer components
        layeredPane.add(imageButton, Integer.valueOf(0));
        layeredPane.add(priceLabel, Integer.valueOf(2));
        panel.add(layeredPane);
        allButtons.add(imageButton);

        return panel;
    }

    /**
     * Returns the file path for the image corresponding to a given item name.
     *
     * Determines whether the item is a food, toy, or gift, and constructs the image
     * path accordingly. Special cases are handled for specific outfit names.
     *
     * @param itemName the name of the item to retrieve the image path for
     * @return the file path to the item's image, or null if the item is not found
     *
     * @author Aya Abdulnabi
     */
    private String getItemImagePath(String itemName) {
        // Check food items first
        if (store.hasFood(itemName)) {
            return "resources/food_" + itemName.toLowerCase().replace(" ", "_") + ".png";
        }
        // Then check toys
        else if (store.hasToys(itemName)) {
            return "resources/toy_" + itemName.toLowerCase().replace(" ", "_") + ".png";
        }
        // Then check gifts (outfits)
        else if (store.hasGift(itemName)) {
            // Special case for outfit images
            if (itemName.equals("outfit1") || itemName.equals("outfit2") || itemName.equals("outfit3")) {
                return "resources/" + itemName + ".png";
            }
            return "resources/gift_" + itemName.toLowerCase().replace(" ", "_") + ".png";
        }
        return null;
    }

    /**
     * Loads an image from the given file path and scales it to the specified dimensions.
     *
     * If loading fails, an error message is printed and null is returned.
     *
     * @param path the file path of the image to load
     * @param width the desired width of the scaled image
     * @param height the desired height of the scaled image
     * @return the scaled ImageIcon, or null if loading fails
     *
     * @author Aya Abdulnabi
     */
    private ImageIcon loadAndScaleImage(String path, int width, int height) {
        try {
            ImageIcon originalIcon = new ImageIcon(path);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.out.println("Error loading image: " + path);
            return null;
        }
    }

    /**
     * Attempts to purchase a store item and updates the player's inventory if successful.
     *
     * Checks the item type (food, toy, or gift) and calls the appropriate purchase method.
     * Logs the player's coin count before and after the attempt.
     *
     * @param itemName the name of the item to purchase
     * @param price the price of the item (not directly used in logic but passed for context)
     * @return true if the purchase was successful, false otherwise
     *
     * @author Aya Abdulnabi
     */
    private boolean attemptPurchase(String itemName, int price) {
        PlayerInventory inventory = gameData.getInventory();
        System.out.println("Player coins before purchase: " + inventory.getPlayerCoins());

        boolean purchaseSuccess = false;

        if (store.hasFood(itemName)) {
            purchaseSuccess = store.buyFood(itemName, inventory, 1);
        } else if (store.hasToys(itemName)) {
            purchaseSuccess = store.buyToy(itemName, inventory, 1);
        } else if (store.hasGift(itemName)) {
            purchaseSuccess = store.buyGift(itemName, inventory, 1);
        }

        if (purchaseSuccess) {
            System.out.println("Player coins after purchase: " + inventory.getPlayerCoins());
            return true;
        }
        return false;
    }

    /**
     * Enables or disables all interactive buttons on the store screen.
     *
     * Iterates through all stored buttons and sets their enabled state.
     * Also explicitly updates the navigation buttons (next and previous).
     *
     * @param enabled true to enable all buttons, false to disable them
     *
     * @author Aya Abdulnabi
     */
    private void setAllButtonsEnabled(boolean enabled) {
        for (int i = 0; i < allButtons.size(); i++) {
            allButtons.get(i).setEnabled(enabled);
        }
        // Also handle navigation buttons separately since they might not be in allButtons
        nextButton.setEnabled(enabled);
        prevButton.setEnabled(enabled);
    }


    /**
     * Displays a popup window with details about a selected store item.
     *
     * The popup includes the item image, name, description, and stats. It provides
     * options to buy or close. If the item is an outfit, validation is performed
     * to ensure it's compatible with the current pet. If the item is a toy, a
     * purchase is denied if the toy is already owned.
     *
     * The popup disables all store buttons while active and restores them afterward.
     *
     * @param itemName the name of the item being previewed
     * @param price the price of the item
     * @param pet the current pet used for outfit validation
     *
     * @author Aya Abdulnabi
     */
    private void showPopup(String itemName, int price, Pet pet) {
        // Disable all buttons first
        setAllButtonsEnabled(false);

        JLayeredPane popup = new JLayeredPane();
        popup.setBounds(372, 86, 336, 579);
        popup.setOpaque(false);

        // Background image
        JLabel popupImage = new JLabel(new ImageIcon("resources/store_popup.png"));
        popupImage.setBounds(0, 0, 336, 579);
        popup.add(popupImage, Integer.valueOf(0));

        // Load and display item image
        String imagePath = getItemImagePath(itemName);
        ImageIcon itemIcon = loadAndScaleImage(imagePath, 130, 130);
        JLabel itemImageLabel = new JLabel(itemIcon);
        itemImageLabel.setBounds(100, 60, 130, 130);
        popup.add(itemImageLabel, Integer.valueOf(1));

        // Item name
        JLabel nameLabel = new JLabel(itemName, SwingConstants.CENTER);
        nameLabel.setBounds(0, 240, 336, 30);
        nameLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        popup.add(nameLabel, Integer.valueOf(1));

        // Initialize description and stats
        String description = "";
        String stats = "";

        if (store.hasFood(itemName)) {
            Food food = store.getFood(itemName);
            if (food.getDescription().isEmpty()) {
                description = "A tasty treat!";
            } else {
                description = food.getDescription();
            }

            stats = "Fullness: " + " + " + food.getFullness();
        }
        else {
            if (store.hasToys(itemName)) {
                Toys toy = store.getToy(itemName);
                if (toy.getDescription().isEmpty()) {
                    description = "Fun to play with!";
                } else {
                    description = toy.getDescription();
                }

                stats = "Happiness: " + " + " + "25";
            }
            else {
                if (store.hasGift(itemName)) {
                    description = "A special outfit for your pet!";
                    stats = "Unique appearance!";
                }
            }
        }


        JTextArea descArea = new JTextArea(description);
        descArea.setBounds(40, 360, 275, 70);
        descArea.setFont(customFont.deriveFont(Font.PLAIN, 15f));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setOpaque(false);
        descArea.setEditable(false);
        descArea.setFocusable(false);
        popup.add(descArea, Integer.valueOf(1));

        // Stats
        JLabel statsLabel = new JLabel(stats, SwingConstants.CENTER);
        statsLabel.setBounds(0, 460, 336, 70);
        statsLabel.setFont(customFont.deriveFont(Font.BOLD, 14f));
        popup.add(statsLabel, Integer.valueOf(1));

        JButton buyButton = new JButton("Buy");
        buyButton.setBounds(90, 540, 80, 30);
        buyButton.addActionListener(e -> {
            System.out.println("Buy button clicked, Outfit clicked is: "+ itemName);

            // Check if the item is an outfit
            if (isOutfit(itemName)) {
                String allowedOutfit = getAllowedOutfit(pet);

                if (!itemName.equals(allowedOutfit)) {
                    System.out.println("Should have failed");
                    showStyledMessage("Purchase Failed",
                            "You have chosen the wrong outfit for this pet!", true);
                    return;
                }
            }

            if (store.hasToys(itemName)) {
                Toys toy = store.getToy(itemName);
                if (playerInventory.hasToy(toy)) {
                    showStyledMessage("Purchase Denied",
                            "You already own this toy!", true);
                    return;
                }
            }

            boolean purchaseSuccess = attemptPurchase(itemName, price);
            if (purchaseSuccess) {
                updateCoinDisplay();

                if (isOutfit(itemName)) {
                    gameData.getInventory().addOutfit(itemName);
                    showStyledMessage("Purchase Successful",
                            "You have purchased the right outfit! Gift your pet now!", false);
                } else {
                    showStyledMessage("Purchase Successful",
                            "You bought " + itemName + "!", false);
                }
            } else {
                showStyledMessage("Purchase Failed",
                        "Not enough coins!", true);
            }

            remove(popup);
            setAllButtonsEnabled(true);
            revalidate();
            repaint();
        });

        popup.add(buyButton, Integer.valueOf(2));


        // Close Button
        JButton closeButton = new JButton("Close");
        closeButton.setBounds(190, 540, 80, 30);
        closeButton.addActionListener(e -> {
            remove(popup);
            setAllButtonsEnabled(true);
            revalidate();
            repaint();
        });
        popup.add(closeButton, Integer.valueOf(1));

        add(popup, Integer.valueOf(5));
        revalidate();
        repaint();
    }


    /**
     * Checks if the given item name corresponds to a predefined outfit.
     *
     * Used to validate purchases and determine if special outfit logic should apply.
     *
     * @param itemName the name of the item to check
     * @return true if the item is an outfit, false otherwise
     *
     * @author Mohammed Abdulnabi
     */
    private boolean isOutfit(String itemName) {
        return itemName.equals("outfit1") || itemName.equals("outfit2") || itemName.equals("outfit3");
    }


    /**
     * Returns the outfit allowed for the given pet based on its type.
     *
     * Each pet type has a corresponding outfit. Used to validate if the selected
     * outfit is suitable for the pet during purchase.
     *
     * @param pet the pet whose allowed outfit is to be determined
     * @return the name of the outfit allowed for the pet, or "None" if unknown
     *
     * @author Mohammed Abdulnabi
     */
    private String getAllowedOutfit(Pet pet) {
        switch (pet.getPetType()) {
            case "PetOption1": return "outfit1";
            case "PetOption2": return "outfit2";
            case "PetOption3": return "outfit3";
            default: return "None"; // In case of an unknown pet type
        }
    }


    /**
     * Updates the visual display of the player's current coin count on the store screen.
     *
     * Removes any previous coin labels and backgrounds, then loads and adds the updated
     * coin display image and label with the current coin value from the player's inventory.
     *
     * @author Aya Abdulnabi
     */
    private void updateCoinDisplay() {
        // Remove existing components if they exist
        if (coinLabel != null) {
            remove(coinLabel);
        }

        // Remove any existing coin display background
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            Component comp = components[i];
            if (comp instanceof JLabel && ((JLabel) comp).getIcon() != null &&
                    ((JLabel) comp).getIcon().toString().contains("coins_display")) {
                remove(comp);
            }
        }


        // Load and scale the coin display image
        ImageIcon originalCoinIcon = new ImageIcon("resources/coins_display.png");
        Image scaledCoinImage = originalCoinIcon.getImage().getScaledInstance(190, 46, Image.SCALE_SMOOTH);
        ImageIcon scaledCoinIcon = new ImageIcon(scaledCoinImage);

        // Create the label with scaled image
        JLabel coinDisplayLabel = new JLabel(scaledCoinIcon);
        coinDisplayLabel.setBounds(10, 20, 200, 46);
        add(coinDisplayLabel, Integer.valueOf(3));

        // Update coin count
        int coins = gameData.getInventory().getPlayerCoins();
        coinLabel = new JLabel(String.valueOf(coins));
        coinLabel.setFont(customFont.deriveFont(Font.BOLD, 17f));
        coinLabel.setForeground(Color.BLACK);
        coinLabel.setBounds(80, 30, 100, 30);
        add(coinLabel, Integer.valueOf(4));

        revalidate();
        repaint();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            updateCoinDisplay(); // Refresh coins when store opens
        }
    }

    private void showStyledMessage(String title, String message, boolean isError) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(240, 240, 240));

        // Create styled message with HTML formatting
        String color;
        if (isError) {
            color = "#A94337";
        } else {
            color = "#2E86C1";
        }

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<font size=4 color='" + color + "'><b>" + title + "</b></font><br>"
                + "<font size=3 color='#5D6D7E'>" + message + "</font></div></html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);

        // Create styled button
        JButton okButton = new JButton("OK");
        okButton.setFont(customFont.deriveFont(Font.BOLD, 14f));
        okButton.setForeground(Color.WHITE);

        if (isError) {
            okButton.setBackground(new Color(231, 76, 60));
        } else {
            okButton.setBackground(new Color(52, 152, 219));
        }

        okButton.setFocusPainted(false);
        okButton.setContentAreaFilled(false);
        okButton.setOpaque(true);
        okButton.setBorderPainted(false);
        okButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = SwingUtilities.getWindowAncestor(panel);
                if (window != null) {
                    window.dispose();
                }
            }
        });

        // Button panel
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
}