package com.hesso.greenliving.ui;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.hesso.greenliving.R;

public class DialogDateSelector extends Activity {
	private DateTime date;
	private DatePicker datePicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_date_selector);
		datePicker = (DatePicker) this.findViewById(R.id.dialog_date_selector_picker);
		long d = this.getIntent().getLongExtra("date", -1);
		if(d != -1) 
			date = new DateTime(d);
		else
			date = new DateTime(DateTime.now());
		
		updateDatePicker();
	}
	
	private void updateDatePicker() {
		int year = date.getYear();
		int month = date.getMonthOfYear() -1;
		int day = date.getDayOfMonth();
		
		datePicker.updateDate(year, month, day);
	}
	
	public void onClickOk(View v) {
		int year = datePicker.getYear();
		int month = datePicker.getMonth() + 1;
		int day = datePicker.getDayOfMonth();
		date = new DateTime(year, month, day, 0 , 0);
		Intent intent = new Intent();
		intent.putExtra("date", date.getMillis());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void onClickCancel(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}

}
