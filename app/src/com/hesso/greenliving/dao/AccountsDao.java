package com.hesso.greenliving.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.ScheduledTransaction;
import com.hesso.greenliving.model.Transaction;
import com.j256.ormlite.dao.Dao;

public class AccountsDao extends EntitiesDao<Account> {

    private TransactionsDao transactionsDao;
    private SchedulesDao schedulesDao;
    private Map<Account, Set<Transaction>> persistedTransactions = new HashMap<Account, Set<Transaction>>();
    private Map<Account, Set<ScheduledTransaction>> persistedSchedules = new HashMap<Account, Set<ScheduledTransaction>>();

    public AccountsDao( Dao<Account, Long> dao, TransactionsDao transactionsDao , SchedulesDao schedulesDao  ) {
	super( dao );
	this.transactionsDao = transactionsDao;
	this.schedulesDao = schedulesDao;
    }

    @Override
    protected void updateChildrenList( Account entity ) {
	this.updateChildrenList( entity, this.transactionsDao, this.persistedTransactions, entity.getTransactions() );
	this.updateChildrenList( entity, this.schedulesDao, this.persistedSchedules, entity.getScheduledTransactions() );
    }

    @Override
    protected void deleteChildren( Account entity ) {
	this.deleteChildren( entity, this.transactionsDao, this.persistedTransactions );
	this.deleteChildren( entity, this.schedulesDao, this.persistedSchedules );
    }
}
