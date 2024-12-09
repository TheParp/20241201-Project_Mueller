/**
 * This class createe a Coordinate object and will handle all operations that 
 * can be made with 3dpoints. Did not exended Point3D from JavaFx to practice 
 * since it is a relatively simple class.
 * 
 * @author Alexandre Parent
 * @version 0.1
 * @since Mueller V0.1
 */
public class Coordinate{

    final static String SEPARATOR_CHARACTER = " ";
    final static String DESCRIPTION_DELIM = "\"";
    final static String NOT_INIT = "Not Initialized";

    /**
     * Number of the actual point
     */
    int pointNumber;

    /**
     * Northing coordinate (Y axis)
     */
    double northing;

    /**
     * Easting coordinate (X axis)
     */
    double easting;

    /**
     * Elevation (Z axis)
     */
    double elevation;

    /**
     * Description given to point. Can be a code, can be a description. In 
     * certain computing, this field will be given a value (in string form)
     */
    String description;


    /**
     * Default constructor. All variables will be 0 and the message string 
     * "Not initialized" will be given to the description
     */
    public Coordinate() {
        this.pointNumber = 0;
        this.northing = 0;
        this.easting = 0;
        this.elevation = 0;
        this.description = NOT_INIT;
    }

    /**
     * This is the overloaded constructor. All parameters must be entered as 
     * method arguments.
     * 
     * @param pNum integer - Point number
     * @param n double - Northing coordinate
     * @param e double - Easting coordinate
     * @param el double - Point Elevation
     * @param desc String - Description/Code
     */
    public Coordinate(int pNum, double n, double e, double el, String desc) {
        this.pointNumber = pNum;
        this.northing = n;
        this.easting = e;
        this.elevation = el;
        this.description = desc;
    }



    /**
     * This constructor is designed to take the input List of Strings from the
     * Fbk class.
     * 
     * @param fbkLine Is a String List containing all data from a single 
     *                  Fbk line.
     */
    public Coordinate(String[] fbkLine) {
        
        this.description = "";

        this.pointNumber = extractPointNumberFromFbk(fbkLine);
        this.northing = extractNorthingFromFbk(fbkLine);
        this.easting = extractEastingFromFbk(fbkLine);
        this.elevation = extractElevationFromFbk(fbkLine);

        this.description = ToolsFormat.fbkConcatDescription(7, fbkLine, SEPARATOR_CHARACTER, DESCRIPTION_DELIM);
    }

    private int extractPointNumberFromFbk(String[] nez) {
        try {
            return Integer.parseInt(nez[2]);
        }
        catch (NumberFormatException e) {
            System.out.printf("Point number not a number on line %s\n", nez[0]);
            return 0;
        }
    }

    private double extractNorthingFromFbk(String[] nez) {
        try {
            return Double.parseDouble(nez[3]);
        }
        catch (NumberFormatException e) {
            System.out.printf("Point northing not a number on line %s\n", nez[0]);
            return 0;
        }
    }

    private double extractEastingFromFbk(String[] nez) {
        try {
            return Double.parseDouble(nez[4]);
        }
        catch (NumberFormatException e) {
            System.out.printf("Point easting not a number on line %s\n", nez[0]);
            return 0;
        }
    }

    private double extractElevationFromFbk(String[] nez) {
        try {
            return Double.parseDouble(nez[5]);
        }
        catch (NumberFormatException e) {
            System.out.printf("Point easting not a number on line %s\n", nez[0]);
            return 0;
        }
    }


    public void setPointNumber(int pNum) {
        this.pointNumber = pNum;
    }

    public void setNorthing(double n) {
        this.northing = n;
    }

    public void setEasting(double e) {
        this.easting = e;
    }

    public void setElevation(double el) {
        this.elevation = el;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }



    public int getPointNumber() {
        return pointNumber;
    }

    public double getNorthing() {
        return northing;
    }

    public double getEasting() {
        return easting;
    }

    public double getElevation() {
        return elevation;
    }

    public String getDescription() {
        return description;
    }


    public String toString() {
        return String.format("%d%s%.4f%s%.4f%s%.4f%s%s", pointNumber, SEPARATOR_CHARACTER, northing, SEPARATOR_CHARACTER, easting, SEPARATOR_CHARACTER, elevation, SEPARATOR_CHARACTER, description);
        
    }
}
