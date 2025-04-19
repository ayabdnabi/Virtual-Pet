package src;

import java.util.HashMap;
import java.util.Map;


/**
 * The PlayerInventory class manages the player's in-game resources, including coins and item collections.
 * It handles inventory for food, toys, gifts, and pet outfits, and provides methods to interact with a pet
 * using these items.
 *
 * <p>This class supports player actions such as:
 * <ul>
 *   <li>Feeding the pet using food items</li>
 *   <li>Giving gifts to boost pet happiness</li>
 *   <li>Visiting the vet to restore health (with cooldown)</li>
 *   <li>Equipping and unequipping pet outfits</li>
 *   <li>Playing with the pet and exercising it</li>
 *   <li>Tracking and modifying the number of coins</li>
 * </ul>
 *
 * @author Mohammed Abdulnabi
 * @author Kamaldeep Ghotra
 * @version 1.0
 */
public class PlayerInventory {

    /** Stores the number of coins the player currently has */
    private int playerCoins;

    /** Stores the quantity of each food item the player owns */
    private final Map<Food, Integer> foodInventory = new HashMap<>();

    /** Stores the quantity of each gift item the player owns */
    private final Map<Gifts, Integer> giftInventory = new HashMap<>();

    /** Stores the quantity of each toy item the player owns */
    private final Map<Toys, Integer> toyInventory = new HashMap<>();

    /** Stores the status of pet outfits */
    private final Map<String, Boolean> outfitInventory = new HashMap<>();


    /**
     * Constructs a new PlayerInventory object and initializes the player's starting items.
     * The player begins with 500 coins and predefined items (if available) from the Store.
     * Specifically, the constructor tries to add:
     * - 5 Oranges (as food)
     * - 1 Wand (as toy)
     *
     * @param store  The Store instance used to retrieve the starting food and toy items.
     */
    public PlayerInventory(Store store) {
        this.playerCoins = 6000; // Set default coins to 500

        // Retrieve predefined items from Store
        Food orange = store.getFood("Orange");
        Toys wand = store.getToy("Wand");

        // Ensure these items exist before adding them
        if (orange != null) {
            addFood(orange, 5);
        } else {
            System.err.println("Error: Orange not found in store.");
        }

        // If the wand toy exists in the store, add 1 wand to the player's toy inventory
        if (wand != null) {
            addToy(wand, 1);
        } else {
            System.err.println("Error: Bouncy Ball not found in store.");
        }
    }


    /**
     * Retrieves the current number of coins the player has.
     *
     * @return  The amount of in-game currency (coins) the player currently owns.
     */
    public int getPlayerCoins() {
        return playerCoins;
    }


    /**
     * Sets the player's in-game coin balance to a specified amount.
     *
     * @param playerCoins  The new coin value to assign to the player.
     */
    public void setPlayerCoins(int playerCoins) {
        this.playerCoins = playerCoins;
    }


    /**
     * Adds a specified quantity of a food item to the player's inventory.
     * If the food item already exists in the inventory, the quantity is increased.
     *
     * @param food  The food item to add.
     * @param quantity  The number of units to add to the inventory.
     */
    public void addFood(Food food, int quantity) {
        foodInventory.put(food, foodInventory.getOrDefault(food, 0) + quantity);
    }


    /**
     * Retrieves the current quantity of the specified food item in the player's inventory.
     * If the food item is not present, returns 0.
     *
     * @param food  The food item to check.
     * @return  The number of units of the food item in inventory.
     */
    public int getFoodCount(Food food) {
        return foodInventory.getOrDefault(food, 0);
    }


    /**
     * Adds the specified number of gift items to the player's inventory.
     * If the gift already exists in the inventory, the quantity is increased.
     *
     * @param gift  The gift item to add.
     * @param quantity  The number of units to add to the inventory.
     */
    public void addGift(Gifts gift, int quantity) {
        giftInventory.put(gift, giftInventory.getOrDefault(gift, 0) + quantity);
    }


    /**
     * Adds a specified quantity of a toy to the player's toy inventory.
     * If the toy already exists in the inventory, the quantity is increased.
     *
     * @param toy The toy item to add.
     * @param quantity The number of toy units to add.
     */
    public void addToy(Toys toy, int quantity) {
        toyInventory.put(toy, toyInventory.getOrDefault(toy, 0) + quantity);
    }


    /**
     * Retrieves the number of a specific gift item currently in the player's inventory.
     *
     * @param gift The gift item to check.
     * @return The quantity of the specified gift in the inventory, or 0 if not present.
     */
    public int getGiftCount(Gifts gift) {
        return giftInventory.getOrDefault(gift, 0);
    }


    /**
     * Retrieves the number of a specific toy item currently in the player's inventory.
     *
     * @param toy The toy item to check.
     * @return The quantity of the specified toy in the inventory, or 0 if not present.
     */
    public int getToyCount(Toys toy) {
        return toyInventory.getOrDefault(toy, 0);
    }


    /**
     * Consumes one unit of the specified food item from the player's inventory.
     *
     * @param food The food item to consume.
     * @return true if the food was successfully consumed, false if none were available.
     */
    public boolean consumeFood(Food food) {
        // Get how many of the specified food the player currently has
        int count = getFoodCount(food);

        // If at least one unit is available
        if (count > 0) {
            // Decrease the count of that food by one
            foodInventory.put(food, count - 1);
            return true;  // Food was consumed
        }
        // If no food was available, return false
        return false;
    }


