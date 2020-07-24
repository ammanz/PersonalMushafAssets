import java.sql.Connection;
import java.util.List;

public abstract class LengthBuilder extends AbstractDataBuilder {
    protected double numOfLines;
    protected double lengthOfLine;
    protected double numOfLinesToSkipPerSurah;

    protected LengthBuilder(MushafMetadata mushafMetadata) {
        super(mushafMetadata);
        this.numOfLines = mushafMetadata.getNumOfLines();
        this.lengthOfLine = mushafMetadata.getLengthOfLine();
        this.numOfLinesToSkipPerSurah = mushafMetadata.getNumOfLinesToSkipPerSurah();
    }

    protected double getLength(List<Integer> firstGlyphOfRangeInfo, List<Integer> lastGlyphOfRangeInfo) {
        int initialPageNumber = firstGlyphOfRangeInfo.get(AbstractDataBuilder.PAGE_NUMBER_INDEX);
        int finalPageNumber = lastGlyphOfRangeInfo.get(AbstractDataBuilder.PAGE_NUMBER_INDEX);

        if (initialPageNumber != finalPageNumber) {
            double numOfLinesOnInitialPage = (numOfLines - (firstGlyphOfRangeInfo.get(AbstractDataBuilder.LINE_NUMBER_INDEX))) + firstGlyphOfRangeInfo.get(AbstractDataBuilder.MAX_X_INDEX) / lengthOfLine;
            double numOfLinesOnFinalPage = (lastGlyphOfRangeInfo.get(AbstractDataBuilder.LINE_NUMBER_INDEX) - 1f) + (lengthOfLine - lastGlyphOfRangeInfo.get(AbstractDataBuilder.MIN_X_INDEX)) / lengthOfLine;
            double numOfLinesInIntermediatePages = (finalPageNumber - initialPageNumber - 1) * numOfLines;
            double numOfLinesToSkip = (lastGlyphOfRangeInfo.get(AbstractDataBuilder.SURAH_NUMBER_INDEX) - firstGlyphOfRangeInfo.get(AbstractDataBuilder.SURAH_NUMBER_INDEX)) * numOfLinesToSkipPerSurah;
            return (numOfLinesOnInitialPage + numOfLinesInIntermediatePages + numOfLinesOnFinalPage - numOfLinesToSkip) / numOfLines;
        } else {
            double firstLine = firstGlyphOfRangeInfo.get(AbstractDataBuilder.MAX_X_INDEX) / lengthOfLine;
            double lastLine = (lengthOfLine - lastGlyphOfRangeInfo.get(AbstractDataBuilder.MIN_X_INDEX)) / lengthOfLine;
            double numOfIntermediateLines = lastGlyphOfRangeInfo.get(AbstractDataBuilder.LINE_NUMBER_INDEX) - firstGlyphOfRangeInfo.get(AbstractDataBuilder.LINE_NUMBER_INDEX) - 1;
            return (firstLine + numOfIntermediateLines + lastLine) / numOfLines;
        }
    }

    protected double getLineLength(List<List<Integer>> lineInfo) {
        return lineInfo.get(0).get(AbstractDataBuilder.MAX_X_INDEX) - lineInfo.get(lineInfo.size() - 1).get(AbstractDataBuilder.MIN_X_INDEX);
    }

    protected double getLengthOfFirstPageOfSurah(Connection connection, int surah) {
        int pageNumber = getPageNumber(connection, surah, 1);
        double totalLength = 0;
        int firstLine = 1;
        List<List<Integer>> currentLine = getLineInfo(connection, pageNumber, 1);

        for (int i = 1; i <= numOfLines; i++) {
            currentLine = getLineInfo(connection, pageNumber, i);
            if (!currentLine.isEmpty()) {
                firstLine = i;
                break;
            }
        }
        for (int i = firstLine + 1; !currentLine.isEmpty(); i++) {
            totalLength += getLineLength(currentLine);
            currentLine = getLineInfo(connection, pageNumber, i);
        }
        return totalLength / lengthOfLine;
    }

    protected List<Integer> getFirstGlyphInfoOfFirstFullPage(Connection connection) {
        Integer surahBakarahFirstPage = getPageNumber(connection, 2, 1);
        List<List<Integer>> currentAyah;

        for (int i = 1; true; i++) {
            currentAyah = getAyahInfo(connection, 2, i);
            if (!currentAyah.get(0).get(AbstractDataBuilder.PAGE_NUMBER_INDEX).equals(surahBakarahFirstPage))
                break;
        }

        return currentAyah.get(0);
    }

    protected List<List<Integer>> getPreviousAyahInfo(Connection connection, int surah, int ayah) {
        if (ayah == 1)
            return getAyahInfo(connection, surah - 1, AbstractDataBuilder.HAFS_SURAH_LENGTHS[surah - 2]);
        else
            return getAyahInfo(connection, surah, ayah - 1);
    }
}
