import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileParser {
    private String INPUT_FILENAME;
    private final String OUTPUT_FILENAME = "output.txt";
    private String [] lines;
    private int order;
    BTree bTree;

    public FileParser(String fileName) {
        INPUT_FILENAME = fileName;
    }

    public void readFileContent() {
        /**
         * Tries to read content of the FileName passed during object creation
         * Throws Exception if
         *      1. File isn't present or can't be read
         *      2. BTree Operation Fails
         */
        try {
            String contents = new String(Files.readAllBytes(Paths.get(INPUT_FILENAME)));
            lines = contents.split("\n");
        }
        catch (IOException e) {
            System.out.println("Filename=" + INPUT_FILENAME + " not found\nException= " + e.toString());
            System.out.println(e);
        }
        this.processFileContent();
    }

    private void processFileContent() {
        /**
         * Translates the input file content to B+Tree Operations
         * Writes into the output file
         */
        try {
            order = Integer.valueOf(lines[0].trim());
        }
        catch(IllegalArgumentException e) {
            System.out.println("[ERROR] Non Integral Order");
            throw e;
        }
        int i = -1;
        StringBuffer output = new StringBuffer();
        try {
            if (lines.length > 1) {
                bTree= new BTree(order);
                for ( i = 1; i < lines.length; i++) {
                    String[] cmd = lines[i].split("\\(");
                    switch (cmd[0]) {
                        case "Insert":
                                bTree.insertKey(new KeyValPair(Double.parseDouble(cmd[1].split(",")[0]),
                                        Double.parseDouble(cmd[1].split("Value")[1].split("\\)")[0])));
                            break;
                        case "Search":
                            if (cmd[1].contains(",")) {
                                ArrayList<KeyValPair> rangeKeys = bTree.searchKeyRange(Double.parseDouble(cmd[1].split(",")[0]),
                                        Double.parseDouble(cmd[1].split(",")[1].split("\\)")[0]));
                                String ot = "\n";
                                if(rangeKeys == null) ot += "Null";
                                else
                                    for(KeyValPair k : rangeKeys)
                                            ot += k + ",";
                                output.append(ot.trim() + "\n");
                            }
                            else {
                                KeyValPair search = bTree.searchKey(Double.parseDouble(cmd[1].split("\\)")[0]));
                                String ot = "\n";
                                if(search == null) ot += "Null";
                                else
                                    ot += search;
                                    output.append(ot.trim() + "\n");
                            }
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                }
            }
        }
        catch(Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.out.println("Caught Exception" + e + "\nStackTrace:" + sw.toString()
                    + "\nOn Reading Line: " + i
                    + " from " + INPUT_FILENAME);
        }
        finally {

            try {
                Files.write(Paths.get("./" + OUTPUT_FILENAME), output.toString().getBytes());
            }
            catch (IOException e) {
                System.err.println("Caught IOException" + e);
            }
            System.out.println("\nCheck output.txt for generated outputs");

        }

    }
    public int getOrder() {
        return order;
    }
}
