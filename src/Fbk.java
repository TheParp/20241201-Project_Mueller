import java.util.ArrayList;

class Fbk {

    static String ESCAPE_CHARACTER = "!";
    static String SEPARATOR_CHARACTER = " ";

    ArrayList<String> inputFbk;
    ArrayList<String> outputCoords = new ArrayList<>();

    public Fbk (ArrayList<String> inputFile) {
        this.inputFbk = new ArrayList<>(inputFile);
        addLineNumber();
        stripEscaped();
        removeEmptyLines();
    }

    private void addLineNumber() {
        for (int i = 0; i < inputFbk.size(); i++) {
            inputFbk.set(i, (i + 1) + SEPARATOR_CHARACTER + inputFbk.get(i));
        }
    }

    private void stripEscaped() {
        ArrayList<String> stripedFbk = new ArrayList<>();

        for (int i = 0; i < inputFbk.size(); i++) {
            String[] tmpList = inputFbk.get(i).split(ESCAPE_CHARACTER);
            stripedFbk.add(tmpList[0]);
        }
        inputFbk = stripedFbk;
    }

    private void removeEmptyLines() {
        ArrayList<String> stripedFbk = new ArrayList<>();
        for (int i = 0; i < inputFbk.size(); i++) {
            String[] tmpList = inputFbk.get(i).split(SEPARATOR_CHARACTER);
            if (tmpList.length > 1) {
                stripedFbk.add(inputFbk.get(i));
            }
        }
        inputFbk = stripedFbk;
    }

    public ArrayList<String> getFbk() {
        return inputFbk;
    }

    private void getUnits() {

    }

    private void getJob() {

    }

    private void getNez() {

    }

    private void computeFbk() {

    }

}