package com.hesso.greenliving.model;

import java.util.Observable;
import java.util.Observer;

public class OffBudgetAccount extends Account implements Observer {
    private static final long serialVersionUID = -1038379197204925171L;

    public OffBudgetAccount() {
	super( "offbudget", 0 );
	Budget.getInstance().addObserver( this );
    }

    @Override
    public void update( Observable observable, Object data ) {
	this.setChanged();
	this.notifyObservers();
    }

    @Override
    public double getTargetAmount() {
	return Budget.getInstance().getTarget() - Budget.getInstance().getBudgeted();
    }
}
