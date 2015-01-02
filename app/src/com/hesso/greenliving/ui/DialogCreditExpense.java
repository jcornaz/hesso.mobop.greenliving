package com.hesso.greenliving.ui;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;

public class DialogCreditExpense extends Activity implements OnCheckedChangeListener {
	private EditText editTextAmount;
	private Spinner spinnerAccount;
	private Switch switchExpenseCredit;
	private Button buttonDate;
	private TextView textViewFromTo;
	private TextView textViewAccount;
	
	private boolean hasAccountPreselected;
	private boolean isLinkedToAccount;
	
	private Account account;
	private DateTime date;

	private static final int REQUESTCODE_DATE_SELECTION = 42;
	private static final boolean EXPENSE = false;
	private static final boolean CREDIT = true;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.dialog_credit_expense );
		Intent intent = getIntent();
		hasAccountPreselected = intent.getBooleanExtra("has_preselected_account", false);
		isLinkedToAccount = intent.getBooleanExtra("linked_to_account", false);
		if(hasAccountPreselected || isLinkedToAccount)
			account = Budget.getInstance().getAccountById(intent.getLongExtra("account_id", 0));
		
		date = DateTime.now();
		editTextAmount = (EditText) this.findViewById( R.id.dialog_credit_expense_edittext_amount );
		spinnerAccount = (Spinner) this.findViewById( R.id.dialog_credit_expense_spinner_account );
		switchExpenseCredit = (Switch) this.findViewById( R.id.dialog_credit_expense_switch_expense_credit );
		buttonDate = (Button) this.findViewById( R.id.dialog_credit_expense_button_date );
		textViewFromTo = (TextView) this.findViewById(R.id.dialog_credit_expense_textview_from);
		textViewAccount = (TextView) this.findViewById(R.id.dialog_credit_expense_textview_account);
		switchExpenseCredit.setOnCheckedChangeListener(this);
		
		setDateToButton();
		if(isLinkedToAccount && account != null) {
			spinnerAccount.setVisibility(View.GONE);
			textViewAccount.setText(account.getName());
			textViewAccount.setTypeface(null, Typeface.BOLD);
		} else {
			ArrayList<Account> accounts = new ArrayList<Account>(Budget.getInstance().getAccounts());
			ArrayAdapter<Account> adapter = new ArrayAdapter<Account>(this, android.R.layout.simple_spinner_dropdown_item, accounts);
			spinnerAccount.setAdapter(adapter);
			if(hasAccountPreselected && account != null) {
				spinnerAccount.setSelection(accounts.indexOf(account));
			}
		}
	}

	public void onClickDate( View v ) {
		Intent intent = new Intent( this, DialogDateSelector.class );
		long d = date.getMillis();
		intent.putExtra( "date", d );
		startActivityForResult( intent, REQUESTCODE_DATE_SELECTION );
	}

	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		if( requestCode == REQUESTCODE_DATE_SELECTION && resultCode == RESULT_OK ) {
			long d = data.getLongExtra( "date", -1 );
			if( d >= 0 ) {
				date = new DateTime( d );
				setDateToButton();
			}
		}
	}

	private void setDateToButton() {
		String d = date.toString( "dd/MM/YYYY", this.getResources().getConfiguration().locale );
		buttonDate.setText( d );
	}

	public void onClickOk( View v ) {
		// TODO : check date
		if( editTextAmount.getText().length() > 0 ) {
			if(isLinkedToAccount && account != null) {
				if( switchExpenseCredit.isChecked() == EXPENSE ) {
					account.expense( Double.valueOf( editTextAmount.getText().toString() ) );
				} else if( switchExpenseCredit.isChecked() == CREDIT ) {
					account.fill( Double.valueOf( editTextAmount.getText().toString() ) );
				}
				finish();
			} else {
				account =(Account) spinnerAccount.getSelectedItem();
				if(account != null) {
					if( switchExpenseCredit.isChecked() == EXPENSE ) {
						account.expense( Double.valueOf( editTextAmount.getText().toString() ) );
					} else if( switchExpenseCredit.isChecked() == CREDIT ) {
						account.fill( Double.valueOf( editTextAmount.getText().toString() ) );
					}
					finish();
				} else {
					Toast.makeText( this, R.string.no_account_selected, Toast.LENGTH_LONG ).show();
				}
			}
		} else {
			Toast.makeText( this, R.string.no_amount_typed, Toast.LENGTH_LONG ).show();
		}
	}

	public void onClickCancel( View v ) {
		finish();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked) {
			this.textViewFromTo.setText(R.string.to);
		} else {
			this.textViewFromTo.setText(R.string.from);
		}
	}
}
