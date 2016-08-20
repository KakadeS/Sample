package com.example.vidhiraj.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
 * Created by vidhiraj on 12-08-2016.
 */
public class ClassActivity extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<ClassData> data=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        ApiKeyConstant.authToken=intent.getStringExtra("token");
        Log.e("token is",ApiKeyConstant.authToken);
        String loginURL = ApiKeyConstant.apiUrl + "/api/v1/time_table_classes.json?authorization_token=" + ApiKeyConstant.authToken;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,loginURL,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                data=new ArrayList<ClassData>();
                try {
                    boolean success=response.getBoolean("success");
                    if(success)
                    {
                        JSONArray jsonArray = response.getJSONArray("time_table_classes");
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            ClassData classData = new ClassData();
                            classData.name= orgObj.getString("class_name");
                            classData.subject= orgObj.getString("subject");
                            classData.image= R.drawable.daily_teach;
                            String id=orgObj.getString("id");
                            classData.id=Integer.parseInt(id);
                            data.add(classData);
                        }
                    }

                    recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    adapter = new ClassAdapter(ClassActivity.this, data);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ClassActivity.this));
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
        setContentView(R.layout.activity_class);
    }

}