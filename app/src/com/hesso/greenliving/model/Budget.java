package com.hesso.greenliving.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.joda.time.DateTime;

import android.support.v4.util.LongSparseArray;
import android.util.Log;

import com.hesso.greenliving.exception.NotSupportedOperationException;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable (tableName = "budgets" )
public class Budget extends Entity {
    private static final long serialVersionUID = 8240495679280473832L;

    private static final double DEFAULT_TARGET = 1000;
    private static final int DEFAULT_DAY_OF_MONTH = 25;

    private static final long ID = 42;

    private static Budget instance;

    public static synchronized Budget getInstance() {
	if( instance == null ) {
	    instance = new Budget();
	}
	return instance;
    }

    @DatabaseField (canBeNull = false )
    private int dayOfMonth;

    @DatabaseField (canBeNull = false )
    private double target = 0;

    @ForeignCollectionField (eager = true )
    private Collection<Account> accounts = new HashSet<Account>();

    @DatabaseField (canBeNull = true, foreign = true )
    private Account offBudgetAccount;

    @DatabaseField (canBeNull = false, dataType = DataType.DATE )
    private Date lastRefill = DateTime.now().toDate();

    private Collection<Transaction> transactions = new HashSet<Transaction>();
    private LongSparseArray<Account> accountsMap = new LongSparseArray<Account>();
    private LongSparseArray<Transaction> transactionsMap = new LongSparseArray<Transaction>();

    private Budget() {
	this.setDayOfMonth( DEFAULT_DAY_OF_MONTH );
	this.setTarget( DEFAULT_TARGET );
	this.setId( ID );
    }

    @Override
    public void init() {
	if( this.offBudgetAccount == null ) {
	    this.offBudgetAccount = Account.createOffBudget();
	} else {
	    this.offBudgetAccount.setOffBudget( true );
	}

	this.accounts = new HashSet<Account>( this.accounts );
	this.accounts.add( this.offBudgetAccount );

	this.map( this.accounts, this.accountsMap );
	this.map( this.transactions, this.transactionsMap );
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

    public double getTarget() {
	return target;
    }

    public void setTarget( double amount ) {
	if( this.target != amount ) {
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
	res.notifyObservers();
	this.addAccount( res );
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

    public Account getOffBudget() {
	return this.offBudgetAccount;
    }

    public double getBudgeted() {
	double res = 0;
	for( Account account : this.getAccounts() ) {
	    if( !account.isOffBudget() )
		res += account.getTargetAmount();
	}
	return res;
    }

    public void autoRefill() {
	if( this.needRefill() ) {
	    this.refill();
	}
    }

    private boolean needRefill() {

	return this.getNextRefillDate().isBeforeNow();
    }

    public DateTime getNextRefillDate() {
	DateTime lastRefillDate = this.getLastRefillDate().withHourOfDay( 0 ).withMinuteOfHour( 0 ).withSecondOfMinute( 0 );
	DateTime res = lastRefillDate.withDayOfMonth( Math.max( 1, Math.min( lastRefillDate.dayOfMonth().withMaximumValue().getDayOfMonth(), this.dayOfMonth ) ) );

	if( res.isBefore( lastRefillDate ) ) {
	    res = res.plusMonths( 1 );
	}
	return res;
    }

    public DateTime getLastRefillDate() {
	return new DateTime( this.lastRefill );
    }

    private void refill() {
	for( Account account : this.accounts ) {
	    account.fill( account.getTargetAmount() );
	}
	this.lastRefill = DateTime.now().toDate();
	this.setChanged();
	this.notifyObservers();
    }
}
