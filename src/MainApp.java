/*
 * This is the main app code
 * 
 */
public class MainApp {
    public static void main(String[] args) {

        // Validate the command arguments
        int argsChoice = Validations.validateArgs(args);

        if (argsChoice == 1) {
            System.out.printf("PROGRAMME PARTI!!!!\n\n");
        }
        
    }

}

