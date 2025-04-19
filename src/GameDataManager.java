package src;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.io.File;


/**
 * Manages storing the game and its corresponding data.
 *
 * The following class adds functionality for managing game data, parental control settings,
 * and player inventory. This class uses Gson to alter data in JSon files,
 * including saving and loading game data, as well as handling parental control and inventory operations.
 *
 * @author Mohammed Abdulnabi
 * @author Kamaldeep Ghotra
 * @version 1.0
 */
public class GameDataManager {
    /** Store that is shared across the games */
    private static final Store sharedStore = new Store();

    /* Made with the help of Artificial Intelligence */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<Food, Integer>>() {}.getType(), new FoodInventoryAdapter())
            .registerTypeAdapter(new TypeToken<Map<Toys, Integer>>() {}.getType(), new ToyInventoryAdapter())
            .registerTypeAdapter(new TypeToken<Map<Gifts, Integer>>() {}.getType(), new GiftInventoryAdapter())
            .registerTypeAdapter(Pet.class, new PetAdapter())
            .registerTypeAdapter(PlayerInventory.class, new PlayerInventoryAdapter(sharedStore))
            .setPrettyPrinting()
            .create();

    // Track when this session started
    private static long sessionStartTime = System.currentTimeMillis();


    /**
     * Saves the game data to a file with the given filename.
     *
     * The session duration since the last save is calculated and added to the previous play time.
     * The game data (pet, inventory, and updated play time) is then converted to JSON and written to the file.
     * After saving, the session start time is reset.
     *
     * @param filename  the file path to save the game data
     * @param pet  the pet instance
     * @param inventory  the player's inventory
     * @param previousPlayTime  the previous total play time in milliseconds
     */
    public static void saveGame(String filename, Pet pet, PlayerInventory inventory, long previousPlayTime) {
        long sessionDuration = System.currentTimeMillis() - sessionStartTime;
        long updatedPlayTime = previousPlayTime + sessionDuration;

        GameData data = new GameData(pet, inventory, updatedPlayTime);
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(data, writer);

        } catch (IOException e) {
            System.out.println("Error saving game");
        }

        // Reset session start time
        sessionStartTime = System.currentTimeMillis();
    }


    /**
     * Loads game data from the specified file.
     * <p>
     * The method attempts to decode the JSON content of the file into a GameData object.
     * If the file cannot be read, it returns null.
     * </p>
     *
     * @param filename the file path from which to load the game data
     * @return the loaded GameData object, or null if loading fails
     */
    public static GameData loadGame(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            GameData data = gson.fromJson(reader, GameData.class);
            sessionStartTime = System.currentTimeMillis();

            return data;
        } catch (IOException e) {
            System.out.println("Error loading game");
            return null;
        }
    }

    private static final String PARENTAL_CONTROL_FILE = "config/parental_control.json";


    /**
     * Saves the parental control settings to a predefined configuration file.
     * <p>
     * The settings are converted to JSON and saved to "config/parental_control.json".
     * </p>
     *
     * @param parentalControl the parental control settings to be saved
     */
    public static void saveParentalControlSettings(ParentalControl parentalControl) {
        try (FileWriter writer = new FileWriter(PARENTAL_CONTROL_FILE)) {
            gson.toJson(parentalControl, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Loads parental control settings from the predefined configuration file.
     *
     * If the configuration file does not exist, a new default ParentalControl instance is returned.
     *
     * @return the loaded ParentalControl settings, or a default instance if loading fails
     */
    public static ParentalControl loadParentalControlSettings() {
        File file = new File(PARENTAL_CONTROL_FILE);
        if (!file.exists()) {
            return new ParentalControl(); // Return default if no file
        }
        try (FileReader reader = new FileReader(file)) {
            ParentalControl parentalControl = gson.fromJson(reader, ParentalControl.class);
            return parentalControl != null ? parentalControl : new ParentalControl();
        } catch (IOException e) {
            e.printStackTrace();
            return new ParentalControl();
        }
    }


    /**
     * Determines whether a new game can be created based on the number of save files.
     *
     * The method checks if the "saves/" directory exists and counts the number of JSON files.
     * A new game can be created if there are fewer than 3 save files.
     *
     * @return true if a new game can be created; false otherwise
     */
    public static boolean canCreateNewGame() {
        File dir = new File("saves/");
        if (dir.exists() && dir.isDirectory()) {
            return dir.listFiles((dir1, name) -> name.endsWith(".json")).length < 3;
        }
        return true; // Allow if the directory doesn't exist
    }


    /**
     * Returns the shared Store instance used across the game.
     *
     * @return the shared Store instance
     */
    public static Store getSharedStore() {
        return sharedStore;
    }


    /**
     * Updates the inventory section of a saved game file with the provided inventory.
     * <p>
     * The existing game data is loaded, and a new GameData object is created using the existing pet and play time,
     * but with the updated inventory. This updated game data is then saved back to the file.
     * </p>
     *
     * @param filename  the file path of the saved game to update
     * @param updatedInventory  the updated player inventory
     */
    public static void saveInventoryToGameFile(String filename, PlayerInventory updatedInventory) {
        GameData existingData = loadGame(filename);
        if (existingData != null) {
            Pet pet = existingData.getPet(); // keep the pet stats
            long totalPlayTime = existingData.getTotalPlayTime(); // keep play time
            GameData updatedData = new GameData(pet, updatedInventory, totalPlayTime);
            System.out.println("Game was saved?");
            try (FileWriter writer = new FileWriter(filename)) {
                gson.toJson(updatedData, writer);
            } catch (IOException e) {
                System.err.println("Failed to save inventory to file: " + e.getMessage());
            }
        }
    }
}
