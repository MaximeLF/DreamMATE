package com.dreammate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import model.CustomAdapter;
import model.User;
import tasks.GetMatchesForUserTask;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    List<User> users = new ArrayList<User>();
    User user;

    private DrawerLayout myDrawerLayout;
    private NavigationView myNavigationView;
    private ActionBarDrawerToggle myToggle;

    private TextView navigationName;
    private TextView navigationEmail;
    private ImageView navigationAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myDrawerLayout = findViewById(R.id.dashboard_drawer);
        myNavigationView = findViewById(R.id.dashboard_navigation);

        View headerView = myNavigationView.getHeaderView(0);

        navigationName = headerView.findViewById(R.id.navigationName);
        navigationEmail = headerView.findViewById(R.id.navigationEmail);
        navigationAvatar = headerView.findViewById(R.id.navigationImage);



        myNavigationView.setNavigationItemSelectedListener(this);
        myToggle = new ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close);
        myDrawerLayout.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!getUserInfo()) {
            return;
        }

        new GetMatchesForUserTask(this).execute(user.id);

        navigationName.setText(user.fullName());
        navigationEmail.setText(user.email);


        /*logoutButton = findViewById(R.id.dashboardLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (myToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.myProfileMenuItem: {
                Intent intent = new Intent(this, MyProfileActivity.class);
                intent.putExtra("callingFrom", "Dashboard");
                finish();
                startActivity(intent);
                return true;
            }
            case R.id.logoutMenuItem: {
                User.logout(getApplicationContext());
                Intent intent = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent);
                return true;
            }
            case R.id.matchesMenuItem: {

            }
            case R.id.aboutMenuItem: {

            }
            case R.id.homeMenuItem: {
                myDrawerLayout.closeDrawers();
            }
            default:

        }
        return false;
    }

    public boolean getUserInfo() {
        user = new User();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String userString = sp.getString("user", "");
        Gson gson = new Gson();

        if (!userString.equals("")) {
            user = gson.fromJson(userString, User.class);
            Log.d("lua", userString);
            return true;
        }
        return false;
    }

    public void onMatchesComputed(List<User> result){

        if(result != null) {
            Log.d("nuno", result.toString());
            users = new ArrayList<User>(result);

            CustomAdapter adapter = new CustomAdapter(this, R.layout.match_layout, users);
            ListView l = findViewById(R.id.matchesListView);
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


    @Override
    public void onBackPressed() {
        if (myDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            myDrawerLayout.closeDrawers();
        }
        else {
            super.onBackPressed();
        }
    }

}
