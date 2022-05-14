package model;

/**
 * Table allows other classes to access the names of the tables.
 *
 * @author Liam Tripp
 */
public enum Table {
    ITEMS,
    DELETED_ITEMS;

    /**
     * Returns the Command as a String.
     *
     * @return a String with the Command's name as a String
     */
    public String getName() {
        return name().toLowerCase();
    }
}
