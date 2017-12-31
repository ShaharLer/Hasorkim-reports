package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActiveReportActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    private PopupWindow popupWindow;
    private ViewGroup popupContainer;
    private Report report;
    private Bitmap bitmap;
    private static final String TAG = "Send Report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_active_report, null);

        mDrawer.addView(thisContainer, 0);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView activeReportystatus = (TextView) findViewById(R.id.activeReportStatus);
        activeReportystatus.setText((CharSequence) report.statusInHebrew());

        TextView activeReportExtraText = (TextView) findViewById(R.id.activeReportExtraText);
        activeReportExtraText.setText((CharSequence) report.getFreeText());

        TextView activeReportyPhoneNumber = (TextView) findViewById(R.id.activeReportyPhoneNumber);
        activeReportyPhoneNumber.setText((CharSequence) report.getPhoneNumber());

        TextView activeReportyLoction = (TextView) findViewById(R.id.activeReportyLoction);
        activeReportyLoction.setText((CharSequence) report.getAddress());

        ImageView activeReportImage = (ImageView) findViewById(R.id.activeReportImageView);
        bitmap = report.getBitmapFromURL("https://3milliondogs.com/blog-assets-two/2014/02/Fotolia_37994094_Subscription_Monthly_XL.jpg");
        activeReportImage.setImageBitmap(bitmap);

        Button cancelReportButton = findViewById(R.id.cancelReport);

        cancelReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final ViewGroup popupContainer = (ViewGroup) layoutInflater.inflate(R.layout.cancel_report_pop, null);

                popupWindow = new PopupWindow(popupContainer, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(mDrawer, Gravity.NO_GRAVITY, 140, 700);
                //mDrawer.setAlpha(0.5F);
                //container.getBackground().setAlpha(120);
                //container.setBackgroundColor(Color.GRAY);

                Button confirmCancelReportButton = popupContainer.findViewById(R.id.confirmCancelReport);
                confirmCancelReportButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView cancelReport = (TextView) popupContainer.findViewById(R.id.cancelReportText);
                        String cancelReportText = cancelReport.getText().toString();

                        report.reportUpdateCancellationText(cancelReportText);
                        report.reportUpdateStatus("CANCELED");

                        Intent intent = new Intent(ActiveReportActivity.this, ReportListActivity.class);
                        ActiveReportActivity.this.startActivity(intent);
                    }
                });
            }
        });

        Button whatNowInfo = findViewById(R.id.whatNowInfo);

        whatNowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.what_now_pop, null);

                popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(mDrawer, Gravity.NO_GRAVITY, 120, 400);
                //mDrawer.setAlpha(0.5F);

                Button confirmCancelReportButton = container.findViewById(R.id.whatNowgoToReportList);
                confirmCancelReportButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ActiveReportActivity.this, ReportListActivity.class);
                        ActiveReportActivity.this.startActivity(intent);
                    }
                });
            }
        });
    }


}



