package il.ac.tau.cloudweb17a.hasorkim;

import java.util.ArrayList;

/**
 * Created by workhourse on 12/23/17.
 */

public class ReportsList {
    private ArrayList<Report> list;

    public ReportsList() {
        this.list =  new ArrayList<>();
        //FirebaseDatabase.getInstance().getReference().child("reports").limitToLast(70)
    }
}
