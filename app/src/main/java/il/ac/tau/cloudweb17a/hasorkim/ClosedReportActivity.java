package il.ac.tau.cloudweb17a.hasorkim;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ClosedReportActivity extends AppCompatActivity {

    private Report report;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_closed_report);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView closedReportystatus = findViewById(R.id.closedReportStatus);
        closedReportystatus.setText(report.statusInHebrew());

        ImageView closedReportImage = findViewById(R.id.closedReportImageView);
        if(report.getImageUrl()!=null) {
            bitmap = report.getBitmapFromURL(report.getImageUrl());
            closedReportImage.setImageBitmap(bitmap);
        }
        else{
            View imageContainer= findViewById(R.id.closedReportImageViewContainer);
            imageContainer.setVisibility(View.GONE);
        }

        TextView closedReportExtraDate = findViewById(R.id.closedReportDate);
        closedReportExtraDate.setText(report.getStartTimeAsString());

        TextView closedReportyLoction = findViewById(R.id.closedReportyLoction);
        closedReportyLoction.setText(report.getAddress());

        TextView closedReportExtraText = findViewById(R.id.closedReportExtraText);
        closedReportExtraText.setText(report.getFreeText());





    }
}



