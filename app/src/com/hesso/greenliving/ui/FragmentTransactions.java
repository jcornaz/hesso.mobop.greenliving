package com.hesso.greenliving.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hesso.greenliving.R;
import com.hesso.greenliving.model.Budget;

public class FragmentTransactions extends AbstractFragment {

    private TransactionListAdapter adapter;

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
	this.adapter = new TransactionListAdapter( container.getContext() );
	res.setAdapter( this.adapter );
	this.adapter.addAll( Budget.getInstance().getTransactions() );
	return res;
    }
}
