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
    private Item testItem = new Item(1, "testItem", 0, 0);

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
                testItem.getAttributeValuesExceptID());

        itemsList = database.selectFromItems("*");
        assertEquals(1, itemsList.size());
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(testItem, itemFromDatabase);
    }

    @Test
    void testDatabasePopulatedWithCorrectNumberOfItems() {
        database.populateDatabase();
        itemsList = database.selectFromItems("*");
        assertEquals(itemsJSONArray.size(), itemsList.size());
    }

    @Test
    void testPopulatedDatabaseItemEquality() {
        database.populateDatabase();
        itemsList = database.selectFromItems("*");
        // TODO: compare itemList Items with those from itemsJSONArray
        //   maybe add constructor for Item from JSONObject
    }

    @Test
    void testValidIdWithDatabaseDeletion() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptID());
        database.deleteFromItems(String.valueOf(testItem.getId()));
        itemsList = database.selectFromItems("*");
        assertEquals(0, itemsList.size());
    }

    @Test
    void testInvalidIdWithDatabaseDeletion() {
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                testItem.getAttributeValuesExceptID());
        int invalidId = 2;
        database.deleteFromItems(String.valueOf(invalidId));
        itemsList = database.selectFromItems("*");
        assertEquals(1, itemsList.size());
    }
}
