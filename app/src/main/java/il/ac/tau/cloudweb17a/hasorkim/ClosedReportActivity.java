package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ClosedReportActivity extends AppCompatActivity {

    private Report report;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_closed_report);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView closedReportStatus = findViewById(R.id.closedReportStatus);
        String reportStatus = (getString(R.string.the_report_string) + report.statusInHebrew());
        closedReportStatus.setText(reportStatus);


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

        String comments = report.getFreeText();
        if ((comments != null) && (!comments.isEmpty())) {
            TextView closedReportExtraText = findViewById(R.id.closedReportExtraText);
            closedReportExtraText.setText(report.getFreeText());
            LinearLayout moreInfoLayout = findViewById(R.id.closed_report_more_info_layout);
            moreInfoLayout.setVisibility(View.VISIBLE);
        }

        TextView closing_or_cancellation_reason_label = findViewById(R.id.closing_or_cancellation_reason_label);
        TextView closing_or_cancellation_reason = findViewById(R.id.closing_or_cancellation_reason);
        LinearLayout closing_or_cancellation_reason_layout = findViewById(R.id.closing_or_cancellation_reason_layout);

        if (report.getStatus().equals("CLOSED")) {
            String closingReason = report.getClosingText();
            if ((closingReason != null) && (!closingReason.isEmpty())) {
                closing_or_cancellation_reason_label.setText(R.string.report_close_reason);
                closing_or_cancellation_reason.setText(closingReason);
                closing_or_cancellation_reason_layout.setVisibility(View.VISIBLE);
            }
        }
        else {
            String canceller = report.getCancellationUserType();
            if ((canceller != null) && (!canceller.isEmpty())) {
                LinearLayout closedByLayout = findViewById(R.id.report_deleted_by);
                TextView deleted_by_label = findViewById(R.id.deleted_by_label);
                TextView closedReportCancellationUserType = findViewById(R.id.closed_report_cancellation_user_type);
                deleted_by_label.setText(R.string.deleted_by);
                closedReportCancellationUserType.setText(report.getCancellationUserType());
                closedByLayout.setVisibility(View.VISIBLE);

                String cancellationReason = report.getCancellationText();
                if ((cancellationReason != null) && (!cancellationReason.isEmpty())) {
                    closing_or_cancellation_reason_label.setText(R.string.report_delete_reason);
                    closing_or_cancellation_reason.setText(cancellationReason);
                    closing_or_cancellation_reason_layout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.share_menu, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        setShareIntent();

        // Return true to display menu
        return true;
    }

    // Call to update the share intent
    private void setShareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setType("text/plain");

        if (report.getPhotoPath() != null) {
            Uri photoURI = FileProvider.getUriForFile(ClosedReportActivity.this,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    new File(report.getPhotoPath()));
            shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
            shareIntent.setType("image/jpeg");
        }
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "פתחתי דיווח על כלב אבוד ברחוב " + report.getAddress() + " דרך אפליקציית הסורקים" +
                "\n" +
                "רוצה לדווח גם?" +
                " הורד את האפליקיה https://play.google.com/store/apps/details?id=il.ac.tau.cloudweb17a.hasorkim");
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();//clear memory
    }
}



