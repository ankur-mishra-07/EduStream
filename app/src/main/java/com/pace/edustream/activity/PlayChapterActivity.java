package com.pace.edustream.activity;

import android.app.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.longtailvideo.jwplayer.JWPlayerFragment;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.playlists.PlaylistItem;
import com.pace.edustream.EduApp;
import com.pace.edustream.ModelData;
import com.pace.edustream.R;


/**
 * The type Movie play activity.
 */
public class PlayChapterActivity extends Activity {

    private SharedPreferences appPref;
    private JWPlayerView playerView;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager pm;
    private String chapter_key_name = "";
    private boolean is_app_backgrd = false, from_popup = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_chapter);

        appPref = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the jwplayer view
        JWPlayerFragment fragment = (JWPlayerFragment) getFragmentManager().findFragmentById(R.id.playerFragment);
        playerView = fragment.getPlayer();

        //To keep screen awake
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        chapter_key_name = getIntent().getExtras().getString("Chapter_KEY_NAME");

        //Update the count of that particular movie
        try {
            String contentUri = appPref.getString("Chapter" + chapter_key_name, "");
            PlaylistItem video = new PlaylistItem(contentUri);
            playerView.load(video);

            if (appPref.getBoolean("isPlayingFirstTime" + chapter_key_name, true)) {
                playerView.play();
                appPref.edit().putBoolean("isPlayingFirstTime" + chapter_key_name, false).apply();
            } else {
                showResumePopup(appPref.getLong("PLAY_POSITION" + chapter_key_name, 0));
            }


        } catch (Exception ex) {
            Toast.makeText(PlayChapterActivity.this, "Sorry something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        playerView.play();
    }


    @Override
    protected void onResume() {
        super.onResume();
        EduApp.activityResumed();
        if (from_popup) {
            playerView.onResume();
            from_popup = false;
        } else if (is_app_backgrd) {
            playerView.play();
            playerView.play();
            is_app_backgrd = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        EduApp.activityPaused();
        playerView.onPause();
        playerView.stop();
    }

    @Override
    protected void onDestroy() {
        this.mWakeLock.release();
        appPref.edit().putLong("PLAY_POSITION" + chapter_key_name, playerView.getPosition()).apply();
        super.onDestroy();
    }

    // Exit fullscreen when the user pressed the Back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (playerView.getFullscreen()) {
                playerView.setFullscreen(false, false);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showResumePopup(final long position) {
        final Dialog dialog1 = new Dialog(PlayChapterActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.popup_resume);
        TextView tv = (TextView) dialog1.findViewById(R.id.tv);
        Button yes = (Button) dialog1.findViewById(R.id.btn_yes);
        Button no = (Button) dialog1.findViewById(R.id.btn_no);

        tv.setText(getResources().getString(R.string.resume));
        yes.setText(getResources().getString(R.string.yes));
        no.setText(getResources().getString(R.string.no));


        tv.setText(getResources().getString(R.string.resume));
        yes.setText(getResources().getString(R.string.yes));
        no.setText(getResources().getString(R.string.no));
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                if (position == 0) {
                    playerView.play();
                    playerView.play();
                } else {
                    playerView.seek(position);
                    playerView.play();
                    playerView.play();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                playerView.play();
            }
        });
        dialog1.setCancelable(false);
        dialog1.show();
        from_popup = true;

    }


}