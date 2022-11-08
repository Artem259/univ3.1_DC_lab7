import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Collection {
    private final List<Singer> singers;
    private final List<Album> albums;

    public Collection() {
        singers = new ArrayList<>();
        albums = new ArrayList<>();
    }

    private Integer indexOfSingerById(int id) {
        for (int i=0; i<singers.size(); i++) {
            if (singers.get(i).getId() == id) {
                return i;
            }
        }
        return null;
    }

    private Integer indexOfAlbumById(int id) {
        for (int i=0; i<albums.size(); i++) {
            if (albums.get(i).getId() == id) {
                return i;
            }
        }
        return null;
    }

    private List<Integer> indicesOfAlbumsOfSingerById(int id) {
        List<Integer> res = new ArrayList<>();
        for (int i=0; i<albums.size(); i++) {
            if (albums.get(i).getSinger().getId() == id) {
                res.add(i);
            }
        }
        return res;
    }

    private List<List<Album>> getSortedAlbums() {
        List<List<Album>> res = new ArrayList<>();
        for (int i=0; i<singers.size(); i++) {
            res.add(new ArrayList<>());
        }
        for (Album album : albums) {
            Integer index = indexOfSingerById(album.getSinger().getId());
            if (index == null) {
                throw new RuntimeException();
            }
            res.get(index).add(album);
        }
        return res;
    }

    // 5
    public Singer getSingerById(int id) {
        Integer i = indexOfSingerById(id);
        if (i == null) {
            return null;
        }
        return singers.get(i);
    }

    public Album getAlbumById(int id) {
        Integer i = indexOfAlbumById(id);
        if (i == null) {
            return null;
        }
        return albums.get(i);
    }

    public Integer getNextSingerId() {
        int res = -1;
        for (Singer singer : singers) {
            res = Math.max(res, singer.getId());
        }
        return res + 1;
    }

    public Integer getNextAlbumId() {
        int res = -1;
        for (Album album : albums) {
            res = Math.max(res, album.getId());
        }
        return res + 1;
    }

    // 1
    public void addSinger(Singer singer) {
        if (indexOfSingerById(singer.getId()) != null) {
            throw new IllegalArgumentException();
        }
        singers.add(singer);
    }

    // 2
    public boolean deleteSingerById(int id) {
        Integer i = indexOfSingerById(id);
        if (i != null) {
            singers.remove(i.intValue());
            List<Integer> indices = indicesOfAlbumsOfSingerById(id);
            for (Integer index : indices) {
                albums.remove(index.intValue());
            }
            return true;
        }
        return false;
    }

    // 3
    public void addAlbum(Album album) {
        if (indexOfSingerById(album.getSinger().getId()) == null) {
            throw new IllegalArgumentException();
        }
        if (indexOfAlbumById(album.getId()) != null) {
            throw new IllegalArgumentException();
        }
        albums.add(album);
    }

    // 4
    public boolean deleteAlbumById(int id) {
        Integer i = indexOfAlbumById(id);
        if (i != null) {
            albums.remove(i.intValue());
            return true;
        }
        return false;
    }

    // 6
    public Integer countAlbumsOfSingerById(int id) {
        if (indexOfSingerById(id) == null) {
            return null;
        }
        List<Integer> indices = indicesOfAlbumsOfSingerById(id);
        return indices.size();
    }

    // 7
    public List<Album> getAlbumsCopy() {
        return new ArrayList<>(albums);
    }

    // 8
    public List<Album> getAlbumsOfSingerById(int id) {
        Integer i = indexOfSingerById(id);
        if (i == null) {
            return null;
        }
        List<Album> list = getSortedAlbums().get(i);
        return new ArrayList<>(list);
    }

    // 9
    public List<Singer> getSingersCopy() {
        return new ArrayList<>(singers);
    }

    public String toXmlString(String dtdPath) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assert doc != null;
        List<List<Album>> sortedAlbums = getSortedAlbums();

        Element collectionElem = doc.createElement("collection");
        doc.appendChild(collectionElem);
        for (int i=0; i<singers.size(); i++) {
            Singer singer = singers.get(i);
            Element singerElem = doc.createElement("singer");
            singerElem.setAttribute("id", singer.getId().toString());
            singerElem.setAttribute("name", singer.getName());
            collectionElem.appendChild(singerElem);
            for (int j=0; j<sortedAlbums.get(i).size(); j++) {
                Album album = sortedAlbums.get(i).get(j);
                Element albumElem = doc.createElement("album");
                albumElem.setAttribute("id", album.getId().toString());
                albumElem.setAttribute("name", album.getName());
                albumElem.setAttribute("year", album.getYear().toString());
                albumElem.setAttribute("genre", album.getGenre());
                singerElem.appendChild(albumElem);
            }
        }

        StringWriter sw = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdPath);
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }

    public String toXmlFormattedString(String dtdPath) {
        StringWriter sw = new StringWriter();
        try {
            String str = toXmlString(dtdPath);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source strSource = new StreamSource(new StringReader(str));
            Result strResult = new StreamResult(sw);
            transformer.transform(strSource, strResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    public void toXmlFile(String xmlPath, String dtdPath) {
        try {
            String str = toXmlString(dtdPath);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Source strSource = new StreamSource(new StringReader(str));
            Result fileResult = new StreamResult(new File(xmlPath));
            transformer.transform(strSource, fileResult);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
