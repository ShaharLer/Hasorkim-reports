package il.ac.tau.cloudweb17a.hasorkim;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VeterinaryClinicAdapter extends RecyclerView.Adapter<VeterinaryClinicAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(VeterinaryClinic item);
    }

    private List<VeterinaryClinic> vetClinicsDataset;
    private final OnItemClickListener listener;
    private static final String MINUTES_DRIVING = " דקות נסיעה";

    // Provide a suitable constructor (depends on the kind of dataset)
    public VeterinaryClinicAdapter(List<VeterinaryClinic> vetClinicsDataset, OnItemClickListener listener) {
        this.vetClinicsDataset = vetClinicsDataset;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VeterinaryClinicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vet_list_item, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(vetClinicsDataset.get(position), listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return vetClinicsDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        // each data item is just a string in this case
        TextView vetNameTextView;
        TextView vetAddressTextView;
        TextView vetDistanceFromOriginTextView;

        public ViewHolder(View view) {
            super(view);
            vetNameTextView               = view.findViewById(R.id.vet_name);
            vetAddressTextView            = view.findViewById(R.id.vet_address);
            vetDistanceFromOriginTextView = view.findViewById(R.id.vet_distance_from_origin);
        }

        public void bind(final VeterinaryClinic item, final OnItemClickListener listener) {
            vetNameTextView.setText(item.getName());
            vetAddressTextView.setText(item.getAddress());
            vetDistanceFromOriginTextView.setText("כ-" + item.getDistanceFromOrigin() + MINUTES_DRIVING);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
