package com.hesso.greenliving.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import org.joda.time.DateTime;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "entries" )
public class Account extends Entity implements Observer {
    private static final long serialVersionUID = -7764049582155718184L;

    @DatabaseField (canBeNull = false, foreign = true )
    private Budget budget = Budget.getInstance();

    @DatabaseField (canBeNull = false )
    private String name;

    @DatabaseField (canBeNull = false )
    private double targetAmount;

    @ForeignCollectionField (eager = true, foreignFieldName = "sourceAccount" )
    private Collection<Transaction> outgoingTransactions = new HashSet<Transaction>();

    @ForeignCollectionField (eager = true, foreignFieldName = "destinationAccount" )
    private Collection<Transaction> incomingTransactions = new HashSet<Transaction>();

    private boolean initialized = false;

    public Account() {
    }

    public Account( String name, double targetAmount ) {
	this.name = name;
	this.targetAmount = targetAmount;
    }

    @Override
    public void init() {
	this.budget = Budget.getInstance();
	Log.d( this.getClass().getSimpleName(), "initializing" );
	this.outgoingTransactions = new HashSet<Transaction>( this.outgoingTransactions );
	this.incomingTransactions = new HashSet<Transaction>( this.incomingTransactions );
	this.initialized = true;
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

    boolean addOutgoingTransaction( Transaction transaction ) {
	Log.i( this.getClass().getSimpleName(), "initilialized = " + String.valueOf( this.initialized ) );
	boolean res = this.outgoingTransactions.add( transaction );

	if( res ) {
	    transaction.addObserver( this );
	    this.setChanged();
	}
	return res;
    }

    boolean removeIncomingTransaction( Transaction transaction ) {
	boolean res = this.incomingTransactions.remove( transaction );

	if( res ) {
	    transaction.deleteObserver( this );
	    this.setChanged();
	}
	return res;
    }

    boolean addIncomingTransaction( Transaction transaction ) {
	boolean res = this.incomingTransactions.add( transaction );

	if( res ) {
	    transaction.addObserver( this );
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
	    res -= transaction.getAmount();
	}

	return res;
    }

    @Override
    public void destroy() {

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

    @Override
    public String toString() {
	return name;
    }

    @Override
    public void update( Observable observable, Object data ) {
	this.setChanged();
	this.notifyObservers();
    }
}
