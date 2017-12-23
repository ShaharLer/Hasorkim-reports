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

        reportList.add(new Report("Shahar", "1-12-2017 13:52:45", "Street Sokolov 14, City Ramat-Gan",
                "הסורקים בדרך", "Dog looks a bit sick", 544764751, "C58",
         7));
        reportList.add(new Report("Bar",  "1-12-2017 13:32:08", "Street Arlozorov 51, City Tel-Aviv",
                "חדש - בבדיקה", "", 503724771, "",
                4));
        reportList.add(new Report("Chan", "1-12-2017 07:01:12", "Street Hod 33, City Arad",
                "הסורקים בדרך", "Dog is in my yard", 544999701, "S4",
                1));
        reportList.add(new Report("Boris", "29-11-2017 13:09:16", "Street Tpuach 18, City Yesod Hamahla",
                "הכלב לא נמצא", "Dog is sad", 523864011, "N1",
                3));
        reportList.add(new Report("Momo",  "29-11-2017 18:18:59", "Street Shlavim 27, City Petach Tikva",
                "נסרק - הוחזר", "", 524710723, "E12",
                13));
        reportList.add(new Report("Gamba", "28-11-2017 19:48:36", "Street Sokolov 4, City Kiryat-Bialic",
                "נסרק - נמצא בית", "I love dogs", 544444891, "N10",
                6));

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
