public class Album {
    private final int id;
    private final Singer singer;
    private String name;
    private int year; // released
    private String genre;

    public Album(int id, Singer singer, String name, int year, String genre) {
        this.id = id;
        this.singer = singer;
        this.name = name;
        this.year = year;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public Singer getSinger() {
        return singer;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
