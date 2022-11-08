public class Application {
    private final Collection collection;
    private final String dtdPath;

    public Application(String dtdPath) {
        collection = new Collection();
        this.dtdPath = dtdPath;
    }

    public void addSinger(String name) {
        collection.addSinger(new Singer(collection.getNextSingerId(),name));
    }

    public void addAlbumToPrevSinger(String name, Integer year, String genre) {
        Singer singer = collection.getSingerById(collection.getNextSingerId() - 1);
        collection.addAlbum(new Album(collection.getNextAlbumId(), singer, name, year, genre));
    }

    public void test() {
        addSinger("singer_0");
        addAlbumToPrevSinger("album_0_0", 2012, "Metal0");
        addAlbumToPrevSinger("album_0_1", 2014, "Pop0");
        addAlbumToPrevSinger("album_0_2", 2018, "Rock0");

        addSinger("singer_1");
        addAlbumToPrevSinger("album_1_0", 2112, "Metal1");
        addAlbumToPrevSinger("album_1_1", 2114, "Pop1");
        addAlbumToPrevSinger("album_1_2", 2118, "Rock1");

        addSinger("singer_2");
        addAlbumToPrevSinger("album_2_0", 2212, "Metal2");
        addAlbumToPrevSinger("album_2_1", 2214, "Pop2");
        addAlbumToPrevSinger("album_2_2", 2218, "Rock2");

        System.out.println(collection.toXmlFormattedString(dtdPath));
        collection.toXmlFile("files/xml_file.xml", dtdPath);
    }

    public static void main(String[] args) {
        new Application("files/dtd_file.dtd").test();
    }
}
