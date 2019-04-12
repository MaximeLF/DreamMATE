package com.dreammate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import model.CustomAdapter;
import model.User;

public class DashboardActivity extends AppCompatActivity {

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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Button logout = (Button) findViewById(R.id.dashboardLogout);

        final ArrayList<User> list = new ArrayList<>();
        for(int i = 0; i < 10; i++){

            User user = new User("John", "Doe", "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio.");
            list.add(user);
        }

        CustomAdapter adapter = new CustomAdapter(this, R.layout.activity_custom_layout, list);
        ListView l = (ListView) findViewById(R.id.listView);
        l.setAdapter(adapter);

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Nuno", "funciona");
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("First Name", list.get(position).getFirstName());
                intent.putExtra("Last Name", list.get(position).getLastName());
                startActivity(intent);
            }
        });
        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logout(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });*/


        /*Button continueRegistration = (Button) findViewById(R.id.dashboardContinueRegistration);

        continueRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyProfileActivity.class);
                startActivity(intent);
            }
        });*/
    }

    public void onMatchesComputed(List<User> users){

        
    }

}
