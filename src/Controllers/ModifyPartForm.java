package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Part;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartForm extends PartForm {
    private Part part;

    public ModifyPartForm(Inventory inventory, Part selectedPart) {
        super(inventory);
        part = selectedPart;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        fillForm(part);
    }

    @Override
    protected String getFormLabelText() {
        return "Modify Part";
    }

    protected String getIdFieldValue() {
        return String.valueOf(part.getId());
    }

    private boolean newTypeMatchesOld() {
        return (type == Type.InHouse && part instanceof InHouse) || (type == Type.Outsourced && part instanceof Outsourced);
    }

    @Override
    protected void saveData() {
        if (!newTypeMatchesOld()) {
           inventory.deletePart(part);
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
    }

    @Override
    public String getWindowTitle() {
        return "Update part";
    }
}
