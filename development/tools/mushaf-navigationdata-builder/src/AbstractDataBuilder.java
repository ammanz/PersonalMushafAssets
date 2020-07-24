import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDataBuilder {
    protected static final int GLYPH_ID_INDEX = 0;
    protected static final int PAGE_NUMBER_INDEX = 1;
    protected static final int LINE_NUMBER_INDEX = 2;
    protected static final int SURAH_NUMBER_INDEX = 3;
    protected static final int AYAH_NUMBER_INDEX = 4;
    protected static final int POSITION_INDEX = 5;
    protected static final int MIN_X_INDEX = 6;
    protected static final int MAX_X_INDEX = 7;
    protected static final int MIN_Y_INDEX = 8;
    protected static final int MAX_Y_INDEX = 9;
    protected MushafMetadata mushafMetadata;
    protected String url;

    protected static final int[] HAFS_SURAH_LENGTHS = {
            7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111,
            43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77,
            227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75,
            85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55,
            78, 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52,
            44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25,
            22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11,
            11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6};

    public AbstractDataBuilder(MushafMetadata mushafMetadata) {
        this.mushafMetadata = mushafMetadata;
        this.url = "jdbc:sqlite:/home/ammanz/Projects/Qur'an/production-files/personal_mushaf" + mushafMetadata.getPath();
    }

    protected int getPageNumber(Connection connection, int surah, int ayah) {
        String sql = "SELECT page_number FROM glyphs WHERE sura_number = " + surah + " AND ayah_number = " + ayah;
        int pageNumber = 0;
        try {
            ResultSet rs = connection.createStatement().executeQuery(sql);
            rs.next();
            pageNumber = rs.getInt("page_number");
            //System.out.println(pageNumber);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return pageNumber;
    }

    protected Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    protected void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    protected String getFormattedArrayString(Object[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        for (int i = 0; i < array.length - 1; i++) {
            builder.append(array[i].toString());
            builder.append(", ");
            /*if (i % 24 == 0 && i != 0)
                builder.append("\n\t");*/
        }

        builder.append(array[array.length - 1].toString());
        builder.append("}, ");

        return builder.toString();
    }

    protected List<List<Integer>> getInfo(Connection connection, String sql) {
        List<List<Integer>> results = new ArrayList<>();
        List<Integer> result;
        try {
            ResultSet rs = connection.createStatement().executeQuery(sql);
            while(rs.next()) {
                result = new ArrayList<>();
                result.add(rs.getInt("glyph_id"));
                result.add(rs.getInt("page_number"));
                result.add(rs.getInt("line_number"));
                result.add(rs.getInt("sura_number"));
                result.add(rs.getInt("ayah_number"));
                result.add(rs.getInt("position"));
                result.add(rs.getInt("min_x"));
                result.add(rs.getInt("max_x"));
                result.add(rs.getInt("min_y"));
                result.add(rs.getInt("max_y"));
                results.add(result);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    protected List<List<Integer>> getAyahInfo(Connection connection, int surah, int ayah) {
        String sql = "SELECT glyph_id, page_number, line_number, sura_number, ayah_number, position, min_x, max_x, min_y, max_y FROM glyphs WHERE sura_number = " + surah + " AND ayah_number = " + ayah;
        return getInfo(connection, sql);
    }

    protected List<List<Integer>> getLineInfo(Connection connection, int pageNumber, int lineNumber) {
        String sql = "SELECT glyph_id, page_number, line_number, sura_number, ayah_number, position, min_x, max_x, min_y, max_y FROM glyphs WHERE page_number = " + pageNumber + " AND line_number = " + lineNumber;
        return getInfo(connection, sql);
    }
}
