package com.hesso.greenliving.ui;

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Budget;

public class DialogBudget extends Activity {
	private EditText editTextIncome;
	private NumberPicker numberPickerDay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_budget);
		editTextIncome = (EditText) this.findViewById(R.id.dialog_budget_edittext_income);
		numberPickerDay = (NumberPicker) this.findViewById(R.id.dialog_budget_numberpicker_day_of_month);
		numberPickerDay.setValue(1);
		numberPickerDay.setMinValue(1);
		numberPickerDay.setMaxValue(31);
	}
	
	public void onClickCancel(View v) {
		finish();
	}
	
	public void onClickOk(View v) {
		BigDecimal target;
		int dayOfMonth;
		if(editTextIncome.getText().length() != 0) {
			target = new BigDecimal(editTextIncome.getText().toString());
			dayOfMonth = numberPickerDay.getValue();
			Budget budget = Budget.getInstance();
			budget.setDayOfMonth(dayOfMonth);
			budget.setTarget(target);
			//Save
			finish();
		} else {
			Toast.makeText(this, "Please type an income", Toast.LENGTH_LONG).show();
		}
		
	}
	
}
