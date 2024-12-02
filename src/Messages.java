/*
 * 
 * 
 */
public class Messages {

    public static void txtWelcome(String in) {
        String MESS_MAIN_WELCOME = "\n" +
                                   "---------------------------------------------------------\n" +
                                   "----------------WELCOME TO PROJECT MUELLER---------------\n" +
                                   "---------------------------------------------------------\n" +
                                   "\n";
        
        if(in == "main") {
            System.out.printf(MESS_MAIN_WELCOME);
        }
    }


    public static void txtUsage(String in) {
        String MESS_GUI_CONSTRUCTION = "\n" +
                                       "---------------------------------------------------------\n" +
                                       "----------------GUI UNDER CONSTRUCTION!------------------\n" +
                                       "---------------------------------------------------------\n" +
                                       "\n";

        String MESS_USAGE = "\n" +
                            "---------------------------------------------------------\n" +
                            "--------------------INVALID ARGUMENT---------------------\n" +
                            "---------------------------------------------------------\n" +
                            "\n" +
                            "How to use :\n" +
                            ".../java MainApp -(control argument) [FILENAME]\n";

        if (in == "gui") {
            System.out.printf(MESS_GUI_CONSTRUCTION);
        }
        else if (in == "usageGen") {
            System.out.printf(MESS_USAGE);
        }

    }
    

    public static void txtError(String in) {

        String ERR_NO_ARG = "The program did not returned any argument validation!\n";

        if (in == "problem with argument validation") {
            System.out.printf(ERR_NO_ARG);
        }
    }

}