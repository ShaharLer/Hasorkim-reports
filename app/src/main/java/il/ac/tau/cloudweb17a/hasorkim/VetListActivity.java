package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class VetListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_list);


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
}
