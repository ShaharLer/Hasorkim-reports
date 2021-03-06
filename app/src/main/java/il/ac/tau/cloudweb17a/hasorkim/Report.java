package il.ac.tau.cloudweb17a.hasorkim;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static il.ac.tau.cloudweb17a.hasorkim.User.getUserWOContext;

public class Report implements java.io.Serializable {


    private String id;
    private String reporterName;
    private int incrementalReportId;
    private String status;
    private long startTime;
    private String address;
    private String freeText;
    private String phoneNumber;
    private String extraPhoneNumber;
    private String assignedScanner;
    private String managerInCharge;
    private int availableScanners;
    private String cancellationText;
    private String closingText;
    private String userId;
    private boolean hasSimilarReports;
    private boolean isDogWithReporter;
    private String imageUrl;
    private String photoPath;
    private String cancellationUserType;
    private double latitude;
    private double longitude;


    private int nextIncrementalId;
    private static final String TAG = Report.class.getSimpleName();

    public Report() {
        // Default constructor required for calls to DataSnapshot.getValue(Report.class)
    }


    public Report(String address, String freeText, User user, double Lat, double Long) {

        this.startTime = -Calendar.getInstance().getTime().getTime();
        this.address = address;
        this.status = "NEW";
        this.freeText = freeText;

        this.assignedScanner = "";
        this.availableScanners = 0;
        this.managerInCharge = "";

        if (user.getName() != null)
            this.reporterName = user.getName();
        else
            this.reporterName = "";

        if (user.getPhoneNumber() != null)
            this.phoneNumber = user.getPhoneNumber();
        else
            this.phoneNumber = "";

        this.userId = user.getId();
        //this.incrementalReportId = this.setIncrementalReportId();

        this.hasSimilarReports = false;

        this.latitude = Lat;
        this.longitude = Long;

        //setListenerOnReportWithUserId();
    }

    public void setIsDogWithReporter(boolean isDogWithReporter) {
        this.isDogWithReporter = isDogWithReporter;
    }

    public boolean getIsDogWithReporter() {
        return isDogWithReporter;
    }

    public String getId() {
        return id;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getFreeText() {
        return freeText;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getExtraPhoneNumber() {
        return extraPhoneNumber;
    }

    public String getAssignedScanner() {
        return assignedScanner;
    }

    public String getManagerInCharge() {
        return managerInCharge;
    }

    public String getStatus() {
        return this.status;
    }

    public String getCancellationUserType() {
        return cancellationUserType;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public String startTimeAsString() {
        Format format = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        return format.format(new Date(-this.startTime));
    }

    public String getAddress() {
        return this.address;
    }

    public String getUserId() {
        return userId;
    }

    public int getAvailableScanners() {
        return this.availableScanners;
    }

    public String getCancellationText() {
        return cancellationText;
    }

    public int getIncrementalReportId() {
        return incrementalReportId;
    }

    public String persistReportGetID() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports");
        String id = reportsRef.push().getKey();
        setId(id);

        return id;
    }


    public void setExtraPhoneNumber(String extraPhoneNumber) {
        this.extraPhoneNumber = extraPhoneNumber;
    }

    public void setReporterName(String reporterName, User user) {
        this.reporterName = reporterName;
        user.setName(reporterName);
    }

    public void setPhoneNumber(String phoneNumber, User user) {
        this.phoneNumber = phoneNumber;
        user.setPhoneNumber(phoneNumber);
    }

    public void setMoreInformation(String moreInformation) {
        this.freeText = moreInformation;
    }

    public void setId(String id) {
        this.id = id;
        //setListenerOnReportWithUserId();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartTime() {
        this.startTime = -Calendar.getInstance().getTime().getTime();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCancellationText(String cancellationText) {
        this.cancellationText = cancellationText;
    }

    public void setCancellationUserType(String cancellationUserType) {
        this.cancellationUserType = cancellationUserType;
    }

    @Exclude
    public void setIncrementalReportId() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("reports").orderByChild("incrementalReportId").limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterable<DataSnapshot> contactChildren = snapshot.getChildren();

                for (DataSnapshot report : contactChildren) {
                    Report lastReport = report.getValue(Report.class);
                    nextIncrementalId = lastReport.getIncrementalReportId() + 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

        this.incrementalReportId = nextIncrementalId;
    }

    public void logReportUpdateStatus(String status) {

        String previousStatus = this.getStatus();
        User user = getUserWOContext();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SS")
                .format(new java.util.Date());

        DatabaseReference logRef = FirebaseDatabase.getInstance().getReference("status_logs")
                .child(this.getId()).child(timeStamp);

        logRef.child("report").setValue(this);
        logRef.child("user").setValue(user);
        logRef.child("oldStatus").setValue(previousStatus);
        logRef.child("newStatus").setValue(status);
    }

    public void reportUpdateStatus(String status) {
        this.logReportUpdateStatus(status);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports").child(this.id).child("status");
        reportsRef.setValue(status);
    }

    public void reportUpdateExtraPhoneNumber() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports").child(this.id);
        Map<String, Object> reportMap = new HashMap<String, Object>();
        reportMap.put("extraPhoneNumber", this.extraPhoneNumber);
        reportsRef.updateChildren(reportMap);
    }

    public void reportUpdateIncrementalReportId() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports").child(this.id).child("incrementalReportId");
        reportsRef.setValue(this.incrementalReportId);
    }

    public void reportUpdateCancellationText(String cancellationText) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports").child(this.id);
        Map<String, Object> reportMap = new HashMap<String, Object>();
        reportMap.put("cancellationText", cancellationText);
        reportsRef.updateChildren(reportMap);
    }

