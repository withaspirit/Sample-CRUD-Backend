/**
 * Attributes contains the properties for Item.
 *
 * @author Liam Tripp
 */
public enum Attributes {
    ID,
    NAME,
    PRICE,
    STOCK;

    public String getName() {
        return name().toLowerCase();
    }
}