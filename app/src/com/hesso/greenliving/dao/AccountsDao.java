package com.hesso.greenliving.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Transaction;
import com.j256.ormlite.dao.Dao;

public class AccountsDao extends EntitiesDao<Account> {

    private TransactionsDao transactionsDao;
    private Map<Account, Set<Transaction>> persistedTransactions = new HashMap<Account, Set<Transaction>>();

    public AccountsDao( Dao<Account, Long> dao, TransactionsDao transactionsDao ) {
	super( dao );
	this.transactionsDao = transactionsDao;
    }

    @Override
    protected void persistChildren( Account entity ) {
	this.persistChildren( entity, this.transactionsDao, this.persistedTransactions, entity.getTransactions() );
    }

    @Override
    protected void deleteChildren( Account entity ) {
	this.deleteChildren( entity, this.transactionsDao, this.persistedTransactions );
	this.persistedTransactions.remove( entity );
    }

    @Override
    protected void refreshChildren( Account entity ) {
	this.refreshChildren( entity, this.transactionsDao, this.persistedTransactions );
    }

    @Override
    protected void hasBeenRefreshed( Account entity ) {
	this.persistedTransactions.put( entity, new HashSet<Transaction>( entity.getTransactions() ) );
    }
}
