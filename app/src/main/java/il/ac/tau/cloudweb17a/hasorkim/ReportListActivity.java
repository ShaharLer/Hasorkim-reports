package il.ac.tau.cloudweb17a.hasorkim;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.google.firebase.database.FirebaseDatabase;


public class ReportListActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;
    final String TAG = "ReportListActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


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

        RecyclerView.Adapter mAdapter = new ReportAdapter(FirebaseDatabase.getInstance().getReference().child("reports").limitToLast(70));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);


    }

}
