package com.dreammate;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import model.User;
import tasks.GetCitiesForCountryTask;
import tasks.GetCountryListTask;
import tasks.GetLanguageListTask;
import tasks.GetUserInfoTask;
import tasks.SendUserInfoTask;

public class MyProfileActivity extends AppCompatActivity
{
    User user;

    MyProfileActivity thisActivity = this;
    List<String> countries = new ArrayList<String>();
    List<String> languages = new ArrayList<String>();
    List<String> cities = new ArrayList<String>();

    private Date birthDate;
    private Date moveInDate;
    private Date moveOutDate;

    private MultiAutoCompleteTextView originCountriesMultiAutoComplete;
    private AutoCompleteTextView wantedCountryAutoComplete;
    private AutoCompleteTextView wantedCityAutoComplete;
    private MultiAutoCompleteTextView spokenLanguagesMultiAutoComplete;

    private Button birthDateButton;
    private Button moveInDateButton;
    private Button moveOutDateButton;
    private Button finishButton;

    private RadioGroup genderRadioGroup;
    private RadioGroup sleepTimeRadioGroup;
    private RadioGroup smokerRadioGroup;
    private RadioGroup occupationRadioGroup;

    private EditText telephoneEdit;
    private EditText budgetEdit;
    private EditText descriptionEdit;

    private ProgressDialog dialog;

    private void findViews()
    {
        originCountriesMultiAutoComplete = findViewById(R.id.originCountriesMultiAutoComplete);
        wantedCountryAutoComplete = findViewById(R.id.wantedCountryAutoComplete);
        wantedCityAutoComplete = findViewById(R.id.wantedCityAutoComplete);
        spokenLanguagesMultiAutoComplete = findViewById(R.id.spokenLanguagesMultiAutoComplete);

        birthDateButton = findViewById(R.id.profileBirthdayDateButton);
        moveInDateButton = findViewById(R.id.profileMoveInDateButton);
        moveOutDateButton = findViewById(R.id.profileMoveOutDateButton);
        finishButton = findViewById(R.id.finishButton);

        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        sleepTimeRadioGroup = findViewById(R.id.profileSleepTimeRadioGroup);
        smokerRadioGroup = findViewById(R.id.smokeRadioGroup);
        occupationRadioGroup = findViewById(R.id.occupationRadioGroup);

        telephoneEdit = findViewById(R.id.profileTelephoneEdit);
        budgetEdit = findViewById(R.id.profileBudgetEdit);
        descriptionEdit = findViewById(R.id.profileDescriptionEdit);
    }

    public boolean getUserInfo()
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String userString = sp.getString("user", "");
        Gson gson = new Gson();

