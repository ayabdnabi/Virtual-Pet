package src;


/**
 * Represents a gift item in the game.
 * <p>
 * A gift is defined by its unique name and associated price.
 * Equality for gift objects is checked solely on their names.
 * </p>
 *
 * @author Mohammed Ali Abdulnabi
 * @version 1.0
 */
public class Gifts {
    /** Name of the gift */
    private String name;
    /** Price of the gift */
    private int price;


    /**
     * Constructs a new Gifts instance with the specified name and price.
     *
     * @param name  the unique name of the gift
     * @param price the price of the gift
     */
    public Gifts(String name, int price) {
        this.name = name;
        this.price = price;
    }


    /**
     * Compares this gift to another object.
     * Two gifts are considered equal if they have the same name.
     *
     * @param obj the object to compare with
     * @return true if the given object is a gift instance with the same name and false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gifts other = (Gifts) obj;
        return name.equals(other.name); // assuming name is unique
    }


    /**
     * Returns a hash code for this gift. The hash code is based solely on the gift's name.
     *
     * @return a hash code value for this gift
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }


    /**
     * Returns the name of the gift.
     *
     * @return the gift's name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the price of the gift.
     *
     * @return the gift's price
     */
    public int getPrice() {
        return price;
    }
}