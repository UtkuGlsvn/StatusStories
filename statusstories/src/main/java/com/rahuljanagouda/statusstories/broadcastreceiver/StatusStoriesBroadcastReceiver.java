package com.rahuljanagouda.statusstories.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StatusStoriesBroadcastReceiver extends BroadcastReceiver {
    public interface StatusStoriesListener {
        void onNext(int counter);
        void onPrev(int counter);
        void onSkip(int counter);
        void onPause(int counter);
        void onFirst(int counter);
    }

    public StatusStoriesListener listener;
    public static final String KEY_ACTION_FIRST = "KEY_ACTION_FIRST";
    public static final String KEY_ACTION_NEXT = "KEY_ACTION_NEXT";
    public static final String KEY_ACTION_PREV = "KEY_ACTION_PREV";
    public static final String KEY_ACTION_SKIP = "KEY_ACTION_SKIP";
    public static final String KEY_ACTION_PAUSE = "KEY_ACTION_PAUSE";
    public static final String KEY_ACTION = "com.uniapps.KEY_ACTION";

    public static final String KEY_INDEX = "KEY_INDEX";

    public StatusStoriesBroadcastReceiver(StatusStoriesListener listener)
    {
        super();
        this.listener = listener;
    }

    public StatusStoriesBroadcastReceiver()
    {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (listener == null)
            return;

        String action = intent.getStringExtra(KEY_ACTION);
        Integer index = intent.getIntExtra(KEY_INDEX, 0);

        if (action == null)
            return;


        if(action.equalsIgnoreCase(KEY_ACTION_NEXT))
        {
            listener.onNext(index);
        }
        else if (action.equalsIgnoreCase(KEY_ACTION_PREV))
        {
            listener.onPrev(index);
        }
        else if (action.equalsIgnoreCase(KEY_ACTION_SKIP))
        {
            listener.onSkip(index);
        }
        else if (action.equalsIgnoreCase(KEY_ACTION_PAUSE))
        {
            listener.onPause(index);
        }
        else if (action.equalsIgnoreCase(KEY_ACTION_FIRST))
        {
            listener.onFirst(index);
        }


    }
}
