package com.hesso.greenliving.model;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "transactions" )
public class Transaction extends Entity {
    private static final long serialVersionUID = -3676967997552222140L;

    @DatabaseField (canBeNull = false, foreign = true )
    private Budget budget = Budget.getInstance();

    @DatabaseField (canBeNull = false, foreign = true )
    private BudgetEntry sourceEntry;

    @DatabaseField (canBeNull = true, foreign = true )
    private BudgetEntry destinationEntry;

    @DatabaseField (canBeNull = false )
    private DateTime date;

    @DatabaseField (canBeNull = false )
    private double amount;

    public Transaction() {
    }

    public Transaction( BudgetEntry sourceEntry, BudgetEntry destinationEntry, DateTime date, double amount ) {
	this.setSourceEntry( sourceEntry );
	this.setDestinationEntry( destinationEntry );
	this.setAmount( amount );
	this.budget.addTransaction( this );
    }

    public DateTime getDate() {
	return date;
    }

    public void setDate( DateTime date ) {
	if( !this.date.equals( date ) ) {
	    this.date = date;
	    this.setChanged();
	}
    }

    public BudgetEntry getSourceEntry() {
	return this.sourceEntry;
    }

    public void setSourceEntry( BudgetEntry budgetEntry ) {
	if( this.sourceEntry != budgetEntry ) {
	    if( this.sourceEntry != null ) {
		this.sourceEntry.removeOutgoingTransaction( this );
	    }
	    this.sourceEntry = budgetEntry;
	    if( this.sourceEntry != null ) {
		this.sourceEntry.addOutgoingTransaction( this );
	    }
	    this.setChanged();
	}
    }

    public BudgetEntry getDestinationEntry() {
	return this.destinationEntry;
    }

    public void setDestinationEntry( BudgetEntry budgetEntry ) {
	if( this.destinationEntry != budgetEntry ) {
	    if( this.destinationEntry != null ) {
		this.destinationEntry.removeIncomingTransaction( this );
	    }
	    this.destinationEntry = budgetEntry;
	    if( this.destinationEntry != null ) {
		this.destinationEntry.addIncomingTransaction( this );
	    }
	    this.setChanged();
	}
    }

    public double getAmount() {
	return this.amount;
    }

    public void setAmount( double amount ) {
	if( amount < 0 ) {
	    BudgetEntry source = this.getSourceEntry();
	    this.setSourceEntry( this.getDestinationEntry() );
	    this.setDestinationEntry( source );
	}
	this.amount = Math.abs( amount );
	this.setChanged();
    }

    public boolean hasDestination() {
	return this.destinationEntry != null;
    }

    public boolean hasSource() {
	return this.sourceEntry != null;
    }

    public TransctionType getType() {
	TransctionType res;
	if( this.hasSource() && this.hasDestination() ) {
	    if( this.hasDestination() ) {
		res = TransctionType.TRANSFER;
	    } else {
		res = TransctionType.EXPENSE;
	    }
	} else {
	    res = TransctionType.FILL;
	}
	return res;
    }
}
