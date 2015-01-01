package com.hesso.greenliving.ui;

import java.util.Observable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;

public class AccountView extends LinearLayout implements IEntityView<Account>, OnClickListener, OnLongClickListener {

    private static final int PROGRESSBAR_SIZE = 1000;

    public static AccountView inflate( ViewGroup parent ) {
	return (AccountView) LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_account, parent, false );
    }

    private Account budgetEntry;

    private TextView entryNameView;
    private TextView targetAmountView;
    private TextView currentAmountView;
    private ProgressBar progressBarView;

    private MainActivity mainActivity;

    @Override
    protected void onFinishInflate() {
	super.onFinishInflate();
	this.entryNameView = (TextView) this.findViewById( R.id.entryName );
	this.targetAmountView = (TextView) this.findViewById( R.id.targetAmount );
	this.currentAmountView = (TextView) this.findViewById( R.id.currentAmount );
	this.progressBarView = (ProgressBar) this.findViewById( R.id.progressBar );
	this.progressBarView.setMax( PROGRESSBAR_SIZE );
	this.setOnClickListener( this );
	this.setOnLongClickListener(this);
    }

    public AccountView( Context context, AttributeSet attrs, int defStyle ) {
	super( context, attrs, defStyle );
    }

    public AccountView( Context context, AttributeSet attrs ) {
	super( context, attrs );
    }

    public AccountView( Context context ) {
	super( context );
    }

    @Override
    public void setModel( Account entry ) {
	if( this.budgetEntry != entry ) {
	    this.budgetEntry = entry;
	    if( this.budgetEntry != null ) {
		this.budgetEntry.deleteObserver( this );
	    }
	    this.budgetEntry.addObserver( this );
	    this.update( this.budgetEntry, this );
	}
    }

    @Override
    public void update( Observable observable, Object data ) {
	double targetAmount = this.budgetEntry.getTargetAmount();
	double currentAmount = this.budgetEntry.getCurrentAmount();

	this.entryNameView.setText( this.budgetEntry.getName() );
	this.currentAmountView.setText( String.valueOf( currentAmount ) );
	this.targetAmountView.setText( String.valueOf( targetAmount ) );

	if( targetAmount != 0 ) {
	    this.progressBarView.setVisibility( View.VISIBLE );
	    this.progressBarView.setProgress( (int) Math.round( PROGRESSBAR_SIZE * Math.max( 0, Math.min( currentAmount, targetAmount ) ) / targetAmount ) );
	} else {
	    this.progressBarView.setVisibility( View.INVISIBLE );
	}
    }

    @Override
    public void setMainActivity( MainActivity mainActivity ) {
	this.mainActivity = mainActivity;
    }

    @Override
    public void onClick( View v ) {
	this.mainActivity.openTransactions( this.budgetEntry );
    }

	@Override
	public boolean onLongClick(View v) {
		return false;
	}
}
