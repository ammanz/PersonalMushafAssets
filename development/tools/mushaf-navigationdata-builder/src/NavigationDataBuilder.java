import java.sql.Connection;

public class NavigationDataBuilder extends AbstractDataBuilder {

    public NavigationDataBuilder(MushafMetadata mushafMetadata) {
        super(mushafMetadata);
    }

    public void printPageNumbers() {

        printJuzPageNumbers();
        printSurahPageNumbers();
        printQuarterPageNumbers();
        printHizbPageNumbers();
        printRukuPageNumbers();

    }

    private void printRukuPageNumbers() {
        Integer[] pageNumbers;
        Connection connection = connect();

        System.out.println("rukuPageNumbers = new int[][]{");

        for (int i = 0; i < NavigationData.rukuInfo.length; i++) {
            pageNumbers = new Integer[NavigationData.rukuInfo[i].length];
            for (int j = 0; j < NavigationData.rukuInfo[i].length; j++) {
                pageNumbers[j] = getPageNumber(connection, NavigationData.rukuInfo[i][j][0], NavigationData.rukuInfo[i][j][1]);
            }
            System.out.println("\t" + getFormattedArrayString(pageNumbers));
        }

        System.out.println("};\n");

    }




    private void printQuarterPageNumbers() {
        int[] pageNumbers = new int[NavigationData.quarterInfo.length];
        Connection connection = connect();

        for (int i = 0; i < NavigationData.quarterInfo.length; i++) {
            pageNumbers[i] = getPageNumber(connection, NavigationData.quarterInfo[i][1], NavigationData.quarterInfo[i][2]);
        }

        System.out.println("quarterPageNumbers = new int[]{");
        System.out.println("\t" + getLineBrokenFormattedArrayString(pageNumbers));
    }

    private void printHizbPageNumbers() {
        int[] pageNumbers = new int[NavigationData.hizbInfo.length];
        Connection connection = connect();

        for (int i = 0; i < NavigationData.hizbInfo.length; i++) {
            pageNumbers[i] = getPageNumber(connection, NavigationData.hizbInfo[i][1], NavigationData.hizbInfo[i][2]);
        }

        System.out.println("hizbPageNumbers = new int[]{");
        System.out.println("\t" + getLineBrokenFormattedArrayString(pageNumbers));
    }



    private void printSurahPageNumbers() {
        int[] pageNumbers = new int[114];
        Connection connection = connect();

        for (int i = 0; i < pageNumbers.length; i++) {
            pageNumbers[i] = getPageNumber(connection, i + 1, 1);
        }

        System.out.println("surahPageNumbers = new int[]{");
        System.out.println("\t" + getLineBrokenFormattedArrayString(pageNumbers));
    }



    private void printJuzPageNumbers() {
        int[] pageNumbers = new int[30];
        Connection connection = connect();

        for (int i = 0; i < pageNumbers.length; i++) {
            pageNumbers[i] = getPageNumber(connection, mushafMetadata.getJuzLocations()[i][0], mushafMetadata.getJuzLocations()[i][1]);
        }

        System.out.println("juzPageNumbers = new int[]{");
        System.out.println("\t" + getLineBrokenFormattedArrayString(pageNumbers));
    }



    private void checkQuarterPageResults(int[] actual, int[][] expected) {
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] != expected[i][3]) {
                System.out.println("expected: " + expected[i][3] + " actual: " + actual[i]);
            }

        }
    }

    private void checkSurah(int[] actual, int[][] expected) {
        for (int i = 0; i < expected.length; i++) {
            if (actual[i] != expected[i][2])
                System.out.println("actual: " + actual[i] + "expected: " + expected[i][2]);
        }

        System.out.println("Success!");
    }

    private void checkRukuResults(int[] actual, int[][][] expected) {
        int n = 0;
        for (int i = 0; i < expected.length; i++)
            for (int j = 0; j < expected[i].length; j++) {
                if (actual[n] != expected[i][j][2]) {
                    System.out.println("expected: " + expected[i][j][2] + " actual: " + actual[n]);
                }
                n++;
            }

    }

    private void checkJuz(int[] actual, int[][] expected) {
        for (int i = 0; i < expected.length; i++) {
            if (actual[i] != expected[i][1])
                System.out.println("actual: " + actual[i] + " expected: " + expected[i][1]);
        }

        System.out.println("Success!");
    }

    private String getLineBrokenFormattedArrayString(int[] array) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < array.length - 1; i++) {
            builder.append(array[i]);
            builder.append(", ");
            if (i % 24 == 0 && i != 0)
                builder.append("\n\t");
        }

        builder.append(array[array.length - 1]);
        builder.append("\n};\n\n");

        return builder.toString();
    }
}
