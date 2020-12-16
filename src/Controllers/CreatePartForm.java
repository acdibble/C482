package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Subclass of the abstract class PartForm that concerns itself solely
 * with the creation of new parts
 * @author Andrew Dibble
 * @see Controllers.PartForm
 */
public class CreatePartForm extends PartForm {
    /**
     * returns an instance of the create part form
     * @param inventory the inventory to add the part to
     */
    public CreatePartForm(Inventory inventory) {
        super(inventory);
    }

    /**
     * Override for Initializable#initialize(URL, ResourceBundle)
     * @see javafx.fxml.Initializable#initialize(URL, ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setInHouse();
    }

    /**
     * Override for Form#getFormLabel()
     * @see Form#getFormLabel()
     */
    @Override
    protected String getFormLabel() {
        return "Add Part";
    }

    /**
     * Override for Form#getIdFieldValue()
     * @see Form#getIdFieldValue()
     */
    @Override
    protected String getIdFieldValue() {
        return "Auto Gen - Disabled";
    }

    /**
     * Override for Form#saveData()
     * @see Form#saveData()
     */
    @Override
    protected boolean saveData() {
        int id = Collections.max(inventory.getAllParts().stream().map(part -> part.getId()).collect(Collectors.toList())) + 1;

        if (type == Type.InHouse) {
            inventory.addPart(new InHouse(id, validatedData.name, validatedData.price, validatedData.inv, validatedData.min, validatedData.max, validatedData.machineId));
        } else {
            inventory.addPart(new Outsourced(id, validatedData.name, validatedData.price, validatedData.inv, validatedData.min, validatedData.max, validatedData.companyName));
        }

        return true;
    }

    /**
     * Override for Form#getWindowTitle()
     * @see Form#getWindowTitle()
     */
    @Override
    public String getWindowTitle() {
        return "Create new part";
    }
}
