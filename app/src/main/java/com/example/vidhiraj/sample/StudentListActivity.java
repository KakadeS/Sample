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
 * Created by lenovo on 22/08/2016.
 */
public class StudentListActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private  static ArrayList<StudentData> dailyTeach=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_catalog);
        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/students?authorization_token=" + ApiKeyConstant.authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                dailyTeach=new ArrayList<StudentData>();
                try {
                    boolean success=response.getBoolean("success");
                    if(success)
                    {
                        JSONArray jsonArray = response.getJSONArray("students");
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            StudentData dailyData = new StudentData();
                            dailyData.stud_name= orgObj.getString("name");
                            dailyData.stud_class_name= orgObj.getString("class_names");
                            dailyData.stud_hostel=orgObj.getBoolean("has_hostel");
                            dailyTeach.add(dailyData);
                        }
                    }

                    recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    adapter = new StudentCatalogAdapter(StudentListActivity.this, dailyTeach);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
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
