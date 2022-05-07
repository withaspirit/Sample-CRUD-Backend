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

    private final String fileName;
    // TODO: ? add file ending to class
//    private final String fileEnding;

    /**
     * Constructor for InputFileReader.
     * 
     * @param fileName the name of the file being read
     */
    public InputFileReader(String fileName) {
        this.fileName = fileName;
    }

    public String[] getValuesToInsert() {
        JSONArray itemsJSONArray = createJSONArray(fileName);

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
     * @param variableName the variable name to retrieve from a JSON file
     * @return JSONArray the JSON file converted to a JSON array
     */
    public JSONArray createJSONArray(String variableName) {
        return (JSONArray) getJSONFileAsObject().get(variableName);
    }

    /**
     * Returns a JSON file with the specified name as a JSONObject.
     *
     * @return JSONObject a JSON file as a JSONObject
     */
    public JSONObject getJSONFileAsObject() {
        try {
            InputStreamReader inputStreamReader = createInputStreamReader(fileName + ".json");
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
