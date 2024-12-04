/*
 * This is the main app code
 * 
 */
public class MainApp {
    public static void main(String[] args) {
    
        CmdArgument cmdArgs = new CmdArgument(args);
        
        if (cmdArgs.argumentIsNotCmd()) {
            System.exit(0);
        }
        
        Messages.txtWelcome("main");
        
       

    }

}

