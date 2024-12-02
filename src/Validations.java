/*
 * 
 * 
 */
public class Validations{

    /*
     * This method validate the args input from command line and validate it.
     * It will return a value that will enable different modes in mainapp. If
     * the command line is not found in the program, messages are sent and 
     * program is closed from here.
     * 
     */
    public static int validateArgs(String[] input) {

        if(input.length == 0) {
            Messages.txtUsage("gui");

            return 0;
        }
        else if (input.length == 1) {

            if(input[0].length() > 2) {
                Messages.txtUsage("usageGen");
    
                return 0;
            }

            
        }
        
        return 1;
    }
}