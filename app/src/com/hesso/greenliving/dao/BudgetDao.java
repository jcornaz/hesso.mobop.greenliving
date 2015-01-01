package com.hesso.greenliving.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;
import com.j256.ormlite.dao.Dao;

public class BudgetDao extends EntitiesDao<Budget> {

    private AccountsDao accountsDao;
    private Map<Budget, Set<Account>> persistedAccounts = new HashMap<Budget, Set<Account>>();

    public BudgetDao( Dao<Budget, Long> dao, AccountsDao entriesDao ) {
	super( dao );
	this.accountsDao = entriesDao;
    }

    @Override
    protected void updateChildren( Budget entity ) {
	this.updateChildren( entity, this.accountsDao, this.persistedAccounts, entity.getAccounts() );
    }

    @Override
    protected void deleteChildren( Budget entity ) {
	this.deleteChildren( entity, this.accountsDao, this.persistedAccounts );
	this.persistedAccounts.remove( entity );
    }

    @Override
    protected void refreshChildren( Budget entity ) {
	this.refreshChildren( entity, this.accountsDao, this.persistedAccounts );
    }

    @Override
    protected void hasBeenRefreshed( Budget entity ) {
	this.persistedAccounts.put( entity, new HashSet<Account>( entity.getAccounts() ) );
    }
}
