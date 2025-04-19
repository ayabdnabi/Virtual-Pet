package src;


/**
 * Represents a pet type configuration used to influence how quickly a pet's stats decline over time.
 * Each pet type has its own multipliers that scale the base decline rates for health, fullness,
 * sleep, and happiness. These multipliers determine how challenging it is to maintain each stat
 * depending on the selected pet type.
 *
 * @author Mohammed Abdulnabi
 * @version 1.0
 */
public class PetType {
    /** Health multipler for pet stats */
    private float healthDeclineMultiplier;
    /** Fullness multipler for pet stats */
    private float fullnessDeclineMultiplier;
    /** Sleep multipler for pet stats */
    private float sleepDeclineMultiplier;
    /** Happiness multipler for pet stats */
    private float happinessDeclineMultiplier;


    /**
     * Constructs a PetType with specific multipliers for how quickly each stat declines.
     * These multipliers allow different pet types to have unique characteristics and behaviors.
     *
     * @param healthDeclineMultiplier  how fast the health stat declines.
     * @param fullnessDeclineMultiplier  how fast the fullness stat declines.
     * @param sleepDeclineMultiplier  how fast the sleep stat declines
     * @param happinessDeclineMultiplier  how fast the happiness stat declines
     */
    public PetType(float healthDeclineMultiplier, float fullnessDeclineMultiplier, float sleepDeclineMultiplier, float happinessDeclineMultiplier) {
        this.healthDeclineMultiplier = healthDeclineMultiplier;
        this.fullnessDeclineMultiplier = fullnessDeclineMultiplier;
        this.sleepDeclineMultiplier = sleepDeclineMultiplier;
        this.happinessDeclineMultiplier = happinessDeclineMultiplier;
    }


    /**
     * Gets the multiplier that controls how quickly the pet's health stat declines over time.
     *
     * @return the health decline multiplier for this pet type
     */
    public float getHealthDeclineMultiplier() {
        return healthDeclineMultiplier;
    }


    /**
     * Retrieves the multiplier that determines how quickly the pet's fullness stat declines.
     *
     * @return the current fullness decline multiplier for this pet type
     */
    public float getFullnessDeclineMultiplier() {
        return fullnessDeclineMultiplier;
    }


    /**
     * Retrieves the multiplier that determines how quickly the pet's sleep stat declines.
     *
     * @return the current sleep decline multiplier for this pet type
     */
    public float getSleepDeclineMultiplier() {
        return sleepDeclineMultiplier;
    }


    /**
     * Retrieves the multiplier that determines how quickly the pet's happiness stat declines.
     *
     * @return the current happiness decline multiplier for this pet type
     */
    public float getHappinessDeclineMultiplier() {
        return happinessDeclineMultiplier;
    }
}
