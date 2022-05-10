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

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(1, itemsList.size());
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @Test
    void testDatabaseItemEqualityNoDecimal() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(1, itemsList.size());
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @Test
    void testDatabasePopulatedWithCorrectNumberOfItems() {
        database.populateDatabase();
        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(itemsJSONArray.size(), itemsList.size());
    }

    @Test
    void testPopulatedDatabaseItemEquality() {
        database.populateDatabase();
        itemsList = database.selectFromTable(Database.ITEMS, "*");
        // TODO: compare itemList Items with those from itemsJSONArray
        //   maybe add constructor for Item from JSONObject
    }

    @Test
    void testValidIdWithDatabaseDeletion() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());
        database.deleteFromItems(String.valueOf(testItem.getId()));

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(0, itemsList.size());
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
    void testInvalidIdWithDatabaseDeletion() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptId());
        int invalidId = 2;
        database.deleteFromItems(String.valueOf(invalidId));

        itemsList = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(1, itemsList.size());
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
