package com.hesso.greenliving.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hesso.greenliving.model.BudgetEntry;
import com.hesso.greenliving.model.ScheduledTransaction;
import com.hesso.greenliving.model.Transaction;
import com.j256.ormlite.dao.Dao;

public class BudgetEntriesDao extends EntitiesDao<BudgetEntry> {

    private TransactionsDao transactionsDao;
    private SchedulesDao schedulesDao;
    private Map<BudgetEntry, Set<Transaction>> persistedTransactions = new HashMap<BudgetEntry, Set<Transaction>>();
    private Map<BudgetEntry, Set<ScheduledTransaction>> persistedSchedules = new HashMap<BudgetEntry, Set<ScheduledTransaction>>();

    public BudgetEntriesDao( Dao<BudgetEntry, Long> dao ) {
	super( dao );
	this.transactionsDao = PersistenceManager.getInstance().getTransactionsDao();
	this.schedulesDao = PersistenceManager.getInstance().getSchedulesDao();
    }

    @Override
    protected void updateChildrenList( BudgetEntry entity ) {
	this.updateChildrenList( entity, this.transactionsDao, this.persistedTransactions, entity.getTransactions() );
	this.updateChildrenList( entity, this.schedulesDao, this.persistedSchedules, entity.getScheduledTransactions() );
    }

    @Override
    protected void deleteChildren( BudgetEntry entity ) {
	this.deleteChildren( entity, this.transactionsDao, this.persistedTransactions );
	this.deleteChildren( entity, this.schedulesDao, this.persistedSchedules );
    }
}
