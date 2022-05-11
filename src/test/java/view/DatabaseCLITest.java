package view;

import model.Database;
import model.InputFileReader;
import model.Item;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import presenter.DatabasePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DatabaseCLITest ensures that DatabaseCLI's CRUD methods work properly. It
 * also ensures the Command.Regex functions work properly with DatabaseCLI's
 * Matcher functions.
 *
 * @author Liam Tripp
 */
public class DatabaseCLITest {

    private Database database;
    private DatabasePresenter databasePresenter;
    private DatabaseCLI databaseCLI;

    @BeforeEach
    void setup() {
        database = new Database();
        database.initializeDatabase();
        databasePresenter = new DatabasePresenter();
        databasePresenter.addModel(database);
        databaseCLI = new DatabaseCLI();
        databaseCLI.addPresenter(databasePresenter);
        InputFileReader inputFileReader = new InputFileReader("inputs", "json");
        JSONObject jsonObject = inputFileReader.getJSONFileAsObject();
    }

    @Test
    void testCreateOneItem() {
        String userInput = "CREATE testName 1.99 1".toLowerCase();
        databaseCLI.processInput(userInput);
        assertEquals(1, database.getSizeOfTable(Database.ITEMS));
    }

    @Test
    void testReadFromEmptyTable() {
        String userInput = "READ " + Database.ITEMS;
        String consoleOutput = databaseCLI.processInput(userInput);
        assertEquals(Database.ITEMS + " is empty.", consoleOutput);
    }

    @Test
    void testReadFromTableWithItems() {
        String createStatement = "CREATE WalterWhite 1.99 1".toLowerCase();
        databaseCLI.processInput(createStatement);

        String readStatement = "READ " + Database.ITEMS;
        String consoleOutput = databaseCLI.processInput(readStatement);
        assertNotEquals(Database.ITEMS + " is empty.", consoleOutput);
        System.out.println(consoleOutput);
    }

    @Test
    void testUpdateOneItemOneAttribute() {
        String createStatement = "CREATE WalterWhite 1.99 1".toLowerCase();
        databaseCLI.processInput(createStatement);
        ArrayList<Item> items = database.selectFromTable(Database.ITEMS, "*");
        Item originalItem = items.get(0);

        String newName = "Heisenberg";
        String updateStatement = "UPDATE 1 name = '" + newName + "'";
        databaseCLI.processInput(updateStatement);
        originalItem.setName(newName);

        items = database.selectFromTable(Database.ITEMS, "*");
        Item updatedItem = items.get(0);
        assertEquals(originalItem, updatedItem);
    }

    @Test
    void testDeleteOneItem() {
        testCreateOneItem();
        int itemId = 1;
        String deleteStatement = "DELETE " + itemId;
        databaseCLI.processInput(deleteStatement);
        assertEquals(0, database.getSizeOfTable(Database.ITEMS));
    }
}
