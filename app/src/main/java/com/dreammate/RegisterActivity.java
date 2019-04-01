package com.dreammate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.User;
import tasks.RegisterUserTask;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register = (Button) findViewById(R.id.registerSignUp);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startRegistration();
            }
        });
    }

    public void startRegistration()
    {
        String name = ((EditText)findViewById(R.id.registerNameEdit)).getText().toString();
        String email = ((EditText)findViewById(R.id.registerEmailEdit)).getText().toString();
        String p1 = ((EditText)findViewById(R.id.registerPassword1Edit)).getText().toString();
        String p2 = ((EditText)findViewById(R.id.registerPassword2Edit)).getText().toString();

        if (name.length() * email.length() * p1.length() * p2.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!p1.equals(p2)) {
            Toast.makeText(getApplicationContext(), getString(R.string.unmatching_password), Toast.LENGTH_SHORT).show();
            return;
        }
        String[] parts = name.split(" ");

        User user;
        if (parts.length > 1)
        {
            String fName = parts[0];
            String lName = "";
            for (int i = 1; i < parts.length; i++)
            {
                lName += parts[i] + " ";
            }
            user = new User(fName, lName, email, p1);
        }
        else if (parts.length == 1){
            String fName = parts[0];
            user = new User(fName, "", email, p1);
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        new RegisterUserTask(this).execute(user);
    }

    public void confirmRegistration(User user)
    {
        if (user != null)
        {
            user.login(this);

            Intent intent = new Intent(getApplicationContext(), CreateProfileActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
    }
}
