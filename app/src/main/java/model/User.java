package model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

public class User implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("email_address")
    public String email;

    @SerializedName("birth_date")
    public String birthDate;

    @SerializedName("encrypted_password")
    public String encrypted;

    @SerializedName("gender")
    public String gender;

    @SerializedName("origin_countries")
    public List<String> originCountries;

    @SerializedName("languages_spoken")
    public List<String> languagesSpoken;

    @SerializedName("staying_country")
    public String stayingCountry;

    @SerializedName("staying_city")
    public String stayingCity;

    @SerializedName("max_budget")
    public int maxBudget;

    @SerializedName("smoker")
    public boolean smokes;

    @SerializedName("occupation")
    public String occupation;

    @SerializedName("description")
    public String description;

    @SerializedName("arrival_date")
    public String arrivalDate;

    @SerializedName("departure_date")
    public String departureDate;

    @SerializedName("film_type")
    public List<String> filmTypes;

    @SerializedName("sleep_time")
    public String sleepTime;

    @SerializedName("Created_date")
    public String createdDate;


    @SerializedName("__v")
    private int v;

    public String hash(String password)
    {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digester.digest(password.getBytes());
            return new String(Base64.encode(bytes, Base64.DEFAULT)).trim(); // The trim is necessary otherwise we get a trailing \n
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public User() {

    }

    public User(String email, String password)
    {
        this.email = email;
        encrypted = hash(password);
    }


    public User(String fName, String lName, String email, String password)
    {
        firstName = fName;
        lastName = lName;
        this.email = email;
        encrypted = hash(password);
    }

    public User(String fName, String lName, String desc){

        firstName = fName;
        firstName = lName;
        description = desc;
    }

    public String getFirstName(){ return firstName;}
    public String getLastName(){ return lastName;}
    public String getDescription(){ return description;}
    public String getBirthdayDate(){ return birthDate;}
    public String getId(){ return id;}

    public void login(AppCompatActivity activity)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("user_id", id);
        ed.putString("first_name", firstName);
        ed.putString("last_name", lastName);
        ed.apply();
    }

    public static void logout(Context context)
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sp.edit();
        ed.remove("user_id");
        ed.remove("first_name");
        ed.remove("last_name");
        ed.apply();
    }

    public String getAge(){
        int year, month, day;
        String [] parts = birthDate.split("-");
        year = Integer.parseInt(parts[0]);
        month = Integer.parseInt(parts[1]);
        day = Integer.parseInt(parts[2].substring(0, 2));

        Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);

        Calendar now = Calendar.getInstance();

        int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;

    }

    @Override
    public String toString() {
        return id + " " + firstName + " " + lastName + " " + email + " " + encrypted;
    }
}
