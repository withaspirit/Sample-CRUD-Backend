package presenter;

import model.Database;
import model.DeletedItem;
import model.Item;
import model.Table;

import java.util.List;

/**
 * DatabasePresenter allows the DatabaseCLI to interact with the Database and
 * receive a response.
 *
 * @author Liam Tripp
 */
public class DatabasePresenter {

    private Database database;

    /**
     * Constructor for DatabasePresenter.
     */
    public DatabasePresenter() {
        database = null;
    }

    /**
     * Initializes the database for the DatabasePresenter.
     *
     * @param database the database to be manipulated
     */
    public void addDatabase(Database database) {
        this.database = database;
    }

    /**
     * Inserts an item into the items table.
     *
     * @param item the item being inserted into the table
     */
    public void createItem(Item item) {
        database.insert(Table.ITEMS.getName(), Item.getAttributeNamesExceptId(),
                item.getValuesInSQLFormatExceptId());
    }

    /**
     * Returns a list of all items from the selected table.
     */
    public List<Item> readFromTable(String tableName) {
        if (!tableName.equals(Table.ITEMS.getName()) &&
                !(tableName.equals(Table.DELETED_ITEMS.getName()))) {
            return null;
        }
        return database.selectFromTable(tableName, "*");
    }

    /**
     * Updates an Item in the items table.
     *
     * @param itemId the id of the item
     * @param columnValuePair the name-value pair of the attribute to be updated
     */
    public Item updateItem(String itemId, String columnValuePair) {
        Item updatedItem = database.updateItem(itemId, columnValuePair);
        return updatedItem;
    }

    /**
     * Deletes an item from the Items table and inserts it into the
     * Deleted_Items table.
     *
     * @param itemId the id of the item to be deleted
     * @param comment (optional) the user's comment for the item's deletion
     */
    public Item deleteItem(String itemId, String comment) {
        List<Item> items = database.selectFromTable(Table.ITEMS.getName(), "*", itemId);
        if (items.isEmpty()) {
            return null;
        }

        Item item = items.get(0);
        String columns;
        String values = item.getValuesInSQLFormat();
        if (!comment.isBlank()) {
            item = new DeletedItem(item, comment);
            columns = String.join(", ", DeletedItem.getAttributeNamesAsArray());
            values = ((DeletedItem) item).getDeletedItemValuesInSQLFormat();
        } else {
            columns = String.join(", ", Item.getAttributeNamesAsArray());
        }

        database.deleteFromTable(Table.ITEMS.getName(), itemId);
        database.insert(Table.DELETED_ITEMS.getName(), columns, values);
        return item;
    }

    /**
     * Restores an item from the table of deleted_items.
     *
     * @param itemId the id of the item being restored
     * @return the item that was restored
     */
    public Item restoreItem(String itemId) {
        List<Item> items = database.selectFromTable(Table.DELETED_ITEMS.getName(), "*", itemId);
        if (items.isEmpty()) {
            return null;
        }
        database.deleteFromTable(Table.DELETED_ITEMS.getName(), itemId);

        Item item = items.get(0);
        String values = item.getValuesInSQLFormat(); // exclude comment
        String columns = String.join(", ", Item.getAttributeNamesAsArray());
        database.insert(Table.ITEMS.getName(), columns, values);
        return item;
    }

    /**
     * Shuts down the Database.
     */
    public void terminateDatabase() {
        database.shutdown();
    }
}
