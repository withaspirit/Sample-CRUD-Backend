package model;

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
        InputFileReader inputFileReader = new InputFileReader(Table.ITEMS.getName(), "json");
        jsonObject = inputFileReader.getJSONFileAsObject();
        itemsJSONArray = inputFileReader.createJSONArray(Table.ITEMS.getName());
    }

    @Test
    void testJSONArrayRetrieved() {
        Object obj = jsonObject.get(Table.ITEMS.getName());
        JSONArray jsonArray = (JSONArray) obj;
        assertEquals(jsonArray.size(), itemsJSONArray.size());
        assertEquals(jsonArray, itemsJSONArray);
    }

    @Test
    void testReadingOfSQLFile() {
        InputFileReader inputFileReader = new InputFileReader("DDL", "sql");
        assertNotNull(inputFileReader.getSQLFileAsString());
    }
}
