package com.notedraw.bos.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;

public class NotifierHelper {

    public void sendNotification(Context caller, Class<?> activityToLaunch, String title, String msg, int numberOfEvents,boolean sound, boolean flashLed, boolean vibrate, long timeInMillis) {
        NotificationManager notifier = (NotificationManager) caller.getSystemService(Context.NOTIFICATION_SERVICE);

        final Notification notify = new Notification(R.drawable.icon, "", System.currentTimeMillis());
        
        notify.icon = R.drawable.icon;
        notify.tickerText = title;
        notify.when = System.currentTimeMillis();
        notify.number = numberOfEvents;
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        if (sound) notify.defaults |= Notification.DEFAULT_SOUND;

        if (flashLed) {
        // add lights
            notify.flags |= Notification.FLAG_SHOW_LIGHTS;
            notify.ledARGB = Color.CYAN;
            notify.ledOnMS = 500;
            notify.ledOffMS = 500;
        }

        if (vibrate) {
            notify.vibrate = new long[] {100, 200, 300};
        }

        Intent toLaunch = new Intent(caller, activityToLaunch);
        toLaunch.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        toLaunch.putExtra("Time_In_Millis", timeInMillis);
        int NOTIFY_1 = (int) SystemClock.elapsedRealtime()/10000; 
        PendingIntent intentBack = PendingIntent.getActivity(caller, NOTIFY_1, toLaunch, PendingIntent.FLAG_UPDATE_CURRENT);

        notify.setLatestEventInfo(caller, title, msg, intentBack);   
        notifier.notify(NOTIFY_1, notify);
    }

    public void clear(Activity caller) {
        NotificationManager notifier = (NotificationManager) caller.getSystemService(Context.NOTIFICATION_SERVICE);
        notifier.cancelAll();
    }
}