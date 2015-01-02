package com.hesso.greenliving.model.compare;

import java.util.Comparator;

import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.OffBudgetAccount;

public class AccountComparatorByName implements Comparator<Account> {

    @Override
    public int compare( Account account1, Account account2 ) {
	int res = 0;

	if( account1 instanceof OffBudgetAccount ) {
	    res = -1;
	} else if( account2 instanceof OffBudgetAccount ) {
	    res = 1;
	} else {
	    res = account1.getName().compareTo( account2.getName() );
	}

	return res;
    }
}
