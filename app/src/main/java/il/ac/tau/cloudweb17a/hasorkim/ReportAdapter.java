package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by hen on 18/12/2017.
 */

public class ReportAdapter_v2 extends RecyclerView.Adapter<ReportAdapter_v2.ReportViewHolder> {
    private ArrayList<Report> mDataset= new ArrayList<>();
    final String TAG = "ReportAdapter";



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView StatusView;
        public final TextView AddressView;
        public final TextView timeView;
        final String TAG = "ViewHolder";
        private Report mReport;
        private Context context;

        public ReportViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            StatusView = v.findViewById(R.id.report_status);
            AddressView = v.findViewById(R.id.report_address);
            timeView = v.findViewById(R.id.report_time);
            context = v.getContext();
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, ActiveReportActivity.class);
            intent.putExtra("Report", mReport);
            context.startActivity(intent);
            Log.d(TAG, mReport.getStartTime());
        }

        public void bindReport(Report report) {
            mReport = report;
            StatusView.setText(report.getStatus());
            AddressView.setText(report.getAddress());
            timeView.setText(report.getStartTime());
        }

    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ReportAdapter_v2(Query query) {
        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference().child("reports");
        Query lastQuery = reportsRef.limitToLast(10);
        lastQuery.addChildEventListener(new ChildEventListener() {
            /* @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot querySnapshot : dataSnapshot.getChildren()) {
                    Report_v2 report = querySnapshot.getValue(Report_v2.class);
                    mDataset.add(report);
                }
                notifyDataSetChanged();
            }*/

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Report report = dataSnapshot.getValue(Report.class);
                mDataset.add(report);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.w(TAG, "======================"+previousChildName); //TODO
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_list_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ReportViewHolder vh = new ReportViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ReportViewHolder holder, int position) {
        //mDataset.get(position).getAddress();
        //holder.StatusView.setText(mDataset.get(position).getStatus());
        //holder.AddressView.setText(mDataset.get(position).getAddress());
        //holder.timeView.setText(.getDate());
        holder.bindReport(mDataset.get(position));
    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}