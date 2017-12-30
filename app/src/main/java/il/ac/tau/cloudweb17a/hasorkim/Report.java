package il.ac.tau.cloudweb17a.hasorkim;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by workhourse on 12/2/17.
 */

public class Report implements  java.io.Serializable{


    private String id;
    private String reporterName;
    private String status;
    private String startTime;
    private String address;
    private String[] imageUrls;
    private String freeText;
    private String phoneNumber;
    private String assignedScanner;
    private int availableScanners;
    private String cancellationText;
    private String userId;

    public Report(){
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }


    public Report(String address, String freeText, User user) {

        this.startTime = Calendar.getInstance().getTime().toString();
        this.address = address;
        this.status = "NEW";
        this.freeText = freeText;

        this.assignedScanner = "";
        this.availableScanners = 0;
        this.reporterName = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.userId = user.getId();
    }

    public String getId() {return id;   }
    public String getReportyName() {
        return reporterName;
    }

    public String getFreeText() {
        return freeText;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAssignedScanner() {
        return assignedScanner;
    }

    public String getStatus() { return this.status; }
    public String getStartTime() { return this.startTime; }
    public String getAddress() { return this.address; }
    public String getUserId() {return userId;   }
    public int getAvailableScanners() { return this.availableScanners; }
    public String getCancellationText() {
        return cancellationText;
    }

    public void persistReport(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports");
        reportsRef.push().setValue(this);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCancellationText(String cancellationText) {
        this.cancellationText = cancellationText;
    }


    public void ReportUpdateStatus(String status){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports").child(this.id).child("status");
        reportsRef.setValue(status);
    }

    public void ReportUpdateCancellationText(String cancellationText){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports").child(this.id);
        Map<String,Object> reportMap = new HashMap<String,Object>();
        reportMap.put("cancellationText", cancellationText);
        reportsRef.updateChildren(reportMap);
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", reportyName='" + reporterName + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", address='" + address + '\'' +
                ", imageUrls=" + Arrays.toString(imageUrls) +
                ", freeText='" + freeText + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", assignedScanner='" + assignedScanner + '\'' +
                ", availableScanners=" + availableScanners +
                '}';
    }

    public boolean isOpenReport(){
        if ((Objects.equals(this.status, "CANCELED")) || (Objects.equals(this.status, "CLOSED")))
            return false;
        else return true;
    }

    public String statusInHebrew(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("NEW", "הדיווח בטיפול מנהל");
        map.put("CLOSED", "הטיפול בדיווח הסתיים");
        map.put("CANCELED", "הדיווח בוטל");
        map.put("SCANER_ON_THE_WAY", "סורק בדרך אליך");
        return map.get(this.status);
    }
}