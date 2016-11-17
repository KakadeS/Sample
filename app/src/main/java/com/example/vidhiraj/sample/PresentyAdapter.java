package com.example.vidhiraj.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class PresentyAdapter extends
        RecyclerView.Adapter<PresentyAdapter.ViewHolder> {
    private List<PresentyData> stList;
    Context context;

    public PresentyAdapter(Context context, List<PresentyData> students) {
        this.context = context;
        this.stList = students;
    }

    @Override
    public PresentyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.presenty_catalog_row, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final int pos = position;
        viewHolder.tvName.setText(stList.get(position).getName());
        viewHolder.chkSelected.setOnCheckedChangeListener(null);
        viewHolder.chkSelected.setChecked(stList.get(position).isSelected());
        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                } else {
                }
            }
        });

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return stList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvEmailId;
        public CheckBox chkSelected;

        public PointsData singlestudent;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);

            chkSelected = (CheckBox) itemLayoutView
                    .findViewById(R.id.chkSelected);


        }

    }

    // method to access in activity after updating selection
    public List<PresentyData> getStudentist() {
        return stList;
    }

}