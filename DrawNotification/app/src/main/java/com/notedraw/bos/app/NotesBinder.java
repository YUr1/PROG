package com.notedraw.bos.app;

import android.database.Cursor;
	import android.graphics.BitmapFactory;
	import android.view.View;
	import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

	public class NotesBinder implements SimpleCursorAdapter.ViewBinder {
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			
			if(view instanceof ImageView) {
				ImageView iv = (ImageView) view;
				byte[] img = cursor.getBlob(columnIndex);
				iv.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
				return true;
			}
			
			if(view instanceof TextView) {
				TextView iv = (TextView) view;
				String str = cursor.getString(columnIndex);
				if (str.equals(""))
						iv.setText(R.string.Nodiscr);
				else if (str.equals("0"))
						iv.setText(R.string.Noreminder);
				else iv.setText(str);
				return true;
			}

			return false;
		}
	}
