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

public class ClosedReportActivity extends BaseActivity {

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

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_closed_report, null);

        mDrawer.addView(thisContainer, 0);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView activeReportExtraText = (TextView) findViewById(R.id.ClosedReportExtraText);
        activeReportExtraText.setText((CharSequence) report.getFreeText());

        TextView activeReportyPhoneNumber = (TextView) findViewById(R.id.ClosedReportyPhoneNumber);
        activeReportyPhoneNumber.setText((CharSequence) report.getPhoneNumber());

        TextView activeReportyLoction = (TextView) findViewById(R.id.ClosedReportyLoction);
        activeReportyLoction.setText((CharSequence) report.getAddress());

    }
}



