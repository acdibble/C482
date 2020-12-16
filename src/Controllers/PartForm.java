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

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Abstract base class for the creation and modification of parts
 * @author Andrew Dibble
 * @see Form
 */
public abstract class PartForm extends Form implements Initializable {
    /**
     * Contains form data that is then passed to an instance of an InHouse or Outsourced
     * part before saving
     */
    protected class FormData {
        String name;
        int inv;
        double price;
        int max;
        int min;
        int machineId;
        String companyName;
    }

    /**
     * used to determine which type of part to instantiate
     */
    protected enum Type {
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

    /**
     * instantiates an instance of the Part form with the inventory provided
     * @param inventory the inventory to use
     */
    public PartForm(Inventory inventory) {
        this.inventory = inventory;
        this.toggleGroup = new ToggleGroup();
    }

    /**
     * Override for Intializable#initialize(URL, ResourceBundle)
     * Fills in the form values and sets the radio buttons up properly
     * @see Initializable#initialize(URL, ResourceBundle)
     */
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
        formLabel.setText(getFormLabel());
    }

    /**
     * Populates the form with values from the part being edited
     * @param part the part being edited
     */
    protected void fillForm(Part part) {
        idField.setText(String.valueOf(part.getId()));
        nameField.setText(part.getName());
        invField.setText(String.valueOf(part.getStock()));
        priceField.setText(formatDoubleField(part.getPrice()));
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

    /**
     * updates the form label for the machine ID field when the radio button is toggled
     */
    protected void setInHouse() {
        type = Type.InHouse;
        extraLabel.setText("Machine ID");
    }

    /**
     * updates the form label for the company name field when the radio button is toggled
     */
    private void setOutsourced() {
        type = Type.Outsourced;
        extraLabel.setText("Company Name");
    }

    /**
     * Override for Form#handleClose(ActionEvent)
     * @see Form#handleClose(ActionEvent) 
     */
    @Override
    @FXML
    protected void handleClose(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.hide();
    }

    /**
     * Override for Form#validateData()
     * @see Form#validateData() 
     */
    @Override
    protected void validateData() throws Exception {
        FormData formData = new FormData();
        formData.name = parseStringField("Name", nameField);
        formData.inv = parseIntField("Inv", invField);
        formData.price = parseDoubleField("Price", priceField);
        formData.max = parseIntField("Max", maxField);
        formData.min = parseIntField("Min", minField);

        if (formData.min > formData.inv || formData.max < formData.inv) {
            throw new Exception("Inv should be between min and max.");
        }

        if (type == Type.InHouse) {
            formData.machineId = parseIntField("Machine ID", extraField);
        } else {
            formData.companyName = parseStringField("Company Name", extraField);
        }
        validatedData = formData;
    }
}
