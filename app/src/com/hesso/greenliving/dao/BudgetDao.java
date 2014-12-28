package com.hesso.greenliving.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.Account;
import com.j256.ormlite.dao.Dao;

public class BudgetDao extends EntitiesDao<Budget> {

    private AccountsDao entriesDao;
    private Map<Budget, Set<Account>> persistedAccounts = new HashMap<Budget, Set<Account>>();

    public BudgetDao( Dao<Budget, Long> dao ) {
	super( dao );
	this.entriesDao = PersistenceManager.getInstance().getEntriesDao();
    }

    @Override
    protected void updateChildrenList( Budget entity ) {
	this.updateChildrenList( entity, this.entriesDao, this.persistedAccounts, entity.getEntries() );
    }

    @Override
    protected void deleteChildren( Budget entity ) {
	this.deleteChildren( entity, this.entriesDao, this.persistedAccounts );
    }
}
