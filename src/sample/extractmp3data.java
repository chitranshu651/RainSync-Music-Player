package sample;

import com.mpatric.mp3agic.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class extractmp3data {
    static Image img;
    static Mp3File mp3file;
    static ID3v2 id3v2Tag;

    public static Image getAlbumArt(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        Mp3File mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            System.out.println(filePath);
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            byte[] imageData = id3v2Tag.getAlbumImage();
            img = new Image(new ByteArrayInputStream(imageData));
            return img;

        }
        return null;
    }
    public static String getTrack(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
            return id3v2Tag.getTrack();
        }
        return null;
    }
    public static String getArtist(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
            return id3v2Tag.getArtist();
        }
        return null;
    }
    public static String getAlbum(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
            return id3v2Tag.getAlbum();
        }
        return null;
    }
    public static String getYear(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
            return id3v2Tag.getYear();
        }
        return null;
    }
    public static String getTitle(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
            return id3v2Tag.getTitle();
        }
        return null;
    }
    public static String getGenre(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
            return id3v2Tag.getGenreDescription();
        }
        return null;
    }
}
