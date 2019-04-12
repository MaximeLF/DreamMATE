package com.dreammate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import model.User;
import tasks.GetCitiesForCountryTask;
import tasks.GetCountryListTask;
import tasks.GetFilmTypesTask;
import tasks.GetLanguageListTask;
import tasks.SendUserInfoTask;

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

    private Button birthDateButton;
    private Button moveInDateButton;
    private Button moveOutDateButton;

    private Date birthDate;
    private Date moveInDate;
    private Date moveOutDate;

    private Spinner sleepTimeSpinner;


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



        birthDateButton = findViewById(R.id.profileBirthdayDateButton);
        moveInDateButton = findViewById(R.id.profileMoveInDateButton);
        moveOutDateButton = findViewById(R.id.profileMoveOutDateButton);


        birthDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int day, month, year;
                Calendar calendar = Calendar.getInstance();

                if (birthDate != null) {
                    calendar = new GregorianCalendar();
                    calendar.setTime(birthDate);
                }

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(CreateProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cldr = Calendar.getInstance();
                                cldr.set(year, monthOfYear, dayOfMonth, 0, 0);
                                birthDate = new Date(cldr.getTimeInMillis());
                                birthDateButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                picker.show();
            }
        });

        moveInDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day, month, year;
                Calendar calendar = Calendar.getInstance();

                if (moveInDate != null) {
                    calendar = new GregorianCalendar();
                    calendar.setTime(moveInDate);
                }

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(CreateProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cldr = Calendar.getInstance();
                                cldr.set(year, monthOfYear, dayOfMonth, 0, 0);
                                moveInDate = new Date(cldr.getTimeInMillis());
                                moveInDateButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(System.currentTimeMillis());
                picker.show();
            }
        });

        moveOutDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (moveInDate == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.select_move_in), Toast.LENGTH_SHORT).show();
                    return;
                }

                int day, month, year;

                Calendar calendar = new GregorianCalendar();

                if (moveOutDate == null) {
                    calendar.setTime(moveInDate);
                }
                else {
                    calendar.setTime(moveOutDate);
                }

                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(CreateProfileActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar cldr = Calendar.getInstance();
                                cldr.set(year, monthOfYear, dayOfMonth, 0, 0);
                                moveOutDate = new Date(cldr.getTimeInMillis());
                                moveOutDateButton.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(moveInDate.getTime());
                picker.show();
            }
        });

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
        User user = new User();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("user_id", "");
        String firstName = sp.getString("first_name", "");
        String lastName = sp.getString("last_name", "");

        user.id = id;
        user.firstName = firstName;
        user.lastName = lastName;




        if (birthDate == null) {
            Toast.makeText(this, getString(R.string.select_birth_date), Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(birthDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String birthDateString = year + "-" + month + "-" + day;

        user.birthDate = birthDateString;




        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        int genderRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();

        if (genderRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.select_gender), Toast.LENGTH_SHORT).show();
            return;
        }
        View genderRadioButton = genderRadioGroup.findViewById(genderRadioButtonId);
        int genderIdx = genderRadioGroup.indexOfChild(genderRadioButton);
        RadioButton genderRadio = (RadioButton) genderRadioGroup.getChildAt(genderIdx);
        String gender = genderRadio.getText().toString();

        user.gender = gender;




        user.originCountries = new ArrayList<>();
        String originCountries = originCountriesMultiAutoComplete.getText().toString().trim();
        String[] singleOriginCountries = originCountries.split("\\s*,\\s*");

        if (singleOriginCountries.length == 0) {
            Toast.makeText(this, getString(R.string.select_origin_countries), Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < singleOriginCountries.length; i++) {
            user.originCountries.add(singleOriginCountries[i].trim());
        }




        user.languagesSpoken = new ArrayList<>();
        String languages = spokenLanguagesMultiAutoComplete.getText().toString().trim();
        String[] singleLanguages = languages.split("\\s*,\\s*");

        if (singleLanguages.length == 0) {
            Toast.makeText(this, getString(R.string.select_languages), Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < singleLanguages.length; i++) {
            user.languagesSpoken.add(singleLanguages[i].trim());
        }





        String wantedCountry = ((EditText)findViewById(R.id.wantedCountryAutoComplete)).getText().toString();
        if (wantedCountry.trim().equals("")) {
            Toast.makeText(this, getString(R.string.select_destination_country), Toast.LENGTH_SHORT).show();
            return;
        }
        user.stayingCountry = wantedCountry;





        String wantedCity = ((EditText)findViewById(R.id.wantedCityAutoComplete)).getText().toString();
        if (wantedCity.trim().equals("")) {
            Toast.makeText(this, getString(R.string.select_destination_city), Toast.LENGTH_SHORT).show();
            return;
        }
        user.stayingCity = wantedCity;





        if (moveInDate == null) {
            Toast.makeText(this, getString(R.string.select_birth_date), Toast.LENGTH_SHORT).show();
            return;
        }
        calendar.setTime(moveInDate);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);
        String moveInDateString = year + "-" + month + "-" + day;

        user.arrivalDate = moveInDateString;






        try {
            int budget = Integer.parseInt(((EditText) findViewById(R.id.profileBudgetEdit)).getText().toString());
            user.maxBudget = budget;
        }
        catch (Exception e) {
            Toast.makeText(this, getString(R.string.select_budget), Toast.LENGTH_SHORT).show();
            return;
        }





        RadioGroup sleepTimeRadioGroup = findViewById(R.id.profileSleepTimeRadioGroup);
        int sleepTimeRadioButtonId = sleepTimeRadioGroup.getCheckedRadioButtonId();

        if (sleepTimeRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.select_sleep_time), Toast.LENGTH_SHORT).show();
            return;
        }
        View sleepTimeRadioButton = sleepTimeRadioGroup.findViewById(sleepTimeRadioButtonId);
        int sleepTimeIdx = sleepTimeRadioGroup.indexOfChild(sleepTimeRadioButton);
        RadioButton sleepTimeRadio = (RadioButton) sleepTimeRadioGroup.getChildAt(sleepTimeIdx);
        String sleepTime = sleepTimeRadio.getText().toString();

        user.sleepTime = sleepTime;






        RadioGroup smokerRadioGroup = findViewById(R.id.smokeRadioGroup);
        int smokerRadioButtonId = smokerRadioGroup.getCheckedRadioButtonId();

        if (smokerRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.select_smoker), Toast.LENGTH_SHORT).show();
            return;
        }
        View smokerRadioButton = smokerRadioGroup.findViewById(smokerRadioButtonId);
        int smokerIdx = smokerRadioGroup.indexOfChild(smokerRadioButton);
        RadioButton smokerRadio = (RadioButton) smokerRadioGroup.getChildAt(smokerIdx);
        String smoker = smokerRadio.getText().toString();

        user.smokes = smoker.equals(getString(R.string.yes));





        RadioGroup occupationRadioGroup = findViewById(R.id.occupationRadioGroup);
        int occupationRadioButtonId = occupationRadioGroup.getCheckedRadioButtonId();

        if (occupationRadioButtonId == -1) {
            Toast.makeText(this, getString(R.string.select_occupation), Toast.LENGTH_SHORT).show();
            return;
        }
        View occupationRadioButton = occupationRadioGroup.findViewById(occupationRadioButtonId);
        int occupationIdx = occupationRadioGroup.indexOfChild(occupationRadioButton);
        RadioButton occupationRadio = (RadioButton) occupationRadioGroup.getChildAt(occupationIdx);
        String occupation = occupationRadio.getText().toString();

        user.occupation = occupation;


        String description = ((EditText) findViewById(R.id.profileDescriptionEdit)).getText().toString();
        user.description = description.trim();


        // call task to send user to server
        new SendUserInfoTask(this).execute(user);
    }




    public void onUserInfoSent(Boolean worked) {
        if (worked) {
            Toast.makeText(getApplicationContext(), getString(R.string.user_info_success), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.user_info_failure), Toast.LENGTH_SHORT).show();
        }
    }
}
