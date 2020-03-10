import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;

public class Transpose {
    @Option(name = "-a", usage = "Width of a field for a word.")
    private int num = 10;

    @Option(name = "-t", usage = "Cuts the word if it doesn't fit in the size set by '-a'.")
    private boolean cutWords;

    @Option(name = "-r", usage = "Aligns the word to the right.")
    private boolean alignRight;

    @Option(name = "-o", usage = "Sets an output file, else STDOUT.")
    private String outputFile;

    @Argument(usage = "Sets an input file, else STDIN.")
    private String inputFileName;

    public static void main(String[] args) throws IOException {
        new Transpose().launch(args);
    }

    private void launch(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar transpose.jar [-a width] [-t] [-r] [-o outputFile] [inputFile]");
            parser.printUsage(System.err);
        }
    }
}
