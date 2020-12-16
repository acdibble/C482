package Controllers;

import javafx.scene.control.Alert;

/**
 * An abstract controller Base class that contains some logic common enough for all controllers to benefit.
 *
 * @author Andrew Dibble
 */
public abstract class Base {
    /**
     * Used to display validation errors to the end user.
     * @param title the title for the error message alert
     * @param message the error message to be displayed
     */
    protected void displayError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Formats a double to two decimal places
     * @param value the double to format
     * @return a string containing a number with two decimal places
     */
    protected String formatDoubleField(double value) {
        return String.format("%.2f", value);
    }
}
