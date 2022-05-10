package presenter;

import model.Database;
import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.Command;
import view.DatabaseCLI;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void testInsertOneItem() {
        databasePresenter.createItem(testItem);
        ArrayList<Item> items = database.selectFromTable(Database.ITEMS, "*");
        assertEquals(1, items.size());
        assertEquals(testItem, items.get(0));
    }

    @Test
    void testUpdateOneItemOneAttribute() {
        // TODO: move into DatabaseCLI
        databasePresenter.createItem(testItem);
        String updatedName = "chowder";

        String UPDATE_REGEX = Command.UPDATE.getRegex();
        String updatePhrase = "UPDATE 1 name = '" + updatedName + "'";
        Matcher matcher = (new DatabaseCLI()).getMatcher(UPDATE_REGEX, updatePhrase);
        assertNotNull(matcher);

        databasePresenter.updateItem(matcher);
        testItem.setName(updatedName);

        ArrayList<Item> items = databasePresenter.readFromTable(Database.ITEMS);
        assertEquals(1, items.size());
        assertEquals(testItem, items.get(0));
    }
}
