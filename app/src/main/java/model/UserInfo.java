package model;

import com.google.gson.annotations.SerializedName;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;

public class UserInfo {

    @SerializedName("_id")
    public String id;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("email_address")
    public String email;

    @SerializedName("encrypted_password")
    public String encrypted;

    public UserInfo(String fName, String lName, String Email, String pwd) {
        firstName = fName;
        lastName = lName;
        email = Email;
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digester.digest(pwd.getBytes());
            encrypted = new String(Base64.encode(bytes, Base64.DEFAULT));
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
