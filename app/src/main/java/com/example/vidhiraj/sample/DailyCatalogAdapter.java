package com.example.vidhiraj.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 21/08/2016.
 */
public class DailyCatalogAdapter extends RecyclerView.Adapter<DailyCatalogAdapter.MyViewHolder> {

    private ArrayList<DailyTeachData> dataSet;
    private Context mcontext;
    public DailyCatalogAdapter(Context mcontext, ArrayList<DailyTeachData> data) {
        this.dataSet = data;
        this.mcontext=mcontext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewClass;
        TextView textViewChapter;
        TextView textViewDate;
        TextView textViewPoints;
        private Context context = null;

        public MyViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.textViewClass = (TextView) itemView.findViewById(R.id.standard);
            this.textViewChapter = (TextView) itemView.findViewById(R.id.chapter);
            this.textViewDate = (TextView) itemView.findViewById(R.id.date);
            this.textViewPoints = (TextView) itemView.findViewById(R.id.points);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    String id=textViewId.getText().toString();
//                    String token=ApiKeyConstant.authToken;
//                    Intent intent=new Intent(context,DailyTeachingActivity.class);
//                    intent.putExtra("teach_id",id);
//                    intent.putExtra("auth_token",token);
//                    context.startActivity(intent);

                }
            });
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_fill_items, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView textViewClass = holder.textViewClass;
        TextView textViewChapter = holder.textViewChapter;
        TextView textViewDate = holder.textViewDate;
        TextView textViewPoints = holder.textViewPoints;
        textViewClass.setText(dataSet.get(position).getStandard());
        textViewChapter.setText(dataSet.get(position).getChapter());
        textViewDate.setText(dataSet.get(position).getDate());
        textViewPoints.setText(dataSet.get(position).getPoints());
    }

    @Override
    public int getItemCount() {
        Log.e("size is", String.valueOf(dataSet.size()));
        return dataSet.size();
    }

}

