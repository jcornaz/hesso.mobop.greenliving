package com.hesso.greenliving.ui;

import android.content.Context;
import android.view.ViewGroup;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;

public class AccountListAdapter extends EntityListAdapter<Account, AccountView> {

    public AccountListAdapter( Context context ) {
	super( context, R.layout.item_account );
    }

    @Override
    protected AccountView inflateItem( ViewGroup parent ) {
	return AccountView.inflate( parent );
    }
}
