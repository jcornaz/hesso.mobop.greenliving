package com.hesso.greenliving.dao;

import com.hesso.greenliving.model.Transaction;
import com.j256.ormlite.dao.Dao;

public class TransactionsDao extends EntitiesDao<Transaction> {

    public TransactionsDao( Dao<Transaction, Long> dao ) {
	super( dao );
    }

    @Override
    protected void persistChildren( Transaction entity ) {
	// Les transactions n'ont pas d'enfants
    }

    @Override
    protected void deleteChildren( Transaction entity ) {
	// Les transactions n'ont pas d'enfants
    }

    @Override
    protected void refreshChildren( Transaction entity ) {
	// Les transactions n'ont pas d'enfants
    }

    @Override
    protected void hasBeenRefreshed( Transaction entity ) {
	// Les ScheduledTransaction n'ont pas d'enfants
    }
}
