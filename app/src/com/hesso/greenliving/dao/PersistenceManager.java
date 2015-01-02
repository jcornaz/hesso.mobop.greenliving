package com.hesso.greenliving.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hesso.greenliving.exception.UnexpectedException;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.Transaction;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public final class PersistenceManager extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "greenliving.db";

    private static final int DB_VERSION = 22;

    private static PersistenceManager instance;
    private BudgetDao budgetDao;
    private AccountsDao entriesDao;
    private TransactionsDao transactionsDao;

    public static PersistenceManager getInstance() {
	return instance;
    }

    public static void start( Context context ) {
	if( instance == null ) {
	    instance = new PersistenceManager( context );
	    instance.doStart();
	}
    }

    public static void stop() {
	if( instance == null )
	    throw new IllegalStateException( "not started" );

	instance.doStop();
	instance = null;
    }

    private PersistenceManager( Context context ) {
	super( context, DB_NAME, null, DB_VERSION );
    }

    @SuppressWarnings ("unchecked" )
    private void doStart() {

	try {
	    this.transactionsDao = new TransactionsDao( (Dao<Transaction, Long>) this.getDao( Transaction.class ) );
	    this.entriesDao = new AccountsDao( (Dao<Account, Long>) this.getDao( Account.class ), this.transactionsDao );
	    this.budgetDao = new BudgetDao( (Dao<Budget, Long>) this.getDao( Budget.class ), this.entriesDao );
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}

	this.budgetDao.refresh( Budget.getInstance() );
    }

    private void doStop() {
	Budget.getInstance().deleteObserver( this.budgetDao );
    }

    @Override
    public void onCreate( SQLiteDatabase database, ConnectionSource connectionSource ) {
	try {
	    TableUtils.createTable( connectionSource, Budget.class );
	    TableUtils.createTable( connectionSource, Account.class );
	    TableUtils.createTable( connectionSource, Transaction.class );
	    this.getDao( Budget.class ).create( Budget.getInstance() );
	    Log.i( this.getClass().getSimpleName(), "tables created" );
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion ) {

	Log.d( this.getClass().getSimpleName(), "upgrading database from version " + oldVersion + " to " + newVersion );
	try {
	    TableUtils.dropTable( connectionSource, Budget.class, true );
	    TableUtils.dropTable( connectionSource, Account.class, true );
	    TableUtils.dropTable( connectionSource, Transaction.class, true );
	    Log.i( this.getClass().getSimpleName(), "tables droped" );
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}

	this.onCreate( database, connectionSource );
    }

    public BudgetDao getBudgetDao() {
	return budgetDao;
    }

    public AccountsDao getEntriesDao() {
	return entriesDao;
    }

    public TransactionsDao getTransactionsDao() {
	return transactionsDao;
    }
}
