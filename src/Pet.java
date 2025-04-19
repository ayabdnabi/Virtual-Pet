package src;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;


/**
 * Pet class represents a pet in the Virtual Pet game
 *
 * <p>This class shows the behavior and attributes of a virtual pet, including its needs (health, sleep, fullness, happiness),
 * state flags (sleeping, hungry, happy, dead), as well as interactions (playing, feeding, vet visits, gifting). The pet's stats decline
 * over time based on its type and current state, makming player intervention to maintain its well-being.</p>
 *
 * <p>Key features:
 * <ul>
 *   <li>Multiple pet types with different stat decline behaviors</li>
 *   <li>Periodic stat decay simulation through applyDecline()</li>
 *   <li>State management</li>
 *   <li>Outfit customization restricted by pet type</li>
 *   <li>Cooldown mechanics for special actions</li>
 *   <li>Warnings for critical stat levels</li>
 * </ul>
 *
 *
 * <p>The class uses GSON annotations for serialization/deserialization of pet state.</p>
 * @author
 * Kamaldeep Ghorta,
 * Mohammed Abdulnabi
 */
public class Pet {
    /** The name given to the pet by the player */
    @SerializedName("name")
    private String name;

    /** Map that holds different types of pets and their declinerate multipliers */
    private final Map<String, PetType> petTypeMap;

    /** Type of the pet */
    @SerializedName("petType")
    private String petType;

    /** Current stats */
    @SerializedName("health")
    private int health;
    @SerializedName("Sleep")
    private int sleep;
    @SerializedName("fullness")
    private int fullness;
    @SerializedName("happiness")
    private int happiness;

    /** Max values for each stat */
    @SerializedName("maxHealth")
    private final int maxHealth;
    @SerializedName("maxSleep")
    private final int maxSleep;
    @SerializedName("maxFullness")
    private final int maxFullness;
    @SerializedName("maxHappiness")
    private final int maxHappiness;

    /** Decline rates for each stat */
    @SerializedName("healthDeclineRate")
    private int healthDeclineRate;
    @SerializedName("fullnessDeclineRate")
    private int fullnessDeclineRate;
    @SerializedName("sleepDeclineRate")
    private int sleepDeclineRate;
    @SerializedName("happinessDeclineRate")
    private int happinessDeclineRate;

    /** State Flags */
    @SerializedName("isSleeping")
    private boolean isSleeping;
    @SerializedName("isHungry")
    private boolean isHungry;
    @SerializedName("isHappy")
    private boolean isHappy;
    @SerializedName("isDead")
    private boolean isDead;


    /** Vet Cooldown */
    @SerializedName("lastVetVisitTime")
    private int lastVetVisitTime;  // Last time the pet visited the vet
    @SerializedName("vetCooldownDuration")
    private int vetCooldownDuration;

    /** Play Cooldown */
    @SerializedName("lastPlayTime")  // The last time the pet played
    private int lastPlayTime;
    @SerializedName("playCooldownDuration")
    private int playCooldownDuration;

    /** Name of the outfit that the pet is wearing (currently) */
    @SerializedName("currentOutfit")
    private String currentOutfit;

    /** Used to track how often applyDecline() is called */
    private static int declineCounter = 0;

    /** Stores which outfits are allowed per pet type */
    private static final Map<String, String> allowedOutfits = new HashMap<>();

    /** Static block to initialize the corresponding outfits to the pet */
    static {
        allowedOutfits.put("PetOption1", "outfit1");
        allowedOutfits.put("PetOption2", "outfit2");
        allowedOutfits.put("PetOption3", "outfit3");
    }


