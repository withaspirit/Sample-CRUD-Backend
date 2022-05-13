package model;

import org.json.simple.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabaseTest ensures the Database methods function properly.
 *
 * @author Liam Tripp
 */
public class DatabaseTest {

    private Database database;
    private ArrayList<Item> itemsList;
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
        database.closeDatabase();
    }

    @Test
    void testDatabaseInsertionForOneItem() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @Test
    void testDatabaseItemEqualityNoDecimal() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());

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
    void testPopulatedDatabaseItemEquality() {
        database.populateDatabase();
        itemsList = database.selectFromTable(Database.ITEMS, "*");
        // TODO: compare itemList Items with those from itemsJSONArray
        //   maybe add constructor for Item from JSONObject
    }

    @Test
    void testInsertIntoDeletedItems() {
        database.insert(Database.DELETED_ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());
        assertEquals(1, database.getSizeOfTable(Database.DELETED_ITEMS));
    }

    @Test
    void testDeleteItemWithValidId() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());
        database.deleteFromTable(Database.ITEMS, String.valueOf(testItem.getId()));

        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testSelectingFromDatabaseItemWithDecimalPrice() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(testItem, itemsList.get(0));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "-1"}) // empty table, invalid id
    void testSelectingFromTableProducesNull(String itemId) {
        Item item = database.selectFromTable(Database.DELETED_ITEMS,
                "*", itemId).get(0);
        assertNull(item);
    }

    @Test
    void testSelectingFromEmptyTableIsEmpty() {
        ArrayList<Item> items = database.selectFromTable(Database.DELETED_ITEMS, "*");
        assertTrue(items.isEmpty());
    }

    @Test
    void testDeleteItemWithInvalidId() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());

        int invalidId = 2;
        database.deleteFromTable(Database.ITEMS, String.valueOf(invalidId));
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testUpdatingOneValueOneItem() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());

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
                testItem.getAttributeValuesExceptId());

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
}
