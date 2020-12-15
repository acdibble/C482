package Controllers;

import Models.Inventory;
import Models.Product;

/**
 * Subclass of the abstract class ProductForm which concerns itself solely
 * with the modification of existing products
 * @author Andrew Dibble
 * @see Controllers.ProductForm
 */
public class ModifyProductForm extends ProductForm {
    public ModifyProductForm(Inventory inventory, Product product) {
        super(inventory, product);
    }

    @Override
    protected String getFormLabel() {
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

    @Override
    protected String getIdFieldValue() {
        return String.valueOf(product.getId());
    }
}
