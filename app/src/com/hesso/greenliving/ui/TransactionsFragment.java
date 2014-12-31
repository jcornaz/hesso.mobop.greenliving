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
import android.widget.ListView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Budget;
import com.hesso.greenliving.model.Account;

public class TransactionsFragment extends AbstractFragment implements Observer, OnMenuItemClickListener {

    private TransactionListAdapter adapter;
    private MainActivity mainActivity;
    private Account budgetEntry;

    public TransactionsFragment() {
    }

    public TransactionsFragment( MainActivity mainActivity ) {
	this();
	this.mainActivity = mainActivity;
    }

    @Override
    public int getNameId() {
	return R.string.fragment_transactions_name;
    }

    @Override
    public int getIconId() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
	ListView res = (ListView) inflater.inflate( R.layout.frag_transactions, container, false );
	this.adapter = new TransactionListAdapter( this.mainActivity, container.getContext() );
	res.setAdapter( this.adapter );

	Budget.getInstance().addObserver( this );
	this.update( Budget.getInstance(), this );
	this.setHasOptionsMenu(true);
	return res;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	super.onCreateOptionsMenu(menu, inflater);
    	inflater.inflate(R.menu.transaction, menu);
    	for(int i = 0 ; i < menu.size() ; i++) {
    		menu.getItem(i).setOnMenuItemClickListener(this);
    	}
    }
    
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_transaction_credit_expense:
			Intent i = new Intent(this.mainActivity, DialogCreditExpense.class);
			startActivity(i);
			break;
		case R.id.menu_transaction_transfer:
			Intent j = new Intent(this.mainActivity, DialogTransfer.class);
			startActivity(j);
			break;
			//Todo : settings and about
		}
		return false;
	}

    @Override
    public void update( Observable observable, Object data ) {
	Log.d( "debug", String.valueOf( this.budgetEntry ) );
	if( this.budgetEntry != null ) {
	    this.adapter.setList( this.budgetEntry.getTransactions() );
	} else {
	    this.adapter.setList( Budget.getInstance().getTransactions() );
	}
    }

    public void setBudgetEntry( Account budgetEntry ) {
	if( this.budgetEntry != null ) {
	    this.budgetEntry.deleteObserver( this );
	} else {
	    Budget.getInstance().deleteObserver( this );
	}
	this.budgetEntry = budgetEntry;
	if( this.budgetEntry != null ) {
	    this.budgetEntry.addObserver( this );
	    this.update( this.budgetEntry, this );
	} else {
	    Budget.getInstance().addObserver( this );
	    this.update( Budget.getInstance(), this );
	}
    }
}
