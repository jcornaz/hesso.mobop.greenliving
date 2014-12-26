package com.hesso.greenliving.dao;

import com.hesso.greenliving.model.Transaction;
import com.j256.ormlite.dao.Dao;

public class TransactionsDao extends EntitiesDao<Transaction> {

    public TransactionsDao( Dao<Transaction, Integer> dao ) {
	super( dao );
    }

    @Override
    protected void updateChildrenList( Transaction entity ) {
	// Les transactions n'ont pas d'enfant
    }

    @Override
    protected void deleteChildren( Transaction entity ) {
	// Les transactions n'ont pas d'enfant
    }
}
