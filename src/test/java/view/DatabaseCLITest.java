package view;

import model.Database;
import model.InputFileReader;
import model.Item;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presenter.DatabasePresenter;

import java.util.ArrayList;
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
    private JSONObject inputs;

    @BeforeEach
    void setup() {
        database = new Database();
        database.initializeDatabase();
        databasePresenter = new DatabasePresenter();
        databasePresenter.addModel(database);
        databaseCLI = new DatabaseCLI();
        databaseCLI.addPresenter(databasePresenter);
        InputFileReader inputFileReader = new InputFileReader("inputs", "json");
        JSONObject jsonObject = inputFileReader.getJSONFileAsObject();
        inputs = (JSONObject) jsonObject.get("inputs");
    }

    /**
     * Returns a JSONObject containing the values used to test each Command's
     * Regular Expression.
     *
     * @param commandName the name of the Command being tested
     * @param validity the validity of the command (either valid or invalid)
     * @return a jsonObject containing the valid/invalid tests
     */
    public JSONObject getCommandTest(String commandName, String validity) {
        return (JSONObject) ((JSONObject) inputs.get(commandName)).get(validity);
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
    void testCreateOneItem() {
        String sqlInput = "CREATE testName 1.99 1".toLowerCase();
        Command command = Command.CREATE;
        databaseCLI.executeCommand(command, sqlInput);
        ArrayList<Item> items = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(1, items.size());
    }

    @Test
    void testDeleteOneItem() {
        testCreateOneItem();

        Command command = Command.DELETE;
        int itemId = 1;
        String userInput = "DELETE " + itemId;
        System.out.println(databaseCLI.executeCommand(command, userInput));

        ArrayList<Item> items = database.selectFromTable(Database.ITEMS, "*");
        System.out.println(items);
        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }
}
