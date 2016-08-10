package com.example.vidhiraj.sample;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vidhiraj on 10-08-2016.
 */
public class AndroidSpinnerExampleActivity extends AppCompatActivity {
    String TITLES[] = {"Home","Change Password","Logout"};
    int ICONS[] = {R.drawable.ic_photos,R.drawable.ic_photos,R.drawable.ic_photos,R.drawable.ic_photos,R.drawable.ic_photos};
    TextView orgName;
    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "xyz";
    String EMAIL = "xyz@gmail.com";
    int PROFILE = R.drawable.ic_photos;

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private Button spinBtn;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    TextView signDiffUser;
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orgName= (TextView) findViewById(R.id.orgname);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        Intent intent=getIntent();
        String email=intent.getStringExtra("email");
        String device_id=intent.getStringExtra("device_id");
        JSONObject requestParam=new JSONObject();
        try {
            requestParam.put("email",email);
            requestParam.put("device_id",device_id);
        } catch (JSONException e) {
            String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
            Log.e("sdcard-err2:",err);

        }


        Log.e("req", String.valueOf(requestParam));
        String loginURL = "http://eracord.com/users/get_organisations";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,loginURL,requestParam, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success=response.getBoolean("success");
                    String orgNameText = null;
                    if(success)
                    {
                        boolean multiple_organisations=response.getBoolean("multiple_organisations");
                        JSONArray jsonArray = response.getJSONArray("organisations");
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            orgNameText =orgObj.getString("organisation_name");
                        }
                        if(multiple_organisations)
                        {
                            Log.e("true","suxxx");
                        }
                        else {
                            orgName.setVisibility(View.VISIBLE);
                            orgName.setText(orgNameText);
                        }
                    }
                } catch (JSONException e) {
                    String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
                    Log.e("sdcard-err2:",err);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
        setContentView(R.layout.activity_org);

        // Spinner element

     //   spinner.setPrompt("Select Org");
        signDiffUser= (TextView) findViewById(R.id.diffuser);
        signDiffUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
                Log.e("count is", String.valueOf(cursor.getCount()));
                UserDB userDB=new UserDB(getApplicationContext());
                SQLiteDatabase db = userDB.getWritableDatabase();
                db.execSQL("DELETE FROM " + UserDB.DATABASE_TABLE);
                Intent intent = new Intent(AndroidSpinnerExampleActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        // Spinner Drop down elements
//        List<String> organisation = new ArrayList<String>();
//        organisation.add("OrganizationOne");
//        organisation.add("OrganizationTwo");
//        organisation.add("OrganizationThree");

        spinBtn= (Button) findViewById(R.id.buttonLogin);
        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AndroidSpinnerExampleActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        // Creating adapter for spinner
     //   ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, organisation);

        // Drop down layout style - list view with radio button
      //  dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
    //    spinner.setAdapter(dataAdapter);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new EraMyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

    }
}
