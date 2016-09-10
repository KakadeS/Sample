package com.example.vidhiraj.sample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "MainActivityLogin";
    public Cursor cursor;
    Button loginButton;
    private EditText userEmail;
    private EditText userPassword;
    String TITLES[] = {"Home", "Change Password", "Logout"};
    int ICONS[] = {R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos};
    String NAME = "xyz";
    String EMAIL = "xyz@gmail.com";
    int PROFILE = R.drawable.ic_photos;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    EditText editTextEmail,editPassword;
    // Declaring Action Bar Drawer Toggle
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    SQLiteOpenHelper dbHelper;
    UserDB userDB;
  Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cursor cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
        Log.e("record is", String.valueOf(cursor.getCount()));
        if (cursor.getCount() != 0) {
            Intent intent = new Intent(MainActivity.this, AndroidSpinnerExampleActivity.class);
            startActivity(intent);
        } else {

            setContentView(R.layout.activity_main);
            getSupportLoaderManager().initLoader(0, null, this);
            toolbar = (Toolbar) findViewById(R.id.tool_bar);
            setSupportActionBar(toolbar);
            editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            editPassword = (EditText) findViewById(R.id.editTextPassword);
            mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
            mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
            mAdapter = new EraMyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
            mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView
            mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
            mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
            Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
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
            mDrawerToggle.syncState();               // Finally we set the drawer toggle sync Stat
            userEmail = (EditText) findViewById(R.id.editTextEmail);
            userPassword = (EditText) findViewById(R.id.editTextPassword);

            loginButton = (Button) findViewById(R.id.buttonLogin);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        login();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  makeJsonObjectRequest();
                }
            });
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        Uri uri = User.CONTENT_URI;
        return new CursorLoader(this, uri, null, null, null, null);
    }

    /**
     * A callback method, invoked after the requested content provider returned all the data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.e("on loadfinished called", String.valueOf(loader.getId()));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        //mAdapter.swapCursor(null);
    }
    public void login() throws JSONException {
    Log.d(TAG, "Login");

    if (!validate()) {
        onLoginFailed();
        return;
    } else{

        final String user_email=editTextEmail.getText().toString();
        String user_password=editPassword.getText().toString();

        JSONObject jo = new JSONObject();
        jo.put("email", user_email);
        jo.put("password", user_password);

        JSONObject userObj = new JSONObject();
        userObj.put("user", jo);


        String loginURL = ApiKeyConstant.apiUrl +"/users/mobile_sign_in";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,loginURL, userObj, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success=response.getBoolean("success");
                    ApiKeyConstant.authToken=response.getString("token");
                    Log.e("email success","done");
                    if(success)
                    {
                        Intent intent = new Intent(MainActivity.this, LoginPinActivity.class);
                        intent.putExtra("email",user_email);
                        intent.putExtra("authorization_token",ApiKeyConstant.authToken);
                        startActivity(intent);
                    }else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);



    }
    }


    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Invalid User", Toast.LENGTH_LONG).show();

        //  _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("enter a valid email address");
            valid = false;
        } else {
            userEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            userPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            userPassword.setError(null);
        }

        return valid;
    }
}
