package com.example.vidhiraj.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 25/08/2016.
 */
public class StudentCatalogAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<StudentData> studentList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Context context;


    public StudentCatalogAdapter(List<StudentData> students, Context context) {
        studentList = students;
        this.context = context;

//        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
//
//            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
//                    .getLayoutManager();
//
//
//            recyclerView
//                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
//                        @Override
//                        public void onScrolled(RecyclerView recyclerView,
//                                               int dx, int dy) {
//                            super.onScrolled(recyclerView, dx, dy);
//
//                            totalItemCount = linearLayoutManager.getItemCount();
//                            lastVisibleItem = linearLayoutManager
//                                    .findLastVisibleItemPosition();
//                            if (!loading
//                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
//                                // End has been reached
//                                // Do something
//                                if (onLoadMoreListener != null) {
//                                    onLoadMoreListener.onLoadMore();
//                                }
//                                loading = true;
//                            }
//                        }
//                    });
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return studentList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.student_catalog_item, parent, false);

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

            StudentData singleStudent = (StudentData) studentList.get(position);

            ((StudentViewHolder) holder).textViewName.setText(singleStudent.getStud_name());

            ((StudentViewHolder) holder).textViewClass.setText(singleStudent.getStud_class_name());

            ((StudentViewHolder) holder).student = singleStudent;
            boolean hostel = singleStudent.stud_hostel;
            if (hostel) {
                ((StudentViewHolder) holder).textViewHostel.setText("yes");
            } else {
                ((StudentViewHolder) holder).textViewHostel.setText("no");
            }


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewClass;
        TextView textViewHostel;
        private Context context = null;
        public StudentData student;

        public StudentViewHolder(View v) {
            super(v);
            context = itemView.getContext();
            this.textViewName = (TextView) itemView.findViewById(R.id.stud_name);
            this.textViewClass = (TextView) itemView.findViewById(R.id.stud_class);
            this.textViewHostel = (TextView) itemView.findViewById(R.id.stud_hostel);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


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
