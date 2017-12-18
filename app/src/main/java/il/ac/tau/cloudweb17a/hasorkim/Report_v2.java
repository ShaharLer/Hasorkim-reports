package il.ac.tau.cloudweb17a.hasorkim;

import java.util.ArrayList;

/**
 * Created by hen on 17/12/2017.
 */

public class Report_v2 {


    public String status;                       //  [new, view?, assigned, canceled by user, closed by manager]
    public String address;
    public String date;



/*    public String closing_reason;               //  [closed list - need to think about]
    public ArrayList<String> imagesLocations;   //  list of storage url
    public String reporter_id;
    public ArrayList<String> ready_scanners;    //  ready to take the report
    public String dispatched_scanner;
    public String comment;


    //public String alternative_phone_number;
    //public String history_list_id;  [date, manager, action(e.g. Changed from new to assign)]?




    public Report_v2() {
    }

/*    public Report_v2(String status, String closing_reason, ArrayList<String> imagesLocations, String date, String address, String reporter_id, ArrayList<String>  ready_scanners, String dispatched_scanner, String comment) {
        this.status = status;
        this.closing_reason = closing_reason;
        this.imagesLocations = imagesLocations;
        this.date = date;
        this.address = address;
        this.reporter_id = reporter_id;
        this.ready_scanners = ready_scanners;
        this.dispatched_scanner = dispatched_scanner;
        this.comment = comment;
    }*/


    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
