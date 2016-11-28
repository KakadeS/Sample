package com.example.vidhiraj.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.vidhiraj.sample.AndroidSpinnerExampleActivity.MY_PREFS_NAME;

public class DailyTeachingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    List<Integer> chapter_array = new ArrayList<Integer>();
    String classid;
    Button createCatalog, cancelCatalog;
    LinearLayout linearpoints;
    Integer chapter_id = null;
    RecyclerView mRecyclerView;
    ProgressDialog mProgress;
    TextView date_selected;
    int day, month, year;
    String TITLES[] = {"Home", "Daily Catalog", "Students", "Logout"};
    int ICONS[] = {R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos};
    String org = null;
    int PROFILE = R.drawable.ic_photos;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mDrawerRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mDrawerAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManagers;         // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    String url_icon;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_teaching);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
        date_selected = (TextView) findViewById(R.id.selected_date);
        final Intent intent = getIntent();
        classid = intent.getStringExtra("teachId");
        day = intent.getIntExtra("day", 0);
        month = intent.getIntExtra("month", 0);
        year = intent.getIntExtra("year", 0);
        date_selected.setText(day + " / " + (month + 1) + " / " + year);
        Log.e("getchap", String.valueOf(classid));
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        createCatalog = (Button) findViewById(R.id.buttonCreate);
        cancelCatalog = (Button) findViewById(R.id.buttonCancel);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String user_email = prefs.getString("email", null);
        org = prefs.getString("specificorg", null);
        url_icon = prefs.getString("org_icon", null);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(org);
        mDrawerRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mDrawerRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size
        mDrawerAdapter = new EraMyAdapter(DailyTeachingActivity.this, TITLES, ICONS, user_email, url_icon);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        mDrawerRecyclerView.setAdapter(mDrawerAdapter);                              // Setting the adapter to RecyclerView
        mLayoutManagers = new LinearLayoutManager(this);                 // Creating a layout Manager
        mDrawerRecyclerView.setLayoutManager(mLayoutManagers);                 // Setting the layout Manager
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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/time_table_classes/" + classid + "/get_chapters.json";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    List<String> categories = new ArrayList<String>();
                    if (success) {
                        JSONArray jsonArray = response.getJSONArray("chapters");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject chapterObj = jsonArray.getJSONObject(i);
                            chapter_array.add(chapterObj.getInt("id"));
                            categories.add(chapterObj.getString("name"));
                        }
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                } catch (JSONException e) {
                    String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
                    Log.e("sdcard-err2:", err);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 401) {
                            Intent intent = new Intent(DailyTeachingActivity.this, AndroidSpinnerExampleActivity.class);
                            startActivity(intent);
                        } else {
                            Log.e("Volley", "Error");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", ApiKeyConstant.authToken);
                return headers;
            }
        };
        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
        cancelCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DailyTeachingActivity.this, ClassActivity.class);
                startActivity(intent1);
            }
        });
        createCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder buff = new StringBuilder();
                String sep = "";
                List<PointsData> stList = ((PointsAdapter) mAdapter)
                        .getStudentist();
                for (int i = 0; i < stList.size(); i++) {
                    PointsData singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == true) {
                        buff.append(sep);
                        buff.append(singleStudent.getPointId());
                        sep = ",";
                        Log.e("buff is", String.valueOf(buff));
                    }
                }
                JSONObject daily_teaching_point = new JSONObject();
                JSONObject userObj = new JSONObject();
                try {
                    daily_teaching_point.put("chapter_id", chapter_id);
                    daily_teaching_point.put("chapters_point_id", buff);
                    daily_teaching_point.put("date", new Date());
                    userObj.put("daily_teaching_point", daily_teaching_point);
                    Log.e("daily_teach", String.valueOf(daily_teaching_point));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String loginURL = ApiKeyConstant.apiUrl + "/api/v1/time_table_classes/" + classid + "/daily_teachs";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, loginURL, userObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                int id = response.getInt("dtp_id");
                                String dtp_id = String.valueOf(id);
                                Log.e("dtp id", String.valueOf(dtp_id));
                                Intent intent1 = new Intent(DailyTeachingActivity.this, PresentyCatalog.class);
                                intent1.putExtra("dtp_id", dtp_id);
                                Log.e("put extra dtp", String.valueOf(dtp_id));
                                startActivity(intent1);
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
                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null && networkResponse.statusCode == 401) {
                                    Intent intent = new Intent(DailyTeachingActivity.this, AndroidSpinnerExampleActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getBaseContext(), "Daily Catalog Not Saved", Toast.LENGTH_LONG).show();
                                }
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Authorization", ApiKeyConstant.authToken);
                        return headers;
                    }
                };
                VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DailyTeaching Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vidhiraj.sample/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DailyTeaching Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.vidhiraj.sample/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("selected", "done");
        Intent intent = getIntent();
        classid = intent.getStringExtra("teachId");
        for (int j = position; j <= position; j++) {
            chapter_id = chapter_array.get(j);
            Log.e("for chap_id", String.valueOf(chapter_id));
        }
        Log.e(" out chap_id", String.valueOf(chapter_id));
        Log.e("getpoints", String.valueOf(classid));
        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/time_table_classes/" + classid + "/chapters/" + chapter_id + "/get_points.json";
        mProgress.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    linearpoints = (LinearLayout) findViewById(R.id.pointslinear);
                    boolean success = response.getBoolean("success");
                    List<PointsData> pointsList = new ArrayList<PointsData>();
                    if (success) {
                        mProgress.dismiss();
                        JSONArray jsonArray = response.getJSONArray("points");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject pointsObj = jsonArray.getJSONObject(i);
                            PointsData points = new PointsData(pointsObj.getString("name"), false, pointsObj.getInt("id"));
                            pointsList.add(points);
                            Log.e("points are", String.valueOf(pointsList));
                        }
                    }
                    mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(DailyTeachingActivity.this));
                    mAdapter = new PointsAdapter(pointsList);
                    mRecyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
                    Log.e("sdcard-err2:", err);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", ApiKeyConstant.authToken);
                return headers;
            }
        };
        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
