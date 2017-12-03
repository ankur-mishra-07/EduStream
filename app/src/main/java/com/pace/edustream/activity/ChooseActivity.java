package com.pace.edustream.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pace.edustream.R;

/**
 * Created by pace on 13/11/17.
 */

public class ChooseActivity extends Activity implements View.OnClickListener {

    private Button btn_kn, btn_en, btn_continue;
    private TextView tv_continue;
    private SharedPreferences appPref;
    private SharedPreferences.Editor prefEditor;
    private boolean isEn = false, isKn = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        appPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        initView();
        setButtonBackground();

        if (appPref.getBoolean("Choosed", false)) {
            Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }


    private void setButtonBackground() {
        if (appPref.getString("DATA_LOCATION", "1").equals("1")) {
            selectBtn(btn_kn);
            unSelectBtn(btn_en);
            isEn = false;
            isKn = true;
            tv_continue.setText("Location selected SD Card");
        } else {
            tv_continue.setText("Location selected Internal Memory");
            selectBtn(btn_en);
            unSelectBtn(btn_kn);
            isEn = true;
            isKn = false;
        }

    }

    private void selectBtn(Button btn) {
        btn.setBackgroundColor(ContextCompat.getColor(ChooseActivity.this, R.color.colorAccent));
        btn.setTextColor(ContextCompat.getColor(ChooseActivity.this, R.color.white));
    }

    private void unSelectBtn(Button btn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            btn.setBackground(ContextCompat.getDrawable(ChooseActivity.this, R.drawable.customborder));
        btn.setTextColor(ContextCompat.getColor(ChooseActivity.this, R.color.black));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kn:
                if (!isKn) {
                    prefEditor = appPref.edit();
                    prefEditor.putString("DATA_LOCATION", "1");
                    prefEditor.apply();
                    selectBtn(btn_kn);
                    unSelectBtn(btn_en);
                    isKn = true;
                    isEn = false;
                    tv_continue.setText("Location selected SD Card");
                }
                break;

            case R.id.btn_en:
                if (!isEn) {
                    tv_continue.setText("Location selected Internal Memory");
                    prefEditor = appPref.edit();
                    prefEditor.putString("DATA_LOCATION", "2");
                    prefEditor.apply();
                    selectBtn(btn_en);
                    unSelectBtn(btn_kn);
                    isEn = true;
                    isKn = false;
                }
                break;

            case R.id.btn_continue:

                prefEditor = appPref.edit();
                prefEditor.putBoolean("Choosed", true);
                prefEditor.apply();

                Intent intent = new Intent(ChooseActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                break;
        }


    }

    private void initView() {
        btn_kn = (Button) findViewById(R.id.btn_kn);
        btn_en = (Button) findViewById(R.id.btn_en);
        tv_continue = (TextView) findViewById(R.id.tv_continue);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_kn.setOnClickListener(this);
        btn_en.setOnClickListener(this);
        btn_continue.setOnClickListener(this);


    }


}
