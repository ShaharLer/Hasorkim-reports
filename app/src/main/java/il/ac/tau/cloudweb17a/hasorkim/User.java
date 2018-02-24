package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.UUID;

public class User {

    private static User user;

    private String id;
    private String name;
    private String phoneNumber;
    private SharedPreferences prefs;
    //public Report myLastOpenReport=null;

    public static final String USER_NAME = "display_name";
    public static final String USER_PHONE_NUMBER = "phone_number";


    private User(Context applicationContext) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        if (this.prefs.contains(applicationContext.getString(R.string.userId))) {
            this.id = this.prefs.getString(applicationContext.getString(R.string.userId), "");
        } else {
            UUID uuid = UUID.randomUUID();
            this.id = uuid.toString();
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putString(applicationContext.getString(R.string.userId), this.id);
            editor.apply();
        }
        this.name = prefs.getString(USER_NAME, null);
        this.phoneNumber = prefs.getString(USER_PHONE_NUMBER, null);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(USER_NAME, name);
        editor.apply();
        this.name = name;
    }

    public void setPhoneNumber(String number) {
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putString(USER_PHONE_NUMBER, number);
        editor.apply();
        this.phoneNumber = number;
    }

    public static User getUser(Context applicationContext) {
        if (user == null) {
            user = new User(applicationContext);
        }
        return user;
    }

    public static User getUserWOContext() {
        return user;
    }

/*    public Report getMyLastOpenReport(){
        return myLastOpenReport;
    }


    public void setMyLastOpenReport(Report myLastOpenReport) {
        this.myLastOpenReport = myLastOpenReport;
    }*/


}
