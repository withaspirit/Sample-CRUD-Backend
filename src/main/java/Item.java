import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;

public class Item {

    private final int id;
    private String name;
    private BigDecimal price;
    private int stock;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
        price = new BigDecimal(0);
        stock = 0;
    }

    public Item(int id, String name, int price) {
       this(id, name);
       // quotient.remainder from / 100
       String formattedPrice = price / 100 + "." + price % 100;
       this.price = new BigDecimal(formattedPrice);
       this.stock = 0;
    }

    public Item(int id, String name, int price, int stock) {
        this(id, name, price);
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        final String formattedString = id + ", '" + name + "', " + price + ", " + stock;
        return formattedString;
    }

    // https://stackoverflow.com/questions/3333974/how-to-loop-over-a-class-attributes-in-java
    public static String getAttributeNamesExceptId() {
        final Field[] attributes = Item.class.getDeclaredFields();
        final Field[] attributesExceptId = Arrays.copyOfRange(attributes, 1, attributes.length);

        StringBuilder commaSeparatedAttributes = new StringBuilder();
        for (int i = 0; i < attributesExceptId.length; i++) {
            commaSeparatedAttributes.append(attributesExceptId[i].getName());
            if (i != attributesExceptId.length - 1) {
                commaSeparatedAttributes.append(", ");
            }
        }
        return commaSeparatedAttributes.toString();
    }

    public String getAttributeValuesExceptID() {
        return "'" + name + "'," + price + ", " + stock;
    }
    // TODO: convert to and from JSONObject
}
