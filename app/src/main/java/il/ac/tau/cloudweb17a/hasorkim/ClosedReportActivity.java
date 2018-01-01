package il.ac.tau.cloudweb17a.hasorkim;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
        bitmap = report.getBitmapFromURL("https://dingo.care2.com/pictures/greenliving/1414/1413160.large.jpg");
        closedReportImage.setImageBitmap(bitmap);

        TextView closedReportExtraDate = findViewById(R.id.closedReportDate);
        closedReportExtraDate.setText(report.getStartTime());

        TextView closedReportyLoction = findViewById(R.id.closedReportyLoction);
        closedReportyLoction.setText(report.getAddress());

        TextView closedReportExtraText = findViewById(R.id.closedReportExtraText);
        closedReportExtraText.setText(report.getFreeText());





    }
}



