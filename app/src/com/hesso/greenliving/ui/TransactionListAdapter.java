package com.hesso.greenliving.ui;

import android.content.Context;
import android.view.ViewGroup;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Transaction;

public class TransactionListAdapter extends EntityListAdapter<Transaction, TransactionView> {

    public TransactionListAdapter( MainActivity mainActivity, Context context ) {
	super( mainActivity, context, R.layout.item_transaction );
    }

    @Override
    protected TransactionView inflateItem( ViewGroup parent ) {
	return TransactionView.inflate( parent );
    }
}
