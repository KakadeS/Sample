package com.example.vidhiraj.sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class PointsAdapter extends
        RecyclerView.Adapter<PointsAdapter.ViewHolder> {
    private List<PointsData> stList;

    public PointsAdapter(List<PointsData> students) {
        this.stList = students;
    }

    @Override
    public PointsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.points_row, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int pos = position;
        viewHolder.tvName.setText(stList.get(position).getName());
        viewHolder.chkSelected.setChecked(stList.get(position).isSelected());
        viewHolder.chkSelected.setTag(stList.get(position));
        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                PointsData contact = (PointsData) cb.getTag();
                contact.setSelected(cb.isChecked());
                stList.get(pos).setSelected(cb.isChecked());
            }
        });
    }

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

    public List<PointsData> getStudentist() {
        return stList;
    }
}