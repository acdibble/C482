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

    public void setParts(ObservableList<Part> parts) {
        this.parts = parts;
        this.tableView.setItems(parts);
        this.tableView.refresh();
    }

    public Part getSelectedPart() {
        return tableView.getSelectionModel().getSelectedItem();
    }

    public void refresh() {
        tableView.refresh();
    }
}
