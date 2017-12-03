package com.pace.edustream.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pace.edustream.ModelData;
import com.pace.edustream.R;
import com.pace.edustream.activity.UnitActivity;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {

    private Context context;
    private ModelData topicList;
    private int[] img_ids = {R.drawable.sub_1, R.drawable.sub_2, R.drawable.sub_3};


    public SubjectAdapter(Context context, ModelData topicLst) {
        this.topicList = topicLst;
        this.context = context;
    }

    @Override
    public SubjectAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_subject, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubjectAdapter.ViewHolder viewHolder, int position) {

        viewHolder.tv_android.setText(topicList.getSubject().get(position).getName());
        viewHolder.rr.setBackground(ContextCompat.getDrawable(context, img_ids[position]));
        viewHolder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UnitActivity.class)
                        .putExtra("SUBJECT_INDEX", viewHolder.getAdapterPosition()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicList.getSubject().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private RelativeLayout rr;

        public ViewHolder(View view) {
            super(view);
            tv_android = (TextView) view.findViewById(R.id.tv_topic);
            rr = (RelativeLayout) view.findViewById(R.id.rr_subject);
        }
    }

}
