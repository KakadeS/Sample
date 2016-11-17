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

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder> {
    private ArrayList<ClassData> dataSet;
    private Context mcontext;

    public ClassAdapter(Context mcontext, ArrayList<ClassData> data) {
        this.dataSet = data;
        this.mcontext = mcontext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewId;
        TextView textViewVersion;
        ImageView imageViewIcon;
        private Context context = null;

        public MyViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewId = (TextView) itemView.findViewById(R.id.textViewid);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = textViewId.getText().toString();
                    String token = ApiKeyConstant.authToken;
                    Intent intent = new Intent(context, CatalogSchedule.class);
                    intent.putExtra("teach_id", id);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.class_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView textViewName = holder.textViewName;
        TextView textViewId = holder.textViewId;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;
        textViewName.setText(dataSet.get(position).getSubject());
        textViewVersion.setText(dataSet.get(position).getName());
        textViewId.setText(Integer.toString(dataSet.get(position).getId()));
        imageView.setImageResource(dataSet.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        Log.e("size is", String.valueOf(dataSet.size()));
        return dataSet.size();
    }
}