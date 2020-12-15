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

    private void setTableViewNotFoundPlaceholder(TableView tableView) {
        tableView.setPlaceholder(new Label("No results found!"));
    }

    private void restoreTableViewPlaceholder(TableView tableView) {
        tableView.setPlaceholder(new Label("No content in table"));
    }

    @FXML
    private void deletePart(ActionEvent event) {
        Part selectedPart = partsTableViewController.getSelectedPart();
        inventory.deletePart(selectedPart);
        partsTableViewController.refresh();
    }

    @FXML
    private void deleteProduct(ActionEvent event) {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        inventory.deleteProduct(selectedProduct);
        productsTableView.refresh();
    }

    private void openPartForm(PartForm controller) {
        openForm(controller, "/Views/PartForm.fxml", partsTableViewController.getTableView(), 600,600);
    }

    @FXML
    private void createPart(ActionEvent event) {
        openPartForm(new CreatePartForm(inventory));
    }

    @FXML
    private void modifyPart(ActionEvent event) {
        Part selectedPart = partsTableViewController.getSelectedPart();
        openPartForm(new ModifyPartForm(inventory, selectedPart));
    }

    private void openProductForm(ProductForm controller) {
        openForm(controller, "/Views/ProductForm.fxml", productsTableView, 1000, 600);
    }

    private void openForm(Form formController, String resourceURL, TableView tableView, double width, double height) {
        System.out.println("openForm");
        if (formOpen) return;
        System.out.println("no form open");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourceURL));
            loader.setController(formController);
            System.out.println("controller set");
            Scene scene = new Scene(loader.load(), width, height);
            System.out.println("scene created");
            Stage stage = new Stage();
            System.out.println("new stage created");
            stage.setScene(scene);
            stage.setTitle(formController.getWindowTitle());
            stage.setResizable(false);
            stage.setOnHidden(ev -> {
                formOpen = false;
                tableView.refresh();
            });
            formOpen = true;
            System.out.println("showing");
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            formOpen = false;
        }
    }

    @FXML
    private void createProduct(ActionEvent event) throws IOException {
//        System.out.println("createProduct");
//        openProductForm(new CreateProductForm(inventory));
        CreateProductForm formController = new CreateProductForm(inventory);
        System.out.println("openForm");
        if (formOpen) return;
        System.out.println("no form open");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductForm.fxml"));
            loader.setController(formController);
            System.out.println("controller set");
            Scene scene = new Scene(loader.load(), 1000, 600);
            System.out.println("scene created");
            Stage stage = new Stage();
            System.out.println("new stage created");
            stage.setScene(scene);
            stage.setTitle(formController.getWindowTitle());
            stage.setResizable(false);
            stage.setOnHidden(ev -> {
                formOpen = false;
                productsTableView.refresh();
            });
            formOpen = true;
            System.out.println("showing");
            stage.showAndWait();
        } finally {
            formOpen = false;
        }
    }

    @FXML
    private void modifyProduct(ActionEvent event) {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        if (product != null) {
            openProductForm(new ModifyProductForm(inventory, product));
        }
    }
}
