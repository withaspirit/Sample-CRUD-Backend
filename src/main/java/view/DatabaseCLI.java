package view;

import model.Database;
import model.DeletedItem;
import model.Item;
import presenter.DatabasePresenter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DatabaseCLI is a command-line interface that allows users to interact with
 * the Database.
 *
 * @author Liam Tripp
 */
public class DatabaseCLI {

    /** DatabaseCLI interacts with the model through databasePresenter */
    private DatabasePresenter databasePresenter;
    private boolean userWantsToQuit;

    /**
     * Constructor for Database CLI.
     */
    public DatabaseCLI() {
        userWantsToQuit = false;
    }

    /**
     * Adds a databasePresenter to the DatabaseCLI (View).
     *
     * @param databasePresenter the database presenter through which the
     */
    public void addPresenter(DatabasePresenter databasePresenter) {
        this.databasePresenter = databasePresenter;
    }

    public void start() {
        String introduction = "Welcome to Liam Tripp's Backend CRUD Sample.\n\n";
        introduction += "Here is a list of commands you may choose from:\n\n";
        System.out.println(introduction + help() + "\n");
        System.out.println("The following tables are part of the program:");
        System.out.println(tables() + "\n");
    }

    /**
     * Asks user for input and delegates to methods, returning output statement.
     */
    public void loopConsole() {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        while (userWantsToQuit == false) {
            System.out.print("Enter command: ");
            String initialInput = scanner.nextLine().toLowerCase().trim();
            String consoleOutput = processInput(initialInput);
            System.out.println(consoleOutput);
            System.out.println();
        }
    }

    /**
     * Processes an input, matching and executing it.
     *
     * @param userInput the user's input
     * @return output message if input is valid, error message otherwise
     */
    public String processInput(String userInput) {
        Matcher matcher = matchInput(userInput);
        String matcherError = validateMatcher(matcher);
        if (!matcherError.equals("")) {
            return matcherError + "\nError text: " + userInput;
        }
        String consoleOutput = executeInput(matcher);
        return consoleOutput;
    }

    /**
     * Executes a Command given its associated SQL information.
     *
     * @param commandMatcher contains the Command and the user's input
     * @return a statement indicating the operation and its level of success
     */
    String executeInput(Matcher commandMatcher) {
        // check that initial input matches command regex
        String commandAsString = commandMatcher.group(1);
        Command command = Command.getCommand(commandAsString);
        if (command == null) {
            String errorMessage = "Please enter a valid command. " + "Enter ' " +
                    Command.HELP.getName() + "' for a list of them.";
            return errorMessage;
        }
        String consoleOutput;
        switch (command) {
            case CREATE -> consoleOutput = createItem(commandMatcher);
            case READ -> consoleOutput = read(commandMatcher);
            case UPDATE -> consoleOutput = updateItem(commandMatcher);
            case DELETE -> consoleOutput = delete(commandMatcher);
            case RESTORE -> consoleOutput = restore(commandMatcher);
            case HELP -> consoleOutput = help();
            case QUIT -> consoleOutput = quit();
            default -> consoleOutput = "ERROR: unhandled command."; // shouldn't be seen in normal program execution
        }
        return consoleOutput;
    }

    /**
     * Creates an Item and returns a String containing information about that item.
     *
     * @param matcher the matcher containing the user's command
     * @return a String containing information about the completed item
     */
    public String createItem(Matcher matcher) {
        Item item;
        item = new Item(matcher);
        databasePresenter.createItem(item);
        return "FIXME: Successfully created item: " + item.getAttributeValuesExceptId();
    }

    /**
     * Returns the contents of the specified table as a String.
     *
     * @param matcher contains the READ command and tableName to be read
     * @return a String containing the contents of the table
     */
    public String read(Matcher matcher) {
        // matcher.group(1) is "READ", group(2) is tableName
        String tableName = matcher.group(2);
        ArrayList<Item> items = databasePresenter.readFromTable(tableName);

        if (items.isEmpty()) {
            return tableName + " is empty.";
        }
        String[] attributeNames;
        if (tableName.equals(Database.ITEMS)) {
            attributeNames = Item.getAttributeNamesAsArray();
        } else {
            attributeNames = DeletedItem.getAttributeNamesAsArray();
        }
        String bar = " | ";
        String attributeNamesBarSeparated = String.join(bar, attributeNames);

        StringBuilder consoleOutput = new StringBuilder();
        consoleOutput.append(attributeNamesBarSeparated).append("\n");
        for (Item item : items) {
            String[] values = item.getValuesAsArray();
            String valuesBarSeparated = String.join(bar, values);
            consoleOutput.append(valuesBarSeparated).append("\n");
        }
        return consoleOutput.toString();
    }

