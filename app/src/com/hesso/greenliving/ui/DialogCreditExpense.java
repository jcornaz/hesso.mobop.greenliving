package com.hesso.greenliving.ui;

import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;

public class DialogCreditExpense extends Activity {
	private EditText editTextAmount;
	private Spinner spinnerAccount;
	private Switch switchExpenseCredit;
	private Button buttonDate;
	private DateTime date;
	private List<Account> accounts;
	private static final int REQUESTCODE_DATE_SELECTION = 42;
	private static final boolean EXPENSE = false;
	private static final boolean CREDIT = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.dialog_credit_expense);
		date = new DateTime(DateTime.now());
		editTextAmount = (EditText) this.findViewById(R.id.dialog_credit_expense_edittext_amount);
		spinnerAccount = (Spinner) this.findViewById(R.id.dialog_credit_expense_spinner_account);
		switchExpenseCredit = (Switch) this.findViewById(R.id.dialog_credit_expense_switch_expense_credit);
		buttonDate = (Button) this.findViewById(R.id.dialog_credit_expense_button_date);
		setDateToButton();
		accounts = Budget.getInstance().getEntriesList();
		ArrayAdapter<Account> adapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_dropdown_item, accounts);
		spinnerAccount.setAdapter(adapter);
	}
	
	public void onClickDate(View v) {
		Intent intent = new Intent(this, DialogDateSelector.class);
		long d = date.getMillis();
		intent.putExtra("date", d);
		startActivityForResult(intent, REQUESTCODE_DATE_SELECTION);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUESTCODE_DATE_SELECTION && resultCode == RESULT_OK) {
			long d = data.getLongExtra("date", -1);
			if(d >= 0) {
				date = new DateTime(d);
				setDateToButton();
			}
		}
	}
	
	private void setDateToButton() {
		String d = date.toString("MM/dd/YYYY", Locale.getDefault());
		buttonDate.setText(d);
	}
	
	public void onClickOk(View v) {
		//TODO : check date
		if(editTextAmount.getText().length() > 0) {
			Account account = (Account) spinnerAccount.getSelectedItem();
			if(account != null) {
				if(switchExpenseCredit.isChecked() == EXPENSE) {
					account.expense(Double.valueOf(editTextAmount.getText().toString()));
				} else if (switchExpenseCredit.isChecked() == CREDIT) {
					account.fill(Double.valueOf(editTextAmount.getText().toString()));
				}
				finish();
			} else {
				Toast.makeText(this, "Please select an account", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Please type an amount", Toast.LENGTH_LONG).show();
		}
	}
	
	public void onClickCancel(View v) {
		finish();
	}
}