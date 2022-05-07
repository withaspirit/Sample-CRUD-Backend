import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
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
    private final String fileEnding;
    private final static String JSON = "json";

    /**
     * Constructor for InputFileReader.
     * 
     * @param fileName the name of the file being read
     */
    public InputFileReader(String fileName, String fileEnding) {
        this.fileName = fileName;
        this.fileEnding = fileEnding;
    }

    public String[] getValuesToInsert() {
        if (!fileEnding.equals(JSON)) {
            throw new IllegalArgumentException("File type must be .json.");
        }
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
        if (!fileEnding.equals(JSON)) {
            throw new IllegalArgumentException("File type must be .json.");
        }
        return (JSONArray) getJSONFileAsObject().get(variableName);
    }

    /**
     * Returns a JSON file with the specified name as a JSONObject.
     *
     * @return JSONObject a JSON file as a JSONObject
     */
    public JSONObject getJSONFileAsObject() {
        if (!fileEnding.equals(JSON)) {
            throw new IllegalArgumentException("File type must be .json.");
        }
        InputStreamReader inputStreamReader = createInputStreamReader();
        JSONParser parser = new JSONParser();
        Object obj = null;

        try {
            obj = parser.parse(inputStreamReader);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return (JSONObject) obj;
    }

    /**
     * Creates an InputStreamReader for a file with the specified name.
     * If the project is not a Maven project, prepend "[file's package]/"
     * if necessary.
     *
     * @return inputStreamReader an InputStreamReader for the file
     */
    private InputStreamReader createInputStreamReader() {
        try {
            InputStream inputStream = getClass().getClassLoader().
                    getResourceAsStream(fileName + "." + fileEnding);
            assert inputStream != null;
            // Specify CharSet as UTF-8
            return new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println(fileName + " was not found.");
            e.printStackTrace();
            return null;
        }
    }
}
