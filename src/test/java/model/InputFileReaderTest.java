package model;

import model.Database;
import model.InputFileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * InputFileReaderTest ensures that the input is read correctly.
 *
 * @author Liam Tripp
 */
public class InputFileReaderTest {

    private JSONObject jsonObject;
    private JSONArray itemsJSONArray;

    @BeforeEach
    void setup() {
        final InputFileReader inputFileReader = new InputFileReader(Database.ITEMS, "json");
        jsonObject = inputFileReader.getJSONFileAsObject();
        itemsJSONArray = inputFileReader.createJSONArray(Database.ITEMS);
    }

    @Test
    void testJSONArrayRetrieved() {
        final Object obj = jsonObject.get(Database.ITEMS);
        final JSONArray jsonArray = (JSONArray) obj;
        assertEquals(jsonArray.size(), itemsJSONArray.size());
        assertEquals(jsonArray, itemsJSONArray);
    }

    @Test
    void testReadingOfSQLFile() {
        final InputFileReader inputFileReader = new InputFileReader("DDL", "sql");
        assertNotNull(inputFileReader.getSQLFileAsString());
    }
}
