package com.hesso.greenliving.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.BudgetEntry;

public class BudgetEntryView extends LinearLayout implements EntityView<BudgetEntry> {

    private static final int PROGRESSBAR_SIZE = 1000;

    public static BudgetEntryView inflate( ViewGroup parent ) {
	return (BudgetEntryView) LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_budget, parent, false );
    }

    private BudgetEntry budgetEntry;

    private TextView entryNameView;
    private TextView targetAmountView;
    private TextView currentAmountView;
    private ProgressBar progressBarView;

    @Override
    protected void onFinishInflate() {
	super.onFinishInflate();

	this.targetAmountView = (TextView) this.findViewById( R.id.targetAmount );
	this.currentAmountView = (TextView) this.findViewById( R.id.currentAmount );
	this.progressBarView = (ProgressBar) this.findViewById( R.id.progressBar );
	this.progressBarView.setMax( PROGRESSBAR_SIZE );
    }

    public BudgetEntryView( Context context, AttributeSet attrs, int defStyle ) {
	super( context, attrs, defStyle );
    }

    public BudgetEntryView( Context context, AttributeSet attrs ) {
	super( context, attrs );
    }

    public BudgetEntryView( Context context ) {
	super( context );
    }

    @Override
    public void setModel( BudgetEntry entry ) {
	this.budgetEntry = entry;

	double targetAmount = this.budgetEntry.getTargetAmount();
	double currentAmount = this.budgetEntry.getCurrentAmount();

	this.entryNameView.setText( this.budgetEntry.getName() );
	this.currentAmountView.setTag( String.valueOf( currentAmount ) );
	this.targetAmountView.setText( String.valueOf( targetAmount ) );

	if( targetAmount != 0 ) {
	    this.progressBarView.setVisibility( View.VISIBLE );
	    this.progressBarView.setProgress( (int) Math.round( PROGRESSBAR_SIZE * Math.max( 0, Math.min( currentAmount, targetAmount ) ) / targetAmount ) );
	} else {
	    this.progressBarView.setVisibility( View.INVISIBLE );
	}
    }
}
