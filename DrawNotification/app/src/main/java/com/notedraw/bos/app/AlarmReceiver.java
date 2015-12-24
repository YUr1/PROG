package com.notedraw.bos.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.notedraw.bos.app.MainNote;

public class AlarmReceiver extends BroadcastReceiver {

	
 @Override
 public void onReceive(Context context, Intent intent) {
     Bundle bundle = intent.getExtras();
     String message = bundle.getString("alarm_message");
     Long timeInMillis = bundle.getLong("time_in_millis");
     Log.e("TIME IN AlarmReceiver", timeInMillis.toString());
     boolean Vibration = bundle.getBoolean("Vibration");
     boolean Sound = bundle.getBoolean("Sound");
     NotifierHelper NH = new NotifierHelper();
     if (message.equals(""))
    	 NH.sendNotification(context, ShowNote.class, context.getResources().getString(R.string.NotTitle), context.getResources().getString(R.string.Nodiscr), 1, Sound, true, Vibration, timeInMillis);
     else 
    	 NH.sendNotification(context, ShowNote.class, context.getResources().getString(R.string.NotTitle), message, 1, Sound, true, Vibration, timeInMillis);
 }
}