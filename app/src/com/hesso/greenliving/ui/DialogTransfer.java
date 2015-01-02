package com.hesso.greenliving.ui;

import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.Transaction;

public class DialogTransfer extends Activity {
	private EditText editTextAmount;
	private Button buttonDate;
	private Spinner spinnerFromAccount;
	private Spinner spinnerToAccount;
	private boolean isExistingTransfer;
	private Transaction transaction;
	private DateTime date;
	private List<Account> accounts;
	private static final int REQUESTCODE_DATE_SELECTION = 42;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		this.setContentView( R.layout.dialog_transfer );
		Intent intent = getIntent();
		isExistingTransfer = intent.getBooleanExtra("is_transfer", false);
		if(isExistingTransfer)
			transaction = Budget.getInstance().getTransactionById(intent.getLongExtra("transaction_id", 0));
		date = DateTime.now();
		accounts = new LinkedList<Account>( Budget.getInstance().getAccounts() );
		editTextAmount = (EditText) this.findViewById( R.id.dialog_transfer_edittext_amount );
		buttonDate = (Button) this.findViewById( R.id.dialog_transfer_button_date );
		spinnerFromAccount = (Spinner) this.findViewById( R.id.dialog_transfer_spinner_account_from );
		spinnerToAccount = (Spinner) this.findViewById( R.id.dialog_transfer_spinner_account_to );
		ArrayAdapter<Account> adapter = new ArrayAdapter<Account>( this, android.R.layout.simple_spinner_dropdown_item, accounts );
		spinnerFromAccount.setAdapter( adapter );
		spinnerToAccount.setAdapter( adapter );


		if(isExistingTransfer && (transaction != null)) {
			editTextAmount.setText(String.valueOf(transaction.getAmount()));
			date = transaction.getDate();
			spinnerFromAccount.setSelection(accounts.indexOf(transaction.getSourceAccount()));
			spinnerToAccount.setSelection(accounts.indexOf(transaction.getDestinationAccount()));
		}
		setDateToButton();
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
		if( editTextAmount.getText().length() > 0 ) {
			Account accountFrom = (Account) spinnerFromAccount.getSelectedItem();
			Account accountTo = (Account) spinnerToAccount.getSelectedItem();
			if(isExistingTransfer && transaction != null){
				if(accountFrom != null) {
					if(accountTo != null){
						if(accountFrom != accountTo) {
							transaction.setAmount(Double.valueOf((editTextAmount.getText().toString())));
							transaction.setDate(date);
							transaction.setSourceAccount(accountFrom);
							transaction.setDestinationAccount(accountTo);
							transaction.notifyObservers();
							finish();
						} else {
							Toast.makeText(this, R.string.same_account, Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(this, R.string.no_account_to, Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(this, R.string.no_account_from, Toast.LENGTH_LONG).show();
				}
			} else {

				if( accountFrom != null ) {
					if( accountTo != null ) {
						if( accountFrom != accountTo ) {
							accountFrom.transfert( Double.valueOf( editTextAmount.getText().toString() ), accountTo );
							finish();
						} else {
							Toast.makeText( this, R.string.same_account, Toast.LENGTH_LONG ).show();
						}
					} else {
						Toast.makeText( this, R.string.no_account_to, Toast.LENGTH_LONG ).show();
					}
				} else {
					Toast.makeText( this, R.string.no_account_from, Toast.LENGTH_LONG ).show();
				}
			}
		} else {
			Toast.makeText( this, R.string.no_amount_typed, Toast.LENGTH_LONG ).show();
		}
	}

	public void onClickCancel( View v ) {
		finish();
	}

}
