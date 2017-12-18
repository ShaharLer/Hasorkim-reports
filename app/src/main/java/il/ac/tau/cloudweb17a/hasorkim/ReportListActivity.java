package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

import android.widget.TextView;


import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ReportListActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    final String TAG = "ReportListActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    //FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_report_list);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_report_list, null);
        mDrawer.addView(thisContainer, 0);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter mAdapter = new ReportAdapter_v2(FirebaseDatabase.getInstance().getReference().child("reports").limitToLast(70));
        mRecyclerView.setAdapter(mAdapter);

        /*
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("reports")
                .limitToLast(50);

        FirebaseRecyclerOptions<Report_v2> options =
                new FirebaseRecyclerOptions.Builder<Report_v2>()
                        .setQuery(query, Report_v2.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Report_v2, reportListViewHolder>(options) {
            @Override
            public reportListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_item, parent, false);

                return new reportListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(reportListViewHolder holder, int position, Report_v2 model) {
                holder.StatusView.setText(model.status);
                holder.AddressView.setText(model.address);
                holder.timeView.setText(model.date);
            }
        };*/




    }

/*
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
*/


/*    private ArrayList<Report> setList() {
        ArrayList<Report> reportList = new ArrayList<>();

        reportList.add(new Report(1,"Shahar", "1-12-2017 13:52:45", "Street Sokolov 14, City Ramat-Gan",
                "הסורקים בדרך", "Dog looks a bit sick", 544764751, "C58",
                7));
        reportList.add(new Report(2, "Bar",  "1-12-2017 13:32:08", "Street Arlozorov 51, City Tel-Aviv",
                "חדש - בבדיקה", "", 503724771, "",
                4));
        reportList.add(new Report(3, "Chan", "1-12-2017 07:01:12", "Street Hod 33, City Arad",
                "הסורקים בדרך", "Dog is in my yard", 544999701, "S4",
                1));
        reportList.add(new Report(4, "Boris", "29-11-2017 13:09:16", "Street Tpuach 18, City Yesod Hamahla",
                "הכלב לא נמצא", "Dog is sad", 523864011, "N1",
                3));
        reportList.add(new Report(5, "Momo",  "29-11-2017 18:18:59", "Street Shlavim 27, City Petach Tikva",
                "נסרק - הוחזר", "", 524710723, "E12",
                13));
        reportList.add(new Report(6,"Gamba", "28-11-2017 19:48:36", "Street Sokolov 4, City Kiryat-Bialic",
                "נסרק - נמצא בית", "I love dogs", 544444891, "N10",
                6));
        return reportList;
    }*/

    /*private void setDB() {

        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference().child("reports");
        Query lastQuery = reportsRef.limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot querySnapshot : dataSnapshot.getChildren()) {
                    Report_v2 report = querySnapshot.getValue(Report_v2.class);
                    Log.d(TAG, report.getClass().getName());
                    Log.d(TAG, report.date);
                    TextView textView = findViewById(R.id.viewDebug);
                    textView.setText(report.date + " " + report.address);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }*/
}
