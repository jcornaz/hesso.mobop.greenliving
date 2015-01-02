package com.hesso.greenliving.ui;

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
    private Budget budget;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
	super.onCreate( savedInstanceState );
	this.setContentView( R.layout.dialog_budget );
	budget = Budget.getInstance();
	editTextIncome = (EditText) this.findViewById( R.id.dialog_budget_edittext_income );
	editTextIncome.setText( MainActivity.DEC_FORMAT.format( budget.getTarget() ) );

	numberPickerDay = (NumberPicker) this.findViewById( R.id.dialog_budget_numberpicker_day_of_month );
	numberPickerDay.setMinValue( 1 );
	numberPickerDay.setMaxValue( 31 );
	numberPickerDay.setValue( budget.getDayOfMonth() );
    }

    public void onClickCancel( View v ) {
	finish();
    }

    public void onClickOk( View v ) {
	double target;
	int dayOfMonth;
	if( editTextIncome.getText().length() != 0 ) {
	    target = Double.parseDouble( editTextIncome.getText().toString() );
	    dayOfMonth = numberPickerDay.getValue();
	    budget.setDayOfMonth( dayOfMonth );
	    budget.setTarget( target );
	    finish();
	} else {
	    Toast.makeText( this, "Please type an income", Toast.LENGTH_LONG ).show();
	}

    }

}
