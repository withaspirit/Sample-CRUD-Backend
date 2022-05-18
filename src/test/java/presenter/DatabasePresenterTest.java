package presenter;

import model.Database;
import model.DeletedItem;
import model.Item;
import model.Table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabasePresenterTest ensures the DatabasePresenter's CRUD methods work
 * properly with the Database.
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
        databasePresenter.addDatabase(database);
        database.initializeDatabase();
        testItem = new Item(1, "testName", "100.99", 1);
    }

    @AfterEach
    void tearDown() {
        databasePresenter.terminateDatabase();
    }

    /**
     * Deletes an item with the provided comment from the Items database.
     *
     * @param comment the optional comment for the item's deletion
     * @return the item deleted from the Items database and insert into the Deleted_Items database
     */
    DeletedItem deleteItemWithComment(String comment) {
        databasePresenter.createItem(testItem);
        String itemId = String.valueOf(testItem.getId());
        DeletedItem deletedItem = new DeletedItem(testItem, comment);

        databasePresenter.deleteItem(itemId, deletedItem.getComment());
        return deletedItem;
    }

    @Test
    void testInsertOneItem() {
        databasePresenter.createItem(testItem);
        assertEquals(1, database.getSizeOfTable(Table.ITEMS.getName()));

        List<Item> items = database.selectFromTable(Table.ITEMS.getName(), "*");
        assertEquals(testItem, items.get(0));
    }

    @Test
    void testReadValidTableName() {
        database.populateDatabase();
        List<Item> items = databasePresenter.readFromTable(Table.ITEMS.getName());
        assertNotNull(items);
    }

    @Test
    void testReadInvalidTableName() {
        String testName = "InvalidName";
        List<Item> items = databasePresenter.readFromTable(testName);
        assertNull(items);
    }

    @Test
    void testUpdateOneItemOneAttribute() {
        databasePresenter.createItem(testItem);

        String updatedName = "chowder";
        String itemId = String.valueOf(testItem.getId());
        String nameValuePair = "name = '" + updatedName + "'";
        Item item = databasePresenter.updateItem(itemId, nameValuePair);

        assertNotNull(item);
        assertEquals(1, database.getSizeOfTable(Table.ITEMS.getName()));

        testItem.setName(updatedName);
        List<Item> items = databasePresenter.readFromTable(Table.ITEMS.getName());
        Item updatedItem = items.get(0);
        assertEquals(testItem, updatedItem);
    }

    @Test
    void testUpdateItemInvalid() {
        databasePresenter.createItem(testItem);

        String updatedName = "chowder";
        int errorId = -1;
        String errorItemId = String.valueOf(errorId);
        String nameValuePair = "name = '" + updatedName + "'";
        Item item = databasePresenter.updateItem(errorItemId, nameValuePair);
        assertNull(item);
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
        assertEquals(1, database.getSizeOfTable(Table.DELETED_ITEMS.getName()));
        assertEquals(0, database.getSizeOfTable(Table.ITEMS.getName()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "words words words"})
    void testDeletedItemEquality(String comment) {
        DeletedItem deletedItem = deleteItemWithComment(comment);
        String itemId = String.valueOf(deletedItem.getId());

        Item item = database.selectFromTable(Table.DELETED_ITEMS.getName(), "*", itemId).get(0);
        DeletedItem deletedItemFromTable = (DeletedItem) item;
        assertEquals(deletedItem, deletedItemFromTable);
    }

    @Test
    void testDeletedItemWithInvalidIdProducesNull() {
        databasePresenter.createItem(testItem);

        int errorId = 1000;
        String itemId = String.valueOf(errorId);
        Item item = databasePresenter.deleteItem(itemId, "");
        assertNull(item);
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

        Item retrievedItem = database.selectFromTable(Table.DELETED_ITEMS.getName(), "*", itemId).get(0);
        Item restoredItem = databasePresenter.restoreItem(itemId);
        assertEquals(testItem, retrievedItem);
        assertEquals(testItem, restoredItem);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "words words words"})
    void testRestoreItemInsertsIntoCorrectTable(String comment) {
        DeletedItem deletedItem = deleteItemWithComment(comment);
        String itemId = String.valueOf(deletedItem.getId());

        databasePresenter.restoreItem(itemId);
        assertEquals(1, database.getSizeOfTable(Table.ITEMS.getName()));
        assertEquals(0, database.getSizeOfTable(Table.DELETED_ITEMS.getName()));
    }

    @Test
    void testRestoreItemInvalid() {
        deleteItemWithComment("");

        int errorId = 1000;
        String itemId = String.valueOf(errorId);
        Item item = databasePresenter.restoreItem(itemId);
        assertNull(item);
    }
}
