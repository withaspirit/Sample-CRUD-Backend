import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DatabasePresenterTest ensures the DatabasePresenter's methods work
 * correctly.
 *
 * @author Liam Tripp
 */
public class DatabasePresenterTest {

    private Database database;
    private DatabasePresenter databasePresenter;

    @BeforeEach
    void setup() {
        database = new Database();
        databasePresenter = new DatabasePresenter();
        databasePresenter.addModel(database);
        database.initializeDatabase();;
    }

    @Test
    void testDatabaseInsertionOneItem() {
        Item item = new Item(1, "testName", "0.0", 1);
        databasePresenter.insertItem(item);
        ArrayList<Item> items = database.selectFromItems("*");
        assertEquals(1, items.size());
        assertEquals(item, items.get(0));
    }
}
