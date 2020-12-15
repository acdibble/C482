package Controllers;

import Models.InHouse;
import Models.Inventory;
import Models.Outsourced;
import Models.Part;

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
    protected void saveData() {
        int id = Collections.max(inventory.getAllParts().stream().map(part -> part.getId()).collect(Collectors.toList())) + 1;

        if (type == Type.InHouse) {
            inventory.addPart(new InHouse(id, validatedData.name, validatedData.price, validatedData.inv, validatedData.min, validatedData.max, validatedData.machineId));
        } else {
            inventory.addPart(new Outsourced(id, validatedData.name, validatedData.price, validatedData.inv, validatedData.min, validatedData.max, validatedData.companyName));
        }
    }

    @Override
    public String getWindowTitle() {
        return "Create new part";
    }
}
