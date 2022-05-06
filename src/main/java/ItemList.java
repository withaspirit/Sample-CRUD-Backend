import java.util.HashMap;

public class ItemList {

    private HashMap<Integer, Item> itemList;

    public ItemList() {
        this.itemList = new HashMap<>();
    }

    public Item getItem(int id) {
        return itemList.get(id);
    }

    public void addItem(Item item) {
        itemList.put(item.getId(), item);
    }

    public void removeItem(int id) {
        itemList.remove(id);
    }
}
