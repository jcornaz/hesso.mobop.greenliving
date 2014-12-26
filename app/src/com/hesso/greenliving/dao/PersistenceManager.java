package com.hesso.greenliving.dao;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.BudgetEntry;
import com.hesso.greenliving.model.ScheduledTransaction;
import com.hesso.greenliving.model.Transaction;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public final class PersistenceManager extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "greenliving.db";
    private static final int DB_VERSION = 1;
    private static PersistenceManager instance;
    private BudgetDao budgetDao;
    private BudgetEntriesDao entriesDao;
    private TransactionsDao transactionsDao;
    private SchedulesDao schedulesDao;

    public static PersistenceManager getInstance() {
	return instance;
    }

    public static void start( Context context ) {
	if( instance != null ) {
	    stop();
	}

	instance = new PersistenceManager( context );
	instance.doStart();
    }

    public static void stop() {
	if( instance == null )
	    throw new IllegalStateException( "not started" );

	instance.doStop();
	instance = null;
    }

    @SuppressWarnings ("unchecked" )
    private PersistenceManager( Context context ) {
	super( context, DB_NAME, null, DB_VERSION );

	try {
	    this.budgetDao = new BudgetDao( (Dao<Budget, Integer>) this.getDao( Budget.class ) );
	    this.entriesDao = new BudgetEntriesDao( (Dao<BudgetEntry, Integer>) this.getDao( BudgetEntry.class ) );
	    this.transactionsDao = new TransactionsDao( (Dao<Transaction, Integer>) this.getDao( Transaction.class ) );
	    this.schedulesDao = new SchedulesDao( (Dao<ScheduledTransaction, Integer>) this.getDao( ScheduledTransaction.class ) );
	} catch( SQLException e ) {
	    throw new RuntimeException( e );
	}
    }

    private void doStart() {
	try {
	    Budget currentBudget = Budget.getInstance();
	    List<Budget> existingBudgets = this.budgetDao.queryForAll();
	    if( existingBudgets.isEmpty() ) {
		this.budgetDao.create( Budget.getInstance() );
	    } else {
		currentBudget.setId( existingBudgets.get( 0 ).getId() );
		this.budgetDao.update( currentBudget );
		this.budgetDao.refresh( currentBudget );
	    }
	    Budget.getInstance().addObserver( this.budgetDao );
	} catch( SQLException e ) {
	    this.doStop();
	    throw new RuntimeException( e );
	}
    }

    private void doStop() {
	Budget.getInstance().deleteObserver( this.budgetDao );
    }

    @Override
    public void onCreate( SQLiteDatabase database, ConnectionSource connectionSource ) {
	try {
	    TableUtils.createTable( connectionSource, Budget.class );
	    TableUtils.createTable( connectionSource, BudgetEntry.class );
	    TableUtils.createTable( connectionSource, ScheduledTransaction.class );
	    TableUtils.createTable( connectionSource, Transaction.class );
	    Log.i( this.getClass().getName(), "tables created" );
	} catch( SQLException e ) {
	    throw new RuntimeException( e );
	}
    }

    @Override
    public void onUpgrade( SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion ) {

	try {
	    TableUtils.dropTable( connectionSource, Budget.class, true );
	    TableUtils.dropTable( connectionSource, BudgetEntry.class, true );
	    TableUtils.dropTable( connectionSource, ScheduledTransaction.class, true );
	    TableUtils.dropTable( connectionSource, Transaction.class, true );
	    Log.i( this.getClass().getName(), "tables droped" );
	} catch( SQLException e ) {
	    throw new RuntimeException( e );
	}

	this.onCreate( database, connectionSource );
    }

    public BudgetDao getBudgetDao() {
	return budgetDao;
    }

    public BudgetEntriesDao getEntriesDao() {
	return entriesDao;
    }

    public TransactionsDao getTransactionsDao() {
	return transactionsDao;
    }

    public SchedulesDao getSchedulesDao() {
	return schedulesDao;
    }
}
