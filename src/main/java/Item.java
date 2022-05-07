import java.lang.reflect.Field;
import java.math.BigDecimal;

public class Item {

    private final int id;
    private String name;
    private BigDecimal price;
    private int stock;
    private static int COUNT = 0;

    public Item(String name) {
        this.id = COUNT++;
        this.name = name;
        price = new BigDecimal(0);
        stock = 0;
    }

    public Item(String name, BigDecimal price) {
       this(name);
       this.price = price;
       this.stock = 0;
    }

    public Item(String name, BigDecimal price, int stock) {
        this(name, price);
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
        String formattedString = id + ", '" + name + "', " + price + ", " + stock;
        return formattedString;
    }

    // https://stackoverflow.com/questions/3333974/how-to-loop-over-a-class-attributes-in-java
    public String getAttributeNames() {
        String str = "";
        Field[] attributes = getClass().getDeclaredFields();

        int indexBeforeCOUNT = attributes.length - 2;
        for (int i = 0; i < indexBeforeCOUNT; i++) {
            str += attributes[i].getName() + ", ";
        }
        str += attributes[indexBeforeCOUNT].getName();
        return str;
    }

    // TODO: convert to and from JSONObject
}
