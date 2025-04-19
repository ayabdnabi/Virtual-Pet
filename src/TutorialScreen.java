package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
/**
 * This class constructs the tutorial screen UI for the Virtual Pet application.
 *<p>
 * The {@code TutorialScreen} class constructs the interactive tutorial interface for the Virtual Pet application.
 * It uses a {@link CardLayout} to switch between multiple screens that guide users through various commands
 * and features, including feeding, playing, gifting, vet visits, sleeping, and exercising.
 *</p>
 *
 * @author
 * Clair Yu,
 * Aya Abdulnabi
 * @version 1.0
 */
public class TutorialScreen extends JLayeredPane {
    /** Custom used throughout this class */
    private Font customFont;
    /** Layout to swap different screens in the game */
    private static CardLayout cardLayout;
    /** Main panel that holds all the screens*/
    private static JPanel mainPanel;
    /** Tutorial text from the backend*/
    private TutorialCommands tutorialText;

    /**
     * Constructs the tutorial screen, initializes navigation, loads assets and fonts,
     * and adds all tutorial steps to the panel.
     *
     * @param customFont the font to apply to on-screen text
     */
    public TutorialScreen(Font customFont) {
        // set the custom font and the dimensions of the screen
        this.customFont = customFont;
        setPreferredSize(new Dimension(1080, 750));
        try {
            InputStream fontStream = getClass().getResourceAsStream("/Early GameBoy.ttf");
            if (fontStream == null) {
                throw new IOException("Font file not found!");
            }
            this.customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            System.out.println("Font file not found!");
            this.customFont = new Font("Arial", Font.PLAIN, 24);
        }

        // From the backend
        this.tutorialText = new TutorialCommands();

        // Set up and create differnt screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBounds(0, 0, 1080, 750); // Set bounds for the mainPanel
        add(mainPanel, Integer.valueOf(1));

        // Creating home button to lead back to the main screen
        JButton homeButton = MainScreen.buttonCreate(20, 20, 192, 64, "resources/white_button.png", "resources/white_button_clicked.png", "Home");
        JLabel homeText = new JLabel("< HOME");
        homeText.setFont(customFont);
        homeText.setForeground(Color.BLACK);
        homeText.setBounds(20,20,192,64);
        homeText.setHorizontalAlignment(SwingConstants.CENTER);
        homeText.setVerticalAlignment(SwingConstants.CENTER);
        add(homeText, Integer.valueOf(3));
        add(homeButton, Integer.valueOf(2));

        // Reset back to the first tutorial page
        resetToGiveGift();

        // Creating the different screens
        JLayeredPane givingGift = giftGiving();
        JLayeredPane feedingScreen = feedingScreen();
        JLayeredPane vetScreen = vetScreen();
        JLayeredPane exerciseScreen = exerciseScreen();
        JLayeredPane sleepingScreen = sleepScreen();
        JLayeredPane playScreen = playingScreen();

        // Add all screens onto the panel
        mainPanel.add(givingGift, "Give Gift");
        mainPanel.add(feedingScreen, "Feeding");
        mainPanel.add(vetScreen, "Vet");
        mainPanel.add(exerciseScreen, "Exercise");
        mainPanel.add(sleepingScreen, "Sleeping");
        mainPanel.add(playScreen, "Play");

        // Show the initial screen
        cardLayout.show(mainPanel, "Give Gift");
        setVisible(true);
    }

