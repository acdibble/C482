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
    /**
     * returns an instance of the create product form
     * @param inventory the inventory to add the product to
     */
    public CreateProductForm(Inventory inventory) {
        super(inventory);
    }


    /**
     * Override for Form#saveData()
     * @see Form#saveData()
     */
    @Override
    protected boolean saveData() {
        int id = Collections.max(inventory.getAllProducts().stream().map(p -> p.getId()).collect(Collectors.toList())) + 1;
        formData.setId(id);
        inventory.addProduct(formData);
        return true;
    }

    /**
     * Override for Form#getFormLabel()
     * @see Form#getFormLabel()
     */
    @Override
    protected String getFormLabel() {
        return "Add Product";
    }

    /**
     * Override for Form#getWindowTitle()
     * @see Form#getWindowTitle()
     */
    @Override
    public String getWindowTitle() {
        return "Create new product";
    }

    /**
     * Override for Form#getIdFieldValue()
     * @see Form#getIdFieldValue()
     */
    @Override
    protected String getIdFieldValue() {
        return "Auto Gen - Disabled";
    }
}
