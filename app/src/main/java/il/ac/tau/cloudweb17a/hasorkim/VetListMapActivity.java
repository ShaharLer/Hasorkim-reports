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

public class VetListMapActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater;
    private ViewGroup thisContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_vet_list);

        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        thisContainer = (ViewGroup) layoutInflater.inflate(R.layout.activity_vet_list_map, null);

        Button openVetListButton = thisContainer.findViewById(R.id.openVetListButton);
        openVetListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VetListMapActivity.this, VetListActivity.class);
                startActivity(intent);
            }
        });

        Button mapOpenReportDetailsButton = thisContainer.findViewById(R.id.mapOpenReportDetailsButton);
        mapOpenReportDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VetListMapActivity.this, NewEventMoreDetailsRequestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(VetListMapActivity.this, ReportEventActivity.class));
        finish();

    }

    public void openReportDetails(View view)
    {
        Intent intent = new Intent(VetListMapActivity.this, NewEventMoreDetailsRequestActivity.class);
        startActivity(intent);
    }

    public void openVetList(View view)
    {
        Intent intent = new Intent(VetListMapActivity.this, VetListActivity.class);
        startActivity(intent);
    }
}
