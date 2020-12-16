package Controllers;

import Models.Part;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * A controller that contains the logic for displaying and filtering parts
 * in a table view.
 *
 *
 * In a future version the Part class and Product class could be modified to be
 * subclasses of a common class or implement a common interface so that this controller
 * could be reusable for any interface that implements some simple functionality, e.g.
 * getId(), getName(), getStock(), and getPrice()
 *
 *
 * @author Andrew Dibble
 */
public class PartsTableView implements Initializable {
    @FXML
    private TableView<Part> tableView;
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

    private ObservableList<Part> parts;

    /**
     * Override for Initializable#initialize(URL, ResourceBundle)
     * @see Initializable#initialize(URL, ResourceBundle)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partIdCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getId()).asObject());
        partNameCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        partInventoryCol.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getStock()).asObject());
        partPriceCol.setCellValueFactory(param -> new SimpleDoubleProperty(param.getValue().getPrice()).asObject());
        if (parts != null) {
            tableView.setItems(parts);
        }
        tableView.refresh();
    }

    /**
     * Filters the parts based off the contents of the search box
     * updates the UI message if no results remain after filtering
     * @param event
     */
    @FXML
    private void filterParts(KeyEvent event) {
        ObservableList<Part> filteredParts = parts;
        String value = partsSearchBox.getText().toUpperCase().trim();

        if (value != "") {
            filteredParts = new FilteredList<>(parts).filtered(part -> {
                return part.getName().toUpperCase().contains(value) || String.valueOf(part.getId()).contains(value);
            });
        } else {
            tableView.setItems(parts);
        }

        if (filteredParts.size() == 0) {
            tableView.setPlaceholder(new Label("No results found!"));
        } else {
            tableView.setPlaceholder(new Label("No content in table"));
        }

        tableView.setItems(filteredParts);
        tableView.refresh();
    }

    /**
     * Sets the parts to be displayed in the underlying table view
     * @param parts the parts to display
     */
    public void setParts(ObservableList<Part> parts) {
        this.parts = parts;
        this.tableView.setItems(parts);
        this.tableView.refresh();
    }

    /**
     * @return the currently selected part in the table view
     */
    public Part getSelectedPart() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    /**
     * Refreshes the table view
     */
    public void refresh() {
        tableView.refresh();
    }

    /**
     * Changes the height of the table view
     * @param v the desired height
     */
    public void setHeight(double v) {
        tableView.setPrefHeight(v);
    }

    /**
     * Hides and disables the search box if unneeded
     */
    public void hideSearchBox() {
        partsSearchBox.setVisible(false);
        partsSearchBox.setDisable(true);
    }

    /**
     * @return the underlying TableView object
     */
    public TableView<Part> getTableView() {
        return tableView;
    }
}
