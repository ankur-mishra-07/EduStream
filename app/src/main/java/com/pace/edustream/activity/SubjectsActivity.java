package com.pace.edustream.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pace.edustream.ModelData;
import com.pace.edustream.R;
import com.pace.edustream.adapter.SubjectAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by pace on 27/9/17.
 */

public class SubjectsActivity extends AppCompatActivity {

    private RecyclerView rv_subject, rv_navList;
    private ModelData modelData;
    private Gson gson = new Gson();
    private ImageView img_menu;
    private LinearLayoutManager layoutManager;
    private ArrayList<String> navListItems;
    private NavListAdapter navAdapter;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main_layout);

        rv_subject = (RecyclerView) findViewById(R.id.rv_topic);
        rv_navList = (RecyclerView) findViewById(R.id.recycler_view_tab);
        img_menu = (ImageView) findViewById(R.id.icn_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar_tab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        layoutManager = new LinearLayoutManager(this);
        rv_navList.setLayoutManager(layoutManager);

        setNavigationAdapter();


        Type collectionType = new TypeToken<ModelData>() {
        }.getType();
        modelData = gson.fromJson(MainActivity.EDU_DATA, collectionType);


        rv_subject.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        rv_subject.setLayoutManager(layoutManager);

        SubjectAdapter adapter = new SubjectAdapter(getApplicationContext(), modelData);
        rv_subject.setAdapter(adapter);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

    }

    private void setNavigationAdapter() {
        navListItems = new ArrayList<>();
        navListItems.add("About Us");
        navListItems.add("Settings");

        navAdapter = new NavListAdapter(navListItems, getApplicationContext());
        rv_navList.setAdapter(navAdapter);

    }


    /**
     * Created by pace on 3/7/17.
     */

    public class NavListAdapter extends RecyclerView.Adapter<NavListAdapter.PlanetViewHolder> {


        private Context mContext;
        private ArrayList<String> navItemsList;
        private int[] imag_ids = {R.drawable.internet, R.drawable.settings};

        public NavListAdapter(ArrayList<String> navList, Context context) {
            this.navItemsList = navList;
            this.mContext = context;
        }

        @Override
        public NavListAdapter.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_nav_item, parent, false);
            PlanetViewHolder viewHolder = new PlanetViewHolder(v);
            v.setClickable(true);
            v.setFocusableInTouchMode(true);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(NavListAdapter.PlanetViewHolder holder, final int position) {

            holder.text.setText(navItemsList.get(position));
            holder.img_nav.setImageDrawable(ContextCompat.getDrawable(mContext, imag_ids[position]));
            holder.text.setTextColor(ContextCompat.getColor(mContext, R.color.black));


        }

        @Override
        public int getItemCount() {
            return (navItemsList == null) ? 0 : navItemsList.size();
        }


        public class PlanetViewHolder extends RecyclerView.ViewHolder {
            protected TextView text;
            protected ImageView img_nav;

            public PlanetViewHolder(View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.tv_nav_name);
                img_nav = (ImageView) itemView.findViewById(R.id.img_nav);
            }
        }


    }


}

