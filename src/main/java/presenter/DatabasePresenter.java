package presenter;

import model.Database;
import model.DeletedItem;
import model.Item;

import java.util.ArrayList;
import java.util.regex.Matcher;

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
        database.insert(Database.ITEMS, Item.getAttributeNamesExceptId(),
                item.getAttributeValuesExceptId());
    }

    /**
     * Returns a list of all items from the selected table.
     */
    public ArrayList<Item> readFromTable(String tableName) {
        if (!tableName.equals(Database.ITEMS) && !(tableName.equals(Database.DELETED_ITEMS))) {
            return null;
        }
        return database.selectFromTable(tableName, "*");
    }

    /**
     * Updates an Item in the items table.
     *
     * @param matcher the matcher containing the itemId and column/ValuePairs for the item
     */
    public void updateItem(Matcher matcher) {
        String itemId = matcher.group(2);
        String columnValuePair = matcher.group(3);
        database.updateItem(itemId, columnValuePair);
    }

    /**
     * Deletes an item from the Items table and inserts it into the
     * Deleted_Items table.
     *
     * @param itemId the id of the item to be deleted
     * @param comment (optional) the user's comment for the item's deletion
     */
    public void deleteItem(String itemId, String comment) {
        ArrayList<Item> items = database.selectFromTable(Database.ITEMS, "*", itemId);
        if (items.isEmpty()) {
            return;
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
        database.deleteFromTable(Database.ITEMS, itemId);
        database.insert(Database.DELETED_ITEMS, columns, values);
    }

    /**
     * Restores an item from the table of deleted_items.
     *
     * @param itemId the id of the item being restored
     * @return the item that was restored
     */
    public Item restoreItem(String itemId) {
        ArrayList<Item> items = database.selectFromTable(Database.DELETED_ITEMS, "*", itemId);
        if (items.isEmpty()) {
            return null;
        }
        Item item = items.get(0);

        database.deleteFromTable(Database.DELETED_ITEMS, itemId);
        String values = item.getValuesInSQLFormat(); // exclude comment
        String columns = String.join(", ", Item.getAttributeNamesAsArray());
        database.insert(Database.ITEMS, columns, values);
        return item;
    }

    /**
     * Shuts down the Database.
     */
    public void terminateDatabase() {
        database.shutdown();
    }
}
