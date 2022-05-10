package view;

import model.Database;
import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presenter.DatabasePresenter;

import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabaseCLITest ensures that DatabaseCLI's CRUD methods work properly. It
 * also ensures the Command.Regex functions work properly with DatabaseCLI's
 * Matcher functions.
 *
 * @author Liam Tripp
 */
public class DatabaseCLITest {

    private Database database;
    private DatabasePresenter databasePresenter;
    private DatabaseCLI databaseCLI;

    @BeforeEach
    void setup() {
        database = new Database();
        database.initializeDatabase();
        databasePresenter = new DatabasePresenter();
        databasePresenter.addModel(database);
        databaseCLI = new DatabaseCLI();
        databaseCLI.addPresenter(databasePresenter);
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

    @Test
    void testDeleteItem() {

    }
}
