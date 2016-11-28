package com.example.vidhiraj.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;
import java.util.Map;

import static com.example.vidhiraj.sample.AndroidSpinnerExampleActivity.MY_PREFS_NAME;

/**
 * Created by lenovo on 22/08/2016.
 */
public class StudentListActivity extends AppCompatActivity {

    String TITLES[] = {"Home", "Daily Catalog", "Students", "Logout"};
    int ICONS[] = {R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos, R.drawable.ic_photos};
    //String NAME = "Eracord";
    String org = null;
    int PROFILE = R.drawable.ic_photos;
    private Toolbar toolbar;                              // Declaring the Toolbar Object
    RecyclerView mDrawerRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mDrawerAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManagers;         // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    private GoogleApiClient client;


    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private StudentCatalogAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static List<StudentData> dailyTeach;
    private int current_page = 1;
    Button load;
    EditText search;
    ProgressDialog pDialog;
    TextView dataAvailability;
    String url_icon;
    ProgressDialog mProgress;
    // private List<StudentData> studentList;

    String url = ApiKeyConstant.apiUrl + "/api/v1/students";
    protected Handler handler;
    NestedScrollView scrollview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_catalog);
        //  toolbar = (Toolbar) findViewById(R.id.toolbar);
        // tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        dataAvailability = (TextView) findViewById(R.id.nodata);
        scrollview = ((NestedScrollView) findViewById(R.id.scrollView));
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

        mDrawerRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mDrawerRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mDrawerAdapter = new EraMyAdapter(StudentListActivity.this, TITLES, ICONS, user_email, url_icon);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mDrawerRecyclerView.setAdapter(mDrawerAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mDrawerRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        load = (Button) findViewById(R.id.loadmore);
        dailyTeach = new ArrayList<StudentData>();
        handler = new Handler();
        //  loadData();
        search = (EditText) findViewById(R.id.search);
        mProgress.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("url is", url);
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        mProgress.dismiss();
                        Log.e("first success", "sss");
                        JSONArray jsonArray = response.getJSONArray("students");
                        Log.e("json array", String.valueOf(jsonArray));
                        int arrayLength = jsonArray.length();
                        Log.e("array length is", String.valueOf(arrayLength));
                        if (arrayLength >= 10) {
                            load.setVisibility(View.VISIBLE);
                        }
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.e("for loop", String.valueOf(jsonArray.length()));
                                JSONObject orgObj = jsonArray.getJSONObject(i);
                                Log.e("json obj", String.valueOf(orgObj));
                                StudentData dailyData = new StudentData();
                                dailyData.stud_name = orgObj.getString("name");
                                dailyData.stud_class_name = orgObj.getString("class_names");
                                dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
                                dailyTeach.add(dailyData);
                                Log.e("data is", String.valueOf(dailyTeach));
                                // mAdapter.notifyItemInserted(dailyTeach.size());
                            }
                        } else {
                            dataAvailability.setVisibility(View.VISIBLE);
                        }
                    }

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new StudentCatalogAdapter(dailyTeach, getApplicationContext());
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
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == 401) {
                            Intent intent = new Intent(StudentListActivity.this, AndroidSpinnerExampleActivity.class);
                            startActivity(intent);
                        } else {
                            mProgress.dismiss();
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
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new loadMoreListView().execute();
            }
        });
        addTextListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(StudentListActivity.this, ClassActivity.class));
        finish();

    }


    public void addTextListener() {
        search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<StudentData> filteredList = new ArrayList<>();

                for (int i = 0; i < dailyTeach.size(); i++) {

                    final String text = dailyTeach.get(i).getStud_class_name().toLowerCase();
                    if (text.contains(query)) {

                        filteredList.add(dailyTeach.get(i));
                    }
                }

                mRecyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
                mAdapter = new StudentCatalogAdapter(filteredList, StudentListActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();  // data set changed
            }
        });
    }

    private class loadMoreListView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            pDialog = new ProgressDialog(
                    StudentListActivity.this);
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(final Void... unused) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // increment current page
                    current_page += 1;

                    // Next page request
                    url = ApiKeyConstant.apiUrl + "/api/v1/students?&page=" + current_page;
                    mProgress.show();
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                boolean success = response.getBoolean("success");
                                if (success) {
                                    mProgress.dismiss();
                                    Log.e("first success", "sss");
                                    JSONArray jsonArray = response.getJSONArray("students");
                                    int arrayLength = jsonArray.length();
                                    Log.e("array length is", String.valueOf(arrayLength));
                                    if (arrayLength >= 10) {
                                        load.setVisibility(View.VISIBLE);
                                    }

                                    Log.e("json array", String.valueOf(jsonArray));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Log.e("for loop", String.valueOf(jsonArray.length()));
                                        JSONObject orgObj = jsonArray.getJSONObject(i);
                                        Log.e("json obj", String.valueOf(orgObj));
                                        StudentData dailyData = new StudentData();
                                        dailyData.stud_name = orgObj.getString("name");
                                        dailyData.stud_class_name = orgObj.getString("class_names");
                                        dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
                                        dailyTeach.add(dailyData);
                                        Log.e("data is", String.valueOf(dailyTeach));
                                        mAdapter.notifyItemInserted(dailyTeach.size());
                                    }
                                    scrollview.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            int x=0,y=10;
                                            scrollview.scrollTo(x,y);
                                        }
                                    });
                                    if (arrayLength == 0) {
                                        load.setVisibility(View.GONE);
                                        mProgress.dismiss();
                                        Toast.makeText(getApplicationContext(), "No More Data to laod", Toast.LENGTH_LONG).show();
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
                                        Intent intent = new Intent(StudentListActivity.this, AndroidSpinnerExampleActivity.class);
                                        startActivity(intent);
                                    } else {
                                        load.setVisibility(View.GONE);
                                        mProgress.dismiss();
                                        Toast.makeText(getApplicationContext(), "No More Data to laod", Toast.LENGTH_LONG).show();
                                     //   Log.e("Poonam", error.getMessage());
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
            // closing progress dialog
            pDialog.dismiss();
        }
    }
}


