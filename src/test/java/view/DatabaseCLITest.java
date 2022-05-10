package view;

import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabaseCLITest ensures the DatabaseCLI's CRUD methods manipulates
 * the Database correctly.
 *
 * @author Liam Tripp
 */
public class DatabaseCLITest {

    private DatabaseCLI databaseCLI;

    @BeforeEach
    void setup() {
        databaseCLI = new DatabaseCLI();
    }

    @Test
    void matcherTest() {
        // demonstrate matcher use
        Item testItem = new Item(1, "testName", "10.30", 0);
        String createRegex = "(\\w+) (\\d+\\.\\d+) (\\d+)";
        String[] testItemValuesArray = testItem.getValuesAsArray();
        String testItemValues = String.join(" ", testItemValuesArray);

        Matcher matcher = databaseCLI.getMatcher(createRegex, testItemValues);
        assertTrue(matcher.matches());
        matcher.reset();

        if (matcher.find()) {
            assertEquals(testItemValues, matcher.group(0));
            for (int i = 1; i < testItemValuesArray.length; i++) {
                assertEquals(testItemValuesArray[i - 1], matcher.group(i));
            }
        } else {
            fail();
        }
    }

    @Test
    void testCreateItem() {
        String sqlInput = "CREATE testName 10.30 4".toLowerCase();
        Matcher matcher = databaseCLI.getMatcher(Command.CREATE.getRegex(), sqlInput);
        assertEquals("", databaseCLI.getMatcherError(matcher));
        databaseCLI.createItem(matcher);
    }
}
