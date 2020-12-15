package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public abstract class Form {
    @FXML
    abstract protected void handleClose(ActionEvent event);

    abstract protected void validateData() throws Exception;
    abstract protected void saveData();

    @FXML
    protected void handleSave(ActionEvent event) {
        try {
            validateData();
            saveData();
        } catch (Exception e) {
            this.displayError(e.getMessage());
            return;
        }

        this.handleClose(event);
    }

    protected void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Field validation error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected double formatDoubleField(String fieldName, TextField field) throws Exception {
        double result;
        try {
            result = Double.parseDouble(field.getText());
        } catch (NumberFormatException e) {
            throw new Exception(getImproperFormatMessage(fieldName));
        }

        return validateIsPositive(fieldName, result);
    }

    protected String formatStringField(String fieldName, TextField field) throws Exception {
        String trimmed = field.getText().trim();
        if (trimmed.length() == 0) {
            throw new Exception(String.format("The field for %s should not be empty", fieldName));
        }
        return trimmed;
    }

    protected int formatIntField(String fieldName, TextField field) throws Exception {
        int result;
        try {
            result = Integer.parseInt(field.getText());
        } catch (NumberFormatException e) {
            throw new Exception(getImproperFormatMessage(fieldName));
        }

        return validateIsPositive(fieldName, result);
    }

    protected String getImproperFormatMessage(String field) {
        return String.format("Unable to save part. Please check format of the field %s", field);
    }

    protected <T extends Number> T validateIsPositive(String field, T value) throws Exception {
        if (new BigDecimal(value.doubleValue()).compareTo(new BigDecimal(0)) == -1) {
            throw new Exception(String.format("The value for the field %s should not be negative", field));
        }

        return value;
    }

    abstract public String getWindowTitle();
}
