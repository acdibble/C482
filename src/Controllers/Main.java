package Controllers;

import Models.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Andrew Dibble
 */
public class Main implements Initializable {
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TextField partsSearchBox;
    @FXML
    private TableColumn<Part, Integer> partIdCol;
    @FXML
    private TableColumn<Part, String> partNameCol;
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;
    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Product> productsTableView;

    private Inventory inventory;

    public Main() {
        this.inventory = new Inventory();
    }

    public Main(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializePartsViewTable();
        inventory.addPart(new InHouse(1, "Test", 0.69, 420, 0, 9001, 2));
        inventory.addPart(new InHouse(2, "Widget", 0.90, 420, 0, 9001, 2));
        inventory.addPart(new Outsourced(3, "Another", 0.90, 420, 0, 9001, "didney"));
        partsTableView.setItems(inventory.getAllParts());
        partsTableView.refresh();
    }

    private void initializePartsViewTable() {
        partIdCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        partNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        partInventoryCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        partPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    public void setPartsTableView(TableView<Part> partsTableView) {
        this.partsTableView = partsTableView;
    }

    @FXML
    public void setProductsTableView(TableView<Product> productsTableView) {
        this.productsTableView = productsTableView;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @FXML
    private void filterParts(KeyEvent event) {
        ObservableList<Part> parts = inventory.getAllParts();

        String value = partsSearchBox.getText().toUpperCase();

        if (value != "") {
            parts = new FilteredList<>(parts).filtered(part -> part.getName().toUpperCase().contains(value));
        }

        partsTableView.setItems(parts);
        partsTableView.refresh();
    }

    @FXML
    private void filterProducts(KeyEvent event) {
        // TODO: implement
    }

    @FXML
    private void deletePart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        inventory.deletePart(selectedPart);
        partsTableView.refresh();
    }
}
