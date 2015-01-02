package com.hesso.greenliving.ui;

import java.util.Observable;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Transaction;

public class TransactionView extends LinearLayout implements IEntityView<Transaction> {

    public static final int COLOR_FILL = Color.rgb( 0, 128, 0 );
    public static final int COLOR_EXPENSE = Color.rgb( 181, 13, 13 );
    private static final int COLOR_TRANSFER = Color.BLACK;

    public static TransactionView inflate( ViewGroup parent ) {
	return (TransactionView) LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_transaction, parent, false );
    }

    private Transaction transaction;

    private TextView textViewFromAccount;
    private TextView textViewToAccount;
    private TextView textViewDate;
    private TextView textViewAmount;

    public TransactionView( Context context ) {
	super( context );
    }

    public TransactionView( Context context, AttributeSet attrs ) {
	super( context, attrs );
    }

    public TransactionView( Context context, AttributeSet attrs, int defStyle ) {
	super( context, attrs, defStyle );
    }

    @Override
    protected void onFinishInflate() {
	super.onFinishInflate();
	this.textViewFromAccount = (TextView) this.findViewById( R.id.budgetFrom );
	this.textViewToAccount = (TextView) this.findViewById( R.id.budgetTo );
	this.textViewDate = (TextView) this.findViewById( R.id.date );
	this.textViewAmount = (TextView) this.findViewById( R.id.amount );
    }

    @Override
    public void setModel( Transaction item ) {
	if( this.transaction != item ) {
	    if( this.transaction != null ) {
		this.transaction.deleteObserver( this );
	    }
	    this.transaction = item;
	    this.transaction.addObserver( this );
	    this.update( this.transaction, this );
	}
    }

    @Override
    public void update( Observable observable, Object data ) {
	if( this.transaction.hasSource() ) {
	    this.textViewFromAccount.setText( AccountDisplayer.toString( this.getContext(), this.transaction.getSourceAccount() ) );
	} else {
	    this.textViewFromAccount.setText( this.getContext().getString( R.string.credit ) );
	}

	if( this.transaction.hasDestination() ) {
	    this.textViewToAccount.setText( AccountDisplayer.toString( this.getContext(), this.transaction.getDestinationAccount() ) );
	} else {
	    this.textViewToAccount.setText( this.getContext().getString( R.string.expense ) );
	}

	int color;
	switch( this.transaction.getType() ) {

	default:
	case TRANSFER:
	    color = COLOR_TRANSFER;
	    this.textViewAmount.setText( MainActivity.DEC_FORMAT.format( this.transaction.getAmount() ) );
	    break;
	case EXPENSE:
	    color = COLOR_EXPENSE;
	    this.textViewAmount.setText( "-" + MainActivity.DEC_FORMAT.format( this.transaction.getAmount() ) );
	    break;
	case CREDIT:
	    color = COLOR_FILL;
	    this.textViewAmount.setText( "+" + MainActivity.DEC_FORMAT.format( this.transaction.getAmount() ) );
	    break;
	}

	this.textViewAmount.setTextColor( color );
	this.textViewDate.setText( this.transaction.getDate().toString( "dd/MM/YYYY", this.getContext().getResources().getConfiguration().locale ) );
    }
}
