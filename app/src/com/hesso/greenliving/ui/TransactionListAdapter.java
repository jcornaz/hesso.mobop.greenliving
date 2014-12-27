package com.hesso.greenliving.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Transaction;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

    public TransactionListAdapter( Context context ) {
	super( context, R.layout.item_transaction );
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
