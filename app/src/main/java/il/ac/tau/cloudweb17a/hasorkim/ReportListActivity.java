package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;


public class ReportListActivity extends BaseActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_report_list);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_report_list, null);

        mDrawer.addView(thisContainer, 0);

        ArrayList<Report> reportList = new ArrayList<>();

        reportList.add(new Report("Shahar", "Street Sokolov 14, City Ramat-Gan",
                "Dog looks a bit sick", "544764751"
        ));
        reportList.add(new Report("Bar", "Street Arlozorov 51, City Tel-Aviv",
                "", "503724771"
        ));
        reportList.add(new Report("Chan", "Street Hod 33, City Arad",
                "Dog is in my yard", "544999701"
        ));
        reportList.add(new Report("Boris", "Street Tpuach 18, City Yesod Hamahla",
                "Dog is sad", "523864011"
        ));
        reportList.add(new Report("Momo", "Street Shlavim 27, City Petach Tikva",
                "", "524710723"
        ));
        reportList.add(new Report("Gamba", "Street Sokolov 4, City Kiryat-Bialic",
                "I love dogs", "544444891"
        ));

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
        });
    }
}
