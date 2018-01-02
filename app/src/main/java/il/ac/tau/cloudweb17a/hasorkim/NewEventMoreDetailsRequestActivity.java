package il.ac.tau.cloudweb17a.hasorkim;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;

import static android.view.View.VISIBLE;
import static il.ac.tau.cloudweb17a.hasorkim.User.getUser;

public class NewEventMoreDetailsRequestActivity extends AppCompatActivity {

    public static final int PHOTO_INTENT_REQUEST_CODE = 10;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 20;
    private static final String TAG = "Send_Report";

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    private String address;
    private User user;
    private Report report;
    private Bitmap bitmap;

    private static final double DEFAULT_LATITUDE  = 32.0820748;  // TODO handle this
    private static final double DEFAULT_LONGITUDE = 34.7717487; // TODO handle this
    private double Lat;
    private double Long;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_more_details_request);

        address = getIntent().getStringExtra("address");
        if(address==null||address.equals(getString(R.string.unknown_address))){
            Log.e(TAG, "no address wes received from the previous activity");
            Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, MapsActivity.class);
            startActivity(intent);
        }

        Lat = getIntent().getDoubleExtra("lat",DEFAULT_LATITUDE);
        Long = getIntent().getDoubleExtra("long", DEFAULT_LONGITUDE);

        user = getUser(getApplicationContext());
        TextView reporterName = findViewById(R.id.reporterName);
        TextView reporterPhoneNumber = findViewById(R.id.reporterPhoneNumber);
        TextView reportLocation = findViewById(R.id.reportLocation);

        if(user.getName()!=null)
            reporterName.setText(user.getName());
        if(user.getPhoneNumber()!=null)
            reporterPhoneNumber.setText(user.getPhoneNumber());
        if(address!=null)
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

                if(address.equals("")){
                    address="אלנבי 9, תל אביב";
                }

                report.setReporterName(reporterName.getText().toString(),user);
                report.setPhoneNumber(reporterPhoneNumber.getText().toString(),user);
                report.setMoreInformation(moreInformation.getText().toString());
                report.setAddress(address);
                report.setIsDogWithReporter(cb.isChecked());



                String error =report.validate();
                if(error.equals("")){
                    if(report.isHasSimilarReports()){
                        popUpSimilarReport();
                        return;
                    }
                    if(!report.getIsDogWithReporter()){
                        popUpNotChecked();
                        return;
                    }
                    report.saveReport(bitmap);
                    Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, ActiveReportActivity.class);
                    intent.putExtra("Report", report);
                    startActivity(intent);
                }
                else{
                    TextView errorMessage = findViewById(R.id.errorMessage);
                    errorMessage.setText(error);
                    errorMessage.setVisibility(VISIBLE);

                }


            }
        });

        report = new Report(address,"",user,Lat,Long);
    }

    private void popUpNotChecked() {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage(R.string.not_checked_dialog_message)
                .setTitle(R.string.not_checked_dialog_title)
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
        }).create();

        rightJustifyAndShow(dialog);

    }

    private void rightJustifyAndShow(AlertDialog dialog) {

        dialog.show();
        TextView messageView = dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.END);

        TextView titleView = dialog.findViewById(getApplicationContext().getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.END);
        }

    }

    private void popUpSimilarReport() {
        AlertDialog dialog =new AlertDialog.Builder(this).setMessage(R.string.similar_report_dialog_message)
                .setTitle(R.string.similar_report_dialog_title)
                .setPositiveButton(R.string.ok_similar_report, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        CheckBox cb = findViewById(R.id.reportCheckbox);
                        if(!cb.isChecked()) {
                            popUpNotChecked();
                        }
                        else {
                            report.saveReport(bitmap);
                            Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, ActiveReportActivity.class);
                            intent.putExtra("Report", report);
                            startActivity(intent);
                        }
                    }
                }).setNegativeButton(R.string.cancel_similar_report, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
        }).create();

        rightJustifyAndShow(dialog);
    }



    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private void createPhotoIntent() {
        Intent photoIntent = new Intent(Intent.ACTION_PICK);

        File photoDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Uri photoUri = Uri.parse(photoDirectory.getPath());

        photoIntent.setDataAndType(photoUri,"image/*");

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

        if (resultCode == RESULT_OK){
            if (requestCode == PHOTO_INTENT_REQUEST_CODE){
                Uri photoUri = data.getData();
                ImageButton imageButton = (ImageButton) thisContainer.findViewById(R.id.imageButtonReport);
                imageButton.setImageURI(photoUri);
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                //ImageView imageView = findViewById(R.id.imageView);
                //imageView.setImageBitmap(imageBitmap);
                //imageView.setVisibility(VISIBLE);

                ImageButton imb = findViewById(R.id.imageButtonReport);
                imb.setBackground(new BitmapDrawable(getResources(), imageBitmap));

                bitmap=imageBitmap;

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                            String permissions[], int[] grantResults){
        switch (requestCode){
            case EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE: {
                if ((grantResults.length > 0) &&
                        (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    createPhotoIntent();
                } else Toast.makeText(this, "Gallery Permission Denied :(",
                        Toast.LENGTH_SHORT).show();

            }
        }

    }

}
