package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        SelectWindowView selectWindow = new SelectWindowView();
        Scene scene = new Scene(selectWindow.getLayout(), 500, 600);

        primaryStage.setTitle("Toy Language - Program Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}