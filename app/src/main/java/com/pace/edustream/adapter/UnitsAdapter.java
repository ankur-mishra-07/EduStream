package com.pace.edustream.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pace.edustream.ModelData;
import com.pace.edustream.R;
import com.pace.edustream.activity.ChapterActivity;


public class UnitsAdapter extends RecyclerView.Adapter<UnitsAdapter.ViewHolder> {

    private Context context;
    private ModelData.SubjectBean subjectBean;
    private int subject_index;


    public UnitsAdapter(Context context, ModelData.SubjectBean subjectBean, int subject_index) {
        this.subjectBean = subjectBean;
        this.context = context;
        this.subject_index = subject_index;
    }

    @Override
    public UnitsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_unit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UnitsAdapter.ViewHolder viewHolder, int position) {

        viewHolder.tv_unit_heading.setText("UNIT " + subjectBean.getUnits().get(position).getUnit_id());
        viewHolder.tv_unit_title.setText(subjectBean.getUnits().get(position).getUnit_name());

        viewHolder.ll_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChapterActivity.class)
                        .putExtra("SUBJECT_INDEX", subject_index)
                        .putExtra("UNIT_INDEX", viewHolder.getAdapterPosition())
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectBean.getUnits().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_unit_title, tv_unit_heading;
        private LinearLayout ll_unit;

        public ViewHolder(View view) {
            super(view);
            tv_unit_title = (TextView) view.findViewById(R.id.tv_unit_title);
            tv_unit_heading = (TextView) view.findViewById(R.id.tv_unit_heading);
            ll_unit = (LinearLayout) view.findViewById(R.id.ll_unit);
        }
    }

}
