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
    /**
     * returns an instance of the ModifyProductForm with the inventory and product provided
     * @param inventory
     * @param product
     */
    public ModifyProductForm(Inventory inventory, Product product) {
        super(inventory);
        this.product = product;
    }

    /**
     * Override for Form#getFormLabel()
     * @see Form#getFormLabel()
     */
    @Override
    protected String getFormLabel() {
        return "Modify Product";
    }

    /**
     * Override for Form#getWindowTitle()
     * @see Form#getWindowTitle()
     */
    @Override
    public String getWindowTitle() {
        return "Update product";
    }

    /**
     * Override for Form#saveData()
     * @see Form#saveData()
     */
    @Override
    protected boolean saveData() {
        formData.setId(product.getId());
        inventory.deleteProduct(product);
        inventory.addProduct(formData);
        return true;
    }

    /**
     * Override for Form#getIdFieldValue()
     * @see Form#getIdFieldValue()
     */
    @Override
    protected String getIdFieldValue() {
        return String.valueOf(product.getId());
    }
}
