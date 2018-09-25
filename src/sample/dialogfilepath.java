package sample;

import com.jfoenix.controls.JFXTextField;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;
import database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class dialogfilepath implements Initializable {
    public static String directory;
    DatabaseHandler databaseHandler;
    @FXML
    private JFXTextField directoryToScan;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    public static String getDir() {
        return directory;
    }

    @FXML
    private void okScan(ActionEvent event) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
        databaseHandler = new DatabaseHandler();

        //for creating new database evertime
        databaseHandler.execAction("DROP TABLE SONGS");

        databaseHandler.setupSongTable();
        directory = directoryToScan.getText();
        List<File> files = SearchingFiles.searchAllFiles(directory);

        /*stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
               + " id int NOT NULL ,\n"
                        + " path varchar(300),\n"
                        + " title varchar(200),\n"
                        + " album varchar(200),\n"
                        + " artist varchar(200),\n"
                        + " genre varchar(200)"
                        + ")");*/
        int i=0;
        for (File file : files) {
            String qu = "INSERT INTO SONGS (id,path,title,album,artist,genre) VALUES (" + i++
                    + ",'" + file.getAbsolutePath() + "',"
                    + "'" + extractmp3data.getTitle(file.getAbsolutePath()) + "',"
                    + "'" + extractmp3data.getAlbum(file.getAbsolutePath()) + "',"
                    + "'" + extractmp3data.getArtist(file.getAbsolutePath()) + "',"
                    + "'" + extractmp3data.getGenre(file.getAbsolutePath()) + "')";
            System.out.println(qu);
            databaseHandler.execAction(qu);
            }



        Parent p = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene pScene = new Scene(p);
        Controller.setDir(directory);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.hide();
        window.setTitle("RainSync Music Player");
        window.setScene(pScene);
        window.show();

    }
}
