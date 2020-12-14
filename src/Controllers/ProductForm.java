package Controllers;

import Models.Part;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class ProductForm implements Initializable {
    @FXML
    PartsTableView allPartsTableViewController;
    @FXML
    PartsTableView associatedPartsTableViewController;

    @FXML
    Label formLabel;
    @FXML
    TextField idField;
    @FXML
    TextField nameField;
    @FXML
    TextField invField;
    @FXML
    TextField priceField;
    @FXML
    TextField maxField;
    @FXML
    TextField minField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    @FXML
    private void handleSave(ActionEvent event) {

    }

    @FXML
    private void handleClose(ActionEvent event) {

    }

    @FXML
    private void removePart(ActionEvent event) {

    }

    @FXML
    private void addPart(ActionEvent event) {

    }
}
