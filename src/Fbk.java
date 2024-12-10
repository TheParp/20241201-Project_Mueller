import java.util.ArrayList;

/**
 * This class will handle all methods relative to fieldbook (fbk) handling
 * 
 * @author Alexandre Parent
 * @since v0.1
 * @version v0.2
 * 
 */
public class Fbk {

    final static String ESCAPE_CHARACTER = "!";
    final static String SEPARATOR_CHARACTER = " ";
    final static String DESCRIPTION_DELIM = "\"";
    final static int MAX_DESCRIPTION_LENGTH = 5;

    final static String DMS_SYMBOL = "DMS";

    /**
     * This variable is going to keep the original fbk
     */
    ArrayList<String> inputFbk;

    /**
     * This variable contains a fromated version of the fbk
     * Escaped character escaped, empty lines deleted
     */
    ArrayList<String> formatedFbk;

    /**
     * This variable contains the coordinates of the surveyed 
     * points, calculated
     */
    ArrayList<String> outputCoords = new ArrayList<>();

    /**
     * This variable contains the job name
     */
    String jobName;

    /**
     * this variable contains the distance units (METER or FEET)
     */
    String jobDistanceUnits;

    /**
     * This variable contains the angle units (DMS or DEC)
     */
    String jobAngleUnits;

    /**
     * This is the main constructor. It take in the fbk and compute the output
     * coords at creation.
     * 
     * @param inputFile Arraylist<String> - Takes a complete fbk file
     */
    public Fbk(ArrayList<String> inputFile) {
        this.inputFbk = inputFile;
        this.formatedFbk = new ArrayList<>(inputFile);
        this.jobName = "";
        this.jobDistanceUnits = "";
        this.jobAngleUnits = "";
        addLineNumber();
        stripEscaped();
        removeEmptyLines();
        findJobName();
        findJobUnits();
        this.outputCoords = computeFbkLines();
    }


    /**
     * This method count the lines in the FORMATEDFBK variable and add the
     * line number in front of each string
     */
    private void addLineNumber() {
        for (int i = 0; i < formatedFbk.size(); i++) {
            formatedFbk.set(i, 
                            (i + 1) + 
                            SEPARATOR_CHARACTER + 
                            formatedFbk.get(i));
        }
    }

    /**
     * This method get rid of all the characters after an escaped character and 
     * get rid of the " character in the descriptions. all in variable formatedFbk
     */
    private void stripEscaped() {

        ArrayList<String> stripedFbk = new ArrayList<>();

        for (int i = 0; i < formatedFbk.size(); i++) {
            String[] tmpList = formatedFbk.get(i)
                                .replaceAll(DESCRIPTION_DELIM,"")
                                .split(ESCAPE_CHARACTER);
            stripedFbk.add(tmpList[0]);
        }
        formatedFbk = stripedFbk;
    }

    /**
     * This method remove empty lines and replace doubled or more
     * separators by unique separator. Then trim each element.
     */
    private void removeEmptyLines() {
        ArrayList<String> stripedFbk = new ArrayList<>();
        for (int i = 0; i < formatedFbk.size(); i++) {
            String[] tmpList = formatedFbk.get(i).split(SEPARATOR_CHARACTER);
            if (tmpList.length > 1) {
                stripedFbk.add(formatedFbk.get(i).trim()
                                .replaceAll(SEPARATOR_CHARACTER + "+",
                                             SEPARATOR_CHARACTER));
            }
        }
        formatedFbk = stripedFbk;
    }

    /**
     * This method find the job name in the fbkand send it to the fbk specific variable
     * If no job name is found, an error is throwned
     */
    private void findJobName() {
        for (String line : formatedFbk) {
            String[] tmpList = line.split(SEPARATOR_CHARACTER);
            if (tmpList[1].contains("JOB:")) {
                if (jobName != "") {
                    System.out.printf("The file have more than 1 job name. Line %s\n", tmpList[0]);
                    System.exit(1);
                }
                else if (tmpList.length != 3) {
                    System.out.printf("Problem with the job name. Line %s\n", tmpList[0]);
                    System.exit(1);
                }
                else {
                    this.jobName = tmpList[2];
                }
            }
        }
    }