    public void reportUpdateCancellationUserType() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reportsRef = ref.child("reports").child(this.id);
        Map<String, Object> reportMap = new HashMap<String, Object>();
        reportMap.put("cancellationUserType", "המדווח/ת");
        reportsRef.updateChildren(reportMap);
    }

    public boolean CheckIfReportOpen() {
        if ((Objects.equals(this.status, "CANCELED")) || (Objects.equals(this.status, "CLOSED")))
            return false;
        else return true;
    }

    public String statusInHebrew() {
        return translateStatus(this.getStatus());
    }

    public static String translateStatus(String status) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("NEW", "דיווח חדש");
        map.put("SCANNER_ENLISTED", "דיווח חדש");
        map.put("MANAGER_ENLISTED", "בטיפול מנהל");
        map.put("MANAGER_ASSIGNED_SCANNER", "נמצא סורק בקרבתך, ניצור קשר בהקדם");
        map.put("SCANNER_ON_THE_WAY", "סורק בדרך אליך");
        map.put("CLOSED", "סגור");
        map.put("CANCELED", "בוטל");

        return map.get(status);
    }

    public String validatePhone(String phoneNumberToCheck) {
        String error = "";
        if (phoneNumberToCheck.equals(""))
            error += "חסר מספר טלפון";
        else if (!phoneNumberToCheck.matches("^(0[57][0-9]{8})|(0[57]-[0-9]{8})|(0[23489][0-9]{7})|(0[23489]-[0-9]{7})$"))
            error += "מספר טלפון לא תקין";

        return error;
    }

    public String validateName(String phoneNumberToCheck) {
        String error = "";
        if (phoneNumberToCheck.equals(""))
            error += "שם המדווח/ת חסר";

        return error;
    }

    public boolean isHasSimilarReports() {
        return this.hasSimilarReports;
    }


    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", reportyName='" + reporterName + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", address='" + address + '\'' +
                ", freeText='" + freeText + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", extraPhoneNumber='" + extraPhoneNumber + '\'' +
                ", assignedScanner='" + assignedScanner + '\'' +
                ", availableScanners=" + availableScanners + '\'' +
                ", managerInCharge=" + managerInCharge +
                '}';
    }

    public String saveReport() {
        String id = null;
        final Report report = this;
        id = persistReportGetID();
        setId(id);
        setStatus("NEW");


        if (this.photoPath != null) {
            StorageReference imagesRef = FirebaseStorage.getInstance().getReference().child("images");
            String fileName = getUserId() + "_" + String.valueOf(new Date().getTime());
            StorageReference imageRef = imagesRef.child(fileName);

            Uri file = Uri.fromFile(new File(this.photoPath));

            UploadTask uploadTask = imageRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    if (downloadUrl != null) {
                        imageUrl = downloadUrl.toString();
                    }
                    persistReport();
                }
            });
        } else {
            persistReport();
        }

        User.user.setId(FirebaseInstanceId.getInstance().getToken());

        return id;

    }

    private void persistReport() {
        this.logReportUpdateStatus("CREATE");
        FirebaseDatabase.getInstance().getReference("reports").child(getId()).setValue(this);
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getClosingText() {
        return closingText;
    }

    public void setClosingText(String closingText) {
        this.closingText = closingText;
    }
}