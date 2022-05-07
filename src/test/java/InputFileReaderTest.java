import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        final InputFileReader inputFileReader = new InputFileReader();
        jsonObject = inputFileReader.getJSONFileAsObject(InputFileReader.ITEMS_FILENAME);
        itemsJSONArray = inputFileReader.createJSONArray(InputFileReader.ITEMS_FILENAME);
    }

    @Test
    void testJSONArrayRetrieved() {
        final Object obj = jsonObject.get(InputFileReader.ITEMS_FILENAME);
        final JSONArray jsonArray = (JSONArray) obj;
        assertEquals(jsonArray.size(), itemsJSONArray.size());
        assertEquals(jsonArray, itemsJSONArray);
    }
}
