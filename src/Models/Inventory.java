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
     * @return an inventory with some seed data for testing purposes
     */
    public static Inventory createWithMockData() {
        Inventory inventory = new Inventory();

        Part part = new InHouse(1, "Test", 0.49, 123, 0, 9001, 2);
        inventory.addPart(part);
        inventory.addPart(new InHouse(2, "Widget", 0.90, 456, 0, 9001, 2));
        inventory.addPart(new Outsourced(3, "Another", 0.91, 789, 0, 9001, "didney"));
        Product product = new Product(1, "Fun toy", 1.00, 70, 1, 100);
        product.addAssociatedPart(part);
        inventory.addProduct(product);
        inventory.addProduct(new Product(2, "yo yo", 3.50, 100, 0, 1000));

        return inventory;
    }

    /**
     * @param newPart the new part to add to the list
     */
    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     *
     * @param newProduct the new product to add to the list
     */
    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * @param partId the id of the part to look up
     * @return the part
     * @throws Exception if the part is not found
     */
    public Part lookupPart(int partId) throws Exception {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }

        throw new Exception("part not found");
    }


    /**
     * No specification as to what action should be taken when the product isn't found so it throws an error to fix
     * a null-pointer bug when filtering the product table view and simplify the logic there
     *
     * @param productId the id of the part to look up
     * @return the part
     * @throws Exception if the product does not exist
     */
    public Product lookupProduct(int productId) throws Exception {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }

        throw new Exception("product not found");
    }

    /**
     * @param partName the name of the part to look up
     * @return the part
     */
    public ObservableList<Part> lookupPart(String partName) {
        return allParts.filtered(part -> part.getName().toUpperCase().contains(partName.toUpperCase()));
    }

    /**
     * @param productName the name of the product to look up
     * @return the product
     */
    public ObservableList<Product> lookupProduct(String productName) {
        return allProducts.filtered(product -> product.getName().toUpperCase().contains(productName.toUpperCase()));
    }

    /**
     * @param index the index of the part in the observable list
     * @param selectedPart the changes to apply
     */
    public void updatePart(int index, Part selectedPart) {
        Part part = allParts.get(index);
        part.setId(selectedPart.getId());
        part.setName(selectedPart.getName());
        part.setPrice(selectedPart.getPrice());
        part.setStock(selectedPart.getStock());
        part.setMin(selectedPart.getMin());
        part.setMax(selectedPart.getMax());
    }

    /**
     * @param index the index of the product in the observable list
     * @param selectedProduct the changes to apply
     */
    public void updateProduct(int index, Product selectedProduct) {
        Product product = allProducts.get(index);
        product.setId(product.getId());
        product.setName(product.getName());
        product.setPrice(product.getPrice());
        product.setStock(product.getStock());
        product.setMin(product.getMin());
        product.setMax(product.getMax());
    }

    /**
     * IMPROVEMENT POSSIBILITY:
     * Could be improved in the future to have some sort of data structure (or better database) to track the M:N
     * relationships of products to parts because the runtime of this function will increase as the number of products
     * grow.
     *
     * An O(1) lookup for associated parts would improve the performance here and when changing the type of part from
     * InHouse to Outsourced or vice versa.
     *
     * @param selectedPart the part to remove from the list
     * @return whether the part was successfully removed
     */
    public boolean deletePart(Part selectedPart) {
        boolean noDependencies = allProducts
                .filtered(product -> product.getAllAssociatedParts().contains(selectedPart)).size() == 0;

        return noDependencies && allParts.remove(selectedPart);
    }

    /**
     * @param selectedProduct the product to remove from the list
     * @return whether the product was successfully removed
     */
    public boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return an observable list of all the parts
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return an observable list of all the products
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
