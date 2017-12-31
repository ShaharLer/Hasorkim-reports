package il.ac.tau.cloudweb17a.hasorkim;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ClosedReportActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    private PopupWindow popupWindow;
    private ViewGroup popupContainer;
    private Report report;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_active_report);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_closed_report, null);

        mDrawer.addView(thisContainer, 0);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView closedReportystatus = (TextView) findViewById(R.id.closedReportStatus);
        closedReportystatus.setText((CharSequence) report.statusInHebrew());

        ImageView closedReportImage = (ImageView) findViewById(R.id.closedReportImageView);
        bitmap = report.getBitmapFromURL("https://3milliondogs.com/blog-assets-two/2014/02/Fotolia_37994094_Subscription_Monthly_XL.jpg");
        closedReportImage.setImageBitmap(bitmap);

        TextView closedReportExtraDate = (TextView) findViewById(R.id.closedReportDate);
        closedReportExtraDate.setText((CharSequence) report.getStartTime());

        TextView closedReportyLoction = (TextView) findViewById(R.id.closedReportyLoction);
        closedReportyLoction.setText((CharSequence) report.getAddress());

        TextView closedReportExtraText = (TextView) findViewById(R.id.closedReportExtraText);
        closedReportExtraText.setText((CharSequence) report.getFreeText());

        TextView closedReportyPhoneNumber = (TextView) findViewById(R.id.closedReportyPhoneNumber);
        closedReportyPhoneNumber.setText((CharSequence) report.getPhoneNumber());





    }
}



