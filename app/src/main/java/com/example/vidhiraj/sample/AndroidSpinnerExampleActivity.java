package com.example.vidhiraj.sample;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
public class AndroidSpinnerExampleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;                              // Declaring the Toolbar Object
    private Button spinBtn;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    TextView signDiffUser;
    TextView useremail;
    ActionBarDrawerToggle mDrawerToggle;
    EditText editPassword;
    String finalEmail;
    String device_id;
    ProgressDialog mProgress;
    boolean multiorg = false;
    int orgid, org_id;
    Spinner spinner;
    public static String MY_PREFS_NAME = null;
    List<Integer> organisationId = new ArrayList<Integer>();
    String orgNameText = null;
    String specific_org = null;
    List<String> organisation = new ArrayList<String>();
    // Declaring Action Bar Drawer Toggle

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        useremail = (TextView) findViewById(R.id.useremail);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("id is", device_id);
        Cursor cursor = null;
        String email = "";
        try {

            cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                email = cursor.getString(cursor.getColumnIndex("cust_email"));
            }
        } finally {

            cursor.close();
        }
        useremail.setText(email);
        String loginURL = ApiKeyConstant.apiUrl + "/users/get_organisations.json?email=" + email + "&device_id=" + device_id;
        Log.e("url", loginURL);
        mProgress.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        mProgress.dismiss();
                        boolean multiple_organisations = response.getBoolean("multiple_organisations");
                        JSONArray jsonArray = response.getJSONArray("organisations");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            orgNameText = orgObj.getString("organisation_name");
                            organisation.add(orgNameText);
                            orgid = orgObj.getInt("organisation_id");
                            organisationId.add(orgid);
                        }
                        if (multiple_organisations) {
                            multiorg = true;
                            Log.e("flag org is", String.valueOf(multiorg));
                            spinner.setVisibility(View.VISIBLE);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, organisation);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);

                        } else {
                            TextView org_name = (TextView) findViewById(R.id.org_id);
                            org_name.setVisibility(View.VISIBLE);
                            org_name.setText(orgNameText);
                            spinner.setVisibility(View.GONE);
                        }
                    } else {
                        mProgress.dismiss();
                        Cursor cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
                        Log.e("record is", String.valueOf(cursor.getCount()));
                        if (cursor.getCount() != 0) {
                            UserDB userDB = new UserDB(getApplicationContext());
                            SQLiteDatabase db = userDB.getWritableDatabase();
                            db.execSQL("DELETE FROM " + UserDB.DATABASE_TABLE);
                            cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
                            Log.e("delete count", String.valueOf(cursor.getCount()));

                            Toast.makeText(getBaseContext(), "Something went wrong,Please Register Again", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AndroidSpinnerExampleActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                } catch (JSONException e) {
                    String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
                    Log.e("sdcard-err2:", err);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.getMessage());
                        mProgress.dismiss();

                    }
                }
        );

        int socketTimeout = 20000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);
        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);


        editPassword = (EditText) findViewById(R.id.editTextPassword);
        signDiffUser = (TextView) findViewById(R.id.diffuser);
        signDiffUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean signdiffflag = true;
                Intent intent = new Intent(AndroidSpinnerExampleActivity.this, MainActivity.class);
                intent.putExtra("diffflag", signdiffflag);
                finish();
                startActivity(intent);

            }
        });
        spinBtn = (Button) findViewById(R.id.buttonLogin);
        finalEmail = email;
        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateCheck();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        toolbar = (Toolbar)
                findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }


    public void validateCheck() throws JSONException {
        if (!validate()) {
            onLoginFailed();
            return;
        } else {
            final String user_password = editPassword.getText().toString();
            Log.e("user", user_password);

            JSONObject userObj = new JSONObject();
            JSONObject user = new JSONObject();

            try {
                userObj.put("email", finalEmail);
                userObj.put("device_id", device_id);
                userObj.put("mpin", user_password);
                Log.e("flag org is", String.valueOf(multiorg));
                if (multiorg) {
                    userObj.put("organisation_id", org_id);
                    Log.e("org_id is", String.valueOf(org_id));
                }

                user.put("user", userObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("user", String.valueOf(user));
            String loginURL = ApiKeyConstant.apiUrl + "/users/mpin_sign_in";

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, loginURL, user, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            ApiKeyConstant.authToken = response.getString("token");
                            String image_url = response.getString("logo_url");
                            MY_PREFS_NAME = "MyPrefsFile";
                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("email", finalEmail);
                            editor.putString("specificorg", specific_org);
                            editor.putString("org_icon", image_url);
                            editor.commit();
                            Intent intent = new Intent(AndroidSpinnerExampleActivity.this, ClassActivity.class);
                            startActivity(intent);
                        }

                    } catch (JSONException e) {
                        String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
                        Log.e("sdcard-err2:", err);
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getBaseContext(), "Enter the correct pin", Toast.LENGTH_LONG).show();
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
        boolean valid = true;
        String userpin = editPassword.getText().toString();
        if (userpin.length() != 4) {
            editPassword.setError("enter only 4 digit pin");
            valid = false;
        } else {

            editPassword.setError(null);

        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }).setNegativeButton("no", null).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("in orgid", "done");
        for (int j = position; j <= position; j++) {
            org_id = organisationId.get(j);
            specific_org = organisation.get(j);
            Log.e("for org_id", String.valueOf(org_id));
        }
        Log.e(" out org_id", String.valueOf(org_id));

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}