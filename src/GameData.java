package src;


/**
 * Capture the game state data.
 *
 * <p>
 * Provides some functionality to encapsulate the game state data,
 * including the pet, the player's inventory, and the total play time.
 * This class is used to store and retrieve key components of the game state.
 *</p>
 * @author Mohammed Abdulnabi
 * @version 1.0
 */
public class GameData {
    /** Type of pet to be saved */
    private Pet pet;
    /** What was in the players inventory so it can be saved when they leave and come back after saving */
    private PlayerInventory inventory;
    /** Total playtime during playing */
    private long totalPlayTime; // in milliseconds, or use int for seconds


    /**
     * Constructs a new GameData instance with the specified pet, inventory, and total play time.
     *
     * @param pet  the pet associated with this game data
     * @param inventory  the player's inventory
     * @param totalPlayTime  the total play time in milliseconds
     */
    public GameData(Pet pet, PlayerInventory inventory, long totalPlayTime) {
        this.pet = pet;
        this.inventory = inventory;
        this.totalPlayTime = totalPlayTime;
    }


    /**
     * Returns the pet.
     *
     * @return the pet object
     */
    public Pet getPet() {
        return pet;
    }


    /**
     * Returns the player's inventory.
     *
     * @return the inventory object
     */
    public PlayerInventory getInventory() {
        return inventory;
    }


    /**
     * Returns the total play time.
     *
     * @return the total play time in milliseconds
     */
    public long getTotalPlayTime() {
        return totalPlayTime;
    }


    /**
     * Sets the total play time.
     *
     * @param totalPlayTime the total play time in milliseconds to set
     */
    public void setTotalPlayTime(long totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

}
