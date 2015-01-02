package com.hesso.greenliving.model;

import java.util.Observable;
import java.util.Observer;

import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "accounts" )
public class OffBudgetAccount extends Account implements Observer {
    private static final long serialVersionUID = -1038379197204925171L;

    public OffBudgetAccount() {
	super( "offbudget", 0 );
	this.budget.addObserver( this );
    }

    @Override
    public void update( Observable observable, Object data ) {
	this.setChanged();
	this.notifyObservers();
    }

    @Override
    public double getTargetAmount() {
	return this.budget.getTarget() - this.budget.getBudgeted();
    }
}
