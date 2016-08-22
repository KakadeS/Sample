package com.example.vidhiraj.sample;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.EditText;
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
    ActionBarDrawerToggle mDrawerToggle;
    EditText editPassword;
    // Declaring Action Bar Drawer Toggle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("id is",device_id);
        Cursor cursor = null;
        String email = "";
        try{

            cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                email = cursor.getString(cursor.getColumnIndex("cust_email"));
            }
        }finally {

            cursor.close();
        }
        String loginURL = ApiKeyConstant.apiUrl + "/users/get_organisations.json?email=" + email + "&device_id=" + device_id;
        Log.e("url",loginURL);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                String orgNameText=null;
                try {
                    boolean success=response.getBoolean("success");
                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
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
                            spinner.setVisibility(View.VISIBLE);
                            List<String> organisation = new ArrayList<String>();
                               organisation.add("OrganizationOne");
                                organisation.add("OrganizationTwo");
                                organisation.add("OrganizationThree");
                               ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, organisation);
                              dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                               spinner.setAdapter(dataAdapter);

                        }
                        else {
                            TextView org_name= (TextView) findViewById(R.id.org_id);
                            org_name.setVisibility(View.VISIBLE);
                            org_name.setText(orgNameText);
                            spinner.setVisibility(View.GONE);

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
        spinBtn= (Button) findViewById(R.id.buttonLogin);
        final String finalEmail = email;


        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassword= (EditText) findViewById(R.id.editTextPassword);
                final String user_password=editPassword.getText().toString();
                Log.e("user",user_password);

                JSONObject userObj=new JSONObject();
                JSONObject user=new JSONObject();

                try {
                    userObj.put("email", finalEmail);
                    userObj.put("device_id",device_id);
                    userObj.put("mpin",user_password);
                    user.put("user",userObj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("user", String.valueOf(user));
                String loginURL = ApiKeyConstant.apiUrl +"/users/mpin_sign_in";

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,loginURL,user, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success=response.getBoolean("success");
                            if(success)
                            {
                                ApiKeyConstant.authToken=response.getString("token");
                                Intent intent=new Intent(AndroidSpinnerExampleActivity.this,ClassActivity.class);
                                intent.putExtra("token",ApiKeyConstant.authToken);
                                startActivity(intent);
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
            }
        });

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
