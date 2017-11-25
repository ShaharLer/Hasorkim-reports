package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VeterinaryClinicAdapter extends ArrayAdapter<VeterinaryClinic> {
    private final int listLayout;

    public VeterinaryClinicAdapter(Context context,
                                   int listLayout,
                                   ArrayList<VeterinaryClinic> veterinaryClinics) {
        super(context, listLayout, veterinaryClinics);
        this.listLayout = listLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        VeterinaryClinic veterinaryClinic = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(listLayout, parent, false);
        }
        // Lookup view for data population
        TextView vetName = (TextView) convertView.findViewById(R.id.vet_name);
        TextView vetAddress = (TextView) convertView.findViewById(R.id.vet_address);
        TextView vetOpeningHours = (TextView) convertView.findViewById(R.id.vet_opening_hours);
        // Populate the data into the template view using the data object
        vetName.setText(veterinaryClinic.getName());
        vetAddress.setText(veterinaryClinic.getAddress());
        vetOpeningHours.setText(veterinaryClinic.getOpeningHours());
        // Return the completed view to render on screen
        return convertView;
    }

}
