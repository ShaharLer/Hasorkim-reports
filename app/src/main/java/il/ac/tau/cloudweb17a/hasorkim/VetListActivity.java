package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class VetListActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_vet_list);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_vet_list, null);

        Button openVetMapButton = thisContainer.findViewById(R.id.openVetMapButton);
        openVetMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VetListActivity.this, VetListMapActivity.class);
                startActivity(intent);
            }
        });

        Button openReportDetailsButton = thisContainer.findViewById(R.id.openReportDetailsButton);
        openReportDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VetListActivity.this, NewEventMoreDetailsRequestActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<VeterinaryClinic> vetList = new ArrayList<>();

        vetList.add(new VeterinaryClinic("Name1", "Address1",
                "OpeningHours1"));
        vetList.add(new VeterinaryClinic("Name2", "Address2",
                "OpeningHours2"));
        vetList.add(new VeterinaryClinic("Name3", "Address3",
                "OpeningHours3"));
        vetList.add(new VeterinaryClinic("Name4", "Address4",
                "OpeningHours4"));
        vetList.add(new VeterinaryClinic("Name5", "Address5",
                "OpeningHours5"));
        vetList.add(new VeterinaryClinic("Name6", "Address6",
                "OpeningHours6"));

        VeterinaryClinicAdapter adapter = new VeterinaryClinicAdapter(
                this,
                R.layout.vet_list_item,
                vetList
        );
        ListView listView = (ListView) findViewById(R.id.list_view_vets);
        listView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(VetListActivity.this, ReportEventActivity.class));
        finish();

    }

    public void openReportDetails(View view)
    {
        Intent intent = new Intent(VetListActivity.this, NewEventMoreDetailsRequestActivity.class);
        startActivity(intent);
    }

    public void openVetMap(View view)
    {
        Intent intent = new Intent(VetListActivity.this, VetListMapActivity.class);
        startActivity(intent);
    }
}
