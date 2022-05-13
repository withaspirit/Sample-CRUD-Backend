package model;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * DeletedItem is an Item that has been deleted.
 *
 * @author Liam Tripp
 */
public class DeletedItem extends Item {

    /** the deletion comment for the item (optional) */
    private final String comment;

    /**
     * Constructor for DeletedItem.
     *
     * @param item the item being deleted
     */
    public DeletedItem(Item item) {
        super(item.getId(), item.getName(), item.getPrice().toString(),
                item.getStock());
        comment = "";
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
        int numberOfItemAttributes = Item.class.getDeclaredFields().length;
        int numberOfDeletedItemAttributes = DeletedItem.class.getDeclaredFields().length;
        int totalNumberOfAttributes = numberOfItemAttributes + numberOfDeletedItemAttributes;

        try {
            String comment = resultSet.getString(totalNumberOfAttributes);
            // prevent DeletedItem from initializing null value
            this.comment = Objects.requireNonNullElse(comment, "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the DeletedItem's properties in SQL format. Identical to
     * Item.getValuesInSQLFormat except it includes the field for comments.
     *
     * @return the DeletedItem's values a format made suitable for SQL
     */
    public String getDeletedItemValuesInSQLFormat() {
        String deletedItemValues = super.getValuesInSQLFormat();
        if (comment != null && !comment.isBlank()) {
            deletedItemValues = String.join(", ",
                    deletedItemValues, getCommentInQuotes());
        }
        return deletedItemValues;
    }

    /**
     * Returns the item's deletion comment.
     *
     * @return the item's deletion comment
     */
    public String getComment() {
        return comment;
    }

    public String getCommentInQuotes() {
        return "'" + comment + "'";
    }

    public static String[] getAttributeNamesAsArray() {
        Field[] attributes = DeletedItem.class.getDeclaredFields();
        String[] attributeNames = new String[attributes.length];

        for (int i = 0; i < attributes.length; i++) {
            attributeNames[i] = attributes[i].getName();
        }
        String[] itemAttributeNames = Item.getAttributeNamesAsArray();
        return ArrayUtils.addAll(itemAttributeNames, attributeNames);
    }

    @Override
    public String[] getValuesAsArray() {
        if (!comment.isBlank()) {
            return ArrayUtils.addAll(super.getValuesAsArray(), comment);
        } else {
            return super.getValuesAsArray();
        }
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
        String deletedItemValues = super.toString();
        if (comment != null && !comment.isBlank()) {
            deletedItemValues = String.join(", ",
                    deletedItemValues, getCommentInQuotes());
        }
        return deletedItemValues;
    }
}