    /**
     * Builds the tutorial screen for the "Give Gift" action.
     * @return the complete screen as a {@link JLayeredPane}
     */
    private JLayeredPane giftGiving() {
        JLayeredPane giftGivingScreen = new JLayeredPane();
        giftGivingScreen.setPreferredSize(new Dimension(1080, 750));

        // Button to switch to the feeding screen
        JButton rightArrow = createButtonWithCardLayout(800, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Feeding", cardLayout, mainPanel);
        giftGivingScreen.add(rightArrow, Integer.valueOf(1));

        // Button to go to the next screen
        ImageIcon rightArrowImage = new ImageIcon("resources/right_arrow.png");
        JLabel rightArrowLabel = new JLabel(rightArrowImage);
        rightArrowLabel.setBounds(817, 265, 32, 32);
        giftGivingScreen.add(rightArrowLabel, Integer.valueOf(2));

        // Build base tutorial screen
        JLayeredPane screen = tutorialScreen("resources/give_gift_tutorial.png", giftGivingScreen);
        JTextArea textArea = new JTextArea(tutorialText.getGiftGivingText());
        return setText(screen, textArea);
    }

    /**
     * Builds the tutorial screen for the "Feeding" action.
     * @return the complete screen as a {@link JLayeredPane}
     */
    private JLayeredPane feedingScreen() {
        JLayeredPane feedingScreen = new JLayeredPane();
        feedingScreen.setPreferredSize(new Dimension(1080, 750));

        // Button to switch to the vet (next) screen
        JButton rightArrow = createButtonWithCardLayout(800, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Vet", cardLayout, mainPanel);
        feedingScreen.add(rightArrow, Integer.valueOf(1));

        // Button to switch back to the gift giving (previous) screen
        JButton leftArrow = createButtonWithCardLayout(175, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Give Gift", cardLayout, mainPanel);
        feedingScreen.add(leftArrow, Integer.valueOf(1));

        arrowImageIcon(feedingScreen);

        // Build base tutorial screen
        JLayeredPane screen = tutorialScreen("resources/feeding_tutorial.png", feedingScreen);
        JTextArea textArea = new JTextArea(tutorialText.getFeedingScreenText());
        return setText(screen, textArea);
    }

    /**
     * Builds the tutorial screen for the "Vet" action.
     * @return the complete screen as a {@link JLayeredPane}
     */
    private JLayeredPane vetScreen() {
        JLayeredPane vetScreen = new JLayeredPane();
        vetScreen.setPreferredSize(new Dimension(1080, 750));

        // Button to switch to the vet (next) screen
        JButton rightArrow = createButtonWithCardLayout(800, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Exercise", cardLayout, mainPanel);
        vetScreen.add(rightArrow, Integer.valueOf(1));

        // Button to switch back to the gift giving (previous) screen
        JButton leftArrow = createButtonWithCardLayout(175, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Feeding", cardLayout, mainPanel);
        vetScreen.add(leftArrow, Integer.valueOf(1));

        arrowImageIcon(vetScreen);

        // Build base tutorial screen
        JLayeredPane screen = tutorialScreen("resources/vet_tutorial.png", vetScreen);

        JTextArea textArea = new JTextArea(tutorialText.getVetScreenText());
        return setText(screen, textArea);

    }

    /**
     * Builds the tutorial screen for the "Exercise" action.
     * @return the complete screen as a {@link JLayeredPane}
     */
    private JLayeredPane exerciseScreen() {
        JLayeredPane exerciseScreen = new JLayeredPane();
        exerciseScreen.setPreferredSize(new Dimension(1080, 750));

        // Button to switch to the vet (next) screen
        JButton rightArrow = createButtonWithCardLayout(800, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Sleeping", cardLayout, mainPanel);
        exerciseScreen.add(rightArrow, Integer.valueOf(1));

        // Button to switch back to the gift giving (previous) screen
        JButton leftArrow = createButtonWithCardLayout(175, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Vet", cardLayout, mainPanel);
        exerciseScreen.add(leftArrow, Integer.valueOf(1));

        arrowImageIcon(exerciseScreen);

        // Build base tutorial screen
        JLayeredPane screen = tutorialScreen("resources/exercise_tutorial.png", exerciseScreen);

        JTextArea textArea = new JTextArea(tutorialText.getExerciseScreenText());
        return setText(screen, textArea);
    }

    /**
     * Builds the tutorial screen for the "Sleep" action.
     * @return the complete screen as a {@link JLayeredPane}
     */
    private JLayeredPane sleepScreen() {
        JLayeredPane sleepingScreen = new JLayeredPane();
        sleepingScreen.setPreferredSize(new Dimension(1080, 750));

        // Button to switch to the vet (next) screen
        JButton rightArrow = createButtonWithCardLayout(800, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Play", cardLayout, mainPanel);
        sleepingScreen.add(rightArrow, Integer.valueOf(1));

        // Button to switch back to the gift giving (previous) screen
        JButton leftArrow = createButtonWithCardLayout(175, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Exercise", cardLayout, mainPanel);
        sleepingScreen.add(leftArrow, Integer.valueOf(1));

        arrowImageIcon(sleepingScreen);

        // Build base tutorial screen
        JLayeredPane screen = tutorialScreen("resources/sleeping_tutorial.png", sleepingScreen);

        JTextArea textArea = new JTextArea(tutorialText.getSleepScreenText());
        return setText(screen, textArea);
    }

    /**
     * Builds the tutorial screen for the "Play" action.
     * @return the complete screen as a {@link JLayeredPane}
     */
    private JLayeredPane playingScreen() {
        JLayeredPane playScreen = new JLayeredPane();
        playScreen.setPreferredSize(new Dimension(1080, 750));

        // button to switch to the feeding screen
        JButton leftArrow = createButtonWithCardLayout(175, 250, 64, 64, "resources/arrow_button.png", "resources/arrow_button_click.png", "Sleeping", cardLayout, mainPanel);
        playScreen.add(leftArrow, Integer.valueOf(1));

        ImageIcon leftArrowImage = new ImageIcon("resources/left_arrow.png");
        JLabel leftArrowLabel = new JLabel(leftArrowImage);
        leftArrowLabel.setBounds(190, 265, 32, 32);
        playScreen.add(leftArrowLabel, Integer.valueOf(2));

        // Build base tutorial screen
        JLayeredPane screen = tutorialScreen("resources/playing_tutorial.png", playScreen);

        JTextArea textArea = new JTextArea(tutorialText.getPlayScreenText());
        return setText(screen, textArea);
    }

    /**
     * Adds left and right arrow icons to a given screen.
     * @param screenSource the pane to which the arrows will be added
     */
    public void arrowImageIcon(JLayeredPane screenSource) {
        // Load the default arrows and position them correctly
        ImageIcon rightArrowImage = new ImageIcon("resources/right_arrow.png");
        ImageIcon leftArrowImage = new ImageIcon("resources/left_arrow.png");

        JLabel rightArrowLabel = new JLabel(rightArrowImage);
        JLabel leftArrowLabel = new JLabel(leftArrowImage);

        rightArrowLabel.setBounds(817, 265, 32, 32);
        leftArrowLabel.setBounds(190, 265, 32, 32);

        screenSource.add(rightArrowLabel, Integer.valueOf(2));
        screenSource.add(leftArrowLabel, Integer.valueOf(2));

    }

    /**
     * Utility method that creates a navigation button that switches screens
     * in a CardLayout-managed panel.
     *
     * @param x                 X-coordinate
     * @param y                 Y-coordinate
     * @param width             Width of button
     * @param height            Height of button
     * @param defaultImageSource  Button image path
     * @param pressedImageSource  Pressed state image path
     * @param location            CardLayout panel name to switch to
     * @param cardLayout          The layout manager
     * @param mainPanel           The parent container
     * @return the created {@link JButton}
     */
    public static JButton createButtonWithCardLayout(int x, int y, int width, int height, String defaultImageSource, String pressedImageSource, String location, CardLayout cardLayout, JPanel mainPanel) {
        // Call the existing buttonCreate function
        JButton button = MainScreen.buttonCreate(x, y, width, height, defaultImageSource, pressedImageSource, location);

        // Add ActionListener to handle screen switching
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the specific screen
                cardLayout.show(mainPanel, location);
            }
        });

        return button;
    }

    /**
     * Resets the tutorial screen to the initial "Give Gift" screen.
     */
    public void resetToGiveGift() {
        cardLayout.show(mainPanel, "Give Gift");
    }

    /**
     * Creates the base for all tutorial screens with background and tutorial image.
     *
     * @param defaultImageSource the tutorial image path
     * @param screenSource the screen container
     * @return the modified screen container
     */
    private JLayeredPane tutorialScreen(String defaultImageSource, JLayeredPane screenSource) {
        // Set the default background for the tutorial screen and scale it
        ImageIcon background = new ImageIcon("resources/grid.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 1080, 750);
        screenSource.add(backgroundLabel, Integer.valueOf(1));
        ImageIcon defaultImageIcon = new ImageIcon(defaultImageSource);
        int width = defaultImageIcon.getIconWidth() - 1000;
        int height = defaultImageIcon.getIconHeight() - 700;
        Image scaledImageIcon = defaultImageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(scaledImageIcon);

        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(-86, -90, width, height);
        screenSource.add(imageLabel, Integer.valueOf(2));

        // Load image that will store where the text will be
        ImageIcon textBoxImageIcon = new ImageIcon("resources/text_box.png");
        JLabel textBoxLabel = new JLabel(textBoxImageIcon);
        textBoxLabel.setBounds(105, 100, 860, 540);
        screenSource.add(textBoxLabel, Integer.valueOf(2));

        return screenSource;

    }

    /**
     * Adds instructional text to the specified screen using a styled {@link JTextArea}.
     *
     * @param screen the layered pane to which text should be added
     * @param textArea the text content
     * @return the updated screen with instructional text
     */
    private JLayeredPane setText(JLayeredPane screen, JTextArea textArea) {
        // Sets the text inside the back written in the backend class
        textArea.setFont(customFont.deriveFont(15f));
        textArea.setForeground(Color.BLACK);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBounds(170, 480, 700, 120);
        screen.add(textArea, Integer.valueOf(3));

        return screen;
    }
}
