package com.hesso.greenliving.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.hesso.greenliving.exception.NotSupportedOperationException;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "budgets" )
public class Budget extends Entity {
    private static final long serialVersionUID = 8240495679280473832L;

    private static final BigDecimal DEFAULT_TARGET = new BigDecimal( 0 );
    private static final int DEFAULT_DAY_OF_MONTH = 25;

    private static Budget instance;

    public static Budget getInstance() {
	if( instance == null ) {
	    instance = new Budget();
	}

	return instance;
    }

    @DatabaseField (canBeNull = false )
    private int dayOfMonth;

    @DatabaseField (canBeNull = false )
    private BigDecimal target = new BigDecimal( 0 );

    @ForeignCollectionField
    private Set<Account> accounts = new HashSet<Account>();

    private Set<Transaction> transactions = new HashSet<Transaction>();

    private Budget() {
	this.setDayOfMonth( DEFAULT_DAY_OF_MONTH );
	this.setTarget( DEFAULT_TARGET );
    }

    public Set<Account> getEntries() {
	return new HashSet<Account>( this.accounts );
    }

    private boolean addAccount( Account account ) {
	boolean res = this.accounts.add( account );

	if( res )
	    this.setChanged();

	return res;
    }

    public boolean removeAccount( Account account ) {
	boolean res = this.accounts.remove( account );

	if( res )
	    this.setChanged();

	return res;
    }

    public int getDayOfMonth() {
	return dayOfMonth;
    }

    public void setDayOfMonth( int dayOfMonth ) {
	if( this.dayOfMonth != dayOfMonth ) {
	    this.dayOfMonth = dayOfMonth;
	    this.setChanged();
	}
    }

    public BigDecimal getTarget() {
	return target;
    }

    public void setTarget( BigDecimal amount ) {
	if( !this.target.equals( amount ) ) {
	    this.target = amount;
	    this.setChanged();
	}
    }

    public Collection<Transaction> getTransactions() {
	return new HashSet<Transaction>( this.transactions );
    }

    public void addTransaction( Transaction transaction ) {
	this.transactions.add( transaction );
	this.setChanged();
    }

    public Account createEntry( String name, double target ) {
	Account res = new Account( name, target );
	this.addAccount( res );
	res.notifyObservers();
	this.notifyObservers();
	return res;
    }

    @Override
    public void setId( long id ) {
	super.setId( id );
    }

    @Override
    public void destroy() {
	throw new NotSupportedOperationException();
    }
}
