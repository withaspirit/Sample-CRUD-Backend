/**
 * Command contains the commands used for the DatabaseCLI.
 *
 * @author Liam Tripp
 */
public enum Command {
    CREATE,
    READ,
    UPDATE,
    DELETE,
    HELP,
    QUIT;

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
