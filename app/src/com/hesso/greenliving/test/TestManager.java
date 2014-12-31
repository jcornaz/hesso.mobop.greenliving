package com.hesso.greenliving.test;

import java.sql.SQLException;

import com.hesso.greenliving.dao.PersistenceManager;
import com.hesso.greenliving.exception.UnexpectedException;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;

// TODO Classe à supprimer dans la version finale
public class TestManager {

    public static void createFakeModelIfNecessary() {
	PersistenceManager persistence = PersistenceManager.getInstance();
	try {
	    if( persistence == null || persistence.getTransactionsDao().queryForAll().isEmpty() ) {

	        Budget budget = Budget.getInstance();

	        Account rent = budget.createEntry( "Rent", 800 );
	        Account meals = budget.createEntry( "Meals", 200 );
	        Account groceries = budget.createEntry( "Groceries", 50 );

	        rent.fill( 800 );
	        rent.expense( 300 );

	        meals.fill( 300 );
	        meals.expense( 50 );

	        meals.transfert( 20, groceries );
	    }
	} catch( SQLException e ) {
	    throw new UnexpectedException( e );
	}
    }
}
