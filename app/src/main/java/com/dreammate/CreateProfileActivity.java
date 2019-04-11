package com.dreammate;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tasks.GetCitiesForCountryTask;
import tasks.GetCountryListTask;
import tasks.GetFilmTypesTask;
import tasks.GetLanguageListTask;

public class CreateProfileActivity extends AppCompatActivity
{
    CreateProfileActivity thisActivity = this;
    List<String> countries = new ArrayList<String>();
    List<String> languages = new ArrayList<String>();
    List<String> filmTypes = new ArrayList<String>();
    List<String> cities = new ArrayList<String>();


    private MultiAutoCompleteTextView originCountriesMultiAutoComplete;
    private AutoCompleteTextView wantedCountryAutoComplete;
    private AutoCompleteTextView wantedCityAutoComplete;
    private MultiAutoCompleteTextView spokenLanguagesMultiAutoComplete;
    private MultiAutoCompleteTextView filmTypesMultiAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        countries.add("Loading country list");
        new GetCountryListTask(this).execute();

        languages.add("Loading language list");
        new GetLanguageListTask(this).execute();

        filmTypes.add("Loading film types list");
        new GetFilmTypesTask(this).execute();

        cities.add("Select a country first!");


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("user_id", "");
        String firstName = sp.getString("first_name", "");
        String lastName = sp.getString("last_name", "");



        originCountriesMultiAutoComplete = findViewById(R.id.originCountriesMultiAutoComplete);
        ArrayAdapter<String> originCountriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        originCountriesMultiAutoComplete.setAdapter(originCountriesAdapter);
        originCountriesMultiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        wantedCountryAutoComplete = findViewById(R.id.wantedCountryAutoComplete);
        ArrayAdapter<String> wantedCountryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        wantedCountryAutoComplete.setAdapter(wantedCountryAdapter);


        wantedCityAutoComplete = findViewById(R.id.wantedCityAutoComplete);
        ArrayAdapter<String> wantedCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        wantedCityAutoComplete.setAdapter(wantedCityAdapter);


        spokenLanguagesMultiAutoComplete = findViewById(R.id.spokenLanguagesMultiAutoComplete);
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        spokenLanguagesMultiAutoComplete.setAdapter(languagesAdapter);
        spokenLanguagesMultiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


        filmTypesMultiAutoComplete = findViewById(R.id.moviesMultiAutoComplete);
        ArrayAdapter<String> moviesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filmTypes);
        filmTypesMultiAutoComplete.setAdapter(moviesAdapter);
        filmTypesMultiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }

    public void onCountriesResultComputed(List<String> result)
    {
        countries = new ArrayList<>(result);

        ArrayAdapter<String> originCountriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        originCountriesMultiAutoComplete.setAdapter(originCountriesAdapter);

        ArrayAdapter<String> wantedCountryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        wantedCountryAutoComplete.setAdapter(wantedCountryAdapter);
        wantedCountryAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_country = parent.getItemAtPosition(position).toString();

                cities = new ArrayList<>();
                cities.add("Loading cities list");

                Log.d("lua", "Country selection detected: " + selected_country);

                new GetCitiesForCountryTask(thisActivity).execute(selected_country);
            }
        });
    }

    public void onCitiesResultComputed(List<String> result)
    {
        cities = new ArrayList<>(result);

        ArrayAdapter<String> wantedCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        wantedCityAutoComplete.setAdapter(wantedCityAdapter);
    }

    public void onLanguagesResultComputed(List<String> result)
    {
        languages = new ArrayList<>(result);

        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        spokenLanguagesMultiAutoComplete.setAdapter(languagesAdapter);
        spokenLanguagesMultiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    public void onFilmTypesResultComputed(List<String> result)
    {
        filmTypes = new ArrayList<>(result);

        ArrayAdapter<String> moviesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filmTypes);
        filmTypesMultiAutoComplete.setAdapter(moviesAdapter);
        filmTypesMultiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }
}
