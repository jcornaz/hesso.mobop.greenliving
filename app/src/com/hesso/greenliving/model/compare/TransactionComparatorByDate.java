package com.hesso.greenliving.model.compare;

import java.util.Comparator;

import com.hesso.greenliving.model.Transaction;

public class TransactionComparatorByDate implements Comparator<Transaction> {

    @Override
    public int compare( Transaction transaction1, Transaction transaction2 ) {
	return transaction2.getDate().compareTo( transaction1.getDate() );
    }
}
