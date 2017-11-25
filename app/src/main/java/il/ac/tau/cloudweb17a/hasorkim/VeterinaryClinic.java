package il.ac.tau.cloudweb17a.hasorkim;

public class VeterinaryClinic {

    private final String name;
    private final String address;
    private final String openingHours;

    VeterinaryClinic(String name, String address, String openingHours) {
        this.name = name;
        this.address = address;
        this.openingHours = openingHours;
    }

    public String getName() { return this.name; }
    public String getAddress() { return this.address; }
    public String getOpeningHours() { return this.openingHours; }
}
