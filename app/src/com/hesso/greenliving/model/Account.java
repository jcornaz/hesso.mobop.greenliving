package com.hesso.greenliving.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.joda.time.DateTime;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "entries" )
public class Account extends Entity {
    private static final long serialVersionUID = -7764049582155718184L;

    @DatabaseField (canBeNull = false, foreign = true )
    private Budget budget = Budget.getInstance();

    @DatabaseField (canBeNull = false )
    private String name;

    @DatabaseField (canBeNull = false )
    private double targetAmount;

    @ForeignCollectionField (eager = true )
    private Collection<ScheduledTransaction> scheduledTransactions = new HashSet<ScheduledTransaction>();

    @ForeignCollectionField (eager = true, foreignFieldName = "sourceAccount" )
    private Collection<Transaction> outgoingTransactions = new LinkedList<Transaction>();

    @ForeignCollectionField (eager = true, foreignFieldName = "destinationAccount" )
    private Collection<Transaction> incomingTransactions = new LinkedList<Transaction>();

    public Account() {
    }

    public Account( String name, double targetAmount ) {
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

    public double getTargetAmount() {
	return this.targetAmount;
    }

    public void setTargetAmount( double targetAmount ) {
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

    public boolean removeScheduledTransaction( ScheduledTransaction schedule ) {
	boolean res = this.scheduledTransactions.remove( schedule );
	if( res ) {
	    this.setChanged();
	}
	return res;
    }

    public boolean addScheduledTransaction( ScheduledTransaction schedule ) {
	boolean res = this.scheduledTransactions.add( schedule );
	if( res ) {
	    this.setChanged();
	}
	return res;
    }

    public void fill( double amount ) {
	new Transaction( null, this, DateTime.now(), amount ).notifyObservers();
	this.notifyObservers();
	this.budget.notifyObservers();
    }

    public void expense( double amount ) {
	new Transaction( this, null, DateTime.now(), amount ).notifyObservers();
	this.notifyObservers();
	this.budget.notifyObservers();
    }

    public void transfert( double amount, Account destination ) {
	new Transaction( this, destination, DateTime.now(), amount ).notifyObservers();
	this.notifyObservers();
	this.budget.notifyObservers();
    }

    public double getCurrentAmount() {
	double res = 0;

	for( Transaction transaction : this.incomingTransactions ) {
	    res += transaction.getAmount();
	}

	for( Transaction transaction : this.outgoingTransactions ) {
	    res += transaction.getAmount();
	}

	return res;
    }

    @Override
    public void destroy() {

	for( Transaction transaction : this.outgoingTransactions ) {
	    transaction.delete();
	}

	for( Transaction transaction : this.incomingTransactions ) {
	    transaction.delete();
	}

	this.budget.removeAccount( this );
	this.budget.notifyObservers();
    }

    @Override
    public String toString() {
	return name;
    }
}