//    // load initial data
//    private void loadData() {
//
//        loginURL = ApiKeyConstant.apiUrl + "/api/v1/students?authorization_token=" + ApiKeyConstant.authToken;
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//             Log.e("url is",loginURL);
//                try {
//                    boolean success = response.getBoolean("success");
//                    if (success) {
//                        Log.e("first success", "sss");
//                        JSONArray jsonArray = response.getJSONArray("students");
//                        Log.e("json array", String.valueOf(jsonArray));
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            Log.e("for loop", String.valueOf(jsonArray.length()));
//                            JSONObject orgObj = jsonArray.getJSONObject(i);
//                            Log.e("json obj", String.valueOf(orgObj));
//                            StudentData dailyData =  new StudentData();
//                            dailyData.stud_name = orgObj.getString("name");
//                            dailyData.stud_class_name = orgObj.getString("class_names");
//                            dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
//                            dailyTeach.add(dailyData);
//                            Log.e("data is", String.valueOf(dailyTeach));
//                          // mAdapter.notifyItemInserted(dailyTeach.size());
//
//                        }
//                    }
//
//                    mRecyclerView.setHasFixedSize(true);
//                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                    mRecyclerView.setLayoutManager(mLayoutManager);
//                    mAdapter = new StudentCatalogAdapter(dailyTeach, mRecyclerView);
//                    mRecyclerView.setAdapter(mAdapter);
//
//                    if (dailyTeach.isEmpty()) {
//                        mRecyclerView.setVisibility(View.GONE);
//                        tvEmptyView.setVisibility(View.VISIBLE);
//
//                    } else {
//                        mRecyclerView.setVisibility(View.VISIBLE);
//                        tvEmptyView.setVisibility(View.GONE);
//                    }
//
//                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//                        @Override
//                        public void onLoadMore() {
//                             page++;
//                            //add null , so the adapter will check view_type and show progress bar at bottom
//                            dailyTeach.add(null);
//                            mAdapter.notifyItemInserted(dailyTeach.size() - 1);
//
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //   remove progress item
//                                    dailyTeach.remove(dailyTeach.size() - 1);
//                                    mAdapter.notifyItemRemoved(dailyTeach.size());
//                                    //add items one by one
//                                     loadMoredata();
//                                    mAdapter.setLoaded();
//                                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                                }
//                            }, 2000);
//
//                        }
//                    });
//                } catch (JSONException e) {
//                    String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
//                    Log.e("sdcard-err2:", err);
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Log.e("Volley", "Error");
//                    }
//                }
//        );
//
//
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//    }
//
//    public void loadMoredata()
//    {
//        page++;
//        String pageStr= String.valueOf(page);
//        Log.e("page count is", String.valueOf(pageStr));
//        final String page=ApiKeyConstant.apiUrl + "/api/v1/students?authorization_token=" + ApiKeyConstant.authToken+"&page="+pageStr;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, page, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    boolean success = response.getBoolean("success");
//                    if (success) {
//                        Log.e("first success", "sss");
//                        JSONArray jsonArray = response.getJSONArray("students");
//                        if(jsonArray!=null) {
//                            Log.e("json array", String.valueOf(jsonArray));
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                Log.e("for loop", String.valueOf(jsonArray.length()));
//                                JSONObject orgObj = jsonArray.getJSONObject(i);
//                                Log.e("json obj", String.valueOf(orgObj));
//                                StudentData dailyData = new StudentData();
//                                dailyData.stud_name = orgObj.getString("name");
//                                dailyData.stud_class_name = orgObj.getString("class_names");
//                                dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
//                                dailyTeach.add(dailyData);
//                                Log.e("data is", String.valueOf(dailyTeach));
//                                mAdapter.notifyItemInserted(dailyTeach.size());
//                            }
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
//                    Log.e("sdcard-err2:", err);
//                }
//
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Toast.makeText(getApplicationContext(),"No More Data to laod",Toast.LENGTH_LONG).show();
//                        Log.e("Poonam", "Error");
//                    }
//                }
//        );
//
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//    }
//
////}
//
////
////    public void addTextListener() {
////
////        search.addTextChangedListener(new TextWatcher() {
////
////            public void afterTextChanged(Editable s) {
////            }
////
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////            }
////
////            public void onTextChanged(CharSequence query, int start, int before, int count) {
////
////                query = query.toString().toLowerCase();
////
////                final List<StudentData> filteredList = new ArrayList<>();
////
////                for (int i = 0; i < dailyTeach.size(); i++) {
////
////                    final String text = dailyTeach.get(i).getStud_class_name().toLowerCase();
////                    if (text.contains(query)) {
////
////                        filteredList.add(dailyTeach.get(i));
////                    }
////                }
////
////                recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
////                adapter = new StudentCatalogAdapter(StudentListActivity.this, (ArrayList<StudentData>) filteredList);
////                recyclerView.setAdapter(adapter);
////                adapter.notifyDataSetChanged();  // data set changed
////            }
////        });
////    }
//


