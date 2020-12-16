package Controllers;

import javafx.scene.control.Alert;

public class Base {
    /**
     * Used to display validation errors to the end user.
     * @param message the error message to be displayed
     */
    protected void displayError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
