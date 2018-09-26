package sample;

import com.mpatric.mp3agic.*;
import database.DatabaseHandler;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static boolean isMediaPlaying;
    //For populating table of songs tab
    ObservableList<Songs> list = FXCollections.observableArrayList();
    @FXML
    private TableView<Songs> tableView;
    /*@FXML
    private TableColumn<Songs, ImageView> tableArt;*/
    @FXML
    private TableColumn<Songs, String> titleCol;
    @FXML
    private TableColumn<Songs, String> albumCol;
    @FXML
    private TableColumn<Songs, String> artistCol;
    @FXML
    private TableColumn<Songs, String> genreCol;

    @FXML
    public void clickItem(MouseEvent event) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        if(event.getClickCount() == 2)
        {
            System.out.println("Playing " + tableView.getSelectionModel().getSelectedItem().getTitle());
            nowPlaying(tableView.getSelectionModel().getSelectedItem().getFilePath());
        }
    }


    @FXML
    private MediaView mediaView;

    private static String filePath;

    @FXML
    private Slider sliderVolume;
    @FXML
    private Slider seekSlider;
    @FXML
    private ImageView albumArt;
    @FXML
     private Text titletext;
    @FXML
    private Text artisttext;
    @FXML
    private Text albumtext;

    DatabaseHandler databaseHandler;

    String absPath;

    public static void setDir(String dir)
    {
        filePath = dir;
        System.out.println(filePath);
    }
    public static String getDir() {
        return filePath;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        isMediaPlaying= false;
        initCol();
        try {
            loadData();
        }
        catch (Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
        File file = new File("image.png");
        Image image = new Image(file.toURI().toString());
        albumArt.setImage(image);
    }


    private void initCol() {
        System.out.println("initCol");
        //tableArt.setCellValueFactory(new PropertyValueFactory<>("Art"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        albumCol.setCellValueFactory(new PropertyValueFactory<>("Album"));
        artistCol.setCellValueFactory(new PropertyValueFactory<>("Artist"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("Genre"));
    }
    private void loadData()  throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        ImageView imageView = new ImageView();
        list.clear();
        DatabaseHandler handler = new DatabaseHandler();
        String qu = "SELECT * FROM SONGS";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String filePath = rs.getString("path");
                String title = rs.getString("title");
                String album = rs.getString("album");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                /*Image image = extractmp3data.getAlbumArt(filePath);
                imageView.setImage(image);*/
                list.add(new Songs(/*imageView,*/title,album,artist,genre,filePath));
            }
        }
        catch (SQLException ex) {
            System.out.println("loadData:songscontroller " + ex.getLocalizedMessage());
        }
        tableView.getItems().setAll(list);
    }
    public static class Songs {
        private final SimpleStringProperty title;
        private final SimpleStringProperty album;
        private final SimpleStringProperty artist;
        private final SimpleStringProperty genre;
        private final SimpleStringProperty filePath;
        //private final ImageView tableArt;

        Songs(/*ImageView img,*/String title, String album,String artist,String genre,String filePath) {
            //this.tableArt = img;
            this.title = new SimpleStringProperty(title);
            this.album = new SimpleStringProperty(album);
            this.artist = new SimpleStringProperty(genre);
            this.genre = new SimpleStringProperty(genre);
            this.filePath = new SimpleStringProperty(filePath);
        }

        //public ImageView getArt() {return tableArt; }
        public String getTitle() {
            return title.get();
        }
        public String getAlbum() {
            return album.get();
        }
        public String getArtist() {
            return artist.get();
        }
        public String getGenre() {
            return genre.get();
        }
        public String getFilePath() {
            return filePath.get();
        }
    }

    private MediaPlayer mediaPlayer;
     private Media media;

    public void nowPlaying(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        Path path = Paths.get(filePath);
        Media media = new Media(path.toUri().toString());
        if(isMediaPlaying) {
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(media);
        }
        else
            mediaPlayer = new MediaPlayer(media);
        isMediaPlaying=true;
        mediaPlayer.play();


        //Volume Slider
        sliderVolume.setValue(mediaPlayer.getVolume()*100);
        sliderVolume.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(sliderVolume.getValue()/100);
            }
        });

        //Seek Slider
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                seekSlider.setValue(newValue.toSeconds());
            }
        });
        seekSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.seconds(seekSlider.getValue()));
            }
        });

        titletext.setText(extractmp3data.getTitle(filePath));
        albumtext.setText(extractmp3data.getAlbum(filePath));
        artisttext.setText(extractmp3data.getArtist(filePath));
        //Setting Album Art
        Image img = extractmp3data.getAlbumArt(filePath);
        try {
            albumArt.setImage(img);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    @FXML
    private void openFileButton(ActionEvent event) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException{
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a Media File ", "*.mp3","*.aif","*.aiff","*.wav","*.aac","*.flv","*.mp4");
            fileChooser .getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(null);
            filePath = file.toURI().toString();
            absPath = file.getAbsolutePath();
            //System.out.println("Path "+ file.getAbsolutePath());

            if(filePath!=null){
                nowPlaying(absPath);




            }
    }

    @FXML
    private void stopButton(ActionEvent event){
        mediaPlayer.stop();
        isMediaPlaying=false;
    }
    @FXML
    private void playButton(ActionEvent event){
        mediaPlayer.play();
    }
    @FXML
    private void pauseButton(ActionEvent event){
        mediaPlayer.pause();
    }

}
