import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Transpose {

    private File outputFile;
    private File inputFile;

    @Option(name = "-a", usage = "Width of a field for a word.")
    private int num;

    @Option(name = "-t", usage = "Cuts words if they don't fit in the size set by '-a'.")
    private boolean cutWords = false;

    @Option(name = "-r", usage = "Aligns words to the right.")
    private boolean alignRight = false;

    @Option(name = "-o", usage = "Sets an output file, else STDOUT.")
    private void setOutputFile(File file) throws IOException {
        if (file.exists() && file.canWrite() || file.createNewFile()) {
            outputFile = file;
        } else {
            throw new FileNotFoundException();
        }
    }

    @Argument(usage = "Sets an input file, else STDIN.")
    private void setInputFile(File file) throws FileNotFoundException {
        if (file.exists() && file.isFile() && file.canRead()) {
            inputFile = file;
        } else {
            throw new FileNotFoundException();
        }
    }

    public static void main(String[] args) throws IOException {
        new Transpose().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("transpose [-a width] [-t] [-r] [-o outputFile] [inputFile]");
            parser.printUsage(System.err);
            return;
        }

        BufferedReader input = (inputFile == null) ? new BufferedReader(new InputStreamReader(System.in)) : new BufferedReader(new FileReader(inputFile));
        BufferedWriter output = (outputFile == null) ? new BufferedWriter(new OutputStreamWriter(System.out)) : new BufferedWriter(new FileWriter(outputFile));

    }

    private ArrayList<ArrayList<String>> readWords(BufferedReader in) throws IOException {
        ArrayList<ArrayList<String>> words = new ArrayList<>();
        int maxLength = 0;

        try (in) {
            String line = in.readLine();
            while (line != null) {
                if (!line.isBlank()) {
                    ArrayList<String> row = new ArrayList<>(Arrays.asList(line.trim().split(" +")));
                    words.add(row);
                    maxLength = Integer.max(maxLength, row.size());
                }
                line = in.readLine();
            }
        }

        ArrayList<String> end = new ArrayList<>();
        for (int i = 0; i < maxLength; i++) {
            end.add("");
        }

        for (ArrayList<String> row : words) {
            int size = row.size();
            if (size < maxLength) {
                row.addAll(end.subList(0, maxLength - size));
            }
        }
        return words;
    }

    private void writeWords(ArrayList<ArrayList<String>> words, BufferedWriter out) throws IOException {
        final int rows = words.size();
        final int columns = words.get(0).size();
        try (out) {
            for (int i = 0; i < columns; i++) {
                StringBuilder line = new StringBuilder();
                for (int j = 0; j < rows; j++) {
                    String word = words.get(j).get(i);
                    if (!word.isEmpty()) {
                        line.append(word);
                        line.append((j == rows - 1) ? "" : " ");
                    }
                }
                out.write(line.toString());
                out.newLine();
            }
        }
    }
}
