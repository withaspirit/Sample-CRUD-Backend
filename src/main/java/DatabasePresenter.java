import java.util.ArrayList;

/**
 * DatabasePresenter allows the DatabaseCLI to interact with the model and
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
     * Inserts an item into the correct table.
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
        return database.selectFromTable(tableName, "*");
    }

    /**
     * Deletes an item from the items table.
     *
     * @param itemId the id of the table being deleted from
     */
    public void deleteItem(String itemId) {
        database.deleteFromTable(Database.ITEMS, itemId);
    }
}
