package com.hesso.greenliving.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "entries" )
public class BudgetEntry extends Entity {
    private static final long serialVersionUID = -7764049582155718184L;

    @DatabaseField (canBeNull = false, foreign = true )
    private Budget budget = Budget.getInstance();

    @DatabaseField (canBeNull = false )
    private String name;

    @DatabaseField (canBeNull = false )
    private BigDecimal targetAmount;

    @ForeignCollectionField (eager = true )
    private Collection<ScheduledTransaction> scheduledTransactions = new HashSet<ScheduledTransaction>();

    @ForeignCollectionField (eager = true, foreignFieldName = "budgetFrom" )
    private Collection<Transaction> outgoingTransactions = new LinkedList<Transaction>();

    @ForeignCollectionField (eager = true, foreignFieldName = "budgetTo" )
    private Collection<Transaction> incomingTransactions = new LinkedList<Transaction>();

    public BudgetEntry() {
    }

    public BudgetEntry( String name, BigDecimal targetAmount ) {
	this.name = name;
	this.targetAmount = targetAmount;
    }

    public Budget getBudget() {
	return budget;
    }

    public String getName() {
	return this.name;
    }

    public void setName( String name ) {
	this.name = name;
	this.setChanged();
    }

    public BigDecimal getTargetAmount() {
	return this.targetAmount;
    }

    public void setTargetAmount( BigDecimal targetAmount ) {
	this.targetAmount = targetAmount;
	this.setChanged();
    }

    public Collection<ScheduledTransaction> getScheduledTransactions() {
	return new HashSet<ScheduledTransaction>( this.scheduledTransactions );
    }

    public Collection<Transaction> getTransactions() {
	Collection<Transaction> res = new LinkedList<Transaction>();

	res.addAll( this.incomingTransactions );
	res.addAll( this.outgoingTransactions );

	return res;
    }

    public Collection<? extends Transaction> getIncomingTransactions() {
	return new LinkedList<Transaction>( this.incomingTransactions );
    }

    public Collection<? extends Transaction> getOutgoingTransactions() {
	return new LinkedList<Transaction>( this.outgoingTransactions );
    }

    public boolean removeOutgoingTransaction( Transaction transaction ) {
	boolean res = this.outgoingTransactions.remove( transaction );
	if( res ) {
	    this.setChanged();
	}
	return res;
    }

    public boolean addOutgoingTransaction( Transaction transaction ) {
	boolean res = this.outgoingTransactions.add( transaction );
	if( res ) {
	    this.setChanged();
	}
	return res;
    }

    public boolean removeIncomingTransaction( Transaction transaction ) {
	boolean res = this.incomingTransactions.remove( transaction );
	if( res ) {
	    this.setChanged();
	}
	return res;
    }

    public boolean addIncomingTransaction( Transaction transaction ) {
	boolean res = this.incomingTransactions.add( transaction );
	if( res ) {
	    this.setChanged();
	}
	return res;
    }
}
