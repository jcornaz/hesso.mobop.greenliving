package com.hesso.greenliving.test;

import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.BudgetEntry;

// TODO Classe à supprimer dans la version finale
public class TestManager {

    public static void createFakeModel() {

	Budget budget = Budget.getInstance();

	BudgetEntry rent = budget.createEntry( "Rent", 800 );
	BudgetEntry meals = budget.createEntry( "Meals", 200 );
	BudgetEntry groceries = budget.createEntry( "Groceries", 50 );

	rent.fill( 800 );
	rent.expense( 300 );

	meals.fill( 300 );
	meals.expense( 50 );

	meals.transfert( 20, groceries );
    }
}
