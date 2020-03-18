import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransposeTests {
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

    private void transposeTest(String argsFirst, String argsSecond, String expectedFirst, String expectedSecond) throws IOException {
        argsFirst = argsFirst + " txt/first.txt -o txt/first_done.txt";
        argsSecond = argsSecond + " txt/second.txt -o txt/second_done.txt";
        Transpose.main(argsFirst.trim().split(" "));
        File first_done = new File("txt/first_done.txt");
        Transpose.main(argsSecond.trim().split(" "));
        File second_done = new File("txt/second_done.txt");

        assertEquals(expectedFirst, first_done);
        assertEquals(expectedSecond, second_done);

        first_done.delete();
        second_done.delete();
    }

    @Test
    public void simplyTranspose() throws IOException {
        final String first_simple = "ПРОСТО ПРОСТО СИНТЕЗ ПРОВЕРЬТЕ БРОНЕТРАНСПОРТЁР ТРАНКВИЛИЗАТОР БЕ С ГИТ\n" +
                "ЗДРАВСТВУЙ КАК СУММАТОРА КОД ЭЛЕКТРОСТАНЦИЯ КОРОНАВИРУС ДЫ БА ЛУЧШАЯ\n" +
                "ДЕЛА ПОЖАЛУЙСТА КВАТЕРНИОН ШКОЙ СИСТЕМА\n" +
                "КОНТРОЛЯ\n" +
                "ВЕРСИЙ";
        final String second_simple = "СТУ ДЕН ЧЕС КАЯ\n" +
                "ПЕРС ПЕК ТИВА";
        transposeTest("", "", first_simple, second_simple);
    }

    @Test
    public void fieldWidth() throws IOException {
        final String first_a7 = "ПРОСТО  ПРОСТО  СИНТЕЗ  ПРОВЕРЬТЕ БРОНЕТРАНСПОРТЁР ТРАНКВИЛИЗАТОР БЕ      С       ГИТ    \n" +
                "ЗДРАВСТВУЙ КАК     СУММАТОРА КОД     ЭЛЕКТРОСТАНЦИЯ КОРОНАВИРУС ДЫ      БА      ЛУЧШАЯ \n" +
                "ДЕЛА    ПОЖАЛУЙСТА КВАТЕРНИОН ШКОЙ    СИСТЕМА\n" +
                "КОНТРОЛЯ\n" +
                "ВЕРСИЙ ";
        final String second_a5 = "СТУ   ДЕН   ЧЕС   КАЯ  \n" +
                "ПЕРС  ПЕК   ТИВА ";
        transposeTest("-a 7", "-a 5", first_a7, second_a5);
    }

    @Test
    public void alignRight() throws IOException {
        final String first_r = "    ПРОСТО     ПРОСТО     СИНТЕЗ  ПРОВЕРЬТЕ БРОНЕТРАНСПОРТЁР ТРАНКВИЛИЗАТОР         БЕ          С        ГИТ\n" +
                "ЗДРАВСТВУЙ        КАК  СУММАТОРА        КОД ЭЛЕКТРОСТАНЦИЯ КОРОНАВИРУС         ДЫ         БА     ЛУЧШАЯ\n" +
                "      ДЕЛА ПОЖАЛУЙСТА КВАТЕРНИОН       ШКОЙ    СИСТЕМА\n" +
                "  КОНТРОЛЯ\n" +
                "    ВЕРСИЙ";
        final String second_r = "       СТУ        ДЕН        ЧЕС        КАЯ\n" +
                "      ПЕРС        ПЕК       ТИВА";
        transposeTest("-r", "-r", first_r, second_r);
    }

    @Test
    public void trim() throws IOException {
        final String first_t = "ПРОСТО     ПРОСТО     СИНТЕЗ     ПРОВЕРЬТЕ  БРОНЕТРАНС ТРАНКВИЛИЗ БЕ         С          ГИТ       \n" +
                "ЗДРАВСТВУЙ КАК        СУММАТОРА  КОД        ЭЛЕКТРОСТА КОРОНАВИРУ ДЫ         БА         ЛУЧШАЯ    \n" +
                "ДЕЛА       ПОЖАЛУЙСТА КВАТЕРНИОН ШКОЙ       СИСТЕМА   \n" +
                "КОНТРОЛЯ  \n" +
                "ВЕРСИЙ    ";
        final String second_t = "СТУ        ДЕН        ЧЕС        КАЯ       \n" +
                "ПЕРС       ПЕК        ТИВА      ";
        transposeTest("-t", "-t", first_t, second_t);
    }

    @Test
    public void fieldWidthAndAlignRight() throws IOException {
        final String first_a16_r = "          ПРОСТО           ПРОСТО           СИНТЕЗ        ПРОВЕРЬТЕ БРОНЕТРАНСПОРТЁР   ТРАНКВИЛИЗАТОР               БЕ                С              ГИТ\n" +
                "      ЗДРАВСТВУЙ              КАК        СУММАТОРА              КОД   ЭЛЕКТРОСТАНЦИЯ      КОРОНАВИРУС               ДЫ               БА           ЛУЧШАЯ\n" +
                "            ДЕЛА       ПОЖАЛУЙСТА       КВАТЕРНИОН             ШКОЙ          СИСТЕМА\n" +
                "        КОНТРОЛЯ\n" +
                "          ВЕРСИЙ";
        final String second_a6_r = "   СТУ    ДЕН    ЧЕС    КАЯ\n" +
                "  ПЕРС    ПЕК   ТИВА";
        transposeTest("-a 16 -r", "-a 6 -r", first_a16_r, second_a6_r);
    }

    @Test
    public void fieldWidthAndTrim() throws IOException {
        final String first_a7_t = "ПРОСТО  ПРОСТО  СИНТЕЗ  ПРОВЕРЬ БРОНЕТР ТРАНКВИ БЕ      С       ГИТ    \n" +
                "ЗДРАВСТ КАК     СУММАТО КОД     ЭЛЕКТРО КОРОНАВ ДЫ      БА      ЛУЧШАЯ \n" +
                "ДЕЛА    ПОЖАЛУЙ КВАТЕРН ШКОЙ    СИСТЕМА\n" +
                "КОНТРОЛ\n" +
                "ВЕРСИЙ ";
        final String second_a2_t = "СТ ДЕ ЧЕ КА\n" +
                "ПЕ ПЕ ТИ";
        transposeTest("-a 7 -t", "-a 2 -t", first_a7_t, second_a2_t);
    }

    @Test
    public void alignRightAndTrim() throws IOException {
        final String first_r_t = "    ПРОСТО     ПРОСТО     СИНТЕЗ  ПРОВЕРЬТЕ БРОНЕТРАНС ТРАНКВИЛИЗ         БЕ          С        ГИТ\n" +
                "ЗДРАВСТВУЙ        КАК  СУММАТОРА        КОД ЭЛЕКТРОСТА КОРОНАВИРУ         ДЫ         БА     ЛУЧШАЯ\n" +
                "      ДЕЛА ПОЖАЛУЙСТА КВАТЕРНИОН       ШКОЙ    СИСТЕМА\n" +
                "  КОНТРОЛЯ\n" +
                "    ВЕРСИЙ";
        final String second_r_t = "       СТУ        ДЕН        ЧЕС        КАЯ\n" +
                "      ПЕРС        ПЕК       ТИВА";
        transposeTest("-r -t", "-r -t", first_r_t, second_r_t);
    }

    @Test
    public void fieldWidthAndAlignRightAndTrim() throws IOException {
        final String first_a9_r_t = "   ПРОСТО    ПРОСТО    СИНТЕЗ ПРОВЕРЬТЕ БРОНЕТРАН ТРАНКВИЛИ        БЕ         С       ГИТ\n" +
                "ЗДРАВСТВУ       КАК СУММАТОРА       КОД ЭЛЕКТРОСТ КОРОНАВИР        ДЫ        БА    ЛУЧШАЯ\n" +
                "     ДЕЛА ПОЖАЛУЙСТ КВАТЕРНИО      ШКОЙ   СИСТЕМА\n" +
                " КОНТРОЛЯ\n" +
                "   ВЕРСИЙ";
        final String second_a4_r_t = " СТУ  ДЕН  ЧЕС  КАЯ\n" +
                "ПЕРС  ПЕК ТИВА";
        transposeTest("-a 9 -r -t", "-a 4 -t -r", first_a9_r_t, second_a4_r_t);
    }
}
