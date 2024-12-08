/**
 * In this class, all the tools needed to format strings as needed.
 * 
 */
public class ToolsFormat {

    /**
     * This method will take a list of String from an fbk file and concatenate
     * the description elements
     * 
     * @param firstArg integer is the index of the first description element
     * @param input String list is the input list
     * @param sep String is the separator character
     * @return a String including all the description
     */
    public static String fbkConcatDescription(int firstArg, String[] input, String sep, String delim) {
        String output = "";

        if (input.length >= firstArg) {
            output = delim + input[firstArg - 1];
            for (int i = firstArg; i < input.length; i++) {
                output += sep + input[i];
            }
            // Add the " at the end of the string
            output += delim;
        }
        return output.strip();
    }
}
