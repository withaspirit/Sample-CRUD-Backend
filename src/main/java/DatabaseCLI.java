import java.util.Locale;
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

    private boolean userWantsToQuit;

    /**
     * Constructor for Database CLI.
     */
    public DatabaseCLI() {
        userWantsToQuit = false;
    }

    public void start() {
        String introduction = "Welcome to Liam Tripp's Backend CRUD Sample.\n";
        introduction += "Here is a list of commands you may choose from:\n";
        System.out.println(introduction + help());
    }

    public void loop() {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        do {
            String consoleOutput = "";
            System.out.print("Enter command: ");
            String initialInput = scanner.nextLine().toLowerCase();

            Matcher matcher = inputMatchesFormat(initialInput);
            if (matcher == null) {
                System.out.println("Input is invalid. Enter 'HELP' for options.");
                continue;
            }

            String[] commandAndSQLInput = separateCommandAndSQLInput(matcher);
            if (commandAndSQLInput == null) {
                System.out.println("Error: matcher.matches() passed, but matcher.find() failed.");
                continue;
            }

            String command = commandAndSQLInput[0];
            String sqlInput = commandAndSQLInput[1];
            consoleOutput = executeCommand(command, sqlInput);

            System.out.println(consoleOutput);
            System.out.println();
        } while (userWantsToQuit == false);
    }

    /**
     * Given a command, execute that command's correpsonding SQL statement
     * with the given inputs.
     *
     * @param commandAsString the command to be executed as a String
     * @param sqlInput the inputs for the SQL statement to be executed
     * @return a statement indicating the operation and its level of success
     */
    String executeCommand(String commandAsString, String sqlInput) {
        Command command = Command.getCommand(commandAsString);
        if (command == null) {
            return "Please enter a valid command. " + "Enter ' " +
                    Command.HELP.getName() + "' for a list of them.";
        }

        Matcher matcher = getMatcher(command.getRegex(), sqlInput);
        String matcherError = checkMatcherError(matcher);
        if (!matcherError.equals("")) {
            return "Error: " + matcherError;
        }

        String consoleOutput = "";
        switch (command) {
            case CREATE -> consoleOutput = createItem(sqlInput);
            case READ -> consoleOutput = read(sqlInput);
            case UPDATE -> consoleOutput = updateItem(sqlInput);
            case DELETE -> consoleOutput = delete(sqlInput);
            case HELP -> consoleOutput = help();
            case QUIT -> consoleOutput = quit();
            default -> consoleOutput = "ERROR: unhandled command.";
        }
        return consoleOutput;
    }

    String[] separateCommandAndSQLInput(Matcher matcher) {
        String[] commandAndSQL = new String[2];
        if (!matcher.find()) {
            return null;
        } else {
            commandAndSQL[0] = matcher.group(1);
            commandAndSQL[1] = matcher.group(2);
            return commandAndSQL;
        }
    }

    Matcher inputMatchesFormat(String initialInput) {
        String wordSpaceAnythingRegex = "(\\w+) (.+)";
        Matcher matcher = getMatcher(wordSpaceAnythingRegex, initialInput);

        if (!matcher.matches()) {
            System.out.println("");
            return null;
        } else {
            matcher.reset();
            return matcher;
        }
    }

    /**
     * Updates an Item and returns a String indicating the level of success.
     *
     * @param sqlInput the user's SQL input to the program
     * @return a String indicating the completion success
     */
    public String updateItem(String sqlInput) {
        String itemAttributesPipeSeparated =
                String.join("|", Item.getAttributeNamesAsArray());
        String updateRegex = "(\\d+) (" + itemAttributesPipeSeparated + ") = (\\w+|\\d+\\.\\d+|\\d+)";
        Matcher matcher = getMatcher(updateRegex, sqlInput);
        return "";
    }

    /**
     * Creates an Item and returns a String indicating the level of success.
     *
     * @param sqlInput the user's SQL input to the program
     * @return a String indicating the completion success
     */
    public String createItem(String sqlInput) {
        String createPrompt = "Enter values for: (" + Item.getAttributeNamesExceptId() + ") for the new Item: ";
        System.out.print(createPrompt);

        // name price[dollar.cent] stock

        Matcher createMatcher = getMatcher(Command.CREATE.getRegex(), sqlInput);
        if (createMatcher.matches() == false) {
            return "Invalid input.";
        }
        createMatcher.reset();

        Item item;
        if (createMatcher.find()) {
            item = new Item(createMatcher);
            System.out.println(item.getAttributeValuesExceptId());

        } else {
            return "ERROR: Input matches creation regex but could not be found.";
        }
        return "FIXME: Successfully created item: " + item.getAttributeValuesExceptId();
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
    public String checkMatcherError(Matcher matcher) {
        if (!matcher.matches()) {
            return "Bad input formatting.";
        }
        matcher.reset();
        if (!matcher.find()) {
            return "Matcher.find() fail.";
        }
        return "";
    }

    private String read(String sqlInput) {
        String readStatement = "";
        if (sqlInput.equals(Database.ITEMS)) {
            // TODO: read from database;
        } else {
            return "InvalidInput";
        }
        return "";
    }

    private String delete(String sqlInput) {
        return "";
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
        stringBuilder.append("READ - view all the rows in a selected table\n");
        stringBuilder.append("UPDATE - update a row in").append(itemsEnding);
        stringBuilder.append("DELETE - delete a row in").append(itemsEnding);
        stringBuilder.append("HELP - view the list of valid commands\n");
        stringBuilder.append("QUIT - exit the command-line interface\n");
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
        return "Exiting program.";
    }
}
