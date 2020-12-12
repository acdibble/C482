package Models;

/**
 * @author Andrew Dibble
 */
public class InHouse extends Part {
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @param machineId the new machine id to set for the part
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * @return the machine id of the part
     */
    public int getMachineId() {
        return machineId;
    }
}
