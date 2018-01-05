package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class ActiveReportActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    private PopupWindow popupWindow;
    private Report report;
    private static final String TAG = "Send Report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_active_report);

        report = (Report) getIntent().getSerializableExtra("Report");

        TextView activeReportStatus = findViewById(R.id.activeReportStatus);
        activeReportStatus.setText(report.statusInHebrew());

        //TextView activeReportExtraText = (TextView) findViewById(R.id.activeReportExtraText);
        //activeReportExtraText.setText((CharSequence) report.getFreeText());

        TextView activeReportPhoneNumber = findViewById(R.id.activeReportPhoneNumber);
        activeReportPhoneNumber.setText(report.getPhoneNumber());

        TextView activeReportLocation = findViewById(R.id.activeReportLocation);
        activeReportLocation.setText(report.getAddress());

        if (report.getExtraPhoneNumber() != null) {
            LinearLayout linearLayout = findViewById(R.id.addAnotherPhoneLinearLayout);
            linearLayout.setVisibility(LinearLayout.VISIBLE);
            EditText addAnotherPhoneNumber = findViewById(R.id.addAnotherPhoneNumber);
            addAnotherPhoneNumber.setText(report.getExtraPhoneNumber());


        }
        //ImageView activeReportImage = (ImageView) findViewById(R.id.activeReportImageView);
        //bitmap = report.getBitmapFromURL("https://3milliondogs.com/blog-assets-two/2014/02/Fotolia_37994094_Subscription_Monthly_XL.jpg");
        //activeReportImage.setImageBitmap(bitmap);

        ImageButton addPhoneNumber = findViewById(R.id.addPhoneNumber);

        addPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = findViewById(R.id.addAnotherPhoneLinearLayout);
                if (linearLayout.getVisibility() == LinearLayout.GONE)
                    linearLayout.setVisibility(LinearLayout.VISIBLE);
                else
                    linearLayout.setVisibility(LinearLayout.GONE);
            }

        });

        ImageButton savePhoneNumber = findViewById(R.id.saveAnotherPhoneNumber);

        savePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView extraPhoneNumber = findViewById(R.id.addAnotherPhoneNumber);
                String extraPhoneNumberString = extraPhoneNumber.getText().toString();
                String error = report.validatePhone(extraPhoneNumberString);
                if (error.equals("")){
                    report.setExtraPhoneNumber(extraPhoneNumberString);
                    report.reportUpdateExtraPhoneNumber();

                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_done_24pp);
                    myIcon.setBounds(0, 0, myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight());
                    extraPhoneNumber.setError("הטלפון נשמר", myIcon);
                }
                else{
                    extraPhoneNumber.setError(error);
                }



            }

        });


        Button cancelReportButton = findViewById(R.id.cancelReport);

        cancelReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                final ViewGroup popupContainer = (ViewGroup) layoutInflater.inflate(R.layout.cancel_report_pop, null);

                //popupWindow = new PopupWindow(popupContainer, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                //popupWindow.showAtLocation(mDrawer, Gravity.NO_GRAVITY, 140, 700);
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

                //popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                //popupWindow.showAtLocation(mDrawer, Gravity.NO_GRAVITY, 120, 400);

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



