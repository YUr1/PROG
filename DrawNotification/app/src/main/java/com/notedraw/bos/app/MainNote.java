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
import java.util.ArrayList;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextMenu;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


public class MainNote extends Activity {
	
	  private Button AddNew;
	  private final int DELETE_ID = 0;
	  private final int ACTIVITY_CREATE = 0;
	  public static NotesDbAdapt mDB;
	  private GridView gridview;
	  public File log;
	  private final String[] COLUMBS = {NotesDbAdapt.KEY_ID, NotesDbAdapt.KEY_IMG, NotesDbAdapt.KEY_NAME, NotesDbAdapt.KEY_DATE, NotesDbAdapt.KEY_TIME};
	  private final String   TABLE   = NotesDbAdapt.NOTES_TABLE;


	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainnote);  
        mDB = new NotesDbAdapt(this);
        mDB.openDB();
       // mDB.Reset();
        AddNew = (Button) findViewById(R.id.add1);
        AddNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainNote.this, FingerPaint.class);
				startActivityForResult(intent, ACTIVITY_CREATE);
			}
		});

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainNote.this);
				AlertDialog alertDialog;

				Context mContext = getApplicationContext();
				LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.notedialog,
				                               (ViewGroup) findViewById(R.id.layout_root));
				TextView k_i = (TextView) arg1.findViewById(R.id.key_id); 
				String id =k_i.getText().toString();
				
				Cursor c = null;
				c = MainNote.mDB.getHandle().rawQuery("select * from note where "+NotesDbAdapt.KEY_ID + "=" + id, null);
				c.moveToFirst();
				
				TextView discrtext = (TextView) layout.findViewById(R.id.discrtext);
				TextView timetext = (TextView) layout.findViewById(R.id.timetext);
				TextView datetext = (TextView) layout.findViewById(R.id.datetext);
				ImageView dialogimage = (ImageView)layout.findViewById(R.id.dialogimage);
				
				if ((c.getColumnIndex(NotesDbAdapt.KEY_NAME)!=-1)) 
					if (c.getString(c.getColumnIndex(NotesDbAdapt.KEY_NAME)).equals("")) discrtext.setText(R.string.Nodiscr);
					else discrtext.setText(c.getString(c.getColumnIndex(NotesDbAdapt.KEY_NAME)));
				
				if ((c.getColumnIndex(NotesDbAdapt.KEY_TIME)!=-1)) 
					if (c.getString(c.getColumnIndex(NotesDbAdapt.KEY_TIME)).equals("0")) timetext.setText(R.string.Noreminder);
					else timetext.setText(c.getString(c.getColumnIndex(NotesDbAdapt.KEY_TIME)));
				
				if ((c.getColumnIndex(NotesDbAdapt.KEY_DATE)!=-1)) 
					if (c.getString(c.getColumnIndex(NotesDbAdapt.KEY_DATE)).equals("0")) datetext.setText(R.string.Noreminder);
					else datetext.setText(c.getString(c.getColumnIndex(NotesDbAdapt.KEY_DATE)));
				if ((c.getColumnIndex(NotesDbAdapt.KEY_IMG)!=-1)) 
				{
					byte[] img = c.getBlob(c. getColumnIndexOrThrow(NotesDbAdapt.KEY_IMG));
					dialogimage.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
				}
				c.close();
				builder.setView(layout);
				alertDialog = builder.create();
				if (!alertDialog.isShowing()) alertDialog.show();
			}
		});
        setNotes();
        registerForContextMenu(gridview); 
    }
    
    public void setNotes()
    {
        Cursor c = MainNote.mDB.getHandle().query(TABLE, COLUMBS, null, null, null, null, null);
 
        startManagingCursor(c);
       
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.file_dialog_row,
                c,
                new String[] {NotesDbAdapt.KEY_ID ,NotesDbAdapt.KEY_IMG, NotesDbAdapt.KEY_NAME, NotesDbAdapt.KEY_DATE, NotesDbAdapt.KEY_TIME},
                new int[] {R.id.key_id ,R.id.img, R.id.txt, R.id.date, R.id.time});
       
        adapter.setViewBinder(new NotesBinder());
 
        gridview.setAdapter(adapter);
    	}
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                View view = info.targetView;
                TextView k_i = (TextView) view.findViewById(R.id.key_id); 
        		int id = Integer.parseInt(k_i.getText().toString());
                mDB.deleteNote(id);
                setNotes();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    

    
    public synchronized void onActivityResult(final int requestCode,
            int resultCode, final Intent data) 
    {
    	if (resultCode == RESULT_CANCELED)
    	{
    	setNotes();
    	long time = data.getLongExtra("Calendar", 0);
    	boolean vibr = data.getBooleanExtra("Vibration", true);
    	boolean sound = data.getBooleanExtra("Sound", true);
    	int Mleft = (int) (((time - System.currentTimeMillis())/1000) / 60);
    	int Sleft = (int) (((time - System.currentTimeMillis())/1000) % 60);
    	if (Sleft>0){
    	Toast.makeText(this,Mleft+" "+getResources().getString(R.string.Minut)+" "+Sleft+" "+getResources().getString(R.string.Seconds)+" "+getResources().getString(R.string.Left), Toast.LENGTH_LONG).show();
    	String str = data.getStringExtra("Value");
    	AlarmStart(time, str, vibr, sound);
    	}
    	else Toast.makeText(this, R.string.WrongTime, Toast.LENGTH_LONG).show();
    	}
    	else setNotes();
    }
    
    
        public void AlarmStart(long time, String str, boolean vibr, boolean sound) {
        	 Intent intent = new Intent(MainNote.this, AlarmReceiver.class);
        	 intent.putExtra("alarm_message", str);
        	 intent.putExtra("Vibration", vibr);
        	 intent.putExtra("Sound", sound);
        	 intent.putExtra("time_in_millis", time);
        	 final int ID = (int) (Math.random()*10000); 
        	 WriteInfoInFile(time, str, vibr, sound);
        	 PendingIntent sender = PendingIntent.getBroadcast(MainNote.this, ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        	 AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        	 am.set(AlarmManager.RTC_WAKEUP, time, sender);        }
        
        
        private void WriteInfoInFile(long time, String str, boolean vibr, boolean sound)
        {
        	try {
        		OutputStream out = openFileOutput("AlarmsFromDrawNote.txt", Context.MODE_APPEND);
        		Log.e("###########", "$$$$$$$"+this.getFilesDir().getAbsolutePath()+"$$$$$$$$$$$$$$$$$$$$$");
        		BufferedWriter lout = new BufferedWriter(new OutputStreamWriter(out));
        		if (!str.equals("")) lout.write(time+"|"+str+"|"+vibr+"|"+sound);
        		else lout.write(time+"|"+"null"+"|"+vibr+"|"+sound);
        		lout.write("\n");
        		lout.close();
        	}
        	catch (IOException e) {
        		Log.e("AppService", "Exception appending to log file", e);
        	}
        }
        

        @Override
        protected void onResume()
        {
        	super.onResume();
        	mDB.openDB();
        	setNotes();
        }
         @Override
        protected void onSaveInstanceState(Bundle outState) {
           mDB.closeDB();
        }
        @Override
        protected void onPause() {
            super.onPause();
            mDB.closeDB();
        }  

        
        /* private int getColombNumber (String name, Cursor c)
        {
        	return c.getColumnIndex(name);
        }
        public ArrayList<NoteClass> getImages() {
            ArrayList<NoteClass> ImageList = new ArrayList<NoteClass>();
			//iv.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length))

            Cursor c = null;
			c = MainNote.mDB.getHandle().rawQuery("select * from note", null);
            c.moveToFirst();

            for (int i = 0; i < c.getCount(); i++)
            {
            	ImageList.add(new NoteClass(BitmapFactory.decodeByteArray(c.getBlob(getColombNumber(NotesDbAdapt.KEY_IMG,c)), 0, c.getBlob(getColombNumber(NotesDbAdapt.KEY_IMG,c)).length),
            								c.getString(getColombNumber(NotesDbAdapt.KEY_NAME,c)),
            								c.getString(getColombNumber(NotesDbAdapt.KEY_DATE,c)),
            								c.getString(getColombNumber(NotesDbAdapt.KEY_TIME,c)),
            								c.getString(getColombNumber(NotesDbAdapt.KEY_TIME_IN_MILL,c)),
            								c.getInt(getColombNumber(NotesDbAdapt.KEY_ID,c))));
                c.moveToNext();
            }

            return ImageList;
        }*/
   
}