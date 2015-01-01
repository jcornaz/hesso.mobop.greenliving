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

public class AccountFragment extends AbstractFragment implements Observer, OnMenuItemClickListener {

    private AccountListAdapter adapter;

    @Override
    public int getNameId() {
	return R.string.fragment_accounts_name;
    }

    @Override
    public int getIconId() {
	return 0;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
	Log.d( "debug", "FragmentBudget#onCreateView" );

	ListView res = (ListView) inflater.inflate( R.layout.frag_accounts, container, false );
	this.adapter = new AccountListAdapter( container.getContext() );

	res.setAdapter( this.adapter );

	Budget.getInstance().addObserver( this );
	this.update( Budget.getInstance(), this );
	this.setHasOptionsMenu( true );
	return res;
    }

    @Override
    public void onDestroyView() {
	super.onDestroyView();
	Budget.getInstance().deleteObserver( this );
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
	super.onCreateOptionsMenu( menu, inflater );
	inflater.inflate( R.menu.account, menu );
	for( int i = 0; i < menu.size(); i++ ) {
	    menu.getItem( i ).setOnMenuItemClickListener( this );
	}
    }

    @Override
    public void update( Observable observable, Object data ) {
	Log.d( "debug", "FragmentBudget#update" );

	this.adapter.setList( Budget.getInstance().getAccounts() );
    }

    @Override
    public boolean onMenuItemClick( MenuItem item ) {
	switch( item.getItemId() ) {
	case R.id.menu_account_new_account:
	    Intent i = new Intent( MainActivity.getInstance(), DialogAccount.class );
	    startActivity( i );
	    break;
	case R.id.menu_account_new_budget:
	    Intent j = new Intent( MainActivity.getInstance(), DialogBudget.class );
	    startActivity( j );
	    break;
	// Todo : settings and about
	}
	return false;
    }
}
