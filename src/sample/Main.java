package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class Main extends Application {

    /*@Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("RainSync Music Player");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }*/
    //for loading dialog filepath fxml
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("dialogfilepath.fxml"));
        primaryStage.setTitle("RainSync Music Player");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }


    public static void main(String[] args)  throws IOException
    {
        List<File> files = SearchingFiles.searchAllFiles("E:/");
        for (File file : files) {
            System.out.println("file: " + file.getAbsolutePath());
        }
        launch(args);
    }
}
