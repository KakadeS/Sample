package com.example.vidhiraj.sample;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
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
    String email;
    Integer mpin;
    EditText uniqueUserPin, uniqueConfirmUserPin;
    TextInputLayout inputConfirmPin;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
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
        uniqueUserPin = (EditText) findViewById(R.id.userpin);
        uniqueConfirmUserPin = (EditText) findViewById(R.id.confirmuserpin);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        ApiKeyConstant.authToken = intent.getStringExtra("authorization_token");
        email = intent.getStringExtra("email");
        Log.e("email is", email);
        login = (Button) findViewById(R.id.loginuser);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
        Log.e("validate chaeck", "entry");
        if (!validate()) {
            onLoginFailed();
            return;
        } else {
            Log.e("success", "done");
            final String mpinString = uniqueConfirmUserPin.getText().toString();
            mpin = Integer.parseInt(mpinString);
            device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.e("id is", device_id);
            JSONObject jo = new JSONObject();
            jo.put("device_id", device_id);
            jo.put("email", email);
            jo.put("mpin", mpin);
            JSONObject userObj = new JSONObject();
            userObj.put("authorization_token", ApiKeyConstant.authToken);
            userObj.put("user", jo);
            String loginURL = ApiKeyConstant.apiUrl + "/users/mobile_sign_up";
            mProgress.show();
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, loginURL, userObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            mProgress.dismiss();
                            Cursor cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
                            Log.e("record is", String.valueOf(cursor.getCount()));
                            if (cursor.getCount() != 0) {
                                UserDB userDB = new UserDB(getApplicationContext());
                                SQLiteDatabase db = userDB.getWritableDatabase();
                                db.execSQL("DELETE FROM " + UserDB.DATABASE_TABLE);
                                cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
                                Log.e("delete count", String.valueOf(cursor.getCount()));
                                ContentValues values = new ContentValues();
                                values.put(UserDB.KEY_EMAIL, email);
                                getContentResolver().insert(User.CONTENT_URI, values);
                                Log.e("Inserted values: ", values.toString());
                                cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
                                Log.e("insert is", String.valueOf(cursor.getCount()));
                            } else {
                                ContentValues values = new ContentValues();
                                values.put(UserDB.KEY_EMAIL, email);
                                getContentResolver().insert(User.CONTENT_URI, values);
                                Log.e("Inserted values: ", values.toString());
                                cursor = getContentResolver().query(User.CONTENT_URI, null, null, null, null);
                                Log.e("insert is", String.valueOf(cursor.getCount()));
                            }
                            Intent intent = new Intent(LoginPinActivity.this, AndroidSpinnerExampleActivity.class);
                            intent.putExtra("email", email);
                            finish();
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
                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null && networkResponse.statusCode == 401) {
                                Toast.makeText(getBaseContext(), "Something went wrong,Try Again", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginPinActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Log.e("Volley", "Error");
                                Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_LONG).show();
                                mProgress.dismiss();
                            }
                        }
                    }
            );
            VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
        }
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Try Again", Toast.LENGTH_LONG).show();
        //  _loginButton.setEnabled(true);
    }

    public boolean validate() {
        Log.e("next validate", "entry");
        boolean valid = true;
        String userpin = uniqueUserPin.getText().toString();
        String confirmpin = uniqueConfirmUserPin.getText().toString();
        if (userpin.length() != 4) {
            uniqueUserPin.setError("enter only 4 digit pin");
            Log.e("user pin size", "false");
            valid = false;
        } else {
            if (!userpin.equals(confirmpin)) {
                uniqueConfirmUserPin.setError("pin does not match");
                valid = false;
            } else {
                uniqueUserPin.setError(null);
            }
        }
        return valid;
    }
}
