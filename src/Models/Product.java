package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Andrew Dibble
 */
public class Product {
    final private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @param id the new id for the product
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name the new name for the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price the new price for the product
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param stock the new stock for the product
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @param min the new min for the product
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max the new max for the product
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the id of the product
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return the name of the product
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the price of the product
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * @return the stock of the product
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * @return the min of the product
     */
    public int getMin() {
        return this.min;
    }

    /**
     * @return the max of the product
     */
    public int getMax() {
        return this.max;
    }

    /**
     * @param part the part to add to the list of associated parts
     */
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }

    /**
     * @param selectedAssociatedPart the part to be removed from the list of associated parts
     * @return whether the part was successfully removed from the list
     */
    public boolean deletedAssociatedPart(Part selectedAssociatedPart) {
        return this.associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * @return an observable list of associated parts
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }
}