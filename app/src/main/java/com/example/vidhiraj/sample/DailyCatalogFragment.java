//package com.example.vidhiraj.sample;
//
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
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
//import java.util.Map;
//
///**
// * Created by Lenovo on 11/11/2016.
// */
//
//public class DailyCatalogFragment extends Fragment {
//
//
//    private static DailyCatalogAdapter adapter;
//    private static RecyclerView recyclerView;
//    private LinearLayoutManager mLayoutManager;
//    private static ArrayList<DailyTeachData> dailyTeach = null;
//    int current_page = 1;
//    Button load;
//    ProgressDialog pDialog, mProgress;
//    TextView dataAvailability;
//    String url = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs";
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        //Returning the layout file after inflating
//        //Change R.layout.tab1 in you classes
//        View rootView = inflater.inflate(R.layout.daily_fill_catalog, container, false);
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//        dataAvailability = (TextView) rootView.findViewById(R.id.noData);
//        mProgress = new ProgressDialog(getContext());
//        mProgress.setTitle("Processing...");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCancelable(false);
//        mProgress.setIndeterminate(true);
//
//
//
//        load = (Button) rootView.findViewById(R.id.loadmore);
//        dailyTeach = new ArrayList<>();
//        mProgress.show();
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    boolean success = response.getBoolean("success");
//                    if (success) {
//                        mProgress.dismiss();
//                        JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
//                        int arrayLength = jsonArray.length();
//                        Log.e("array length is", String.valueOf(arrayLength));
//                        if (arrayLength >= 10) {
//                            load.setVisibility(View.VISIBLE);
//                        }
//                        if (jsonArray.length() != 0) {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject orgObj = jsonArray.getJSONObject(i);
//                                DailyTeachData dailyData = new DailyTeachData();
//                                dailyData.standard = orgObj.getString("jkci_class");
//                                dailyData.chapter = orgObj.getString("chapter");
//                                dailyData.date = orgObj.getString("date");
//                                dailyData.points = orgObj.getString("points");
//                                dailyData.id = orgObj.getInt("id");
//                                dailyTeach.add(dailyData);
//                            }
//                        } else {
//                            dataAvailability.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//                    recyclerView.setHasFixedSize(true);
//                    mLayoutManager = new LinearLayoutManager(getContext());
//                    recyclerView.setLayoutManager(mLayoutManager);
//                    adapter = new DailyCatalogAdapter(getContext(), dailyTeach, recyclerView);
//                    recyclerView.setAdapter(adapter);
//
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
//                        mProgress.dismiss();
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                headers.put("Authorization", ApiKeyConstant.authToken);
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
//        return rootView;
//    }
//
//
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
//                    String url = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs&page=" + current_page;
//
//                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                boolean success = response.getBoolean("success");
//                                if (success) {
//                                    JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
//
//                                    int arrayLength = jsonArray.length();
//                                    Log.e("array length is", String.valueOf(arrayLength));
//                                    if (arrayLength >= 10) {
//                                        load.setVisibility(View.VISIBLE);
//                                    }
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//                                        JSONObject orgObj = jsonArray.getJSONObject(i);
//                                        DailyTeachData dailyData = new DailyTeachData();
//                                        dailyData.standard = orgObj.getString("jkci_class");
//                                        dailyData.chapter = orgObj.getString("chapter");
//                                        dailyData.date = orgObj.getString("date");
//                                        dailyData.points = orgObj.getString("points");
//                                        dailyData.id = orgObj.getInt("id");
//                                        dailyTeach.add(dailyData);
//                                        adapter.notifyItemInserted(dailyTeach.size());
//                                    }
//
//
////                                    else
////                                    {
////                                        // Toast.makeText(getApplicationContext(),"No More Data to laod",Toast.LENGTH_LONG).show();
////                                        load.setVisibility(View.GONE);
////                                    }
//                                }
//                            } catch (JSONException e) {
//                                String err = (e.getMessage() == null) ? "SD Card failed" : e.getMessage();
//                                Log.e("sdcard-err2:", err);
//                            }
//
//                        }
//                    },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    load.setVisibility(View.GONE);
//                                    Toast.makeText(getContext(), "No More Data to laod", Toast.LENGTH_LONG).show();
//                                    Log.e("Poonam", "Error");
//                                }
//                            }) {
//                        @Override
//                        public Map<String, String> getHeaders() throws AuthFailureError {
//                            HashMap<String, String> headers = new HashMap<String, String>();
//                            headers.put("Content-Type", "application/json; charset=utf-8");
//                            headers.put("Authorization", ApiKeyConstant.authToken);
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
//
//
//
