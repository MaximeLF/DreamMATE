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

    Button registerButton;
    Button loginButton;
    EditText emailEdit;
    EditText passwordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerButton = findViewById(R.id.loginRegister);
        loginButton = findViewById(R.id.loginSignIn);

        emailEdit = findViewById(R.id.loginEmailEdit);
        passwordEdit = findViewById(R.id.loginPasswordEdit);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void startLogin()
    {
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (email.length() * password.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(email, password);

        new CheckUserLoginTask(this).execute(user);
    }

    public void confirmLogin(User user)
    {
        if (user == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
        else if (user.id == null || user.id.equals("")) {
            Toast.makeText(getApplicationContext(), getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
        }
        else {
            user.login(this);

            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
        }
    }
}
