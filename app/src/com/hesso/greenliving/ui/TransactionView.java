package com.hesso.greenliving.ui;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Transaction;

public class TransactionView extends LinearLayout {

    private static final DecimalFormat DEC_FORMAT = new DecimalFormat( "#0.00" );

    private static final int COLOR_FILL = Color.rgb( 0, 128, 0 );

    private static final int COLOR_EXPENSE = Color.rgb( 181, 13, 13 );

    private static final int COLOR_TRANSFER = Color.BLACK;

    public static TransactionView inflate( ViewGroup parent ) {
	return (TransactionView) LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_transaction, parent, false );
    }

    private Transaction transaction;

    private TextView from;
    private TextView to;
    private TextView amount;
    private LinearLayout budgetToLayout;

    private boolean initialized = false;

    public TransactionView( Context context ) {
	super( context );
    }

    public TransactionView( Context context, AttributeSet attrs ) {
	super( context, attrs );
    }

    public TransactionView( Context context, AttributeSet attrs, int defStyle ) {
	super( context, attrs, defStyle );
    }

    private void init() {
	this.budgetToLayout = (LinearLayout) this.findViewById( R.id.budgetToLayout );
	this.from = (TextView) this.findViewById( R.id.budgetFrom );
	this.to = (TextView) this.findViewById( R.id.budgetTo );
	this.amount = (TextView) this.findViewById( R.id.amount );
	this.initialized = true;
    }

    public void setItem( Transaction item ) {
	if( !this.initialized )
	    this.init();

	this.transaction = item;

	if( this.transaction.hasSource() ) {
	    this.from.setText( this.transaction.getSourceEntry().getName() );
	} else {
	    this.from.setText( this.getContext().getString( R.string.refill ) );
	}

	if( this.transaction.hasDestination() ) {
	    this.budgetToLayout.setVisibility( View.VISIBLE );
	    this.to.setText( item.getDestinationEntry().getName() );

	} else {
	    this.budgetToLayout.setVisibility( View.INVISIBLE );
	}

	int color;
	switch( this.transaction.getType() ) {
	default:
	case TRANSFER:
	    color = COLOR_TRANSFER;
	    break;
	case EXPENSE:
	    color = COLOR_EXPENSE;
	    break;
	case FILL:
	    color = COLOR_FILL;
	    break;
	}

	this.amount.setText( DEC_FORMAT.format( this.transaction.getAmount() ) );
	this.amount.setTextColor( color );
    }

    public Transaction getItem() {
	return this.transaction;
    }
}
