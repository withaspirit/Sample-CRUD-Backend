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

    /**
     * Constructor for Database CLI.
     */
    public DatabaseCLI() {

    }

    public void start() {
        String introduction = "Welcome to Liam Tripp's Backend CRUD Sample.\n";
        introduction += "Here is a list of commands you may choose from:\n";
        System.out.println(introduction + help());
    }

    public void loop() {
        boolean userWantsToQuit = false;
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        do {
            String consoleOutput = "";
            System.out.print("Enter command: ");
            String userInput = scanner.nextLine().toLowerCase();
            Command command = Command.getCommand(userInput);

            if (command == null) {
                consoleOutput = "Please enter a valid command. " +
                        "Enter ' " + Command.HELP.getName() + "' for a list of them.";
            } else {
                switch (command) {
                    case QUIT:
                        userWantsToQuit = true;
                        consoleOutput = "Exiting program.";
                        break;
                    case HELP:
                        help();
                        break;
                    case CREATE:
                        consoleOutput = createItem(userInput);
                        break;
                    case READ:
                        consoleOutput = read();
                        break;
                    case UPDATE:
                        consoleOutput = updateItem(userInput);
                        break;
                    case DELETE:
                        consoleOutput = delete();
                        break;
                    default:
                        break;
                }
            }
            System.out.println(consoleOutput);
            System.out.println();
        } while (userWantsToQuit == false);
    }

    /**
     * Updates an Item and returns a String indicating the level of success.
     *
     * @param userInput the user's input to the program
     * @return a String indicating the completion success
     */
    public String updateItem(String userInput) {
        return "";
    }

    /**
     * Creates an Item and returns a String indicating the level of success.
     *
     * @param userInput the user's input to the program
     * @return a String indicating the completion success
     */
    public String createItem(String userInput) {
        String createPrompt = "Enter values for: (" + Item.getAttributeNamesExceptId() + ") for the new Item: ";
        System.out.print(createPrompt);

        // name price[dollar.cent] stock
        String createRegex = "(\\w+) (\\d+\\.\\d+) (\\d+)";
        Matcher createMatcher = getMatcher(createRegex, userInput);
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

    private String read() {
        return "";
    }

    private String delete() {
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
}
