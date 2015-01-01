package com.hesso.greenliving.ui;

import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;

public class TransactionsFragment extends AbstractFragment implements Observer, OnMenuItemClickListener {

    private TransactionListAdapter adapter;
    private Account account;

    @Override
    public int getNameId() {
	return R.string.fragment_transactions_name;
    }

    @Override
    public int getIconId() {
	return 0;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
	ListView res = (ListView) inflater.inflate( R.layout.frag_transactions, container, false );
	this.adapter = new TransactionListAdapter( container.getContext() );
	res.setAdapter( this.adapter );

	Budget.getInstance().addObserver( this );
	this.update( Budget.getInstance(), this );
	this.setHasOptionsMenu( true );
	return res;
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();

	if( this.account != null ) {
	    this.account.deleteObserver( this );
	    this.account = null;
	} else {
	    Budget.getInstance().deleteObserver( this );
	}
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
	super.onCreateOptionsMenu( menu, inflater );
	inflater.inflate( R.menu.transaction, menu );
	for( int i = 0; i < menu.size(); i++ ) {
	    menu.getItem( i ).setOnMenuItemClickListener( this );
	}
    }

    @Override
    public boolean onMenuItemClick( MenuItem item ) {
	switch( item.getItemId() ) {
	case R.id.menu_transaction_credit_expense:
	    Intent i = new Intent( MainActivity.getInstance(), DialogCreditExpense.class );
	    startActivity( i );
	    break;
	case R.id.menu_transaction_transfer:
	    Intent j = new Intent( MainActivity.getInstance(), DialogTransfer.class );
	    startActivity( j );
	    break;
	// Todo : settings and about
	}
	return false;
    }

    @Override
    public void update( Observable observable, Object data ) {
	if( this.account != null ) {
	    this.adapter.setList( this.account.getTransactions() );
	} else {
	    this.adapter.setList( Budget.getInstance().getTransactions() );
	}
    }

    public void setAccount( Account budgetEntry ) {
	if( this.account != null ) {
	    this.account.deleteObserver( this );
	} else {
	    Budget.getInstance().deleteObserver( this );
	}
	this.account = budgetEntry;
	if( this.account != null ) {
	    this.account.addObserver( this );
	    this.update( this.account, this );
	} else {
	    Budget.getInstance().addObserver( this );
	    this.update( Budget.getInstance(), this );
	}
    }
}
