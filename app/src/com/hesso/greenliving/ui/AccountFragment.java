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
	private ListView accountsListView;
	private int itemLongClickPosition;

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

		this.accountsListView = (ListView) inflater.inflate( R.layout.frag_accounts, container, false );
		this.adapter = new AccountListAdapter(container.getContext());
		//this.adapter.setList(Budget.getInstance().getAccounts());
		this.accountsListView.setAdapter(this.adapter);
		this.accountsListView.setOnItemLongClickListener(this);

		Budget.getInstance().addObserver(this);
		this.update(Budget.getInstance(), this);
		this.setHasOptionsMenu(true);
		return this.accountsListView;
	}
	
    @Override
    public void onDestroyView() {
	super.onDestroyView();
	Budget.getInstance().deleteObserver( this );
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
		//this.adapter.notifyDataSetChanged();
		this.adapter.setList(Budget.getInstance().getAccounts());
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case R.id.menu_account_new_account:
			intent = new Intent(getActivity(), DialogAccount.class);
			startActivity(intent);
			break;
		case R.id.menu_account_new_budget:
			intent = new Intent(getActivity(), DialogBudget.class);
			startActivity(intent);
			break;
		case R.id.menu_account_credit_expense:
			intent = new Intent(getActivity(), DialogCreditExpense.class);
			startActivity(intent);
			break;
		case R.id.menu_account_transfer:
			intent = new Intent(getActivity(), DialogTransfer.class);
			startActivity(intent);
			break;
		case R.id.menu_account_settings:
			//Todo : settings
			break;
		case R.id.menu_account_help:
			intent = new Intent(getActivity(), DialogAccountHelp.class);
			startActivity(intent);
			break;
		case R.id.menu_account_about:
			intent = new Intent(getActivity(), DialogAbout.class);
			startActivity(intent);
			break;
		
		case R.id.menu_account_list_credit_expense:
			intent = new Intent(getActivity(), DialogCreditExpense.class);
			intent.putExtra("linked_to_account", true);
			intent.putExtra("account_id", ((Account)accountsListView.getItemAtPosition(itemLongClickPosition)).getId());
			//intent.putExtra("account", (Account)accountsListView.getItemAtPosition(itemLongClickPosition));
			startActivity(intent);
			break;
		case R.id.menu_account_list_update:
			Account account = (Account)accountsListView.getItemAtPosition(itemLongClickPosition);
			intent = new Intent(getActivity(), DialogAccount.class);
			intent.putExtra("is_update", true);
			//intent.putExtra("account", account);
			intent.putExtra("account_id", account.getId());
			startActivity(intent);
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
