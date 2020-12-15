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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The main controller for the application. It handles displaying all the parts and products
 * and spawning new windows when a user wants to add or modify a part or product.
 * @author Andrew Dibble
 */
public class Main implements Initializable {
    @FXML
    private PartsTableView partsTableViewController;

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

    private boolean formOpen = false;

    public Main(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeProductsViewTable();
        partsTableViewController.setParts(inventory.getAllParts());
        partsTableViewController.setHeight(400);
    }

    /**
     * Sets the column types so the data is properly displayed
     */
    private void initializeProductsViewTable() {
        productIdCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        productNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        productInventoryCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        productPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        productsTableView.setItems(inventory.getAllProducts());
        productsTableView.refresh();
    }

    /**
     * exits the application
     */
    @FXML
    private void handleExit() {
        Platform.exit();
    }

    /**
     * Filters products based off the contents of the search box
     * @param event a key event from JavaFX
     */
    @FXML
    private void filterProducts(KeyEvent event) {
        ObservableList<Product> products = inventory.getAllProducts();

        String value = productsSearchBox.getText().toUpperCase().trim();

        if (value != "") {
            products = new FilteredList<>(products).filtered(product -> {
                return product.getName().toUpperCase().contains(value) || String.valueOf(product.getId()).contains(value);
            });
        }

        if (products.size() == 0) {
            setTableViewNotFoundPlaceholder(productsTableView);
        } else {
            restoreTableViewPlaceholder(productsTableView);
        }

        productsTableView.setItems(products);
        productsTableView.refresh();
    }

    /**
     * Updates the UI message to let the user know no results were found after a filter.
     * @param tableView the table view being filtered
     */
    private void setTableViewNotFoundPlaceholder(TableView tableView) {
        tableView.setPlaceholder(new Label("No results found!"));
    }

    /**
     * Restores the UI message to its default state after the empty filter is gone.
     * @param tableView the table view being filtered
     */
    private void restoreTableViewPlaceholder(TableView tableView) {
        tableView.setPlaceholder(new Label("No content in table"));
    }

    /**
     * removes a part from the inventory
     * @param event an action event from JavaFX
     */
    @FXML
    private void deletePart(ActionEvent event) {
        Part selectedPart = partsTableViewController.getSelectedPart();
        inventory.deletePart(selectedPart);
        partsTableViewController.refresh();
    }

    /**
     * removes a product from the inventory
     * @param event an action event from JavaFX
     */
    @FXML
    private void deleteProduct(ActionEvent event) {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        inventory.deleteProduct(selectedProduct);
        productsTableView.refresh();
    }

    /**
     * opens the part form with the passed-in controller
     * @param controller a subclass of the abstract PartForm controller
     */
    private void openPartForm(PartForm controller) {
        openForm(controller, "/Views/PartForm.fxml", partsTableViewController.getTableView(), 600,600);
    }

    /**
     * opens the part form with a CreatePartForm controller
     * @param event an action event from JavaFX
     */
    @FXML
    private void createPart(ActionEvent event) {
        openPartForm(new CreatePartForm(inventory));
    }

    /**
     * opens the part form with a ModifyPartForm controller
     * @param event an action event from JavaFX
     */
    @FXML
    private void modifyPart(ActionEvent event) {
        Part selectedPart = partsTableViewController.getSelectedPart();
        openPartForm(new ModifyPartForm(inventory, selectedPart));
    }

    /**
     * opens the product form with the passed-in controller
     * @param controller a subclass of the abstract ProductForm controller
     */
    private void openProductForm(ProductForm controller) {
        openForm(controller, "/Views/ProductForm.fxml", productsTableView, 1000, 600);
    }

    /**
     * opens the product form with a CreateProductForm controller
     * @param event an action event from JavaFX
     */
    @FXML
    private void createProduct(ActionEvent event) {
        openProductForm(new CreateProductForm(inventory));
    }

    /**
     * opens the product form with a ModifyProductForm controller
     * @param event an action event from JavaFX
     */
    @FXML
    private void modifyProduct(ActionEvent event) {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        if (product != null) {
            openProductForm(new ModifyProductForm(inventory, product));
        }
    }

    /**
     * Opens a new window the the requested form controller and resource
     * @param formController a PartForm or ProductForm controller
     * @param resourceURL the path to the FXML file
     * @param tableView a table view to refresh after the form closes
     * @param width the desired width of the new window
     * @param height the desired height of the new window
     */
    private void openForm(Form formController, String resourceURL, TableView tableView, double width, double height) {
        if (formOpen) return;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceURL));
            loader.setController(formController);
            Scene scene = new Scene(loader.load(), width, height);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(formController.getWindowTitle());
            stage.setResizable(false);
            stage.setOnHidden(ev -> {
                formOpen = false;
                tableView.refresh();
            });
            formOpen = true;
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            formOpen = false;
        }
    }
}
