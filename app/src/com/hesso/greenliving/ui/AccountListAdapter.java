package com.hesso.greenliving.ui;

import java.util.Comparator;

import android.content.Context;
import android.view.ViewGroup;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Account;
import com.hesso.greenliving.model.compare.AccountComparatorByName;

public class AccountListAdapter extends EntityListAdapter<Account, AccountView> {

    private Comparator<Account> comparator = new AccountComparatorByName();

    public AccountListAdapter( Context context ) {
	super( context, R.layout.item_account );
    }

    @Override
    protected AccountView inflateItem( ViewGroup parent ) {
	return AccountView.inflate( parent );
    }

    @Override
    protected Comparator<? super Account> getComparator() {
	return this.comparator;
    }
}
