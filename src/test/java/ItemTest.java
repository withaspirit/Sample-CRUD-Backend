import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {

    private Item item;

    @BeforeEach
    void setup() {
        item = new Item("Item");
    }

    @Test
    void testItemInitialization() {
        assertEquals(0, item.getId());
        assertEquals("Item", item.getName());
        assertEquals(new BigDecimal(0), item.getPrice());
        assertEquals(0, item.getStock());
    }
}
