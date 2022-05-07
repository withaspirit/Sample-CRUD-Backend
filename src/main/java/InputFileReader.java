import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * InputFileReader allows files to be read and returned as usable objects.
 *
 * @author Liam Tripp
 */
public class InputFileReader {

    public static final String ITEMS_FILENAME = "items";

    /**
     * Constructor for InputFileReader.
     */
    public InputFileReader() {
    }

    public String[] getValuesToInsert() {

        JSONArray itemsJSONArray = createJSONArray(ITEMS_FILENAME);

        String[] valuesToInsert = new String[itemsJSONArray.size()];
        for (int i = 0; i < itemsJSONArray.size(); i++) {
            JSONObject item = (JSONObject) itemsJSONArray.get(i);

            // TODO: don't use magic strings
            String name = "'" + item.get("name") + "'";
            String price = Long.toString((long) item.get("price"));
            String stock = Long.toString((long) item.get("stock"));

            valuesToInsert[i] = String.join(",", name, price, stock);
        }
        return valuesToInsert;
    }

    /**
     * Returns a JSONArray for a JSON file with the specified name.
     *
     * @param name the name for the JSON file
     * @return JSONArray the JSON file converted to a JSON array
     */
    public JSONArray createJSONArray(String name) {
        return (JSONArray) getJSONFileAsObject(name).get(name);
    }

    /**
     * Returns a JSON file with the specified name as a JSONObject.
     *
     * @param name the name of the JSON file
     * @return JSONObject a JSON file as a JSONObject
     */
    public JSONObject getJSONFileAsObject(String name) {
        try {
            InputStreamReader inputStreamReader = createInputStreamReader(name + ".json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(inputStreamReader);
            return (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates an InputStreamReader for a file with the specified name.
     * If the project is not a Maven project, prepend "[file's package]/
     * if necessary.
     *
     * @param name the name of the file
     * @return inputStreamReader an InputStreamReader for the file
     */
    private InputStreamReader createInputStreamReader(String name) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name);
            assert inputStream != null;
            // Specify CharSet as UTF-8
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println(name + " was not found.");
            e.printStackTrace();
            return null;
        }
    }
}
