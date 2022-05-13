package model;

import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;

/**
 * Item is a data class simulating an item in a Store.
 *
 * @author Liam Tripp
 */
public class Item {

    private final int id;
    private String name;
    private BigDecimal price;
    private int stock;

    /**
     * Constructor for Item.
     *
     * @param id the id of the item
     * @param name the name of the Item
     * @param price the price of the item (in format XX.XX)
     * @param stock the amount of an item in stock
     */
    public Item(int id, String name, String price, int stock) {
        this.id = id;
        this.name = name;
        this.price = new BigDecimal(price);
        this.stock = stock;
    }

    /**
     * Constructs an Item from a JSONObject from the items.json file.
     *
     * @param jsonItem a JSONObject containing a name, price, and stock
     */
    public Item(JSONObject jsonItem) {
        this(-1, // not used
                (String) jsonItem.get("name"),
                (String) jsonItem.get("price"),
                Math.toIntExact((Long) jsonItem.get("stock")));
    }

    /**
     * Constructs an Item from a ResultSet.
     *
     * @param resultSet the ResultSet from an JDBC SQL query
     */
    public Item(ResultSet resultSet) {
        final Field[] attributes = Item.class.getDeclaredFields();

        try {
            id = resultSet.getInt(attributes[0].getName());
            name = resultSet.getString(attributes[1].getName());
            int price = resultSet.getInt((attributes[2].getName()));
            this.price = new BigDecimal(price / 100 + "." + price % 100);
            stock = resultSet.getInt(attributes[3].getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs an Item from a Matcher. Used when adding a new item to a
     * table.
     *
     * @param matcher contains the new Item's name, price, and stock
     */
    public Item(Matcher matcher) {
        // matcher.group(1) is the CREATE command
        this(-1, // not used
                matcher.group(2),
                matcher.group(3),
               Integer.parseInt((matcher.group(4))));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameInQuotes() {
        return "'" + name + "'";
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(String priceAsDecimalString) {
        double priceAsDouble = Double.parseDouble(priceAsDecimalString);
        String priceAsDecimal = String.format("%.2f", priceAsDouble);
        this.price = new BigDecimal(priceAsDecimal);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Returns the attributes of Item as a String array.
     *
     * @return the attributes of Item as a String array
     */
    public static String[] getAttributeNamesAsArray() {
        Field[] attributes = Item.class.getDeclaredFields();
        String[] attributeNames = new String[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            attributeNames[i] = attributes[i].getName();
        }
        return attributeNames;
    }

    /**
     * Returns the values of Item as a String array.
     *
     * @return the values of Item as a String array
     */
    public String[] getValuesAsArray() {
        return new String[] {
                String.valueOf(id),
                name,
                price.toString(),
                String.valueOf(stock)
        };
    }

    /**
     * Returns the values of all attributes as an SQL-formatted String.
     *
     * @return the SQL-formatted, comma-separated Item values
     */
    public String getValuesInSQLFormat() {
        return String.join(", ",
                String.valueOf(id),
                getValuesInSQLFormatExceptId());
    }

    /**
     * Returns the values of all attributes except id as an SQL-formatted String.
     *
     * @return the SQL-formatted, comma-separated Item values except id
     */
    public String getValuesInSQLFormatExceptId() {
        return String.join(", ",
                getNameInQuotes(),
                price.scaleByPowerOfTen(2).toString(),
                String.valueOf(stock));
    }

    public static String getAttributeNamesExceptId() {
        String[] attributeNames = getAttributeNamesAsArray();
        String[] attributeNamesExceptId = Arrays.copyOfRange(attributeNames, 1, attributeNames.length);
        return String.join(", ", attributeNamesExceptId);
    }

    // used for SQL table population
    public String getAttributeNameValueListExceptId() {
        return String.join(", ",
                "name = " + getNameInQuotes(),
                "price = " + price.scaleByPowerOfTen(2).intValue(),
                ("stock = " + stock));
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Item item)) {
            return false;
        }

        return id == item.getId() &&
                name.equals(item.getName()) &&
                price.compareTo(item.getPrice()) == 0 &&
                stock == item.getStock();
    }

    @Override
    public String toString() {
        return String.join(", ",
                String.valueOf(id),
                getNameInQuotes(),
                price.toString(),
                String.valueOf(stock));
    }
}
