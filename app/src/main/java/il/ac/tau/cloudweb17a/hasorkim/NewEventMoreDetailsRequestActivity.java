package il.ac.tau.cloudweb17a.hasorkim;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

public class NewEventMoreDetailsRequestActivity extends AppCompatActivity {

    public static final int PHOTO_INTENT_REQUEST_CODE = 10;
    public static final int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 20;

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_event_more_details_request);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_new_event_more_details_request, null);

        ImageButton imageButtonReport = thisContainer.findViewById(R.id.imageButtonReport);
        imageButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

        Button submitReport = thisContainer.findViewById(R.id.submitReport);
        submitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewEventMoreDetailsRequestActivity.this, ActiveReportActivity.class);
                startActivity(intent);
            }
        });
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
            createPhotoIntent();
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
                return;
            }
        }


    }

}
