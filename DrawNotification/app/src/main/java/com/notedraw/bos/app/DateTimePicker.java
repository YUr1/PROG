/**
 * Copyright 2010 Lukasz Szmit <devmail@szmit.eu>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/
package com.notedraw.bos.app;

import java.util.Calendar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;


public class DateTimePicker extends RelativeLayout implements View.OnClickListener, OnDateChangedListener, OnTimeChangedListener {

	// DatePicker посилання
	private DatePicker		datePicker;
	// TimePicker посилання
	private TimePicker		timePicker;
	// ViewSwitcher посилання
	private ViewSwitcher	viewSwitcher;
	// Calendar посилання
	private Calendar		mCalendar;

	// Constructor початок
	public DateTimePicker(Context context) {
		this(context, null);
	}

	public DateTimePicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// Отримати рорзмітку LayoutInflater
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Inflate myself
		inflater.inflate(R.layout.datetimepicker, this, true);

		// перегляд значень дати і часу
		final LinearLayout datePickerView = (LinearLayout) inflater.inflate(R.layout.datepicker, null);
		final LinearLayout timePickerView = (LinearLayout) inflater.inflate(R.layout.timepicker, null);
		
		// Взяти екземпляр календару
		mCalendar = Calendar.getInstance();
		// Візяти ViewSwitcher і покласти в нього значення дати і часу
		viewSwitcher = (ViewSwitcher) this.findViewById(R.id.DateTimePickerVS);

		// Ініціалізація дати
		datePicker = (DatePicker) datePickerView.findViewById(R.id.DatePicker);
		datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);

		// Ініціалізація часу
		timePicker = (TimePicker) timePickerView.findViewById(R.id.TimePicker);
		timePicker.setOnTimeChangedListener(this);

		// Handle кнопка
		((Button) findViewById(R.id.SwitchToTime)).setOnClickListener(this); // shows the time picker
		((Button) findViewById(R.id.SwitchToDate)).setOnClickListener(this); // shows the date picker

		// показати ViewSwitcher
		viewSwitcher.addView(datePickerView, 0);
		viewSwitcher.addView(timePickerView, 1);
	}
	// закінчення Constructor

	// Викликається кожен раз , коли користувач змінює значення DatePicker
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// Оновлення екземпляр внутрішнього календар
		mCalendar.set(year, monthOfYear, dayOfMonth, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
	}

	// Викликається кожен раз , коли користувач змінює значення TimePicker
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// Оновлення екземпляр внутрішнього календар
		mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
	}

	// Handle кнопка
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.SwitchToDate:
				v.setEnabled(false);
				findViewById(R.id.SwitchToTime).setEnabled(true);
				viewSwitcher.showPrevious();
				break;

			case R.id.SwitchToTime:
				v.setEnabled(false);
				findViewById(R.id.SwitchToDate).setEnabled(true);
				viewSwitcher.showNext();
				break;
		}
	}

	// зображення внутрішнього календара
	public int get(final int field) {
		return mCalendar.get(field);
	}

	// Скидання DatePicker , TimePicker та примірник внутрішнього календаря
	public void reset() {
		final Calendar c = Calendar.getInstance();
		updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		updateTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
	}

	// Cзображення внутрішнього календара
	public long getDateTimeMillis() {
		return mCalendar.getTimeInMillis();
	}

	// дизайн TimePicker
	public void setIs24HourView(boolean is24HourView) {
		timePicker.setIs24HourView(is24HourView);
	}
	
	// дизайн TimePicker
	public boolean is24HourView() {
		return timePicker.is24HourView();
	}

    // дизайн DatePicker
	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		datePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	// дизайн TimePicker
	public void updateTime(int currentHour, int currentMinute) {
		timePicker.setCurrentHour(currentHour);
		timePicker.setCurrentMinute(currentMinute);
	}
	
}