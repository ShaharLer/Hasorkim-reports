package il.ac.tau.cloudweb17a.hasorkim;

/**
 * Created by hen on 30/12/2017.
 */

public class User {

    private String id;
    private String name;
    private String phoneNumber;



    private User(){
        this.id="12#3";
        this.name="אלי";
        this.phoneNumber="";
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

    static User getUser(){
        return new User();
    }

}
