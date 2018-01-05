package il.ac.tau.cloudweb17a.hasorkim;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        TextView closedReportStatus = findViewById(R.id.closedReportStatus);
        closedReportStatus.setText(report.statusInHebrew());

        if (report.getImageUrl() != null) {
            //TextView imageHeadline= findViewById(R.id.closed_report_image_headline);
            bitmap = report.getBitmapFromURL(report.getImageUrl());
            ImageView closedReportImage = findViewById(R.id.closedReportImageView);
            closedReportImage.setImageBitmap(bitmap);
            //imageHeadline.setVisibility(View.VISIBLE);
            closedReportImage.setVisibility(View.VISIBLE);
        }

        TextView closedReportExtraDate = findViewById(R.id.closedReportDate);
        String reportDate = report.getStartTimeAsString();
        reportDate = reportDate.substring(6,reportDate.length());
        closedReportExtraDate.setText(reportDate);


        TextView closedReportLocation = findViewById(R.id.closedReportLocation);
        closedReportLocation.setText(report.getAddress());

        TextView closedReportCancellationUserType = findViewById(R.id.closed_report_cancellation_user_type);
        closedReportCancellationUserType.setText(report.getCancellationUserType());

        String comments = report.getFreeText();
        if (comments != null) {
            LinearLayout commentsLayout = findViewById(R.id.comments_layout);
            TextView closedReportExtraText = findViewById(R.id.closedReportExtraText);
            closedReportExtraText.setText(report.getFreeText());
            commentsLayout.setVisibility(View.VISIBLE);
        }
    }
}



