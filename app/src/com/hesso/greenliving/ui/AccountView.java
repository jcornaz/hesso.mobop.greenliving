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

    private Account account;

    private TextView entryNameView;
    private TextView targetAmountView;
    private TextView currentAmountView;
    private ProgressBar progressBarView;

    @Override
    protected void onFinishInflate() {
	super.onFinishInflate();
	this.entryNameView = (TextView) this.findViewById( R.id.entryName );
	this.targetAmountView = (TextView) this.findViewById( R.id.targetAmount );
	this.currentAmountView = (TextView) this.findViewById( R.id.currentAmount );
	this.progressBarView = (ProgressBar) this.findViewById( R.id.progressBar );
	this.progressBarView.setMax( PROGRESSBAR_SIZE );
	this.setOnClickListener( this );
	this.setOnLongClickListener( this );
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
	if( this.account != entry ) {
	    this.account = entry;
	    if( this.account != null ) {
		this.account.deleteObserver( this );
	    }
	    this.account.addObserver( this );
	    this.update( this.account, this );
	}
    }

    @Override
    public void update( Observable observable, Object data ) {
	double targetAmount = this.account.getTargetAmount();
	double currentAmount = this.account.getCurrentAmount();

	this.entryNameView.setText( this.account.getName( this.getContext() ) );
	this.currentAmountView.setText( MainActivity.DEC_FORMAT.format( currentAmount ) );
	this.targetAmountView.setText( MainActivity.DEC_FORMAT.format( targetAmount ) );

	if( targetAmount != 0 ) {
	    this.progressBarView.setVisibility( View.VISIBLE );
	    this.progressBarView.setProgress( (int) Math.round( PROGRESSBAR_SIZE * Math.max( 0, Math.min( currentAmount, targetAmount ) ) / targetAmount ) );
	} else {
	    this.progressBarView.setVisibility( View.INVISIBLE );
	}
    }

    @Override
    public void onClick( View v ) {
	MainActivity.getInstance().openTransactions( this.account );
    }

    @Override
    public boolean onLongClick( View v ) {
	return false;
    }
}
