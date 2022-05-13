package model;

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

    /**
     * Returns an SQL file as a String.
     *
     * @return the sql file as a String
     */
    public String getSQLFileAsString() {
        if (!fileEnding.equalsIgnoreCase("SQL")) {
            throw new IllegalArgumentException("File type must be .sql");
        }
        // IntelliJ says the inputStream could be null, but that's handled in
        // createInputStream(). Ignore the warning
        InputStream inputStream = createInputStream();
        String sqlTable;
        try {
            sqlTable = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sqlTable;
    }

    /**
     * Returns values for Database to insert into the appropriate table(s).
     *
     * @return SQL-formatted values for Database to insert into the appropriate table.
     */
    public String[] getValuesToInsertFromJSONFile() {
        if (!fileEnding.equals(JSON)) {
            throw new IllegalArgumentException("File type must be .json");
        }

        JSONArray itemsJSONArray = createJSONArray(fileName);
        String[] valuesToInsert = new String[itemsJSONArray.size()];
        for (int i = 0; i < itemsJSONArray.size(); i++) {
            JSONObject jsonItem = (JSONObject) itemsJSONArray.get(i);
            Item item = new Item(jsonItem);
            valuesToInsert[i] = item.getAttributeValuesExceptId();
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
        if (!fileEnding.equalsIgnoreCase(JSON)) {
            throw new IllegalArgumentException("File type must be .json.");
        }
        InputStream inputStream = createInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
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
     * @return inputStream an InputStreamReader for the file
     */
    private InputStream createInputStream() {
        try {
            InputStream inputStream = getClass().getClassLoader().
                    getResourceAsStream(fileName + "." + fileEnding);
            assert inputStream != null;
            return inputStream;
        } catch (Exception e) {
            System.err.println(fileName + " was not found.");
            e.printStackTrace();
            return null;
        }
    }
}
