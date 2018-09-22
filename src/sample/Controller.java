package sample;

import com.mpatric.mp3agic.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Controller {
    private MediaPlayer mediaPlayer;
    @FXML
    private MediaView mediaView;

    private String filePath;

    @FXML
    private Slider sliderVolume;
    @FXML
    private Slider seekSlider;
    @FXML
    private ImageView albumArt;
    private void setAlbumArt(String filePath) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException
    {
        Mp3File mp3file = new Mp3File(filePath);
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            byte[] imageData = id3v2Tag.getAlbumImage();
            Image img = new Image(new ByteArrayInputStream(imageData));
            albumArt.setImage(img);
            /*if(imageData!= null){
                System.out.println("Hello");
                String mimeType = id3v2Tag.getAlbumImageMimeType();
                RandomAccessFile file = new RandomAccessFile("album-artwork","rw");
                file.write(mimeType);
                file.close();
            }*/
        }
    }
    @FXML
    private void openFileButton(ActionEvent event) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException{
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a File (*.mp3","*.mp3");
            fileChooser .getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(null);
            filePath = file.toURI().toString();
            //System.out.println("Path "+ file.getAbsolutePath());

            if(filePath!=null){
                Media media = new Media(filePath);
                mediaPlayer = new MediaPlayer(media);
                //mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.setAutoPlay(true);

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
                    setAlbumArt(file.getAbsolutePath());
                    Mp3File mp3file = new Mp3File(file.getAbsolutePath());
                    System.out.println("Length of mp3 is:"+ mp3file.getLengthInSeconds() + "Seconds");

            }
    }

    @FXML
    private void stopButton(ActionEvent event){
        mediaPlayer.stop();
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
