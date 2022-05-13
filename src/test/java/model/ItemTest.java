package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ItemTest ensures that Item's methods are functioning correctly.
 *
 * @author Liam Tripp
 */
public class ItemTest {

    private Item item;
    private String testName = "testName";

    @BeforeEach
    void setup() {
        item = new Item(1, testName, "0.0", 0);
    }

    @Test
    void testItemInitialization() {
        assertEquals(1, item.getId());
        assertEquals(testName, item.getName());
        assertEquals(new BigDecimal("0.0"), item.getPrice());
        assertEquals(0, item.getStock());
    }

    @Test
    void testItemEqualityNoDecimalPrice() {
        Item itemWithNoDecimalPrice = new Item(1, testName, "0", 0);
        assertEquals(item, itemWithNoDecimalPrice);
    }

    @Test
    void testItemEqualityWithDecimalPrice() {
        Item itemWithNoDecimalPrice = new Item(1, testName, "1", 0);
        Item itemWithDecimalPrice = new Item(1, testName, "1.0", 0);
        assertEquals(itemWithNoDecimalPrice, itemWithDecimalPrice);
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.", ".0", "100", "100.99"})
    void testPriceInSQLFormat(String price) {
        item.setPrice(price);
        String sqlFormattedPrice = item.getPriceTimes100().toString();
        assertTrue(item.getValuesInSQLFormat().contains(sqlFormattedPrice));
        assertTrue(item.getValuesInSQLFormatExceptId().contains(sqlFormattedPrice));
        assertTrue(item.getAttributeNameValueListExceptId().contains(sqlFormattedPrice));
    }
}
