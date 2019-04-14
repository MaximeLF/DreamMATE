package com.dreammate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import model.User;

public class ProfileActivity extends AppCompatActivity {

    private User viewedUser;

    private ImageView profileImage;

    private TextView nameTV;
    private TextView ageTV;
    private TextView telephoneTV;
    private TextView emailTV;

    private TextView originTv;
    private TextView placeTV;
    private TextView fromTV;
    private TextView toTV;
    private TextView languagesTV;
    private TextView budgetTV;
    private TextView sleepTV;
    private TextView occupationTV;
    private TextView smokerTV;
    private TextView descriptionTV;


    private String formattedDate(String s) {
        int year, month, day;
        String [] parts = s.split("-");
        year = Integer.parseInt(parts[0]);
        month = Integer.parseInt(parts[1]);
        day = Integer.parseInt(parts[2].substring(0, 2));
        return day + "/" + month + "/" + year;
    }

    private void findViews() {
        profileImage = findViewById(R.id.profileImageView);

        nameTV = findViewById(R.id.nameText);
        ageTV = findViewById(R.id.ageText);
        telephoneTV = findViewById(R.id.telephoneText);
        emailTV = findViewById(R.id.mailText);

        originTv = findViewById(R.id.origin);
        placeTV = findViewById(R.id.destination);
        fromTV = findViewById(R.id.dates);
        toTV = findViewById(R.id.dates2);
        languagesTV = findViewById(R.id.languages);
        budgetTV = findViewById(R.id.budget);
        sleepTV = findViewById(R.id.sleep);
        occupationTV = findViewById(R.id.occupation);
        smokerTV = findViewById(R.id.smokesTitle);
        descriptionTV = findViewById(R.id.description);
    }

    public void getViewedUser() {
        viewedUser = new User();
        viewedUser = (User) getIntent().getSerializableExtra("user");
    }

    private void fillViewsWithUserInfo() {
        if (viewedUser.gender.equals(getString(R.string.male))) {
            profileImage.setImageResource(R.drawable.male_avatar);
        }
        else if (viewedUser.gender.equals(getString(R.string.female))) {
            profileImage.setImageResource(R.drawable.girl_avatar);
        }
        else {
            profileImage.setImageResource(R.drawable.other_avatar);
        }


        String name = viewedUser.fullName();
        nameTV.setText(name);

        String age = Integer.toString(viewedUser.getAge()) + " " + getString(R.string.years);
        ageTV.setText(age);

        String email = viewedUser.email;
        emailTV.setText(email);

        String telephone = viewedUser.telephone;
        telephoneTV.setText(telephone);

        String originString = "";
        for (int i = 0; i < viewedUser.originCountries.size(); i++) {
            originString += viewedUser.originCountries.get(i) + ", ";
        }
        originString = originString.substring(0, originString.length() - 2);
        originTv.setText(originString);

        String placeString = viewedUser.stayingCity + ", " + viewedUser.stayingCountry;
        placeTV.setText(placeString);

        String from = formattedDate(viewedUser.arrivalDate);
        fromTV.setText(from);

        String to;
        if (viewedUser.departureDate == null) {
            to = "-";
        }
        else {
            to = formattedDate(viewedUser.departureDate);
        }
        toTV.setText(to);


        String languageString = "";
        for (int i = 0; i < viewedUser.languagesSpoken.size(); i++) {
            languageString += viewedUser.languagesSpoken.get(i) + ", ";
        }
        languageString = languageString.substring(0, languageString.length() - 2);
        languagesTV.setText(languageString);


        String budgetString = Integer.toString(viewedUser.maxBudget) + " " + getString(R.string.euro);
        budgetTV.setText(budgetString);

        String sleepString = viewedUser.sleepTime;
        sleepTV.setText(sleepString);

        String occupationString = viewedUser.occupation;
        occupationTV.setText(occupationString);

        boolean smokes = viewedUser.smokes;
        String smokesString = "";
        if (smokes) {
            smokesString = getString(R.string.smokes);
        }
        else {
            smokesString = getString(R.string.not_smokes);
        }
        smokerTV.setText(smokesString);

        String descriptionString = viewedUser.description;
        descriptionTV.setText(descriptionString);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        findViews();

        getViewedUser();

        fillViewsWithUserInfo();
    }
}
