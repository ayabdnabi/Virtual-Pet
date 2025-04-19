package src;


/**
 * The TutorialCommands class holds tutorial text for different interactive actions
 * in the virtual pet game. These instructions are used to guide the player on how to
 * interact with their pet using both button clicks and keyboard shortcuts.
 *
 * <p>Each tutorial string corresponds to a specific pet interaction such as:
 * <ul>
 *   <li>Gift giving</li>
 *   <li>Feeding</li>
 *   <li>Visiting the vet</li>
 *   <li>Exercising</li>
 *   <li>Sleeping</li>
 *   <li>Playing</li>
 * </ul>
 *
 * @author Kamaldeep Ghotra
 * @author Mohammed Abdulnabi
 * @author Aya Abdulnabi
 * @version 1.0
 */
public class TutorialCommands {
    /** String for the gift giving tutorial */
    public String GiftGivingText = "Who doesn't love presents? You can give your pet a gift by clicking the Gift button-or the G key-to dress it up! This will make your pet's happiness meter MAX OUT! If your pet gets angry, it will ignore you until you give it a gift!";
    /** String for the feeding tutorial */
    public String FeedingScreenText = "Your pet will get hungry like all pets do!. You can give your pet food by clicking the Feed button-or the F key-in order to make it fuller! Try different foods, some are super filling, while others are just snacks. ";
    /** String for the vet tutorial */
    public String VetScreenText = "Oh no, your pet is sick! Click the Vet button-or V key-for a quick heal! The vet fixes everything, but you'll have to wait for a bit before going again, so don't leave it till last minute!";
    /** String for the exercise tutorial */
    public String ExerciseScreenText = "Get up and move! Click the Exercise button-or E key-to make your pet do some workout. Exercise keeps your pet fit, but will make your pet more sleepy and fullness decreases, so be careful!";
    /** String for the sleeping tutorial */
    public String SleepScreenText = "All that playing and exercising, of course they'll get tired! Click the Sleep button-or the S key-to let them rest. Be patient though, because your pet won't wake up until it's fully rested!";
    /** String for the playing tutorial */
    public String PlayScreenText = "Naturally pets need to play. You can play with your pet by clicking the Play button-or the P key-to let them have fun! Playing makes your happiness meter increase! If your pet gets angry, it will ignore you until you play with it!";


    /**
     * Default constructor for the TutorialCommands class.
     * Initializes a new instance with predefined tutorial messages
     * for each pet interaction feature in the game.
     */
    public TutorialCommands() {}


    /**
     * Retrieves the tutorial message explaining how gift-giving works in the game.
     *
     * @return A String containing the gift-giving tutorial text.
     */
    public String getGiftGivingText() {
        return GiftGivingText;
    }


    /**
     * Retrieves the tutorial message explaining how feeding works in the game.
     *
     * @return A String containing the feeding tutorial text.
     */
    public String getFeedingScreenText() {
        return FeedingScreenText;
    }


    /**
     * Retrieves the tutorial message that explains how the vet feature works in the game.
     *
     * @return A String containing the vet tutorial instructions.
     */
    public String getVetScreenText() {
        return VetScreenText;
    }


    /**
     * Returns the tutorial message related to the exercise feature.
     * This message explains how exercising affects the pet’s stats.
     *
     * @return A String describing how the exercise function works in the game.
     */
    public String getExerciseScreenText() {
        return ExerciseScreenText;
    }


    /**
     * Returns the tutorial message related to the sleep feature.
     * This message explains how and when to let the pet rest.
     *
     * @return A String describing how the sleep function works in the game.
     */
    public String getSleepScreenText() {
        return SleepScreenText;
    }


    /**
     * Returns the tutorial message for the play feature.
     * This message instructs players on how to play with their pet,
     * including the effects of play on the pet's happiness.
     *
     * @return A String containing the play screen tutorial text.
     */
    public String getPlayScreenText() {
        return PlayScreenText;
    }


}