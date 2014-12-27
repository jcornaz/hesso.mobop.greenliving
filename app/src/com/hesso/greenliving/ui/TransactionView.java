package com.hesso.greenliving.ui;

import java.text.DecimalFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.BudgetEntry;
import com.hesso.greenliving.model.Transaction;

public class TransactionView extends LinearLayout {

    private static final DecimalFormat DEC_FORMAT = new DecimalFormat( "#0.00" );

    public static TransactionView inflate( ViewGroup parent ) {
	return (TransactionView) LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_transaction, parent, false );
    }

    private Transaction transaction;

    private TextView from;
    private TextView to;
    private TextView amount;

    private boolean initialized = false;

    public TransactionView( Context context, AttributeSet attrs, int defStyle ) {
	super( context, attrs, defStyle );
    }

    public TransactionView( Context context, AttributeSet attrs ) {
	super( context, attrs );
    }

    public TransactionView( Context context ) {
	super( context );
    }

    private void init() {
	this.from = (TextView) this.findViewById( R.id.budgetFrom );
	this.to = (TextView) this.findViewById( R.id.budgetTo );
	this.amount = (TextView) this.findViewById( R.id.amount );
	this.initialized = true;
    }

    public void setItem( Transaction item ) {
	if( !this.initialized )
	    this.init();

	this.transaction = item;
	BudgetEntry fromEntry = this.transaction.getSourceEntry();
	if( fromEntry == null ) {
	    this.from.setText( this.getContext().getString( R.string.refill ) );
	    ;
	} else {
	    this.from.setText( fromEntry.getName() );
	}

	this.amount.setText( DEC_FORMAT.format( this.transaction.getAmount() ) );

	if( item.isTransfert() ) {
	    this.to.setVisibility( View.VISIBLE );
	    this.to.setText( item.getDestinationEntry().getName() );
	} else {
	    this.to.setVisibility( View.INVISIBLE );
	}
    }

    public Transaction getItem() {
	return this.transaction;
    }
}
