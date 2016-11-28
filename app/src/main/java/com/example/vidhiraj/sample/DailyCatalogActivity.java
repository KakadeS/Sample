package com.example.vidhiraj.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.vidhiraj.sample.AndroidSpinnerExampleActivity.MY_PREFS_NAME;

public class DailyCatalogActivity extends AppCompatActivity {
    String TITLES[] = {"Home", "Daily Catalog", "Students", "Logout"};
    int ICONS[] = {R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos};
    String org = null;
    int PROFILE = R.drawable.ic_photos;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManagers;         // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;
    private GoogleApiClient client;
    private static DailyCatalogAdapter adapter;
    private static RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private static ArrayList<DailyTeachData> dailyTeach = null;
    int current_page = 1;
    Button load;
    ProgressDialog pDialog, mProgress;
    TextView dataAvailability;
    String url_icon;
    String url = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_fill_catalog);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        dataAvailability = (TextView) findViewById(R.id.noData);
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
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
        mAdapter = new EraMyAdapter(DailyCatalogActivity.this, TITLES, ICONS, user_email, url_icon);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
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
        load = (Button) findViewById(R.id.loadmore);
        dailyTeach = new ArrayList<>();
        mProgress.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        mProgress.dismiss();
                        JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
                        int arrayLength = jsonArray.length();
                        Log.e("array length is", String.valueOf(arrayLength));
                        if (arrayLength >= 10) {
                            load.setVisibility(View.VISIBLE);
                        }
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orgObj = jsonArray.getJSONObject(i);
                                DailyTeachData dailyData = new DailyTeachData();
                                dailyData.standard = orgObj.getString("jkci_class");
                                dailyData.chapter = orgObj.getString("chapter");
                                dailyData.date = orgObj.getString("date");
                                dailyData.points = orgObj.getString("points");
                                dailyData.id = orgObj.getInt("id");
                                dailyTeach.add(dailyData);
                            }
                        } else {
                            dataAvailability.setVisibility(View.VISIBLE);
                        }
                    }
                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    adapter = new DailyCatalogAdapter(getApplicationContext(), dailyTeach, recyclerView);
                    recyclerView.setAdapter(adapter);
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
                            Intent intent = new Intent(DailyCatalogActivity.this, AndroidSpinnerExampleActivity.class);
                            startActivity(intent);
                        } else {
                            mProgress.dismiss();
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
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new loadMoreListView().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DailyCatalogActivity.this, ClassActivity.class));
        finish();
    }

    private class loadMoreListView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    DailyCatalogActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(final Void... unused) {
            runOnUiThread(new Runnable() {
                public void run() {
                    current_page += 1;
                    String url = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs&page=" + current_page;
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success = response.getBoolean("success");
                                if (success) {
                                    JSONArray jsonArray = response.getJSONArray("daily_teaching_points");

                                    int arrayLength = jsonArray.length();
                                    Log.e("array length is", String.valueOf(arrayLength));
                                    if (arrayLength >= 10) {
                                        load.setVisibility(View.VISIBLE);
                                    }
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject orgObj = jsonArray.getJSONObject(i);
                                        DailyTeachData dailyData = new DailyTeachData();
                                        dailyData.standard = orgObj.getString("jkci_class");
                                        dailyData.chapter = orgObj.getString("chapter");
                                        dailyData.date = orgObj.getString("date");
                                        dailyData.points = orgObj.getString("points");
                                        dailyData.id = orgObj.getInt("id");
                                        dailyTeach.add(dailyData);
                                        adapter.notifyItemInserted(dailyTeach.size());
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
                                    NetworkResponse networkResponse = error.networkResponse;
                                    if (networkResponse != null && networkResponse.statusCode == 401) {
                                        Intent intent = new Intent(DailyCatalogActivity.this, AndroidSpinnerExampleActivity.class);
                                        startActivity(intent);
                                    } else {
                                        load.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "No More Data to laod", Toast.LENGTH_LONG).show();
                                        Log.e("Poonam", "Error");
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
            return (null);
        }

        protected void onPostExecute(Void unused) {
            pDialog.dismiss();
        }
    }
}
