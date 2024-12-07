import java.util.ArrayList;

public class Fbk {

    final static String ESCAPE_CHARACTER = "!";
    final static String SEPARATOR_CHARACTER = " ";
    final static int MAX_DESCRIPTION_LENGTH = 5;

    ArrayList<String> inputFbk;
    ArrayList<String> formatedFbk;
    ArrayList<String> outputCoords = new ArrayList<>();
    String jobName;
    String jobDistanceUnits;
    String jobAngleUnits;

    public Fbk (ArrayList<String> inputFile) {
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
        this.outputCoords = computeCoordinates();
    }

    private void addLineNumber() {
        for (int i = 0; i < formatedFbk.size(); i++) {
            formatedFbk.set(i, (i + 1) + SEPARATOR_CHARACTER + formatedFbk.get(i));
        }
    }

    private void stripEscaped() {
        ArrayList<String> stripedFbk = new ArrayList<>();

        for (int i = 0; i < formatedFbk.size(); i++) {
            String[] tmpList = formatedFbk.get(i).replaceAll("\"","").split(ESCAPE_CHARACTER);
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




    private ArrayList<String> computeCoordinates() {
        String[] tmpList;

        Coordinate nowStation = new Coordinate();
        Coordinate nowBacksight = new Coordinate();
        double nowPrism = -1;

        ArrayList<Coordinate> knownCoords = new ArrayList<>();
        ArrayList<Coordinate> outputCoords = new ArrayList<>();
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
                outputCoords.add(nowStation); // a enlenver

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

                for (Coordinate coord : knownCoords) {
                    try{
                        if (coord.getPointNumber() == Integer.parseInt(tmpList[2])) {
                            nowBacksight = new Coordinate(coord.getPointNumber(), 
                                                          coord.getNorthing(),
                                                          coord.getEasting(),
                                                          coord.getElevation(),
                                                          (String)tmpList[3]); // Height from STN reading
                        }
                    }
                    catch(NumberFormatException e) {
                        System.out.printf("Backsight point number not a number on line %s\n", tmpList[0]);
                        System.exit(1);
                    }
                }
                outputCoords.add(nowBacksight); // a enlenver
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

                outputCoords.add(new Coordinate(0,0,0,0,Double.toString(nowPrism))); // a enlenver
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

            }
            else {
                
            }

            
        }

        for (Coordinate coord : outputCoords) {
            formatedOutput.add(coord.getTxtLine());
        }
        for (Coordinate coord : knownCoords) {
            formatedOutput.add(coord.getTxtLine());
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