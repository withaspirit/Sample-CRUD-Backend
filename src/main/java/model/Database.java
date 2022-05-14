package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database contains the CRUD functionality for the SQLite database.
 * (CRUD = create, read, update, destroy)
 *
 * @author Liam Tripp
 */
public class Database {

    private final Connection connection;
    private final Statement statement;
    private final static String CLASS_LOADER_NAME = "org.sqlite.JDBC";
    private final static String DATABASE_NAME = "jdbc:sqlite:warehouse.db";

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

    /**
     * Creates the database.
     */
    public void initializeDatabase() {
        InputFileReader inputFileReader = new InputFileReader("DDL", "sql");
        String sqlTableCreateStatement = inputFileReader.getSQLFileAsString();
        executeStatement(sqlTableCreateStatement);
    }

    /**
     * Adds values from items.json to the ITEMS table.
     */
    public void populateDatabase() {
        InputFileReader inputFileReader = new InputFileReader(Table.ITEMS.getName(), "json");
        List<Item> itemsFromJSONFile = inputFileReader.getItemsFromJSONFile();

        String columnsToInsert = Item.getAttributeNamesExceptId();
        for (Item item : itemsFromJSONFile) {
            String valuesToInsert = item.getValuesInSQLFormatExceptId();
            insert(Table.ITEMS.getName(), columnsToInsert, valuesToInsert);
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
     * @return list of selected rows in the selected table
     */
    public List<Item> selectFromTable(String tableName, String selectedColumns, String itemId) {
        ResultSet resultSet = getResultSet(tableName, selectedColumns, itemId);
        List<Item> items = new ArrayList<>();

        try {
            while (resultSet.next()) {
                if (tableName.equals(Table.DELETED_ITEMS.getName())) {
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
        return items;
    }

    /**
     * Selects and returns a list of Items from the selected table.
     *
     * @param tableName the name of the table being selected from
     * @param selectedColumns the range of columns to be selected
     * @return list of selected items in the selected table
     */
    public List<Item> selectFromTable(String tableName, String selectedColumns) {
        return selectFromTable(tableName, selectedColumns, "");
    }

    /**
     * Updates a single attribute of a single item from a table
     *
     * @param itemId the item being updated
     * @param columnValuePairs the name-value pairs used to update the Item
     */
    public Item updateItem(String itemId, String columnValuePairs) {
        String statementToExecute = "UPDATE " + Table.ITEMS.getName() + " SET " +
                columnValuePairs + " WHERE id = " + itemId;
        // check that item exists
        if (selectFromTable(Table.ITEMS.getName(), "*", itemId).isEmpty()) {
            return null;
        }
        executeStatement(statementToExecute);
        return selectFromTable(Table.ITEMS.getName(), "*", itemId).get(0);
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
     * Terminates the database's connection and statement.
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
