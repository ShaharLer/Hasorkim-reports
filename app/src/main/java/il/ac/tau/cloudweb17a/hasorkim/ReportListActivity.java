package il.ac.tau.cloudweb17a.hasorkim;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ReportListActivity extends AppCompatActivity {
    final String TAG = "ReportListActivity";
    ProgressBar mProgressBar;
    RecyclerView mRecyclerView;
    TextView mNoReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        mNoReports = findViewById(R.id.no_reports);
        mProgressBar = findViewById(R.id.report_list_progress_bar);
        mRecyclerView = findViewById(R.id.my_reports_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // setting the decoration for the reports list
        ListItemDecoration decoration = new ListItemDecoration(this, Color.LTGRAY, 1f);
        mRecyclerView.addItemDecoration(decoration);

        //setting up a user object for the list
        User.getUser(getApplicationContext());

        RecyclerView.Adapter mAdapter = new ReportAdapter();
        //RecyclerView.Adapter mAdapter = new ReportAdapter(FirebaseDatabase.getInstance().getReference().child("reports").limitToLast(70)); TODO delete after debug
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL)); TODO delete this after debug
        mRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);
        if (mAdapter.getItemCount() == 0) {
            mNoReports.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
        }

    }
}
