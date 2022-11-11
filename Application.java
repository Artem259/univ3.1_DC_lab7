import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    private final Collection collection;
    private final File xmlFile;
    private final File dtdFile;
    private final Database db;

    public Application(File dtdFile, Connection connection) {
        this.collection = new Collection();
        this.xmlFile = new File("files", "xml_file.xml");
        this.dtdFile = dtdFile;
        this.db = new Database(connection);
    }

    public void addSinger(String name) {
        collection.addSinger(new Singer(collection.getNextSingerId(),name));
    }

    public void addAlbumToPrevSinger(String name, Integer year, String genre) {
        Singer singer = collection.getSingerById(collection.getNextSingerId() - 1);
        collection.addAlbum(new Album(collection.getNextAlbumId(), singer, name, year, genre));
    }

    public void fillCollection1() {
        addSinger("singer_1");
        addAlbumToPrevSinger("album_1_1", 2011, "Metal1");
        addAlbumToPrevSinger("album_1_2", 2012, "Pop1");
        addAlbumToPrevSinger("album_1_3", 2013, "Rock1");

        addSinger("singer_2");
        addAlbumToPrevSinger("album_2_1", 2021, "Metal2");
        addAlbumToPrevSinger("album_2_2", 2022, "Pop2");
        addAlbumToPrevSinger("album_2_3", 2023, "Rock2");

        addSinger("singer_3");

        addSinger("singer_4");
        addAlbumToPrevSinger("album_4_1", 2041, "Metal4");
        addAlbumToPrevSinger("album_4_2", 2042, "Pop4");
    }

    public boolean fillDatabase(Collection collection) {
        db.clear();
        return db.addCollection(collection);
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
        if (!fillDatabase(collection)) {
            throw new RuntimeException();
        }

        Collection c = db.getAll();
        System.out.println(c.toXmlFormattedString(dtdFile));
        if (!c.toXmlString(dtdFile).equals(collection.toXmlString(dtdFile))) {
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
