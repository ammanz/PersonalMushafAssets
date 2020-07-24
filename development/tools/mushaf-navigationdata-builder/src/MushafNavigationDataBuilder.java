public class MushafNavigationDataBuilder {
    public static String NASKH13_PATH = "/modern_naskh_13_line/databases/ayahinfo_modernnaskh13line.db";
    public static double NASKH13_LINES = 13f;
    public static double NASKH13_LENGTH_OF_LINE = 1198f;
    public static double NASKH13_NUM_OF_LINES_TO_SKIP_PER_SURAH = 2f;

    public static String NASKH15_PATH = "/classic_naskh_15_line/databases/ayahinfo_classicnaskh15line.db";
    public static double NASKH15_LINES = 15f;
    public static double NASKH15_LENGTH_OF_LINE = 1152f;
    public static double NASKH15_NUM_OF_LINES_TO_SKIP_PER_SURAH = 2f;

    public static String MADANI15_PATH = "/classic_madani_15_line/databases/ayahinfo_classicmadani15line.db";
    public static double MADANI15_LINES = 15f;
    public static double MADANI15_LENGTH_OF_LINE = 1260f;
    public static double MADANI15_NUM_OF_LINES_TO_SKIP_PER_SURAH = 2f;

    public static void main(String[] args) {
        MushafMetadata naskh13Metadata = new MushafMetadata(NASKH13_PATH, NASKH13_LINES, NASKH13_LENGTH_OF_LINE,
                                        NASKH13_NUM_OF_LINES_TO_SKIP_PER_SURAH, NavigationData.naskhJuzLocations);

        MushafMetadata naskh15Metadata = new MushafMetadata(NASKH15_PATH, NASKH15_LINES, NASKH15_LENGTH_OF_LINE,
                NASKH15_NUM_OF_LINES_TO_SKIP_PER_SURAH, NavigationData.naskhJuzLocations);

        MushafMetadata madani15Metadata = new MushafMetadata(MADANI15_PATH, MADANI15_LINES, MADANI15_LENGTH_OF_LINE,
                                        MADANI15_NUM_OF_LINES_TO_SKIP_PER_SURAH, NavigationData.madaniJuzLocations);

        printMushafNavigationData(naskh15Metadata);

    }

    private static void printMushafNavigationData(MushafMetadata mushafMetadata) {
        printLandmarkPageNumbersForMushaf(mushafMetadata);
        printLengthsForMushaf(mushafMetadata);
    }

    private static void printLandmarkPageNumbersForMushaf(MushafMetadata mushafMetadata) {
        NavigationDataBuilder navigationDataBuilder = new NavigationDataBuilder(mushafMetadata);
        navigationDataBuilder.printPageNumbers();
    }

    private static void printLengthsForMushaf(MushafMetadata mushafMetadata) {
        QuarterLengthBuilder quarterLengthBuilder = new QuarterLengthBuilder(NavigationData.quarterInfo, mushafMetadata, "quarter");
        QuarterLengthBuilder hizbLengthBuilder = new QuarterLengthBuilder(NavigationData.hizbInfo, mushafMetadata, "hizb");
        RukuLengthBuilder rukuLengthBuilder = new RukuLengthBuilder(NavigationData.rukuInfo, mushafMetadata);
        SurahLengthBuilder surahLengthBuilder = new SurahLengthBuilder(mushafMetadata);
        JuzLengthBuilder juzLengthBuilder = new JuzLengthBuilder(mushafMetadata);

        juzLengthBuilder.printLengths();
        surahLengthBuilder.printLengths();
        quarterLengthBuilder.printLengths();
        hizbLengthBuilder.printLengths();
        rukuLengthBuilder.printLengths();
    }


}
