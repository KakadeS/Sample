package com.example.vidhiraj.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by vidhiraj on 12-08-2016.
 */
public class ClassActivity extends AppCompatActivity {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<ClassData> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);


        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<ClassData>();
        data.add(new ClassData("ClassOne","Create Daily Teaching",R.drawable.ic_photos));
        data.add(new ClassData("ClassTwo","Create Daily Teaching",R.drawable.ic_photos));
        data.add(new ClassData("ClassThree","Create Daily Teaching",R.drawable.ic_photos));

        adapter = new ClassAdapter(data);
        recyclerView.setAdapter(adapter);
    }
}