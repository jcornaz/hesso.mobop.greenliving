package com.hesso.greenliving.ui;

import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.Transaction;

public class TransactionsFragment extends AbstractFragment implements Observer, OnMenuItemClickListener, OnItemLongClickListener {

    private TransactionListAdapter adapter;
    private ListView listViewTransaction;
    private int itemLongClickPosition;
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
	listViewTransaction = (ListView) inflater.inflate( R.layout.frag_transactions, container, false );
	this.adapter = new TransactionListAdapter( container.getContext() );
	listViewTransaction.setAdapter( this.adapter );

	Budget.getInstance().addObserver( this );
	this.update( Budget.getInstance(), this );
	this.setHasOptionsMenu( true );
	listViewTransaction.setOnItemLongClickListener( this );
	return listViewTransaction;
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
	Intent intent;
	switch( item.getItemId() ) {
	case R.id.menu_transaction_credit_expense:
	    intent = new Intent( MainActivity.getInstance(), DialogCreditExpense.class );
	    if( account != null ) {
		intent.putExtra( "has_preselected_account", true );
		intent.putExtra( "account_id", account.getId() );
	    }
	    startActivity( intent );
	    break;
	case R.id.menu_transaction_transfer:
	    intent = new Intent( MainActivity.getInstance(), DialogTransfer.class );
	    startActivity( intent );
	    break;
	case R.id.menu_transaction_help:
	    intent = new Intent( getActivity(), DialogTransactionHelp.class );
	    startActivity( intent );
	    break;
	case R.id.menu_transaction_about:
	    intent = new Intent( getActivity(), DialogAbout.class );
	    startActivity( intent );
	    break;
	case R.id.menu_transaction_list_update:
	    // Hard to implement : manage source and destination of transaction,
	    // complicated in case of money transfer
	    break;
	case R.id.menu_transaction_list_delete:
	    ((Transaction) this.listViewTransaction.getItemAtPosition( itemLongClickPosition )).delete();
	    break;
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

    @Override
    public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id ) {
	itemLongClickPosition = position;
	PopupMenu popup = new PopupMenu( getActivity(), view );
	MenuInflater inflater = popup.getMenuInflater();
	Menu menu = popup.getMenu();
	inflater.inflate( R.menu.transaction_list, menu );
	popup.show();
	for( int i = 0; i < menu.size(); i++ ) {
	    menu.getItem( i ).setOnMenuItemClickListener( this );
	}
	return false;
    }
}
