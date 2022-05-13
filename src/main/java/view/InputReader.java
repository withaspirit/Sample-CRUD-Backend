package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InputReader provides methods to read in user inputs.
 *
 * @author Liam Tripp
 */
public class InputReader {

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
