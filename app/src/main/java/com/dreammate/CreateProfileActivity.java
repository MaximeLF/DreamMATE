package com.dreammate;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.List;

import tasks.GetCountryListTask;
import tasks.GetFilmTypesTask;
import tasks.GetLanguageListTask;

public class CreateProfileActivity extends AppCompatActivity {
    List<String> countries = new ArrayList<String>();
    List<String> languages = new ArrayList<String>();
    List<String> filmTypes = new ArrayList<String>();

    private MultiAutoCompleteTextView languagesEditText;

    private MultiAutoCompleteTextView moviesEditText;

    private MaskEditText dateEditText;

    private MaskEditText hourEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        countries.add("Loading country list");
        new GetCountryListTask(this).execute();

        languages.add("Loading country list");
        new GetLanguageListTask(this).execute();

        filmTypes.add("Loading film types list");
        new GetFilmTypesTask(this).execute();

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

        AutoCompleteTextView contriesEditText = findViewById(R.id.countriesAutoComplete);
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        contriesEditText.setAdapter(countriesAdapter);

        languagesEditText = findViewById(R.id.languagesMultiAutoComplete);
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        languagesEditText.setAdapter(languagesAdapter);
        languagesEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        dateEditText = findViewById(R.id.profileBirthdayDateEdit);

        hourEditText = findViewById(R.id.profileSleepTomeEdit);

    }

    public void onCountriesResultComputed(List<String> result)
    {
        countries = new ArrayList<>(result);
        AutoCompleteTextView contriesEditText = findViewById(R.id.countriesAutoComplete);
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        contriesEditText.setAdapter(countriesAdapter);

    }

    public void onCitiesResultComputed(List<String> cities)
    {

    }

    public void onLanguagesResultComputed(List<String> result)
    {
        languages = new ArrayList<>(result);
        languagesEditText = findViewById(R.id.languagesMultiAutoComplete);
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages);
        languagesEditText.setAdapter(languagesAdapter);
        languagesEditText.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

    }

    public void onFilmTypesResultComputed(List<String> result)
    {
        filmTypes = new ArrayList<>(result);

    }

    //public void dateShowText(View v)
    //{
    //    Toast.makeText(this, dateEditText.getText(), Toast.LENGTH_SHORT).show();

    //}

    //public void hourShowText()
}
