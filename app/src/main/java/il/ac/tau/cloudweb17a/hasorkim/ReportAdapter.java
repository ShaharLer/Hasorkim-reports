package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportAdapter extends ArrayAdapter<Report> {
    private final int listLayout;

    public ReportAdapter(Context context,
                                   int listLayout,
                                   ArrayList<Report> Reports) {
        super(context, listLayout, Reports);
        this.listLayout = listLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Report report = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(listLayout, parent, false);
        }
        // Lookup view for data population
        TextView reportStatus = (TextView) convertView.findViewById(R.id.report_status);
        TextView reportAddress = (TextView) convertView.findViewById(R.id.report_address);
        TextView reportTime = (TextView) convertView.findViewById(R.id.report_time);
        // Populate the data into the template view using the data object
        reportStatus.setText(report.getStatus());
        reportAddress.setText(report.getAddress());
        reportTime.setText(report.getStartTime());
        // Return the completed view to render on screen
        return convertView;
    }

}


//mDrawer.addView(thisContainer, 0);

/*
ReportAdapter adapter = new ReportAdapter(
        this,
        R.layout.report_list_item,
        reportList
);

ListView listView = (ListView) findViewById(R.id.list_view_reports);
listView.setAdapter(adapter);
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

        //Object o = ((ListView)parent).getItemAtPosition(position);
        if (position == 0)
        {
            Intent intent = new Intent(ReportListActivity.this, ActiveReportActivity.class);
            startActivity(intent);
        }
    }
});*/
