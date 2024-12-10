/*
 * This is the main app code
 * 
 */

public class MainApp {
    public static void main(String[] args) {
    
        CmdArgument cmdArgs = new CmdArgument(args);
        

        // TODO mettre dans la classe CmdArgument
        if (cmdArgs.argumentIsNotCmd()) {
            System.exit(0);
        }

        Messages.txtWelcome("main");


        switch (cmdArgs.getCommand()) {
            case "-c" :
            
                String outputPath;
                // Name file provided ?
                if (cmdArgs.argNum < 2) {
                    System.out.printf("Input file name not provided");
                    System.exit(2);
                }

                // Import a fbk file from the path provided in the command arguments
                Fbk input = new Fbk(ToolsFileIo.importFile(cmdArgs.arguments[1]));
                

                // Try the output file path
                try {
                    outputPath = cmdArgs.arguments[2];
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    // Gives default file name
                    outputPath = input.getJobName();
                }

                // Output converted file
                ToolsFileIo.exportFile(input.getOutputCoords(), outputPath + ".txt");
                System.out.printf("***File Succesfully Converted***\n\n");

                break;
            case "-itxt":

                // utiliser string join
                // verifier outpu fichiet  déjà existant
                // verifier input fichier bon extension
                // demander nom de fichier output
                // faire en sorte que l'objet coord renvoie un description sans guillemets ou avec
                // throw les exceptions fileio
                // search method pour le spoints
                




                System.out.printf("itxt\n");
                break;
            default:
                break;
        }
        
        
       

    }

}

