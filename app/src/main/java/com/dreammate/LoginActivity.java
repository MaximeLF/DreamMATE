package com.dreammate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.User;
import tasks.CheckUserLoginTask;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button register = (Button) findViewById(R.id.loginRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button login = (Button) findViewById(R.id.loginSignIn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });
    }

    public void startLogin()
    {
        String email = ((EditText) findViewById(R.id.loginEmailEdit)).getText().toString();
        String password = ((EditText) findViewById(R.id.loginPasswordEdit)).getText().toString();

        if (email.length() * password.length() == 0)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
        }
        User user = new User(email, password);

        new CheckUserLoginTask(this).execute(user);
    }

    public void confirmLogin(User user)
    {
        if (user != null)
        {
            user.login(this);

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
    }
}