    /**
     * Checks whether the player owns at least one unit of the specified toy.
     *
     * @param toy The toy to check in the player's inventory.
     * @return true if the player has at least one of the toy, false otherwise.
     */
    public boolean hasToy(Toys toy) {
        return getToyCount(toy) > 0;
    }


    /**
     * Equips the specified outfit to the given pet, if the player owns it.
     * If the pet is already wearing an outfit, it is first unequipped.
     *
     * @param outfitName  The name of the outfit to equip.
     * @param pet  The pet to equip the outfit on.
     * @return true if the outfit was successfully equipped, false otherwise.
     */
    public boolean equipOutfit(String outfitName, Pet pet) {
        // if the player owns the outfit. If not, return false and notify
        if (!ownsOutfit(outfitName)) {
            System.out.println("Player does not own outfit: " + outfitName);
            return false; // Can't equip if player doesn't own it
        }

        // If the pet is already wearing an outfit, unequip it first
        if (pet.isWearingOutfit()) {
            unequipOutfit(pet);
        }

        // Equip the new outfit
        pet.setOutfit(outfitName);
        outfitInventory.put(outfitName, false); // Mark as equipped
        System.out.println("Equipped outfit: " + outfitName);
        return true;
    }


    /**
     * Adds an outfit to the player's inventory if it is not already owned.
     * Marks the outfit as available to equip.
     *
     * @param outfitName The name of the outfit to add to the inventory.
     */
    public void addOutfit(String outfitName) {
        // If the player doesn't own the outfit, mark it as owned
        if (!outfitInventory.containsKey(outfitName)) {
            outfitInventory.put(outfitName, true);  // "true" means the outfit is owned
            // If already owned, print a message to avoid duplication
        } else {
            System.out.println("Player already owns " + outfitName);
        }
    }


    /**
     * Unequips the pet's currently worn outfit, if any, and returns it
     * to the player's inventory by marking it as owned again.
     *
     * @param pet The pet whose outfit should be unequipped.
     */
    public void unequipOutfit(Pet pet) {
        // Get the name of the outfit the pet is currently wearing
        String currentOutfit = pet.getCurrentOutfit();

        // If the pet is wearing an outfit
        if (currentOutfit != null && !currentOutfit.isEmpty()) {
            pet.setOutfit(null); // Remove the outfit
            outfitInventory.put(currentOutfit, true); // Mark it as owned again
        } else {
            // No outfit to unequip
            System.out.println("No outfit to unequip.");
        }
    }


    /**
     * Checks whether the player owns a specific outfit.
     *
     * @param outfitName The name of the outfit to check.
     * @return true if the player owns the outfit, false otherwise.
     */
    public boolean ownsOutfit(String outfitName) {
        return outfitInventory.getOrDefault(outfitName, false);
    }


    /**
     * Feeds the specified pet with the given food item if the player has it in inventory.
     * Increases the pet's fullness and refunds 25% of the food's price to the player.
     *
     * @param pet  The pet to feed.
     * @param food  The food item to be consumed.
     * @return true if the feeding was successful; false if the player doesn't have the food.
     */
    public boolean feedPet(Pet pet, Food food) {
        if (consumeFood(food)) {
            pet.increaseFullness(food.getFullness());
            // Give the player back 25% of the food price back when feeding
            int returnOnFeeding = (int) (food.getPrice() * 0.25);
            setPlayerCoins(getPlayerCoins()+ returnOnFeeding);
            return true;
        }
        return false;
    }


    /**
     * Takes the pet to the vet if the cooldown period has passed.
     * Restores the pet's health and rewards the player with coins.
     *
     * @param pet  The pet to take to the vet.
     * @param currentTime  The current in-game time, used to check cooldown eligibility.
     * @return true if the pet was successfully taken to the vet; false if cooldown is still active.
     */
    public boolean takePetToVet(Pet pet, int currentTime) {
        // Check if the vet cooldown has passed by comparing current time with last visit
        if (currentTime - pet.getLastVetVisitTime() >= pet.getVetCooldownDuration()) {
            // Increase the pet's health
            pet.increaseHealth(30);

            // Update the last vet visit time to the current time
            pet.setLastVetVisitTime(currentTime);

            //Give 500 when taking to the vet
            setPlayerCoins(getPlayerCoins()+500);
            return true;
        }
        return false;
    }


    /**
     * Makes the pet perform an exercise action.
     * Decreases the pet's sleep and fullness, increases health,
     * and rewards the player with coins.
     *
     * @param pet The pet to exercise.
     */
    public void exercisePet(Pet pet) {
        pet.decreaseSleep(10);
        pet.decreaseFullness(10);
        pet.increaseHealth(15);
        setPlayerCoins(getPlayerCoins()+200);
    }


    /**
     * Retrieves the player's current food inventory.
     *
     * @return A map of Food items and their respective quantities.
     */
    public Map<Food, Integer> getFoodInventory() {
        return foodInventory;
    }


    /**
     * Retrieves the player's current toy inventory.
     *
     * @return A map of Toys and their respective quantities.
     */
    public Map<Toys, Integer> getToyInventory() {
        return toyInventory;
    }


    /**
     * Returns the player's current inventory of gifts.
     *
     * @return A map containing Gifts as keys and their quantities as values.
     */
    public Map<Gifts, Integer> getGiftInventory() {
        return giftInventory;
    }


    /**
     * Returns the player's outfit inventory.
     * The map tracks outfit names and their status:
     * true means the outfit is owned and available,
     * false means the outfit is currently equipped and unavailable.
     *
     * @return A map of outfit names to their availability status.
     */
    public Map<String, Boolean> getOutfitInventory() {
        return outfitInventory;
    }
}