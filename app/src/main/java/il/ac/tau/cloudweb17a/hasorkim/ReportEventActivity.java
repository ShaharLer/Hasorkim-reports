package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class ReportEventActivity extends BaseActivity {

    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private LinearLayout StartReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View contentView = inflater.inflate(R.layout.activity_report_event, null);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        ViewGroup thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_report_event, null);

        mDrawer.addView(thisContainer, 0);

        //setContentView(R.layout.activity_report_event);
        //startActivity(new Intent(ReportEventActivity.this, MapsActivity.class));

        Button newReportButton = findViewById(R.id.newReport);
        //StartReport = findViewById(R.id.StartReport);

        newReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_new_event_vet_question_pop, null);

                popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
                popupWindow.showAtLocation(mDrawer, Gravity.NO_GRAVITY, 100, 700);
                //mDrawer.setAlpha(0.5F);
                //container.getBackground().setAlpha(120);
                //container.setBackgroundColor(Color.GRAY);

                Button goToVetList = container.findViewById(R.id.goToVetList);
                goToVetList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ReportEventActivity.this, VetListActivity.class);
                        ReportEventActivity.this.startActivity(intent);
                    }
                });

                Button goToReportDetails = container.findViewById(R.id.goToReportDetails);
                goToReportDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ReportEventActivity.this, NewEventMoreDetailsRequestActivity.class);
                        ReportEventActivity.this.startActivity(intent);
                    }
                });


            }
        });
    }
}
