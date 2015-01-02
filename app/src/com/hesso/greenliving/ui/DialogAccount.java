package com.hesso.greenliving.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;

public class DialogAccount extends Activity {
    private EditText editTextAccountName;
    private EditText editTextTargetAmount;
    private Account account;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
	super.onCreate( savedInstanceState );
	this.setContentView( R.layout.dialog_account );
	editTextAccountName = (EditText) this.findViewById( R.id.dialog_account_edittext_account_name );
	editTextTargetAmount = (EditText) this.findViewById( R.id.dialog_account_edittext_target_amount );

	Intent i = getIntent();
	if( i.getBooleanExtra( "is_update", false ) ) {
	    account = Budget.getInstance().getAccountById( i.getLongExtra( "account_id", 0 ) );
	    // account = (Account)i.getSerializableExtra("account");
	}
	if( account != null ) {
	    editTextAccountName.setText( account.getName( this ) );
	    editTextTargetAmount.setText( String.valueOf( account.getTargetAmount() ) );
	}
    }

    public void onClickOk( View v ) {
	if( editTextAccountName.getText().length() != 0 && editTextTargetAmount.length() != 0 ) {
	    String accountName = editTextAccountName.getText().toString();
	    double amount = Double.valueOf( editTextTargetAmount.getText().toString() );
	    if( account == null ) {
		Budget budget = Budget.getInstance();
		budget.createAccount( accountName, amount );
	    } else {
		account.setName( accountName );
		account.setTargetAmount( amount );
		account.notifyObservers();
	    }
	    finish();
	} else
	    Toast.makeText( this, "Please fill Account name and Target amount", Toast.LENGTH_LONG ).show();
    }

    public void onClickCancel( View v ) {
	finish();
    }
}
