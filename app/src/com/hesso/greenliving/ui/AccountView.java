package com.hesso.greenliving.ui;

import java.util.Observable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
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

    private static final int COLOR_BACKGROUND = Color.rgb( 200, 200, 200 );
    private static final int COLOR_STANDARD = Color.BLACK;
    private static final int COLOR_OK = TransactionView.COLOR_FILL;
    private static final int COLOR_WARNING = Color.rgb( 230, 145, 0 );
    private static final int COLOR_OUT_OF_BUDGET = TransactionView.COLOR_EXPENSE;

    private static final int PROGRESSBAR_ROUND_SIZE = 10;

    private static final int PROGRESSBAR_SIZE = 1000;

    public static AccountView inflate( ViewGroup parent ) {
	return (AccountView) LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_account, parent, false );
    }

    private Account account;

    private TextView entryNameView;
    private TextView targetAmountView;
    private TextView currentAmountView;
    private ProgressBar progressBarView;

    private ShapeDrawable pgDrawable;

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

	ShapeDrawable background = new ShapeDrawable( new RoundRectShape( new float[] { PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE }, null, null ) );
	background.getPaint().setColor( COLOR_BACKGROUND );
	this.pgDrawable = new ShapeDrawable( new RoundRectShape( new float[] { PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE, PROGRESSBAR_ROUND_SIZE }, null, null ) );
	this.progressBarView.setProgressDrawable( new ClipDrawable( this.pgDrawable, Gravity.START, ClipDrawable.HORIZONTAL ) );
	this.progressBarView.setBackground( background );
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

	this.entryNameView.setText( AccountDisplayer.toString( this.getContext(), this.account ) );
	this.currentAmountView.setText( MainActivity.DEC_FORMAT.format( currentAmount ) );
	this.targetAmountView.setText( MainActivity.DEC_FORMAT.format( targetAmount ) );

	if( targetAmount < 0 ) {
	    this.targetAmountView.setTextColor( COLOR_OUT_OF_BUDGET );
	} else if( this.account.isOffBudget() ) {
	    this.targetAmountView.setTextColor( COLOR_OK );
	} else {
	    this.targetAmountView.setTextColor( COLOR_STANDARD );
	}

	if( currentAmount < 0 ) {
	    this.currentAmountView.setTextColor( COLOR_OUT_OF_BUDGET );
	} else {
	    this.currentAmountView.setTextColor( COLOR_STANDARD );
	}

	if( this.account.isInBudget() ) {
	    this.pgDrawable.getPaint().setColor( COLOR_OK );
	} else {
	    this.pgDrawable.getPaint().setColor( COLOR_WARNING );
	}

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

    public Account getModel() {
	return this.account;
    }
}