//package com.example.vidhiraj.sample;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by lenovo on 22/08/2016.
// */
//public class StudentListActivity extends AppCompatActivity {
//
//    private TextView tvEmptyView;
//    private RecyclerView mRecyclerView;
//    private StudentCatalogAdapter mAdapter;
//    private LinearLayoutManager mLayoutManager;
//    private static List<StudentData> dailyTeach ;
//    private int page=1;
//
//   // private List<StudentData> studentList;
//
//
//    protected Handler handler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.student_catalog);
//        //  toolbar = (Toolbar) findViewById(R.id.toolbar);
//        tvEmptyView = (TextView) findViewById(R.id.empty_view);
//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        dailyTeach = new ArrayList<StudentData>();
//        handler = new Handler();
//        loadData();
//    }
//    // load initial data
//    private void loadData() {
//
//        final String loginURL = ApiKeyConstant.apiUrl + "/api/v1/students?authorization_token=" + ApiKeyConstant.authToken;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    boolean success = response.getBoolean("success");
//                    if (success) {
//                        Log.e("first success", "sss");
//                        JSONArray jsonArray = response.getJSONArray("students");
//                        Log.e("json array", String.valueOf(jsonArray));
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            Log.e("for loop", String.valueOf(jsonArray.length()));
//                            JSONObject orgObj = jsonArray.getJSONObject(i);
//                            Log.e("json obj", String.valueOf(orgObj));
//                            StudentData dailyData =  new StudentData();
//                            dailyData.stud_name = orgObj.getString("name");
//                            dailyData.stud_class_name = orgObj.getString("class_names");
//                            dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
//                            dailyTeach.add(dailyData);
//                            Log.e("data is", String.valueOf(dailyTeach));
//
//                        }
//                    }
//
//                    mRecyclerView.setHasFixedSize(true);
//                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                    mRecyclerView.setLayoutManager(mLayoutManager);
//                    mAdapter = new StudentCatalogAdapter(dailyTeach, mRecyclerView);
//                    mRecyclerView.setAdapter(mAdapter);
//
//                    if (dailyTeach.isEmpty()) {
//                        mRecyclerView.setVisibility(View.GONE);
//                        tvEmptyView.setVisibility(View.VISIBLE);
//
//                    } else {
//                        mRecyclerView.setVisibility(View.VISIBLE);
//                        tvEmptyView.setVisibility(View.GONE);
//                    }
//
//                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//                        @Override
//                        public void onLoadMore() {
//
//                            //add null , so the adapter will check view_type and show progress bar at bottom
//                            dailyTeach.add(null);
//                            mAdapter.notifyItemInserted(dailyTeach.size() - 1);
//
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //   remove progress item
//                                    dailyTeach.remove(dailyTeach.size() - 1);
//                                    mAdapter.notifyItemRemoved(dailyTeach.size());
//                                    //add items one by one
//                                     loadMoredata();
//                                    mAdapter.setLoaded();
//                                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                                }
//                            }, 2000);
//
//                        }
//                    });
//                } catch (JSONException e) {
//                    String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
//                    Log.e("sdcard-err2:", err);
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Log.e("Volley", "Error");
//                    }
//                }
//        );
//
//
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//    }
//
//    public void loadMoredata()
//    {
//        page++;
//        String pageStr= String.valueOf(page);
//        Log.e("page count is", String.valueOf(pageStr));
//        final String page=ApiKeyConstant.apiUrl + "/api/v1/students?authorization_token=" + ApiKeyConstant.authToken+"&page="+pageStr;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, page, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    boolean success = response.getBoolean("success");
//                    if (success) {
//                        Log.e("first success", "sss");
//                        JSONArray jsonArray = response.getJSONArray("students");
//                        if(jsonArray!=null) {
//                            Log.e("json array", String.valueOf(jsonArray));
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                Log.e("for loop", String.valueOf(jsonArray.length()));
//                                JSONObject orgObj = jsonArray.getJSONObject(i);
//                                Log.e("json obj", String.valueOf(orgObj));
//                                StudentData dailyData = new StudentData();
//                                dailyData.stud_name = orgObj.getString("name");
//                                dailyData.stud_class_name = orgObj.getString("class_names");
//                                dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
//                                dailyTeach.add(dailyData);
//                                Log.e("data is", String.valueOf(dailyTeach));
//                                mAdapter.notifyItemInserted(dailyTeach.size());
//                            }
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
//                    Log.e("sdcard-err2:", err);
//                }
//
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Log.e("Poonam", "Error");
//                    }
//                }
//        );
//
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//    }
//
//}
//
////
////    public void addTextListener() {
////
////        search.addTextChangedListener(new TextWatcher() {
////
////            public void afterTextChanged(Editable s) {
////            }
////
////            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////            }
////
////            public void onTextChanged(CharSequence query, int start, int before, int count) {
////
////                query = query.toString().toLowerCase();
////
////                final List<StudentData> filteredList = new ArrayList<>();
////
////                for (int i = 0; i < dailyTeach.size(); i++) {
////
////                    final String text = dailyTeach.get(i).getStud_class_name().toLowerCase();
////                    if (text.contains(query)) {
////
////                        filteredList.add(dailyTeach.get(i));
////                    }
////                }
////
////                recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
////                adapter = new StudentCatalogAdapter(StudentListActivity.this, (ArrayList<StudentData>) filteredList);
////                recyclerView.setAdapter(adapter);
////                adapter.notifyDataSetChanged();  // data set changed
////            }
////        });
////    }
//
