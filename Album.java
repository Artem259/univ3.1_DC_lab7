public class Album {
    private final int id;
    private final Singer singer;
    private final String name;
    private final int year; // released
    private final String genre;

    public Album(int id, Singer singer, String name, int year, String genre) {
        this.id = id;
        this.singer = singer;
        this.name = name;
        this.year = year;
        this.genre = genre;
    }
}
