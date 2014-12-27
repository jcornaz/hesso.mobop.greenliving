package com.hesso.greenliving.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.BudgetEntry;
import com.j256.ormlite.dao.Dao;

public class BudgetDao extends EntitiesDao<Budget> {

    private BudgetEntriesDao entriesDao;
    private Map<Budget, Set<BudgetEntry>> persistedEntries = new HashMap<Budget, Set<BudgetEntry>>();

    public BudgetDao( Dao<Budget, Long> dao ) {
	super( dao );
	this.entriesDao = PersistenceManager.getInstance().getEntriesDao();
    }

    @Override
    protected void updateChildrenList( Budget entity ) {
	this.updateChildrenList( entity, this.entriesDao, this.persistedEntries, entity.getEntries() );
    }

    @Override
    protected void deleteChildren( Budget entity ) {
	this.deleteChildren( entity, this.entriesDao, this.persistedEntries );
    }
}