    /**
     * Constructs a new Pet with its initial values and behavior types.
     *
     * This constructor initializes all the pet's stats, states, cooldowns,
     * and also sets up the predefined pet type behavior multipliers.
     * It calls {@code setPetType(petType)} at the end to update decline rates accordingly.
     *
     * @param name  The name of the pet.
     * @param petType  The selected pet type (e.g., PetOption1, PetOption2, PetOption3)
     * @param health  The current health of the pet
     * @param sleep  The current sleep value
     * @param fullness  The current fullness (hunger) value
     * @param happiness  The current happiness value
     * @param maxHealth  The max possible health value
     * @param maxSleep  The max possible sleep value
     * @param maxFullness  The max possible fullness value
     * @param maxHappiness  The max possible happiness value
     * @param healthDeclineRate  How quickly health should drop
     * @param fullnessDeclineRate  How quickly fullness should drop
     * @param sleepDeclineRate  How quickly sleep should drop
     * @param happinessDeclineRate  How quickly happiness should drop
     * @param isSleeping  Whether the pet starts in a sleeping state
     * @param isHungry  Whether the pet starts hungry
     * @param isHappy  Whether the pet starts happy
     * @param isDead  Whether the pet is dead
     * @param lastVetVisitTime  Time of last vet visit
     * @param vetCooldownDuration  Time required between vet visits
     * @param lastPlayTime  Time of last play session
     * @param playCooldownDuration  Time required between play sessions
     * @param currentOutfit  Name of the outfit currently worn (null if none)
     */
    public Pet(String name, String petType, int health, int sleep, int fullness, int happiness,
               int maxHealth, int maxSleep, int maxFullness, int maxHappiness,
               int healthDeclineRate, int fullnessDeclineRate, int sleepDeclineRate, int happinessDeclineRate,
               boolean isSleeping, boolean isHungry, boolean isHappy, boolean isDead,
               int lastVetVisitTime, int vetCooldownDuration, int lastPlayTime, int playCooldownDuration,
               String currentOutfit) {

        /* Store the pet's name and type */
        this.name = name;
        this.petType = petType;

        /*  Initialize map that links pet types with decline rate multipliers */
        this.petTypeMap = new HashMap<>();

        /* Decline behaviour for each pet */
        petTypeMap.put("PetOption1", new PetType(.5F,.9F,.5F,.5F));
        petTypeMap.put("PetOption2",  new PetType(.6F,.4F,.6F,.9F));
        petTypeMap.put("PetOption3",  new PetType(.5F,.5F,.9F,.6F));

        /* Initialize stat values */
        this.name = name;
        this.health = health;
        this.sleep = sleep;
        this.fullness = fullness;
        this.happiness = happiness;

        /* Initialize max stat values */
        this.maxHealth = maxHealth;
        this.maxSleep = maxSleep;
        this.maxFullness = maxFullness;
        this.maxHappiness = maxHappiness;

        /* Initialize decline rate for each stat */
        this.healthDeclineRate = healthDeclineRate;
        this.fullnessDeclineRate = fullnessDeclineRate;
        this.sleepDeclineRate = sleepDeclineRate;
        this.happinessDeclineRate = happinessDeclineRate;

        /* Initialize pets starting state */
        this.isSleeping = isSleeping;
        this.isHungry = isHungry;
        this.isHappy = isHappy;
        this.isDead = isDead;

        /* Initialize vet cooldown values */
        this.lastVetVisitTime = lastVetVisitTime;  // The last time the pet visited the vet
        this.vetCooldownDuration = vetCooldownDuration;

        /* Initialize play cooldown values */
        this.lastPlayTime = lastPlayTime;  // the last time the vet played
        this.playCooldownDuration = playCooldownDuration;

        /* Set the pets current outfit */
        this.currentOutfit = currentOutfit;

        /* Apply the specific pets decline rates*/
        setPetType(petType);
    }


    /**
     * Gets the time (in seconds) when the pet last visited the vet.
     *
     * @return The timestamp of the last vet visit.
     */
    public int getLastVetVisitTime() {
        return lastVetVisitTime;
    }


    /**
     * Updates the timestamp for the pet's last vet visit.
     *
     * @param lastVetVisitTime The new timestamp (in seconds) of the vet visit.
     */
    public void setLastVetVisitTime(int lastVetVisitTime) {
        this.lastVetVisitTime = lastVetVisitTime;
    }


    /**
     * Retrieves the cooldown duration after a vet visit.
     *
     * @return The cooldown duration in seconds.
     */
    public int getVetCooldownDuration() {
        return vetCooldownDuration;
    }


    /**
     * Sets the number of seconds the player must wait before the pet
     * can visit the vet again.
     *
     * @param vetCooldownDuration The cooldown duration in seconds.
     */
    public void setVetCooldownDuration(int vetCooldownDuration) {
        this.vetCooldownDuration = vetCooldownDuration;
    }


    /**
     * Returns the time (in seconds since the game started) when the pet was last played with.
     *
     * @return The timestamp of the pet's last playtime.
     */
    public int getLastPlayTime() {
        return lastPlayTime;
    }


