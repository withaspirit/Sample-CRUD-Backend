package view;

import model.Database;

/**
 * Command contains the commands used for the DatabaseCLI.
 *
 * @author Liam Tripp
 */
public enum Command {
    CREATE("(CREATE) (\\w+) (\\d+\\.\\d+) (\\d+)"),
    READ("(READ) (" + String.join("|", Database.ITEMS, Database.DELETED_ITEMS) + ")"), // READ [tableName]
    UPDATE("(UPDATE) (\\d+) (name = '\\w+'|price = \\d+\\.\\d+|stock = \\d+)"),
    DELETE("(DELETE) (\\d+) ?((?:\\w+)\\W*)*"),
    RESTORE("(RESTORE) (\\d+)"),
    HELP("(help)"),
    QUIT("(quit)"),
    TABLES("(tables)");

    private String regex;

    /**
     * Constructor for Command.
     *
     * @param regex the regular expression for the
     */
    Command(String regex) {
        this.regex = regex;
    }

    /**
     * Returns the Regular Expression associated with the Command.
     *
     * @return the Regular Expression associated with the Command.
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Returns the Command as a String.
     *
     * @return a String with the Command's name as a String
     */
    public String getName() {
        return name().toLowerCase();
    }

    /**
     * Matches and returns a command given a String.
     *
     * @param commandName the name of the command to be retrieved
     * @return the Command if the commandName is valid, null otherwise
     */
    public static Command getCommand(String commandName) {
        try {
            return valueOf(commandName.trim().toUpperCase());
        } catch (IllegalArgumentException iae) {
            System.err.println("Command does not exist");
            return null;
        }
    }
}
