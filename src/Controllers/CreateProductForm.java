package Controllers;

import Models.Inventory;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Subclass of the abstract class ProductForm which concerns itself solely
 * with the creation of new products
 * @author Andrew Dibble
 * @see Controllers.ProductForm
 */
public class CreateProductForm extends ProductForm {
    public CreateProductForm(Inventory inventory) {
        super(inventory);
    }

    /**
     * assigns a new id and adds the created product to the inventory of products
     */
    protected void saveData() {
        int id = Collections.max(inventory.getAllProducts().stream().map(p -> p.getId()).collect(Collectors.toList())) + 1;
        formData.setId(id);
        inventory.addProduct(formData);
    }

    @Override
    protected String getFormLabel() {
        return "Add Product";
    }

    @Override
    public String getWindowTitle() {
        return "Create new product";
    }

    @Override
    protected String getIdFieldValue() {
        return "Auto Gen - Disabled";
    }
}
