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



