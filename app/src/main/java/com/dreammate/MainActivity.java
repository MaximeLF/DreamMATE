package com.dreammate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        Intent intent;
        if (sp.getString("user_id", "").equals(""))
        {
            intent = new Intent(this, LoginActivity.class);
        }
        else {
            intent = new Intent(this, DashboardActivity.class);
        }
        startActivity(intent);
    }
}
