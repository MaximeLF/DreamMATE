package com.dreammate;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tasks.GetCountryListTask;

public class CreateProfileActivity extends AppCompatActivity {
    List<String> countries = new ArrayList<String>();
    List<String> languages = new ArrayList<~>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        countries.add("Loading country list");
        new GetCountryListTask(this).execute();

        languages.add("Loading language list");
        new GetCountryListTask(this).execute();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("user_id", "");
        String firstName = sp.getString("first_name", "");
        String lastName = sp.getString("last_name", "");

        if (!id.equals("")) {
            ((TextView) findViewById(R.id.profileDescription)).setText(firstName + " " + lastName + " (" + id + ")");
        }

        Spinner genderDropdown  = findViewById(R.id.genderSpinner);
        String [] genderItems = new String[]{getString(R.string.female),getString(R.string.male),getString(R.string.other)};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, genderItems);
        genderDropdown.setAdapter(genderAdapter);



    }

    public void onCountriesResultComputed(List<String> result)
    {
        countries = new ArrayList<>(result);
    }

    public void onlanguagesResultComputed(List<String> result)
    {
       languages = new ArrayList<>(result);

}
