package Controllers;

import Models.Inventory;
import Models.Product;

public class CreateProductForm extends ProductForm {
    public CreateProductForm(Inventory inventory) {
        super(inventory);
    }

    protected void saveData() {
        inventory.addProduct(product);
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
