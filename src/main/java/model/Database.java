package model;

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
    public final static String DELETED_ITEMS = "deleted_items";
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
        executeStatement(sqlTableCreateStatement);
    }

    public void populateDatabase() {
        InputFileReader inputFileReader = new InputFileReader(ITEMS, "json");
        // FIXME: insertion arguments inconsistent
        String[] valuesToInsert = inputFileReader.getValuesToInsertFromJSONFile();
        String columnsToInsert = Item.getAttributeNamesExceptId();

        for (String values : valuesToInsert) {
            insert(ITEMS, columnsToInsert, values);
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
        executeStatement(statementToExecute);
    }

    /**
     * Selects and returns one or more Items from the selected table.
     * If itemId is left blank, it returns a list of items. Otherwise, it
     * returns a single item.
     *
     * @param tableName the name of the table being selected from
     * @param selectedColumns the columns to be selected
     * @param itemId the provided itemId
     * @return arrayList of selected row in the selected table
     */
    public ArrayList<Item> selectFromTable(String tableName, String selectedColumns, String itemId) {
        ResultSet resultSet = getResultSet(tableName, selectedColumns, itemId);
        ArrayList<Item> items = new ArrayList<>();

        try {
            while (resultSet.next()) {
                if (tableName.equals(Database.DELETED_ITEMS)) {
                    DeletedItem item = new DeletedItem(resultSet);
                    items.add(item);
                } else {
                    Item item = new Item(resultSet);
                    items.add(item);
                }

            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (items.isEmpty()) {
            return null;
        }
        return items;
    }

    /**
     * Selects and returns an ArrayList of Items from the selected table.
     *
     * @param tableName the name of the table being selected from
     * @param selectedColumns the range of columns to be selected
     * @return arrayList of selected items in the selected table
     */
    public ArrayList<Item> selectFromTable(String tableName, String selectedColumns) {
        return selectFromTable(tableName, selectedColumns, "");
    }

    /**
     * Updates a single attribute of a single item from a table
     *
     * @param itemId the item being updated
     * @param columnValuePairs the name-value pairs used to update the Item
     */
    public void updateItem(String itemId, String columnValuePairs) {
        String statementToExecute = "UPDATE " + ITEMS + " SET " +
                columnValuePairs + " WHERE id = " + itemId;
        executeStatement(statementToExecute);
    }

    /**
     * Deletes items matching the provided ids from the selected table.
     *
     * @param tableName the table being selected
     * @param itemId the provided item ids
     */
    public void deleteFromTable(String tableName, String itemId) {
        String statementToExecute = "DELETE FROM " + tableName +
                " WHERE id = " + itemId;
        executeStatement(statementToExecute);
    }

    /**
     * Returns the number of rows in a table.
     *
     * @param tableName the name of the table
     * @return the number of rows in the table
     */
    public int getSizeOfTable(String tableName) {
        return selectFromTable(tableName, "*").size();
    }

    /**
     * Selects and returns a ResultSet of selected rows from a selected table.
     * If itemId is blank, it selects all rows in the table.
     *
     * @param tableName the name of the table
     * @param selectedColumns the columns to be selected
     * @param itemId if left blank, returns . Otherwise, returns a single item
     * @return resultSet containing one or more rows of a table
     */
    public ResultSet getResultSet(String tableName, String selectedColumns, String itemId) {
        String statementToExecute = "SELECT " + selectedColumns + " FROM " + tableName;
        if (!itemId.isBlank()) {
            statementToExecute += " WHERE id = " + itemId;
        }

        try {
            return statement.executeQuery(statementToExecute);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Executes an SQL statement on a table.
     *
     * @param sqlStatement the SQL statement to execute
     */
    public void executeStatement(String sqlStatement) {
        try {
            statement.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     */
    public void shutdown() {
        try {
            if (connection != null) {
                connection.close();
            }
            statement.close();
        } catch (SQLException e) {
            // connection close failed.
            throw new RuntimeException(e);
        }
    }
}
