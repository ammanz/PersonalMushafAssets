import java.sql.Connection;
import java.util.List;

public class RukuLengthBuilder extends LengthBuilder {
    private int[][][] source;

    public RukuLengthBuilder(int[][][] source, MushafMetadata mushafMetadata) {
        super(mushafMetadata);
        this.source = source;
    }

    public void printLengths() {
        System.out.println("rukuLengths = new double[][]{");

        Double[][] rukuLengths = new Double[source.length][40];

        Connection connection = connect();

        rukuLengths[0][0] = getFirstRukuLength(connection);
        rukuLengths[0][1] = getSecondRukuLength(connection);

        for (int j = 2; j < source[0].length; j++) {
            rukuLengths[0][j] = getRukuLength(connection, 0, j);
        }

        for (int i = 1; i < source.length - 1; i++) {
            for (int j = 0; j < source[i].length ; j++) {
                rukuLengths[i][j] = getRukuLength(connection, i, j);
            }
        }

        for (int j = 0; j < source[source.length - 1].length - 1; j++) {
            rukuLengths[source.length - 1][j] = getRukuLength(connection, source.length - 1, j);
        }

        rukuLengths[source.length - 1][source[source.length - 1].length - 1] = getLastRukuLength(connection);

        for (int i = 0; i < source.length; i++) {
            System.out.println(getFormattedArrayString(rukuLengths[i], i));
        }

        System.out.println("};\n");

        closeConnection(connection);
    }

    private double getRukuLength(Connection connection, int juzNumber, int index) {
        List<Integer> firstGlyphOfRukuInfo = getFirstAyahOfRukuInfo(connection, juzNumber, index).get(0);
        List<Integer> lastGlyphOfRukuInfo = index != source[juzNumber].length - 1 ? getLastGlyphOfRukuInfo(connection, getFirstAyahOfRukuInfo(connection, juzNumber, index + 1)) :
                                                                                    getLastGlyphOfRukuInfo(connection, getFirstAyahOfRukuInfo(connection, juzNumber + 1, 0));
        return getLength(firstGlyphOfRukuInfo, lastGlyphOfRukuInfo);
    }

    private List<Integer> getLastGlyphOfRukuInfo(Connection connection, List<List<Integer>> firstAyahOfNextRukuInfo) {
        List<List<Integer>> lastAyahOfRukuInfo = getPreviousAyahInfo(connection, firstAyahOfNextRukuInfo.get(0).get(SURAH_NUMBER_INDEX), firstAyahOfNextRukuInfo.get(0).get(AYAH_NUMBER_INDEX));
        return lastAyahOfRukuInfo.get(lastAyahOfRukuInfo.size() - 1);
    }

    private List<List<Integer>> getFirstAyahOfRukuInfo(Connection connection, int juzNumber, int index) {
        int surah = source[juzNumber][index][0];
        int ayah  = source[juzNumber][index][1];
        return getAyahInfo(connection, surah, ayah);
    }

    private double getFirstRukuLength(Connection connection) {
        return getLengthOfFirstPageOfSurah(connection, 1) / numOfLines;
    }

    private double getSecondRukuLength(Connection connection) {
        List<Integer> firstGlyphOfRukuInfo = getFirstGlyphInfoOfFirstFullPage(connection);
        List<Integer> lastGlyphOfRukuInfo = getLastGlyphOfRukuInfo(connection, getFirstAyahOfRukuInfo(connection, 0, 2));
        return getLength(firstGlyphOfRukuInfo, lastGlyphOfRukuInfo) + getLengthOfFirstPageOfSurah(connection, 2) / numOfLines;
    }

    private double getLastRukuLength(Connection connection) {
        List<Integer> firstGlyphOfRuku = getAyahInfo(connection, 114, 1).get(0);
        List<List<Integer>> lastAyahOfRuku = getAyahInfo(connection, 114, HAFS_SURAH_LENGTHS[113]);
        List<Integer> lastGlyphOfRuku = lastAyahOfRuku.get(lastAyahOfRuku.size() - 1);
        return getLength(firstGlyphOfRuku, lastGlyphOfRuku);
    }

    protected String getFormattedArrayString(Double[] array, int juzNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t{");
        for (int i = 0; i < source[juzNumber].length - 1; i++) {
            stringBuilder.append(String.format("%.12f", array[i]));
            stringBuilder.append(",\t");
        }
        stringBuilder.append(String.format("%.12f", array[source[juzNumber].length - 1]));
        stringBuilder.append("},");
        return stringBuilder.toString();
    }
}
