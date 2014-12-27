package com.hesso.greenliving.ui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Transaction;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

    private Set<Transaction> transactions = new HashSet<Transaction>();

    public TransactionListAdapter( Context context ) {
	super( context, R.layout.item_transaction );
    }

    public void setList( Collection<Transaction> transactions ) {
	Set<Transaction> toDelete = new HashSet<Transaction>( this.transactions );
	for( Transaction transaction : transactions ) {
	    if( !toDelete.remove( transaction ) ) {
		this.add( transaction );
	    }
	}

	this.remove( toDelete );
    }

    private void remove( Collection<Transaction> transactions ) {
	for( Transaction transaction : transactions ) {
	    this.remove( transaction );
	}
    }

    @Override
    public void add( Transaction transaction ) {
	super.add( transaction );
	this.transactions.add( transaction );
    }

    @Override
    public long getItemId( int position ) {
	return this.getItem( position ).getId();
    }

    @Override
    public boolean hasStableIds() {
	return true;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
	TransactionView res;

	if( convertView instanceof TransactionView ) {
	    res = (TransactionView) convertView;
	} else {
	    res = TransactionView.inflate( parent );
	}

	res.setItem( this.getItem( position ) );

	return res;
    }
}
