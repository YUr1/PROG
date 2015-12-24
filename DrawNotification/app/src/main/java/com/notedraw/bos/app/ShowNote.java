package com.notedraw.bos.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;



public class ShowNote extends Activity{
	private static NotesDbAdapt ShowNoteDb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notedialog);
		
		Intent intent = getIntent();
		long time = intent.getLongExtra("Time_In_Millis", 0);

		Cursor c = null;
		ShowNoteDb = new NotesDbAdapt(this);
		ShowNoteDb.openDB();
		c = ShowNoteDb.getHandle().rawQuery("select * from note where "+NotesDbAdapt.KEY_TIME_IN_MILL + "=" + time, null);
		c.moveToFirst();
		
		TextView discrtext = (TextView) findViewById(R.id.discrtext);
		TextView timetext = (TextView) findViewById(R.id.timetext);
		TextView datetext = (TextView) findViewById(R.id.datetext);
		ImageView dialogimage = (ImageView)findViewById(R.id.dialogimage);
		
		if ((c.getColumnIndex(NotesDbAdapt.KEY_NAME)!=-1)) 
			discrtext.setText(c.getString(c.getColumnIndex(NotesDbAdapt.KEY_NAME)));
		if ((c.getColumnIndex(NotesDbAdapt.KEY_TIME)!=-1)) 
			timetext.setText(c.getString(c. getColumnIndex(NotesDbAdapt.KEY_TIME)));
		if ((c.getColumnIndex(NotesDbAdapt.KEY_DATE)!=-1)) 
			datetext.setText(c.getString(c. getColumnIndex(NotesDbAdapt.KEY_DATE)));
		if ((c.getColumnIndex(NotesDbAdapt.KEY_IMG)!=-1)) 
		{
			byte[] img = c.getBlob(c. getColumnIndexOrThrow(NotesDbAdapt.KEY_IMG));
			dialogimage.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
		}
		c.close();
		ShowNoteDb.openDB();
	}

}
