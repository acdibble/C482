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

public abstract class PartForm extends Form implements Initializable {
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

    protected FormData validatedData;

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

    @FXML
    protected void handleClose(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.hide();
    }

    @Override
    protected void validateData() throws Exception {
        FormData formData = new FormData();
        formData.name = formatStringField("Name", nameField);
        formData.inv = formatIntField("Inv", invField);
        formData.price = formatDoubleField("Price", priceField);
        formData.max = formatIntField("Max", maxField);
        formData.min = formatIntField("Min", minField);

        if (formData.min > formData.inv || formData.max < formData.inv) {
            throw new Exception("Inv should be between min and max.");
        }

        if (type == Type.InHouse) {
            formData.machineId = formatIntField("Machine ID", extraField);
        } else {
            formData.companyName = formatStringField("Company Name", extraField);
        }
        validatedData = formData;
    }
}
