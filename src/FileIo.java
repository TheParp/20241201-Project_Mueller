import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class FileIo {

    public static ArrayList<String> importFile (String filePath) {
        ArrayList<String> fileContent = new ArrayList<>();
        String line;

        try {
            BufferedReader inputFile = new BufferedReader(new FileReader(filePath));  
            while ((line = inputFile.readLine()) != null) {
                fileContent.add(line);
            }
            inputFile.close();
        }
        catch (IOException e) {
            System.out.printf("File not found");
            System.exit(1);
        }

        return fileContent;
    }

    public static boolean exportFile (ArrayList<String> content, String filePath) {
        try {
            BufferedWriter outputFile = new BufferedWriter(new FileWriter(filePath));
            for (String line : content) {
                outputFile.write(line + "\n");
            }
            outputFile.close();
        }
        catch (IOException e) {
            System.out.printf("Cannot Create File");
            return false;
        }

        return true;
    }

}