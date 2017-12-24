package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class ReportListActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    private ArrayList<Report> reportsList;
    private static final String TAG = "ReportListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_report_list);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_report_list, null);

        mDrawer.addView(thisContainer, 0);

        reportsList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reports");
        ref.orderByChild("startTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {

                    Report report = messageSnapshot.getValue(Report.class);
                    report.setId(messageSnapshot.getKey());
                    reportsList.add(report);
                }

                Collections.reverse(reportsList);

                ReportAdapter adapter = new ReportAdapter(
                        ReportListActivity.this,
                        R.layout.report_list_item,
                        reportsList
                );
                ListView listView = (ListView) findViewById(R.id.list_view_reports);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                        Report report = (Report) ((ListView)parent).getItemAtPosition(position);

                        Intent intent = new Intent(ReportListActivity.this, ActiveReportActivity.class);
                        intent.putExtra("Report", report);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