    /**
     * Sets the timestamp of when the pet was last played with.
     *
     * @param lastPlayTime The new value representing the last play time in seconds.
     */
    public void setLastPlayTime(int lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }


    /**
     * Retrieves the cooldown duration required between play sessions.
     *
     * @return The number of seconds the pet must wait before it can play again.
     */

    public int getPlayCooldownDuration() {
        return playCooldownDuration;
    }


    /**
     * Sets the cooldown duration required between play sessions.
     *
     * @param playCooldownDuration The number of seconds the pet must wait before playing again.
     */
    public void setPlayCooldownDuration(int playCooldownDuration) {
        this.playCooldownDuration = playCooldownDuration;
    }


    /**
     * Retrieves the name of the pet.
     *
     * @return The pet's name.
     */
    public String getName() {
        return name;
    }


    /**
     * Increases the pet's happiness by a given amount.
     * Ensures the happiness does not exceed the pet's maximum happiness.
     *
     * @param amount The amount to increase happiness by.
     * @return The updated happiness value after the increase.
     */

    public int increaseHappiness(int amount) {
        happiness = Math.min(happiness + amount, maxHappiness);
        return happiness;
    }


    /**
     * Decreases the pet's happiness by a given amount.
     * Ensures the happiness does not go below zero.
     *
     * @param amount The amount to decrease happiness by.
     * @return The updated happiness value after the decrease.
     */

    public int decreaseHappiness(int amount) {
        happiness = Math.max(happiness - amount, 0);
        return happiness;
    }


    /**
     * Increases the pet's fullness by a given amount.
     * Ensures the fullness does not exceed the pet's maximum fullness.
     *
     * @param amount The amount to increase fullness by.
     * @return The updated fullness value after the increase.
     */

    public int increaseFullness(int amount) {
        fullness = Math.min(fullness + amount, maxFullness);
        return fullness;
    }


    /**
     * Reduces the pet's fullness by a specified amount.
     * Ensures that fullness does not fall below 0.
     *
     * @param amount the amount to subtract from the pet's current fullness
     * @return the updated fullness value after the decrease
     */
    public int decreaseFullness(int amount) {
        fullness = Math.max(fullness - amount, 0);
        return fullness;
    }


    /**
     * Increases the pet's health by the specified amount.
     * Ensures that health does not exceed the maximum health value.
     *
     * @param amount the amount to increase the pet's health by
     * @return the updated health value after the increase
     */
    public int increaseHealth(int amount) {
        health = Math.min(health + amount, maxHealth);
        return health;
    }


    /**
     * Decreases the pet's health by the specified amount.
     * Ensures that health does not go below zero.
     *
     * @param amount the amount to decrease the pet's health by
     * @return the updated health value after the decrease
     */

    public int decreaseHealth(int amount) {
        health = Math.max(health - amount, 0);
        return health;
    }


    /**
     * Increases the pet's sleep value by the specified amount.
     * Ensures the sleep value does not exceed the maximum allowed.
     *
     * @param amount the amount to increase the pet's sleep by
     * @return the updated sleep value after the increase
     */

    public int increaseSleep(int amount) {
        sleep = Math.min(sleep + amount, maxSleep);
        return sleep;
    }


    /**
     * Decreases the pet's sleep value by the specified amount.
     * Ensures the sleep value does not fall below zero.
     *
     * @param amount the amount to reduce the pet's sleep by
     * @return the updated sleep value after the decrease
     */

    public int decreaseSleep(int amount) {
        sleep = Math.max(sleep - amount, 0);
        return sleep;
    }


    /**
     * Returns the current happiness value of the pet.
     *
     * @return the pet's current happiness level
     */

    public int getHappiness() {
        return this.happiness;
    }


    /**
     * Returns the current fullness value of the pet.
     *
     * @return the pet's current fullness level
     */
    public int getFullness() {
        return this.fullness;
    }


    /**
     * Returns the current health value of the pet.
     *
     * @return the pet's current health level
     */

    public int getHealth() {
        return this.health;
    }


    /**
     * Returns the current sleep value of the pet.
     *
     * @return the pet's current sleep level
     */
    public int getSleepiness() {
        return this.sleep;
    }


    /**
     * Returns the map that holds all defined pet types and their corresponding stat multipliers.
     *
     * @return a map of pet type names to their PetType configurations
     */

