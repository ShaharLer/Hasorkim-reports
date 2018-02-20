package il.ac.tau.cloudweb17a.hasorkim;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static il.ac.tau.cloudweb17a.hasorkim.User.getUser;

public class NewReportActivity extends AppCompatActivity {

    private static final String TAG = NewReportActivity.class.getSimpleName();

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 102;

    private String address;
    private User user;
    private Report report;

    private static final double DEFAULT_LATITUDE = 32.0820748;  // TODO handle this
    private static final double DEFAULT_LONGITUDE = 34.7717487; // TODO handle this
    private double Lat;
    private double Long;

    ImageView dogImage;
    TextView reporterName;
    TextView reporterPhoneNumber;
    TextView reportLocation;
    private View new_report_all_view;


    public interface MyCallBackClass {
        void execute();
    }

    final MyCallBackClass  popupSimilarReportCallBack  = new MyCallBackClass() {
        @Override
        public void execute() {
            new_report_all_view.setVisibility(View.VISIBLE);
            if(!report.isHasSimilarReports()) {
                report.saveReport();
                Intent intent = new Intent(NewReportActivity.this, ActiveReportActivity.class);
                intent.putExtra("Report", report);
                startActivity(intent);
                finish();
            }
            else{
                popUpSimilarReport();
            }
        }
    };

    private void popUpSimilarReport() {

        TextView title = new TextView(this);
        title.setText(R.string.similar_report_dialog_title);
        title.setPadding(10, 50, 64, 9);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        title.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        new AlertDialog.Builder(this).setMessage(R.string.similar_report_dialog_message)
                //.setTitle(R.string.similar_report_dialog_title)
                .setCustomTitle(title)
                .setPositiveButton(R.string.ok_similar_report, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        report.saveReport();
                        Intent intent = new Intent(NewReportActivity.this, ActiveReportActivity.class);
                        intent.putExtra("Report", report);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.cancel_similar_report, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);


        address = getIntent().getStringExtra("address");
        if (address == null || address.equals(getString(R.string.unknown_address))) {
            Log.e(TAG, "no address wes received from the previous activity");
            Intent intent = new Intent(NewReportActivity.this, MapsActivity.class);
            Toast.makeText(this, R.string.location_error, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        Lat = getIntent().getDoubleExtra("lat", DEFAULT_LATITUDE);
        Long = getIntent().getDoubleExtra("long", DEFAULT_LONGITUDE);

        user = getUser(getApplicationContext());

        new_report_all_view = findViewById(R.id.details_scroll_view);


        reporterName = findViewById(R.id.reporterName);
        reporterPhoneNumber = findViewById(R.id.reporterPhoneNumber);
        reportLocation = findViewById(R.id.reportLocation);
        dogImage = findViewById(R.id.ReportImageView);

        if (user.getName() != null)
            reporterName.setText(user.getName());
        if (user.getPhoneNumber() != null)
            reporterPhoneNumber.setText(user.getPhoneNumber());
        if (address != null)
            reportLocation.setText(address);


        dogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });



        Button submitReport = findViewById(R.id.submitReport);
        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView moreInformation = findViewById(R.id.moreInformation);
                TextView reporterName = findViewById(R.id.reporterName);
                TextView reporterPhoneNumber = findViewById(R.id.reporterPhoneNumber);
                TextView reportLocation = findViewById(R.id.reportLocation);
                CheckBox cb = findViewById(R.id.reportCheckbox);
                address = reportLocation.getText().toString();

                if (address.equals("")) {
                    address = "אלנבי 9, תל אביב";
                }

                report.setReporterName(reporterName.getText().toString(), user);
                report.setPhoneNumber(reporterPhoneNumber.getText().toString(), user);
                report.setMoreInformation(moreInformation.getText().toString());
                report.setAddress(address);
                report.setIsDogWithReporter(cb.isChecked());

                String nameError = report.validateName(report.getReporterName());
                String phoneError = report.validatePhone(report.getPhoneNumber());

                if (nameError.equals("") && phoneError.equals("")) {
                    if (!report.getIsDogWithReporter()) {
                        popUpNotChecked();
                        return;
                    }

                    new_report_all_view.setVisibility(View.INVISIBLE);
                    report.checkForSimilarReportAndSubmit(popupSimilarReportCallBack);


                } else {
                    if (!phoneError.equals("")) {
                        reporterPhoneNumber.setError(phoneError);
                        reporterPhoneNumber.requestFocus();
                    }

                    if (!nameError.equals("")) {
                        reporterName.setError(nameError);
                        reporterName.requestFocus();
                    }
                }
            }
        });

        report = new Report(address, "", user, Lat, Long);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, "Error creating photo file");
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "il.ac.tau.cloudweb17a.hasorkim.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                dogImage.setVisibility(View.VISIBLE);
                Glide.with(this).load(report.getPhotoPath()).into(dogImage);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an dogImage file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        report.setPhotoPath(image.getAbsolutePath());
        return image;
    }

    private void popUpNotChecked() {

        TextView title = new TextView(this);
        title.setText(R.string.not_checked_dialog_title);
        title.setPadding(10, 50, 64, 9);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        title.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        new AlertDialog.Builder(this).setMessage(R.string.not_checked_dialog_message)
                //.setTitle(R.string.not_checked_dialog_title)
                .setPositiveButton(R.string.ok_not_checked, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new_report_all_view.setVisibility(View.INVISIBLE);
                        report.checkForSimilarReportAndSubmit(popupSimilarReportCallBack);
                    }
                }).setNegativeButton(R.string.cancel_not_checked, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).setCustomTitle(title).create().show();
    }



    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                if ((grantResults.length > 0) &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    dispatchTakePictureIntent();
                } else Toast.makeText(this, R.string.need_permission,
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();//clear memory
    }

}
