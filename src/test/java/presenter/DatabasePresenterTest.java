package presenter;

import model.Database;
import model.DeletedItem;
import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import view.Command;
import view.DatabaseCLI;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabasePresenterTest ensures the DatabasePresenter's methods work
 * correctly.
 *
 * @author Liam Tripp
 */
public class DatabasePresenterTest {

    private Database database;
    private DatabasePresenter databasePresenter;
    private Item testItem;

    @BeforeEach
    void setup() {
        database = new Database();
        databasePresenter = new DatabasePresenter();
        databasePresenter.addModel(database);
        database.initializeDatabase();
        testItem = new Item(1, "testName", "0.0", 1);
    }

    public DeletedItem deleteItemWithComment(String comment) {
        databasePresenter.createItem(testItem);
        String itemId = String.valueOf(testItem.getId());
        DeletedItem deletedItem = new DeletedItem(testItem, comment);

        databasePresenter.deleteItem(itemId, deletedItem.getComment());
        return deletedItem;
    }

    @Test
    void testInsertOneItem() {
        databasePresenter.createItem(testItem);
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));

        ArrayList<Item> items = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(testItem, items.get(0));
    }

    @Test
    void testReadValidTableName() {
        ArrayList<Item> items = databasePresenter.readFromTable(Database.ITEMS);
        assertNotNull(items);
    }

    @Test
    void testReadInvalidTableName() {
        String testName = "InvalidName";
        ArrayList<Item> items = databasePresenter.readFromTable(testName);
        assertNull(items);
    }

    @Test
    void testUpdateOneItemOneAttribute() {
        // TODO: move into DatabaseCLI
        databasePresenter.createItem(testItem);
        String updatedName = "chowder";

        String UPDATE_REGEX = Command.UPDATE.getRegex();
        String updatePhrase = "UPDATE 1 name = '" + updatedName + "'";

        // we need databaseCLI for its matcher methods
        DatabaseCLI databaseCLI = new DatabaseCLI();
        Matcher matcher = databaseCLI.getMatcher(UPDATE_REGEX, updatePhrase);
        String matcherError = databaseCLI.validateMatcher(matcher);
        assertEquals("", matcherError);

        databasePresenter.updateItem(matcher);
        testItem.setName(updatedName);

        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
        ArrayList<Item> items = databasePresenter.readFromTable(Database.ITEMS);
        assertEquals(testItem, items.get(0));
    }

    @Test
    void testDeleteMultipleItems() {
        // TODO?
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "words words words"})
    void testDeletionInsertsIntoCorrectTable(String comment) {
        DeletedItem deletedItem = deleteItemWithComment(comment);
        String itemId = String.valueOf(deletedItem.getId());

        databasePresenter.deleteItem(itemId, deletedItem.getComment());
        assertEquals(1, database.getSizeOfTable(Database.DELETED_ITEMS));
        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "words words words"})
    void testDeletedItemEquality(String comment) {
        DeletedItem deletedItem = deleteItemWithComment(comment);
        String itemId = String.valueOf(deletedItem.getId());

        Item item = database.selectFromTable(Database.DELETED_ITEMS, "*", itemId);
        DeletedItem deletedItemFromTable = (DeletedItem) item;
        assertEquals(deletedItem, deletedItemFromTable);
    }

    @Test
    void testRestoreFromEmptyTableProducesNull() {
        // should be the same for invalid id
        String itemId = String.valueOf(testItem.getId());
        Item restoredItem = databasePresenter.restoreItem(itemId);
        assertNull(restoredItem);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "words words words"})
    void testRestoreItemEquality(String comment) {
        DeletedItem deletedItem = deleteItemWithComment(comment);
        String itemId = String.valueOf(deletedItem.getId());

        Item retrievedItem = database.selectFromTable(Database.DELETED_ITEMS, "*", itemId);
        Item restoredItem = databasePresenter.restoreItem(itemId);
        assertEquals(testItem, retrievedItem);
        assertEquals(testItem, restoredItem);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "words words words"})
    void testRestoreItemInsertsIntoItemsTable(String comment) {
        DeletedItem deletedItem = deleteItemWithComment(comment);
        String itemId = String.valueOf(deletedItem.getId());

        databasePresenter.restoreItem(itemId);
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
        assertEquals(0, database.getSizeOfTable(Database.DELETED_ITEMS));
    }
}
