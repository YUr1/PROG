
package com.notedraw.bos.app;

import java.util.Calendar;


import android.app.Dialog;
import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class FingerPaint extends GraphicsActivity
        implements ColorPickerDialog.OnColorChangedListener {    
	
	int mYear;
	int mMonth;
	int mDay;
	int mHour;
	int mMinute;
    final CharSequence[] items = {"Red", "Green", "Blue"};
    private EditText TextEd;
    private SeekBar WidthBar;
    private Button RemindButton;
	RelativeLayout mDateTimeDialogView;
	DateTimePicker mDateTimePicker;
	boolean sound = false;
	boolean vibration = false;
	private final int REMIND_BUTTON_ID = 134515;
	private String month;
	private String day;
	private String hour;
	private String minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(15);
        mPaint.setAlpha(200);
        
        RelativeLayout LL = new RelativeLayout(this);
        setContentView(LL);
        LL.addView(new MyView(this));
        
        RelativeLayout.LayoutParams params1 = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params1.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        
        
        TextEd = new EditText(this); 
        TextEd.setTextSize(16); 
        TextEd.setFocusable(true);
        TextEd.setClickable(true);
        TextEd.setHint(R.string.hint);
        LL.addView(TextEd,params1);
        
        RelativeLayout.LayoutParams params2 = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        RemindButton = new Button(this);  
        RemindButton.setTextSize(16); 
        RemindButton.setId(REMIND_BUTTON_ID);
        RemindButton.setFocusable(true);
        RemindButton.setClickable(true);
        RemindButton.setText(R.string.RemindButton);
        LL.addView(RemindButton,params2);
        
        RelativeLayout.LayoutParams params3 = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);      
        params3.addRule(RelativeLayout.ABOVE, RemindButton.getId());
        WidthBar = new SeekBar(this);
        WidthBar.setMax(70);
        WidthBar.setProgress(15);
        WidthBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser)
				mPaint.setStrokeWidth(progress);		
			}
		});
        LL.addView(WidthBar,params3);
        
        RemindButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    showDateTimeDialog();	
			}
		});

        
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }
    

    
    private Paint       mPaint;
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;
    public Bitmap  mBitmap;
    public Bitmap mBitmapPath;
    
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }
    
    

    public class MyView extends View {
       
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        
        Display display = getWindowManager().getDefaultDisplay();     
        public int getwidth() { return display.getWidth();}
        public int getheight() { return display.getHeight();}
        
        public MyView(Context c) {
            super(c);
            mBitmap = Bitmap.createBitmap(getwidth(), getheight()-50, Bitmap.Config.ARGB_4444);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.BLACK);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            mBitmapPaint.setColor(Color.rgb(255, 255, 255));
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }
        
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 1;
        
        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            mCanvas.drawPath(mPath, mPaint);
            mPath.reset();
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
    
    private static final int COLOR_MENU_ID = Menu.FIRST;
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 1;
    private static final int BLUR_MENU_ID = Menu.FIRST + 2;
    private static final int ERASE_MENU_ID = Menu.FIRST + 3;
    private static final int SRCATOP_MENU_ID = Menu.FIRST + 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');

        /****   Is this the mechanism to extend with filter effects?
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(
                              Menu.ALTERNATIVE, 0,
                              new ComponentName(this, NotesList.class),
                              null, intent, 0, null);
        *****/
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);

        switch (item.getItemId()) {
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, Color.BLACK).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                        PorterDuff.Mode.CLEAR));
                return true;
            case SRCATOP_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                    PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void AddToDb(boolean flag, long time)
    { 
    	if (flag)
    	{
        // time = System.currentTimeMillis() + 10000;
       	getIntent().putExtra("Calendar", time);
       	getIntent().putExtra("Value", TextEd.getText().toString());
      	getIntent().putExtra("Sound", sound);
        getIntent().putExtra("Vibration", vibration);
       	setResult(RESULT_CANCELED, getIntent());

       	
       	if (mMonth<10) {
       		 month = "0"+ Integer.toString(mMonth);
       	}
       	else month = Integer.toString(mMonth);
    	if (mDay<10) {
      		 day = "0"+ Integer.toString(mDay);
      	}
      	else day = Integer.toString(mDay);
    	if (mHour<10) {
     		 hour = "0"+ Integer.toString(mHour);
     	}
     	else hour = Integer.toString(mHour);
    	if (mMinute<10) {
    		 minute = "0"+ Integer.toString(mMinute);
    	}
    	else minute = Integer.toString(mMinute);

    	MainNote.mDB.createNoteEntry(new NoteClass(mBitmap, TextEd.getText().toString(), mYear+"-"+month+
    			"-"+day, hour+":"+minute, Long.toString(time)));
    	}
    	else MainNote.mDB.createNoteEntry(new NoteClass(mBitmap, TextEd.getText().toString(), "0", "0", "0"));
    	
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			setResult(RESULT_OK, getIntent());
			AddToDb(false,0);
			finish();
			return true;
		}
			return super.onKeyDown(keyCode, event);
		}

     private void showDateTimeDialog() {
    	// Створити dialog
 		final Dialog mDateTimeDialog = new Dialog(this);
 		// Вибрати layout
 		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
 		// вибрати widget
 		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
 		// використання системи часу 24 години
 		final String timeS = android.provider.Settings.System.getString(getContentResolver(), android.provider.Settings.System.TIME_12_24);
 		final boolean is24h = !(timeS == null || timeS.equals("12"));
        		
 		((CheckBox) mDateTimeDialogView.findViewById(R.id.Sound)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sound = ((CheckBox) v).isChecked();			
			}
		});
		((CheckBox) mDateTimeDialogView.findViewById(R.id.Vibration)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				vibration = ((CheckBox) v).isChecked();			
			}
		});
        		// Оновлення демо TextViews , коли кнопка " ОК " натиснута
        		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

        			public void onClick(View v) 
        			{
        				// TODO Auto-generated method stub
        				mYear = mDateTimePicker.get(Calendar.YEAR);
        				mMonth = (mDateTimePicker.get(Calendar.MONTH)+1);
        			    mDay = mDateTimePicker.get(Calendar.DAY_OF_MONTH);
        				if (mDateTimePicker.is24HourView()) {
        					mHour = mDateTimePicker.get(Calendar.HOUR_OF_DAY);
        					mMinute = mDateTimePicker.get(Calendar.MINUTE);
            				AddToDb(true, mDateTimePicker.getDateTimeMillis());
        				} else {
        					mHour = mDateTimePicker.get(Calendar.HOUR_OF_DAY);
        					mMinute = mDateTimePicker.get(Calendar.MINUTE);
        					AddToDb(true, mDateTimePicker.getDateTimeMillis());
        				}
        				mDateTimeDialog.dismiss();
        				finish();
        			}
        		});

        		// Скасувати діалог, коли кнопка "Скасувати" натиснута
        		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

        			public void onClick(View v) {
        				// TODO Auto-generated method stub
        				mDateTimeDialog.cancel();
        			}
        		});

        		// Скидання дати і часу , коли збирачі "Скидання" Кнопка натиснута
        		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {

        			public void onClick(View v) {
        				// TODO Auto-generated method stub
        				mDateTimePicker.reset();
        			}
        		});
        		
        		// Налаштування TimePicker
        		mDateTimePicker.setIs24HourView(is24h);
        		// Без назви у діалоговому вікні
        		mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        		// Встановити діалоговий вид контенту
        		mDateTimeDialog.setContentView(mDateTimeDialogView);
        		// Відображення діалогового вікна
        		mDateTimeDialog.show();
        	}
}
