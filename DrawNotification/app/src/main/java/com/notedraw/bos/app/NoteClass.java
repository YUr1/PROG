package com.notedraw.bos.app;

import android.graphics.Bitmap;

public class NoteClass {

	    private Bitmap bmp;
	    private String name;
	    private String date;
	    private String time;
	    private String timeInMiills;

	    public NoteClass(Bitmap b, String n, String date1, String time1, String timeInMills1) {
	        bmp = b;
	        name = n;
	        date = date1; 
	        time = time1; 
	        timeInMiills = timeInMills1;
	     //   idKey = idKey1;
	    }
	    public Bitmap getBitmap() { return bmp; }
	    public String getName() { return name; }
	    public String getDate() { return date; }
	    public String getTime() { return time; }
	    public String getTimeInMillis() { return timeInMiills; }
	    //public int getIdKey() {return idKey;}
}
