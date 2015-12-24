package com.notedraw.bos.app;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class OnBootReceiver1 extends BroadcastReceiver {
	boolean flag = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try{
        	File log = new File(context.getFilesDir(), "AlarmsFromDrawNote.txt");
    		Log.e("###########", "$$$$$$$"+context.getFilesDir().getAbsolutePath()+"$$$$$$$$$$$$$$$$$$$$$");
    		FileInputStream in = context.openFileInput("AlarmsFromDrawNote.txt");
    		BufferedReader lin = new BufferedReader(new InputStreamReader(in));
    	    		

    	    		String AlarmString;
    	    		int i = 0;
    	    		
    	    		Log.e("AlarmsFromDrawNote", "###################################################");
    	    		while ((AlarmString = lin.readLine())!=null)
    	    		{	
    	    			Log.e("AlarmsFromDrawNote", AlarmString);
    	    			Long AlarmTime = Long.parseLong(AlarmString.substring(0, AlarmString.indexOf("|")));
    	    			if (AlarmTime < System.currentTimeMillis() && !flag) i++;
    	    			else {
    	    				flag = true;
    	    				String AlarmArray[] = AlarmString.split("\\|");
    	    				
    	    				Intent AlarmIntent = new Intent(context , AlarmReceiver.class);
    	    				Log.e("AlarmsFromDrawNote", AlarmArray[1].toString());
    	    				AlarmIntent.putExtra("time_in_millis", AlarmTime);
    	    				
    	    				if (AlarmArray[1].equals("null")) AlarmIntent.putExtra("alarm_message", "");
    	    				else AlarmIntent.putExtra("alarm_message", AlarmArray[1]);
    	    				
    	    				if (AlarmArray[2].equals("true"))
    	    				AlarmIntent.putExtra("Vibration", true);
    	    				else AlarmIntent.putExtra("Vibration", false);
    	    				
    	    				if (AlarmArray[3].equals("true"))
    	    				AlarmIntent.putExtra("Sound", true);
    	    				else AlarmIntent.putExtra("Sound", false);
    	    				
    	    				
    	    				
    	    	        	 final int _ID =(int) (Math.random()*10000); 
    	    	        	 PendingIntent sender = PendingIntent.getBroadcast(context , _ID, AlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    	    	        	 AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    	    	        	 am.set(AlarmManager.RTC_WAKEUP, AlarmTime, sender);
    	    			}
    	    		}
    	    		lin.close();
    	    		in.close();
    	    		Log.e("AlarmsFromDrawNote", Integer.toString(i));
    	    		if (i>0)
    	    		{
    	    		 try {
    	    			File TmpFile = new File(context.getFilesDir(), "Alarmstext.tmp");
    	    		    OutputStream out = context.openFileOutput("Alarmstext.tmp", Context.MODE_APPEND);
    	          		BufferedWriter lout = new BufferedWriter(new OutputStreamWriter(out));
    	    		      if (!log.isFile()) {
    	    		        Log.e("Mis", "Parameter is not an existing file");
    	    		        return;
    	    		      }
    	    		      
    	    		      BufferedReader br = new BufferedReader(new FileReader(log));
    	    		      
    	    		      String line = null;

    	    		      int j = 0;
    	    		      while ((line = br.readLine()) != null) {
    	    		  		Log.e("AlarmsFromDrawNote ��� ����������", line);
    	    		        if (j>=i){
    	    		        	Log.e("Alarmstext.tmp", line);
    	    		        	lout.write(line);
    	    		        	lout.write("\n");
    	    		        	lout.flush();
    	    		        }
    	    		        j++;
    	    		      }
    	    		      lout.close();
    	    		      br.close();
    	    		      
    	    		      //Видалити оригінальний файл
    	    		      if (!log.delete()) {
    	    		        System.out.println("Could not delete file");
    	    		        return;
    	    		      } 
    	    		      
    	    		      //Перейменуйте новий файл в імені файлу вихідний файл був.
    	    		      if (!TmpFile.renameTo(log))
    	    		        System.out.println("Could not rename file");
    	    		      
    	    		    }
    	    		    catch (FileNotFoundException ex) {
    	    		      ex.printStackTrace();
    	    		    }
    	    		    catch (IOException ex) {
    	    		      ex.printStackTrace();
    	    		    }
    	    		  }
    	    }
    	    catch(Exception e) {Log.e("ERROR", "##############################################################");
    	    	e.printStackTrace();};

}

}

