import java.io.File;

public class Application {
    private final Collection collection;
    private final File xmlFile;
    private final File dtdFile;

    public Application(File dtdFile) {
        this.collection = new Collection();
        this.xmlFile = new File("files", "xml_file.xml");
        this.dtdFile = dtdFile;
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

    public static void main(String[] args) {
        new Application(new File("files", "dtd_file.dtd")).test2();
    }
}