    public Map<String, PetType> getPetTypeMap() {
        return petTypeMap;
    }


    /**
     * Retrieves the pet's current sleep level.
     *
     * @return the current sleep value
     */

    public int getSleep() {
        return sleep;
    }


    /**
     * Returns the maximum health value the pet can have.
     *
     * @return the maximum health stat
     */
    public int getMaxHealth() {
        return maxHealth;
    }


    /**
     * Returns the maximum sleep value the pet can have.
     *
     * @return the maximum sleep stat
     */
    public int getMaxSleep() {
        return maxSleep;
    }


    /**
     * Returns the maximum fullness value the pet can reach.
     *
     * @return the maximum fullness stat
     */
    public int getMaxFullness() {
        return maxFullness;
    }


    /**
     * Returns the maximum happiness value the pet can have.
     *
     * @return the maximum happiness stat
     */
    public int getMaxHappiness() {
        return maxHappiness;
    }


    /**
     * Checks if the pet is currently in a happy state.
     *
     * @return true if the pet is happy; false otherwise
     */
    public boolean isHappy() {
        return isHappy;
    }


    /**
     * Updates the stat decline rates (health, fullness, sleep, happiness) based on the pet's type.
     *
     * Each pet type has predefined multipliers that affect how quickly each stat decays.
     * This method fetches the current pet's type and applies its corresponding multipliers
     * to base stat decline values.
     */
    public void updateRatesBasedOfType() {
        PetType type = petTypeMap.get(this.petType);  // Get PetType object for current pet from Map

        // If the pet type doesn't exist, leave
        if (type == null) return;

        // Set default base decline rates (these can be constants or tuned)
        int baseHealth = 5;
        int baseFullness = 5;
        int baseSleep = 5;
        int baseHappiness = 5;

        // Apply multipliers from the selected PetType
        this.healthDeclineRate = Math.round(baseHealth * type.getHealthDeclineMultiplier());
        this.fullnessDeclineRate = Math.round(baseFullness * type.getFullnessDeclineMultiplier());
        this.sleepDeclineRate = Math.round(baseSleep * type.getSleepDeclineMultiplier());
        this.happinessDeclineRate = Math.round(baseHappiness * type.getHappinessDeclineMultiplier());
    }


    /**
     * Returns the type of the pet (e.g., "PetOption1", "PetOption2", or "PetOption3").
     *
     * @return A string representing the pet's type.
     */
    public String getPetType() {
        return petType;
    }


    /**
     * Checks if the pet is currently sleeping.
     *
     * @return true if the pet is sleeping; false otherwise.
     */
    public boolean isSleeping() {
        return isSleeping;
    }


    /**
     * Checks if the pet is currently hungry.
     *
     * @return true if the pet's fullness has dropped to 0; false otherwise.
     */
    public boolean isHungry() {
        return isHungry;
    }


    /**
     * Checks if the pet is currently angry.
     * A pet is considered angry when its happiness reaches zero.
     *
     * @return true if happiness is 0, false otherwise.
     */
    public boolean isAngry() {
        // Check if happiness is 0
        if(getHappiness() == 0)
        {
            return true;  // Angry pet
        }
        return false;  // Pet is not angry
    }


    /**
     * Checks if the pet is dead.
     * A pet is considered dead when its health is 0 or less.
     * Also updates the internal `isDead` flag accordingly.
     *
     * @return true if the pet's health is 0 or below, false otherwise.
     */
    public boolean isDead() {
        // If health less than or equal to 0, mark the pet as dead
        if(getHealth() <= 0) {
            isDead = true;
        }
        else{
            // Otherwise, the pet is not dead
            isDead = false;
        }
        return isDead;  // Return whether the pet is dead or not
    }


