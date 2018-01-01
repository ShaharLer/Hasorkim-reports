package il.ac.tau.cloudweb17a.hasorkim;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by hen on 18/12/2017.
 */


public class reportListViewHolder extends RecyclerView.ViewHolder {
    public final TextView StatusView;
    public final TextView AddressView;
    public final TextView timeView;

    public reportListViewHolder(View itemView) {
            super(itemView);
            StatusView = itemView.findViewById(R.id.report_status);
            AddressView = itemView.findViewById(R.id.report_address);
            timeView = itemView.findViewById(R.id.report_time);
    }
}
