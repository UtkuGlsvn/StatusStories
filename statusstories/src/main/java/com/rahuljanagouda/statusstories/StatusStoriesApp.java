package com.rahuljanagouda.statusstories;

import android.app.Activity;

public class StatusStoriesApp {

    private static Activity currentActivity = null;

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        StatusStoriesApp.currentActivity = currentActivity;
    }
}
