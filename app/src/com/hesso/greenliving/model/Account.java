package com.hesso.greenliving.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import android.util.Log;

import com.hesso.greenliving.exception.NotSupportedOperationException;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "accounts" )
public class Account extends Entity implements Observer {
    private static final long serialVersionUID = -7764049582155718184L;
    private static final long MONTH_DURATION = 30 * 24;

    public static Account createOffBudget() {
	Account res = new Account( "offbudget", 0 );
	res.setOffBudget( true );
	return res;
    }

    @DatabaseField (canBeNull = false, foreign = true )
    protected Budget budget = Budget.getInstance();

    @DatabaseField (canBeNull = false )
    private String name;

    @DatabaseField (canBeNull = false )
    private double targetAmount;

    @ForeignCollectionField (eager = true, foreignFieldName = "sourceAccount" )
    private Collection<Transaction> outgoingTransactions = new LinkedList<Transaction>();

    @ForeignCollectionField (eager = true, foreignFieldName = "destinationAccount" )
    private Collection<Transaction> incomingTransactions = new LinkedList<Transaction>();

    @DatabaseField (canBeNull = false )
    private boolean isOffBudget = false;

    public Account() {
    }

    public Account( String name, double targetAmount ) {
	this.name = name;
	this.targetAmount = targetAmount;
    }

    @Override
    public void init() {
	this.budget = Budget.getInstance();

	this.outgoingTransactions = new LinkedList<Transaction>( this.outgoingTransactions );
	this.incomingTransactions = new LinkedList<Transaction>( this.incomingTransactions );

	this.setOffBudget( this.isOffBudget );
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
	double res;

	if( this.isOffBudget ) {
	    res = this.budget.getTarget() - this.budget.getBudgeted();
	} else {
	    res = this.targetAmount;
	}

	return res;
    }

    public void setTargetAmount( double targetAmount ) {
	this.targetAmount = targetAmount;
	this.setChanged();
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
	    transaction.deleteObserver( this );
	    this.setChanged();
	}

	return res;
    }

    public boolean addOutgoingTransaction( Transaction transaction ) {
	boolean res = !this.outgoingTransactions.contains( transaction ) && this.outgoingTransactions.add( transaction );

	if( res ) {
	    transaction.addObserver( this );
	    this.setChanged();
	}

	return res;
    }

    public boolean removeIncomingTransaction( Transaction transaction ) {
	boolean res = this.incomingTransactions.remove( transaction );

	Log.d( this.getClass().getSimpleName(), "removeIncomingTransaction of " + transaction.getAmount() + " = " + res );

	if( res ) {
	    transaction.deleteObserver( this );
	    this.setChanged();
	}

	return res;
    }

    public boolean addIncomingTransaction( Transaction transaction ) {
	Log.d( this.getClass().getSimpleName(), "currentAmount = " + this.getCurrentAmount() );

	boolean res = !this.incomingTransactions.contains( transaction ) && this.incomingTransactions.add( transaction );

	Log.d( this.getClass().getSimpleName(), "addIncomingTransaction of " + transaction.getAmount() + " = " + res );
	Log.d( this.getClass().getSimpleName(), "currentAmount = " + this.getCurrentAmount() );

	if( res ) {
	    transaction.addObserver( this );
	    this.setChanged();
	}

	return res;
    }

    public void fill( double amount ) {
	this.fill( amount, DateTime.now() );
    }

    public void fill( double amount, DateTime date ) {
	this.createTransaction( null, this, date, amount );
    }

    public void expense( double amount, DateTime date ) {
	this.createTransaction( this, null, date, amount );
    }

    public void transfert( double amount, Account destination, DateTime date ) {
	this.createTransaction( this, destination, date, amount );
    }

    public void createTransaction( Account source, Account destination, double amount ) {
	this.createTransaction( source, destination, DateTime.now(), amount );
    }

    public void createTransaction( Account source, Account destination, DateTime date, double amount ) {
	Transaction transaction = new Transaction( source, destination, date, amount );

	if( source != null ) {
	    source.notifyObservers();
	}
	if( destination != null ) {
	    destination.notifyObservers();
	}

	this.budget.notifyObservers();
	this.budget.addTransaction( transaction );
    }

    public double getCurrentAmount() {
	double res = 0;

	for( Transaction transaction : this.incomingTransactions ) {
	    res += transaction.getAmount();
	}

	for( Transaction transaction : this.outgoingTransactions ) {
	    res -= transaction.getAmount();
	}

	return res;
    }

    @Override
    public void destroy() {
	if( this.isOffBudget() ) {
	    throw new NotSupportedOperationException();
	} else {
	    Transaction current;
	    Iterator<Transaction> i = this.outgoingTransactions.iterator();
	    while( i.hasNext() ) {
		current = i.next();
		i.remove();
		current.delete();
	    }

	    i = this.incomingTransactions.iterator();
	    while( i.hasNext() ) {
		current = i.next();
		i.remove();
		current.delete();
	    }

	    this.budget.removeAccount( this );
	    this.budget.notifyObservers();
	}
    }

    public void setOffBudget( boolean value ) {
	this.isOffBudget = value;
	if( this.isOffBudget ) {
	    this.budget.addObserver( this );
	} else {
	    this.budget.deleteObserver( this );
	}
    }

    public boolean isOffBudget() {
	return this.isOffBudget;
    }

    @Override
    public void update( Observable observable, Object data ) {
	this.setChanged();
	this.notifyObservers();
    }

    public boolean isInBudget() {
	return (this.getCurrentAmount() - this.getTheoricAmount()) >= 0;
    }

    public double getTheoricAmount() {
	return Math.min( this.targetAmount, this.getTargetAmount() * new Duration( DateTime.now(), this.budget.getNextRefillDate() ).getStandardHours() / MONTH_DURATION );
    }
}
