import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    private final Collection collection;
    private final File xmlFile;
    private final File dtdFile;
    private final Connection connection;

    public Application(File dtdFile, Connection connection) {
        this.collection = new Collection();
        this.xmlFile = new File("files", "xml_file.xml");
        this.dtdFile = dtdFile;
        this.connection = connection;
    }

    public void addSinger(String name) {
        collection.addSinger(new Singer(collection.getNextSingerId(),name));
    }

    public void addAlbumToPrevSinger(String name, Integer year, String genre) {
        Singer singer = collection.getSingerById(collection.getNextSingerId() - 1);
        collection.addAlbum(new Album(collection.getNextAlbumId(), singer, name, year, genre));
    }

    public void fillCollection1() {
        addSinger("singer_0");
        addAlbumToPrevSinger("album_0_0", 2012, "Metal0");
        addAlbumToPrevSinger("album_0_1", 2014, "Pop0");
        addAlbumToPrevSinger("album_0_2", 2018, "Rock0");

        addSinger("singer_1");
        addAlbumToPrevSinger("album_1_0", 2112, "Metal1");
        addAlbumToPrevSinger("album_1_1", 2114, "Pop1");
        addAlbumToPrevSinger("album_1_2", 2118, "Rock1");

        addSinger("singer_2");

        addSinger("singer_3");
        addAlbumToPrevSinger("album_3_0", 2312, "Metal3");
        addAlbumToPrevSinger("album_3_1", 2314, "Pop3");
    }

    public void test1() {
        fillCollection1();
        System.out.println(collection.toXmlFormattedString(dtdFile));
        collection.toXmlFile(xmlFile, dtdFile);
    }

    public void test2() {
        fillCollection1();
        String str1 = collection.toXmlString(dtdFile);

        for (int i=0; i<100; i++) {
            collection.toXmlFile(xmlFile, dtdFile);
            collection.clear();
            collection.fromXmlFile(xmlFile);
        }

        String str2 = collection.toXmlString(dtdFile);
        if (!str1.equals(str2)) {
            throw new RuntimeException();
        }
    }

    public void test3() {
        fillCollection1();

        Database db = new Database(connection);
        db.clear();

        int s1 = db.addSinger(new Singer(null, "s1"));
        int a1_1 = db.addAlbum(s1, new Album(null, null, "a1_1", 1000, "genre"));
        int a1_2 = db.addAlbum(s1, new Album(null, null, "a1_2", 1000, "genre"));

        int s2 = db.addSinger(new Singer(null, "s2"));
        int a2_1 = db.addAlbum(s2, new Album(null, null, "a2_1", 1000, "genre"));
        int a2_2 = db.addAlbum(s2, new Album(null, null, "a2_2", 1000, "genre"));

        boolean b;
        b = db.deleteAlbumById(a2_2);
        b = b && db.deleteSingerById(s1);

        b = b && db.updateSinger(new Singer(s2, "s2_new"));
        b = b && db.updateAlbum(new Album(a2_1, null, "a2_1_new", 2000, "genre_new"));

        if (!b) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        try {
            String url = "Jdbc:mysql://localhost:3306/store";
            Connection connection = DriverManager.getConnection(url, "root", "mypassword");
            new Application(new File("files", "dtd_file.dtd"), connection).test3();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
