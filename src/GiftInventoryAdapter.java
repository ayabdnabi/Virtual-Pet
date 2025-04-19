package src;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Code Derived and adapted from;
 * https://stackoverflow.com/questions/68011041/how-to-serialize-hashmapobject-object-with-gson
 * Handles the custom JSON serialization and deserialization of a {@code Map<Gifts, Integer>} using the Gson library.
 *
 * <p>
 * Converts the gift inventory into a {@link JsonObject} where each gift's name is
 * used as a key and its quantity as the value. During deserialization, the adapter reconstructs
 * the inventory by retrieving gift objects from the {@link Store} based on their names.</p>
 *
 * @author
 * Kamaldeep Ghotra,
 * Mohammed Abdulnabi
 * @version 1.0
 * */

public class GiftInventoryAdapter implements JsonSerializer<Map<Gifts, Integer>>, JsonDeserializer<Map<Gifts, Integer>> {

    /**
     * Serializes a {@code Map<Gifts, Integer>} into a {@link JsonObject}, using each gift's
     * name as the key and its quantity as the value.
     *
     * @param giftMap the gift inventory to serialize
     * @param type the generic type
     * @param context the Gson serialization context
     * @return a {@link JsonElement} representing the gift inventory in JSON format
     */
    @Override
    public JsonElement serialize(Map<Gifts, Integer> giftMap, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        for (Map.Entry<Gifts, Integer> entry : giftMap.entrySet()) {
            obj.add(entry.getKey().getName(), new JsonPrimitive(entry.getValue()));
        }
        return obj;
    }

    /**
     * Deserializes a {@link JsonElement} back into a {@code Map<Gifts, Integer>},
     * reconstructing the gifts using the {@link Store} lookup by name.
     *
     * @param json the JSON element to deserialize
     * @param type the expected map type
     * @param context the Gson deserialization context
     * @return a {@code Map<Gifts, Integer>} representing the reconstructed gift inventory
     * @throws JsonParseException if the input JSON is malformed
     */
    @Override
    public Map<Gifts, Integer> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Map<Gifts, Integer> giftMap = new HashMap<>();
        Store store = new Store(); // to retrieve Gifts by name

        JsonObject obj = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            Gifts gift = store.getGift(entry.getKey());
            if (gift != null) {
                giftMap.put(gift, entry.getValue().getAsInt());
            }
        }
        return giftMap;
    }
}