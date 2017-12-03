package com.pace.edustream;

import android.app.Application;


public class EduApp extends Application {

    private static EduApp ourInstance = new EduApp();

    public static EduApp getInstance() {
        return ourInstance;
    }

    private EduApp() {
    }

    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

}
