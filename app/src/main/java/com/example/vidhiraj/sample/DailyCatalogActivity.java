package com.example.vidhiraj.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 21/08/2016.
 */
public class DailyCatalogActivity extends AppCompatActivity {

    private static DailyCatalogAdapter adapter;
    private static RecyclerView recyclerView;
    protected Handler handler;
    private LinearLayoutManager mLayoutManager;
    private  static ArrayList<DailyTeachData> dailyTeach=null;
    int page=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_fill_catalog);
        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        dailyTeach=new ArrayList<>();
        handler = new Handler();
        loadData();

    }


    public void loadData(){

        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs?authorization_token=" + ApiKeyConstant.authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success=response.getBoolean("success");
                    if(success)
                    {
                        JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
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

                    if (dailyTeach.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                      //  tvEmptyView.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        //tvEmptyView.setVisibility(View.GONE);
                    }

                    adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {

                            //add null , so the adapter will check view_type and show progress bar at bottom
                            dailyTeach.add(null);
                            adapter.notifyItemInserted(dailyTeach.size() - 1);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //   remove progress item
                                    dailyTeach.remove(dailyTeach.size() - 1);
                                    adapter.notifyItemRemoved(dailyTeach.size());
                                    //add items one by one
                                    loadMoredata();
                                    adapter.setLoaded();
                                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                }
                            }, 2000);

                        }
                    });
                } catch (JSONException e) {
                    String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
                    Log.e("sdcard-err2:",err);
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
        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs?authorization_token=" + ApiKeyConstant.authToken+"&page="+pageStr;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success=response.getBoolean("success");
                    if(success)
                    {
                        JSONArray jsonArray = response.getJSONArray("daily_teaching_points");
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            DailyTeachData dailyData = new DailyTeachData();
                            dailyData.standard= orgObj.getString("jkci_class");
                            dailyData.chapter= orgObj.getString("chapter");
                            dailyData.date=orgObj.getString("date");
                            dailyData.points=orgObj.getString("points");
                            dailyData.id=orgObj.getInt("id");
                            dailyTeach.add(dailyData);
                            adapter.notifyItemInserted(dailyTeach.size());
                        }
                    }
                } catch (JSONException e) {
                    String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
                    Log.e("sdcard-err2:",err);
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
}