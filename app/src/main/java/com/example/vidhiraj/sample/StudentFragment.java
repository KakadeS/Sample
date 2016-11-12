//package com.example.vidhiraj.sample;
//
//import android.annotation.TargetApi;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.roughike.bottombar.BottomBar;
//import com.roughike.bottombar.OnMenuTabSelectedListener;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Lenovo on 11/11/2016.
// */
//
//public class StudentFragment extends Fragment {
//
//    private TextView tvEmptyView;
//    private RecyclerView mRecyclerView;
//    private StudentCatalogAdapter mAdapter;
//    private LinearLayoutManager mLayoutManager;
//    private static List<StudentData> dailyTeach;
//    private int current_page = 1;
//    Button load;
//    EditText search;
//    ProgressDialog pDialog;
//    TextView dataAvailability;
//    // private List<StudentData> studentList;
//
//    String url= ApiKeyConstant.apiUrl + "/api/v1/students";
//    protected Handler handler;
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        //Returning the layout file after inflating
//        //Change R.layout.tab1 in you classes
//        View rootView = inflater.inflate(R.layout.student_catalog, container, false);
//
//
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//        dataAvailability=(TextView) rootView.findViewById(R.id.nodata);
//        // Set the color for the active tab. Ignored on mobile when there are more than three tabs.
//
//
//        load = (Button) rootView.findViewById(R.id.loadmore);
//        dailyTeach = new ArrayList<StudentData>();
//        handler = new Handler();
//        //  loadData();
//        search= (EditText) rootView.findViewById(R.id.search);
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
//            @TargetApi(Build.VERSION_CODES.M)
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("url is",url);
//                try {
//                    boolean success = response.getBoolean("success");
//                    if (success) {
//                        Log.e("first success", "sss");
//                        JSONArray jsonArray = response.getJSONArray("students");
//                        Log.e("json array", String.valueOf(jsonArray));
//                        int arrayLength=jsonArray.length();
//                        Log.e("array length is", String.valueOf(arrayLength));
//                        if(arrayLength >= 10)
//                        {
//                            load.setVisibility(View.VISIBLE);
//                        }
//                        if(jsonArray.length()!=0) {
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
//                                // mAdapter.notifyItemInserted(dailyTeach.size());
//
//                            }
//                        }
//                        else {
//                            dataAvailability.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    mRecyclerView.setHasFixedSize(true);
//                    mLayoutManager = new LinearLayoutManager(getActivity());
//                    mRecyclerView.setLayoutManager(mLayoutManager);
//                    mAdapter = new StudentCatalogAdapter(dailyTeach,getActivity());
//                    mRecyclerView.setAdapter(mAdapter);
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
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("Authorization",ApiKeyConstant.authToken);
//                return headers;
//            }
//        };
//
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//        load.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new loadMoreListView().execute();
//            }
//        });
//        addTextListener();
//        return rootView;
//
//    }
//
//
//    public void addTextListener() {
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
//                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                mAdapter = new StudentCatalogAdapter(filteredList,getActivity());
//                mRecyclerView.setAdapter(mAdapter);
//                mAdapter.notifyDataSetChanged();  // data set changed
//            }
//        });
//    }
//
//    private class loadMoreListView extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            // Showing progress dialog before sending http request
//            pDialog = new ProgressDialog(getContext());
//            pDialog.setMessage("Please wait..");
//            pDialog.setIndeterminate(true);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        protected Void doInBackground(final Void... unused) {
//            getActivity().runOnUiThread(new Runnable() {
//                public void run() {
//                    // increment current page
//                    current_page += 1;
//
//                    // Next page request
//                    url = ApiKeyConstant.apiUrl + "/api/v1/students&page="+ current_page;
//
//                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            try {
//                                boolean success = response.getBoolean("success");
//                                if (success) {
//                                    Log.e("first success", "sss");
//                                    JSONArray jsonArray = response.getJSONArray("students");
//                                    int arrayLength=jsonArray.length();
//                                    Log.e("array length is", String.valueOf(arrayLength));
//                                    if(arrayLength >= 10)
//                                    {
//                                        load.setVisibility(View.VISIBLE);
//                                    }
//
//                                    Log.e("json array", String.valueOf(jsonArray));
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        Log.e("for loop", String.valueOf(jsonArray.length()));
//                                        JSONObject orgObj = jsonArray.getJSONObject(i);
//                                        Log.e("json obj", String.valueOf(orgObj));
//                                        StudentData dailyData = new StudentData();
//                                        dailyData.stud_name = orgObj.getString("name");
//                                        dailyData.stud_class_name = orgObj.getString("class_names");
//                                        dailyData.stud_hostel = orgObj.getBoolean("has_hostel");
//                                        dailyTeach.add(dailyData);
//                                        Log.e("data is", String.valueOf(dailyTeach));
//                                        mAdapter.notifyItemInserted(dailyTeach.size());
//                                    }
//
//
//                                }
//
//                            } catch (JSONException e) {
//                                String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
//                                Log.e("sdcard-err2:", err);
//                            }
//
//                        }
//                    },
//                            new Response.ErrorListener() {
//                                @TargetApi(Build.VERSION_CODES.M)
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    load.setVisibility(View.GONE);
//                                    Toast.makeText(getContext(),"No More Data to laod",Toast.LENGTH_LONG).show();
//                                    Log.e("Poonam", "Error");
//                                }
//                            }) {
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            HashMap<String, String> headers = new HashMap<String, String>();
//                            headers.put("Content-Type", "application/json; charset=utf-8");
//                            headers.put("Authorization",ApiKeyConstant.authToken);
//                            return headers;
//                        }
//                    };
//
//                    VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//
//                }
//            });
//
//            return (null);
//        }
//
//        protected void onPostExecute(Void unused) {
//            // closing progress dialog
//            pDialog.dismiss();
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
