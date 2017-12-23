package il.ac.tau.cloudweb17a.hasorkim;

/**
 * Created by workhourse on 12/2/17.
 */

public class Report {

    private int id;
    private String reportyName;
    private String status;
    private final String startTime;
    private final String address;
    private String[] imageUrls;
    private String freeText;
    private long phoneNumber;
    private String assignedScanner;
    private int availableScanners;

    Report(int id, String startTime, String address) {
        this.id = id;
        this.startTime = startTime;
        this.address = address;
    }

    Report(String reportyName, String startTime, String address, String status,
           String freeText, long phoneNumber, String assignedScanner,
           int availableScanners) {
        this.reportyName = reportyName;
        this.startTime = startTime;
        this.address = address;
        this.status = status;
        this.freeText = freeText;
        this.phoneNumber = phoneNumber;
        this.assignedScanner = assignedScanner;
        this.availableScanners = availableScanners;
    }

    public String getStatus() { return this.status; }
    public String getStartTime() { return this.startTime; }
    public String getAddress() { return this.address; }
    public int getAvailableScanners() { return this.availableScanners; }
}