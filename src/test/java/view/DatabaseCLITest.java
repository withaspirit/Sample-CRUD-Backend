package view;

import model.Database;
import model.Item;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presenter.DatabasePresenter;

import java.util.Arrays;
import java.util.List;

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
        databasePresenter.addDatabase(database);
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
        assertTrue(consoleOutput.contains("ERROR"));
    }

    @Test
    void testReadFromTableWithItems() {
        createItem();
        String readStatement = "READ " + Database.ITEMS;
        String consoleOutput = databaseCLI.processInput(readStatement);
        assertFalse(consoleOutput.contains("ERROR"));
    }

    @Test
    void testUpdateOneItemOneAttribute() {
        createItem();
        List<Item> items = database.selectFromTable(Database.ITEMS, "*");
        Item originalItem = items.get(0);

        String newName = "newTestName";
        String updateStatement = "UPDATE 1 name = '" + newName + "'";
        String consoleOutput = databaseCLI.processInput(updateStatement);
        assertFalse(consoleOutput.contains("ERROR"));

        originalItem.setName(newName);
        items = database.selectFromTable(Database.ITEMS, "*");
        Item updatedItem = items.get(0);
        assertEquals(originalItem, updatedItem);
    }

    @Test
    void testUpdateItemInvalid() {
        createItem();

        int errorId = 10000;
        String newName = "newTestName";
        String updateStatement = "UPDATE " + errorId + " name = '" + newName + "'";
        String consoleOutput = databaseCLI.processInput(updateStatement);
        assertTrue(consoleOutput.contains("ERROR"));
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
