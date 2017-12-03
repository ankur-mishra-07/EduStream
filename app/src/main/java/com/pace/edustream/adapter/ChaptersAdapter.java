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
import com.pace.edustream.activity.PlayChapterActivity;


public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.ViewHolder> {

    private Context context;
    private ModelData.SubjectBean.UnitsBean unitsBean;

    public ChaptersAdapter(Context context, ModelData.SubjectBean.UnitsBean topicLst) {
        this.unitsBean = topicLst;
        this.context = context;
    }

    @Override
    public ChaptersAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_unit, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChaptersAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.tv_unit_title.setText(unitsBean.getChapter().get(position).getChapter_name());
        viewHolder.tv_unit_heading.setText("Chapter " + unitsBean.getChapter().get(position).getChapter_id());

        viewHolder.ll_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayChapterActivity.class);
                i.putExtra("Chapter_KEY_NAME", unitsBean.getChapter().get(position).getChapter_key_name());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return unitsBean.getChapter().size();
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
