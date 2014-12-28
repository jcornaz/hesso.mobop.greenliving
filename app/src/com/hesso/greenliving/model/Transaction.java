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
    private Account sourceAccount;

    @DatabaseField (canBeNull = true, foreign = true )
    private Account destinationAccount;

    @DatabaseField (canBeNull = false )
    private DateTime date;

    @DatabaseField (canBeNull = false )
    private double amount;

    public Transaction() {
    }

    public Transaction( Account sourceEntry, Account destinationEntry, DateTime date, double amount ) {
	this.setSourceAccount( sourceEntry );
	this.setDestinationAccount( destinationEntry );
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

    public Account getSourceAccount() {
	return this.sourceAccount;
    }

    public void setSourceAccount( Account budgetEntry ) {
	if( this.sourceAccount != budgetEntry ) {
	    if( this.sourceAccount != null ) {
		this.sourceAccount.removeOutgoingTransaction( this );
	    }
	    this.sourceAccount = budgetEntry;
	    if( this.sourceAccount != null ) {
		this.sourceAccount.addOutgoingTransaction( this );
	    }
	    this.setChanged();
	}
    }

    public Account getDestinationAccount() {
	return this.destinationAccount;
    }

    public void setDestinationAccount( Account budgetEntry ) {
	if( this.destinationAccount != budgetEntry ) {
	    if( this.destinationAccount != null ) {
		this.destinationAccount.removeIncomingTransaction( this );
	    }
	    this.destinationAccount = budgetEntry;
	    if( this.destinationAccount != null ) {
		this.destinationAccount.addIncomingTransaction( this );
	    }
	    this.setChanged();
	}
    }

    public double getAmount() {
	return this.amount;
    }

    public void setAmount( double amount ) {
	if( amount < 0 ) {
	    Account source = this.getSourceAccount();
	    this.setSourceAccount( this.getDestinationAccount() );
	    this.setDestinationAccount( source );
	}
	this.amount = Math.abs( amount );
	this.setChanged();
    }

    public boolean hasDestination() {
	return this.destinationAccount != null;
    }

    public boolean hasSource() {
	return this.sourceAccount != null;
    }

    public TransctionType getType() {
	TransctionType res;
	if( this.hasSource() ) {
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

    public boolean contains( Account budgetAccount ) {
	if( this.sourceAccount != null ) {
	    if( this.sourceAccount.equals( budgetAccount ) ) {
		return true;
	    }
	}

	if( this.destinationAccount != null ) {
	    if( this.destinationAccount.equals( budgetAccount ) ) {
		return true;
	    }
	}

	return false;
    }
}
