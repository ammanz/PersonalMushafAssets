import java.sql.Connection;
import java.util.List;

public class SurahLengthBuilder extends LengthBuilder {

    public SurahLengthBuilder(MushafMetadata mushafMetadata) {
        super(mushafMetadata);
    }

    public void printLengths() {
        System.out.println("surahLengths = new double[]{");

        Double[] surahLengths = new Double[114];

        Connection connection = connect();

        surahLengths[0] = getFirstSurahLength(connection);

        for (int i = 1; i < 114; i++) {
            surahLengths[i] = getSurahLength(connection, i + 1);
        }


        System.out.println(getFormattedArrayString(surahLengths));
        System.out.println("};\n");

        closeConnection(connection);
    }

    private double getSurahLength(Connection connection, int surah) {
        List<Integer> firstGlyphOfSurahInfo = getFirstAyahOfSurahInfo(connection, surah).get(0);
        List<Integer> lastGlyphOfSurahInfo = getLastGlyphOfSurahInfo(connection, surah);
        return getLength(firstGlyphOfSurahInfo, lastGlyphOfSurahInfo);
    }

    private List<Integer> getLastGlyphOfSurahInfo(Connection connection, int surah) {
        List<List<Integer>> lastAyahOfSurahInfo = getAyahInfo(connection, surah, HAFS_SURAH_LENGTHS[surah - 1]);
        return lastAyahOfSurahInfo.get(lastAyahOfSurahInfo.size() - 1);
    }

    private List<List<Integer>> getFirstAyahOfSurahInfo(Connection connection, int surah) {
        return getAyahInfo(connection, surah, 1);
    }

    private double getFirstSurahLength(Connection connection) {
        return getLengthOfFirstPageOfSurah(connection, 1) / numOfLines;
    }


    protected String getFormattedArrayString(Double[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t");
        for (int i = 0; i < 114; i++) {
            stringBuilder.append(String.format("%.12f", array[i]));
            stringBuilder.append(", ");
            if (i % 24 == 0 && i != 0)
                stringBuilder.append("\n\t");
        }

        return stringBuilder.toString();
    }

}
