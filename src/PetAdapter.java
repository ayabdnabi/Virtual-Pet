package src;

import com.google.gson.*;
import java.lang.reflect.Type;
/**
 * Code Derived and adapted from;
 * https://stackoverflow.com/questions/68011041/how-to-serialize-hashmapobject-object-with-gson
 *
 * A custom JsonSerializer and JsonDeserializer implementation for the Pet class
 * This adapter handles the conversion between Pet objects and their JSON representation,
 * allowing for custom serialization and deserialization logic.
 *
 * @author
 * Kamaldeep Ghorta,
 * Mohammed Abdulnabi
 * @version 1.0
 */
public class PetAdapter implements JsonSerializer<Pet>, JsonDeserializer<Pet> {

    /**
     * Serializes a Pet object into its JSON representation
     *
     * @param pet the Pet object that needs to be serialized
     * @param type the type of the object to be serialized
     * @param context the serialization context
     * @return a JsonElement representing the serialized Pet object
     */
    @Override
    public JsonElement serialize(Pet pet, Type type, JsonSerializationContext context) {
        // Create a JSON object to hold all pet properties
        JsonObject obj = new JsonObject();
        // Add all properties of a pet into the JSON object
        obj.addProperty("name", pet.getName());
        obj.addProperty("petType", pet.getPetType());
        obj.addProperty("health", pet.getHealth());
        obj.addProperty("sleep", pet.getSleepiness());
        obj.addProperty("fullness", pet.getFullness());
        obj.addProperty("happiness", pet.getHappiness());
        // Add the maximum values for their statistics
        obj.addProperty("maxHealth", pet.getMaxHealth());
        obj.addProperty("maxSleep", pet.getMaxSleep());
        obj.addProperty("maxFullness", pet.getMaxFullness());
        obj.addProperty("maxHappiness", pet.getMaxHappiness());
        // Add decline rates for pet attrtibutes
        obj.addProperty("healthDeclineRate", pet.getHealthDeclineRate());
        obj.addProperty("fullnessDeclineRate", pet.getFullnessDeclineRate());
        obj.addProperty("sleepDeclineRate", pet.getSleepDeclineRate());
        obj.addProperty("happinessDeclineRate", pet.getHappinessDeclineRate());
        // Set the flags of teh different states a pet can be in
        obj.addProperty("isSleeping", pet.isSleeping());
        obj.addProperty("isHungry", pet.isHungry());
        obj.addProperty("isHappy", pet.isHappy());
        obj.addProperty("isDead", pet.isDead());
        //Time-related properties
        obj.addProperty("lastVetVisitTime", pet.getLastVetVisitTime());
        obj.addProperty("vetCooldownDuration", pet.getVetCooldownDuration());
        obj.addProperty("lastPlayTime", pet.getLastPlayTime());
        obj.addProperty("playCooldownDuration", pet.getPlayCooldownDuration());
        // Outfit property
        obj.addProperty("currentOutfit", pet.getCurrentOutfit());

        return obj;
    }

    /**
     * Deserializes a JSON element into a Pet object.
     *
     * @param json the JSON element deserialize
     * @param typeOfT the type of the object to deserialize
     * @param context the deserialization context
     * @return a Pet object constructed from the JSON data
     * @throws JsonParseException if there is an error parsing the JSON
     */
    @Override
    public Pet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        // Read value from JSON with defaut for PetType missing
        String name = obj.get("name").getAsString();
        String petType = obj.has("petType") && !obj.get("petType").isJsonNull() ? obj.get("petType").getAsString() : "Dog";  // Default if missing

        // Read numeric attributes
        int health = obj.get("health").getAsInt();
        int sleep = obj.get("sleep").getAsInt();
        int fullness = obj.get("fullness").getAsInt();
        int happiness = obj.get("happiness").getAsInt();
        // Read maximum attribute values
        int maxHealth = obj.get("maxHealth").getAsInt();
        int maxSleep = obj.get("maxSleep").getAsInt();
        int maxFullness = obj.get("maxFullness").getAsInt();
        int maxHappiness = obj.get("maxHappiness").getAsInt();
        // Read decline rate values
        int healthDeclineRate = obj.get("healthDeclineRate").getAsInt();
        int fullnessDeclineRate = obj.get("fullnessDeclineRate").getAsInt();
        int sleepDeclineRate = obj.get("sleepDeclineRate").getAsInt();
        int happinessDeclineRate = obj.get("happinessDeclineRate").getAsInt();
        // Read states of pet as a boolean
        boolean isSleeping = obj.get("isSleeping").getAsBoolean();
        boolean isHungry = obj.get("isHungry").getAsBoolean();
        boolean isHappy = obj.get("isHappy").getAsBoolean();
        boolean isDead = obj.get("isDead").getAsBoolean();
        // Read time-related properties
        int lastVetVisitTime = obj.get("lastVetVisitTime").getAsInt();
        int vetCooldownDuration = obj.get("vetCooldownDuration").getAsInt();
        int lastPlayTime = obj.get("lastPlayTime").getAsInt();
        int playCooldownDuration = obj.get("playCooldownDuration").getAsInt();
        // Read optional outfit
        String outfit = obj.has("currentOutfit") && !obj.get("currentOutfit").isJsonNull()
                ? obj.get("currentOutfit").getAsString()
                : null;

        // Create and return a new Pet instance with all the deserialized values
        Pet pet = new Pet(
                name, petType, health, sleep, fullness, happiness,
                maxHealth, maxSleep, maxFullness, maxHappiness,
                healthDeclineRate, fullnessDeclineRate, sleepDeclineRate, happinessDeclineRate,
                isSleeping, isHungry, isHappy, isDead,
                lastVetVisitTime, vetCooldownDuration, lastPlayTime, playCooldownDuration,
                outfit
        );

        return pet;
    }
}
