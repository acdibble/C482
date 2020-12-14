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
    protected void saveData(FormData formData) {
        if (!newTypeMatchesOld()) {
           inventory.deletePart(part);
           if (type == Type.InHouse) {
               part = new InHouse(part.getId(), null, 0.0, 0, 0, 0, 0);
           } else {
               part = new Outsourced(part.getId(), null, 0.0, 0, 0, 0, null);
           }
           inventory.addPart(part);
        }

        part.setName(formData.name);
        part.setPrice(formData.price);
        part.setStock(formData.inv);
        part.setMax(formData.max);
        part.setMin(formData.min);

        if (part instanceof Outsourced) {
            ((Outsourced) part).setCompanyName(formData.companyName);
        } else {
            ((InHouse) part).setMachineId(formData.machineId);
        }
    }
}
