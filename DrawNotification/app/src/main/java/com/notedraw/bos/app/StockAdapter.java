package com.notedraw.bos.app;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StockAdapter extends BaseAdapter {
		private static NotesDbAdapt ShowDb;

	    private final List<NoteClass> rows;
	    private Context ctx;

	    public StockAdapter(final Context context, final int itemResId,
	            final List<NoteClass> items) {
	        this.rows = items;
	        this.ctx = context;
	    }

	    public int getCount() {
	        return this.rows.size();
	    }

	    public Object getItem(int position) {
	        return this.rows.get(position);
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    /**
	     * Set the content for a row here
	     */
	    public View getView(int position, View convertView, ViewGroup parent) {

	        final NoteClass row = this.rows.get(position);
	        View itemView = null;

	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) parent.getContext()
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            itemView = inflater.inflate(R.layout.file_dialog_row, null);
	        } else {
	            itemView = convertView;
	        }


			TextView discrtext = (TextView) itemView.findViewById(R.id.txt);
			discrtext.setText(row.getName());
			TextView timetext = (TextView) itemView.findViewById(R.id.time);
			timetext.setText(row.getTime());
			TextView datetext = (TextView) itemView.findViewById(R.id.date);
			datetext.setText(row.getDate());
			
			ShowDb = new NotesDbAdapt(ctx);
			ShowDb.openDB();
			Cursor c = null;
			c = ShowDb.getHandle().rawQuery("select * from note where "+NotesDbAdapt.KEY_TIME_IN_MILL + "=" + row.getTimeInMillis(), null);
			c.moveToFirst();
			TextView idText = (TextView) itemView.findViewById(R.id.key_id);
			idText.setText(c.getString(c.getColumnIndex(NotesDbAdapt.KEY_TIME_IN_MILL)));
			ImageView dialogimage = (ImageView)itemView.findViewById(R.id.img);
			dialogimage.setImageBitmap(row.getBitmap());
	        
	        return itemView;

	    }

	    /**
	     * Delete a row from the list of rows
	     * @param row row to be deleted
	     */
	    public void deleteRow(NoteClass row) {
	        
	        if(this.rows.contains(row)) {
	            this.rows.remove(row);
	        }
	    }
}
