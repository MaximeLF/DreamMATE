package com.dreammate;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

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
    List<String> cities = new ArrayList<String>();


    private MultiAutoCompleteTextView originCountriesMultiAutoComplete;
    private AutoCompleteTextView wantedCountryAutoComplete;
    private AutoCompleteTextView wantedCityAutoComplete;
    private MultiAutoCompleteTextView spokenLanguagesMultiAutoComplete;

    private MaskEditText dateEditText;
    private MaskEditText hourEditText;
    private MaskEditText MoveInDateEditText;
    private MaskEditText MoveOutDateEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Button finish = (Button) findViewById(R.id.finishButton);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startCreateProfile();
            }
        });

        countries.add("Loading country list");
        new GetCountryListTask(this).execute();

        languages.add("Loading language list");
        new GetLanguageListTask(this).execute();

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



        dateEditText = findViewById(R.id.profileBirthdayDateEdit);

        hourEditText = findViewById(R.id.profileSleepTomeEdit);

        MoveInDateEditText = findViewById(R.id.profileMoveInDateEdit);

        MoveOutDateEditText = findViewById(R.id.profileMoveOutDateEdit);

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

    public void startCreateProfile()
    {
        String dateOfBirth = ((EditText)findViewById(R.id.profileBirthdayDateEdit)).getText().toString();

        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        int genderRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        View genderRadioButton = genderRadioGroup.findViewById(genderRadioButtonId);
        int genderIdx = genderRadioGroup.indexOfChild(genderRadioButton);
        RadioButton genderRadio = (RadioButton) genderRadioGroup.getChildAt(genderIdx);
        String gender = genderRadio.getText().toString();

        String originCountries = originCountriesMultiAutoComplete.getText().toString().trim();
        String[] singleOriginCountries = originCountries.split("\\s*,\\s*");

        String languages = spokenLanguagesMultiAutoComplete.getText().toString().trim();
        String[] singleLanguages = languages.split("\\s*,\\s*");

        String wantedCountry = ((EditText)findViewById(R.id.wantedCountryAutoComplete)).getText().toString();

        String wantedCity = ((EditText)findViewById(R.id.wantedCityAutoComplete)).getText().toString();

        int budget = Integer.parseInt(((EditText)findViewById(R.id.profileBudgetEdit)).getText().toString());

        RadioGroup smokerRadioGroup = findViewById(R.id.smokeRadioGroup);
        int smokerRadioButtonId = smokerRadioGroup.getCheckedRadioButtonId();
        View smokerRadioButton = smokerRadioGroup.findViewById(smokerRadioButtonId);
        int smokerIdx = smokerRadioGroup.indexOfChild(smokerRadioButton);
        RadioButton smokerRadio = (RadioButton) smokerRadioGroup.getChildAt(smokerIdx);
        String smoker = smokerRadio.getText().toString();

        RadioGroup occupationRadioGroup = findViewById(R.id.occupationRadioGroup);
        int occupationRadioButtonId = occupationRadioGroup.getCheckedRadioButtonId();
        View occupationRadioButton = occupationRadioGroup.findViewById(occupationRadioButtonId);
        int occupationIdx = occupationRadioGroup.indexOfChild(occupationRadioButton);
        RadioButton occupationRadio = (RadioButton) occupationRadioGroup.getChildAt(occupationIdx);
        String occupation = occupationRadio.getText().toString();




        Log.d("teresa", gender + " " + dateOfBirth + " " + budget);
    }

}
