/**
 * This class createe a Coordinate object and will handle all operations that 
 * can be made with 3dpoints. Did not exended Point3D from JavaFx to practice 
 * since it is a relatively simple class.
 * 
 * @author Alexandre Parent
 * @version 1.0
 * @since Mueller V0.1
 */
public class GeoPoint {

    final static String NOT_INIT = "Not Initialized";
    final static String DEFAULT_UNITS = "METERS";
    final static String SEPARATOR_CHARACTER = " ";

    /**
     * Number of the actual point
     */
    int pointId;

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
     * Units of the currents coordinates
     */
    String units;

    /**
     * Default constructor. 
     * All variables will be 0 and the message string 
     * "Not initialized" will be given to the description
     */
    public GeoPoint() {
        this.pointId = 0;
        this.northing = 0;
        this.easting = 0;
        this.elevation = 0;
        this.description = NOT_INIT;
        this.units = DEFAULT_UNITS;
    }

    /**
     * This is the full overloaded constructor. 
     * All parameters must be entered as method arguments.
     * 
     * @param pointId integer - Point number
     * @param north double - Northing coordinate
     * @param east double - Easting coordinate
     * @param elev double - Point Elevation
     * @param description String - Description/Code
     * @param units String - Units used by these coordinates
     */
    public GeoPoint(int pointId, double north, double east, double elev, String description, String units) {
        this.pointId = pointId;
        this.northing = north;
        this.easting = east;
        this.elevation = elev;
        this.description = description;
        this.units = units;
    }

    /**
     * This method returns a GeoPoint with the specified coordinates added to the
     * coordinates of this point
     * 
     * @param deltaN double - delta Northing
     * @param deltaE double - delta Easting
     * @param deltaZ double - delta Elevation
     * @return a Geopoint with the sum as coordinates
     */
    public GeoPoint add(double deltaN, double deltaE, double deltaZ) {

        GeoPoint output = new GeoPoint();

        output.setNorthing(this.northing + deltaN);
        output.setEasting(this.easting + deltaE);
        output.setElevation(this.elevation + deltaZ);

        return output;
    }

    /**
     * This method will compute the angle between the station and the reference point (destination)
     * in perspective of the N (y) axis
     * 
     * @param destination GeoPoint - The reference (BS) GeoPoint object
     * @return the angle between the station and the reference in the current coordinate system
     */
    public double computeNorthAngle(GeoPoint destination) {
        double adjustedAngle = 0;
        double adjustment = -1;
        double deltaN = 0;
        double deltaE = 0;

        deltaN = destination.getNorthing() - this.northing;
        deltaE = destination.getEasting() - this.easting;

        // Find the angle adjustment for computing
        if (deltaN >= 0){
            if (deltaE >= 0) {
                adjustment = 0;
            }
            else {
                adjustment = 360;
            }
        }
        else {
            if (deltaE >= 0) {
                adjustment = 180;
            }
            else {
                adjustment = 180;
            }
        }

        // Compute the adjusted angle
        adjustedAngle = Math.toDegrees(Math.atan(deltaE / deltaN)) + adjustment;

        // Adjust the angle if more than 360
        if (adjustedAngle > 360) {
            adjustedAngle -= 360;
        }
        return adjustedAngle;
    }

    /**
     * This methods compute the distance between this point and the input point
     * in NEZ format
     * 
     * @param northing double - Northing coordinate of the destination point
     * @param easting double - Easting coordinate of the destination point
     * @param elevation double - Elevation of the destination point
     * @return a double value containing the distance
     */
    public double distance(double northing, double easting, double elevation) {

        double output;

        double deltaN = this.northing - northing;
        double deltaE = this.easting - easting;
        double deltaZ = this.elevation - elevation;

        output = Math.pow(deltaN, 2) + Math.pow(deltaE, 2) + Math.pow(deltaZ, 2);
        output = Math.sqrt(output);

        return output;
    }

    /**
     * This methods compute the distance between this point and the input point
     * in Geopoint format
     * 
     * @param input GeoPoint - Destination point
     * @return a double value containing the distance
     */
    public double distance(GeoPoint input) {

        double output;

        double deltaN = this.northing - input.northing;
        double deltaE = this.easting - input.easting;
        double deltaZ = this.elevation - input.elevation;

        output = Math.pow(deltaN, 2) + Math.pow(deltaE, 2) + Math.pow(deltaZ, 2);
        output = Math.sqrt(output);

        return output;
    }

    /**
     * This methods compute the horizontal distance between this point and 
     * the input point in NEZ format
     * 
     * @param northing double - Northing coordinate of the destination point
     * @param easting double - Easting coordinate of the destination point
     * @return a double value containing the distance
     */
    public double horDistance(double northing, double easting) {

        double output;

        double deltaN = this.northing - northing;
        double deltaE = this.easting - easting;

        output = Math.pow(deltaN, 2) + Math.pow(deltaE, 2);
        output = Math.sqrt(output);

        return output;
    }

    /**
     * This methods compute the distance between this point and the input point
     * in Geopoint format
     * 
     * @param input GeoPoint - Destination point
     * @return a double value containing the distance
     */
    public double horDistance(GeoPoint input) {

        double output;

        double deltaN = this.northing - input.northing;
        double deltaE = this.easting - input.easting;

        output = Math.pow(deltaN, 2) + Math.pow(deltaE, 2);
        output = Math.sqrt(output);

        return output;
    }

    /**
     * This method returns a GeoPoint with the coordinates of the specifies
     * point added to the coordinates of this point
     * 
     * @param input GeoPoint - input GeoPoint (delta)
     * @return a Geopoint with the sum as coordinates
     */
    public GeoPoint sum(GeoPoint input) {
        GeoPoint output = new GeoPoint();
        output.setNorthing(this.northing + input.northing);
        output.setEasting(this.easting + input.easting);
        output.setElevation(this.elevation + input.elevation);

        return output;
    } 






    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public void setNorthing(double north) {
        this.northing = north;
    }

    public void setEasting(double east) {
        this.easting = east;
    }

    public void setElevation(double elev) {
        this.elevation = elev;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUnits(String units) {
        this.units = units;
    }



    public int getPointId() {
        return pointId;
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
        return String.format("%d%s%.4f%s%.4f%s%.4f%s%s", pointId, SEPARATOR_CHARACTER, northing, SEPARATOR_CHARACTER, easting, SEPARATOR_CHARACTER, elevation, SEPARATOR_CHARACTER, description);
    }
}