package com.wooplr.spotlight.utils;

/**
 * Created by Carlos Reyna on 30/07/16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wooplr.spotlight.SpotlightConfig;
import com.wooplr.spotlight.SpotlightView;
import com.wooplr.spotlight.prefs.PreferencesManager;
import com.wooplr.spotlight.target.Target;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class responsible for performing a sequence of
 * spotlight animations one after the other.
 */
public class SpotlightSequence {

    private final Activity activity;
    private final LinkedList<SpotlightView.Builder> queue;
    private final  AtomicBoolean running = new AtomicBoolean(false);
    private final SpotlightListener listener = new SpotlightListener() {
        @Override public void onUserClicked(SpotlightView l, String s) {
            playNext(l);
        }
        @Override public void onAborted(SpotlightView l, String s) {
            playNext(l);
        }
    };
    private SpotlightConfig config;

    private final String TAG = "Tour Sequence";
    private final PreferencesManager pm;

    /**
     * Creates an instance of SpotlightSequence
     * with an empty queue and a {@link SpotlightConfig} configuration
     * @param activity where this sequence will be executed
     * @param config {@link SpotlightConfig}
     */
    public SpotlightSequence(Activity activity, SpotlightConfig config){
        Log.d(TAG,"NEW TOUR_SEQUENCE INSTANCE");
        this.activity = activity;
        setConfig(config);
        queue = new LinkedList<>();
        pm = new PreferencesManager(activity);
    }

    /**
     * Adds a new SpotlightView.Builder object to {@link this.queue}
     * @param target View where the spotlight will focus
     * @param title Spotlight title
     * @param subtitle Spotlight subtitle
     *                 * @param usageId id used to store the SpotlightView in {@link PreferencesManager}
     * @return SpotlightSequence instance
     */
    public SpotlightSequence addSpotlight(@NonNull Target target, CharSequence title, CharSequence subtitle, String usageId){
        // Try to avoid work based on no repeat logic
        if ( usageId != null ) {
            if ( pm.isDisplayed(usageId)) return this;
            for (SpotlightView.Builder sb : queue) {
                if (usageId.equals(sb.getUsageId()) ) {
                    Log.d(TAG, "skip duplicate "+usageId+" #"+queue.size());
                    return this;
                }
            }
        }
        Log.d(TAG, "Adding " + usageId+" "+queue.size());
        SpotlightView.Builder builder = new SpotlightView.Builder(activity)
                .setConfiguration(config)
                .headingTvText(title)
                .usageId(usageId)
                .subHeadingTvText(subtitle)
                .target(target)
                .setListener(listener)
                .enableDismissAfterShown(true);
        queue.add(builder);
        return this;
    }

    /**
     * Adds a new SpotlightView.Builder object to {@link this.queue}
     * @param target View where the spotlight will focus
     * @param titleResId Spotlight title
     * @param subTitleResId Spotlight subtitle
     * @param usageId id used to store the SpotlightView in {@link PreferencesManager}
     * @return SpotlightSequence instance
     */
    public SpotlightSequence addSpotlight(@NonNull Target target, int titleResId, int subTitleResId, String usageId){
        String title = activity.getString(titleResId);
        String subtitle = activity.getString(subTitleResId);
        return addSpotlight(target, title, subtitle, usageId);
    }

    public boolean isShowing() {
	return running.get() || (queue != null && queue.size() > 0);
    }

    /**
     * Starts the sequence.
     */
    public void startSequence(){
        Log.d(TAG, "startSequence() -> "+running.get()+" "+queue.size());
        if(queue.isEmpty()) {
            Log.d(TAG, "EMPTY SEQUENCE");
            running.set(false);
        }else if ( running.compareAndSet(false,true) ){
            queue.poll().show();
        }
    }

    /**
     * Free variables. Executed when the tour has finished
     */
    public void clear() {
        queue.clear();
        running.set(false);
    }

    /**
     * Executes the next Spotlight animation in the queue.
     * If no more animations are found, resetTour()is called.
     * @param l
     */
    private void playNext(SpotlightView l){
        Log.d(TAG,"PLAYING NEXT SPOTLIGHT "+running.get()+" "+queue.size());
        SpotlightView.Builder next = queue.poll();
        if(next != null){
            next.setSpeedStart();
            next.show().setReady(true);
            // l.invalidate(); // can we limit close animation?
        }else {
            Log.d(TAG, "END OF QUEUE");
            running.set(false);
        }
    }

    /**
     * Clear all Spotlights usageId from shared preferences.
     * @param context
     */
    public static void resetSpotlights(@NonNull Context context){
        new PreferencesManager(context).resetAll();
    }

    /**
     * Sets the specified {@link SpotlightConfig} configuration
     * as the configuration to use.
     * If no configuration is specified, the default configuration is used.
     * @param config {@link SpotlightConfig}
     */
    private void setConfig(@Nullable SpotlightConfig config) {
        if(config == null){
            config = new SpotlightConfig();
            config.setLineAndArcColor(Color.parseColor("#eb273f"));
            config.setDismissOnTouch(true);
            config.setMaskColor(Color.argb(240,0,0,0));
            config.setHeadingTvColor(Color.parseColor("#eb273f"));
            config.setHeadingTvSize(32);
            config.setSubHeadingTvSize(16);
            config.setSubHeadingTvColor(Color.parseColor("#ffffff"));
            config.setPerformClick(false);
            config.setRevealAnimationEnabled(true);
            config.setLineAnimationDuration(400);
        }
        this.config = config;
    }

}

