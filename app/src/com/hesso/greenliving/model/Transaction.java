package com.hesso.greenliving.model;

import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "transactions" )
public class Transaction extends Entity {
    private static final long serialVersionUID = -3676967997552222140L;

    @DatabaseField (canBeNull = false, foreign = true )
    private BudgetEntry sourceEntry;

    @DatabaseField (canBeNull = true, foreign = true )
    private BudgetEntry destinationEntry;

    @DatabaseField (canBeNull = false )
    private DateTime date;

    @DatabaseField (canBeNull = false )
    private BigDecimal amount;

    public Transaction() {
    }

    public Transaction( BudgetEntry sourceEntry, DateTime date, BigDecimal amount ) {
	this.setSourceEntry( sourceEntry );
	this.setDestinationEntry( destinationEntry );
	this.setAmount( amount );
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
	    this.sourceEntry.removeOutgoingTransaction( this );
	    this.sourceEntry = budgetEntry;
	    this.sourceEntry.addOutgoingTransaction( this );
	    this.setChanged();
	}
    }

    public BudgetEntry getDestinationEntry() {
	return this.destinationEntry;
    }

    public void setDestinationEntry( BudgetEntry budgetEntry ) {
	if( this.destinationEntry != budgetEntry ) {
	    this.destinationEntry.removeIncomingTransaction( this );
	    this.destinationEntry = budgetEntry;
	    this.destinationEntry.addIncomingTransaction( this );
	    this.setChanged();
	}
    }

    public BigDecimal getAmount() {
	return this.amount;
    }

    public void setAmount( BigDecimal amount ) {
	if( amount.doubleValue() < 0 ) {
	    BudgetEntry source = this.getSourceEntry();
	    this.setSourceEntry( this.getDestinationEntry() );
	    this.setDestinationEntry( source );
	}
	this.amount = amount.abs();
	this.setChanged();
    }

    public boolean isTransfert() {
	return this.destinationEntry != null;
    }
}