    /**
     * Updates an Item and returns a String indicating the level of success.
     *
     * @param matcher the matcher containing the user's command
     * @return a String indicating the completion success
     */
    public String updateItem(Matcher matcher) {
        databasePresenter.updateItem(matcher);
        return "FIXME: update";
    }

    /**
     * Deletes an Item from a specified table.
     *
     * @param matcher contains the user's command and table to be deleted
     * @return a String indicating the deletion of the Item
     */
    public String delete(Matcher matcher) {
        // matcher.group(1) is "delete"
        String itemId = matcher.group(2);
        String comment = "";
        if (matcher.groupCount() > 2) {
            comment = Objects.requireNonNullElse(matcher.group(3), "");
        }
        databasePresenter.deleteItem(itemId, comment);
        return "FIXME: delete";
    }

    /**
     * Restores a DeletedItem to its corresponding table.
     *
     * @param matcher contains the user's command and itemId of the item to restore
     * @return a string indicating the completion of restoring the item
     */
    public String restore(Matcher matcher) {
        String itemId = matcher.group(2);
        Item restoredItem = databasePresenter.restoreItem(itemId);

        if (restoredItem == null) {
            return "Item does not exist.";
        }
        return "Restored item: " + restoredItem;
    }

    /**
     * Returns a list of valid user commands.
     *
     * @return a list of valid user commands
     */
    public String help() {
        StringBuilder stringBuilder = new StringBuilder();
        String itemsEnding = " the table '" + Database.ITEMS + "'\n";

        stringBuilder.append("CREATE - insert a row into").append(itemsEnding);
        stringBuilder.append("READ - view the entries from one of the following tables: ")
                .append(tables()).append("\n");
        stringBuilder.append("UPDATE - update a row in").append(itemsEnding);
        stringBuilder.append("DELETE - delete a row in").append(itemsEnding);
        stringBuilder.append("`RESTORE [id]` - restores an item with the provided id to its corresponding table.\n");
        stringBuilder.append("HELP - view the list of valid commands\n");
        stringBuilder.append("QUIT - exit the command-line interface");
        return stringBuilder.toString();
    }

    /**
     * Prepares the program to terminate and returns a string indicating the
     * end of the program.
     *
     * @return a string indicating the end of the program
     */
    public String quit() {
        userWantsToQuit = true;
        databasePresenter.closeModel();
        return "Exiting program.";
    }

    /**
     * Returns a list of the tables in the Database.
     *
     * @return a list of the tables in the Database as a String
     */
    public String tables() {
        return String.join(", ", Database.ITEMS, Database.DELETED_ITEMS);
    }

    /**
     * Matches an input to one of the Command's Regexes.
     *
     * @param userInput the user's input
     * @return a matcher matching the user's input, null otherwise
     */
    public Matcher matchInput(String userInput) {
        Matcher matcher = null;
        for (Command command : Command.values()) {
            matcher = getMatcher(command.getRegex(), userInput);
            if (matcher.matches()) {
                matcher.reset();
                return matcher;
            }
        }
        return matcher;
    }

    /**
     * Returns a Matcher matching input to a given Regular Expression.
     *
     * @param regex a Regular Expression to capture
     * @param userInput the input to the matcher
     * @return the matcher after matching the userInput to the regex
     */
    public Matcher getMatcher(String regex, String userInput) {
        Pattern createPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return createPattern.matcher(userInput);
    }

    /**
     * Returns a String indicating whether the Matcher has an error.
     * Also prepares the matcher to be used.
     *
     * @param matcher the matcher being examined
     * @return a String with an error statement if there is an error, "" otherwise
     */
    public String validateMatcher(Matcher matcher) {
        if (!matcher.matches()) {
            return "Bad input formatting. Enter '" + Command.HELP.getName() +
                    "' for options.";
        }
        matcher.reset();
        if (!matcher.find()) {
            return "Matcher.find() fail.";
        }
        return "";
    }
}
