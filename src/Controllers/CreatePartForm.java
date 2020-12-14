package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CreatePartForm extends PartForm {
    public CreatePartForm(Inventory inventory) {
        super(inventory);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setInHouse();
    }

    @Override
    protected String getFormLabelText() {
        return "Add Part";
    }

    protected String getIdFieldValue() {
        return "Auto Gen- Disabled";
    }

    @Override
    protected void saveData(FormData formData) {
        int id = Collections.max(inventory.getAllParts().stream().map(part -> part.getId()).collect(Collectors.toList())) + 1;

        if (type == Type.InHouse) {
            inventory.addPart(new InHouse(id, formData.name, formData.price, formData.inv, formData.min, formData.max, formData.machineId));
        } else {
            inventory.addPart(new Outsourced(id, formData.name, formData.price, formData.inv, formData.min, formData.max, formData.companyName));
        }
    }

}
