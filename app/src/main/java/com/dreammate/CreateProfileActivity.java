package com.dreammate;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CreateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("user_id", "");
        String firstName = sp.getString("first_name", "");
        String lastName = sp.getString("last_name", "");

        if (!id.equals("")) {
            ((TextView) findViewById(R.id.profileDescription)).setText(firstName + " " + lastName + " (" + id + ")");
        }
    }
}
