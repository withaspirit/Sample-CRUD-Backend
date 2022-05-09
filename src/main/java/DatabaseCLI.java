import java.util.Scanner;

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
                        System.out.println();
                        break;
                    case READ:
                        break;
                    case UPDATE:
                        break;
                    case DELETE:
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
