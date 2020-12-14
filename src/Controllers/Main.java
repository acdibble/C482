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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
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
    @FXML
    private TextField productsSearchBox;
    @FXML
    private TableColumn<Product, Integer> productIdCol;
    @FXML
    private TableColumn<Product, String> productNameCol;
    @FXML
    private TableColumn<Product, Integer> productInventoryCol;
    @FXML
    private TableColumn<Product, Double> productPriceCol;

    final private Inventory inventory;

    private boolean partFormOpen = false;

    public Main(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializePartsViewTable();
        initializeProductsViewTable();
    }

    private void initializePartsViewTable() {
        partIdCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        partNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        partInventoryCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        partPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        partsTableView.setItems(inventory.getAllParts());
        partsTableView.refresh();
    }

    private void initializeProductsViewTable() {
        productIdCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        productNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        productInventoryCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        productPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        productsTableView.setItems(inventory.getAllProducts());
        productsTableView.refresh();
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

    @FXML
    private void filterParts(KeyEvent event) {
        ObservableList<Part> parts = inventory.getAllParts();

        String value = partsSearchBox.getText().toUpperCase().trim();

        if (value != "") {
            parts = new FilteredList<>(parts).filtered(part -> part.getName().toUpperCase().contains(value));
        }

        if (parts.size() == 0) {
            setTableViewNotFoundPlaceholder(partsTableView);
        } else {
            restoreTableViewPlaceholder(partsTableView);
        }

        partsTableView.setItems(parts);
        partsTableView.refresh();
    }

    @FXML
    private void filterProducts(KeyEvent event) {
        ObservableList<Product> products = inventory.getAllProducts();

        String value = productsSearchBox.getText().toUpperCase().trim();

        if (value != "") {
            products = new FilteredList<>(products).filtered(product -> product.getName().toUpperCase().contains(value));
        }

        if (products.size() == 0) {
            setTableViewNotFoundPlaceholder(productsTableView);
        } else {
            restoreTableViewPlaceholder(productsTableView);
        }

        productsTableView.setItems(products);
        productsTableView.refresh();
    }

    private void setTableViewNotFoundPlaceholder(TableView tableView) {
        tableView.setPlaceholder(new Label("No results found!"));
    }

    private void restoreTableViewPlaceholder(TableView tableView) {
        tableView.setPlaceholder(new Label("No content in table"));
    }

    @FXML
    private void deletePart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        inventory.deletePart(selectedPart);
        partsTableView.refresh();
    }

    @FXML
    private void deleteProduct(ActionEvent event) {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        inventory.deleteProduct(selectedProduct);
        productsTableView.refresh();
    }

    private void openPartForm(PartForm controller) {
        if (partFormOpen) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/PartForm.fxml"));
            loader.setController(controller);
            Scene scene = new Scene(loader.load(), 600, 600);
            Stage stage = new Stage();
            stage.setTitle("Create new part");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            stage.setOnHidden(ev -> {
                partFormOpen = false;
                partsTableView.refresh();

            });
            this.partFormOpen = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void createPart(ActionEvent event) {
        openPartForm(new CreatePartForm(inventory));
    }


    @FXML
    private void modifyPart(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        openPartForm(new ModifyPartForm(inventory, selectedPart));
    }
}
