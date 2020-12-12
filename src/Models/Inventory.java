package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Andrew Dibble
 */
public class Inventory {
    final private ObservableList<Part> allParts = FXCollections.observableArrayList();
    final private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param newPart the new part to add to the list
     */
    public void addPart(Part newPart) {
        this.allParts.add(newPart);
    }

    /**
     *
     * @param newProduct the new product to add to the list
     */
    public void addProduct(Product newProduct) {
        this.allProducts.add(newProduct);
    }

    /**
     * @param partId the id of the part to look up
     * @return the part
     */
    public Part lookupPart(int partId) throws Exception {
        for (Part part : this.allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }

        throw new Exception("part not found");
    }


    /**
     * @param productId the id of the part to look up
     * @return the part
     */
    public Product lookupProduct(int productId) throws Exception {
        for (Product product : this.allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }

        throw new Exception("product not found");
    }

    /**
     *
     * @param partName the name of the part to look up
     * @return the part
     */
    public Part lookupPart(String partName) throws Exception {
        for (Part part: this.allParts) {
            if (part.getName() == partName) {
                return part;
            }
        }

        throw new Exception("part not found");
    }

    /**
     *
     * @param productName the name of the product to look up
     * @return the product
     */
    public Product lookupProduct(String productName) throws Exception {
        for (Product product: this.allProducts) {
            if (product.getName() == productName) {
                return product;
            }
        }

        throw new Exception("product not found");
    }

    /**
     *
     * @param index the index of the part in the observable list
     * @param selectedPart the changes to apply
     */
    public void updatePart(int index, Part selectedPart) {
        Part part = this.allParts.get(index);
        part.setId(selectedPart.getId());
        part.setName(selectedPart.getName());
        part.setPrice(selectedPart.getPrice());
        part.setStock(selectedPart.getStock());
        part.setMin(selectedPart.getMin());
        part.setMax(selectedPart.getMax());
    }

    /**
     *
     * @param index the index of the product in the observable list
     * @param selectedProduct the changes to apply
     */
    public void updateProduct(int index, Product selectedProduct) {
        Product product = this.allProducts.get(index);
        product.setId(product.getId());
        product.setName(product.getName());
        product.setPrice(product.getPrice());
        product.setStock(product.getStock());
        product.setMin(product.getMin());
        product.setMax(product.getMax());
    }

    /**
     *
     * @param selectedPart the part to remove from the list
     * @return whether the part was successfully removed
     */
    public boolean deletePart(Part selectedPart) {
        return this.allParts.remove(selectedPart);
    }

    /**
     *
     * @param selectedProduct the product to remove from the list
     * @return whether the product was successfully removed
     */
    public boolean deleteProduct(Product selectedProduct) {
        return this.allProducts.remove(selectedProduct);
    }

    /**
     *
     * @return an observable list of all the parts
     */
    public ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    /**
     *
     * @return an observable list of all the products
     */
    public ObservableList<Product> getAllProducts() {
        return this.allProducts;
    }
}
