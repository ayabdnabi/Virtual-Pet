package src;
import java.util.HashMap;
import java.util.Map;


/**
 * Provides a store for managing in-game items such as food, toys, and gifts.
 * <p>
 * The Store class loads default items into three collections (food, toys, and gifts)
 * and offers methods for retrieving items, checking item availability, and processing
 * purchases by updating a player's inventory.
 * </p>
 *
 * @author Kamaldeep Ghotra
 * @author Mohammed Abdulnabi
 * @author Aya Abdulnabi
 * @version 1.0
 */
public class Store {
    /** Maps to the food item based on ID */
    private final Map<String, Food> foodMap;
    /** Maps to the gift item based on ID */
    private final Map<String, Gifts> giftsMap;
    /** Maps to the toys item based on ID */
    private final Map<String, Toys> toysMap;


    /**
     * Constructs a new Store and loads default food, toy, and gift items.
     */
    public Store() {

        /* Load in Default food items */
        foodMap = new HashMap<>();
        foodMap.put("Orange", new Food("Orange", 50, 5, "A tangy, sweet taste!"));
        foodMap.put("Bunny Cookie", new Food("Bunny Cookie", 60, 6, "Jumps around in your mouth!"));
        foodMap.put("Swiss Roll", new Food("Swiss Roll", 200, 20, "Sweet swirls of joy"));
        foodMap.put("Carrot Cake", new Food("Carrot Cake", 350, 35, "With uncomfortably orange carrot icing"));
        foodMap.put("Lamb Chop", new Food("Lamb Chop", 1100, 100, "The fanciest bite in town!"));
        foodMap.put("Chicken", new Food("Chicken",  650,65, "A complete feast for your soul"));

        /* Load in Default toy items */
        toysMap = new HashMap<>();
        toysMap.put("Wand", new Toys("Wand",199, "For aspiring magicians!"));
        toysMap.put("Stuffed Animal", new Toys("Stuffed Animal",299, "Slightly lumpy from all that love!"));
        toysMap.put("Unicorn Balloon", new Toys("Unicorn Balloon",349, "Hold on tight so it doesn't fly away!"));
        toysMap.put("Fan", new Toys("Fan",249, "Who knew wind could be this fun?"));
        toysMap.put("Basketball", new Toys("Basketball",199, "Perfect for indoor slam dunks"));
        toysMap.put("Guitar", new Toys("Guitar",299,"Perfect  for shredding... literally!"));

        /* Load in default gifts */
        giftsMap = new HashMap<>();
        giftsMap.put("outfit1", new Gifts("outfit1", 3000));
        giftsMap.put("outfit2", new Gifts("outfit2", 3000));
        giftsMap.put("outfit3", new Gifts("outfit3", 3000));

    }


    /**
     * Returns the map of all food items available in the store.
     *
     * @return a map where keys are food names and values are Food objects
     */
    public Map<String, Food> getAllFood() {
        return foodMap;
    }


    /**
     * Returns the map of all toy items available in the store.
     *
     * @return a map where keys are toy names and values are Toys objects
     */
    public Map<String, Toys> getAllToys() {
        return toysMap;
    }


    /**
     * Returns the map of all gift items available in the store.
     *
     * @return a map where keys are gift names and values are Gifts objects
     */
    public Map<String, Gifts> getAllGifts() {
        return giftsMap;
    }


    /**
     * Retrieves a food item by its name.
     *
     * @param name the name of the food
     * @return the Food object corresponding to the given name, or null if not found
     */
    public Food getFood(String name) {
        return foodMap.get(name);
    }


    /**
     * Checks if a food item exists in the store.
     *
     * @param name the name of the food to check
     * @return true if the food exists, false otherwise
     */
    public boolean hasFood(String name) {
        return foodMap.containsKey(name);
    }


    /**
     * Retrieves a gift item by its name.
     *
     * @param name the name of the gift
     * @return the Gifts object corresponding to the given name, or null if not found
     */
    public Gifts getGift(String name) {
        return giftsMap.get(name);
    }


    /**
     * Checks if a gift item exists in the store.
     *
     * @param name the name of the gift to check
     * @return true if the gift exists, false otherwise
     */
    public boolean hasGift(String name) {
        return giftsMap.containsKey(name);
    }


    /**
     * Retrieves a toy item by its name.
     *
     * @param name the name of the toy
     * @return the Toys object corresponding to the given name, or null if not found
     */
    public Toys getToy(String name) {
        return toysMap.get(name);
    }


    /**
     * Checks if a toy item exists in the store.
     *
     * @param name the name of the toy to check
     * @return true if the toy exists, false otherwise
     */
    public boolean hasToys(String name) {
        return toysMap.containsKey(name);
    }


    /**
     * Processes the purchase of a food item.
     *
     * The total cost is calculated as the food price multiplied by the quantity.
     * If the player has enough coins, the cost is deducted and the food is added to the inventory.
     *
     * @param name      the name of the food to purchase
     * @param inventory the player's inventory
     * @param quantity  the quantity of the food to purchase (must be greater than 0)
     * @return true if the purchase is successful, false otherwise
     */
    public boolean buyFood(String name, PlayerInventory inventory, int quantity) {
        if (hasFood(name) && quantity > 0) {  // Add quantity check
            Food food = getFood(name);
            int totalCost = food.getPrice() * quantity;

            // If the player has enough coins, decrease the coins and increase the qty by the amount
            if (inventory.getPlayerCoins() >= totalCost) {
                inventory.setPlayerCoins(inventory.getPlayerCoins() - totalCost);
                inventory.addFood(food, quantity);
                return true;
            }
        }
        return false;
    }


    /**
     * Processes the purchase of a toy item.
     *
     * If the toy exists and the player has enough coins, the toy is added to the inventory.
     * Note that the cost is not multiplied by quantity, so it is assumed that quantity is used only to update the count.
     *
     * @param name      the name of the toy to purchase
     * @param inventory the player's inventory
     * @param quantity  the quantity of the toy to purchase
     * @return true if the purchase is successful, false otherwise
     */
    public boolean buyToy(String name, PlayerInventory inventory, int quantity) {
        // if the player has entered a valid toy name
        if (hasToys(name)) {
            Toys toy = getToy(name);
            int cost = toy.getPrice();

            // if the player has enough coins
            if (inventory.getPlayerCoins() >= cost) {
                // Decrease the coins and increase the qty by the amount
                inventory.setPlayerCoins(inventory.getPlayerCoins() - cost);
                inventory.addToy(toy, quantity);
                return true;
            }
        }
        return false;
    }


    /**
     * Processes the purchase of a gift item.
     *
     * If the gift exists and the player has enough coins, the cost is deducted from the player's coins,
     * and the gift is added to the inventory.
     *
     * @param name  the name of the gift to purchase
     * @param inventory  the player's inventory
     * @param quantity  the quantity of the gift to purchase
     * @return true if the purchase is successful, false otherwise
     */
    public boolean buyGift(String name, PlayerInventory inventory, int quantity) {
        // if the player has entered a valid gift name
        if (hasGift(name)) {
            Gifts gift = getGift(name);
            int cost = gift.getPrice();

            // if the player has enough coins
            if (inventory.getPlayerCoins() >= cost) {
                // Decrease the coins and increase the qty by the amount
                inventory.setPlayerCoins(inventory.getPlayerCoins() - cost);
                inventory.addGift(gift, quantity);
                return true;
            }
        }
        return false;
    }

}













