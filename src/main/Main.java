package main;

import Models.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Inventory inventory = new Inventory();

        Controllers.Main mainController = new Controllers.Main(inventory);
        FXMLLoader loader = new FXMLLoader();
        loader.setController(mainController);
        Parent root = loader.load(getClass().getResource("/Views/Main.fxml"));

        primaryStage.setScene(new Scene(root, 900, 640));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
