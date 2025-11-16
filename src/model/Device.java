package model;

/**
 * Simple model representing a device in the inventory.
 * Contains common fields such as device number, type, brand, status and cost.
 */
public class Device {
    private int id;
    private String deviceNumber;
    private String type;
    private String brand;
    private String model;
    private String status;
    private String location;
    private String purchaseDate;
    private String notes;
    private double cost;

    public Device() {
    }

    /**
     * Create an empty Device instance. Fields may be populated later via setters.
     */

    // Getters and setters
    /**
     * Get the database id of the device.
     *
     * @return device id
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the database id for this device.
     *
     * @param id numeric database id
     */

    /**
     * Get the device's inventory number.
     *
     * @return device number string
     */
    public String getDeviceNumber() {
        return deviceNumber;
    }

    /**
     * Set the device's inventory number.
     *
     * @param deviceNumber device number string
     */
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    /**
     * Get the device type (e.g. "Desktop", "Laptop").
     *
     * @return device type string
     */
    public String getType() {
        return type;
    }

    /**
     * Set the device type.
     *
     * @param type device type string
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the device brand.
     *
     * @return brand string
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Set the device brand.
     *
     * @param brand brand string
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Get the device model string.
     *
     * @return model string
     */
    public String getModel() {
        return model;
    }

    /**
     * Set the device model string.
     *
     * @param model model string
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Get the device status (e.g. "New", "Broken").
     *
     * @return status string
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the device status.
     *
     * @param status status string
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the device location (e.g. lab or storage).
     *
     * @return location string
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the device location.
     *
     * @param location location string
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Get the purchase date as stored (free-text).
     *
     * @return purchase date string
     */
    public String getPurchaseDate() {
        return purchaseDate;
    }

    /**
     * Set the purchase date string.
     *
     * @param purchaseDate purchase date string
     */
    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    /**
     * Get the notes for this device.
     *
     * @return notes text
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Set the notes for this device.
     *
     * @param notes notes text
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Get the cost of the device.
     *
     * @return cost as double
     */
    public double getCost() {
        return cost;
    }

    /**
     * Set the cost of the device.
     *
     * @param cost numeric cost
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
}
