package com.example.vidhiraj.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.example.vidhiraj.sample.AndroidSpinnerExampleActivity.MY_PREFS_NAME;

/**
 * Created by Lenovo on 11/13/2016.
 */

public class CatalogSchedule extends AppCompatActivity {
    CalendarView calender;

    String TITLES[] = {"Home", "Daily Catalog", "Students", "Logout"};
    int ICONS[] = {R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos};
    //String NAME = "Eracord";
    String org = null;
    int PROFILE = R.drawable.ic_photos;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;         // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    String url_icon;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        Intent intent = getIntent();
        final String teachId = intent.getStringExtra("teach_id");
        calender = (CalendarView) findViewById(R.id.calendarView);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String user_email = prefs.getString("email", null);
        org = prefs.getString("specificorg", null);
        url_icon = prefs.getString("org_icon", null);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(org);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mAdapter = new EraMyAdapter(CatalogSchedule.this, TITLES, ICONS, user_email, url_icon);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Intent intent = new Intent(CatalogSchedule.this, DailyTeachingActivity.class);
                intent.putExtra("day", dayOfMonth);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                intent.putExtra("teachId", teachId);
                startActivity(intent);
            }
        });
    }
}

