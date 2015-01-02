package com.hesso.greenliving.model;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Observer;

import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.hesso.greenliving.exception.NotSupportedOperationException;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "budgets" )
public class Budget extends Entity {
    private static final long serialVersionUID = 8240495679280473832L;

    private static final BigDecimal DEFAULT_TARGET = new BigDecimal( 1000 );
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

    @ForeignCollectionField (eager = true )
    private Collection<Account> accounts = new HashSet<Account>();

    @ForeignCollectionField (eager = true )
    private Collection<Transaction> transactions = new HashSet<Transaction>();

    // @DatabaseField (canBeNull = false )
    // private Account offBudgetAccount = null;

    private LongSparseArray<Account> accountsMap = new LongSparseArray<Account>();
    private LongSparseArray<Transaction> transactionsMap = new LongSparseArray<Transaction>();

    private Budget() {
	this.setDayOfMonth( DEFAULT_DAY_OF_MONTH );
	this.setTarget( DEFAULT_TARGET );
    }

    @Override
    public void init() {
	this.accounts = new HashSet<Account>( this.accounts );
	this.transactions = new HashSet<Transaction>( this.transactions );

	this.map( this.accounts, this.accountsMap );
	this.map( this.transactions, this.transactionsMap );

	// if( this.offBudgetAccount == null ) {
	// this.offBudgetAccount = new OffBudgetAccount();
	// }
    }

    public Collection<Account> getAccounts() {
	return new HashSet<Account>( this.accounts );
    }

    public Account getAccountById( long id ) {
	return this.accountsMap.get( id );
    }

    public Transaction getTransactionById( long id ) {
	return this.transactionsMap.get( id );
    }

    private boolean addAccount( Account account ) {
	boolean res = this.accounts.add( account );

	if( res ) {
	    this.accountsMap.put( account.getId(), account );
	    this.setChanged();
	}

	return res;
    }

    boolean removeAccount( Account account ) {
	boolean res = this.accounts.remove( account );

	if( res ) {
	    this.accountsMap.remove( account.getId() );
	    this.setChanged();
	    Log.i( this.getClass().getSimpleName(), "Account " + account.getId() + " removed from budget" );
	} else {
	    Log.w( this.getClass().getSimpleName(), "Account " + account.getId() + " not removed from budget" );
	}

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

    boolean addTransaction( Transaction transaction ) {
	boolean res = this.transactions.add( transaction );

	if( res ) {
	    this.transactionsMap.put( transaction.getId(), transaction );
	    this.setChanged();
	}

	return res;
    }

    boolean removeTransaction( Transaction transaction ) {
	boolean res = this.transactions.remove( transaction );

	if( res ) {
	    this.transactionsMap.remove( transaction.getId() );
	    this.setChanged();
	}

	return res;
    }

    public Account createAccount( String name, double target ) {
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
	// Le budget ne peut pas être supprimé
	throw new NotSupportedOperationException();
    }

    @Override
    public void addObserver( Observer observer ) {
	Log.d( this.getClass().getSimpleName(), "observer added : " + observer.getClass().getSimpleName() );
	Log.d( this.getClass().getSimpleName(), this.countObservers() + " observers watching" );
	super.addObserver( observer );
    }

    @Override
    public synchronized void deleteObserver( Observer observer ) {
	Log.d( this.getClass().getSimpleName(), "observer removed : " + observer.getClass().getSimpleName() );
	Log.d( this.getClass().getSimpleName(), this.countObservers() + " observers watching" );
	super.deleteObserver( observer );
    }

    @Override
    public void notifyObservers() {
	Log.d( this.getClass().getSimpleName(), "notify " + this.countObservers() + " observers : " + this.hasChanged() );
	super.notifyObservers();
    }

    @Override
    public synchronized void deleteObservers() {
	Log.d( this.getClass().getSimpleName(), " observers deleted" );
	super.deleteObservers();
    }

    // public Account getOffBudget() {
    // return this.offBudgetAccount;
    // }
}
