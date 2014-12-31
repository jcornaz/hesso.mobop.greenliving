package com.hesso.greenliving.ui;

import java.util.Observable;

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

public class TransactionView extends LinearLayout implements IEntityView<Transaction> {

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

	this.budgetToLayout = (LinearLayout) this.findViewById( R.id.budgetToLayout );
	this.from = (TextView) this.findViewById( R.id.budgetFrom );
	this.to = (TextView) this.findViewById( R.id.budgetTo );
	this.amount = (TextView) this.findViewById( R.id.amount );
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
	    this.from.setText( this.transaction.getSourceAccount().getName() );
	} else {
	    this.from.setText( this.getContext().getString( R.string.refill ) );
	}

	if( this.transaction.hasDestination() ) {
	    this.budgetToLayout.setVisibility( View.VISIBLE );
	    this.to.setText( this.transaction.getDestinationAccount().getName() );

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
	case CREDIT:
	    color = COLOR_FILL;
	    break;
	}

	this.amount.setText( MainActivity.DEC_FORMAT.format( this.transaction.getAmount() ) );
	this.amount.setTextColor( color );
    }

    @Override
    public void setMainActivity( MainActivity mainActivity ) {
    }
}
