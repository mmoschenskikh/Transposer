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

    private boolean numSet = false;
    private int num = 10;

    @Option(name = "-a", usage = "Width of a field for a word.")
    private void setNum(int number) {
        num = number;
        numSet = true;
    }

    @Option(name = "-t", usage = "Cuts words if they don't fit in the size set by '-a'.")
    private boolean cutWords = false;

    @Option(name = "-r", usage = "Aligns words to the right.")
    private boolean alignRight = false;

    @Option(name = "-o", usage = "Sets an output file, else STDOUT.")
    private void setOutputFile(File file) throws IOException {
        if (file.exists() && file.canWrite() || file.createNewFile()) {
            outputFile = file;
        } else {
            throw new IOException("Can't write to the specified output file.");
        }
    }

    @Argument(usage = "Sets an input file, else STDIN.")
    private void setInputFile(File file) throws FileNotFoundException {
        if (file.exists() && file.isFile() && file.canRead()) {
            inputFile = file;
        } else {
            throw new FileNotFoundException("Can't read the specified input file.");
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

        writeWords(readWords(input), output);
    }

    private ArrayList<ArrayList<String>> readWords(BufferedReader in) throws IOException {
        if (num < 1) {
            throw new IllegalArgumentException("Incorrect value for '-a'. It must be >= 1.");
        }
        ArrayList<ArrayList<String>> words = new ArrayList<>();
        int maxLength = 0;
        try (in) {
            String line = in.readLine();
            while (line != null) {
                if (!line.isBlank()) {
                    ArrayList<String> row = new ArrayList<>(Arrays.asList(line.trim().split(" +")));
                    if (cutWords) {
                        cut(row, num);
                    }
                    if (alignRight) {
                        align(row, num);
                    } else if (numSet || cutWords) {
                        addSpace(row, num);
                    }
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
        if (words.isEmpty()) {
            throw new IOException("Input file is empty.");
        }
        return words;
    }

    private void writeWords(ArrayList<ArrayList<String>> words, BufferedWriter out) throws IOException {
        final int columns = words.get(0).size();
        try (out) {
            for (int i = 0; i < columns; i++) {
                StringBuilder line = new StringBuilder();
                for (ArrayList<String> strings : words) {
                    String word = strings.get(i);
                    if (!word.isEmpty()) {
                        line.append(word);
                        line.append(" ");
                    }
                }
                out.write(line.deleteCharAt(line.length() - 1).toString());
                out.newLine();
            }
        }
    }

    private void cut(ArrayList<String> row, int width) {
        for (int i = 0; i < row.size(); i++) {
            StringBuilder word = new StringBuilder().append(row.get(i));
            if (word.length() > width) {
                row.set(i, word.delete(width, word.length()).toString());
            }
        }
    }

    private void align(ArrayList<String> row, int width) {
        for (int i = 0; i < row.size(); i++) {
            StringBuilder word = new StringBuilder().append(row.get(i));
            if (word.length() < width) {
                row.set(i, word.insert(0, " ".repeat(width - word.length())).toString());
            }
        }
    }

    private void addSpace(ArrayList<String> row, int width) {
        for (int i = 0; i < row.size(); i++) {
            StringBuilder word = new StringBuilder().append(row.get(i));
            if (word.length() < width) {
                row.set(i, word.append(" ".repeat(width - word.length())).toString());
            }
        }
    }
}
