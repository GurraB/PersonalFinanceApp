package se.mah.af6589.personalfinanceapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gustaf Bohlin on 12/09/2017.
 */

public class User implements Parcelable {

    private String username, password, name, lastname;

    public User(String username, String password, String name, String lastname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.lastname = lastname;
    }

    public User(Parcel in) {
        String userinformation[] = new String[4];
        in.readStringArray(userinformation);
        username = userinformation[0];
        password = userinformation[1];
        name = userinformation[2];
        lastname = userinformation[3];
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {username, password, name, lastname});
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
