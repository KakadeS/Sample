package com.example.vidhiraj.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 22/08/2016.
 */
public class StudentCatalogAdapter  extends RecyclerView.Adapter<StudentCatalogAdapter.MyViewHolder> {

    private ArrayList<StudentData> dataSet;
    private Context mcontext;
    public StudentCatalogAdapter(Context mcontext, ArrayList<StudentData> data) {
        this.dataSet = data;
        this.mcontext=mcontext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewClass;
        TextView textViewHostel;
        private Context context = null;

        public MyViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.textViewName = (TextView) itemView.findViewById(R.id.stud_name);
            this.textViewClass = (TextView) itemView.findViewById(R.id.stud_class);
            this.textViewHostel = (TextView) itemView.findViewById(R.id.stud_hostel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_catalog_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView textViewName= holder.textViewName;
        TextView textViewClass = holder.textViewClass;
        TextView textViewHostel = holder.textViewHostel;
        textViewName.setText(dataSet.get(position).getStud_name());
        textViewClass.setText(dataSet.get(position).getStud_class_name());
        boolean hostel=dataSet.get(position).stud_hostel;
        if(hostel)
        {
            textViewHostel.setText("yes");
        }
        else {
            textViewHostel.setText("no");
        }

    }

    @Override
    public int getItemCount() {
        Log.e("size is", String.valueOf(dataSet.size()));
        return dataSet.size();
    }

}

