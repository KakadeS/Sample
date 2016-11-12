//package com.example.vidhiraj.sample;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.android.volley.AuthFailureError;
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
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Lenovo on 11/11/2016.
// */
//
//public class CreateFragment extends Fragment {
//
//
//    private static RecyclerView.Adapter adapter;
//    private static RecyclerView recyclerView;
//    private static ArrayList<ClassData> data = null;
//    private static ArrayList<DailyTeachData> dailyTeach = null;
//    SwipeRefreshLayout swipeRefreshLayout;
//    boolean mIsRefreshing = false;
//    TextView dataAvailability;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        //Returning the layout file after inflating
//        //Change R.layout.tab1 in you classes
//        View rootView = inflater.inflate(R.layout.activity_class, container, false);
//        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
//        dataAvailability = (TextView) rootView.findViewById(R.id.nodata);
//        fetchClassData();
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (Utils.isConnected(getContext())) {
//                    mIsRefreshing = true;
//                    fetchClassData();
//                } else {
//                    android.os.Handler handler = new android.os.Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }, 5000);
//
//                }
//            }
//        });
//        return rootView;
//    }
//
//    public void fetchClassData() {
//
//        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/time_table_classes.json";
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, loginURL, new JSONObject(), new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                data = new ArrayList<ClassData>();
//                try {
//                    boolean success = response.getBoolean("success");
//                    if (success) {
//                        swipeRefreshLayout.setRefreshing(false);
//                        JSONArray jsonArray = response.getJSONArray("time_table_classes");
//                        if (jsonArray.length() != 0) {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject orgObj = jsonArray.getJSONObject(i);
//                                ClassData classData = new ClassData();
//                                classData.name = orgObj.getString("class_name");
//                                classData.subject = orgObj.getString("subject");
//                                classData.image = R.drawable.daily_teach;
//                                String id = orgObj.getString("id");
//                                classData.id = Integer.parseInt(id);
//                                data.add(classData);
//                            }
//                        } else {
//                            dataAvailability.setVisibility(View.VISIBLE);
//                        }
//                    }
//
//
//                    recyclerView.setHasFixedSize(true);
//                    adapter = new ClassAdapter(data);
//                    recyclerView.setAdapter(adapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                    recyclerView.setItemAnimator(new DefaultItemAnimator());
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
//                        Log.e("Volley", "Error");
//                        swipeRefreshLayout.setRefreshing(false);
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
//        VolleyControl.getInstance().addToRequestQueue(jsonObjReq);
//    }
//}
