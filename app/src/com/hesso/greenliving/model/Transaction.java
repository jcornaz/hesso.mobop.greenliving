package com.hesso.greenliving.model;

import java.util.Date;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "transactions" )
public class Transaction extends Entity {
    private static final long serialVersionUID = -3676967997552222140L;

    @DatabaseField (canBeNull = false, foreign = true )
    private Budget budget = Budget.getInstance();

    @DatabaseField (columnName = "sourceAccount", canBeNull = true, foreign = true )
    private Account sourceAccount;

    @DatabaseField (columnName = "destinationAccount", canBeNull = true, foreign = true )
    private Account destinationAccount;

    @DatabaseField (canBeNull = false )
    private double amount;

    @DatabaseField (canBeNull = false, dataType = DataType.DATE )
    private Date date;

    public Transaction() {
    }

    public Transaction( Account sourceEntry, Account destinationEntry, DateTime date, double amount ) {
	this();
	this.setAmount( amount );
	this.setSourceAccount( sourceEntry );
	this.setDestinationAccount( destinationEntry );
	this.setDate( date );
	this.budget.addTransaction( this );
    }

    @Override
    public void init() {
	this.budget = Budget.getInstance();
	this.budget.addTransaction( this );
    }

    public DateTime getDate() {
	return new DateTime( this.date );
    }

    public void setDate( DateTime date ) {
	if( date == null ) {
	    throw new NullPointerException( "transaction date cannot be null" );
	}

	if( this.date == null || !this.date.equals( date ) ) {
	    this.date = date.toDate();
	    this.setChanged();
	}
    }

    public Account getSourceAccount() {
	return this.sourceAccount;
    }

    public void setSourceAccount( Account account ) {
	if( this.sourceAccount != account ) {
	    if( this.sourceAccount != null ) {
		this.sourceAccount.removeOutgoingTransaction( this );
	    }
	    this.sourceAccount = account;
	    if( this.sourceAccount != null ) {
		this.sourceAccount.addOutgoingTransaction( this );
	    }
	    this.setChanged();
	}
    }

    public Account getDestinationAccount() {
	return this.destinationAccount;
    }

    public void setDestinationAccount( Account account ) {
	if( this.destinationAccount != account ) {
	    if( this.destinationAccount != null ) {
		this.destinationAccount.removeIncomingTransaction( this );
	    }
	    this.destinationAccount = account;
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
	    res = TransctionType.CREDIT;
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

    @Override
    protected void destroy() {

	if( this.hasSource() ) {
	    this.sourceAccount.removeOutgoingTransaction( this );
	    this.sourceAccount.notifyObservers();
	}

	if( this.hasDestination() ) {
	    this.destinationAccount.removeIncomingTransaction( this );
	    this.destinationAccount.notifyObservers();
	}

	this.budget.removeTransaction( this );
	this.budget.notifyObservers();
    }
}
