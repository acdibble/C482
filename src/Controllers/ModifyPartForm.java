package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Part;
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
     * Override for Initializable#initialize(URL, ResourceBundle)
     * @see Initializable#initialize(URL, ResourceBundle)
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
     * @see Form#saveData()
     */
    @Override
    protected boolean saveData() {
        if (!newTypeMatchesOld()) {
           if (!inventory.deletePart(part)) {
               displayError("Error", "The type of this part cannot be changed as it is currently associated with a product!");
               return false;
           }
           if (type == Type.InHouse) {
               part = new InHouse(part.getId(), "", 0.0, 0, 0, 0, 0);
           } else {
               part = new Outsourced(part.getId(), "", 0.0, 0, 0, 0, null);
           }
           inventory.addPart(part);
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
