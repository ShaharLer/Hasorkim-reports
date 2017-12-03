package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class ActiveReportActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_active_report);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_active_report, null);

        mDrawer.addView(thisContainer, 0);

        Button cancelReportButton = findViewById(R.id.cancelReport);
        //StartReport = findViewById(R.id.StartReport);

        cancelReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_cancel_report_pop, null);

                popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.showAtLocation(mDrawer, Gravity.NO_GRAVITY, 100, 700);
                mDrawer.setAlpha(0.5F);
                //container.getBackground().setAlpha(120);
                //container.setBackgroundColor(Color.GRAY);

                Button confirmCancelReportButton = container.findViewById(R.id.confirmCancelReport);
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



