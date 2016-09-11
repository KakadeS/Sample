package com.example.vidhiraj.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 21/08/2016.
 */

public class DailyCatalogActivity extends AppCompatActivity {

    private static DailyCatalogAdapter adapter;
    private static RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private  static ArrayList<DailyTeachData> dailyTeach=null;
    int current_page=1;
    Button load;
    ProgressDialog pDialog,mProgress;
    String url=ApiKeyConstant.apiUrl + "/api/v1/daily_teachs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_fill_catalog);
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.main_menu2, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                Intent intent;
                switch (itemId) {
                    case R.id.create_item:
                        intent=new Intent(DailyCatalogActivity.this,ClassActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.student_item:
                        intent=new Intent(DailyCatalogActivity.this,StudentListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.teach_item:
                        intent=new Intent(DailyCatalogActivity.this,DailyCatalogActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
        bottomBar.setActiveTabColor("#337ab7");





        load= (Button) findViewById(R.id.loadmore);
        dailyTeach=new ArrayList<>();
        mProgress.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success=response.getBoolean("success");
                    if(success)
                    {
                        mProgress.dismiss();
                        JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
                        int arrayLength=jsonArray.length();
                        Log.e("array length is", String.valueOf(arrayLength));
                        if(arrayLength >= 10)
                        {
                            load.setVisibility(View.VISIBLE);
                        }
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            DailyTeachData dailyData = new DailyTeachData();
                            dailyData.standard= orgObj.getString("jkci_class");
                            dailyData.chapter= orgObj.getString("chapter");
                            dailyData.date=orgObj.getString("date");
                            dailyData.points=orgObj.getString("points");
                            dailyData.id=orgObj.getInt("id");
                            dailyTeach.add(dailyData);
                        }
                    }

                    recyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    adapter = new DailyCatalogAdapter(getApplicationContext(),dailyTeach, recyclerView);
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

                        Log.e("Volley", "Error");
                        mProgress.dismiss();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization",ApiKeyConstant.authToken);
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
    public void onBackPressed()
    {
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
                    // increment current page
                    current_page += 1;

                    // Next page request
                    String url = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs&page="+current_page;

                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url,new JSONObject(), new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                boolean success=response.getBoolean("success");
                                if(success)
                                {
                                    JSONArray jsonArray = response.getJSONArray("daily_teaching_points");

                                    int arrayLength=jsonArray.length();
                                    Log.e("array length is", String.valueOf(arrayLength));
                                    if(arrayLength >= 10)
                                    {
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


//                                    else
//                                    {
//                                        // Toast.makeText(getApplicationContext(),"No More Data to laod",Toast.LENGTH_LONG).show();
//                                        load.setVisibility(View.GONE);
//                                    }
                                }
                            }  catch (JSONException e) {
                                String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
                                Log.e("sdcard-err2:", err);
                            }

                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    load.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"No More Data to laod",Toast.LENGTH_LONG).show();
                                    Log.e("Poonam", "Error");
                                }
                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            headers.put("Authorization",ApiKeyConstant.authToken);
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





















//public class DailyCatalogActivity extends AppCompatActivity {
//
//    private static DailyCatalogAdapter adapter;
//    private static RecyclerView recyclerView;
//    protected Handler handler;
//    private LinearLayoutManager mLayoutManager;
//    private  static ArrayList<DailyTeachData> dailyTeach=null;
//    int page=1;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.daily_fill_catalog);
//        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
//        dailyTeach=new ArrayList<>();
//        handler = new Handler();
//        loadData();
//
//    }
//
//
//    public void loadData(){
//
//        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs?authorization_token=" + ApiKeyConstant.authToken;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    boolean success=response.getBoolean("success");
//                    if(success)
//                    {
//                        JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
//                        for (int i=0; i<jsonArray.length(); i++) {
//                            JSONObject orgObj = jsonArray.getJSONObject(i);
//                            DailyTeachData dailyData = new DailyTeachData();
//                            dailyData.standard= orgObj.getString("jkci_class");
//                            dailyData.chapter= orgObj.getString("chapter");
//                            dailyData.date=orgObj.getString("date");
//                            dailyData.points=orgObj.getString("points");
//                            dailyData.id=orgObj.getInt("id");
//                            dailyTeach.add(dailyData);
//                        }
//                    }
//
//                    recyclerView.setHasFixedSize(true);
//                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                    recyclerView.setLayoutManager(mLayoutManager);
//                    adapter = new DailyCatalogAdapter(getApplicationContext(),dailyTeach, recyclerView);
//                    recyclerView.setAdapter(adapter);
//
//                    if (dailyTeach.isEmpty()) {
//                        recyclerView.setVisibility(View.GONE);
//                      //  tvEmptyView.setVisibility(View.VISIBLE);
//
//                    } else {
//                        recyclerView.setVisibility(View.VISIBLE);
//                        //tvEmptyView.setVisibility(View.GONE);
//                    }
//
//                    adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//                        @Override
//                        public void onLoadMore() {
//
//                            //add null , so the adapter will check view_type and show progress bar at bottom
//                            dailyTeach.add(null);
//                            adapter.notifyItemInserted(dailyTeach.size() - 1);
//
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //   remove progress item
//                                    dailyTeach.remove(dailyTeach.size() - 1);
//                                    adapter.notifyItemRemoved(dailyTeach.size());
//                                    //add items one by one
//                                    loadMoredata();
//                                    adapter.setLoaded();
//                                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
//                                }
//                            }, 2000);
//
//                        }
//                    });
//                } catch (JSONException e) {
//                    String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
//                    Log.e("sdcard-err2:",err);
//                }
//
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
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//    }
//
//
//
//
//    public void loadMoredata()
//    {
//        page++;
//        String pageStr= String.valueOf(page);
//        Log.e("page count is", String.valueOf(pageStr));
//        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs?authorization_token=" + ApiKeyConstant.authToken+"&page="+pageStr;
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    boolean success=response.getBoolean("success");
//                    if(success)
//                    {
//                        JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
//                        for (int i=0; i<jsonArray.length(); i++) {
//                            JSONObject orgObj = jsonArray.getJSONObject(i);
//                            DailyTeachData dailyData = new DailyTeachData();
//                            dailyData.standard= orgObj.getString("jkci_class");
//                            dailyData.chapter= orgObj.getString("chapter");
//                            dailyData.date=orgObj.getString("date");
//                            dailyData.points=orgObj.getString("points");
//                            dailyData.id=orgObj.getInt("id");
//                            dailyTeach.add(dailyData);
//                            adapter.notifyItemInserted(dailyTeach.size());
//                        }
//                    }
//                } catch (JSONException e) {
//                    String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
//                    Log.e("sdcard-err2:",err);
//                }
//
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
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//
//    }
//}
