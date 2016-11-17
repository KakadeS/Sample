package com.example.vidhiraj.sample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lenovo on 21/08/2016.
 */
public class DailyCatalogAdapter extends RecyclerView.Adapter {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private ArrayList<DailyTeachData> dataSet;
    private Context mcontext;

    public DailyCatalogAdapter(Context mcontext, ArrayList<DailyTeachData> data, RecyclerView recyclerView) {
        this.dataSet = data;
        this.mcontext = mcontext;


        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();


        recyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition();
                        if (!loading
                                && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            // End has been reached
                            // Do something
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            loading = true;
                        }
                    }
                });
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.daily_fill_items, parent, false);

            vh = new StudentViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StudentViewHolder) {

            DailyTeachData singleStudent = (DailyTeachData) dataSet.get(position);
            ((StudentViewHolder) holder).textViewClass.setText(singleStudent.getStandard());
            ((StudentViewHolder) holder).textViewChapter.setText(singleStudent.getChapter());
            ((StudentViewHolder) holder).textViewDate.setText(singleStudent.getDate());
            ((StudentViewHolder) holder).textViewPoints.setText(singleStudent.getPoints());
            ((StudentViewHolder) holder).textViewId.setText(Integer.toString(singleStudent.getId()));

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClass;
        TextView textViewChapter;
        TextView textViewDate;
        TextView textViewPoints;
        TextView textViewId;
        private Context context = null;
        public StudentData student;

        public StudentViewHolder(View v) {
            super(v);
            context = itemView.getContext();
            context = itemView.getContext();
            this.textViewClass = (TextView) itemView.findViewById(R.id.standard);
            this.textViewChapter = (TextView) itemView.findViewById(R.id.chapter);
            this.textViewDate = (TextView) itemView.findViewById(R.id.date);
            this.textViewPoints = (TextView) itemView.findViewById(R.id.points);
            this.textViewId = (TextView) itemView.findViewById(R.id.dailyid);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String id=textViewId.getText().toString();
                    Intent intent=new Intent(context,PresentyCatalog.class);
                    intent.putExtra("dtp_id",id);
                    context.startActivity(intent);
                }
            });
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}





