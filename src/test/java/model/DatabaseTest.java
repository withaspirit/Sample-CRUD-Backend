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
        InputFileReader inputFileReader = new InputFileReader(Table.ITEMS.getName(), "json");
        itemsJSONArray = inputFileReader.createJSONArray(Table.ITEMS.getName());
    }

    @AfterEach
    void tearDown() {
        database.shutdown();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "0.", ".0", "0.0", "100.99", "10000.999"})
    void testItemResultSetConstructorConvertsPricesCorrectly(String price) {
        testItem.setPrice(price);
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        // Item's ResultSet constructor is used here
        itemsList = database.selectFromTable(Table.ITEMS.getName(), "*");
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @Test
    void testDatabaseInsertionForOneItem() {
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());
        assertEquals(1, database.getSizeOfTable(Table.ITEMS.getName()));

        itemsList = database.selectFromTable(Table.ITEMS.getName(), "*");
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @Test
    void testInsertIntoDeletedItems() {
        database.insert(Table.DELETED_ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());
        assertEquals(1, database.getSizeOfTable(Table.DELETED_ITEMS.getName()));
    }

    @Test
    void testDatabasePopulatedWithCorrectNumberOfItems() {
        database.populateDatabase();
        assertEquals(itemsJSONArray.size(), database.getSizeOfTable(Table.ITEMS.getName()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "-1"})
        // empty table, invalid id
    void testSelectingFromTableWithInvalidInputProducesNull(String itemId) {
        List<Item> items = database.selectFromTable(Table.DELETED_ITEMS.getName(),
                "*", itemId);
        assertTrue(items.isEmpty());
    }

    @Test
    void testUpdatingOneValueOneItem() {
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        String newName = "NewName";
        String updateStatement = "name = '" + newName + "'";
        database.updateItem(String.valueOf(testItem.getId()), updateStatement);
        testItem.setName(newName);

        itemsList = database.selectFromTable(Table.ITEMS.getName(), "*");
        assertEquals(testItem, itemsList.get(0));
    }

    @Test
    void testUpdatingMultipleValuesOneItem() {
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        String newName = "NewName";
        String price = String.valueOf(10.99);
        int stock = 2;
        testItem.setName(newName);
        testItem.setPrice(price);
        testItem.setStock(stock);

        database.updateItem(String.valueOf(testItem.getId()),
                testItem.getAttributeNameValueListExceptId());

        itemsList = database.selectFromTable(Table.ITEMS.getName(), "*");
        assertEquals(testItem, itemsList.get(0));
    }

    @Test
    void testUpdatingItemInvalid() {
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        int invalidId = -3;
        Item item = database.updateItem(String.valueOf(invalidId),
                testItem.getAttributeNameValueListExceptId());
        assertNull(item);
    }

    @Test
    void testDeleteItemWithValidId() {
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());
        database.deleteFromTable(Table.ITEMS.getName(), String.valueOf(testItem.getId()));

        assertEquals(0, database.getSizeOfTable(Table.ITEMS.getName()));
    }

    @Test
    void testDeleteItemWithInvalidId() {
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                testItem.getValuesInSQLFormatExceptId());

        int invalidId = 2;
        database.deleteFromTable(Table.ITEMS.getName(), String.valueOf(invalidId));
        assertEquals(1, database.getSizeOfTable(Table.ITEMS.getName()));
    }
}
