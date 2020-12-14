package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Part;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class PartForm implements Initializable {
    protected class FormData {
        String name;
        int inv;
        double price;
        int max;
        int min;
        int machineId;
        String companyName;
    }

    public enum Type {
        InHouse,
        Outsourced
    }

    @FXML
    private Label formLabel;
    @FXML
    private RadioButton inHouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField invField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
    @FXML
    private Label extraLabel;
    @FXML
    private TextField extraField;

    protected ToggleGroup toggleGroup;

    protected Type type;
    protected Inventory inventory;

    public PartForm(Inventory inventory) {
        this.inventory = inventory;
        this.toggleGroup = new ToggleGroup();
    }

    abstract protected String getIdFieldValue();
    abstract protected String getFormLabelText();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inHouseRadioButton.setToggleGroup(toggleGroup);
        outsourcedRadioButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(inHouseRadioButton);
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (newValue == inHouseRadioButton) {
                    setInHouse();
                } else {
                    setOutsourced();
                }
            }
        });
        idField.setDisable(true);
        idField.setText(getIdFieldValue());
        formLabel.setText(getFormLabelText());
    }

    protected void fillForm(Part part) {
        idField.setText(String.valueOf(part.getId()));
        nameField.setText(part.getName());
        invField.setText(String.valueOf(part.getStock()));
        priceField.setText(String.valueOf(part.getPrice()));
        maxField.setText(String.valueOf(part.getMax()));
        minField.setText(String.valueOf(part.getMin()));

        if (part instanceof Outsourced) {
            extraField.setText(((Outsourced) part).getCompanyName());
            setOutsourced();
            toggleGroup.selectToggle(outsourcedRadioButton);
        } else {
            extraField.setText(String.valueOf(((InHouse) part).getMachineId()));
            setInHouse();
            toggleGroup.selectToggle(inHouseRadioButton);
        }
    }

    protected void setInHouse() {
        type = Type.InHouse;
        extraLabel.setText("Machine ID");
    }

    private void setOutsourced() {
        type = Type.Outsourced;
        extraLabel.setText("Company Name");
    }

    abstract protected void saveData(FormData formData);

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            saveData(getFormData());
        } catch (Exception e) {
            this.displayError(e.getMessage());
            return;
        }

        this.handleClose(event);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.hide();
    }

    private FormData getFormData() throws Exception {
        FormData formData = new FormData();
        formData.name = formatStringField("Name", nameField.getText());
        formData.inv = formatIntField("Inv", invField.getText());
        formData.price = formatDoubleField("Price", priceField.getText());
        formData.max = formatIntField("Max", maxField.getText());
        formData.min = formatIntField("Min", minField.getText());

        if (formData.min > formData.inv || formData.max < formData.inv) {
            throw new Exception("Inv should be between min and max.");
        }

        if (type == Type.InHouse) {
            formData.machineId = formatIntField("Machine ID", extraField.getText());
        } else {
            formData.companyName = formatStringField("Company Name", extraField.getText());
        }
        return formData;
    }

    private void displayError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Field validation error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double formatDoubleField(String fieldName, String value) throws Exception {
        double result;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new Exception(getImproperFormatMessage(fieldName));
        }

        return validateIsPositive(fieldName, result);
    }

    private String formatStringField(String field, String data) throws Exception {
        String trimmed = data.trim();
        if (trimmed.length() == 0) {
            throw new Exception(String.format("The field for %s should not be empty", field));
        }
        return trimmed;
    }

    private int formatIntField(String fieldName, String value) throws Exception {
        int result;
        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new Exception(getImproperFormatMessage(fieldName));
        }

        return validateIsPositive(fieldName, result);
    }

    private String getImproperFormatMessage(String field) {
        return String.format("Unable to save part. Please check format of the field %s", field);
    }

    private <T extends Number> T validateIsPositive(String field, T value) throws Exception {
        if (new BigDecimal(value.doubleValue()).compareTo(new BigDecimal(0)) == -1) {
            throw new Exception(String.format("The value for the field %s should not be negative", field));
        }

        return value;
    }
}
