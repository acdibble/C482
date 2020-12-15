package Controllers;

import Models.Inventory;
import Models.Part;
import Models.Product;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ProductForm extends Form implements Initializable {
    @FXML
    private PartsTableView allPartsTableViewController;
    @FXML
    private PartsTableView associatedPartsTableViewController;

    @FXML
    private Label formLabel;
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

    protected Product formData;
    protected Product product;
    protected Inventory inventory;

    public ProductForm(Inventory inventory) {
        this.inventory = inventory;
        this.formData = new Product(0, null, 0, 0, 0, 0);
    }

    public ProductForm(Inventory inventory, Product product) {
        this.inventory = inventory;
        this.product = product;
        this.formData = new Product(0, null, 0, 0, 0, 0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        associatedPartsTableViewController.hideSearchBox();
        formLabel.setText(getFormLabelValue());
        populateForm();
    }

    private void populateForm() {
        if (product != null) {
            idField.setText(String.valueOf(product.getId()));
            nameField.setText(product.getName());
            priceField.setText(String.valueOf(product.getPrice()));
            invField.setText(String.valueOf(product.getStock()));
            maxField.setText(String.valueOf(product.getMax()));
            minField.setText(String.valueOf(product.getMin()));
            product.getAllAssociatedParts().forEach(part -> formData.addAssociatedPart(part));
        } else {
            idField.setText("Auto Gen - Disabled");
        }
        setPartsForTables();
        idField.setDisable(true);
    }

    abstract protected String getFormLabelValue();

    private void setAssociatedParts(ObservableList<Part> parts) {
        associatedPartsTableViewController.setParts(parts);
    }

    public void setAllParts(ObservableList<Part> parts) {
        allPartsTableViewController.setParts(parts);
    }

    protected void validateData() throws Exception {
        formData.setName(formatStringField("Name", nameField));
        formData.setPrice(formatDoubleField("Price", priceField));
        formData.setStock(formatIntField("Inv", invField));
        formData.setMin(formatIntField("Min", minField));
        formData.setMax(formatIntField("Max", maxField));
    }

    @FXML
    protected void handleClose(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void removePart(ActionEvent event) {
        Part part = associatedPartsTableViewController.getSelectedPart();
        if (part != null) {
            formData.deletedAssociatedPart(part);
            setPartsForTables();
        }
    }


    @FXML
    private void addPart(ActionEvent event) {
        Part part = allPartsTableViewController.getSelectedPart();
        if (part != null) {
            formData.addAssociatedPart(part);
            setPartsForTables();
        }
    }

    private void setPartsForTables() {
        ObservableList<Part> associatedParts = formData.getAllAssociatedParts();
        associatedPartsTableViewController.setParts(associatedParts);

        ObservableList<Part> unassociatedParts = inventory.getAllParts().filtered(part -> !associatedParts.contains(part));
        allPartsTableViewController.setParts(unassociatedParts);
    }
}
