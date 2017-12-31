package il.ac.tau.cloudweb17a.hasorkim;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by hen on 30/12/2017.
 */

public class User {

    private String id;
    private String name;
    private String phoneNumber;
    private static User user;



    private User(String id){
        this.id=id;
        this.name="";
        this.phoneNumber="";
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
         this.name=name;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber=number;
    }

    static User getUser(Context context){
        if(user==null){
            String id = id(context);
            user = new User(id);
        }
        return user;
    }




    private static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

}
