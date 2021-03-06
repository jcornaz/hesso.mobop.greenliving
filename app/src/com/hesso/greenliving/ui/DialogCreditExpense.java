package com.hesso.greenliving.ui;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
import com.hesso.greenliving.model.Transaction;
import com.hesso.greenliving.model.TransctionType;

public class DialogCreditExpense extends Activity implements OnCheckedChangeListener {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern( "dd/MM/YYYY" );

    private EditText editTextAmount;
    private Spinner spinnerAccount;
    private Switch switchExpenseCredit;
    private Button buttonDate;
    private TextView textViewFromTo;
    private TextView textViewAccount;

    private boolean hasAccountPreselected;
    private boolean isLinkedToAccount;
    private boolean isExistingTransaction;

    private Account account;
    private Transaction transaction;
    private DateTime date;

    private Interval day;

    private static final int REQUESTCODE_DATE_SELECTION = 42;
    private static final boolean EXPENSE = false;
    private static final boolean CREDIT = true;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
	super.onCreate( savedInstanceState );
	this.setContentView( R.layout.dialog_credit_expense );
	Intent intent = getIntent();
	hasAccountPreselected = intent.getBooleanExtra( "has_preselected_account", false );
	isLinkedToAccount = intent.getBooleanExtra( "linked_to_account", false );
	isExistingTransaction = intent.getBooleanExtra( "is_credit_expense", false );

	if( hasAccountPreselected || isLinkedToAccount )
	    account = Budget.getInstance().getAccountById( intent.getLongExtra( "account_id", 0 ) );
	if( isExistingTransaction )
	    transaction = Budget.getInstance().getTransactionById( intent.getLongExtra( "transaction_id", 0 ) );

	date = DateTime.now();
	this.day = new Interval( this.date.withMillisOfDay( 0 ), this.date.withMillisOfDay( 0 ).plusDays( 1 ) );
	editTextAmount = (EditText) this.findViewById( R.id.dialog_credit_expense_edittext_amount );
	spinnerAccount = (Spinner) this.findViewById( R.id.dialog_credit_expense_spinner_account );
	switchExpenseCredit = (Switch) this.findViewById( R.id.dialog_credit_expense_switch_expense_credit );
	buttonDate = (Button) this.findViewById( R.id.dialog_credit_expense_button_date );
	textViewFromTo = (TextView) this.findViewById( R.id.dialog_credit_expense_textview_from );
	textViewAccount = (TextView) this.findViewById( R.id.dialog_credit_expense_textview_account );
	switchExpenseCredit.setOnCheckedChangeListener( this );

	setDateToButton();
	if( isLinkedToAccount && account != null ) {
	    spinnerAccount.setVisibility( View.GONE );
	    textViewAccount.setText( AccountDisplayer.toString( this, this.account ) );
	    textViewAccount.setTypeface( null, Typeface.BOLD );
	} else {
	    List<AccountDisplayer> accounts = AccountDisplayer.convert( this, Budget.getInstance().getAccounts() );
	    ArrayAdapter<AccountDisplayer> adapter = new ArrayAdapter<AccountDisplayer>( this, android.R.layout.simple_spinner_dropdown_item, accounts );
	    spinnerAccount.setAdapter( adapter );
	    if( hasAccountPreselected && account != null ) {
		spinnerAccount.setSelection( accounts.indexOf( account ) );
	    } else if( isExistingTransaction && transaction != null ) {
		date = transaction.getDate();
		editTextAmount.setText( String.valueOf( transaction.getAmount() ) );
		if( transaction.getType() == TransctionType.CREDIT ) {
		    switchExpenseCredit.setChecked( true );
		    spinnerAccount.setSelection( accounts.indexOf( transaction.getDestinationAccount() ) );
		} else {
		    switchExpenseCredit.setChecked( false );
		    spinnerAccount.setSelection( accounts.indexOf( transaction.getSourceAccount() ) );
		}
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
	String d = date.toString( DATE_FORMAT );
	buttonDate.setText( d );
    }

    public void onClickOk( View v ) {
	DateTime dte = DateTime.parse( this.buttonDate.getText().toString(), DATE_FORMAT );
	if( !this.day.contains( dte ) )
	{
	    this.date = dte;
	}
	if( editTextAmount.getText().length() > 0 ) {
	    if( isLinkedToAccount && account != null ) {
		if( switchExpenseCredit.isChecked() == EXPENSE ) {
		    account.expense( Double.valueOf( editTextAmount.getText().toString() ), this.date );
		} else if( switchExpenseCredit.isChecked() == CREDIT ) {
		    account.fill( Double.valueOf( editTextAmount.getText().toString() ), this.date );
		}
		finish();
	    } else if( isExistingTransaction && transaction != null ) {
		transaction.setAmount( Double.valueOf( editTextAmount.getText().toString() ) );
		transaction.setDate( date );
		if( switchExpenseCredit.isChecked() == EXPENSE ) {
		    transaction.setSourceAccount( (Account) spinnerAccount.getSelectedItem() );
		    transaction.setDestinationAccount( null );
		} else {
		    transaction.setDestinationAccount( (Account) spinnerAccount.getSelectedItem() );
		    transaction.setSourceAccount( null );
		}
		transaction.notifyObservers();
		finish();
	    } else {
		account = (Account) spinnerAccount.getSelectedItem();
		if( account != null ) {
		    if( switchExpenseCredit.isChecked() == EXPENSE ) {
			account.expense( Double.valueOf( editTextAmount.getText().toString() ), this.date );
		    } else if( switchExpenseCredit.isChecked() == CREDIT ) {
			account.fill( Double.valueOf( editTextAmount.getText().toString() ), this.date );
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
    public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
	if( isChecked ) {
	    this.textViewFromTo.setText( R.string.to );
	} else {
	    this.textViewFromTo.setText( R.string.from );
	}
    }
}
