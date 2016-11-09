package com.example.vidhiraj.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 31/08/2016.
 */
public class PresentyCatalog extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<PresentyData> data = null;
    Button savePresenty, cancelPresenty;
    TextView dataAvailabiliy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presenty_catalog);
        final Intent intent = getIntent();
        final String id = intent.getStringExtra("dtp_id");
        Log.e("next dtp", String.valueOf(id));
        savePresenty = (Button) findViewById(R.id.savepresenty);
        cancelPresenty = (Button) findViewById(R.id.button);
        dataAvailabiliy = (TextView) findViewById(R.id.nodata);
        String url = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs/" + id + "/get_catlogs";
        Log.e("dtp url", url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                data = new ArrayList<>();
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        savePresenty.setVisibility(View.VISIBLE);
                        cancelPresenty.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = response.getJSONArray("class_catlogs");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orgObj = jsonArray.getJSONObject(i);
                                PresentyData classData = new PresentyData();
                                classData.setName(orgObj.getString("name"));
                                classData.setSelected(orgObj.getBoolean("is_present"));
                                classData.setPointId(orgObj.getInt("id"));
                                data.add(classData);
                            }
                        } else {
                            savePresenty.setVisibility(View.GONE);
                            cancelPresenty.setVisibility(View.GONE);
                            dataAvailabiliy.setVisibility(View.VISIBLE);
                        }
                    }

                    recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    adapter = new PresentyAdapter(PresentyCatalog.this, data);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PresentyCatalog.this));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());

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


        cancelPresenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(PresentyCatalog.this, DailyCatalogActivity.class);
                startActivity(intent1);
            }
        });


        savePresenty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder buff = new StringBuilder();
                String sep = "";
                List<PresentyData> stList = ((PresentyAdapter) adapter)
                        .getStudentist();
                for (int i = 0; i < stList.size(); i++) {
                    PresentyData singleStudent = stList.get(i);
                    if (singleStudent.isSelected() == false) {
                        buff.append(sep);
                        buff.append(singleStudent.getPointId());
                        sep = ",";
                        Log.e("buff is", String.valueOf(buff));

                    }
                }

                Log.e("buff is", String.valueOf(buff));
                JSONObject daily_teaching = new JSONObject();
                JSONObject daily_teaching_point = new JSONObject();

                try {
                    daily_teaching.put("absenty_string", buff);
                    daily_teaching_point.put("daily_teaching_point", daily_teaching);
                    Log.e("daily_teach", String.valueOf(daily_teaching_point));
                } catch (JSONException e) {

                    e.printStackTrace();
                }

                String loginURL = ApiKeyConstant.apiUrl + "/api/v1/daily_teachs/" + id + "/save_catlogs";
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, loginURL, daily_teaching_point, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(getBaseContext(), "Student Presenty Saved", Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(PresentyCatalog.this, DailyCatalogActivity.class);
                                startActivity(intent1);
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
                                Toast.makeText(getBaseContext(), "Student Presenty Not Saved", Toast.LENGTH_LONG).show();
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


    }
}
