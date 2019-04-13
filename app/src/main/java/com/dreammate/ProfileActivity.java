package com.dreammate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import model.User;

public class ProfileActivity extends AppCompatActivity {

    private User user;
    /*TextView fname = findViewById(R.id.fname);
    TextView lname = findViewById(R.id.lname);*/
    TextView age;
    TextView languages;
    TextView habbits;
    TextView country;
    TextView occupation;
    TextView gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        age = (TextView) findViewById(R.id.age);
        habbits = (TextView) findViewById(R.id.habbits);
        languages = (TextView) findViewById(R.id.languages);
        country = (TextView) findViewById(R.id.gender);
        occupation = (TextView) findViewById(R.id.occupation);
        gender = (TextView) findViewById(R.id.gender);

        getSupportActionBar().setTitle("ProfileActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");

        /*fname.setText(user.firstName);
        lname.setText(user.lastName);*/
        age.setText(user.getAge());

        Boolean smokes = user.smokes;
        if(smokes) {
            habbits.setText("Smoker");
        }
        else{ habbits.setText("Non smoker");}
        List listLanguages = user.languagesSpoken;
        int numberOfLanguages = listLanguages.size();
        String numberOfLanguagesString = Integer.toString(numberOfLanguages);
        languages.setText(numberOfLanguages + " languages");

        List listCountry = user.originCountries;
        country.setText(listCountry.get(0).toString());

        occupation.setText(user.occupation);
        gender.setText(user.gender);

    }
}
