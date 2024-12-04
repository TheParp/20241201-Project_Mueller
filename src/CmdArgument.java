/*
 * 
 * 
 * 
 */
class CmdArgument {
    
    int argNum;
    String[] arguments;
    String[] GUI_ARG = {"gui"};
    String[] knownCommands = {"-c", "-?", "gui"};
    
    String OPTION_CMD_LIST = "-?";

    public CmdArgument(String[] inputArgs) {
        this.argNum = inputArgs.length;
        this.arguments = inputArgs.clone();

        // Initialize the first string of arguments array to gui since empty
        if (argNum == 0) {
            this.arguments = GUI_ARG.clone();
        }
    }

    public boolean isNotValidCmd() {
        // Search through the known commands to see if input valid
        for (int i = 0; i < knownCommands.length; i++) {
            if (knownCommands[i].equals(this.arguments[0])){
                return false;
            }
        }
        return true;
    }

    public boolean isAskingGui() {
        if (argNum == 0 || (argNum == 1 && arguments[0].equals(GUI_ARG[0]))) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isAskingCmdList() {
        if (arguments[0].equals(OPTION_CMD_LIST)) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean argumentIsNotCmd() {
        if (this.isAskingGui()) {
            messageUsage("gui");
            return true;
        }
        if (this.isNotValidCmd()) {
            messageUsage("main");
            return true;
        }
        if (this.isAskingCmdList()) {
            messageUsage("list");
            return true;
        }
        return false;
    }

    public static void messageUsage(String in) {
        String MESS_GUI_CONSTRUCTION = "\n" +
                                       "---------------------------------------------------------\n" +
                                       "-----------------GUI UNDER CONSTRUCTION!-----------------\n" +
                                       "---------------------------------------------------------\n" +
                                       "\n";

        String MESS_MAIN = "\n" +
                            "---------------------------------------------------------\n" +
                            "---------------------INVALID ARGUMENT--------------------\n" +
                            "---------------------------------------------------------\n" +
                            "\n" +
                            "How to use :\n" +
                            ".../java MainApp -(command) [FILENAME] [optional-FILENAME]\n" +
                            "...use command -? to see a command list\n" +
                            "\n";

        String MESS_CMD_LIST = "\n" +
                                "---------------------------------------------------------\n" +
                                "-----------------------COMMAND LIST----------------------\n" +
                                "---------------------------------------------------------\n" +
                                "\n" +
                                "gui __ GUI app\n" +
                                "-c __ Option 1\n" +
                                "\n";

        if (in == "gui") {
            System.out.printf(MESS_GUI_CONSTRUCTION);
        }
        else if (in == "main") {
            System.out.printf(MESS_MAIN);
        }
        else if (in == "list") {
            System.out.printf(MESS_CMD_LIST);
        }

    }
}