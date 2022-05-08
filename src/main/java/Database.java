import java.sql.*;
import java.util.Arrays;

/**
 * Database contains the CRUD functionality for the SQLite database.
 * (CRUD = create, read, update, destroy)
 *
 * @author Liam Tripp
 */
public class Database {

    private Connection connection;
    private Statement statement;
    public final static String ITEMS = "items";
    private final static String CLASS_LOADER_NAME = "org.sqlite.JDBC";
    private final static String DATABASE_NAME = "jdbc:sqlite:items.db";

    public Database() {
        // load the sqlite-JDBC driver using the current class loader
        try {
            Class.forName(CLASS_LOADER_NAME);
            connection = DriverManager.getConnection(DATABASE_NAME);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
    }

    public void initializeDatabase() {
        InputFileReader inputFileReader = new InputFileReader("DDL", "sql");
        String sqlTableCreateStatement = inputFileReader.getSQLFileAsString();
        try {
            statement.executeUpdate(sqlTableCreateStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void populateDatabase() {
        String[] columns = { "id", "name", "price", "stock" };
        String[] allColumnsExceptID = Arrays.copyOfRange(columns, 1, columns.length - 1);
        String columnsToInsert = String.join(",", allColumnsExceptID);

        InputFileReader inputFileReader = new InputFileReader(ITEMS, "json");
        String[] valuesToInsert = inputFileReader.getValuesToInsert();

        for (String values : valuesToInsert) {
            insert(ITEMS, columnsToInsert, values);
        }
    }

    public void closeDatabase() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e);
        }
    }

    /**
     * Inserts a set of values into a table.
     *
     * @param tableName the name of the table to insert into
     * @param columns the set of columns selected for insertion
     * @param values the set of values to insert
     */
    public void insert(String tableName, String columns, String values) {
        String statementToExecute = "INSERT INTO " + tableName +
                "(" + columns + ") VALUES (" + values + ");";
        try {
            statement.executeUpdate(statementToExecute);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
