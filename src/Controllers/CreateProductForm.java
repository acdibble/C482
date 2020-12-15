package Controllers;

import Models.Inventory;

import java.util.Collections;
import java.util.stream.Collectors;

public class CreateProductForm extends ProductForm {
    public CreateProductForm(Inventory inventory) {
        super(inventory);
    }

    protected void saveData() {
        int id = Collections.max(inventory.getAllProducts().stream().map(p -> p.getId()).collect(Collectors.toList())) + 1;
        formData.setId(id);
        inventory.addProduct(formData);
    }

    @Override
    protected String getFormLabelValue() {
        return "Add Product";
    }

    @Override
    public String getWindowTitle() {
        return "Create new product";
    }
}
