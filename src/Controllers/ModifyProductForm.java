package Controllers;

import Models.Inventory;
import Models.Product;

public class ModifyProductForm extends ProductForm {
    public ModifyProductForm(Inventory inventory, Product product) {
        super(inventory, product);
    }

    @Override
    protected String getFormLabelValue() {
        return "Modify Product";
    }

    @Override
    public String getWindowTitle() {
        return "Update product";
    }

    @Override
    protected void saveData() {
        formData.setId(product.getId());
        inventory.deleteProduct(product);
        inventory.addProduct(formData);
    }
}
