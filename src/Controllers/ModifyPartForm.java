package Controllers;

import Models.*;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Subclass of the abstract class PartForm which concerns itself solely
 * with the modification of existing parts
 * @author Andrew Dibble
 * @see Controllers.PartForm
 */
public class ModifyPartForm extends PartForm {
    private Part part;

    /**
     * returns an instance of the ModifyPartForm class for updating a part
     * @param inventory the inventory that contains the part
     * @param selectedPart the part to be modified
     */
    public ModifyPartForm(Inventory inventory, Part selectedPart) {
        super(inventory);
        part = selectedPart;
    }

    /**
     * Override for PartForm#initialize(URL, ResourceBundle)
     * @see PartForm#initialize(URL, ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        fillForm(part);
    }

    /**
     * Override for Form#getFormLabel()
     * @see Form#getFormLabel()
     */
    @Override
    protected String getFormLabel() {
        return "Modify Part";
    }

    @Override
    protected String getIdFieldValue() {
        return String.valueOf(part.getId());
    }

    /**
     * Prevents an issue with the machineId/companyName field being improperly parsed for the part
     * if the type has changed during modification
     *
     * @return whether the type of part changed during editing
     */
    private boolean newTypeMatchesOld() {
        return (type == Type.InHouse && part instanceof InHouse) || (type == Type.Outsourced && part instanceof Outsourced);
    }

    /**
     * Override for Form#saveData()
     *
     * Creates a new part if the type changed, which includes updating the associated parts of any affected products
     *
     * @see Form#saveData()
     */
    @Override
    protected boolean saveData() {
        if (!newTypeMatchesOld()) {
            ObservableList<Product> associatedProducts = null;
           if (!inventory.deletePart(part)) {
               associatedProducts = inventory.getAllProducts().filtered(p -> p.getAllAssociatedParts().contains(part));
               associatedProducts.forEach(p -> p.deletedAssociatedPart(part));
               inventory.deletePart(part);
           }
           if (type == Type.InHouse) {
               part = new InHouse(part.getId(), "", 0.0, 0, 0, 0, 0);
           } else {
               part = new Outsourced(part.getId(), "", 0.0, 0, 0, 0, null);
           }
           inventory.addPart(part);
           if (associatedProducts != null) associatedProducts.forEach(p -> p.addAssociatedPart(part));
        }

        part.setName(validatedData.name);
        part.setPrice(validatedData.price);
        part.setStock(validatedData.inv);
        part.setMax(validatedData.max);
        part.setMin(validatedData.min);

        if (part instanceof Outsourced) {
            ((Outsourced) part).setCompanyName(validatedData.companyName);
        } else {
            ((InHouse) part).setMachineId(validatedData.machineId);
        }
        return true;
    }

    /**
     * Override for Form#getWindowTitle()
     * @see Form#getWindowTitle()
     */
    @Override
    public String getWindowTitle() {
        return "Update part";
    }
}
