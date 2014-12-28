package com.hesso.greenliving.ui;

import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Budget;

public class AccountFragment extends AbstractFragment implements Observer {

    private AccountListAdapter adapter;
    private MainActivity mainActivity;

    public AccountFragment() {
    }

    public AccountFragment( MainActivity mainActivity ) {
	this();
	this.mainActivity = mainActivity;
    }

    @Override
    public int getNameId() {
	return R.string.fragment_accounts_name;
    }

    @Override
    public int getIconId() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
	Log.d( "debug", "FragmentBudget#onCreateView" );

	ListView res = (ListView) inflater.inflate( R.layout.frag_accounts, container, false );
	this.adapter = new AccountListAdapter( this.mainActivity, container.getContext() );

	res.setAdapter( this.adapter );

	Budget.getInstance().addObserver( this );
	this.update( Budget.getInstance(), this );

	return res;
    }

    @Override
    public void update( Observable observable, Object data ) {
	Log.d( "debug", "FragmentBudget#update" );

	this.adapter.setList( Budget.getInstance().getEntries() );
    }
}
