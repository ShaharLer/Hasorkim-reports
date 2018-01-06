package il.ac.tau.cloudweb17a.hasorkim;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static il.ac.tau.cloudweb17a.hasorkim.User.getUser;

public class NewEventMoreDetailsRequestActivity extends AppCompatActivity {

    public static final int PHOTO_INTENT_REQUEST_CODE = 10;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 20;
    private static final String TAG = "Send_Report";

    private String address;
    private User user;
    private Report report;
    private Bitmap bitmap;

    private static final double DEFAULT_LATITUDE = 32.0820748;  // TODO handle this
    private static final double DEFAULT_LONGITUDE = 34.7717487; // TODO handle this
    private double Lat;
    private double Long;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_more_details_request);

        address = getIntent().getStringExtra("address");
        if (address == null || address.equals(getString(R.string.unknown_address))) {
            Log.e(TAG, "no address wes received from the previous activity");
            Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, MapsActivity.class);
            startActivity(intent);
        }

        Lat = getIntent().getDoubleExtra("lat", DEFAULT_LATITUDE);
        Long = getIntent().getDoubleExtra("long", DEFAULT_LONGITUDE);

        user = getUser(getApplicationContext());
        TextView reporterName = findViewById(R.id.reporterName);
        TextView reporterPhoneNumber = findViewById(R.id.reporterPhoneNumber);
        TextView reportLocation = findViewById(R.id.reportLocation);

        if (user.getName() != null)
            reporterName.setText(user.getName());
        if (user.getPhoneNumber() != null)
            reporterPhoneNumber.setText(user.getPhoneNumber());
        if (address != null)
            reportLocation.setText(address);


        ImageButton imageButtonReport = findViewById(R.id.imageButtonReport);
        imageButtonReport.setOnClickListener(new View.OnClickListener() {
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
                    if (report.isHasSimilarReports()) {
                        popUpSimilarReport();
                        return;
                    }

                    if (!report.getIsDogWithReporter()) {
                        popUpNotChecked();
                        return;
                    }

                    report.saveReport(bitmap);
                    Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, ActiveReportActivity.class);
                    intent.putExtra("Report", report);
                    startActivity(intent);
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
                        report.saveReport(bitmap);
                        Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, ActiveReportActivity.class);
                        intent.putExtra("Report", report);
                        startActivity(intent);
                    }
                }).setNegativeButton(R.string.cancel_not_checked, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).setCustomTitle(title).create().show();


    }

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

                        CheckBox cb = findViewById(R.id.reportCheckbox);
                        if (!cb.isChecked()) {
                            popUpNotChecked();
                        } else {
                            report.saveReport(bitmap);
                            Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, ActiveReportActivity.class);
                            intent.putExtra("Report", report);
                            startActivity(intent);
                        }
                    }
                }).setNegativeButton(R.string.cancel_similar_report, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        }).create().show();

    }


    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "il.ac.tau.cloudweb17a.hasorkim.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }


    private void createPhotoIntent() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        File photoDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Uri photoUri = Uri.parse(photoDirectory.getPath());

        photoIntent.setDataAndType(photoUri, "image/*");

        startActivityForResult(photoIntent, PHOTO_INTENT_REQUEST_CODE);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            dispatchTakePictureIntent();
            //createPhotoIntent();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ImageView image = findViewById(R.id.ReportImageView);

            if (requestCode == PHOTO_INTENT_REQUEST_CODE) {
                Uri photoUri = data.getData();
                Picasso.with(this).load(photoUri).resize(30, 30).centerCrop().into(image);
                image.setVisibility(View.VISIBLE);
            }
            if (requestCode == REQUEST_TAKE_PHOTO) {
                Picasso.with(this).load("file://" + mCurrentPhotoPath).resize(30, 30).centerCrop().into(image);
                image.setVisibility(View.VISIBLE);

            }

            bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                if ((grantResults.length > 0) &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    createPhotoIntent();
                } else Toast.makeText(this, "Gallery Permission Denied :(",
                        Toast.LENGTH_SHORT).show();

            }
        }

    }


    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