    /**
     * This method finds the job units (angle and distance) and send it to the fbk specific variable
     */
    private void findJobUnits() {
        for (String line : formatedFbk) {
            String[] tmpList = line.split(SEPARATOR_CHARACTER);
            if (tmpList[1].contains("UNITS")) {
                if (jobDistanceUnits != "" || jobAngleUnits != "") {
                    System.out.printf("The file have more than 1 job units definition. Line %s\n", tmpList[0]);
                    System.exit(1);
                }
                else if (tmpList.length != 4) {
                    System.out.printf("Problem with the job units definition. Line %s\n", tmpList[0]);
                    System.exit(1);
                }
                else {
                    this.jobDistanceUnits = tmpList[2];
                    this.jobAngleUnits = tmpList[3];
                }
            }
        }
    }

    /**
     * This method will compute all the coordinates from a complete formted fbk
     * included in the Fbk object as a String Assray list.
     * 
     * @return a String Array list including coordinates of all survey points from fieldbook
     */
    private ArrayList<String> computeFbkLines() {
        String[] tmpList;
        double bsAngle = 0;

        GeoPoint nowStation = new GeoPoint();
        GeoPoint nowBacksight = new GeoPoint();
        double nowPrism = -1;

        ArrayList<GeoPoint> knownCoords = new ArrayList<>();
        ArrayList<GeoPoint> surveyCoords = new ArrayList<>();
        ArrayList<String> formatedOutput = new ArrayList<>();
 
        // For each line in the formated fbk variable, check the type and act
        // accordingly
        for (String line : formatedFbk) {

            // Split the entire line
            tmpList = line.split(SEPARATOR_CHARACTER);

            // If reference coordinates NEZ, put it in know coords variable.
            if (tmpList[1].contains("NEZ")) {
                if (tmpList.length < 6 || tmpList.length > 6 + MAX_DESCRIPTION_LENGTH) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }

                // Create a NEZ known point, pass it the line coordinates
                GeoPoint output = new GeoPoint();
                try {
                    output.pointId = Integer.parseInt(tmpList[2]);
                    output.northing = Double.parseDouble(tmpList[3]);
                    output.easting = Double.parseDouble(tmpList[4]);
                    output.elevation = Double.parseDouble(tmpList[5]);
                    output.units = jobDistanceUnits;
                }
                catch (NumberFormatException e) {
                    System.out.printf("Point coordinate not a number on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                // put the description back together
                output.description = ToolsFormat.fbkConcatDescription(7, tmpList, SEPARATOR_CHARACTER, DESCRIPTION_DELIM);
                
                // Add this coord to the know Arraylist
                knownCoords.add(output);
            }

            // If fbk line is a Station line (STN)
            else if (tmpList[1].contains("STN")) {
                if (tmpList.length != 4) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }

                // Search the point Id in the known coords arraylist. If found, give those
                // coordinates to the STN point. Give the point the height as description
                for (GeoPoint coord : knownCoords) {
                    try{
                        if (coord.getPointId() == Integer.parseInt(tmpList[2])) {
                            nowStation = new GeoPoint(coord.getPointId(), 
                                                        coord.getNorthing(),
                                                        coord.getEasting(),
                                                        coord.getElevation(),
                                                        (String)tmpList[3], // Height from STN reading
                                                        jobDistanceUnits); 
                        }
                    }
                    catch(NumberFormatException e) {
                        System.out.printf("Station point id not a number on line %s\n", tmpList[0]);
                        System.exit(1);
                    }
                }
            }

            // If fbk line is a backsight line (BS)
            else if (tmpList[1].contains("BS")) {
                if (tmpList.length != 4) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowStation.getDescription().contains(GeoPoint.NOT_INIT)) {
                    System.out.printf("Backsight but no station, line %s\n", tmpList[0]);
                    System.exit(1);
                }

                // Search the point Id in the known coords arraylist. If found, give those
                // coordinates to the STN point. Give the point the height as description
                for (GeoPoint coord : knownCoords) {
                    try{
                        if (coord.getPointId() == Integer.parseInt(tmpList[2])) {
                            nowBacksight = new GeoPoint(coord.getPointId(), 
                                                          coord.getNorthing(),
                                                          coord.getEasting(),
                                                          coord.getElevation(),
                                                          (String)tmpList[3], // Angle from ref reading
                                                          jobDistanceUnits); 
                        }
                    }
                    catch(NumberFormatException e) {
                        System.out.printf("Backsight point number not a number on line %s\n", tmpList[0]);
                        System.exit(1);
                    }
                }
            }

            // If the fbk line is a prism height, put it in the fbk prism variable
            else if (tmpList[1].contains("PRISM")) {
                if (tmpList.length != 3) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowStation.getDescription().contains(GeoPoint.NOT_INIT)) {
                    System.out.printf("Prism but no station, line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowBacksight.getDescription().contains(GeoPoint.NOT_INIT)) {
                    System.out.printf("Prism but no backsight, line %s\n", tmpList[0]);
                    System.exit(1);
                }

                try {
                    nowPrism = Double.parseDouble(tmpList[2]);
                }
                catch(NumberFormatException e) {
                    System.out.printf("Prism height not a number, line %s\n", tmpList[0]);
                    System.exit(1);
                }
            }

            // If fbk line is a survey reading
            else if (tmpList[1].contains("F1")) {                
                if (tmpList.length < 7 || tmpList.length > 7 + MAX_DESCRIPTION_LENGTH) {
                    System.out.printf("Problem with survey data on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowStation.getDescription().contains(GeoPoint.NOT_INIT)) {
                    System.out.printf("Survey data but no station, line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowBacksight.getDescription().contains(GeoPoint.NOT_INIT)) {
                    System.out.printf("Survey data but no backsight, line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowPrism == -1) {
                    System.out.printf("Survey data but no prism height, line %s\n", tmpList[0]);
                    System.exit(1);
                }

                GeoPoint surveyPoint = new GeoPoint();
                
                bsAngle = nowStation.computeNorthAngle(nowBacksight);
                surveyPoint = fbkComputeCoord(tmpList, bsAngle, nowStation, nowBacksight, nowPrism, jobAngleUnits);
                
                surveyPoint.description = ToolsFormat.fbkConcatDescription(8, tmpList, SEPARATOR_CHARACTER, DESCRIPTION_DELIM);    
                surveyCoords.add(surveyPoint);
            }            
        }

        // Recombine the known coordinates and the survey coordinates in the output variable
        for (GeoPoint coord : surveyCoords) {
            formatedOutput.add(coord.toString());
        }
        for (GeoPoint coord : knownCoords) {
            formatedOutput.add(coord.toString());
        }

        return formatedOutput;
    }


    /**
     * This method will compute the coordinates from a SINGLE survey String list coming
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
    public static GeoPoint fbkComputeCoord(String[] surveyed, double bsAngle, GeoPoint station, GeoPoint backsight, double prismHeight, String angleUnits) {
        GeoPoint output = new GeoPoint();

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

        output.setPointId(pointNumber);
        output.setNorthing(station.getNorthing() + deltaN);
        output.setEasting(station.getEasting() + deltaE);
        output.setElevation(station.getElevation() + deltaZ);

        return output;
    }






    public ArrayList<String> getFbk() {
        return formatedFbk;
    }

    public String getJobName() {
        return jobName;
    }

    public String getDistanceUnits() {
        return jobDistanceUnits;
    }

    public String getAngleUnits() {
        return jobAngleUnits;
    }

    public ArrayList<String> getOutputCoords() {
        return outputCoords;
    }



    public String toString() {
        String output;

        output = "Job Name : " + jobName + "\n" +
                 "Job units : " + jobDistanceUnits + " " + jobAngleUnits + "\n";

        return output;
    }

}