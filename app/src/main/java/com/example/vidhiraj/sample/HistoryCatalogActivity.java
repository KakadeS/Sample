package com.example.vidhiraj.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 22/08/2016.
 */
public class HistoryCatalogActivity extends AppCompatActivity {
    TextView daily_catalog_date;
    TextView daily_catalog_chapter;
    TextView daily_catalog_points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_catalog);
        daily_catalog_date= (TextView) findViewById(R.id.date);
        daily_catalog_chapter= (TextView) findViewById(R.id.chapter);
        daily_catalog_points= (TextView) findViewById(R.id.points);
        Intent intent=getIntent();
        String id=intent.getStringExtra("daily_id");
        Log.e("id is",id);
        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs/" + id +"?authorization_token=" + ApiKeyConstant.authToken;
        Log.e("daily url",loginURL);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success=response.getBoolean("success");
                    if(success)
                    {

                        JSONObject daily_teach=response.getJSONObject("daily_teaching_point");
                        daily_catalog_date.setText(daily_teach.getString("date"));
                        daily_catalog_chapter.setText(daily_teach.getString("chapter"));
                        daily_catalog_points.setText(daily_teach.getString("points"));
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
