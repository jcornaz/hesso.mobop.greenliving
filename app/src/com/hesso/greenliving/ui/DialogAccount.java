package com.hesso.greenliving.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Budget;

public class DialogAccount extends Activity {
	private EditText editTextAccountName;
	private EditText editTextTargetAmount;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_account);
		editTextAccountName = (EditText) this.findViewById(R.id.dialog_account_edittext_account_name);
		editTextTargetAmount = (EditText) this.findViewById(R.id.dialog_account_edittext_target_amount);
	}
	
	public void onClickOk(View v) {
		if(editTextAccountName.getText().length() != 0 && editTextTargetAmount.length() != 0)
		{
			String accountName = editTextAccountName.getText().toString();
			double amount = Double.valueOf(editTextTargetAmount.getText().toString());
			Budget budget = Budget.getInstance();
			budget.createAccount(accountName, amount);
			finish();
		} else 
			Toast.makeText(this, "Please fill Account name and Target amount", Toast.LENGTH_LONG).show();
		
		
	}
	
	public void onClickCancel(View v) {
		finish();
	}
}
