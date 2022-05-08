import java.sql.*;
import java.util.ArrayList;

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
            throw new RuntimeException(e);
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            throw new RuntimeException(e);
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
        InputFileReader inputFileReader = new InputFileReader(ITEMS, "json");
        // FIXME: insertion arguments inconsistent
        String[] valuesToInsert = inputFileReader.getValuesToInsert();
        String columnsToInsert = Item.getAttributeNamesExceptId();

        for (String values : valuesToInsert) {
            insert(ITEMS, columnsToInsert, values);
        }
    }

    public void closeDatabase() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // connection close failed.
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a set of values into a table.
     *
     * @param tableName the name of the table to insert into
     * @param columns the set of columns selected for insertion as a string
     * @param values the set of values to insert as a string
     */
    public void insert(String tableName, String columns, String values) {
        String statementToExecute = "INSERT INTO " + tableName +
                "(" + columns + ") VALUES (" + values + ");";
        try {
            // TODO?: used executeUpdate with columns names
            statement.executeUpdate(statementToExecute);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Selects and returns an ArrayList of Items from the items table
     *
     * @param selectedItems the range of items to be selected
     * @return arrayList of items contained in the items table
     */
    public ArrayList<Item> selectItems(String selectedItems) {
        ResultSet resultSet = getResultSet(ITEMS, selectedItems);
        ArrayList<Item> items = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Item item = new Item(resultSet);
                items.add(item);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    /**
     * Selects and returns a ResultSet of selected rows from a selected table.
     *
     * @param tableName the name of the table
     * @param selectedRows the rows to be selected
     * @return resultSet containing rows of a table
     */
    public ResultSet getResultSet(String tableName, String selectedRows) {
        // TODO?: add "where"
        String statementToExecute = "SELECT " + selectedRows + " FROM " + tableName;
        try {
            return statement.executeQuery(statementToExecute);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
