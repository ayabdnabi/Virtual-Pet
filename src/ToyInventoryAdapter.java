package src;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Code Derived and adapted from;
 * https://stackoverflow.com/questions/68011041/how-to-serialize-hashmapobject-object-with-gson
 * Provides custom serialization and deserialization logic for a {@code Map<Toys, Integer>} using Gson.
 *
 * <p>
 * This adapter converts the toy inventory into a simplified JSON object where
 * each toy's name is used as a key and its quantity as the value. During deserialization,
 * the adapter reconstructs the toy objects by using the {@link Store} class to look up
 * toys by name.
 * </p>
 *
 * @author
 * Kamaldeep Ghotra,
 * Mohammed Abdulnabi
 * @version 1.0
 */
public class ToyInventoryAdapter implements JsonSerializer<Map<Toys, Integer>>, JsonDeserializer<Map<Toys, Integer>> {

    /**
     * Serializes a {@code Map<Toys, Integer>} into a {@link JsonObject}, using the toy names
     * as keys and their quantities as values.
     *
     * @param toyMap the toy inventory map to serialize
     * @param type the type of the map
     * @param context the serialization context
     * @return a {@link JsonElement} representing the toy inventory in JSON format
     */
    @Override
    public JsonElement serialize(Map<Toys, Integer> toyMap, Type type, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        for (Map.Entry<Toys, Integer> entry : toyMap.entrySet()) {
            obj.add(entry.getKey().getName(), new JsonPrimitive(entry.getValue()));
        }
        return obj;
    }

    /**
     * Deserializes a {@link JsonElement} back into a {@code Map<Toys, Integer>}, using a
     * {@link Store} to reconstruct toy objects by name.
     *
     * @param json the JSON data to deserialize
     * @param type the expected type
     * @param context the deserialization context
     * @return a reconstructed map of toys and their quantities
     * @throws JsonParseException if the input JSON is invalid
     */
    @Override
    public Map<Toys, Integer> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Map<Toys, Integer> toyMap = new HashMap<>();
        Store store = new Store(); // to retrieve Toys by name

        JsonObject obj = json.getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            Toys toy = store.getToy(entry.getKey());
            if (toy != null) {
                toyMap.put(toy, entry.getValue().getAsInt());
            }
        }
        return toyMap;
    }
}