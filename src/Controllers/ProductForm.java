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

    protected Product product;
    protected Inventory inventory;

    public ProductForm(Inventory inventory) {
        this.inventory = inventory;
        this.product = new Product(0, null, 0, 0, 0, 0);
    }

    public ProductForm(Inventory inventory, Product product) {
        this.inventory = inventory;
        this.product = product;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPartsForTables();
        associatedPartsTableViewController.hideSearchBox();
        formLabel.setText(getFormLabelValue());
    }

    abstract protected String getFormLabelValue();

    private void setAssociatedParts(ObservableList<Part> parts) {
        associatedPartsTableViewController.setParts(parts);
    }

    public void setAllParts(ObservableList<Part> parts) {
        allPartsTableViewController.setParts(parts);
    }

    protected void validateData() throws Exception {
        product.setName(formatStringField("Name", nameField));
        product.setPrice(formatDoubleField("Price", priceField));
        product.setStock(formatIntField("Inv", invField));
        product.setMin(formatIntField("Min", minField));
        product.setMax(formatIntField("Max", maxField));
    }

    @FXML
    protected void handleClose(ActionEvent event) {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.hide();
    }

    @FXML
    private void removePart(ActionEvent event) {
        product.deletedAssociatedPart(associatedPartsTableViewController.getSelectedPart());
        setPartsForTables();
    }

    @FXML
    private void addPart(ActionEvent event) {
        product.addAssociatedPart(allPartsTableViewController.getSelectedPart());
        setPartsForTables();
    }

    private void setPartsForTables() {
        ObservableList<Part> associatedParts = product.getAllAssociatedParts();
        associatedPartsTableViewController.setParts(associatedParts);

        ObservableList<Part> unassociatedParts = inventory.getAllParts().filtered(p -> !associatedParts.contains(p));
        allPartsTableViewController.setParts(unassociatedParts);
    }
}
