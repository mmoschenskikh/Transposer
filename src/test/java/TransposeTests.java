import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransposeTests {

    String first_simple = "ПРОСТО ПРОСТО С СИН ПРОВЕРЬТЕ\n" +
            "ЗДРАВСТВУЙ КАК НО ТЕЗ КОД\n" +
            "ДЕЛА В СУМ ПОЖАЛУЙСТА\n" +
            "ЫМ МА\n" +
            "ГО ТО\n" +
            "Д РА\n" +
            "ОМ";
    String second_simple = "СТУ ДЕН ЧЕС КАЯ\n" +
            "ПЕРС ПЕК ТИВА";

    String first_a7 = "ПРОСТО  ПРОСТО  С       СИН     ПРОВЕРЬТЕ\n" +
            "ЗДРАВСТВУЙ КАК     НО      ТЕЗ     КОД    \n" +
            "ДЕЛА    В       СУМ     ПОЖАЛУЙСТА\n" +
            "ЫМ      МА     \n" +
            "ГО      ТО     \n" +
            "Д       РА     \n" +
            "ОМ     ";
    String second_a5 = "СТУ   ДЕН   ЧЕС   КАЯ  \n" +
            "ПЕРС  ПЕК   ТИВА ";

    String first_r = "    ПРОСТО     ПРОСТО          С        СИН  ПРОВЕРЬТЕ\n" +
            "ЗДРАВСТВУЙ        КАК         НО        ТЕЗ        КОД\n" +
            "      ДЕЛА          В        СУМ ПОЖАЛУЙСТА\n" +
            "        ЫМ         МА\n" +
            "        ГО         ТО\n" +
            "         Д         РА\n" +
            "        ОМ";
    String second_r = "       СТУ        ДЕН        ЧЕС        КАЯ\n" +
            "      ПЕРС        ПЕК       ТИВА";

    private boolean equals(String expected, File actual) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(actual));
        BufferedReader string = new BufferedReader(new StringReader(expected));
        try (file) {
            String fileLine = file.readLine();
            String stringLine = string.readLine();
            while (fileLine != null) {
                if (!fileLine.equals(stringLine)) {
                    System.out.println("Expected line: " + stringLine);
                    System.out.println("  Actual line: " + fileLine);
                    return false;
                }
                fileLine = file.readLine();
                stringLine = string.readLine();
            }
        }
        return true;
    }

    private void assertEquals(String expected, File actual) throws IOException {
        assertTrue(equals(expected, actual));
    }

    @Test
    public void simplyTranspose() throws IOException {
        Transpose.main(new String[]{"txt/first.txt", "-o", "txt/first_done.txt"});
        File first_done = new File("txt/first_done.txt");
        Transpose.main(new String[]{"txt/second.txt", "-o", "txt/second_done.txt"});
        File second_done = new File("txt/second_done.txt");

        assertEquals(first_simple, first_done);
        assertEquals(second_simple, second_done);

        first_done.delete();
        second_done.delete();
    }

    @Test
    public void fieldWidth() throws IOException {
        Transpose.main(new String[]{"-a", "7", "txt/first.txt", "-o", "txt/first_done.txt"});
        File first_done = new File("txt/first_done.txt");
        Transpose.main(new String[]{"-a", "5", "txt/second.txt", "-o", "txt/second_done.txt"});
        File second_done = new File("txt/second_done.txt");

        assertEquals(first_a7, first_done);
        assertEquals(second_a5, second_done);

        first_done.delete();
        second_done.delete();
    }

    @Test
    public void alignRight() throws IOException {
        Transpose.main(new String[]{"-r", "txt/first.txt", "-o", "txt/first_done.txt"});
        File first_done = new File("txt/first_done.txt");
        Transpose.main(new String[]{"-r", "txt/second.txt", "-o", "txt/second_done.txt"});
        File second_done = new File("txt/second_done.txt");

        assertEquals(first_r, first_done);
        assertEquals(second_r, second_done);

        first_done.delete();
        second_done.delete();
    }
}
