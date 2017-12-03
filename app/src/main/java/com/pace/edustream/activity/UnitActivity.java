package com.pace.edustream.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pace.edustream.ModelData;
import com.pace.edustream.R;
import com.pace.edustream.adapter.SubjectAdapter;
import com.pace.edustream.adapter.UnitsAdapter;

import java.lang.reflect.Type;

/**
 * Created by pace on 27/9/17.
 */

public class UnitActivity extends AppCompatActivity {

    private TextView tv_subject_name;
    private RecyclerView rv_topic;
    private ModelData modelData;
    private Gson gson = new Gson();
    private int subject_index;
    private ModelData.SubjectBean subjectBean;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);


        subject_index = getIntent().getIntExtra("SUBJECT_INDEX", 0);

        Type collectionType = new TypeToken<ModelData>() {
        }.getType();
        modelData = gson.fromJson(MainActivity.EDU_DATA, collectionType);

        subjectBean = modelData.getSubject().get(subject_index);

        img_back = (ImageView) findViewById(R.id.img_back);
        rv_topic = (RecyclerView) findViewById(R.id.rv_units);
        tv_subject_name = (TextView) findViewById(R.id.tv_subject_name);

        tv_subject_name.setText(subjectBean.getName());
        rv_topic.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rv_topic.setLayoutManager(layoutManager);

        UnitsAdapter adapter = new UnitsAdapter(getApplicationContext(), subjectBean, subject_index);
        rv_topic.setAdapter(adapter);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

