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
    public void addModel(Database database) {
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
        if (!tableName.equals(Database.ITEMS)) {
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
        database.updateItems(itemId, columnValuePair);
    }

    /**
     * Deletes an item from the items table.
     *
     * @param itemId the id of the table being deleted from
     */
    public void deleteItem(String itemId) {
        database.deleteFromTable(Database.ITEMS, itemId);
    }

    /**
     * Restores an item from the table of deleted_items.
     *
     * @param itemId the id of the item being restored
     * @return the item that was restored
     */
    public Item restore(String itemId) {
        DeletedItem item = (DeletedItem) database.selectFromTable(Database.DELETED_ITEMS, "*", itemId);
        database.deleteFromTable(Database.DELETED_ITEMS, itemId);
        String values = String.join(", ", ((Item) item).getValuesAsArray()); // exclude comment
        String columns = String.join(", ", Item.getAttributeNamesAsArray());
        database.insert(Database.ITEMS, columns, values);
        return item;
    }

    /**
     * Shuts down the Database.
     */
    public void closeModel() {
        database.closeDatabase();
    }
}
