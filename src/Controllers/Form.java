package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * The base class for all forms. Contains logic for field validation
 * and defines abstract methods to give all the forms an identical flow.
 * @author Andrew Dibble
 */
public abstract class Form extends Base {
    /**
     * Called by JavaFX when the "Cancel" button is clicked.
     * Called by the programmer after having saved the data.
     * @param event action event from JavaFX
     */
    @FXML
    abstract protected void handleClose(ActionEvent event);

    /**
     * Validates the form data is correct for the given product/part
     * @throws Exception an exception containing which information did not pass validation
     */
    abstract protected void validateData() throws Exception;

    /**
     * Called by the "handleSave" method. It either updates the existing product/part
     * in the observable list or adds the new product/part to the observable list.
     */
    abstract protected void saveData();

    /**
     * Wraps BaseController#displayError(String, String) to provide a static title
     * @param message the error message to display
     * @see Base#displayError(String, String)
     */
    protected void displayError(String message) {
        super.displayError("Field validation error", message);
    }

    /**
     * Called after the "Save" button is clicked on any form. It tries to validate and
     * save the data. If a validation error is found, it will display it in an alert
     * to the user.
     * @param event action event from JavaFX
     */
    @FXML
    protected void handleSave(ActionEvent event) {
        try {
            validateData();
            saveData();
            handleClose(event);
        } catch (Exception e) {
            System.out.println(e);
            displayError(e.getMessage());
        }
    }

    /**
     * Used by all Form classes to convert text to doubles
     * @param fieldName the name of the field we are parsing, used in error messages
     * @param field the text field that contains form information
     * @return the double value parsed from the text field
     * @throws Exception if the field cannot be parsed into a double, an exception is thrown
     */
    protected double parseDoubleField(String fieldName, TextField field) throws Exception {
        double result;
        try {
            String value = field.getText().trim();
            if (!value.matches("^\\d*\\.?\\d?\\d?$")) throw new NumberFormatException();
            result = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new Exception(getImproperFormatMessage(fieldName));
        }

        return validateIsPositive(fieldName, result);
    }

    /**
     * Used by all Form classes to check that strings aren't empty
     * @param fieldName the name of the field we are parsing, used in error messages
     * @param field the text field that contains form information
     * @return the String value parsed from the text field
     * @throws Exception if the field is empty, an exception is thrown
     */
    protected String parseStringField(String fieldName, TextField field) throws Exception {
        String trimmed = field.getText().trim();
        if (trimmed.length() == 0) {
            throw new Exception(String.format("The field for %s should not be empty", fieldName));
        }
        return trimmed;
    }

    /**
     * Used by all Form classes to convert text to ints
     * @param fieldName the name of the field we are parsing, used in error messages
     * @param field the text field that contains form information
     * @return the int value parsed from the text field
     * @throws Exception if the field cannot be parsed into an int, an exception is thrown
     */
    protected int parseIntField(String fieldName, TextField field) throws Exception {
        int result;
        try {
            result = Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new Exception(getImproperFormatMessage(fieldName));
        }

        return validateIsPositive(fieldName, result);
    }

    /**
     * Used to format the error message to the end user.
     * @param field The name of the field with the issue
     * @return a string error message to be displayed to the end user
     */
    private String getImproperFormatMessage(String field) {
        return String.format("Unable to save part. Please check format of the field %s", field);
    }

    /**
     * Used to ensure that all ints in the form are positive before getting saved
     * @param field the field being parsed
     * @param value the integer value that has been parsed
     * @return the param value if it is positive
     * @throws Exception if the int is less than zero, an exception is thrown
     */
    private int validateIsPositive(String field, int value) throws Exception {
        if (value < 0) {
            throw new Exception(String.format("The value for the field %s should not be negative", field));
        }

        return value;
    }

    /**
     * Used to ensure that all doubles in the form are positive before getting saved
     * @param field the field being parsed
     * @param value the double value that has been parsed
     * @return the param value if it is positive
     * @throws Exception if the double is less than zero, an exception is thrown
     */
    private double validateIsPositive(String field, double value) throws Exception {
        if (value < 0) {
            throw new Exception(String.format("The value for the field %s should not be negative", field));
        }

        return value;
    }

    /**
     * Dynamically sets the correct title for the form window based off the controller's
     * intended use
     * @return a string that contains the title for the form window
     */
    abstract public String getWindowTitle();

    /**
     * Dynamically sets the correct label for the form window based off the controller's
     * intended use
     * @return a string that contains the label for the form
     */
    abstract protected String getFormLabel();

    /**
     * This method was implemented to prevent null pointer issues when trying
     * to access Product#getId() or Part#getId() on a null product/part
     *
     * @return the value to place in the ID text field
     */
    abstract protected String getIdFieldValue();
}
