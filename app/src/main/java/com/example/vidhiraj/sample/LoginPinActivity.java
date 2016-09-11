package com.example.vidhiraj.sample;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vidhiraj on 10-08-2016.
 */
public class LoginPinActivity extends AppCompatActivity implements View.OnClickListener {

    Button login;
    String device_id;
 //   String authorization_token;
    String email;
    Integer mpin;
    EditText uniqueUserPin, uniqueConfirmUserPin;
    TextInputLayout inputConfirmPin;
    String TITLES[] = {"Home","Change Password","Logout"};
    int ICONS[] = {R.drawable.ic_photos,R.drawable.ic_photos,R.drawable.ic_photos,R.drawable.ic_photos,R.drawable.ic_photos};

    //Similarly we Create a String Resource for the name and email in the header view
    //And we also create a int resource for profile picture in the header view

    String NAME = "xyz";
    String EMAIL = "xyz@gmail.com";
    int PROFILE = R.drawable.ic_photos;

    private Toolbar toolbar;                              // Declaring the Toolbar Object

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ProgressDialog mProgress;
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_layout);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        inputConfirmPin = (TextInputLayout) findViewById(R.id.inputconfirm);
        //setPinbutton = (Button) findViewById(R.id.confirmuser);
        uniqueUserPin = (EditText) findViewById(R.id.userpin);
        uniqueConfirmUserPin = (EditText) findViewById(R.id.confirmuserpin);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new EraMyAdapter(LoginPinActivity.this,TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
        Intent intent = getIntent();

        ApiKeyConstant.authToken = intent.getStringExtra("authorization_token");

        email = intent.getStringExtra("email");
        Log.e("email is", email);
//

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {

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

        login = (Button) findViewById(R.id.loginuser);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.loginuser:
                try {
                    validateCheck();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public void validateCheck() throws JSONException {
        Log.e("validate chaeck","entry");
        if (!validate()) {
            onLoginFailed();
            return;
        } else {

            Log.e("success","done");

            final String mpinString=uniqueConfirmUserPin.getText().toString();
            mpin= Integer.parseInt(mpinString);
            device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.e("id is",device_id);
            JSONObject jo = new JSONObject();
            jo.put("device_id", device_id);
            jo.put("email", email);
            jo.put("mpin", mpin);


            JSONObject userObj = new JSONObject();
            userObj.put("authorization_token",ApiKeyConstant.authToken);
            userObj.put("user", jo);

            String loginURL = ApiKeyConstant.apiUrl +"/users/mobile_sign_up";
            mProgress.show();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,loginURL, userObj, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean success=response.getBoolean("success");
                        if(success)
                        {
                            mProgress.dismiss();
                            ContentValues values = new ContentValues();
                            values.put(UserDB.KEY_EMAIL,email);
                            getContentResolver().insert(User.CONTENT_URI, values);
                            Log.e("Inserted values: ", values.toString());
                            Intent intent = new Intent(LoginPinActivity.this, AndroidSpinnerExampleActivity.class);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Volley", "Error");
                            Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                            mProgress.dismiss();

                        }
                    }
            );
            VolleyControl.getInstance().addToRequestQueue(jsonObjReq);

        }
        }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        //  _loginButton.setEnabled(true);
    }

    public boolean validate() {
        Log.e("next validate","entry");
        boolean valid = true;
        String userpin = uniqueUserPin.getText().toString();
        String confirmpin=uniqueConfirmUserPin.getText().toString();
        if (userpin.length()!=4) {
            uniqueUserPin.setError("enter only 4 digit pin");
            Log.e("user pin size","false");
            valid = false;
        } else {
            if(!userpin.equals(confirmpin)) {
                uniqueConfirmUserPin.setError("pin does not match");
                valid=false;
            }
            else
            {
                uniqueUserPin.setError(null);
            }
        }
        return valid;
    }

}
