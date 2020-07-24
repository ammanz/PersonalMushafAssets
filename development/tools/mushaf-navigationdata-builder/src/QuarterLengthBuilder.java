import java.sql.Connection;
import java.util.List;

public class QuarterLengthBuilder extends LengthBuilder {
    private String quarterType;
    private int[][] source;

    public QuarterLengthBuilder(int[][] source, MushafMetadata mushafMetadata, String quarterType) {
        super(mushafMetadata);
        this.quarterType = quarterType;
        this.source = source;
    }

    public void printLengths() {

        System.out.print(quarterType + "Lengths = new double[]");

        Double[] quarterLengths = new Double[source.length];

        Connection connection = connect();

        quarterLengths[0] = getFirstQuarterLength(connection);

        for (int i = 1; i < quarterLengths.length - 1; i++)
            quarterLengths[i] = getQuarterLength(connection, i);

        quarterLengths[quarterLengths.length - 1] = getLastQuarterLength(connection);

        System.out.println(getFormattedArrayString(quarterLengths) + "\n");

        closeConnection(connection);
    }

    private double getQuarterLength(Connection connection, int index) {
        List<Integer> firstGlyphOfQuarterInfo = getFirstAyahOfQuarterInfo(connection, index).get(0);
        List<Integer> lastGlyphOfQuarterInfo = getLastGlyphOfQuarterInfo(connection, getFirstAyahOfQuarterInfo(connection, index + 1));
        return getLength(firstGlyphOfQuarterInfo, lastGlyphOfQuarterInfo);
    }

    private double getLastQuarterLength(Connection connection) {
        List<Integer> firstGlyphOfQuarterInfo = getFirstAyahOfQuarterInfo(connection, source.length - 1).get(0);
        List<List<Integer>> lastAyahOfQuarterInfo = getAyahInfo(connection, 114, 6);
        List<Integer> lastGlyphOfQuarterInfo = lastAyahOfQuarterInfo.get(lastAyahOfQuarterInfo.size() - 1);
        return getLength(firstGlyphOfQuarterInfo, lastGlyphOfQuarterInfo);
    }

    private double getLengthOfFirstPages(Connection connection, List<Integer> firstAyahOfQuarterInfo) {
        double lengthOfFirstTwoPages = 0d;
        if (firstAyahOfQuarterInfo.get(AbstractDataBuilder.SURAH_NUMBER_INDEX).equals(1)){
            lengthOfFirstTwoPages += getLengthOfFirstPageOfSurah(connection, 1);
        }

        lengthOfFirstTwoPages += getLengthOfFirstPageOfSurah(connection, 2);

        return lengthOfFirstTwoPages / numOfLines;
    }

    private double getFirstQuarterLength(Connection connection) {
        List<Integer> firstGlyphOfQuarterInfo = getFirstAyahOfQuarterInfo(connection, 0).get(0);
        double lengthOfFirstTwoPages = getLengthOfFirstPages(connection, firstGlyphOfQuarterInfo);
        firstGlyphOfQuarterInfo = getFirstGlyphInfoOfFirstFullPage(connection);
        List<Integer> lastAyahOfQuarterInfo = getLastGlyphOfQuarterInfo(connection, getFirstAyahOfQuarterInfo(connection, 1));
        return getLength(firstGlyphOfQuarterInfo, lastAyahOfQuarterInfo) + lengthOfFirstTwoPages;
    }

    private List<List<Integer>> getFirstAyahOfQuarterInfo(Connection connection, int index) {
        int surah = source[index][1];
        int ayah = source[index][2];
        return getAyahInfo(connection, surah, ayah);
    }

    private List<Integer> getLastGlyphOfQuarterInfo(Connection connection, List<List<Integer>> firstAyahOfNextQuarterInfo) {
        Integer nextQuarterSurah = firstAyahOfNextQuarterInfo.get(0).get(AbstractDataBuilder.SURAH_NUMBER_INDEX);
        Integer nextQuarterAyah = firstAyahOfNextQuarterInfo.get(0).get(AbstractDataBuilder.AYAH_NUMBER_INDEX);
        List<List<Integer>> lastAyahOfQuarterInfo = getPreviousAyahInfo(connection, nextQuarterSurah, nextQuarterAyah);
        return lastAyahOfQuarterInfo.get(lastAyahOfQuarterInfo.size() - 1);
    }

    private String getFormattedArrayString(Double[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n\t");

        for (int i = 0; i < array.length - 1; i++) {
            builder.append(String.format("%.12f", array[i]));
            builder.append(",\t");
            if (i % 24 == 0 && i != 0)
                builder.append("\n\t");
        }

        builder.append(String.format("%.12f", array[array.length - 1]));
        builder.append("\n}; ");

        return builder.toString();
    }


}