    /**
     * Sets the dead status of the pet.
     *
     * This method allows external classes to manually mark the pet
     * as dead or alive based on game logic or revive features.
     *
     * @param isDead true if the pet should be marked as dead, false otherwise.
     */
    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }

    /**
     * Sets the sleeping status of the pet.
     *
     * This can be used to manually put the pet to sleep or wake it up,
     * depending on the game state or user actions.
     *
     * @param isSleeping true if the pet should be marked as sleeping, false to wake it up.
     */

    public void setSleeping(boolean isSleeping) {
        this.isSleeping = isSleeping;
    }


    /**
     * Sets the hungry status of the pet.
     *
     * This flag indicates whether the pet is currently hungry. It can be
     * toggled manually or based on gameplay events like fullness reaching zero.
     *
     * @param isHungry true if the pet is hungry; false otherwise.
     */
    public void setHungry(boolean isHungry) {
        this.isHungry = isHungry;
    }


    /**
     * Sets the rate at which the pet's health declines over time.
     *
     * This value determines how quickly the pet loses health during gameplay,
     * especially when other needs like sleep or food are unmet.
     *
     * @param rate The new health decline rate to assign.
     */
    public void setHealthDeclineRate(int rate) {
        this.healthDeclineRate = rate;
    }


    /**
     * Sets the rate at which the pet's fullness decreases over time.
     *
     * This rate controls how quickly the pet gets hungry as the game progresses.
     *
     * @param rate The new fullness decline rate to assign.
     */
    public void setFullnessDeclineRate(int rate) {
        this.fullnessDeclineRate = rate;
    }


    /**
     * Sets the rate at which the pet's sleep level declines over time.
     *
     * A higher rate means the pet gets tired more quickly during gameplay.
     *
     * @param rate The new sleep decline rate to apply.
     */

    public void setSleepDeclineRate(int rate) {
        this.sleepDeclineRate = rate;
    }


    /**
     * Sets the rate at which the pet's happiness decreases over time.
     *
     * A higher value means the pet becomes unhappy more quickly if not entertained or cared for.
     *
     * @param rate The new happiness decline rate to assign.
     */
    public void setHappinessDeclineRate(int rate) {
        this.happinessDeclineRate = rate;
    }


    /**
     * Sets the pet's current happiness value.
     *
     * This method directly assigns a new happiness value, which should typically
     * be within the range of 0 and the pet's max happiness.
     *
     * @param happiness The new happiness value to assign.
     */
    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }


    /**
     * Returns the rate at which the pet's health decreases over time.
     *
     * @return The health decline rate as an integer value.
     */
    public int getHealthDeclineRate() {
        return healthDeclineRate;
    }


    /**
     * Returns the rate at which the pet's fullness decreases over time.
     *
     * @return The fullness decline rate as an integer value.
     */

    public int getFullnessDeclineRate() {
        return fullnessDeclineRate;
    }


    /**
     * Returns the rate at which the pet's sleep level decreases over time.
     *
     * @return The sleep decline rate as an integer.
     */
    public int getSleepDeclineRate() {
        return sleepDeclineRate;
    }


    /**
     * Returns the rate at which the pet's happiness decreases over time.
     *
     * @return The happiness decline rate as an integer.
     */
    public int getHappinessDeclineRate() {
        return happinessDeclineRate;
    }


    /**
     * Sets the pet type and updates the stat decline rates based on the selected type.
     * If the given type does not exist in the petTypeMap, it prints an error message.
     *
     * @param petType The type of the pet.
     */
    public void setPetType(String petType) {
        // Check if the provided pet type exists in the map of valid pet types
        if (petTypeMap.containsKey(petType)) {
            // Set the pet type (if valid)
            this.petType = petType;
            updateRatesBasedOfType();
        } else {
            // If the type is not recognized, print an error message for debugging
            System.out.println("Invalid pet type: " + petType);
        }
    }


    /**
     * Applies periodic stat decline logic for the pet based on its current state.
     * This method should be called on a timer to simulate the passage of time.
     *
     * Every 15 calls, it:
     * - Decreases sleep, fullness, and happiness if the pet is awake
     * - Regenerates sleep if the pet is sleeping
     * - Lowers health if the pet is starving or exhausted
     * - Updates state flags like isHungry, isHappy, isSleeping, and isDead
     */
    public void applyDecline() {
        // Increment declineCounter every time this method is called
        declineCounter++;

        // Only apply stat decline every 15 calls to slow down decay
        if(declineCounter % 15 == 0)
        {
            // Do nothing if the pet is already dead
            if (isDead) return;

            // 1. Passive stat changes depending on sleep state
            if (!isSleeping) {
                // If awake, reduce fullness and sleep
                decreaseFullness(fullnessDeclineRate);
                decreaseSleep(sleepDeclineRate);

                // If starving (fullness is 0), happiness drops faster
                decreaseHappiness(fullness <= 0 ? happinessDeclineRate * 2 : happinessDeclineRate);
            } else {
                // Regenerate sleep while sleeping
                increaseSleep(sleepDeclineRate);
            }

            // 2. Hunger check
            if (fullness <= 0) {
                // If starving, mark as hungry and reduce health
                isHungry = true;
                decreaseHealth(healthDeclineRate);
            } else {
                // If not starving, set isHungry to false
                isHungry = false;
            }

            // 3. Update happiness status
            isHappy = happiness > 0;

            // 4. Sleep logic
            if (sleep <= 0) {
                // If sleep reaches 0, pet gets sick (health penalty) and is forced to sleep
                decreaseHealth(healthDeclineRate);
                sleep = 0;
                isSleeping = true;
            } else if (sleep >= maxSleep) {
                isSleeping = false; // Wakes up when rested
            }

            // 5. Check Death
            if (health <= 0) {
                health = 0;
                isDead = true;
            }
        }
    }


    /**
     * Checks if the pet is currently wearing an outfit.
     *
     * @return true if the pet has an outfit equipped, false if not.
     */
    public boolean isWearingOutfit() {
        return currentOutfit != null && !currentOutfit.isEmpty();
    }


    /**
     * Checks if the pet's health is critically low.
     *
     * @return true if health is less than or equal to 25% of max health, false otherwise.
     */
    public boolean isWarningHealth() {
        return health <= (maxHealth / 4);
    }


    /**
     * Checks if the pet's sleep level is critically low.
     *
     * @return true if sleep is less than or equal to 25% of max sleep, false otherwise.
     */
    public boolean isWarningSleep() {
        return sleep <= (maxSleep / 4);
    }


    /**
     * Checks if the pet's fullness level is critically low.
     *
     * @return true if fullness is less than or equal to 25% of the maximum, false otherwise.
     */
    public boolean isWarningFullness() {
        return fullness <= (maxFullness / 4);
    }

    /**
     * Checks if the pet's happiness level is critically low.
     *
     * @return true if fullness is less than or equal to 25% of the maximum, false otherwise.
     */
    public boolean isWarningHappiness() {
        return happiness <= (maxHappiness / 4);
    }


    /**
     * Gets the name of the currently equipped outfit.
     *
     * @return The name of the current outfit, or null if no outfit is equipped.
     */
    public String getCurrentOutfit() {
        return currentOutfit;
    }


    /**
     * Attempts to equip an outfit to the pet. The outfit must match the allowed outfit
     * for the pet's type, as defined in the `allowedOutfits` map.
     *
     * @param outfitName The name of the outfit to equip, or null/empty to remove the current outfit.
     * @return true if the outfit was successfully set or removed; false otherwise.
     *
     */
    public boolean setOutfit(String outfitName) {
        // If the outfit is null or empty, remove the current outfit
        if (outfitName == null || outfitName.isEmpty()) {
            this.currentOutfit = null;
            return true;
        }

        // Get the outfit that is allowed for this pet type (from the predefined map)
        String allowedOutfit = allowedOutfits.get(this.petType);

        // If no allowed outfit is found for this pet type, deny the request
        if (allowedOutfit == null) {
            System.out.println("ERROR: No outfit restrictions defined for this pet type.");
            return false;
        }

        // If the provided outfit doesn't match the allowed outfit, deny it
        if (!outfitName.equalsIgnoreCase(allowedOutfit)) {
            System.out.println("ERROR: " + this.petType + " can only wear " + allowedOutfit + "!");
            return false;
        }

        // Outfit is valid and allowed — set it as the current outfit
        this.currentOutfit = outfitName;
        return true;
    }


    /**
     * Removes the currently equipped outfit from the pet, if one exists.
     * Outputs the removed outfit's name to the console for logging or debugging.
     *
     */
    public void removeOutfit() {
        if (this.currentOutfit != null && !this.currentOutfit.isEmpty()) {
            System.out.println("Removing outfit: " + this.currentOutfit);
            this.currentOutfit = null;
        }
    }


    /**
     * Resets the pet's state to its default.
     * This method is typically used when restarting the game or reviving the pet.
     *
     */
    public void resetState() {
            this.isDead = false;
            this.isSleeping = false;
            this.isHungry = false;
            this.isHappy = true;

            this.health = maxHealth;
            this.sleep = maxSleep;
            this.fullness = maxFullness;
            this.happiness = maxHappiness;
    }
}
