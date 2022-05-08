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

    @BeforeEach
    void setup() {
        database = new Database();
        database.initializeDatabase();
        InputFileReader inputFileReader = new InputFileReader("items", "json");
        itemsJSONArray = inputFileReader.createJSONArray(Database.ITEMS);
    }

    @AfterEach
    void tearDown() {
        database.closeDatabase();
    }

    @Test
    void testDatabaseInsertionForOneItem() {
        Item item = new Item(0, "Default", 0, 0);
        // TODO: replace magic strings
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                item.getAttributeValuesExceptID());

        itemsList = database.selectItems("*");
        assertEquals(1, itemsList.size());
        Item itemFromDatabase = itemsList.get(0);
        assertEquals(item, itemFromDatabase);
    }

    @Test
    void testDatabasePopulatedWithCorrectNumberOfItems() {
        database.populateDatabase();
        itemsList = database.selectItems("*");
        assertEquals(itemsJSONArray.size(), itemsList.size());
    }
}
