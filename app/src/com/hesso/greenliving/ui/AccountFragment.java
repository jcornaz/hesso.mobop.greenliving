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

public class AccountFragment extends AbstractFragment implements Observer, OnMenuItemClickListener, OnItemLongClickListener {

	private AccountListAdapter adapter;
	private MainActivity mainActivity;
	private ListView accountsListView;
	private int itemLongClickPosition;

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

		this.accountsListView = (ListView) inflater.inflate( R.layout.frag_accounts, container, false );
		this.adapter = new AccountListAdapter( this.mainActivity, container.getContext() );

		this.accountsListView.setAdapter( this.adapter );
		this.accountsListView.setOnItemLongClickListener(this);

		Budget.getInstance().addObserver( this );
		this.update( Budget.getInstance(), this );
		this.setHasOptionsMenu(true);
		return this.accountsListView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.account, menu);
		for(int i = 0 ; i < menu.size() ; i++) {
			menu.getItem(i).setOnMenuItemClickListener(this);
		}
	}

	@Override
	public void update( Observable observable, Object data ) {
		Log.d( "debug", "FragmentBudget#update" );

		this.adapter.setList( Budget.getInstance().getEntries() );
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_account_new_account:
			Intent i = new Intent(this.mainActivity, DialogAccount.class);
			startActivity(i);
			break;
		case R.id.menu_account_new_budget:
			Intent j = new Intent(this.mainActivity, DialogBudget.class);
			startActivity(j);
			break;
			//Todo : settings and about
			
		case R.id.menu_account_list_update:
			Account account = (Account)accountsListView.getItemAtPosition(itemLongClickPosition);
			Intent k = new Intent(this.mainActivity, DialogAccount.class);
			k.putExtra("is_update", true);
			k.putExtra("account", account);
			startActivity(k);
			break;
			
		case R.id.menu_account_list_delete:
			((Account)accountsListView.getItemAtPosition(itemLongClickPosition)).delete();
			break;
			
			
		}
		return false;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		itemLongClickPosition = position;
	    PopupMenu popup = new PopupMenu(getActivity(), view);
	    MenuInflater inflater = popup.getMenuInflater();
	    Menu menu = popup.getMenu();
	    inflater.inflate(R.menu.account_list, menu);
	    popup.show();
	    for(int i = 0 ; i < menu.size() ; i++) {
	    	menu.getItem(i).setOnMenuItemClickListener(this);
	    }
	    
		return false;
	}
}
