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


        switch (cmdArgs.getCommand()) {
            case "-c" :
                // Name file provided ?
                if (cmdArgs.argNum < 2) {
                    System.out.printf("Input file name not provided");
                    System.exit(2);
                }

                String outputPath;

                // Try the output file path
                try {
                    outputPath = cmdArgs.arguments[2];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    // Gives default file name
                    outputPath = "noname.txt";
                }

                Fbk test = new Fbk(FileIo.importFile(cmdArgs.arguments[1]));
                FileIo.exportFile(test.getFbk(), outputPath);

            
                // set file lines in a string list
                // verify the fbk data validity
                // convert to txt string list
                // close the program
                break;
        }
        
        
       

    }

}

