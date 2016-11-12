//package com.example.vidhiraj.sample;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;
//
//import static com.example.vidhiraj.sample.AndroidSpinnerExampleActivity.MY_PREFS_NAME;
//
///**
// * Created by Lenovo on 11/11/2016.
// */
//
//public class TabsMainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{
//
//
//    String TITLES[] = {"Home", "Change Password", "Logout"};
//    int ICONS[] = {R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos};
//    String NAME = "Eracord";
//    String EMAIL;
//    int PROFILE = R.drawable.ic_photos;
//    private Toolbar toolbar;                              // Declaring the Toolbar Object
//    RecyclerView mRecyclerView;                           // Declaring RecyclerView
//    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
//    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
//    DrawerLayout Drawer;                                  // Declaring DrawerLayout
//    ActionBarDrawerToggle mDrawerToggle;
//
//    private GoogleApiClient client;
//
//    //This is our tablayout
//    private TabLayout tabLayout;
//
//    //This is our viewPager
//    private ViewPager viewPager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.tab_activity);
//
//        //Adding toolbar to the activity
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        String user_email = prefs.getString("email", null);
//
//
//        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
//        toolbar = (Toolbar) findViewById(R.id.tool_bar);
//        setSupportActionBar(toolbar);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
//
//        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
//
//        mAdapter = new EraMyAdapter(TabsMainActivity.this, TITLES, ICONS, NAME, user_email, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
//        // And passing the titles,icons,header view name, header view email,
//        // and header view profile picture
//
//        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
//
//        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
//
//        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
//
//
//        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
//                // open I am not going to put anything here)
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                // Code here will execute once drawer is closed
//            }
//
//
//        }; // Drawer Toggle Object Made
//        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
//        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//
//
//        //Initializing the tablayout
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//
//        //Adding the tabs using addTab() method
//        tabLayout.addTab(tabLayout.newTab().setText("Create"));
//        tabLayout.addTab(tabLayout.newTab().setText("Catalog"));
//        tabLayout.addTab(tabLayout.newTab().setText("Students"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        //Initializing viewPager
//        viewPager = (ViewPager) findViewById(R.id.pager);
//
//        //Creating our pager adapter
//        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
//
//        //Adding adapter to pager
//        viewPager.setAdapter(adapter);
//
//        //Adding onTabSelectedListener to swipe views
//        tabLayout.setOnTabSelectedListener(this);
//    }
//
//
//    @Override
//   public void onBackPressed() {
//        if (this.Drawer.isDrawerOpen(GravityCompat.START)) {
//            this.Drawer.closeDrawer(GravityCompat.START);
//        } else {
//            this.Drawer.openDrawer(GravityCompat.START);
//        }
//
//    }
//
//
//
//
//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }
//}
