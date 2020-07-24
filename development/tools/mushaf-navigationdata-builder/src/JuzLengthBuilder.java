import java.sql.Connection;
import java.util.List;

public class JuzLengthBuilder extends LengthBuilder {
    private int[][] source;

    public JuzLengthBuilder(MushafMetadata mushafMetadata) {
        super(mushafMetadata);
        this.source = mushafMetadata.getJuzLocations();
    }

    public void printLengths() {
        System.out.println("juzLengths = new double[]{");

        Double[] juzLengths = new Double[30];

        Connection connection = connect();

        juzLengths[0] = getFirstJuzLength(connection);

        for (int i = 1; i < 29; i++) {
            juzLengths[i] = getJuzLength(connection, i);
        }

        juzLengths[29] = getLastJuzLength(connection);

        System.out.println(getFormattedArrayString(juzLengths));
        System.out.println("};\n");

        closeConnection(connection);
    }

    private double getJuzLength(Connection connection, int juzNumber) {
        List<Integer> firstGlyphOfJuzInfo = getFirstAyahOfJuzInfo(connection, juzNumber).get(0);
        List<Integer> lastGlyphOfJuzInfo = getLastGlyphOfJuzInfo(connection, juzNumber);
        if (juzNumber != 9)
            return getLength(firstGlyphOfJuzInfo, lastGlyphOfJuzInfo);
        else
            return getLength(firstGlyphOfJuzInfo, lastGlyphOfJuzInfo) + 1/numOfLines;
    }

    private List<Integer> getLastGlyphOfJuzInfo(Connection connection, int juzNumber) {
        List<List<Integer>> lastAyahOfJuzInfo = getPreviousAyahInfo(connection, source[juzNumber + 1][0], source[juzNumber + 1][1]);
        return lastAyahOfJuzInfo.get(lastAyahOfJuzInfo.size() - 1);
    }

    private List<List<Integer>> getFirstAyahOfJuzInfo(Connection connection, int juzNumber) {
        return getAyahInfo(connection, source[juzNumber][0], source[juzNumber][1]);
    }

    private double getFirstJuzLength(Connection connection) {
        return (getLengthOfFirstPageOfSurah(connection, 1) / numOfLines) + (getLengthOfFirstPageOfSurah(connection, 2) / numOfLines) +
                getLength(getFirstGlyphInfoOfFirstFullPage(connection), getLastGlyphOfJuzInfo(connection, 0));
    }

    private double getLastJuzLength(Connection connection) {
        List<Integer> firstGlyphOfJuzInfo = getFirstAyahOfJuzInfo(connection, 29).get(0);
        List<List<Integer>> lastAyahOfJuz = getAyahInfo(connection, 114, HAFS_SURAH_LENGTHS[113]);
        List<Integer> lastGlyphOfJuzInfo = lastAyahOfJuz.get(lastAyahOfJuz.size() - 1);
        return getLength(firstGlyphOfJuzInfo, lastGlyphOfJuzInfo);
    }


    protected String getFormattedArrayString(Double[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t");
        for (int i = 0; i < 30; i++) {
            stringBuilder.append(String.format("%.12f", array[i]));
            stringBuilder.append(", ");
            if (i % 24 == 0 && i != 0)
                stringBuilder.append("\n\t");
        }

        return stringBuilder.toString();
    }
}
