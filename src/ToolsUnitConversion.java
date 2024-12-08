public class ToolsUnitConversion {

    final static int ANGLE_PRECISION = 5;
    
    public static double convertDmsToDec (double input) {
        double output;

        int degrees;
        int minutes;
        int seconds;
        double tmp;

        degrees = (int)input;
        tmp = (input - degrees) * 100.0;
        minutes = (int)tmp;
        tmp = (tmp - minutes) * 100.0;
        seconds = (int)tmp;

        output = (((seconds / 60.0) + minutes) / 60.0) + degrees;

        return output;
    }

}
