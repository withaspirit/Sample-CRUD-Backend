package view;

import org.junit.jupiter.params.provider.EnumSource;
import view.Command;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * CommandTest ensures that Command's search method functions correctly.
 *
 * @author Liam Tripp
 */
public class CommandTest {

    @ParameterizedTest
    @EnumSource(Command.class)
    void testGetCommandValidInputs(Command command) {
            String commandName = command.getName();
            Command foundCommand = Command.getCommand(commandName);
            Assertions.assertNotNull(foundCommand);
            assertEquals(commandName, foundCommand.getName());
    }

    @Test
    void testGetCommandInvalidInputs() {
        String invalidCommandName = "invalidCommandName";
        Command invalidCommand = Command.getCommand(invalidCommandName);
        assertNull(invalidCommand);
    }
}
