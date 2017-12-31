package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ActiveReportActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    private PopupWindow popupWindow;
    private ViewGroup popupContainer;
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_active_report);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_active_report, null);

        mDrawer.addView(thisContainer, 0);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView activeReportExtraText = (TextView) findViewById(R.id.ActiveReportExtraText);
        activeReportExtraText.setText((CharSequence) report.getFreeText());

        TextView activeReportyPhoneNumber = (TextView) findViewById(R.id.ActiveReportyPhoneNumber);
        activeReportyPhoneNumber.setText((CharSequence) report.getPhoneNumber());

        TextView activeReportyLoction = (TextView) findViewById(R.id.ActiveReportyLoction);
        activeReportyLoction.setText((CharSequence) report.getAddress());

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



