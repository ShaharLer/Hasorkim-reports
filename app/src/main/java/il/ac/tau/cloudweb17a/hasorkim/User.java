package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by hen on 30/12/2017.
 */

public class User {

    private static User user;

    private String id;
    private String name;
    private String phoneNumber;
    private Context applicationContext;
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PHONE_NUMBER = "USER_PHONE_NUMBER";
    public Report myLastOpenReport=null;




    private User(Context applicationContext){
        this.applicationContext=applicationContext;
        this.id= FirebaseInstanceId.getInstance().getToken();
        this.name = applicationContext.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE).getString(USER_NAME, null);
        this.phoneNumber=applicationContext.getSharedPreferences(USER_PHONE_NUMBER, Context.MODE_PRIVATE).getString(USER_PHONE_NUMBER, null);
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
        SharedPreferences.Editor editor = applicationContext.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(USER_NAME, name);
        editor.apply();
        this.name=name;
    }

    public void setPhoneNumber(String number) {
        SharedPreferences.Editor editor = applicationContext.getSharedPreferences(USER_PHONE_NUMBER, Context.MODE_PRIVATE).edit();
        editor.putString(USER_PHONE_NUMBER, number);
        editor.apply();
        this.phoneNumber=number;
    }

    public static User getUser(Context applicationContext){
        if(user==null){
            user = new User(applicationContext);
        }
        return user;
    }

    public static User getUserWOContext(){
        return user;
    }

    public Report getMyLastOpenReport(){
        return myLastOpenReport;
    }


    public void setMyLastOpenReport(Report myLastOpenReport) {
        this.myLastOpenReport = myLastOpenReport;
    }

}
