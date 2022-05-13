package model;

import org.json.simple.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabaseTest ensures the Database methods function properly.
 *
 * @author Liam Tripp
 */
public class DatabaseTest {

    private Database database;
    private List<Item> itemsList;
    private JSONArray itemsJSONArray;
    private final Item testItem = new Item(1, "testItem", "100.99", 0);

    @BeforeEach
    void setup() {
        database = new Database();
        database.initializeDatabase();
        InputFileReader inputFileReader = new InputFileReader(Database.ITEMS, "json");
        itemsJSONArray = inputFileReader.createJSONArray(Database.ITEMS);
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @Test
    void testDatabaseInsertionForOneItem() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @Test
    void testDatabasePopulatedWithCorrectNumberOfItems() {
        database.populateDatabase();
        assertEquals(itemsJSONArray.size(), database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testInsertIntoDeletedItems() {
        database.insert(Database.DELETED_ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());
        assertEquals(1, database.getSizeOfTable(Database.DELETED_ITEMS));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.", ".0", "0.0", "100.99", "10000.999"})
    void testSelectingFromDatabaseValidWithDifferentPrices(String price) {
        testItem.setPrice(price);
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "-1"}) // empty table, invalid id
    void testSelectingFromTableWithInvalidInputProducesNull(String itemId) {
        List<Item> items = database.selectFromTable(Database.DELETED_ITEMS,
                "*", itemId);
        assertTrue(items.isEmpty());
    }

    @Test
    void testUpdatingOneValueOneItem() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        String newName = "NewName";
        String updateStatement = "name = '" + newName + "'";
        database.updateItem(String.valueOf(testItem.getId()), updateStatement);
        testItem.setName(newName);

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(testItem, itemsList.get(0));
    }

    @Test
    void testUpdatingMultipleValuesOneItem() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        String newName = "NewName";
        String price = String.valueOf(10.99);
        int stock = 2;
        testItem.setName(newName);
        testItem.setPrice(price);
        testItem.setStock(stock);

        database.updateItem(String.valueOf(testItem.getId()),
                testItem.getAttributeNameValueListExceptId());

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(testItem, itemsList.get(0));
    }

    @Test
    void testUpdatingItemInvalid() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        int invalidId = -3;
        Item item = database.updateItem(String.valueOf(invalidId),
                        testItem.getAttributeNameValueListExceptId());
        assertNull(item);
    }

    @Test
    void testDeleteItemWithValidId() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());
        database.deleteFromTable(Database.ITEMS, String.valueOf(testItem.getId()));

        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testDeleteItemWithInvalidId() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        int invalidId = 2;
        database.deleteFromTable(Database.ITEMS, String.valueOf(invalidId));
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
    }
}
