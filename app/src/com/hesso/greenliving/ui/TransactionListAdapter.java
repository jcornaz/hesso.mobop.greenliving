package com.hesso.greenliving.ui;

import java.util.Comparator;

import android.content.Context;
import android.view.ViewGroup;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Transaction;
import com.hesso.greenliving.model.compare.TransactionComparatorByDate;

public class TransactionListAdapter extends EntityListAdapter<Transaction, TransactionView> {

    private Comparator<Transaction> comparator = new TransactionComparatorByDate();

    public TransactionListAdapter( Context context ) {
	super( context, R.layout.item_transaction );
    }

    @Override
    protected TransactionView inflateItem( ViewGroup parent ) {
	return TransactionView.inflate( parent );
    }

    @Override
    protected Comparator<Transaction> getComparator() {
	return this.comparator;
    }
}
