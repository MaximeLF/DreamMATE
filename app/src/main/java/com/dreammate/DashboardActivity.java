package com.dreammate;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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


        if (!getUserInfo()) {
            return;
        }


        myDrawerLayout = findViewById(R.id.dashboard_drawer);
        myNavigationView = findViewById(R.id.dashboard_navigation);

        View headerView = myNavigationView.getHeaderView(0);

        navigationName = headerView.findViewById(R.id.navigationName);
        navigationEmail = headerView.findViewById(R.id.navigationEmail);
        navigationAvatar = headerView.findViewById(R.id.navigationImage);

        navigationName.setText(user.fullName());
        navigationEmail.setText(user.email);

        if (user.gender.equals(getString(R.string.female))) {
            navigationAvatar.setImageResource(R.drawable.girl_avatar);
        }
        else if (user.gender.equals(getString(R.string.male))) {
            navigationAvatar.setImageResource(R.drawable.male_avatar);
        }
        else {
            navigationAvatar.setImageResource(R.drawable.other_avatar);
        }



        myNavigationView.setNavigationItemSelectedListener(this);
        myToggle = new ActionBarDrawerToggle(this, myDrawerLayout, R.string.open, R.string.close);
        myDrawerLayout.addDrawerListener(myToggle);
        myToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new GetMatchesForUserTask(this).execute(user.id);
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
                myDrawerLayout.closeDrawers();
            }
            case R.id.aboutMenuItem: {
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
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

    public class CustomAdapter extends ArrayAdapter<User> {

        private int layout;
        private Context context;

        public CustomAdapter(Context context, int resource, List<User> data)
        {
            super(context, resource, data);
            this.layout = resource;
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View view = convertView;
            if (view == null) {

                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(this.layout, parent, false);
            }

            String gender = getItem(position).getGender();
            String fname = getItem(position).getFirstName();
            String lname = getItem(position).getLastName();
            String age = Integer.toString(getItem(position).getAge());
            String description = getItem(position).getDescription();
            String profileInfo = fname + " " + lname + " " + age;
            TextView tvDescription = view.findViewById(R.id.description);
            TextView tvProfileInfo = view.findViewById(R.id.profileInfo);

            tvProfileInfo.setText(profileInfo);
            tvDescription.setText(description);

            ImageView avatar = view.findViewById(R.id.avatar);

            if(gender.equals("Female")){
                avatar.setImageResource(R.drawable.girl_avatar);
            }

            else if(gender.equals("Male")){
                avatar.setImageResource(R.drawable.male_avatar);
            }

            else{
                avatar.setImageResource(R.drawable.other_avatar);
            }

            return view;
        }

    }

}
