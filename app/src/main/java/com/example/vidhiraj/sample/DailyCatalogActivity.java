package com.example.vidhiraj.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private  static ArrayList<DailyTeachData> dailyTeach=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_fill_catalog);
        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs?authorization_token=" + ApiKeyConstant.authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                dailyTeach=new ArrayList<DailyTeachData>();
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
                            dailyTeach.add(dailyData);
                        }
                    }

                    recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    adapter = new DailyCatalogAdapter(DailyCatalogActivity.this, dailyTeach);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(DailyCatalogActivity.this));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

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
