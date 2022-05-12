package model;

import org.json.simple.JSONArray;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DatabaseTest ensures the Database methods function properly.
 *
 * @author Liam Tripp
 */
public class DatabaseTest {

    private Database database;
    private ArrayList<Item> itemsList;
    private JSONArray itemsJSONArray;
    private final Item testItem = new Item(1, "testItem", "0.00", 0);

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
    void testValidIdWithDatabaseDeletion() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());
        database.deleteFromTable(Database.ITEMS, String.valueOf(testItem.getId()));

        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testSelectingFromDatabaseItemWithDecimalPrice() {
        String testName = "name";
        String price = "10.99";
        Item itemWithDecimalPrice = new Item(1, testName, price, 0);
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                itemWithDecimalPrice.getAttributeValuesExceptId());

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(itemWithDecimalPrice, itemsList.get(0));
    }

    @Test
    void testSelectOneItem() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());

        Item testItem2 = new Item(2, "testItem2", "2.22", 2);
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem2.getAttributeValuesExceptId());
        assertEquals(2, database.getSizeOfTable(Database.ITEMS));

        String itemId2 = String.valueOf(testItem2.getId());
        Item retrievedItem = database.selectFromTable(Database.ITEMS, "*",
                itemId2);
        assertEquals(testItem2, retrievedItem);
    }

    @Test
    void testInvalidIdWithDatabaseDeletion() {
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
        database.updateItems(String.valueOf(testItem.getId()), updateStatement);
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

        database.updateItems(String.valueOf(testItem.getId()),
                testItem.getAttributeNameValueListExceptId());

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(testItem, itemsList.get(0));
    }
}
