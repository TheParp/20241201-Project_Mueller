/**
 * This class include all the mmethods necessary for different calculations
 * -.fbk computing
 * 
 */

public class ToolsFormulas {

    final static String DMS_SYMBOL = "DMS";
    
    /**
     * This method will compute the angle between the station and the reference in the current
     * coordinate system
     * 
     * @param origin Coordinate - The origin (STN) coordinate object
     * @param destination Coordinate - The reference (BS) coordinate object
     * @return the angle between the station and the reference in the current coordinate system
     */
    public static double fbkComputeBsAngle(Coordinate origin, Coordinate destination) {
        double adjustedAngle = 0;
        double adjustment = -1;
        double deltaN = 0;
        double deltaE = 0;

        deltaN = destination.getNorthing() - origin.getNorthing();
        deltaE = destination.getEasting() - origin.getEasting();

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
     * This method will compute the coordinates from a single survey String list coming
     * from a fieldbook (.fbk)
     * 
     * @param surveyed String - list including all arguments from a survey fieldbook (.fbk)
     * @param bsAngle double - the angle computed from station and reference
     * @param station Coordinate - the station coordinate object including height as description
     * @param backsight Coordinate - the backsight coordinate object including ref angle as description
     * @param prismHeight double - the last prism height met in the fieldbook
     * @param angleUnits String - Angle units (DMS or DEC)
     * @return
     */
    public static Coordinate fbkComputeCoord(String[] surveyed, double bsAngle, Coordinate station, Coordinate backsight, double prismHeight, String angleUnits) {
        Coordinate output = new Coordinate();

        int pointNumber = 0;
        double hAngle = 0;
        double vAngle = 0;
        double distance = 0;
        double instrumentHeight = 0;
        double refAngle = 0;

        double bearing = 0;
        double horDistance = 0;
        double deltaN = 0;
        double deltaE = 0;
        double deltaZ = 0;
        
        try {
            if (angleUnits.contains(DMS_SYMBOL)) {
                pointNumber = Integer.parseInt(surveyed[3]);
                hAngle = ToolsUnitConversion.convertDmsToDec(Double.parseDouble(surveyed[4]));
                vAngle = ToolsUnitConversion.convertDmsToDec(Double.parseDouble(surveyed[6]));
                distance = Double.parseDouble(surveyed[5]);
                instrumentHeight = Double.parseDouble(station.getDescription());
                refAngle = ToolsUnitConversion.convertDmsToDec(Double.parseDouble(backsight.getDescription()));
                
            }
            else {
                pointNumber = Integer.parseInt(surveyed[3]);
                hAngle = Double.parseDouble(surveyed[4]);
                vAngle = Double.parseDouble(surveyed[6]);
                distance = Double.parseDouble(surveyed[5]);
                instrumentHeight = Double.parseDouble(station.getDescription());
                refAngle = Double.parseDouble(backsight.getDescription());
            }
        }
        catch (NumberFormatException e) {
            System.out.printf("Survey value is not a number on line %s\n", surveyed[0]);
            System.exit(1);
        }
        
        bearing = hAngle + bsAngle - refAngle;

        if (bearing < 0.0) {
            bearing += 360.0;
        }
        else if (bearing > 360.0) {
            bearing -= 360.0;
        }

        horDistance = distance * Math.sin(Math.toRadians(vAngle));
        deltaN = horDistance * Math.cos(Math.toRadians(bearing));
        deltaE = horDistance * Math.sin(Math.toRadians(bearing));
        deltaZ = instrumentHeight + (distance * Math.cos(Math.toRadians(vAngle))) - prismHeight;

        output.setPointNumber(pointNumber);
        output.setNorthing(station.getNorthing() + deltaN);
        output.setEasting(station.getEasting() + deltaE);
        output.setElevation(station.getElevation() + deltaZ);

        return output;
    }
}
