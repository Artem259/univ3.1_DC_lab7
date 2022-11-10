import java.sql.*;

public class Database {
    private final Connection connection;

    public Database(Connection connection) {
        this.connection = connection;
    }

    private int execAdd(String sql) {
        try {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private boolean execUpdateDelete(String sql) {
        try {
            Statement stm = connection.createStatement();
            stm.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean clear() {
        String sql = "DELETE FROM singer";
        return execUpdateDelete(sql);
    }

    // 1
    public int addSinger(Singer singer) {
        String sql;
        if (singer.getId() != null) {
            sql = "INSERT INTO singer (id, name) " +
                    "VALUES (" + singer.getId() + ", '" + singer.getName() + "')";
        }
        else {
            sql = "INSERT INTO singer (name) " +
                    "VALUES ('" + singer.getName() + "')";
        }
        return execAdd(sql);
    }

    // 2
    public boolean deleteSingerById(int id) {
        String sql = "DELETE FROM singer " +
                "WHERE id = " + id;
        return execUpdateDelete(sql);
    }

    // 3
    public int addAlbum(int singerId, Album album) {
        String sql;
        if (album.getId() != null) {
            sql = "INSERT INTO album (id, singer_id, name, year, genre) " +
                    "VALUES (" + album.getId() + ", " + singerId + ", '" +
                    album.getName() + "', " + album.getYear() + ", '" +
                    album.getGenre() + "')";
        }
        else {
            sql = "INSERT INTO album (singer_id, name, year, genre) " +
                    "VALUES (" + singerId + ", '" + album.getName() + "', " +
                    album.getYear() + ", '" + album.getGenre() + "')";
        }
        return execAdd(sql);
    }

    // 4
    public boolean deleteAlbumById(int id) {
        String sql = "DELETE FROM album " +
                "WHERE id = " + id;
        return execUpdateDelete(sql);
    }

    // 5
    public boolean updateSinger(Singer singer) {
        String sql = "UPDATE singer SET name = '" + singer.getName() +
                "' where id = " + singer.getId();
        return execUpdateDelete(sql);
    }

    public boolean updateAlbum(Album album) {
        String sql = "UPDATE album SET name = '" + album.getName() + "', year = " +
                album.getYear() + ", genre = '" + album.getGenre() +  "' " +
                "where id = " + album.getId();
        return execUpdateDelete(sql);
    }
/*
    // 6
    public Integer countAlbumsOfSingerById(int id) {

    }

    // 7
    public List<Album> getAlbumsCopy() {

    }

    // 8
    public List<Album> getAlbumsOfSingerById(int id) {

    }

    // 9
    public List<Singer> getSingersCopy() {

    }*/
}
