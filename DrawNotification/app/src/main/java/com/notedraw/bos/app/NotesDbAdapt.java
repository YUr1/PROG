
package com.notedraw.bos.app;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.provider.BaseColumns;

public class NotesDbAdapt {
    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_NAME = "notes";
    public static final String KEY_IMG = "image";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_TIME_IN_MILL = "timemiles";
   
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
   
    private static final String DATABASE_NAME = "NOTESDB";
    private static final int DATABASE_VERSION = 1;
   
    public static final String NOTES_TABLE = "note";

    private static final String CREATE_NOTES_TABLE = "create table "+NOTES_TABLE+" ("
    +KEY_ID+" integer primary key autoincrement, "
    +KEY_IMG+" blob not null, "
    +KEY_DATE+" text not null, "
    +KEY_TIME+" text not null, "
    +KEY_TIME_IN_MILL+" text not null, "
    +KEY_NAME+" text not null);";
                                             
    private final Context mCtx;
    private boolean opened = false;
    

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
   
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_NOTES_TABLE);
        }
   
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+NOTES_TABLE);
            onCreate(db);
        }
    }
   
    public void Reset() {
    	openDB();
    	mDbHelper.onUpgrade(this.mDb, 1, 1);
    	closeDB();
    }
   
    public NotesDbAdapt(Context ctx) {
        mCtx = ctx;
        mDbHelper = new DatabaseHelper(mCtx);
    }
   
    public SQLiteDatabase openDB() {
        if(!opened)
            mDb = mDbHelper.getWritableDatabase();
        opened = true;
        return mDb;
    }
    
    public SQLiteDatabase getHandle() { return openDB(); }
   
    public void closeDB() {
        if(opened)
            mDbHelper.close();
        opened = false;
    }

    public void createNoteEntry(NoteClass noteclass) {
    	openDB();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        noteclass.getBitmap().compress(Bitmap.CompressFormat.JPEG, 10, out);
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMG, out.toByteArray());            
        cv.put(KEY_NAME, noteclass.getName());
        cv.put(KEY_DATE, noteclass.getDate());
        cv.put(KEY_TIME_IN_MILL, noteclass.getTimeInMillis());
        cv.put(KEY_TIME, noteclass.getTime());
        mDb.insert(NOTES_TABLE, null, cv);
        closeDB();
    }
    
    public boolean deleteNote(long rowId) {

        return mDb.delete(NOTES_TABLE, KEY_ID + "=" + rowId, null) > 0;
    }
} 
