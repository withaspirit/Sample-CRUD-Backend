package model;

import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {

    private Item item;
    private String testName = "testName";

    @BeforeEach
    void setup() {
        item = new Item(0, testName);
    }

    @Test
    void testItemInitialization() {
        assertEquals(0, item.getId());
        assertEquals(testName, item.getName());
        assertEquals(new BigDecimal(0), item.getPrice());
        assertEquals(0, item.getStock());
    }

    @Test
    void testItemEqualityNoDecimalPrice() {
        Item itemWithNoDecimalPrice = new Item(0, testName, "0", 0);
        assertEquals(item, itemWithNoDecimalPrice);
    }

    @Test
    void testItemEqualityWithDecimalPrice() {
        Item itemWithNoDecimalPrice = new Item(0, testName, "1", 0);
        Item itemWithDecimalPrice = new Item(0, testName, "1.0", 0);
        assertEquals(itemWithNoDecimalPrice, itemWithDecimalPrice);
    }

    @Test
    void testGettingPropertiesAsString() {
        System.out.println(Item.getAttributeNamesExceptId());
    }
}
