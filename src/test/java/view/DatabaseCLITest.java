package view;

import model.Database;
import model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presenter.DatabasePresenter;

import java.util.ArrayList;
import java.util.Arrays;

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
    private Item testItem;

    @BeforeEach
    void setup() {
        database = new Database();
        database.initializeDatabase();
        databasePresenter = new DatabasePresenter();
        databasePresenter.addModel(database);
        databaseCLI = new DatabaseCLI();
        databaseCLI.addPresenter(databasePresenter);
        testItem = new Item(1, "testName", "100.99", 1);
    }

    @AfterEach
    void tearDown() {
        databaseCLI.quit();
    }

    /**
     * Creates the testItem.
     */
    void createItem() {
        String[] itemValues = testItem.getValuesAsArray();
        String[] itemValuesExceptId = Arrays.copyOfRange(itemValues, 1, itemValues.length);
        String values = String.join(" ", itemValuesExceptId);

        String userInput = "CREATE " + values;
        databaseCLI.processInput(userInput);
    }

    @Test
    void testCreateOneItem() {
        createItem();
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testReadFromEmptyTable() {
        String userInput = "READ " + Database.ITEMS;
        String consoleOutput = databaseCLI.processInput(userInput);
        assertEquals(Database.ITEMS + " is empty.", consoleOutput);
    }

    @Test
    void testReadFromTableWithItems() {
        createItem();
        String readStatement = "READ " + Database.ITEMS;
        String consoleOutput = databaseCLI.processInput(readStatement);
        assertNotEquals(Database.ITEMS + " is empty.", consoleOutput);
    }

    @Test
    void testUpdateOneItemOneAttribute() {
        createItem();
        ArrayList<Item> items = database.selectFromTable(Database.ITEMS, "*");
        Item originalItem = items.get(0);

        String newName = "newTestName";
        String updateStatement = "UPDATE 1 name = '" + newName + "'";
        databaseCLI.processInput(updateStatement);
        originalItem.setName(newName);

        items = database.selectFromTable(Database.ITEMS, "*");
        Item updatedItem = items.get(0);
        assertEquals(originalItem, updatedItem);
    }

    @Test
    void testDeleteOneItem() {
        createItem();
        String deleteStatement = "DELETE " + testItem.getId();
        databaseCLI.processInput(deleteStatement);
        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testRestoreOneItemReturnsCorrectItem() {
        testDeleteOneItem();
        String restoreStatement = "RESTORE " + testItem.getId();
        String restoredItemMessage = databaseCLI.processInput(restoreStatement);
        String[] itemValues = testItem.getValuesAsArray();
        itemValues[1] = "'" + itemValues[1] + "'"; // put apostrophes around name
        String values = String.join(", ", itemValues);
        assertTrue(restoredItemMessage.contains(values));
    }

    @Test
    void testRestoreOneItemPlacesItemInCorrectTable() {
        testDeleteOneItem();
        String restoreStatement = "RESTORE " + testItem.getId();
        databaseCLI.processInput(restoreStatement);
        assertEquals(0, database.getSizeOfTable(Database.DELETED_ITEMS));
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
    }
}
