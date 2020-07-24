public class MushafMetadata {
    private String path;
    private double numOfLines;
    private double lengthOfLine;
    private double numOfLinesToSkipPerSurah;
    private int[][] juzLocations;

    public MushafMetadata(String path, double numOfLines, double lengthOfLine, double numOfLinesToSkipPerSurah, int[][] juzLocations) {
        this.path = path;
        this.numOfLines = numOfLines;
        this.lengthOfLine = lengthOfLine;
        this.numOfLinesToSkipPerSurah = numOfLinesToSkipPerSurah;
        this.juzLocations = juzLocations;
    }

    public String getPath() {
        return path;
    }

    public double getNumOfLines() {
        return numOfLines;
    }

    public double getLengthOfLine() {
        return lengthOfLine;
    }

    public double getNumOfLinesToSkipPerSurah() {
        return numOfLinesToSkipPerSurah;
    }

    public int[][] getJuzLocations() {
        return juzLocations;
    }
}
