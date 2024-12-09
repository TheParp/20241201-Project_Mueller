import java.util.ArrayList;

/**
 * This class can 
 * 
 */
public class Fbk {

    final static String ESCAPE_CHARACTER = "!";
    final static String SEPARATOR_CHARACTER = " ";
    final static String DESCRIPTION_DELIM = "\"";
    final static int MAX_DESCRIPTION_LENGTH = 5;

    ArrayList<String> inputFbk;
    ArrayList<String> formatedFbk;
    ArrayList<String> outputCoords = new ArrayList<>();
    String jobName;
    String jobDistanceUnits;
    String jobAngleUnits;

    /**
     * 
     * @param inputFile
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

    private void addLineNumber() {
        for (int i = 0; i < formatedFbk.size(); i++) {
            formatedFbk.set(i, (i + 1) + SEPARATOR_CHARACTER + formatedFbk.get(i));
        }
    }

    private void stripEscaped() {
        ArrayList<String> stripedFbk = new ArrayList<>();

        for (int i = 0; i < formatedFbk.size(); i++) {
            String[] tmpList = formatedFbk.get(i).replaceAll(DESCRIPTION_DELIM,"").split(ESCAPE_CHARACTER);
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
                stripedFbk.add(formatedFbk.get(i).trim().replaceAll(SEPARATOR_CHARACTER + "+", " "));
            }
        }
        formatedFbk = stripedFbk;
    }

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

        Coordinate nowStation = new Coordinate();
        Coordinate nowBacksight = new Coordinate();
        double nowPrism = -1;

        ArrayList<Coordinate> knownCoords = new ArrayList<>();
        ArrayList<Coordinate> surveyCoords = new ArrayList<>();
        ArrayList<String> formatedOutput = new ArrayList<>();
 
        for (String line : formatedFbk) {
            tmpList = line.split(SEPARATOR_CHARACTER);


            if (tmpList[1].contains("NEZ")) {
                if (tmpList.length < 6 || tmpList.length > 6 + MAX_DESCRIPTION_LENGTH) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                knownCoords.add(new Coordinate(tmpList));
            }


            else if (tmpList[1].contains("STN")) {
                if (tmpList.length != 4) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                for (Coordinate coord : knownCoords) {
                    try{
                        if (coord.getPointNumber() == Integer.parseInt(tmpList[2])) {
                            nowStation = new Coordinate(coord.getPointNumber(), 
                                                        coord.getNorthing(),
                                                        coord.getEasting(),
                                                        coord.getElevation(),
                                                        (String)tmpList[3]); // Height from STN reading
                        }
                    }
                    catch(NumberFormatException e) {
                        System.out.printf("Station point number not a number on line %s\n", tmpList[0]);
                        System.exit(1);
                    }
                }
            }


            else if (tmpList[1].contains("BS")) {
                if (tmpList.length != 4) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowStation.getDescription().contains(Coordinate.NOT_INIT)) {
                    System.out.printf("Backsight but no station, line %s\n", tmpList[0]);
                    System.exit(1);
                }

                // TODO -- Faire méthode de recherche

                
                for (Coordinate coord : knownCoords) {
                    try{
                        if (coord.getPointNumber() == Integer.parseInt(tmpList[2])) {
                            nowBacksight = new Coordinate(coord.getPointNumber(), 
                                                          coord.getNorthing(),
                                                          coord.getEasting(),
                                                          coord.getElevation(),
                                                          (String)tmpList[3]); // Angle from ref reading
                        }
                    }
                    catch(NumberFormatException e) {
                        System.out.printf("Backsight point number not a number on line %s\n", tmpList[0]);
                        System.exit(1);
                    }
                }
            }


            else if (tmpList[1].contains("PRISM")) {
                if (tmpList.length != 3) {
                    System.out.printf("Problem on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowStation.getDescription().contains(Coordinate.NOT_INIT)) {
                    System.out.printf("Prism but no station, line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowBacksight.getDescription().contains(Coordinate.NOT_INIT)) {
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


            else if (tmpList[1].contains("F1")) {                
                if (tmpList.length < 7 || tmpList.length > 7 + MAX_DESCRIPTION_LENGTH) {
                    System.out.printf("Problem with survey data on line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowStation.getDescription().contains(Coordinate.NOT_INIT)) {
                    System.out.printf("Survey data but no station, line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowBacksight.getDescription().contains(Coordinate.NOT_INIT)) {
                    System.out.printf("Survey data but no backsight, line %s\n", tmpList[0]);
                    System.exit(1);
                }
                if (nowPrism == -1) {
                    System.out.printf("Survey data but no prism height, line %s\n", tmpList[0]);
                    System.exit(1);
                }

                Coordinate surveyPoint = new Coordinate();
                
                bsAngle = ToolsFormulas.fbkComputeBsAngle(nowStation, nowBacksight);
                surveyPoint = ToolsFormulas.fbkComputeCoord(tmpList, bsAngle, nowStation, nowBacksight, nowPrism, jobAngleUnits);
                
                surveyPoint.description = ToolsFormat.fbkConcatDescription(8, tmpList, SEPARATOR_CHARACTER, DESCRIPTION_DELIM);    
                surveyCoords.add(surveyPoint);
                
            }            
        }

        for (Coordinate coord : surveyCoords) {
            formatedOutput.add(coord.toString());
        }
        for (Coordinate coord : knownCoords) {
            formatedOutput.add(coord.toString());
        }

        return formatedOutput;
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