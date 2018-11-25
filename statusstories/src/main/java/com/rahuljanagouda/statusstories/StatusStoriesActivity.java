package com.rahuljanagouda.statusstories;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.rahuljanagouda.statusstories.broadcastreceiver.StatusStoriesBroadcastReceiver;
import com.rahuljanagouda.statusstories.data.StatusStoriesObject;
import com.rahuljanagouda.statusstories.glideProgressBar.DelayBitmapTransformation;
import com.rahuljanagouda.statusstories.glideProgressBar.LoggingListener;
import com.rahuljanagouda.statusstories.glideProgressBar.ProgressTarget;

import java.util.Locale;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class StatusStoriesActivity extends AppCompatActivity implements StoryStatusView.UserInteractionListener {

    public static final String STATUS_RESOURCES_KEY = "statusStoriesResources";
    public static final String STATUS_DURATION_KEY = "statusStoriesDuration";
    public static final String STATUS_DURATIONS_ARRAY_KEY = "statusStoriesDurations";
    public static final String IS_IMMERSIVE_KEY = "isImmersive";
    public static final String IS_CACHING_ENABLED_KEY = "isCaching";
    public static final String IS_TEXT_PROGRESS_ENABLED_KEY = "isText";



    public static final String KEY_STATUS_STORIES_LISTENER = "STATUS_STORIES_LISTENER";
    public static final String KEY_STATUS_STORIES = "KEY_STATUS_STORIES";

    private static StoryStatusView storyStatusView;
    private ImageView image;
    private int counter = 0;

//    private String[] statusResources;
    //    private long[] statusResourcesDuration;
 //   private long statusDuration;
 //   private boolean isImmersive = true;
 //   private boolean isCaching = true;
    private static boolean isTextEnabled = true;

    private StatusStoriesObject statusStoriesObject;

    private ProgressTarget<String, Bitmap> target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_stories);

        statusStoriesObject = (StatusStoriesObject) getIntent().getSerializableExtra(this.KEY_STATUS_STORIES);
        isTextEnabled = statusStoriesObject.getTextProgressEnabled();


        ProgressBar imageProgressBar = findViewById(R.id.imageProgressBar);
        TextView textView = findViewById(R.id.textView);
        image = findViewById(R.id.image);

        storyStatusView = findViewById(R.id.storiesStatus);
        storyStatusView.setStoriesCount(statusStoriesObject.getResources().size());
        storyStatusView.setStoryDuration(statusStoriesObject.getDuration());
        // or
        // statusView.setStoriesCountWithDurations(statusResourcesDuration);
        storyStatusView.setUserInteractionListener(this);
        storyStatusView.playStories();
        target = new MyProgressTarget<>(new BitmapImageViewTarget(image), imageProgressBar, textView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyStatusView.skip();
            }
        });

        storyStatusView.pause();
        target.setModel(statusStoriesObject.getResources().get(counter));


        sendBroadcastEvent(StatusStoriesBroadcastReceiver.KEY_ACTION_FIRST, counter);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                //.skipMemoryCache(!isCaching)
                //.diskCacheStrategy(isCaching ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)

                .priority(Priority.HIGH);



        GlideApp.with(image.getContext()).asBitmap()
                .load(target.getModel())
                .transition(withCrossFade())
                .apply(options.transforms(new CenterCrop(), new DelayBitmapTransformation(1000)))
                .listener(new LoggingListener<Bitmap>())
                .into(target);

        // bind reverse view
        findViewById(R.id.reverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyStatusView.reverse();
            }
        });

        // bind skip view
        findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyStatusView.skip();
            }
        });

        findViewById(R.id.actions).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    storyStatusView.pause();
                } else {
                    storyStatusView.resume();
                }
                return true;
            }
        });



    }

    private void sendBroadcastEvent(String actionName, Integer index){
        Intent i = new Intent(StatusStoriesBroadcastReceiver.KEY_ACTION);
        i.putExtra(StatusStoriesBroadcastReceiver.KEY_ACTION, actionName);
        i.putExtra(StatusStoriesBroadcastReceiver.KEY_INDEX, index);
        sendBroadcast(i);
    }

    @Override
    public void onNext() {
        //Log.d("SEH", "onNext");
        storyStatusView.pause();
        ++counter;
        target.setModel(statusStoriesObject.getResources().get(counter));

        sendBroadcastEvent(StatusStoriesBroadcastReceiver.KEY_ACTION_NEXT, counter);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                //.skipMemoryCache(!isCaching)
                //.diskCacheStrategy(isCaching ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)

                .priority(Priority.HIGH);

        sendBroadcastEvent(StatusStoriesBroadcastReceiver.KEY_ACTION_NEXT, counter);

        GlideApp.with(image.getContext()).asBitmap()
                .load(target.getModel())
                .transition(withCrossFade())
                .apply(options.transforms(new CenterCrop(), new DelayBitmapTransformation(1000)))
                .listener(new LoggingListener<Bitmap>())
                .into(target);
    }

    @Override
    public void onPrev() {

        if (counter - 1 < 0) return;
        storyStatusView.pause();
        --counter;
        target.setModel(statusStoriesObject.getResources().get(counter));


        sendBroadcastEvent(StatusStoriesBroadcastReceiver.KEY_ACTION_PREV, counter);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .skipMemoryCache(!statusStoriesObject.getCachingEnabled())
                .diskCacheStrategy(statusStoriesObject.getCachingEnabled() ? DiskCacheStrategy.ALL : DiskCacheStrategy.NONE)

                .priority(Priority.HIGH);

        GlideApp.with(image.getContext()).asBitmap()
                .load(target.getModel())
                .transition(withCrossFade())

                .apply(options.transforms(new CenterCrop(), new DelayBitmapTransformation(1000)))
                .listener(new LoggingListener<Bitmap>())
                .into(target);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (statusStoriesObject.getImmersive() && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (hasFocus) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    @Override
    public void onComplete() {
        finish();
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storyStatusView.destroy();
        super.onDestroy();
    }

    /**
     * Demonstrates 3 different ways of showing the progress:
     * <ul>
     * <li>Update a full fledged progress bar</li>
     * <li>Update a text view to display size/percentage</li>
     * <li>Update the placeholder via Drawable.level</li>
     * </ul>
     * This last one is tricky: the placeholder that Glide sets can be used as a progress drawable
     * without any extra Views in the view hierarchy if it supports levels via <code>usesLevel="true"</code>
     * or <code>level-list</code>.
     *
     * @param <Z> automatically match any real Glide target so it can be used flexibly without reimplementing.
     */
    @SuppressLint("SetTextI18n") // text set only for debugging
    private static class MyProgressTarget<Z> extends ProgressTarget<String, Z> {
        private final TextView text;
        private final ProgressBar progress;

        public MyProgressTarget(Target<Z> target, ProgressBar progress, TextView text) {
            super(target);
            this.progress = progress;
            this.text = text;
        }

        @Override
        public float getGranualityPercentage() {
            return 0.1f; // this matches the format string for #text below
        }

        @Override
        protected void onConnecting() {
            //Log.d("SEH", "onConnecting");
            progress.setIndeterminate(true);
            progress.setVisibility(View.VISIBLE);

            if (isTextEnabled) {
                text.setVisibility(View.VISIBLE);
                text.setText("connecting");
            } else {
                text.setVisibility(View.INVISIBLE);
            }
            storyStatusView.pause();
        }

        @Override
        protected void onDownloading(long bytesRead, long expectedLength) {
            progress.setIndeterminate(false);
            progress.setProgress((int) (100 * bytesRead / expectedLength));

            if (isTextEnabled) {
                text.setVisibility(View.VISIBLE);
                text.setText(String.format(Locale.ROOT, "downloading %.2f/%.2f MB %.1f%%",
                        bytesRead / 1e6, expectedLength / 1e6, 100f * bytesRead / expectedLength));
            } else {
                text.setVisibility(View.INVISIBLE);
            }


            storyStatusView.pause();

        }

        @Override
        protected void onDownloaded() {
            //Log.d("SEH", "onDownloaded");
            progress.setIndeterminate(true);
            if (isTextEnabled) {
                text.setVisibility(View.VISIBLE);
                text.setText("decoding and transforming");
            } else {
                text.setVisibility(View.INVISIBLE);
            }


            storyStatusView.pause();
        }

        @Override
        protected void onDelivered() {
            //Log.d("SEH", "onDelivered");
            progress.setVisibility(View.INVISIBLE);
            text.setVisibility(View.INVISIBLE);
            storyStatusView.resume();
        }
    }
}
