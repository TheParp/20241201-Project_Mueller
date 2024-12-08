public class Coordinate {

    final static String SEPARATOR_CHARACTER = " ";
    final static String DESCRIPTION_DELIM = "\"";
    final static String NOT_INIT = "Not Initialized";

    int pointNumber;
    double northing;
    double easting;
    double elevation;
    String description;


    public Coordinate() {
        this.pointNumber = 9999;
        this.northing = 0;
        this.easting = 0;
        this.elevation = 0;
        this.description = NOT_INIT;
    }

    public Coordinate(int pNum, double n, double e, double el, String desc) {
        this.pointNumber = pNum;
        this.northing = n;
        this.easting = e;
        this.elevation = el;
        this.description = desc;
    }



    /**
     * This constructor is designed to take the inputs from the Fbk class.
     * @param nez
     */
    public Coordinate(String[] nez) {
        
        this.description = "";

        this.pointNumber = extractPointNumberFromFbk(nez);
        this.northing = extractNorthingFromFbk(nez);
        this.easting = extractEastingFromFbk(nez);
        this.elevation = extractElevationFromFbk(nez);

        this.description = ToolsFormat.fbkConcatDescription(7, nez, SEPARATOR_CHARACTER, DESCRIPTION_DELIM);
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
