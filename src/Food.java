package src;


/**
 * This class allows access to various food characteristics.
 *
 * <p>
 * This class represents a food item with a unique name, price, fullness value, and description.
 * It offers methods to access these properties.
 *</p>
 *
 * @author Mohammed Abdulnabi
 * @author Kamaldeep Ghotra
 * @version 1.0
 */
public class Food {
    /** name of the food */
    private String name;
    /** price of the food */
    private int price;
    /** fullness of the food, how much fullness does it increase */
    private int fullness;
    /** description of the food */
    private String description;


    /**
     * Constructs a new Food instance.
     *
     * @param name  the name of the food
     * @param price  the price of the food
     * @param fullness  the fullness value provided by the food
     * @param description  a brief description of the food
     */
    public Food(String name, int price, int fullness, String description) {
        this.name = name;
        this.price = price;
        this.fullness = fullness;
        this.description = description;
    }


    /**
     * Returns the name of the food.
     *
     * @return the food's name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the price of the food.
     *
     * @return the food's price
     */
    public int getPrice() {
        return price;
    }


    /**
     * Returns the fullness value of the food.
     *
     * @return the fullness value
     */
    public int getFullness() {
        return fullness;
    }


    /**
     * Returns the description of the food.
     *
     * @return the food's description
     */
    public String getDescription() {
        return description;
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * Two Food objects are considered equal if they have the same name.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument and false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Food other = (Food) obj;
        return name.equals(other.name); // compare by name
    }


    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return name.hashCode(); // ensure it hashes consistently
    }
}
