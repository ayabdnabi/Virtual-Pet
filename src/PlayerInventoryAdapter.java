package src;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Code Derived and adapted from;
 * https://stackoverflow.com/questions/68011041/how-to-serialize-hashmapobject-object-with-gson
 * Custom JSON serializer/deserializer for the {@link PlayerInventory} class, using Google's Gson library.
 *
 * <p>
 * This adapter handles the serialization and deserialization of all components
 * within a player's inventory, including food, toys, gifts, outfits, and coin balance.
 * It uses  supporting adapters such as {@link FoodInventoryAdapter},
 * {@link GiftInventoryAdapter}, and {@link ToyInventoryAdapter} for nested conversions.
 * </p>
 *
 * @author
 * Kamaldeep Ghotra,
 * Mohammed Abdulnabi
 * @version 1.0
 */
public class PlayerInventoryAdapter implements JsonSerializer<PlayerInventory>, JsonDeserializer<PlayerInventory> {
    /** Store reference for item look up */
    private final Store store;

    /**
     * Constructs a new {@code PlayerInventoryAdapter} with the given store instance,
     * which is used to look up and rebuild item objects during deserialization.
     *
     * @param store the {@link Store} object that provides access to standard items
     */
    public PlayerInventoryAdapter(Store store) {
        this.store = store;
    }

    /**
     * Serializes a {@link PlayerInventory} object into a JSON representation.
     *
     * @param inventory the inventory to serialize
     * @param typeOfSrc the actual type
     * @param context the context for serialization
     * @return a {@link JsonElement} containing the full inventory structure
     */
    @Override
    public JsonElement serialize(PlayerInventory inventory, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("playerCoins", inventory.getPlayerCoins());
        obj.add("foodInventory", new FoodInventoryAdapter().serialize(inventory.getFoodInventory(), null, context));
        obj.add("giftInventory", new GiftInventoryAdapter().serialize(inventory.getGiftInventory(), null, context));
        obj.add("toyInventory", new ToyInventoryAdapter().serialize(inventory.getToyInventory(), null, context));
        obj.add("outfitInventory", context.serialize(inventory.getOutfitInventory()));
        return obj;
    }

    /**
     * Deserializes a {@link JsonElement} into a {@link PlayerInventory} object.
     * Reconstructs all inventory sub-maps and restores the player's coin balance.
     *
     * @param json the JSON data to deserialize
     * @param typeOfT the target type (not used)
     * @param context the context for deserialization
     * @return a fully reconstructed {@link PlayerInventory}
     * @throws JsonParseException if any part of the JSON is invalid or missing
     */
    @Override
    public PlayerInventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        PlayerInventory inventory = new PlayerInventory(store);

        if (obj.has("playerCoins")) {
            inventory.setPlayerCoins(obj.get("playerCoins").getAsInt());
        }

        Type foodType = new com.google.gson.reflect.TypeToken<Map<Food, Integer>>() {}.getType();
        Type giftType = new com.google.gson.reflect.TypeToken<Map<Gifts, Integer>>() {}.getType();
        Type toyType = new com.google.gson.reflect.TypeToken<Map<Toys, Integer>>() {}.getType();
        Type outfitType = new com.google.gson.reflect.TypeToken<Map<String, Boolean>>() {}.getType();

        Map<Food, Integer> foodMap = new FoodInventoryAdapter().deserialize(obj.get("foodInventory"), foodType, context);
        Map<Gifts, Integer> giftMap = new GiftInventoryAdapter().deserialize(obj.get("giftInventory"), giftType, context);
        Map<Toys, Integer> toyMap = new ToyInventoryAdapter().deserialize(obj.get("toyInventory"), toyType, context);
        Map<String, Boolean> outfitMap = context.deserialize(obj.get("outfitInventory"), outfitType);

        inventory.getFoodInventory().putAll(foodMap);
        inventory.getGiftInventory().putAll(giftMap);
        inventory.getToyInventory().putAll(toyMap);
        inventory.getOutfitInventory().putAll(outfitMap);

        return inventory;
    }
}