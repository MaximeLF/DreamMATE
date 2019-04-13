package com.dreammate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import model.CustomAdapter;
import model.User;
import tasks.GetMatchesForUserTask;
import tasks.GetUserInfoTask;

public class DashboardActivity extends AppCompatActivity {


    List<User> users = new ArrayList<User>();
    User user = new User();

    private TextView mTextMessage;
    private LinearLayout profile;
    private Intent intent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_profile:
                    intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_matches:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getUserInfo();
        new GetMatchesForUserTask(this).execute(user.id);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button logout = (Button) findViewById(R.id.dashboardLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });


        /*Button continueRegistration = (Button) findViewById(R.id.dashboardContinueRegistration);

        continueRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void getUserInfo(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String id = sp.getString("user_id", "");

        user.id = id;
    }

    public void onMatchesComputed(List<User> result){

        if(result != null) {
            Log.d("nuno", result.toString());
            users = new ArrayList<User>(result);

            CustomAdapter adapter = new CustomAdapter(this, R.layout.activity_custom_layout, users);
            ListView l = (ListView) findViewById(R.id.listView);
            l.setAdapter(adapter);

            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("Nuno", "funciona");
                    User selectedUserProfile = users.get(position);
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("user", selectedUserProfile);
                    startActivity(intent);
                }
            });
        }
        else {
            Toast.makeText(this, "No one likes you", Toast.LENGTH_SHORT).show();
        }
    }

}
