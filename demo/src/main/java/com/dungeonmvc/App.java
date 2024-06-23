package com.dungeonmvc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

import com.dungeonmvc.models.Board;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        stage.setResizable(false);
        GameManager gm = GameManager.getInstance();
        Board board = new Board(15, "floor", "wall");
        gm.setBoard(board);
        gm.testGame();
        scene = new Scene(loadFXML("mainView"));
        stage.setScene(scene);
        stage.setTitle("Java y mazmorras");
        stage.getIcons().add(new Image(App.class.getResource("images/logo.png").toExternalForm()));
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void restart() throws IOException {
        mainStage.close();
        new App().start(mainStage);
    }
}
