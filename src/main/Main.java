package main;

import Models.Inventory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * Override for Application#start(Stage)
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Main.fxml"));
        loader.setController(new Controllers.Main(Inventory.createWithMockData()));
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root, 900, 640));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    /**
     * launches the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
