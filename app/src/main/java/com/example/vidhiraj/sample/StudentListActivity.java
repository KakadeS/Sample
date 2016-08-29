package com.example.vidhiraj.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
 * Created by lenovo on 22/08/2016.
 */
public class StudentListActivity extends AppCompatActivity {

    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    private StudentCatalogAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private static List<StudentData> dailyTeach ;
    private int page=1;

   // private List<StudentData> studentList;


    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_catalog);
        //  toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        dailyTeach = new ArrayList<StudentData>();
        handler = new Handler();
        loadData();
    }
    // load initial data
    private void loadData() {

        final String loginURL = ApiKeyConstant.apiUrl + "/api/v1/students?authorization_token=" + ApiKeyConstant.authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        Log.e("first success", "sss");
                        JSONArray jsonArray = response.getJSONArray("students");
                        Log.e("json array", String.valueOf(jsonArray));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.e("for loop", String.valueOf(jsonArray.length()));
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            Log.e("json obj", String.valueOf(orgObj));
                            StudentData dailyData =  new StudentData();
                            dailyData.stud_name = orgObj.getString("name");
                            dailyData.stud_class_name = orgObj.getString("class_names");
                            dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
                            dailyTeach.add(dailyData);
                            Log.e("data is", String.valueOf(dailyTeach));

                        }
                    }

                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new StudentCatalogAdapter(dailyTeach, mRecyclerView);
                    mRecyclerView.setAdapter(mAdapter);

                    if (dailyTeach.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        tvEmptyView.setVisibility(View.VISIBLE);

                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        tvEmptyView.setVisibility(View.GONE);
                    }

                    mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {

                            //add null , so the adapter will check view_type and show progress bar at bottom
                            dailyTeach.add(null);
                            mAdapter.notifyItemInserted(dailyTeach.size() - 1);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //   remove progress item
                                    dailyTeach.remove(dailyTeach.size() - 1);
                                    mAdapter.notifyItemRemoved(dailyTeach.size());
                                    //add items one by one
                                     loadMoredata();
                                    mAdapter.setLoaded();
                                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                }
                            }, 2000);

                        }
                    });
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
                }
        );


        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void loadMoredata()
    {
        page++;
        String pageStr= String.valueOf(page);
        Log.e("page count is", String.valueOf(pageStr));
        final String page=ApiKeyConstant.apiUrl + "/api/v1/students?authorization_token=" + ApiKeyConstant.authToken+"&page="+pageStr;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, page, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        Log.e("first success", "sss");
                        JSONArray jsonArray = response.getJSONArray("students");
                        if(jsonArray!=null) {
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

                        Log.e("Poonam", "Error");
                    }
                }
        );

        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
    }

}

//
//    public void addTextListener() {
//
//        search.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {
//            }
//
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence query, int start, int before, int count) {
//
//                query = query.toString().toLowerCase();
//
//                final List<StudentData> filteredList = new ArrayList<>();
//
//                for (int i = 0; i < dailyTeach.size(); i++) {
//
//                    final String text = dailyTeach.get(i).getStud_class_name().toLowerCase();
//                    if (text.contains(query)) {
//
//                        filteredList.add(dailyTeach.get(i));
//                    }
//                }
//
//                recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
//                adapter = new StudentCatalogAdapter(StudentListActivity.this, (ArrayList<StudentData>) filteredList);
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();  // data set changed
//            }
//        });
//    }

