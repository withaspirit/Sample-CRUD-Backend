package model;

import java.math.BigDecimal;

public class DeletedItem extends Item {

    /** the deletion comment for the item (optional) */
    private String comment;

    /**
     * Constructor for DeletedItem.
     *
     * @param item the item being deleted
     */
    public DeletedItem(Item item) {
        super(item.getId(), item.getName(), item.getPrice().toString(),
                item.getStock());
    }

    /**
     * Constructor for DeletedItem.
     *
     * @param item the item being deleted
     * @param comment the deletion comment for the item
     */
    public DeletedItem(Item item, String comment) {
        super(item.getId(), item.getName(), item.getPrice().toString(),
                item.getStock());
        this.comment = comment;
    }

    public DeletedItem(int id, String name, String price, int stock, String comment) {
        super(id, name, price, stock);
        this.comment = comment;
    }

    /**
     * Returns the item's deletion comment.
     *
     * @return the item's deletion comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Modifies the item's deletion comment
     *
     * @param comment the new value for the Item's comment.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}
