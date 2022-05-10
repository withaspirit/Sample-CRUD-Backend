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
}
