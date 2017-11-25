package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ReportEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_event);
        //startActivity(new Intent(ReportEventActivity.this, MapsActivity.class));


    }

    public void openNewReport(View view)
    {
        Intent intent = new Intent(ReportEventActivity.this, NewEventVetQuestionActivity.class);
        startActivity(intent);
    }
}
