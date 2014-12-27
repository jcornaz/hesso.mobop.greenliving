package com.hesso.greenliving.test;

import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.BudgetEntry;

public class MockupManager {
    public static void createFakeModel() {
	BudgetEntry rent = Budget.getInstance().createEntry( "Rent", 800 );
	BudgetEntry meals = Budget.getInstance().createEntry( "Meals", 200 );
	BudgetEntry groceries = Budget.getInstance().createEntry( "Groceries", 50 );

	rent.fill( 800 );
	rent.expense( -300 );

	meals.fill( 300 );
	meals.expense( -50 );

	meals.transfert( 20, groceries );
    }
}
