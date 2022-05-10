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
            String consoleOutput;
            System.out.print("Enter command: ");
            String initialInput = scanner.nextLine().toLowerCase();

            // check command is in a valid format
            String wordSpaceAnythingRegex = "(\\w+) (.+)";
            Matcher matcher = getMatcher(wordSpaceAnythingRegex, initialInput);
            String matcherError = getMatcherError(matcher);
            if (!matcherError.equals("")) {
                System.out.println(matcherError);
                continue;
            }

            // check command is valid
            String commandAsString = matcher.group(1);
            Command command = Command.getCommand(commandAsString);
            if (command == null) {
                String errorMessage = "Please enter a valid command. " + "Enter ' " +
                        Command.HELP.getName() + "' for a list of them.";
                System.out.println(errorMessage);
                continue;
            }
            // check if input matches the regex of its command
            consoleOutput = executeCommand(command, initialInput);
            System.out.println(consoleOutput);
            System.out.println();
        } while (userWantsToQuit == false);
    }

    /**
     * Given a command, execute that command's corresponding SQL statement
     * with the given inputs.
     *
     * @param command the command to be executed
     * @param initialInput the user's input
     * @return a statement indicating the operation and its level of success
     */
    String executeCommand(Command command, String initialInput) {
        // check that initial input matches command regex
        // TODO?: could have iterated over the loop Command.regex instead
        Matcher matcher = getMatcher(command.getRegex(), initialInput);
        String matcherError = getMatcherError(matcher);
        if (!matcherError.equals("")) {
            return "Error: " + matcherError;
        }

        String consoleOutput;
        switch (command) {
            case CREATE -> consoleOutput = createItem(matcher);
            case READ -> consoleOutput = read(matcher);
            case UPDATE -> consoleOutput = updateItem(matcher);
            case DELETE -> consoleOutput = delete(matcher);
            case HELP -> consoleOutput = help();
            case QUIT -> consoleOutput = quit();
            default -> consoleOutput = "ERROR: unhandled command.";
        }
        return consoleOutput;
    }

    /**
     * Creates an Item and returns a String indicating the level of success.
     *
     * @param matcher the matcher containing the user's command
     * @return a String indicating the completion success
     */
    public String createItem(Matcher matcher) {
        Item item;
        item = new Item(matcher);
        System.out.println(item.getAttributeValuesExceptId());
        return "FIXME: Successfully created item: " + item.getAttributeValuesExceptId();
    }

    public String read(Matcher matcher) {
        String readStatement = "";
        return "";
    }

    /**
     * Updates an Item and returns a String indicating the level of success.
     *
     * @param matcher the matcher containing the user's command
     * @return a String indicating the completion success
     */
    public String updateItem(Matcher matcher) {
        return "";
    }

    public String delete(Matcher matcher) {
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
    public String getMatcherError(Matcher matcher) {
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
