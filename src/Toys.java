package src;

/**
 * Represents a toy item.
 * <p>
 * A toy is defined by its unique name, price, and description.
 * This class provides methods to access these properties, and two toys are considered
 * equal if they share the same name.
 * </p>
 *
 * @author Mohammed Abdulnabi
 * @version 1.0
 */
public class Toys {
    /** Name of the toy */
    private String name;
    /** Price of the toy */
    private int price;
    /** Description of the toy */
    private String description;  // optional


    /**
     * Constructs a new toy instance with the specified name, price, and description.
     *
     * @param name  the unique name of the toy
     * @param price  the price of the toy
     * @param description  a brief description of the toy
     */
    public Toys(String name, int price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }


    /**
     * Compares this toy to the specified object.
     * Two toy instances are considered equal if they have the same name.
     *
     * @param obj the object to compare with
     * @return true if the given object is a toy instance with the same name and false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Toys other = (Toys) obj;
        return name.equals(other.name); // assuming name is unique
    }


    /**
     * Returns a hash code value for this toy.
     *
     * @return a hash code value for this toy
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }


    /**
     * Returns the name of the toy.
     *
     * @return the toy's name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the price of the toy.
     *
     * @return the toy's price
     */
    public int getPrice() {
        return price;
    }


    /**
     * Returns the description of the toy.
     *
     * @return the toy's description
     */
    public String getDescription() {
        return description;
    }
}