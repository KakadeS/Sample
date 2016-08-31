package com.example.vidhiraj.sample;

import android.content.Intent;
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
 * Created by lenovo on 31/08/2016.
 */
public class PresentyCatalog extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private static RecyclerView recyclerView;
    private static ArrayList<PresentyData> data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String id= intent.getStringExtra("dtp_id");
        Log.e("next dtp", String.valueOf(id));

       String url=ApiKeyConstant.apiUrl + "/api/v1/daily_teachs/"+id+"/get_catlogs?authorization_token=" + ApiKeyConstant.authToken;
        Log.e("dtp url",url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,url,new JSONObject(), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                data=new ArrayList<>();
                try {
                    boolean success=response.getBoolean("success");
                    if(success)
                    {
                        JSONArray jsonArray = response.getJSONArray("class_catlogs");
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject orgObj = jsonArray.getJSONObject(i);
                            PresentyData classData = new PresentyData();
                            classData.setName(orgObj.getString("name"));
                            classData.setSelected(orgObj.getBoolean("is_present"));
                            data.add(classData);
                        }
                    }

                    recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
                    recyclerView.setHasFixedSize(true);
                    adapter = new PresentyAdapter(PresentyCatalog.this, data);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(PresentyCatalog.this));
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



        setContentView(R.layout.presenty_catalog);


    }
}
