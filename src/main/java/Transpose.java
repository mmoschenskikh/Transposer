import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Transpose {

    private File outputFile;
    private File inputFile;

    @Option(name = "-a", usage = "Width of a field for a word.")
    private int num = 10;

    @Option(name = "-t", usage = "Cuts the word if it doesn't fit in the size set by '-a'.")
    private boolean cutWords = false;

    @Option(name = "-r", usage = "Aligns the word to the right.")
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

    public static void main(String[] args) {
        new Transpose().launch(args);
    }

    private void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("transpose [-a width] [-t] [-r] [-o outputFile] [inputFile]");
            parser.printUsage(System.err);
        }
    }
}