        if (!userString.equals("")) {
            user = gson.fromJson(userString, User.class);
            Log.d("lua", "Retrieved user from preferences: " + userString);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        getUserInfo();

        findViews();

        dialog = ProgressDialog.show(MyProfileActivity.this, "", getString(R.string.loading), true);
        dialog.show();


        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToSendUserInfo();
            }
        });

        new GetCountryListTask(this).execute();
        new GetLanguageListTask(this).execute();
        new GetUserInfoTask(this).execute(user.id);


        originCountriesMultiAutoComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries));
        originCountriesMultiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        wantedCountryAutoComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries));

        wantedCityAutoComplete.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities));

        spokenLanguagesMultiAutoComplete.setAdapter( new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, languages));
        spokenLanguagesMultiAutoComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());


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
                DatePickerDialog picker = new DatePickerDialog(MyProfileActivity.this,
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
                DatePickerDialog picker = new DatePickerDialog(MyProfileActivity.this,
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
                DatePickerDialog picker = new DatePickerDialog(MyProfileActivity.this,
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
                picker.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        moveOutDate = null;
                        moveOutDateButton.setText(R.string.select_date);
                    }
                });
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

                Log.d("lua", "Country selection detected: " + selected_country);

                wantedCityAutoComplete.setHint(R.string.wanted_city_hint);

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


    public void tryToSendUserInfo()
    {

        String telephone = telephoneEdit.getText().toString().trim();

        if (telephone.length() == 0) {
            Toast.makeText(this,getString(R.string.select_telephone), Toast.LENGTH_SHORT).show();
            return;
        }

        user.telephone = telephone;




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
            Toast.makeText(this, getString(R.string.select_move_in), Toast.LENGTH_SHORT).show();
            return;
        }
        calendar.setTime(moveInDate);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1;
        year = calendar.get(Calendar.YEAR);

        user.arrivalDate = year + "-" + month + "-" + day;





        if (moveOutDate != null) {
            calendar.setTime(moveOutDate);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH) + 1;
            year = calendar.get(Calendar.YEAR);

            user.departureDate = year + "-" + month + "-" + day;
        }
        else {
            user.departureDate = "";
        }




        try {
            int budget = Integer.parseInt(budgetEdit.getText().toString());
            user.maxBudget = budget;
        }
        catch (Exception e) {
            Toast.makeText(this, getString(R.string.select_budget), Toast.LENGTH_SHORT).show();
            return;
        }






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




        user.description = descriptionEdit.getText().toString().trim();



        // call task to send user to server
        new SendUserInfoTask(this).execute(user);
    }




    public void onUserInfoSent(Boolean worked)
    {
        if (worked) {
            Toast.makeText(getApplicationContext(), getString(R.string.user_info_success), Toast.LENGTH_SHORT).show();

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor e = sp.edit();

            Gson gson = new Gson();

            e.putString("user", gson.toJson(user));

            e.apply();

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            finish();
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.user_info_failure), Toast.LENGTH_SHORT).show();
        }
    }


    public void onUserInfoReceived(User user)
    {
        dialog.dismiss();

        Log.d("lua", "Dismissed loading");

        if (user == null) {
            return;
        }


        if (user.telephone != null) {
            telephoneEdit.setText(user.telephone);
            telephoneEdit.setSelection(user.telephone.length());
        }


        if (user.birthDate != null) {
            int year, month, day;
            String [] parts = user.birthDate.split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2].substring(0, 2));

            Calendar cldr = Calendar.getInstance();
            cldr.set(year, month - 1, day, 0, 0);

            birthDate = new Date(cldr.getTimeInMillis());
            birthDateButton.setText(day + "/" + month + "/" + year);
        }


        if (user.gender != null) {
            if (user.gender.equals(getString(R.string.female))) {
                genderRadioGroup.check(R.id.radio_female);
            }
            else if (user.gender.equals(getString(R.string.male))) {
                genderRadioGroup.check(R.id.radio_male);
            }
            else {
                genderRadioGroup.check(R.id.radio_other);
            }
        }


        if (user.originCountries != null && user.originCountries.size() != 0) {
            String helper = "";
            for (int i = 0; i < user.originCountries.size(); i++) {
                helper += user.originCountries.get(i) + ", ";
            }
            originCountriesMultiAutoComplete.setText(helper);
            originCountriesMultiAutoComplete.setSelection(helper.length());
        }


        if (user.languagesSpoken != null && user.languagesSpoken.size() != 0) {
            String helper = "";
            for (int i = 0; i < user.languagesSpoken.size(); i++) {
                helper += user.languagesSpoken.get(i) + ", ";
            }
            spokenLanguagesMultiAutoComplete.setText(helper);
            spokenLanguagesMultiAutoComplete.setSelection(helper.length());
        }


        if (user.stayingCountry != null) {
            wantedCountryAutoComplete.setText(user.stayingCountry);
            wantedCountryAutoComplete.setSelection(user.stayingCountry.length());
        }


        if (user.stayingCity != null) {
            wantedCityAutoComplete.setText(user.stayingCity);
            wantedCityAutoComplete.setSelection(user.stayingCity.length());
        }


        if (user.arrivalDate != null) {
            int year, month, day;
            String [] parts = user.arrivalDate.split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2].substring(0, 2));

            Calendar cldr = Calendar.getInstance();
            cldr.set(year, month - 1, day, 0, 0);

            moveInDate = new Date(cldr.getTimeInMillis());
            moveInDateButton.setText(day + "/" + month + "/" + year);
        }


        if (user.departureDate != null) {
            int year, month, day;
            String [] parts = user.departureDate.split("-");
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2].substring(0, 2));

            Calendar cldr = Calendar.getInstance();
            cldr.set(year, month - 1, day, 0, 0);

            moveOutDate = new Date(cldr.getTimeInMillis());
            moveOutDateButton.setText(day + "/" + month + "/" + year);
        }


        if (user.maxBudget != null) {
            budgetEdit.setText(Integer.toString(user.maxBudget));
            budgetEdit.setSelection(budgetEdit.getText().toString().length());
        }


        if (user.sleepTime != null) {
            if (user.sleepTime.equals(getString(R.string.radio_9_10))) {
                sleepTimeRadioGroup.check(R.id.radio_9_10);
            }
            else if (user.sleepTime.equals(getString(R.string.radio_10_12))) {
                sleepTimeRadioGroup.check(R.id.radio_10_12);
            }
            else {
                sleepTimeRadioGroup.check(R.id.radio_12_plus);
            }
        }


        if (user.smokes != null) {
            if (user.smokes) {
                smokerRadioGroup.check(R.id.radio_yes);
            }
            else {
                smokerRadioGroup.check(R.id.radio_no);
            }
        }


        if (user.occupation != null) {
            if (user.occupation.equals(getString(R.string.local_student))) {
                occupationRadioGroup.check(R.id.radio_lStudent);
            }
            else if (user.occupation.equals(getString(R.string.international_student))) {
                occupationRadioGroup.check(R.id.radio_iStudent);
            }
            else if (user.occupation.equals(getString(R.string.work))) {
                occupationRadioGroup.check(R.id.radio_work);
            }
            else {
                occupationRadioGroup.check(R.id.radio_life);
            }
        }


        if (user.description != null) {
            descriptionEdit.setText(user.description);
            descriptionEdit.setSelection(user.description.length());
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        String caller = getIntent().getStringExtra("callingFrom");

        if (caller.equals("Dashboard")) {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}
