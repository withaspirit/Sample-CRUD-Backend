package model;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DeletedItem is an Item that has been deleted.
 *
 * @author Liam Tripp
 */
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

    public DeletedItem(ResultSet resultSet) {
        super(resultSet);
        final Field[] attributes = DeletedItem.class.getFields();
        // if deletedItem has comment
        int commentIndex = 5;
        if (attributes.length == commentIndex) {
            try {
                this.comment =   resultSet.getString(commentIndex);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // TODO: constructor from Matcher
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
     * Modifies the item's deletion comment.
     *
     * @param comment the new value for the Item's comment.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    public static String[] getAttributeNamesAsArray() {
        Field[] attributes = DeletedItem.class.getFields();
        String[] attributeNames = new String[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            attributeNames[i] = attributes[i].getName();
        }
        return attributeNames;
    }

    @Override
    public String[] getValuesAsArray() {
        return new String[] {
                String.valueOf(getId()),
                getName(),
                getPrice().toString(),
                String.valueOf(getStock()),
                getComment()
        };
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof DeletedItem item)) {
            return false;
        }

        return getId() == item.getId() &&
                getName().equals(item.getName()) &&
                getPrice().compareTo(item.getPrice()) == 0 &&
                getStock() == item.getStock() &&
                getComment().equals(item.getComment());
    }

    @Override
    public String toString() {
        return String.join(", ", super.toString(), getComment());
    }
}
