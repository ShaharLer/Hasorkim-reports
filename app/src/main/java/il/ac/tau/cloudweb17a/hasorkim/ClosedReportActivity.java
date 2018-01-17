package il.ac.tau.cloudweb17a.hasorkim;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ClosedReportActivity extends AppCompatActivity {

    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_closed_report);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView closedReportStatus = findViewById(R.id.closedReportStatus);
        closedReportStatus.setText(report.statusInHebrew());


        if (report.getPhotoPath() != null) {
            ImageView closedReportImage = findViewById(R.id.closedReportImageView);
            closedReportImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(report.getPhotoPath()).into(closedReportImage);
        }

        TextView closedReportExtraDate = findViewById(R.id.closedReportDate);
        String reportDate = report.startTimeAsString();
        reportDate = reportDate.substring(6, reportDate.length());
        closedReportExtraDate.setText(reportDate);


        TextView closedReportLocation = findViewById(R.id.closedReportLocation);
        closedReportLocation.setText(report.getAddress());

        String canceller = report.getCancellationUserType();
        if ((canceller != null) && (!canceller.isEmpty())) {
            TextView closedReportCancellationUserType = findViewById(R.id.closed_report_cancellation_user_type);
            closedReportCancellationUserType.setText(report.getCancellationUserType());
            LinearLayout closedByLayout = findViewById(R.id.report_closed_by);
            closedByLayout.setVisibility(View.VISIBLE);
        }

        String comments = report.getFreeText();
        if ((comments != null) && (!comments.isEmpty())) {
            TextView closedReportExtraText = findViewById(R.id.closedReportExtraText);
            closedReportExtraText.setText(report.getFreeText());
            LinearLayout moreInfoLayout = findViewById(R.id.closed_report_more_info_layout);
            moreInfoLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();//clear memory
    }
}



