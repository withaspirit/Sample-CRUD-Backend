package presenter;

import model.Database;
import model.DeletedItem;
import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private DeletedItem deletedItemWithComment;

    @BeforeEach
    void setup() {
        database = new Database();
        databasePresenter = new DatabasePresenter();
        databasePresenter.addModel(database);
        database.initializeDatabase();
        testItem = new Item(1, "testName", "0.0", 1);
        deletedItemWithComment = new DeletedItem(testItem);
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

    @Test
    void testDeletionWithoutCommentInsertsIntoDeletedItemsTable() {
        databasePresenter.createItem(testItem);
        String itemId = String.valueOf(testItem.getId());
        String comment = "";

        databasePresenter.deleteItem(itemId, comment);
        assertEquals(1, database.getSizeOfTable(Database.DELETED_ITEMS));
        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testDeletionWithCommentInsertsIntoDeletedItemsTable() {
        databasePresenter.createItem(testItem);
        String itemId = String.valueOf(testItem.getId());
        databasePresenter.deleteItem(itemId, deletedItemWithComment.getComment());
        assertEquals(1, database.getSizeOfTable(Database.DELETED_ITEMS));
        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testDeletedItemEquality() {
        databasePresenter.createItem(testItem);
        String itemId = String.valueOf(testItem.getId());
        databasePresenter.deleteItem(itemId, deletedItemWithComment.getComment());

        ArrayList<Item> items = databasePresenter.readFromTable(Database.DELETED_ITEMS);
        DeletedItem deletedItemFromTable = (DeletedItem) items.get(0);

        assertEquals(deletedItemWithComment, deletedItemFromTable);
    }

    @Test
    void testRestoreItemEquality() {
        testDeletionWithCommentInsertsIntoDeletedItemsTable();
        String itemId = String.valueOf(testItem.getId());

        Item retrievedItem = database.selectFromTable(Database.DELETED_ITEMS, "*", itemId);
        Item restoredItem = databasePresenter.restoreItem(itemId);
        assertEquals(testItem, retrievedItem);
        assertEquals(testItem, restoredItem);
        assertEquals(restoredItem, restoredItem);
    }

    @Test
    void testRestoreItemInsertsIntoItemsTable() {
        testDeletionWithoutCommentInsertsIntoDeletedItemsTable();
        String itemId = String.valueOf(testItem.getId());
        databasePresenter.restoreItem(itemId);
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
        assertEquals(0, database.getSizeOfTable(Database.DELETED_ITEMS));
    }
}
