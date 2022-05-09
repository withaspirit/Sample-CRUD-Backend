import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * CommandTest ensures that Command's search method functions correctly.
 *
 * @author Liam Tripp
 */
public class CommandTest {

    @Test
    void testGetCommandValidInputs() {
        for (Command command : Command.values()) {
            String commandName = command.getName();
            Command foundCommand = Command.getCommand(commandName);
            Assertions.assertNotNull(foundCommand);
            assertEquals(commandName, foundCommand.getName());
        }
    }

    @Test
    void testGetCommandInvalidInputs() {
        String invalidCommandName = "invalidCommandName";
        Command invalidCommand = Command.getCommand(invalidCommandName);
        assertNull(invalidCommand);
    }
}